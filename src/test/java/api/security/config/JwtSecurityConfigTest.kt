package api.security.config

import api.security.authorize.JwtAuthenticationProvider
import api.security.authorize.JwtAuthenticationTokenFilter
import api.security.authorize.SkipPathAndMethodsRequestMatcher
import api.security.exceptions.ErrorHandler
import api.security.login.JwtLoginProvider
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be instance of`
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.util.ReflectionTestUtils

@RunWith(MockitoJUnitRunner::class)
class JwtSecurityConfigTest {

    @Spy
    @InjectMocks
    lateinit var testSubject: JwtSecurityConfig

    @Mock
    lateinit var authenticationProvider: JwtAuthenticationProvider

    @Mock
    lateinit var userDetailsService: UserDetailsService

    @Mock
    lateinit var errorHandler: ErrorHandler

    @Test
    fun `jwtLoginProvider is configured`(){

        //when
        val result = testSubject.jwtLoginProvider()

        //then
        result `should be instance of` JwtLoginProvider::class
    }

    @Test
    fun `encoder is configured`(){

        //when
        val result = testSubject.encoder()

        //then
        result `should be instance of` BCryptPasswordEncoder::class
    }

    @Test
    fun `authenticationManager is configured`(){

        //when
        val result = testSubject.authenticationManager()

        //then
        verify(testSubject, times(1)).jwtLoginProvider()
        result `should be instance of` ProviderManager::class
        (result as ProviderManager).providers.size `should be equal to` 2
    }

    @Test
    fun `authenticationTokenFilter is configured`(){

        //when
        val result = testSubject.authenticationTokenFilter()
        val matcher = ReflectionTestUtils.getField(result, "requiresAuthenticationRequestMatcher")

        //then
        result `should be instance of` JwtAuthenticationTokenFilter::class
        matcher `should be instance of` SkipPathAndMethodsRequestMatcher::class
    }
}