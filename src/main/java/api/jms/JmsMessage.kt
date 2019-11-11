package api.jms

data class JmsMessage(var to: String, var body: String, var subject: String)