package api

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ApiApplication

fun main(args: Array<String>) {
    SpringApplication.run(ApiApplication::class.java, *args)

//    val jmsTemplate = context.getBean(JmsTemplate::class.java)
//
//    jmsTemplate.convertAndSend("mailbox",
//            JmsMessage("umetsky.dmitry@gmail.com", "Ватафак мазафака, Application Venus has been running since ${LocalDateTime.now()}"))
//
//    val schedulerService = context.getBean(SchedulerService::class.java)
//
//    schedulerService.startAllSchedulers()
//    schedulerService.runJob("TEST_GROUP", TaskJob::class.java)
}



