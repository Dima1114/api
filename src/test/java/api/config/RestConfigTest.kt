package api.config

import api.auditor.AuditAwareImpl
import api.entity.TestEntity
import api.projection.TestProjection
import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.whenever
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.`should have value`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.data.rest.core.config.ProjectionDefinitionConfiguration
import org.springframework.data.rest.core.config.RepositoryRestConfiguration

@RunWith(MockitoJUnitRunner::class)
class RestConfigTest {

    lateinit var testSubject: RestConfig

    @Mock
    lateinit var config: RepositoryRestConfiguration

    @Captor
    private lateinit var moduleCaptor: ArgumentCaptor<Module>

    @Before
    fun setUp(){
        testSubject = RestConfig()
    }

    @Test
    fun `registered repository projections`(){

        //given
        val projectionConfiguration = ProjectionDefinitionConfiguration()
        whenever(config.projectionConfiguration).thenReturn(projectionConfiguration)

        //when
        testSubject.configureRepositoryRestConfiguration(config)
        val projectionsMap = config.projectionConfiguration.getProjectionsFor(TestEntity::class.java)

        //then
        projectionsMap.size `should be equal to` 1
        projectionsMap `should have value` TestProjection::class.java
    }

    @Test
    fun `auditor provider is configured`(){

        //when
        val result = testSubject.auditorProvider()
        //then
        result `should be instance of` AuditAwareImpl::class
    }

    @Test
    fun `registered object mapper module exists`(){

        //given
        val objectMapper = spy(ObjectMapper())

        //when
        testSubject.configureJacksonObjectMapper(objectMapper)

        //then
        Mockito.verify(objectMapper, times(3)).registerModule(moduleCaptor.capture())
        moduleCaptor.allValues[0].moduleName `should be equal to` "Jdk8Module"
        moduleCaptor.allValues[1].moduleName `should be equal to` "jackson-module-kotlin"
        moduleCaptor.allValues[2].moduleName `should be equal to` "LocalDateModule"
    }
}
