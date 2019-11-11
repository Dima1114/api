package api.security.exceptions

import com.fasterxml.jackson.databind.ObjectMapper
import api.security.model.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException
import java.util.Date

@Component
class ErrorHandler : AccessDeniedHandler, AuthenticationFailureHandler, AuthenticationEntryPoint {

    override fun onAuthenticationFailure(request: HttpServletRequest, response: HttpServletResponse, exception: AuthenticationException) {
        handle(response, exception)
    }

    @Throws(IOException::class)
    fun handle(response: HttpServletResponse, e: Exception) {

        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        ObjectMapper().writeValue(response.writer,
                ErrorResponse(HttpStatus.UNAUTHORIZED, e.message))
    }

    @Throws(IOException::class, ServletException::class)
    override fun handle(request: HttpServletRequest, response: HttpServletResponse,
                        accessDeniedException: AccessDeniedException) {
        handle(response, accessDeniedException)
    }

    @Throws(IOException::class, ServletException::class)
    override fun commence(httpServletRequest: HttpServletRequest,
                          httpServletResponse: HttpServletResponse,
                          e: AuthenticationException) {
        handle(httpServletResponse, e)
    }
}
