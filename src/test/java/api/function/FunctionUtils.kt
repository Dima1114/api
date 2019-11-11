package api.function

import org.amshove.kluent.`should be equal to`
import org.junit.Test
import venus.utillibrary.function.takeOrThrow
import java.lang.Exception

class FunctionUtils {

    @Test(expected = Exception::class)
    fun `throwIf must throw exception`() {

        //when
        venus.utillibrary.function.throwIf(true) { Exception() }
    }

    @Test
    fun `throwIf must return Unit`() {

        //when
        venus.utillibrary.function.throwIf(false) { Exception() }
    }

    @Test(expected = Exception::class)
    fun `takeOrThrow must throw exception`() {

        //when
        1.takeOrThrow({ it > 2 }) { Exception() }
    }

    @Test
    fun `takeOrThrow must return value`() {

        //when
        val result = 1.takeOrThrow({ it < 2 }) { Exception() }

        //then
        result `should be equal to` 1
    }
}