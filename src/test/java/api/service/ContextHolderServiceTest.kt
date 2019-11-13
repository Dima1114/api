package api.service

import api.security.model.JwtAuthenticationToken
import api.security.model.JwtUserDetails
import org.amshove.kluent.`should equal`
import org.junit.Before
import org.junit.Test
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import venus.utillibrary.model.base.Role

class ContextHolderServiceTest {

    @Before
    fun setUp() {
        SecurityContextHolder.getContext().authentication = null
    }

    @Test
    fun `should return user from security context`() {

        //given
        val auth = JwtUserDetails().apply {
            setId(1)
            username = "user"
            setAuthorities(mutableSetOf(Role.ROLE_WRITE, Role.ROLE_READ))
        }
        SecurityContextHolder.getContext().authentication = JwtAuthenticationToken(auth, "", mutableSetOf<Role>(), "")

        //when
        val user = getUserFromContext()

        //then
        user!!.username `should equal` auth.username
        user.roles `should equal` auth.authorities
        user.id `should equal` auth.getId()
    }

    @Test
    fun `should return null because context is not UsernamePasswordAuthenticationToken instance`() {

        //given
        SecurityContextHolder.getContext().authentication =
                AnonymousAuthenticationToken("user", "user", listOf(Role.ROLE_ADMIN))

        //when
        val user = getUserFromContext()

        //then
        user `should equal` null
    }

    @Test
    fun `should return null from security context`() {

        //when
        val user = getUserFromContext()

        //then
        user `should equal` null
    }
}