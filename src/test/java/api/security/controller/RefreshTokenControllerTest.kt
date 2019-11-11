package api.security.controller

import api.security.model.JwtUserDetails
import api.security.service.JwtTokenService
import api.service.UserService
import com.nhaarman.mockito_kotlin.doNothing
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
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@RunWith(MockitoJUnitRunner::class)
class RefreshTokenControllerTest {

    @InjectMocks
    lateinit var testSubject: RefreshTokenController

    @Mock
    lateinit var tokenService: JwtTokenService
    @Mock
    lateinit var userService: UserService
    @Mock
    lateinit var userDetailsService: UserDetailsService

    lateinit var mvc: MockMvc

    @Before
    fun setUp() {
        mvc = MockMvcBuilders.standaloneSetup(testSubject).build()

        whenever(tokenService.verifyToken(token)).thenReturn(true)
        whenever(tokenService.getUsernameFromJWT(token)).thenReturn(username)
        whenever(tokenService.generateAccessToken(userDetails)).thenReturn("accessToken")
        whenever(tokenService.generateRefreshToken(userDetails)).thenReturn(token)
        whenever(userService.updateRefreshToken(anyString(), anyString())).thenReturn(1)
    }

    companion object {
        const val token = "refreshToken"
        const val username = "user"
        val userDetails = JwtUserDetails().apply {
            username = "user"
            setRefreshToken("refreshToken")
        }

    }

    @Test
    fun `refreshed successfully`(){

        //given
        whenever(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails)

        //when
        val result =  performRefresh()

        //then
        verify(userService, times(1)).updateRefreshToken(anyString(), anyString())
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("accessToken").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("refreshToken").exists())
    }

    @Test(expected = Exception::class)
    fun `refresh fail`(){

        //given
        whenever(userDetailsService.loadUserByUsername(username))
                .thenReturn(userDetails.apply { setRefreshToken("error") })

        //when
        performRefresh()
    }

    private fun performRefresh() : ResultActions =
            mvc.perform(MockMvcRequestBuilders.post("/auth/token")
                    .header("X-Auth", "Bearer refreshToken")
                    .contentType(MediaType.APPLICATION_JSON))
}