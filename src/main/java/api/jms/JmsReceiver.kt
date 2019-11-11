package api.jms

import api.email.EmailService
import mu.KLogging
import org.springframework.context.annotation.Profile
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicInteger

@Component
@Profile("!test")
class JmsReceiver(val emailService: EmailService) {

    companion object : KLogging()

    val counter = AtomicInteger()

    @JmsListener(destination = "mailbox", containerFactory = "containerFactory")
    fun processMessage(jmsMessage: JmsMessage){
        logger.info("process jmsMessage: $jmsMessage")

        emailService.sendEmail(jmsMessage.to, jmsMessage.body, jmsMessage.subject)
        counter.incrementAndGet()
    }
}
