package api.security.service

import org.springframework.security.core.userdetails.UserDetails
import venus.utillibrary.model.base.Role
import venus.utillibrary.security.JwtUserDetails

interface JwtTokenService {

    fun generateAccessToken(userDetails: UserDetails): String
    fun generateRefreshToken(userDetails: UserDetails): String
    fun getUsernameFromJWT(token: String): String
    fun getExpTimeFromJWT(token: String): Long

    fun getUserRolesFromJWT(token: String): Set<Role>
    fun getUserIdFromJWT(token: String): Long

    fun getUserDetailsFromJWT(token: String): JwtUserDetails

    fun verifyToken(authToken: String): Boolean

    fun generateRegistrationToken(username: String): String
}
