package api.security.controller

import api.handler.getErrors
import api.security.model.ErrorResponse
import api.security.model.RegistrationRequest
import api.security.model.RegistrationResponse
import api.security.service.JwtTokenService
import api.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.*
import venus.utillibrary.security.JwtUserDetails
import javax.validation.Valid

//@RestController
@RequestMapping("/auth/registration")
class RegistrationController(val userService: UserService, val jwtTokenService: JwtTokenService) {

    @PostMapping
    fun register(@Valid @RequestBody registrationRequest: RegistrationRequest, errors: Errors): ResponseEntity<*> {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrors(errors))
        }

        val token = jwtTokenService.generateRegistrationToken(registrationRequest.username)
        userService.registerNewUser(
                registrationRequest.username,
                registrationRequest.password,
                registrationRequest.email,
                token)

        return ResponseEntity.ok(
                RegistrationResponse(
                        registrationRequest.username,
                        registrationRequest.email))
    }

    @GetMapping
    fun registrationConfirm(@RequestParam("token") token: String): ResponseEntity<*> {

        jwtTokenService.verifyToken(token)
        val username = jwtTokenService.getUsernameFromJWT(token)
        val user = userService.findByUsername(username)
                .orElseThrow { UsernameNotFoundException("username : $username does not exist") }

        if (user.refreshToken != token) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorResponse(HttpStatus.BAD_REQUEST, "This link was already used. You can`t use it again"))
        }

        val refreshToken = jwtTokenService.generateRefreshToken(JwtUserDetails.create(user))

        userService.saveUser(
                user.apply {
                    isEnabled = true
                    this.refreshToken = refreshToken
                })

        return ResponseEntity.ok(mapOf("refreshToken" to refreshToken))
    }
}