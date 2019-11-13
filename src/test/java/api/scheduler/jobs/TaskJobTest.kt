package api.scheduler.jobs

import venus.utillibrary.model.api.Task
import api.jms.JmsMessage
import venus.utillibrary.repository.api.TaskRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.quartz.JobExecutionContext
import org.quartz.Scheduler
import org.quartz.Trigger
import org.quartz.TriggerKey
import org.springframework.jms.core.JmsTemplate
import venus.utillibrary.model.base.User
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class TaskJobTest {

    @InjectMocks
    lateinit var testSubject: TaskJob

    @Mock
    lateinit var taskRepository: TaskRepository
    @Mock
    lateinit var jmsTemplate: JmsTemplate
    @Mock
    lateinit var context: JobExecutionContext
    @Mock
    lateinit var scheduler: Scheduler

    companion object {
        val user = User()
        private val task = Task().apply {
            title = "Task"
            userAdded = user
        }
        val tasks = listOf(task)
    }

    @Before
    fun setUp(){
        whenever(taskRepository.findAllByStatusAndDueDateBefore(any(), any())).thenReturn(tasks)

        setUpContext()
    }

    @Test
    fun `execute and reschedule job successfully`(){

        //given
        user.email = "email@test.com"

        //when
        testSubject.execute(context)

        //then
        verify(taskRepository, times(1)).overdueTasks()
        verify(taskRepository, times(1)).findAllByStatusAndDueDateBefore(any(), any())
        verify(context, times(1)).scheduler
        verify(scheduler, times(1)).rescheduleJob(any(), any())
        verify(jmsTemplate, times(1)).convertAndSend(anyString(), org.amshove.kluent.any(JmsMessage::class))
    }

    @Test
    fun `execute and reschedule job successfully but emails was not sent`(){

        //given
        user.email = null

        //when
        testSubject.execute(context)

        //then
        verify(taskRepository, times(1)).overdueTasks()
        verify(taskRepository, times(1)).findAllByStatusAndDueDateBefore(any(), any())
        verify(context, times(1)).scheduler
        verify(scheduler, times(1)).rescheduleJob(any(), any())
        verify(jmsTemplate, times(0)).convertAndSend(anyString(), org.amshove.kluent.any(JmsMessage::class))
    }

    private fun setUpContext(){
        val triggerKey = TriggerKey("name", "group")

        val trigger = Mockito.mock(Trigger::class.java)
        whenever(trigger.key).thenReturn(triggerKey)

        whenever(scheduler.rescheduleJob(any(), any())).thenReturn(Date())

        whenever(context.trigger).thenReturn(trigger)
        whenever(context.scheduler).thenReturn(scheduler)
    }


}