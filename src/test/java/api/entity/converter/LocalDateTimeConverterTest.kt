package api.entity.converter

import org.amshove.kluent.`should be instance of`
import org.junit.Test
import java.sql.Timestamp
import java.time.LocalDateTime

class LocalDateTimeConverterTest {

    private val testSubject: LocalDateTimeConverter = LocalDateTimeConverter()

    @Test
    fun `should convert LocalDateTime to Timestamp`(){

        //when
        val result = testSubject.convertToDatabaseColumn(LocalDateTime.now())

        //then
        result `should be instance of` Timestamp::class
    }

    @Test
    fun `should convert Timestamp to LocalDateTime`(){

        //when
        val result = testSubject.convertToEntityAttribute(Timestamp.valueOf(LocalDateTime.now()))

        //then
        result `should be instance of` LocalDateTime::class
    }
}