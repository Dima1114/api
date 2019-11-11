package api.auditor

import api.security.model.JwtAuthenticationToken
import api.security.model.JwtUserDetails
import org.amshove.kluent.`should be`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.security.core.context.SecurityContextHolder

@RunWith(MockitoJUnitRunner::class)
class AuditAwareTest {

    @InjectMocks
    lateinit var testSubject: AuditAwareImpl

    @Before
    fun setUp() {
        val userDetails = JwtUserDetails().apply { username = "user" }
        SecurityContextHolder.getContext().authentication =
                JwtAuthenticationToken(userDetails, null, null, null)
    }

    @Test
    fun `should return logged user from context`() {

        //when
        val result = testSubject.currentAuditor.get()

        //then
        result.username `should be` "user"
    }
}