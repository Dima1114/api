package api.security.model

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class JwtAuthenticationToken : UsernamePasswordAuthenticationToken {

    var token: String? = null

    constructor(token: String?) : super(null, null) {
        isAuthenticated = false
        this.token = token
    }

    constructor(principal: Any?, credentials: Any?, authorities: Collection<GrantedAuthority>?, token: String?) : super(principal, credentials, authorities) {
        this.token = token
    }
}
