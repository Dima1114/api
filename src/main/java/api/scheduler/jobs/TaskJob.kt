package api.scheduler.jobs

import venus.utillibrary.model.api.Task
import venus.utillibrary.model.api.TaskStatus
import api.jms.JmsMessage
import venus.utillibrary.repository.api.TaskRepository
import api.scheduler.executeTomorrow
import api.scheduler.tomorrow
import mu.KLogging
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jms.core.JmsTemplate
import java.time.LocalDate
import java.time.LocalDateTime

class TaskJob : Job {

    @Autowired
    private lateinit var taskRepository: TaskRepository
    @Autowired
    private lateinit var jmsTemplate: JmsTemplate

    companion object : KLogging()

    override fun execute(context: JobExecutionContext) {

        val triggerKey = context.trigger.key
        logger.info { "EXECUTING JOB $triggerKey AT ${LocalDateTime.now()}" }

        getOverdueTasks().forEach { sendEmail(it) }
        taskRepository.overdueTasks()

        context.scheduler.rescheduleJob(triggerKey, executeTomorrow(triggerKey))
        logger.info { "RESCHEDULED $triggerKey ON ${tomorrow()}" }
    }

    private fun getOverdueTasks() = taskRepository.findAllByStatusAndDueDateBefore(TaskStatus.ACTIVE, LocalDate.now())

    private fun sendEmail(task: Task) =
            task.userAdded?.email?.let {
                jmsTemplate.convertAndSend(
                        "mailbox", JmsMessage(it, "Your Task '${task.title}' is overdue now", "Task overdue notification"))
            }


}