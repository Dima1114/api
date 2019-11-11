package api.security.model

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class RegistrationRequest : LoginRequest() {

    @NotBlank(message = "Email is absent")
    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
            message = "Wrong email format")
    @Size(max = 40, message = "Length must be less than 40")
    var email: String = ""
}