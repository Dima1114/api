package api.security.service.impl

import api.service.UserService
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import org.amshove.kluent.`should equal`
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.security.core.userdetails.UsernameNotFoundException
import venus.utillibrary.model.base.User
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class JwtUserDetailsServiceTest {

    @InjectMocks
    lateinit var testSubject: JwtUserDetailsService

    @Mock
    lateinit var userService: UserService

    @Test
    fun `should return user`() {

        //given
        val user = User().apply { username = "user" }
        whenever(userService.findByUsername(any())).thenReturn(Optional.of(user))

        //when
        val usr = testSubject.loadUserByUsername("user")

        //then
        usr.username `should equal` user.username
    }

    @Test(expected = UsernameNotFoundException::class)
    fun `should throw exception`() {

        //given
        whenever(userService.findByUsername(any())).thenThrow(UsernameNotFoundException("username does not exist"))

        //when
        testSubject.loadUserByUsername("user")
    }

}