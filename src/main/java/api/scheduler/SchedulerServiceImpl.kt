package api.scheduler

import mu.KLogging
import org.quartz.Job
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.stereotype.Service
import kotlin.reflect.KClass

@Service
class SchedulerServiceImpl(val schedulerFactory: SchedulerFactoryBean,
                           val jobsCreator: JobsCreator) : SchedulerService {

    companion object : KLogging()

    override fun startAllSchedulers() {
        logger.info { "Starting schedulers" }
        schedulerFactory.scheduler.start()
        logger.info { "Schedulers started successfully" }
    }

    override fun runJob(group: String, clazz: KClass<out Job>) {
        jobsCreator.createJob(schedulerFactory.scheduler, group, clazz)
    }
}