package api.controller

import api.entity.TaskStatus
import venus.utillibrary.repository.api.TaskRepository
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/tasks")
class TaskController(val taskRepository: TaskRepository) {

    //TODO integration test
    @PatchMapping(path = ["/setStatus"])
    fun updateStatuses(@RequestBody wrap: Wrap) {
        taskRepository.updateStatuses(wrap.id, wrap.status, wrap.dateComplete)
    }
}

class Wrap(val id: List<Long>, val status: TaskStatus, val dateComplete: LocalDateTime?)