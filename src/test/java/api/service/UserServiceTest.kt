package api.service

import api.jms.JmsMessage
import venus.utillibrary.repository.base.UserRepository
import api.service.impl.UserServiceImpl
import com.nhaarman.mockito_kotlin.*
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should equal`
import org.amshove.kluent.`should not equal`
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.jms.core.JmsTemplate
import org.springframework.security.core.userdetails.UsernameNotFoundException
import venus.utillibrary.model.base.User
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class UserServiceTest {

    @InjectMocks
    lateinit var testSubject: UserServiceImpl

    @Mock
    lateinit var userRepository: UserRepository
    @Mock
    lateinit var jmsTemplate: JmsTemplate

    @Test
    fun `should call repository to delete token`() {

        //given
        whenever(userRepository.updateRefreshToken(any(), anyOrNull())).thenReturn(1)

        //when
        testSubject.dropRefreshToken("user")

        //then
        verify(userRepository, times(1)).updateRefreshToken(any(), anyOrNull())
    }

    @Test
    fun `should call repository to update token`() {

        //given
        whenever(userRepository.updateRefreshToken(any(), any())).thenReturn(1)

        //when
        testSubject.updateRefreshToken("user", "token")

        //then
        verify(userRepository, times(1)).updateRefreshToken(any(), any())
    }

    @Test
    fun `should get user by username`() {

        //given
        whenever(userRepository.findByUsername(any())).thenReturn(Optional.of(User().apply { username = "user" }))

        //when
        val result = testSubject.findByUsername("user")

        //then
        result.isPresent `should be equal to` true
        result.get().username `should equal` "user"
        verify(userRepository, times(1)).findByUsername(any())
    }

    @Test
    fun `should create new user and send jms message`() {

        //given
        whenever(userRepository.save(org.amshove.kluent.any(User::class))).thenReturn(User())


        //when
        val result = testSubject.registerNewUser("user", "password", "email", "token")

        //then
        result.username `should equal` "user"
        result.password `should not equal` "password"
        result.email `should equal` "email"
        result.refreshToken `should equal` "token"

        verify(userRepository, times(1)).save(org.amshove.kluent.any(User::class))
        verify(jmsTemplate, times(1)).convertAndSend(anyString(), org.amshove.kluent.any(JmsMessage::class))
    }

    @Test
    fun `should save user`() {

        //given
        val user = User().apply { isEnabled = false; refreshToken = "token"}
        whenever(userRepository.save(org.amshove.kluent.any(User::class))).thenReturn(user)

        //when
        val result = testSubject.saveUser(user)

        //then
        result.isEnabled `should be equal to` false
        result.refreshToken `should equal` "token"
        verify(userRepository, times(1)).save(org.amshove.kluent.any(User::class))
    }


    @Test
    fun `should change user password`() {

        //given
        val user = User().apply { password = "password"; email = "email" }
        val password = user.password
        whenever(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user))

        //when
        testSubject.saveNewPassword("user")

        //then
        user.password `should not equal` password

        verify(jmsTemplate, times(1)).convertAndSend(anyString(), org.amshove.kluent.any(JmsMessage::class))
    }

    @Test(expected = UsernameNotFoundException::class)
    fun `should throw exception because user does not exist`() {

        //given
        whenever(userRepository.findByUsername(anyString())).thenReturn(Optional.empty())

        //when
        testSubject.saveNewPassword("user")
    }
}