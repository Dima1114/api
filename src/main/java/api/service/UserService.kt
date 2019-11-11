package api.service

import venus.utillibrary.model.base.User
import java.util.*

interface UserService {

    fun dropRefreshToken(username: String): Int?

    fun updateRefreshToken(username: String, refreshToken: String): Int?

    fun registerNewUser(username: String, password: String, email: String, token: String) : User

    fun findByUsername(username: String) : Optional<User>

    fun saveNewPassword(username: String)

    fun saveUser(user: User): User
}
