package ru.yandex.javacource.syrovatsky.schedule.manger;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import ru.yandex.javacource.syrovatsky.schedule.task.Epic;
import ru.yandex.javacource.syrovatsky.schedule.enums.Status;
import ru.yandex.javacource.syrovatsky.schedule.task.Subtask;
import ru.yandex.javacource.syrovatsky.schedule.task.Task;

class InMemoryHistoryManagerTest {
    private static TaskManager taskManager;
    private HistoryManager historyManager;
    private Epic epic;
    private Task task1;
    private Task task2;
    private Task task3;
    private Task task4;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
        historyManager = new InMemoryHistoryManager();
        epic = new Epic("Name1", "Epic 1");
        task1 = new Task(1, "Task 1");
        task2 = new Task(2, "Task 2");
        task3 = new Task(3, "Task 3");
        task4 = new Task(4, "Task 4");
        epic.addSubtaskId(task3.getId());
        epic.addSubtaskId(task4.getId());
        historyManager.addHistory(task1);
        historyManager.addHistory(task2);
    }

    @Test
    public void testRemoveOldId() {
        historyManager.addHistory(task1);
        historyManager.remove(1);
        historyManager.addHistory(task2);
        assertNotEquals(1, task2.getId(),"Удаленная задача содержит старый id");

    }

    @Test
    public void testAddNewTask() {
        historyManager.addHistory(task1);
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task1, history.get(0), "Добавленная задача не появилась в истории просмотров.");
    }

    @Test
    public void testRemoveTask() {
        historyManager.remove(1);
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task2, history.get(0));
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
        String newName = "Не забыть паспорт";
        String newDescription = "Не забыть талон";
        subtask.setName(newName);
        subtask.setDescription(newDescription);
        taskManager.updateSubtask(subtask);
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