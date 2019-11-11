package api.security.config

import api.integration.AbstractTestMvcIntegration
import org.amshove.kluent.`should equal`
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class JwtSettingsTest : AbstractTestMvcIntegration() {

    @Autowired
    lateinit var testSubject: JwtSettings

    @Test
    fun `settings should be taken from properties`(){

        //then
        testSubject.jwtSecret `should equal`  "security"
        testSubject.jwtExpirationInMs `should equal` 3600000
        testSubject.jwtRefreshExpirationInMs `should equal` 432000000
        testSubject.jwtRegistrationExpirationInMs `should equal` 86400000
    }
}