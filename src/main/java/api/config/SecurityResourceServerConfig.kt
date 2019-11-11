package api.config


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
import javax.crypto.Cipher.PRIVATE_KEY

//@Configuration
//@EnableResourceServer
class SecurityResourceServerConfig : ResourceServerConfigurerAdapter() {

    companion object {
        private const val RESOURCE_ID = "resource_id"
        private const val PRIVATE_KEY = "auth_private_key"
    }

    @Bean
    fun tokenEnhancer(): JwtAccessTokenConverter {
        val converter = JwtAccessTokenConverter()
        converter.setSigningKey(PRIVATE_KEY)
        return converter
    }

    @Bean
    fun tokenStore(): JwtTokenStore {
        return JwtTokenStore(tokenEnhancer())
    }

    @Bean
    @Primary
    fun tokenServices(): DefaultTokenServices {
        val defaultTokenServices = DefaultTokenServices()
        defaultTokenServices.setTokenStore(tokenStore())
        defaultTokenServices.setSupportRefreshToken(true)
        return defaultTokenServices
    }

    override fun configure(resources: ResourceServerSecurityConfigurer) {
        resources.resourceId(RESOURCE_ID).stateless(false)
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
