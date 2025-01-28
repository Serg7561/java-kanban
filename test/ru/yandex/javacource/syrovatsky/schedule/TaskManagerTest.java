package ru.yandex.javacource.syrovatsky.schedule;

import org.junit.jupiter.api.Test;
import ru.yandex.javacource.syrovatsky.schedule.manger.TaskManager;
import ru.yandex.javacource.syrovatsky.schedule.task.Epic;
import ru.yandex.javacource.syrovatsky.schedule.enums.Status;
import ru.yandex.javacource.syrovatsky.schedule.task.Subtask;
import ru.yandex.javacource.syrovatsky.schedule.task.Task;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;

    protected Task createTask() {
        return new Task("Description", "Title", Status.NEW, Instant.now(), 0);
    }

    protected Epic createEpic() {

        return new Epic("Description", "Title", Status.NEW, Instant.now(), 0);
    }

    protected Subtask createSubtask(Epic epic) {
        return new Subtask("Description", "Title", Status.NEW, epic.getId(), Instant.now(), 0);
    }

    @Test
    public void shouldCreateTask() {
        Task task = createTask();
        manager.addTask(task);
        List<Task> tasks = manager.getTasks();
        assertNotNull(task.getStatus());
        assertEquals(Status.NEW, task.getStatus());
        assertEquals(List.of(task), tasks);
    }

    @Test
    public void shouldCreateEpic() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        List<Epic> epics = manager.getEpics();
        assertNotNull(epic.getStatus());
        assertEquals(Status.NEW, epic.getStatus());
        assertEquals(Collections.EMPTY_LIST, epic.getSubtaskId());
        assertEquals(List.of(epic), epics);
    }

    @Test
    public void shouldCreateSubtask() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        Subtask subtask = createSubtask(epic);
        manager.addSubtask(subtask);
        List<Subtask> subtasks = manager.getSubtasks();
        assertNotNull(subtask.getStatus());
        assertEquals(epic.getId(), subtask.getEpicId());
        assertEquals(Status.NEW, subtask.getStatus());
        assertEquals(List.of(subtask), subtasks);
        assertEquals(List.of(subtask.getId()), epic.getSubtaskId());
    }

    @Test
    void shouldReturnNullWhenCreateTaskNull() {
        Task task = manager.addTask(null);
        assertNull(task);
    }

    @Test
    void shouldReturnNullWhenCreateEpicNull() {
        Epic epic = manager.addEpic(null);
        assertNull(epic);
    }

    @Test
    void shouldReturnNullWhenCreateSubtaskNull() {
        Subtask subtask = manager.addSubtask(null);
        assertNull(subtask);
    }

    @Test
    public void shouldUpdateTaskStatusToInProgress() {
        Task task = createTask();
        manager.addTask(task);
        task.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task);
        assertEquals(Status.IN_PROGRESS, manager.getTaskByID(task.getId()).getStatus());
    }

    @Test
    public void shouldUpdateEpicStatusToInProgress() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        epic.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, manager.getEpicByID(epic.getId()).getStatus());
    }

    @Test
    public void shouldUpdateSubtaskStatusToInProgress() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        Subtask subtask = createSubtask(epic);
        manager.addSubtask(subtask);
        subtask.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask);
        assertEquals(Status.IN_PROGRESS, manager.getSubtaskByID(subtask.getId()).getStatus());
        assertEquals(Status.IN_PROGRESS, manager.getEpicByID(epic.getId()).getStatus());
    }

    @Test
    public void shouldUpdateTaskStatusToInDone() {
        Task task = createTask();
        manager.addTask(task);
        task.setStatus(Status.DONE);
        manager.updateTask(task);
        assertEquals(Status.DONE, manager.getTaskByID(task.getId()).getStatus());
    }

    @Test
    public void shouldUpdateEpicStatusToInDone() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        epic.setStatus(Status.DONE);
        assertEquals(Status.DONE, manager.getEpicByID(epic.getId()).getStatus());
    }

    @Test
    public void shouldUpdateSubtaskStatusToInDone() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        Subtask subtask = createSubtask(epic);
        manager.addSubtask(subtask);
        subtask.setStatus(Status.DONE);
        manager.updateSubtask(subtask);
        assertEquals(Status.DONE, manager.getSubtaskByID(subtask.getId()).getStatus());
        assertEquals(Status.DONE, manager.getEpicByID(epic.getId()).getStatus());
    }

    @Test
    public void shouldNotUpdateTaskIfNull() {
        Task task = createTask();
        manager.addTask(task);
        manager.updateTask(null);
        assertEquals(task, manager.getTaskByID(task.getId()));
    }

    @Test
    public void shouldNotUpdateEpicIfNull() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        manager.updateEpic(null);
        assertEquals(epic, manager.getEpicByID(epic.getId()));
    }

    @Test
    public void shouldNotUpdateSubtaskIfNull() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        Subtask subtask = createSubtask(epic);
        manager.addSubtask(subtask);
        manager.updateSubtask(null);
        assertEquals(subtask, manager.getSubtaskByID(subtask.getId()));
    }

    @Test
    public void shouldDeleteAllTasks() {
        Task task = createTask();
        manager.addTask(task);
        manager.deleteAllTasks();
        assertEquals(Collections.EMPTY_LIST, manager.getTasks());
    }

    @Test
    public void shouldDeleteAllEpics() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        manager.deleteAllEpics();
        assertEquals(Collections.EMPTY_LIST, manager.getEpics());
    }

    @Test
    public void shouldDeleteAllSubtasks() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        Subtask subtask = createSubtask(epic);
        manager.addSubtask(subtask);
        manager.deleteAllSubtasks();
        assertTrue(epic.getSubtaskId().isEmpty());
        assertTrue(manager.getSubtasks().isEmpty());
    }


    @Test
    public void shouldDeleteTaskById() {
        Task task = createTask();
        manager.addTask(task);
        manager.deleteTask(task.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getTasks());
    }

    @Test
    public void shouldDeleteEpicById() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        manager.deleteEpic(epic.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getEpics());
    }

    @Test
    public void shouldNotDeleteTaskIfBadId() {
        Task task = createTask();
        manager.addTask(task);
        manager.deleteTask(999);
        assertEquals(List.of(task), manager.getTasks());
    }

    @Test
    public void shouldNotDeleteEpicIfBadId() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        manager.deleteEpic(999);
        assertEquals(List.of(epic), manager.getEpics());
    }

    @Test
    public void shouldNotDeleteSubtaskIfBadId() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        Subtask subtask = createSubtask(epic);
        manager.addSubtask(subtask);
        manager.deleteSubtask(999);
        assertEquals(List.of(subtask), manager.getSubtasks());
        assertEquals(List.of(subtask.getId()), manager.getEpicByID(epic.getId()).getSubtaskId());
    }

    @Test
    public void shouldDoNothingIfTaskHashMapIsEmpty() {
        manager.deleteAllTasks();
        manager.deleteTask(999);
        assertEquals(0, manager.getTasks().size());
    }

    @Test
    public void shouldDoNothingIfEpicHashMapIsEmpty() {
        manager.deleteAllEpics();
        manager.deleteEpic(999);
        assertTrue(manager.getEpics().isEmpty());
    }

    @Test
    public void shouldDoNothingIfSubtaskHashMapIsEmpty() {
        manager.deleteAllEpics();
        manager.deleteSubtask(999);
        assertEquals(0, manager.getSubtasks().size());
    }

    @Test
    void shouldReturnEmptyListWhenGetSubtaskByEpicIdIsEmpty() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        List<Subtask> subtasks = manager.getEpicSubtasks(epic.getId());
        assertTrue(subtasks.isEmpty());
    }

    @Test
    public void shouldReturnEmptyListTasksIfNoTasks() {
        assertTrue(manager.getTasks().isEmpty());
    }

    @Test
    public void shouldReturnEmptyListEpicsIfNoEpics() {
        assertTrue(manager.getEpics().isEmpty());
    }

    @Test
    public void shouldReturnEmptyListSubtasksIfNoSubtasks() {
        assertTrue(manager.getSubtasks().isEmpty());
    }

    @Test
    public void shouldReturnNullIfTaskDoesNotExist() {
        assertNull(manager.getTaskByID(999));
    }

    @Test
    public void shouldReturnNullIfEpicDoesNotExist() {
        assertNull(manager.getEpicByID(999));
    }

    @Test
    public void shouldReturnNullIfSubtaskDoesNotExist() {
        assertNull(manager.getSubtaskByID(999));
    }

    @Test
    public void shouldReturnEmptyHistory() {
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }

    @Test
    public void shouldReturnEmptyHistoryIfTasksNotExist() {
        manager.getTaskByID(999);
        manager.getSubtaskByID(999);
        manager.getEpicByID(999);
        assertTrue(manager.getHistory().isEmpty());
    }

    @Test
    public void shouldReturnHistoryWithTasks() {
        Epic epic = createEpic();
        manager.addEpic(epic);
        Subtask subtask = createSubtask(epic);
        manager.addSubtask(subtask);
        manager.getEpicByID(epic.getId());
        manager.getSubtaskByID(subtask.getId());
        List<Task> list = manager.getHistory();
        assertEquals(2, list.size());
        assertTrue(list.contains(subtask));
        assertTrue(list.contains(epic));
    }

}
