package api.config

import api.scheduler.AutoWiringSpringBeanJobFactory
import org.springframework.boot.autoconfigure.quartz.QuartzProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.scheduling.quartz.SpringBeanJobFactory
import java.util.*
import javax.sql.DataSource

@Configuration
@Profile("!test")
class QuartzConfig(val context: ApplicationContext, val dataSource: DataSource, val quartzProperties: QuartzProperties) {

    @Bean
    fun springBeanJobFactory(): SpringBeanJobFactory {
        val jobFactory = AutoWiringSpringBeanJobFactory()
        jobFactory.setApplicationContext(context)
        return jobFactory
    }

    @Bean
    @Primary
    fun scheduler(): SchedulerFactoryBean {
        val properties = Properties()
        properties.putAll(quartzProperties.properties)

        val factory = SchedulerFactoryBean()
        factory.setOverwriteExistingJobs(true)
        factory.setDataSource(dataSource)
        factory.setQuartzProperties(properties)
        factory.setJobFactory(springBeanJobFactory())

        return factory
    }

}