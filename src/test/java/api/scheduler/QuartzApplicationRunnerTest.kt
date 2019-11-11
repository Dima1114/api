package api.scheduler

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class QuartzApplicationRunnerTest {

    @InjectMocks
    lateinit var testSubject: QuartzApplicationRunner

    @Mock
    lateinit var schedulerService: SchedulerService

    @Test
    fun `run all schedulers and add task job`(){

        //when
        testSubject.run(null)

        //then
        verify(schedulerService, times(1)).startAllSchedulers()
        verify(schedulerService, times(1)).runJob(any(), any())
    }
}