package api.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import venus.utillibrary.model.base.Role
import venus.utillibrary.model.base.RoleSecured

@RestController
@RequestMapping("rest/hello")
class HelloController {

    @GetMapping
    @RoleSecured(Role.ROLE_ADMIN)
    fun hello(): ResponseEntity<*> = ResponseEntity("Hello World!", HttpStatus.OK)

    @GetMapping("/user")
    fun helloUser(): String = "Hello, User!"
}
