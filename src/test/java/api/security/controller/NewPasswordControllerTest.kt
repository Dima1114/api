package api.security.controller

import api.service.UserService
import com.nhaarman.mockito_kotlin.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NewPasswordControllerTest {

    @InjectMocks
    lateinit var testSubject: NewPasswordController

    @Mock
    lateinit var userService: UserService

    @Test
    fun `should update user password`(){

        //when
        testSubject.newPassword(User("user"))

        //then
        verify(userService, times(1)).saveNewPassword(anyString())
    }
}