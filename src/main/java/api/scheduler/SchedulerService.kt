package api.scheduler

import org.quartz.Job
import kotlin.reflect.KClass

interface SchedulerService {

    fun startAllSchedulers()

    fun runJob(group: String, clazz: KClass<out Job>)
}