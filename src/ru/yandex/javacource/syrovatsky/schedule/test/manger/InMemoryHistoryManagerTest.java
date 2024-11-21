package ru.yandex.javacource.syrovatsky.schedule.test.manger;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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

        List<Task> tasks = taskManager.getTasks();
        for (Task task : tasks) {
            taskManager.getTaskByID(task.getId());
        }

        List<Task> list = taskManager.getHistory();
        assertEquals(10, list.size(), "Неверное количество элементов в истории ");
    }

    @Test
    public void getHistoryReturnTaskAfterUpdate() {
        Task task = new Task("Выбросить мусор", "Можно вместе с ведром");
        taskManager.addTask(task);
        taskManager.getTaskByID(task.getId());
        taskManager.updateTask(new Task(task.getId(), "Не забыть выбросить мусор",
                "Хотя бы пакет ", Status.IN_PROGRESS));
        List<Task> tasks = taskManager.getHistory();
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
        List<Task> epics = taskManager.getHistory();
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
        Subtask subtask = new Subtask("Взять паспорт", "В нем квитанция", epic.getId());
        taskManager.addSubtask(subtask);
        Subtask initialSubtask = taskManager.getSubtaskByID(subtask.getId());
        String newName = "Take Passport";
        String newDescription = "It has the receipt";
        subtask.setName(newName);
        subtask.setDescription(newDescription);
        taskManager.updateSubtask(subtask); // Assuming you have an updateSubtask method
        List<Task> history = taskManager.getHistory();
        Subtask updatedSubtaskFromHistory = null;
        for (Task task : history) {
            if (task.getId() == subtask.getId()) {
                updatedSubtaskFromHistory = (Subtask) task;
                break;
            }
        }
        assertNotNull(updatedSubtaskFromHistory, "Обновленной подзадачи нет в истории");
        assertEquals(initialSubtask.getName(), updatedSubtaskFromHistory.getName(),
                "В истории должно остаться старое имя подзадачи");
        assertEquals(initialSubtask.getDescription(), updatedSubtaskFromHistory.getDescription(),
                "В истории должно остаться старое описание подзадачи");


    }
}