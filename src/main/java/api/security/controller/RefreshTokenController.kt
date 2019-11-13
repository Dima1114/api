package api.security.controller

import api.security.model.LoginResponse
import api.security.service.JwtTokenService
import api.security.service.extract
import api.service.UserService
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import venus.utillibrary.security.JwtUserDetails
import venus.utillibrary.security.exceptions.JwtAuthenticationException
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/auth/token")
class RefreshTokenController(private val tokenService: JwtTokenService,
                             private val userDetailsService: UserDetailsService,
                             private val userService: UserService) {

    @PostMapping
    fun refreshToken(request: HttpServletRequest): LoginResponse {

        var refreshToken = extract(request)
        tokenService.verifyToken(refreshToken)
        val username = tokenService.getUsernameFromJWT(refreshToken)
        val userDetails = userDetailsService.loadUserByUsername(username) as JwtUserDetails
        val dataBaseRefreshToken = userDetails.getRefreshToken()

        if (dataBaseRefreshToken != refreshToken) {
            throw JwtAuthenticationException("Invalid refresh Token")
        }

        refreshToken = tokenService.generateRefreshToken(userDetails)
        val accessToken = tokenService.generateAccessToken(userDetails)
        userService.updateRefreshToken(username, refreshToken)

        return LoginResponse(accessToken, refreshToken)
    }
}
