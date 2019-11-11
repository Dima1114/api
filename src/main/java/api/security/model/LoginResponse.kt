package api.security.model

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(content = JsonInclude.Include.NON_NULL)
class LoginResponse(val accessToken: String?,
                    val refreshToken: String?,
                    val username: String?,
                    val expTime: Long) {

    constructor(accessToken: String, refreshToken: String) : this(accessToken, refreshToken, null, 0)
}
