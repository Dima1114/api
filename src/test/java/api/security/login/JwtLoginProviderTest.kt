package api.security.login

import api.security.model.JwtAuthenticationToken
import api.security.model.JwtUserDetails
import com.nhaarman.mockito_kotlin.whenever
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.`should equal`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@RunWith(MockitoJUnitRunner::class)
class JwtLoginProviderTest {

    @InjectMocks
    lateinit var testSubject: JwtLoginProvider

    @Mock
    lateinit var userDetailsService: UserDetailsService
    @Mock
    lateinit var encoder: BCryptPasswordEncoder

    @Before
    fun setUp(){
        userDetails.isEnabled = true
        whenever(userDetailsService.loadUserByUsername("user")).thenReturn(userDetails)
    }

    companion object {
        val userDetails = JwtUserDetails().apply {
            username = "user"
            password = "password"
            isEnabled = true
            setAuthorities(setOf(Role.ROLE_READ))
        }
        val authentication = UsernamePasswordAuthenticationToken("user", "password")
    }

    @Test
    fun `authentication successful`(){

        //given
        whenever(encoder.matches("password", "password")).thenReturn(true)

        //when
        val result = testSubject.authenticate(authentication)

        //then
        result `should be instance of` UsernamePasswordAuthenticationToken::class
        result.principal `should be instance of` JwtUserDetails::class
        (result.principal as JwtUserDetails).username `should equal` "user"
        result.authorities.size `should equal` 1
        result.authorities.contains(Role.ROLE_READ) `should equal` true
    }

    @Test(expected = AuthenticationException::class)
    fun `wrong password`(){

        //given
        whenever(encoder.matches("password", "password")).thenReturn(false)

        //when
        testSubject.authenticate(authentication)
    }

    @Test(expected = AuthenticationException::class)
    fun `user is banned`(){

        //given
        whenever(encoder.matches("password", "password")).thenReturn(true)
        userDetails.isEnabled = false

        //when
        testSubject.authenticate(authentication)
    }

    @Test
    fun `supports test`(){
        //when
        var result = testSubject.supports(UsernamePasswordAuthenticationToken::class.java)
        //then
        result `should equal` true

        //when
        result = testSubject.supports(JwtAuthenticationToken::class.java)
        //then
        result `should equal` false
    }
}