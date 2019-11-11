package api.security.config

import api.config.GlobalMethodSecurityConfig
import api.security.RoleSecured
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.`should contain all`
import org.junit.Test
import org.springframework.security.access.SecurityConfig
import org.springframework.security.access.annotation.SecuredAnnotationSecurityMetadataSource

class GlobalMethodSecurityConfigTest {

    private val testSubject = GlobalMethodSecurityConfig()

    @Test
    fun `Custom method security metadata source is configured`(){
        //when
        val result = testSubject.customMethodSecurityMetadataSource()
        val attributes = result.getAttributes(TestSecuredClass::class.java.getMethod("secured"), TestSecuredClass::class.java)

        //then
        result `should be instance of` SecuredAnnotationSecurityMetadataSource::class
        attributes.size `should be equal to` 1
        attributes `should contain all` listOf(SecurityConfig("ROLE_READ"))
    }

    private class TestSecuredClass {
        @Suppress("unused")
        @RoleSecured(Role.ROLE_READ)
        fun secured() {
        }
    }
}