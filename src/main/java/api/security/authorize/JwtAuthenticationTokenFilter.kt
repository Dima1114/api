package api.security.authorize

import api.security.exceptions.ErrorHandler
import api.security.model.JwtAuthenticationToken
import api.security.service.extract
import mu.KLogging
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.RequestMatcher

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException

class JwtAuthenticationTokenFilter(matcher: RequestMatcher, private val errorHandler: ErrorHandler) : AbstractAuthenticationProcessingFilter(matcher) {

    companion object: KLogging()

    @Throws(AuthenticationException::class, IOException::class, ServletException::class)
    override fun attemptAuthentication(httpServletRequest: HttpServletRequest,
                                       httpServletResponse: HttpServletResponse): Authentication {

        logger.info("REQUESTED URL ==> ${httpServletRequest.requestURL}")
        val token = extract(httpServletRequest)
        return authenticationManager.authenticate(JwtAuthenticationToken(token))
    }

    @Throws(IOException::class, ServletException::class)
    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authResult: Authentication) {
        super.successfulAuthentication(request, response, chain, authResult)
        chain.doFilter(request, response)
    }

    @Throws(IOException::class, ServletException::class)
    override fun unsuccessfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, failed: AuthenticationException) {
        //        super.unsuccessfulAuthentication(request, response, failed);
        errorHandler.handle(response, failed)
    }
}
