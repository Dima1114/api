package api.validation

import api.entity.TestEntity
import api.integration.AbstractTestMvcIntegration
import api.repository.TestEntityRepository
import api.search.operator.SearchOperatorTest
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW
import org.springframework.transaction.support.DefaultTransactionDefinition
import java.time.LocalDate

@FixMethodOrder(MethodSorters.JVM)
class UniqueValidatorTest : AbstractTestMvcIntegration() {

    @Autowired
    lateinit var testEntityRepository: TestEntityRepository
    @Autowired
    lateinit var transactionManager: PlatformTransactionManager

    @Before
    fun setUp() {
        val status = transactionManager.getTransaction(DefaultTransactionDefinition(PROPAGATION_REQUIRES_NEW))
        testEntityRepository.save(TestEntity(name = "test1", date = LocalDate.now(), float = 100F))
        transactionManager.commit(status)
    }

    @After
    fun cleanUp(){
        val status = transactionManager.getTransaction(DefaultTransactionDefinition(PROPAGATION_REQUIRES_NEW))
        testEntityRepository.deleteAll()
        transactionManager.commit(status)
    }

    @Test
    fun `should fail to save entity because of field reject`() {

        //when
        val result = performPost("/testEntities", """{"name": "test1"}""")

        //then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.errors[?(@.field == 'name')]").exists())
                .andExpect(jsonPath("$.errors[?(@.defaultMessage)]").exists())
                .andExpect(jsonPath("$.errors[?(@.entity == 'TestEntity')]").exists())

    }

    @Test
    fun `should fail to save entity because of fields bunch reject`() {

        //when
        val result = performPost("/testEntities", """{"name": "test2", "date":"${LocalDate.now()}", "float":100}""")

        //then
        result.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.errors[?(@.field == 'float')]").exists())
                .andExpect(jsonPath("$.errors[?(@.field == 'date')]").doesNotExist())
                .andExpect(jsonPath("$.errors[?(@.defaultMessage)]").exists())
                .andExpect(jsonPath("$.errors[?(@.entity == 'TestEntity')]").exists())

    }

    private fun performPost(query: String, body: String): ResultActions {
        return mvc.perform(MockMvcRequestBuilders.post(query)
                .content(body)
                .accept(MediaType.APPLICATION_JSON))
    }
}