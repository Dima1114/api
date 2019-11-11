package api.security.authorize

import org.amshove.kluent.`should be equal to`
import org.junit.Test
import org.springframework.mock.web.MockHttpServletRequest

class SkipPathAndMethodsRequestMatcherTest {

    val testSubject = SkipPathAndMethodsRequestMatcher(listOf("/test"), listOf("POST"), "/**")

    @Test
    fun `should skip path`(){

        //given
        val request = MockHttpServletRequest("GET", "").apply { servletPath = "/test" }

        //when
        val result = testSubject.matches(request)

        //then
        result `should be equal to` false
    }

    @Test
    fun `should skip method`(){

        //given
        val request = MockHttpServletRequest("POST", "/hello")

        //when
        val result = testSubject.matches(request)

        //then
        result `should be equal to` false
    }

    @Test
    fun `should apply filter`(){

        //given
        val request = MockHttpServletRequest("GET", "/hello").apply { servletPath = "/rest" }

        //when
        val result = testSubject.matches(request)

        //then
        result `should be equal to` true
    }
}