package api.security.controller

import venus.utillibrary.security.exceptions.JwtAuthenticationException
import api.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("auth/logout")
class LogoutController(private val userService: UserService) {

    @GetMapping
    fun logout(): ResponseEntity<*> {
        val authentication = SecurityContextHolder.getContext().authentication
        val username = authentication.name
        val user = userService.dropRefreshToken(username)
        if (user == 0) {
            throw JwtAuthenticationException("logout error")
        }
        return ResponseEntity(user, HttpStatus.OK)
    }
}
