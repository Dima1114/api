package api.config

import api.scheduler.AutoWiringSpringBeanJobFactory
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.mock
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.boot.autoconfigure.quartz.QuartzProperties
import org.springframework.context.ApplicationContext
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import javax.sql.DataSource

@RunWith(MockitoJUnitRunner::class)
class QuartzConfigTest {

    @InjectMocks
    lateinit var testSubject: QuartzConfig

    @Mock
    lateinit var context: ApplicationContext
    @Mock
    lateinit var dataSource: DataSource
    @Mock
    lateinit var quartzProperties: QuartzProperties

    @Before
    fun setUp(){
        val factory = mock(AutowireCapableBeanFactory::class)
        whenever(context.autowireCapableBeanFactory).thenReturn(factory)
    }

    @Test
    fun `should return AutoWiringSpringBeanJobFactory instance`(){

        //when
        val result = testSubject.springBeanJobFactory()

        //then
        result `should be instance of` AutoWiringSpringBeanJobFactory::class
        verify(context, times(1)).autowireCapableBeanFactory
    }

    @Test
    fun `should configure SchedulerFactoryBean`(){

        //given
        whenever(quartzProperties.properties).thenReturn(mapOf())

        //when
        val result = testSubject.scheduler()

        //then
        result `should be instance of` SchedulerFactoryBean::class
        verify(quartzProperties, times(1)).properties
    }
}