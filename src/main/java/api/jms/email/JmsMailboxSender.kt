package api.jms.email

import api.jms.JmsSender
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component

@Component
class JmsMailboxSender(private val jmsTemplate: JmsTemplate) : JmsSender<JmsMailboxTemplate> {

    override fun sendMessage(template: JmsMailboxTemplate, vararg resources: Any) {

        jmsTemplate.convertAndSend("mailbox", template.getJmsMessage(resources))
    }
}