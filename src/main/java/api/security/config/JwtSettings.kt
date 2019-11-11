package api.security.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class JwtSettings {

    @Value("\${jwt.security.secret}")
    val jwtSecret: String? = null
    @Value("\${jwt.access.expirationInMs}")
    val jwtExpirationInMs: Long = 0
    @Value("\${jwt.refresh.expirationInMs}")
    val jwtRefreshExpirationInMs: Long = 0
    @Value("\${jwt.registration.expirationInMs}")
    val jwtRegistrationExpirationInMs: Long = 0
}
