package api.security.controller

import api.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/forgot")
class NewPasswordController(val userService: UserService){

    @PostMapping
    fun newPassword(@RequestBody user: User): ResponseEntity<Unit> {

        userService.saveNewPassword(user.username)

        return ResponseEntity.ok().build()
    }
}

data class User(val username: String)
