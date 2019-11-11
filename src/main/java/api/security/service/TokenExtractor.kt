package api.security.service

import api.security.config.JwtSecurityConfig
import venus.utillibrary.security.exceptions.JwtAuthenticationException

import javax.servlet.http.HttpServletRequest

private const val PREFIX = "Bearer "

fun extract(request: HttpServletRequest): String {

    val header = request.getHeader(JwtSecurityConfig.TOKEN_HEADER)

    if (header == null || !header.startsWith(PREFIX)) {
        throw JwtAuthenticationException("Token is missing")
    }

    return header.substring(PREFIX.length)
}
