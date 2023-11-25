package sk.streetofcode.webapi.client.wp

import org.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

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

        return fetchGraphql(query, null)
    }

    private fun fetchGraphql(query: String, variables: Any?): String {
        val request = GraphqlRequest(query, variables)

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
        } else {
            try {
                val data = JSONObject(response.body).getJSONObject("data").getJSONObject("post")
                return data.toString()
            } catch (e: Exception) {
                log.info("Fetch graphql was not successful, response: {}", response)
            }
        }

        return ""
    }
}

data class GraphqlRequest(
    val query: String,
    val variables: Any?
)
