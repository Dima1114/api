package api.controller

import api.entity.TaskStatus
import venus.utillibrary.repository.api.TaskRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.anyOrNull
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TaskControllerTest {

    @InjectMocks
    lateinit var testSubject: TaskController

    @Mock
    lateinit var taskRepository: TaskRepository

    @Test
    fun `should call repository method`(){

        //when
        testSubject.updateStatuses(Wrap(listOf(), TaskStatus.ACTIVE, null))

        //then
        verify(taskRepository, times(1)).updateStatuses(any(), any(), anyOrNull())
    }
}