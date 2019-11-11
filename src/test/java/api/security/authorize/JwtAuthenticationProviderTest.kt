package api.security.authorize

import venus.utillibrary.security.exceptions.JwtAuthenticationException
import api.security.model.JwtAuthenticationToken
import api.security.model.JwtUserDetails
import api.security.service.JwtTokenService
import com.nhaarman.mockito_kotlin.whenever
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.`should equal`
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

@RunWith(MockitoJUnitRunner::class)
class JwtAuthenticationProviderTest {

    @InjectMocks
    lateinit var testSubject: JwtAuthenticationProvider

    @Mock
    lateinit var jwtTokenService: JwtTokenService

    companion object {
        const val token = "token"
        val userDetails = JwtUserDetails().apply {
            username = "user"
            setAuthorities(setOf(Role.ROLE_READ))
        }
    }

    @Test
    fun `authentication successful`(){

        //given
        val authentication = JwtAuthenticationToken(token)
        whenever(jwtTokenService.verifyToken(token)).thenReturn(true)
        whenever(jwtTokenService.getUserDetailsFromJWT(token)).thenReturn(userDetails)

        //when
        val result = testSubject.authenticate(authentication)

        //then
        result `should be instance of` JwtAuthenticationToken::class
        result.principal `should be instance of` JwtUserDetails::class
        (result.principal as JwtUserDetails).username `should equal` "user"
        result.authorities.size `should equal` 1
        result.authorities.contains(Role.ROLE_READ) `should equal` true
    }

    @Test(expected = JwtAuthenticationException::class)
    fun `authentication failed token is missing`(){

        //given
        val authentication = JwtAuthenticationToken(null)

        //when
        testSubject.authenticate(authentication)
    }

    @Test(expected = JwtAuthenticationException::class)
    fun `authentication failed invalid token`(){

        //given
        val authentication = JwtAuthenticationToken(token)
        whenever(jwtTokenService.verifyToken(token)).thenThrow(JwtAuthenticationException("token is missing"))

        //when
        testSubject.authenticate(authentication)
    }

    @Test
    fun `supports test`(){

        //when
        var result = testSubject.supports(UsernamePasswordAuthenticationToken::class.java)
        //then
        result `should equal` false

        //when
        result = testSubject.supports(JwtAuthenticationToken::class.java)
        //then
        result `should equal` true
    }
}