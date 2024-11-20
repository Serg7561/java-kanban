package ru.yandex.javacource.syrovatsky.schedule.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import ru.yandex.javacource.syrovatsky.schedule.manger.TaskManager;
import ru.yandex.javacource.syrovatsky.schedule.manger.Managers;
import ru.yandex.javacource.syrovatsky.schedule.task.Epic;
import ru.yandex.javacource.syrovatsky.schedule.enums.Status;
import ru.yandex.javacource.syrovatsky.schedule.task.Subtask;
import ru.yandex.javacource.syrovatsky.schedule.task.Task;

class InMemoryHistoryManagerTest {
    private static TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    @Test
    public void getHistoryReturnArrayList() {
        for (int i = 0; i < 20; i++) {
            taskManager.addTask(new Task("Имя", "Текст"));
        }

        ArrayList<Task> tasks = taskManager.getTasks();
        for (Task task : tasks) {
            taskManager.getTaskByID(task.getId());
        }

        ArrayList<Task> list = taskManager.getHistory();
        assertEquals(10, list.size(), "Неверное количество элементов в истории ");
    }

    @Test
    public void getHistoryReturnTaskAfterUpdate() {
        Task task = new Task("Выбросить мусор", "Можно вместе с ведром");
        taskManager.addTask(task);
        taskManager.getTaskByID(task.getId());
        taskManager.updateTask(new Task(task.getId(), "Не забыть выбросить мусор",
                "Хотя бы пакет ", Status.IN_PROGRESS));
        ArrayList<Task> tasks = taskManager.getHistory();
        Task oldTask = tasks.getFirst();
        assertEquals(task.getName(), oldTask.getName(), "В истории не сохранилась предыдущая версия задачи");
        assertEquals(task.getDescription(), oldTask.getDescription(),
                "В истории не сохранилась предыдущая версия задачи");

    }

    @Test
    public void getHistoryReturnEpicAfterUpdate() {
        Epic epic = new Epic("Починить машину", "Постараться успеть за выходные");
        taskManager.addEpic(epic);
        taskManager.getEpicByID(epic.getId());
        taskManager.updateEpic(new Epic(epic.getId(), "Новое имя", "Новое описание",
                Status.IN_PROGRESS));
        ArrayList<Task> epics = taskManager.getHistory();
        Epic oldEpic = (Epic) epics.getFirst();
        assertEquals(epic.getName(), oldEpic.getName(),
                "В истории не сохранилась предыдущая версия задачи");
        assertEquals(epic.getDescription(), oldEpic.getDescription(),
                "В истории не сохранилась предыдущая версия задачи");
    }

    @Test
    public void getHistoryReturnSubtaskAfterUpdate() {
        Epic epic = new Epic("Сходить на почту", "Сначала попробовать записаться");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Взять паспорт", "В нем квитанция",
                epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.getSubtaskByID(subtask.getId());
        ArrayList<Task> subtasks = taskManager.getHistory();
        Subtask oldSubtask = (Subtask) subtasks.getFirst();
        assertEquals(subtask.getName(), oldSubtask.getName(),
                "В истории не сохранилась предыдущая версия задачи");
        assertEquals(subtask.getDescription(), oldSubtask.getDescription(),
                "В истории не сохранилась предыдущая версия задачи");
    }
}