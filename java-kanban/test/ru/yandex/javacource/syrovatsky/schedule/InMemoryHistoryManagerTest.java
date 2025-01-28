package ru.yandex.javacource.syrovatsky.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.syrovatsky.schedule.enums.Status;
import ru.yandex.javacource.syrovatsky.schedule.manger.HistoryManager;
import ru.yandex.javacource.syrovatsky.schedule.manger.InMemoryHistoryManager;
import ru.yandex.javacource.syrovatsky.schedule.task.Task;


import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    HistoryManager manager;
    private int id = 0;

    public int generateId() {
        return ++id;
    }

    protected Task createTask() {
        return new Task("Description", "Title", Status.NEW, Instant.now(), 0);
    }

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryHistoryManager();
    }

    @Test
    public void shouldAddTasksToHistory() {
        Task task1 = createTask();
        int newTaskId1 = generateId();
        task1.setId(newTaskId1);
        Task task2 = createTask();
        int newTaskId2 = generateId();
        task2.setId(newTaskId2);
        Task task3 = createTask();
        int newTaskId3 = generateId();
        task3.setId(newTaskId3);
        manager.addHistory(task1);
        manager.addHistory(task2);
        manager.addHistory(task3);
        assertEquals(List.of(task1, task2, task3), manager.getHistory());
    }

    @Test
    public void shouldRemoveTask() {
        Task task1 = createTask();
        int newTaskId1 = generateId();
        task1.setId(newTaskId1);
        Task task2 = createTask();
        int newTaskId2 = generateId();
        task2.setId(newTaskId2);
        Task task3 = createTask();
        int newTaskId3 = generateId();
        task3.setId(newTaskId3);
        manager.addHistory(task1);
        manager.addHistory(task2);
        manager.addHistory(task3);
        manager.remove(task2.getId());
        assertEquals(List.of(task1, task3), manager.getHistory());
    }

    @Test
    public void shouldRemoveOnlyOneTask() {
        Task task = createTask();
        int newTaskId = generateId();
        task.setId(newTaskId);
        manager.addHistory(task);
        manager.remove(task.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }

    @Test
    public void shouldHistoryIsEmpty() {
        Task task1 = createTask();
        int newTaskId1 = generateId();
        task1.setId(newTaskId1);
        Task task2 = createTask();
        int newTaskId2 = generateId();
        task2.setId(newTaskId2);
        Task task3 = createTask();
        int newTaskId3 = generateId();
        task3.setId(newTaskId3);
        manager.remove(task1.getId());
        manager.remove(task2.getId());
        manager.remove(task3.getId());
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }

    @Test
    public void shouldNotRemoveTaskWithBadId() {
        Task task = createTask();
        int newTaskId = generateId();
        task.setId(newTaskId);
        manager.addHistory(task);
        manager.remove(0);
        assertEquals(List.of(task), manager.getHistory());
    }
}