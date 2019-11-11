package api.security.controller

import api.handler.getErrors
import api.security.model.ErrorResponse
import api.security.model.LoginRequest
import api.security.model.LoginResponse
import api.security.service.JwtTokenService
import api.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/auth/login")
class LoginController(private val jwtTokenService: JwtTokenService,
                      private val userService: UserService,
                      private val authenticationManager: AuthenticationManager) {

    @PostMapping
    fun login(@Valid @RequestBody loginRequest: LoginRequest, errors: Errors): ResponseEntity<*> {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrors(errors))
        }

        val authentication: Authentication
        try {
            authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
            )
        } catch (ex: Exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorResponse(HttpStatus.UNAUTHORIZED, ex.message))
        }

        SecurityContextHolder.getContext().authentication = authentication

        val accessToken = jwtTokenService.generateAccessToken(authentication.principal as UserDetails)
        val refreshToken = jwtTokenService.generateRefreshToken(authentication.principal as UserDetails)

        userService.updateRefreshToken(loginRequest.username, refreshToken)

        return ResponseEntity.ok(
                LoginResponse(
                        accessToken,
                        refreshToken,
                        loginRequest.username,
                        jwtTokenService.getExpTimeFromJWT(accessToken)))
    }
}
