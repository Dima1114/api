package api.security.controller

import venus.utillibrary.model.api.User
import api.security.service.JwtTokenService
import api.service.UserService
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class RegistrationControllerTest {

    @InjectMocks
    lateinit var testSubject: RegistrationController

    @Mock
    lateinit var jwtTokenService: JwtTokenService
    @Mock
    lateinit var userService: UserService

    lateinit var mvc: MockMvc

    @Before
    fun setUp() {
        mvc = MockMvcBuilders.standaloneSetup(testSubject).build()

        whenever(jwtTokenService.generateRegistrationToken(any())).thenReturn("token")
        whenever(jwtTokenService.generateRefreshToken(any())).thenReturn("token")
        whenever(jwtTokenService.getUsernameFromJWT(any())).thenReturn("username")
        whenever(jwtTokenService.verifyToken(any())).thenReturn(true)
        whenever(userService.registerNewUser(any(), any(), any(), any())).thenReturn(User())
        whenever(userService.saveUser(any())).thenReturn(User())
        whenever(userService.findByUsername(any())).thenReturn(Optional.of(User().apply { refreshToken = "token" }))
    }

    @Test
    fun `request should fail because of validation errors`() {

        //when
        val result = performPostRequest("""{"username":"user","password":"qwerty","email":""}""")

        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.errors").exists())
    }

    @Test
    fun `request should create new user`() {

        //when
        val result = performPostRequest("""{"username":"user","password":"qwerty","email":"email@goofle.com"}""")

        //then
        result
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.errors").doesNotExist())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.email").exists())

        verify(jwtTokenService, times(1)).generateRegistrationToken(any())
        verify(userService, times(1)).registerNewUser(any(), any(), any(), any())
    }

    @Test(expected = Exception::class)
    fun `request should fail because user not found`() {

        //given
        whenever(userService.findByUsername(any())).thenReturn(Optional.empty())

        //when
        performGetRequest("token")
    }

    @Test
    fun `request should fail because link already used`() {

        //given
        whenever(userService.findByUsername(any())).thenReturn(Optional.of(User().apply { refreshToken = "tokenUsed" }))

        //when
        val result = performGetRequest("token")

        //then
        result
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.message").exists())
    }

    @Test
    fun `request should complete registration`() {

        //when
        val result = performGetRequest("token")

        //then
        result.andExpect(status().isOk)
                .andExpect(jsonPath("$.refreshToken").exists())

        verify(jwtTokenService, times(1)).verifyToken(any())
        verify(jwtTokenService, times(1)).generateRefreshToken(any())
        verify(jwtTokenService, times(1)).getUsernameFromJWT(any())
        verify(userService, times(1)).findByUsername(any())
        verify(userService, times(1)).saveUser(any())
    }

    private fun performPostRequest(body: String): ResultActions =
            mvc.perform(MockMvcRequestBuilders.post("/auth/registration")
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON))

    private fun performGetRequest(token: String): ResultActions =
            mvc.perform(MockMvcRequestBuilders.get("/auth/registration?token=$token")
                    .contentType(MediaType.APPLICATION_JSON))
}