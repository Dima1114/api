package api.jms

import api.email.EmailService
import com.nhaarman.mockito_kotlin.*
import org.amshove.kluent.`should be equal to`
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class JmsReceiverTest {

    @InjectMocks
    lateinit var testSubject : JmsReceiver

    @Mock
    lateinit var emailService: EmailService

    @Test
    fun `should process received message`(){

        //given
        val email = JmsMessage("to", "message", "subject")
        doNothing().whenever(emailService).sendEmail(any(), any(), any())
        testSubject.counter.get() `should be equal to` 0

        //when
        testSubject.processMessage(email)

        //then
        testSubject.counter.get() `should be equal to` 1
        verify(emailService, times(1)).sendEmail(any(), any(), any())
    }
}