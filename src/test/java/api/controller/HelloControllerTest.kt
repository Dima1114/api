package api.controller

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles("test")
class HelloControllerTest {

    @Autowired
    private lateinit var context: WebApplicationContext

    protected lateinit var mvc: MockMvc

    @Before
    fun setup() {

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
//                .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
                .build()
    }

    @Test
    @WithMockUser(username = "john")
    fun `test hello controller`(){

        val result = mvc.perform(get("/rest/hello").accept(MediaType.APPLICATION_JSON))

        result
                .andDo(print())
                .andExpect(status().isOk)
    }
}