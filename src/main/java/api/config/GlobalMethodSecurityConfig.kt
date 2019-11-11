package api.config

import api.security.RoleSecured
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.SecurityConfig
import org.springframework.security.access.annotation.AnnotationMetadataExtractor
import org.springframework.security.access.annotation.SecuredAnnotationSecurityMetadataSource
import org.springframework.security.access.method.MethodSecurityMetadataSource
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class GlobalMethodSecurityConfig : GlobalMethodSecurityConfiguration() {

    public override fun customMethodSecurityMetadataSource(): MethodSecurityMetadataSource {
        return SecuredAnnotationSecurityMetadataSource(RoleSecuredAnnotationMetadataExtractor())
    }

    private inner class RoleSecuredAnnotationMetadataExtractor : AnnotationMetadataExtractor<RoleSecured> {
        override fun extractAttributes(roleSecured: RoleSecured) =
                roleSecured.value.map { it.authority }.map(::SecurityConfig)
    }
}
