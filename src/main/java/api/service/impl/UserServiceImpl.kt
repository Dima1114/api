package api.service.impl

import api.jms.email.JmsMailboxSender
import api.jms.email.JmsMailboxTemplate
import venus.utillibrary.repository.base.UserRepository
import api.service.UserService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import venus.utillibrary.model.base.Role
import venus.utillibrary.model.base.User
import java.util.*

@Service
class UserServiceImpl(private val userRepository: UserRepository, private val jmsMailboxSender: JmsMailboxSender) : UserService {

    override fun dropRefreshToken(username: String): Int? = userRepository.updateRefreshToken(username, null)

    override fun updateRefreshToken(username: String, refreshToken: String): Int? =
            userRepository.updateRefreshToken(username, refreshToken)

    override fun saveUser(user: User): User = userRepository.save(user)

    override fun findByUsername(username: String): Optional<User> = userRepository.findByUsername(username)

    override fun registerNewUser(username: String, password: String, email: String, token: String): User {

        val user = User().apply {
            this.username = username
            this.password = password
            this.email = email
            isEnabled = false
            roles = mutableSetOf(Role.ROLE_USER)

            refreshToken = token
        }

        userRepository.save(user)
        jmsMailboxSender.sendMessage(JmsMailboxTemplate.NEW_USER, user)

        return user
    }

    override fun saveNewPassword(username: String) {

        val user = findByUsername(username)
                .orElseThrow { UsernameNotFoundException("username : $username does not exist") }

        val password = UUID.randomUUID().toString().substring(0, 10)
        user.password = password

        userRepository.save(user)
        jmsMailboxSender.sendMessage(JmsMailboxTemplate.PASSWORD_CHANGED, user, password)
    }


}
