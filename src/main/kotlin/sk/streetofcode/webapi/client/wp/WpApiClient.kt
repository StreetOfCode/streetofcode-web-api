package sk.streetofcode.webapi.client.wp

import org.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import sk.streetofcode.webapi.api.exception.InternalErrorException
import sk.streetofcode.webapi.api.exception.ResourceNotFoundException

@Service
class WpApiClient(
    @Qualifier("wpGraphqlRestTemplate") private val restTemplate: RestTemplate,
) {
    companion object {
        private val log = LoggerFactory.getLogger(WpApiClient::class.java)

        private val FULL_POST_SCHEMA_BASE = """
            id
            title
            excerpt
            slug
            date
            content
            featuredImage {
              node {
                sourceUrl
              }
            }
            author {
              node {
                name
                firstName
                lastName
                avatar {
                  url
                }
              }
            }
            tags {
              nodes {
                id
                name
              }
            }
        """.trimIndent()
    }

    fun getPostBySlug(slug: String): String {
        val query = """
            {
              post(id: "$slug", idType: SLUG) {
                $FULL_POST_SCHEMA_BASE
              }
            }
        """

        return fetchPost(query, slug)
    }

    private fun fetchPost(query: String, slug: String): String {
        val request = GraphqlRequest(query, null)

        val jsonObject = JSONObject()
        jsonObject.put("variables", request.variables)
        jsonObject.put("query", request.query)

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val entity = org.springframework.http.HttpEntity(jsonObject.toString(), headers)

        val response = restTemplate.postForEntity(
            "/graphql",
            entity,
            String::class.java,
        )

        if (!response.statusCode.is2xxSuccessful) {
            log.info("Fetch graphql was not successful, response: {}", response)
            throw InternalErrorException("Error fetching graphql data")
        } else {
            try {
                val data = JSONObject(response.body).getJSONObject("data").get("post").toString()
                if (data != "null") {
                    return data
                } else {
                    throw ResourceNotFoundException("Post with $slug not found")
                }
            } catch (e: ResourceNotFoundException) {
                throw e
            } catch (e: Exception) {
                throw InternalErrorException("Error fetching graphql data")
            }
        }
    }
}

data class GraphqlRequest(
    val query: String,
    val variables: Any?
)
