package api.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.config.JmsListenerContainerFactory
import org.springframework.jms.support.converter.MappingJackson2MessageConverter
import org.springframework.jms.support.converter.MessageConverter
import org.springframework.jms.support.converter.MessageType
import venus.utillibrary.json.LocalDateModule
import javax.jms.ConnectionFactory

@Configuration
@EnableJms
class CommonConfig {

    @Bean
    fun containerFactory(connectionFactory: ConnectionFactory,
                         configurer: DefaultJmsListenerContainerFactoryConfigurer): JmsListenerContainerFactory<*> {
        val factory = DefaultJmsListenerContainerFactory()
        configurer.configure(factory, connectionFactory)
        return factory
    }

    @Bean
    fun jacksonJmsMessageConverter(): MessageConverter =
            MappingJackson2MessageConverter().apply {
                setTargetType(MessageType.TEXT)
                setTypeIdPropertyName("_type")
                setObjectMapper(objectMapper())
            }

    @Bean
    fun objectMapper(): ObjectMapper =
            ObjectMapper().apply {
                registerModule(Jdk8Module())
                registerModule(KotlinModule())
                registerModule(LocalDateModule())
            }
}