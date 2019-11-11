package api.auditor

import api.service.getUserFromContext
import org.springframework.data.domain.AuditorAware
import venus.utillibrary.model.base.User
import java.util.*

open class AuditAwareImpl : AuditorAware<User> {

    override fun getCurrentAuditor(): Optional<User> {

        val user = getUserFromContext()
        return Optional.ofNullable(user)
    }
}