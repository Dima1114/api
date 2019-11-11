package api.config

import api.auditor.AuditAwareImpl

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.reflections.Reflections
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean
import org.springframework.data.rest.core.config.Projection
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer
import venus.utillibrary.json.LocalDateModule
import venus.utillibrary.model.base.User

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = ["venus.utillibrary.repository.api", "venus.utillibrary.repository.base"],
        repositoryFactoryBeanClass = JpaRepositoryFactoryBean::class)
@EntityScan( basePackages = ["venus.utillibrary.model.api", "venus.utillibrary.model.base"] )
class RestConfig : RepositoryRestConfigurer {

    override fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration) {
        registerRepositoryProjections(config, "venus/utillibrary/projection")
    }

    private fun registerRepositoryProjections(config: RepositoryRestConfiguration, vararg packages: String) {
        packages.forEach { packageName ->
            val reflections = Reflections(packageName)
            reflections.getTypesAnnotatedWith(Projection::class.java)
                    .forEach { projection -> config.projectionConfiguration.addProjection(projection) }
        }
    }

    override fun configureJacksonObjectMapper(objectMapper: ObjectMapper) {
        objectMapper.apply {
            registerModule(Jdk8Module())
            registerModule(KotlinModule())
            registerModule(LocalDateModule())
        }
    }

    @Bean
    fun auditorProvider(): AuditorAware<User> = AuditAwareImpl()
}
