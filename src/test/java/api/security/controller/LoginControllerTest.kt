package api.security.controller

import api.security.model.JwtUserDetails
import api.security.service.JwtTokenService
import api.service.UserService
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import venus.utillibrary.model.base.Role

@RunWith(MockitoJUnitRunner::class)
class LoginControllerTest {

    @InjectMocks
    lateinit var testSubject: LoginController

    @Mock
    lateinit var jwtTokenService: JwtTokenService
    @Mock
    lateinit var userService: UserService
    @Mock
    lateinit var authenticationManager: AuthenticationManager

    lateinit var mvc: MockMvc

    companion object {

        val userDetails = JwtUserDetails().apply {
            username = "user"
            setAuthorities(setOf(Role.ROLE_READ))
        }
        val authentication = UsernamePasswordAuthenticationToken(userDetails, "password", userDetails.authorities)
    }

    @Before
    fun setUp() {
        mvc = MockMvcBuilders.standaloneSetup(testSubject).build()

        whenever(jwtTokenService.generateAccessToken(userDetails)).thenReturn("accessToken")
        whenever(jwtTokenService.generateRefreshToken(userDetails)).thenReturn("refreshToken")
        whenever(jwtTokenService.getExpTimeFromJWT("accessToken")).thenReturn(10000)
        whenever(userService.updateRefreshToken(anyString(), anyString())).thenReturn(1)
    }

    @Test
    fun `should return new jwt token in response`() {

        //given
        whenever(authenticationManager.authenticate(any())).thenReturn(authentication)

        //when
        val result = performLoginRequest()

        //then
        verify(userService, times(1)).updateRefreshToken("user", "refreshToken")
        result
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"))
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.expTime").value(10000))
    }

    @Test
    fun `should return error message`() {

        //given
        whenever(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException("wrong password"))

        //when
        val result = performLoginRequest()

        //then
        result
                .andExpect(status().isUnauthorized)
                .andExpect(jsonPath("$.message").value("wrong password"))
    }

    private fun performLoginRequest() : ResultActions =
            mvc.perform(post("/auth/login")
                .content("""{"username":"user","password":"qwerty"}""")
                .contentType(MediaType.APPLICATION_JSON))
}