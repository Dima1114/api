package api.scheduler

import api.scheduler.jobs.TaskJob
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.amshove.kluent.any
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.quartz.JobKey
import org.quartz.Scheduler
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class JobsCreatorTest {

    var testSubject = JobsCreatorImpl()

    @Mock
    lateinit var scheduler: Scheduler

    @Test
    fun `should create new job`(){

        //given
        whenever(scheduler.checkExists(any(JobKey::class))).thenReturn(false)
        whenever(scheduler.scheduleJob(any(), any())).thenReturn(Date())

        //when
        testSubject.createJob(scheduler, "test", TaskJob::class)

        //then
        verify(scheduler, times(1)).scheduleJob(any(), any())
    }

    @Test
    fun `should not new job because it exists`(){

        //given
        whenever(scheduler.checkExists(any(JobKey::class))).thenReturn(true)

        //when
        testSubject.createJob(scheduler, "test", TaskJob::class)

        //then
        verify(scheduler, times(0)).scheduleJob(any(), any())
    }
}