package api.security.authorize

import venus.utillibrary.security.exceptions.JwtAuthenticationException
import api.security.model.JwtAuthenticationToken
import api.security.service.JwtTokenService
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationProvider(private val jwtTokenService: JwtTokenService) : AuthenticationProvider {

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication {
        val jwtAuthenticationToken = authentication as JwtAuthenticationToken

        val token = jwtAuthenticationToken.token ?: throw JwtAuthenticationException("token is missing")
        jwtTokenService.verifyToken(token)

        val userDetails = jwtTokenService.getUserDetailsFromJWT(token)

        return JwtAuthenticationToken(userDetails, null, userDetails.authorities, token)
    }

    override fun supports(aClass: Class<*>): Boolean {
        return JwtAuthenticationToken::class.java.isAssignableFrom(aClass)
    }
}
