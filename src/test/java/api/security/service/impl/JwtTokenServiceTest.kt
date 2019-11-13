package api.security.service.impl

import venus.utillibrary.repository.base.UserRepository
import api.security.config.JwtSettings
import venus.utillibrary.security.exceptions.JwtAuthenticationException
import api.security.model.JwtUserDetails
import com.nhaarman.mockito_kotlin.whenever
import org.amshove.kluent.*
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.util.ResourceUtils
import venus.utillibrary.model.base.Role
import venus.utillibrary.model.base.User


@RunWith(MockitoJUnitRunner::class)
class JwtTokenServiceTest {

    @InjectMocks
    lateinit var testSubject: JwtTokenServiceImpl

    @Mock
    lateinit var userRepository: UserRepository
    @Mock
    lateinit var jwtSettings: JwtSettings

    @get:Rule
    var thrown = ExpectedException.none()

    @Before
    fun setUp() {
        whenever(jwtSettings.jwtExpirationInMs).thenReturn(10000)
        whenever(jwtSettings.jwtRefreshExpirationInMs).thenReturn(20000)
        whenever(jwtSettings.jwtRegistrationExpirationInMs).thenReturn(20000)
        whenever(jwtSettings.jwtSecret).thenReturn("secret")

        user.refreshToken = null
    }

    companion object {
        val user = User().apply {
            id = 1
            username = "user"
            password = "password"
            email = "email"
            roles = mutableSetOf(Role.ROLE_READ, Role.ROLE_WRITE)
            refreshToken = null
        }

        val userDetails = JwtUserDetails.create(user)
    }

    @Test
    fun `should fail verification cause token expired`() {

        //given
        val token = loadToken("expired")

        //when
        thrown.expect(JwtAuthenticationException::class.java)
        thrown.expectMessage(Matchers.containsString("JWT expired"))
        testSubject.verifyToken(token)
    }

    @Test
    fun `should fail verification cause token invalid signature`() {

        //given
        val token = loadToken("invalid-signature")

        //when
        thrown.expect(JwtAuthenticationException::class.java)
        thrown.expectMessage(Matchers.containsString("JWT signature does not match locally computed signature"))
        testSubject.verifyToken(token)
    }

    @Test
    fun `should generate a valid access token`() {

        //when
        val token = testSubject.generateAccessToken(userDetails)

        //then
        testSubject.verifyToken(token) `should be equal to` true

    }

    @Test
    fun `should generate a valid refresh token`() {

        //when
        val token = testSubject.generateRefreshToken(userDetails)

        //then
        testSubject.verifyToken(token) `should be equal to` true

    }

    @Test
    fun `should return username from token`() {

        //given
        val token = testSubject.generateAccessToken(userDetails)

        //when
        val username = testSubject.getUsernameFromJWT(token)

        //then
        username `should be equal to` user.username!!

    }

    @Test
    fun `should return expiration time from token`() {

        //given
        val token = testSubject.generateAccessToken(userDetails)

        //when
        val time = testSubject.getExpTimeFromJWT(token)

        //then
        time `should be greater than` 0

    }

    @Test
    fun `should return roles from token`() {

        //given
        val token = testSubject.generateAccessToken(userDetails)

        //when
        val roles = testSubject.getUserRolesFromJWT(token)

        //then
        roles.size `should be equal to` 2
        roles `should contain` Role.ROLE_READ
        roles `should contain` Role.ROLE_WRITE

    }

    @Test
    fun `should return user id from token`() {

        //given
        val token = testSubject.generateAccessToken(userDetails)

        //when
        val id = testSubject.getUserIdFromJWT(token)

        //then
        id `should be equal to` 1

    }

    @Test
    fun `should return user details from token`() {

        //given
        val token = testSubject.generateAccessToken(userDetails)

        //when
        val details: JwtUserDetails = testSubject.getUserDetailsFromJWT(token)

        //then
        details.getId() `should equal` userDetails.getId()
        details.username `should equal` userDetails.username
        details.authorities `should equal` userDetails.authorities

    }

    @Test
    fun `should generate a valid registration token`() {

        //when
        val token = testSubject.generateRegistrationToken(userDetails.username!!)

        //then
        testSubject.verifyToken(token) `should be equal to` true

    }

    private fun loadToken(name: String): String =
            ResourceUtils.getFile("classpath:tokens/$name.txt")
                    .readText(Charsets.UTF_8)
}