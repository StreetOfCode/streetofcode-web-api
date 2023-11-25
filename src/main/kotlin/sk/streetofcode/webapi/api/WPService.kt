package sk.streetofcode.webapi.api

interface WPService {
    fun getPostBySlug(slug: String, revalidate: Boolean): String
}
