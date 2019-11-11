package api.security.service

import venus.utillibrary.security.exceptions.JwtAuthenticationException
import org.amshove.kluent.`should be equal to`
import org.junit.Test
import org.springframework.mock.web.MockHttpServletRequest

class TokenExtractorTest {

    @Test
    fun `token is extracted from header`(){

        //given
        val request = MockHttpServletRequest()
        request.addHeader("X-Auth", "Bearer token")

        //when
        val result = extract(request)

        //then
        result `should be equal to` "token"
    }

    @Test(expected = JwtAuthenticationException::class)
    fun `header is missing`(){

        //given
        val request = MockHttpServletRequest()

        //when
        extract(request)
    }

    @Test(expected = JwtAuthenticationException::class)
    fun `token is missing`(){

        //given
        val request = MockHttpServletRequest()
        request.addHeader("X-Auth", "NOT_Bearer token")

        //when
        extract(request)
    }
}