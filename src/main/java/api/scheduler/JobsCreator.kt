package api.scheduler

import org.quartz.Job
import org.quartz.Scheduler
import org.quartz.SchedulerException
import kotlin.reflect.KClass

interface JobsCreator {

    @Throws(SchedulerException::class)
    fun createJob(scheduler: Scheduler, group: String,
                           clazz: KClass<out Job>)
}