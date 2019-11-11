package api.security.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import venus.utillibrary.model.base.Role
import venus.utillibrary.model.base.User

class JwtUserDetails : UserDetails {

    private var id: Long? = null
    private var username: String? = null
    private var password: String? = null
    private var isEnabled: Boolean = false
    private var roles: Set<Role> = mutableSetOf()
    private var refreshToken: String? = null

    override fun getAuthorities(): Collection<GrantedAuthority> = roles

    override fun getPassword(): String? = password

    override fun getUsername(): String? = username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = isEnabled

    fun getId(): Long? = id

    fun setId(id: Long?): JwtUserDetails {
        this.id = id
        return this
    }

    fun setUsername(username: String?): JwtUserDetails {
        this.username = username
        return this
    }

    fun setPassword(password: String?): JwtUserDetails {
        this.password = password
        return this
    }

    fun setEnabled(enabled: Boolean): JwtUserDetails {
        isEnabled = enabled
        return this
    }

    fun setAuthorities(roles: Set<Role>): JwtUserDetails {
        this.roles = roles
        return this
    }

    fun getRefreshToken(): String? = refreshToken

    fun setRefreshToken(refreshToken: String?): JwtUserDetails {
        this.refreshToken = refreshToken
        return this
    }

    companion object {

        fun create(user: User): JwtUserDetails {
            return JwtUserDetails()
                    .setAuthorities(user.roles)
                    .setEnabled(user.isEnabled)
                    .setId(user.id)
                    .setUsername(user.username)
                    .setPassword(user.password)
                    .setRefreshToken(user.refreshToken)
        }
    }
}

fun JwtUserDetails.getUser(): User {
    return User().apply {
        id = getId()
        username = getUsername()
        isEnabled = isEnabled()
        roles = authorities.map { it as Role }.toMutableSet()
    }
}
