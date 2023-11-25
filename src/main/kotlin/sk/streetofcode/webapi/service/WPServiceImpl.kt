package sk.streetofcode.webapi.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import sk.streetofcode.webapi.api.WPService
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException
import sk.streetofcode.webapi.client.wp.WpApiClient
import sk.streetofcode.webapi.db.repository.WpPostRepository
import sk.streetofcode.webapi.model.WpPost

@Service
class WPServiceImpl(
    private val apiClient: WpApiClient,
    private val wpPostRepository: WpPostRepository
) : WPService {

    companion object {
        private val log = LoggerFactory.getLogger(WPServiceImpl::class.java)
    }

    // cache map of slug to string which represents post
    private val postsBySlug = mutableMapOf<String, String>()

    /**
     * Get post by slug.
     * If it's not in cache, try to find it in database.
     * If it's not in database, get it from API and save it to database.
     * @param slug slug of post
     * @param revalidate if true, get post from API and save it to database and cache
     * @return post as string
     */
    override fun getPostBySlug(slug: String, revalidate: Boolean): String {
        if (revalidate) {
            val updatedPost = apiClient.getPostBySlug(slug)
            if (updatedPost.isEmpty()) {
                throw ResourceNotFoundException("Post with slug $slug not found")
            }

            postsBySlug[slug] = updatedPost
            wpPostRepository.save(WpPost(slug, updatedPost))
            log.info("Post with slug $slug updated in cache and database.")
            return updatedPost
        }

        if (postsBySlug.containsKey(slug)) {
            return postsBySlug[slug]!!
        } else {
            wpPostRepository.findBySlug(slug)?.let {
                postsBySlug[slug] = it.post
                return it.post
            } ?: run {
                val post = apiClient.getPostBySlug(slug)
                if (post.isEmpty()) {
                    throw ResourceNotFoundException("Post with slug $slug not found")
                }
                postsBySlug[slug] = post
                wpPostRepository.save(WpPost(slug, post))
                return post
            }
        }
    }
}
