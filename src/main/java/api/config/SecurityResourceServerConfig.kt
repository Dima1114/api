package api.config


import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
import javax.validation.constraints.NotNull

@Configuration
@EnableResourceServer
class SecurityResourceServerConfig : ResourceServerConfigurerAdapter() {

    @Value("\${jwt.security.resourceId}")
    @NotNull
    lateinit var resourceId: String

    companion object {
        private const val PRIVATE_KEY = "auth_private_key"
    }

    @Bean
    fun tokenEnhancer(): JwtAccessTokenConverter {
        return JwtAccessTokenConverter().apply {
            setSigningKey(PRIVATE_KEY)
        }
    }

    @Bean
    fun tokenStore(): JwtTokenStore {
        return JwtTokenStore(tokenEnhancer())
    }

    @Bean
    @Primary
    fun tokenServices(): DefaultTokenServices {
        return DefaultTokenServices().apply {
            setTokenStore(tokenStore())
            setSupportRefreshToken(true)
        }
    }

    override fun configure(resources: ResourceServerSecurityConfigurer) {
        resources.resourceId(resourceId)
//                .stateless(false)
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
                .anonymous().disable()
                .authorizeRequests().antMatchers("/**").authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().accessDeniedHandler(OAuth2AccessDeniedHandler())
    }


}
