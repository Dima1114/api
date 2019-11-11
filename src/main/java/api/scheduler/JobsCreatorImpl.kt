package api.scheduler

import mu.KLogging
import org.quartz.*
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
class JobsCreatorImpl : JobsCreator {

    companion object : KLogging()

    override fun createJob(scheduler: Scheduler, group: String, clazz: KClass<out Job>) {

        if (!scheduler.checkExists(JobKey.jobKey(group, group))) {
            logger.info("Creating a job $group")
            val jobDetail = JobBuilder
                    .newJob(clazz.java)
                    .withIdentity(group, group)
                    .build()

            val trigger = TriggerBuilder.newTrigger()
                    .withIdentity(group, group)
                    .startNow()
                    .build()

            scheduler.scheduleJob(jobDetail, trigger)
        } else {
            logger.info("Creating job $group failed. Already exists")
        }
    }
}