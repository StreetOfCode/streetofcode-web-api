package sk.streetofcode.courseplatformbackend.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import sk.streetofcode.courseplatformbackend.configuration.properties.SwaggerProperties
import springfox.documentation.RequestHandler
import springfox.documentation.builders.*
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.GrantType
import springfox.documentation.service.SecurityReference
import springfox.documentation.service.SecurityScheme
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.SecurityConfiguration
import springfox.documentation.swagger.web.SecurityConfigurationBuilder
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.function.Consumer

@Configuration
@EnableSwagger2
class SwaggerConfig(val swaggerProperties: SwaggerProperties) {
    companion object {
        const val AUTHORIZATION_NAME = "Cognito"
    }

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis { input: RequestHandler? -> input!!.groupName() != "basic-error-controller" }
            .paths(PathSelectors.any())
            .build()
            .securitySchemes(listOf(securityScheme()))
            .securityContexts(listOf(securityContext()))
    }

    @Bean
    fun security(): SecurityConfiguration? {
        return SecurityConfigurationBuilder.builder()
            .clientId(swaggerProperties.clientId)
            .clientSecret(swaggerProperties.clientSecret)
            .useBasicAuthenticationWithAccessCodeGrant(true)
            .build()
    }

    private fun securityScheme(): SecurityScheme? {
        return OAuthBuilder().name(AUTHORIZATION_NAME)
            .grantTypes(grantTypes())
            .scopes(scopes())
            .build()
    }

    private fun grantTypes(): List<GrantType> {
        val tokenRequestConsumer =
            Consumer<TokenRequestEndpointBuilder> {
                tokenRequestEndpointBuilder ->
                tokenRequestEndpointBuilder.url(swaggerProperties.authorizeUri)
            }

        val tokenEndpointConsumer =
            Consumer<TokenEndpointBuilder> { tokenEndpointBuilder ->
                tokenEndpointBuilder.url(swaggerProperties.tokenUri)
                tokenEndpointBuilder.tokenName(AUTHORIZATION_NAME)
            }

        val authorizationGrant = AuthorizationCodeGrantBuilder()
            .tokenEndpoint(tokenEndpointConsumer)
            .tokenRequestEndpoint(tokenRequestConsumer)
            .build()

        return listOf(authorizationGrant)
    }

    private fun scopes(): List<AuthorizationScope> {
        return listOf()
    }

    private fun securityContext(): SecurityContext? {
        return SecurityContext.builder()
            .securityReferences(cognitoAuth())
            .build()
    }

    fun cognitoAuth(): List<SecurityReference?>? {
        return listOf(SecurityReference(AUTHORIZATION_NAME, scopes().toTypedArray()))
    }
}
