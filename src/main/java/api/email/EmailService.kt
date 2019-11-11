package api.email

import mu.KLogging
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailService(val javaMailSender: JavaMailSender) {

    companion object : KLogging()

    fun sendEmail(to: String, text: String, subject: String){

        val message = javaMailSender.createMimeMessage()
        MimeMessageHelper(message, true).apply {
            setFrom("Venus", "Venus")
            setTo(to)
            setSubject(subject)
            setText(text, true)
        }

        try {
            javaMailSender.send(message)
            logger.info("email was sent to: $to")
        }catch (e: Exception){
            logger.error(e.message, e)
        }

    }
}