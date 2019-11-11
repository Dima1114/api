package api.security.controller

import venus.utillibrary.security.exceptions.JwtAuthenticationException
import api.security.model.JwtAuthenticationToken
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
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@RunWith(MockitoJUnitRunner::class)
class LogoutControllerTest {

    @InjectMocks
    lateinit var testSubject: LogoutController

    @Mock
    lateinit var userService: UserService

    private lateinit var mvc: MockMvc

    @Before
    fun setUp() {
        mvc = MockMvcBuilders.standaloneSetup(testSubject).build()

        SecurityContextHolder.getContext().authentication = JwtAuthenticationToken("token")
    }

    @Test
    fun `logout successfully`(){

        //given
        whenever(userService.dropRefreshToken(any())).thenReturn(1)

        //when
        val result =  performLogout()

        //then
        verify(userService, times(1)).dropRefreshToken(any())
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(1))
    }

    @Test(expected = Exception::class)
    fun `logout failed`(){

        //given
        whenever(userService.dropRefreshToken(any())).thenThrow(JwtAuthenticationException("fail"))

        //when
        performLogout()
    }

    private fun performLogout() : ResultActions =
            mvc.perform(MockMvcRequestBuilders.get("/auth/logout")
            .contentType(MediaType.APPLICATION_JSON))
}