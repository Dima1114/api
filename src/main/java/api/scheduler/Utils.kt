package api.scheduler

import org.quartz.DateBuilder
import org.quartz.DateBuilder.futureDate
import org.quartz.Trigger
import org.quartz.TriggerBuilder
import org.quartz.TriggerKey
import java.util.*

fun executeInMinutes(triggerKey: TriggerKey, interval: Int): Trigger {
    return TriggerBuilder.newTrigger()
            .withIdentity(triggerKey)
            .startAt(futureDate(interval, DateBuilder.IntervalUnit.MINUTE))
            .build()
}

fun executeTomorrow(triggerKey: TriggerKey): Trigger {
    return TriggerBuilder.newTrigger()
            .withIdentity(triggerKey)
            .startAt(tomorrow())
            .build()
}

fun tomorrow() : Date = DateBuilder.tomorrowAt(0, 0, 0)