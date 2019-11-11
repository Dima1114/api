package api.scheduler

import api.scheduler.jobs.TaskJob
import mu.KLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("!test")
class QuartzApplicationRunner(val schedulerService: SchedulerService) : ApplicationRunner {

    companion object : KLogging()

    override fun run(args: ApplicationArguments?) {

        logger.info { "Schedule all new scheduler jobs at app startup - starting" }
        try {
            schedulerService.startAllSchedulers()
            schedulerService.runJob("TEST_GROUP", TaskJob::class)
            logger.info { "Schedule all new scheduler jobs at app startup - complete" }
        } catch (e: Exception) {
            logger.error("Schedule all new scheduler jobs at app startup - error", e)
        }


    }
}