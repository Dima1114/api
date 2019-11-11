package api.jms.email

import api.jms.JmsMessage
import venus.utillibrary.model.base.User

enum class JmsMailboxTemplate {
    NEW_USER {
        override fun getJmsMessage(vararg resources: Any): JmsMessage {
            val user = resources[0] as User
            return JmsMessage(user.email!!,
                    "You have been successfully signed up at Venus ToDoList service.<br>" +
                            "To complete registration follow this link<br> " +
                            "<a href='http://localhost:3000/registration/complete?token=${user.refreshToken}'>" +
                            "Click here to Complete Registration</a>",
                    "Registration")
        }
    },
    PASSWORD_CHANGED {
        override fun getJmsMessage(vararg resources: Any): JmsMessage {
            val user = resources[0] as User
            val password = resources[1] as String
            return JmsMessage(user.email!!,
                    "Your password has been changed.<br> New password: $password",
                    "Change Password")
        }
    };

    abstract fun getJmsMessage(vararg resources: Any): JmsMessage
}