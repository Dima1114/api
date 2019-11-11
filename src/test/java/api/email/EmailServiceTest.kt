package api.email

import com.nhaarman.mockito_kotlin.doNothing
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.mail.javamail.JavaMailSender
import javax.mail.internet.MimeMessage

@RunWith(MockitoJUnitRunner::class)
class EmailServiceTest {

    @InjectMocks
    lateinit var testSubject: EmailService

    @Mock
    lateinit var javaMailSender: JavaMailSender

    @Mock
    lateinit var message: MimeMessage

    @Test
    fun `should send email`(){

        //given
        doNothing().whenever(javaMailSender).send(message)
        whenever(javaMailSender.createMimeMessage()).thenReturn(message)

        //when
        testSubject.sendEmail("from", "to", "subject")

        //then
        verify(javaMailSender, times(1)).send(message)
    }

}