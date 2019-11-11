package api.security.model

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

open class LoginRequest {

    @NotBlank(message = "Username is absent")
    @Size(max = 40, min = 4, message = "Length must be between 4 and 40")
    var username: String = ""

    @NotBlank(message = "Password is absent")
    @Size(max = 40, min = 6, message = "Length must be between 6 and 40")
    var password: String = ""
}
