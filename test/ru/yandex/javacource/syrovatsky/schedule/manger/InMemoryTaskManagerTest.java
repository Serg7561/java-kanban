package ru.yandex.javacource.syrovatsky.schedule.manger;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.javacource.syrovatsky.schedule.task.Epic;
import ru.yandex.javacource.syrovatsky.schedule.enums.Status;
import ru.yandex.javacource.syrovatsky.schedule.task.Subtask;
import ru.yandex.javacource.syrovatsky.schedule.task.Task;

import java.util.List;


class InMemoryTaskManagerTest {
    private static TaskManager taskManager;
    private Epic epic;
    private Task task1;
    private Task task2;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
        epic = new Epic("1", "Epic 1");
        task1 = new Task(2, "Task 1");
        task2 = new Task(3, "Task 2");
        epic.addSubtaskId(task1.getId());
        epic.addSubtaskId(task2.getId());
    }

    @Test
    public void testDeleteSubtasks() {
        epic.clearSubtasks();
        List<Integer> subtaskIds = epic.getSubtaskId();
        assertTrue(subtaskIds.isEmpty(),"Содержит не актуальные Id");
    }

    @Test
    void addNewTask() {
        final Task task = taskManager.addTask(new Task("Test addNewTask", "Test addNewTask description"));
        final Task savedTask = taskManager.getTaskByID(task.getId());
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void addNewEpicAndSubtasks() {
        final Epic test = taskManager.addEpic(new Epic("Продать мебель",
                "Самовывозом"));
        final Subtask test1 = taskManager.addSubtask(new Subtask("Придумать объявление",
                "Выложить объявление", test.getId()));
        final Subtask test2 = taskManager.addSubtask(new Subtask("Убрать старую мебель",
                "Не повредить", test.getId()));
        final Subtask test3 = taskManager.addSubtask(new Subtask("Заказать новую мебель", "Проверить размеры",
                test.getId()));
        final Epic savedEpic = taskManager.getEpicByID(test.getId());
        final Subtask savedSubtask1 = taskManager.getSubtaskByID(test1.getId());
        final Subtask savedSubtask2 = taskManager.getSubtaskByID(test2.getId());
        final Subtask savedSubtask3 = taskManager.getSubtaskByID(test3.getId());
        assertNotNull(savedEpic, "Эпик не найден.");
        assertNotNull(savedSubtask2, "Подзадача не найдена.");
        assertEquals(test, savedEpic, "Эпики не совпадают.");
        assertEquals(test1, savedSubtask1, "Подзадачи не совпадают.");
        assertEquals(test3, savedSubtask3, "Подзадачи не совпадают.");

        final List<Epic> epics = taskManager.getEpics();
        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(test, epics.getFirst(), "Эпики не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubtasks();
        assertNotNull(subtasks, "Подзадачи не возвращаются.");
        assertEquals(3, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(savedSubtask1, subtasks.getFirst(), "Подзадачи не совпадают.");
    }

    @Test
    public void updateTask() {
        final Task expected = new Task("имя", "описание");
        taskManager.addTask(expected);
        final Task updatedTask = new Task(expected.getId(), "новое имя", "новое описание", Status.DONE);
        final Task actual = taskManager.updateTask(updatedTask);
        assertEquals(expected.getId(), actual.getId(), "Вернулась задачи с другим id");
    }

    @Test
    public void updateEpic() {
        final Epic expected = new Epic("имя", "описание");
        taskManager.addEpic(expected);
        final Epic updatedEpic = new Epic(expected.getId(), "новое имя", "новое описание", Status.DONE);
        final Epic actual = taskManager.updateEpic(updatedEpic);
        assertEquals(expected.getId(), actual.getId(), "Вернулся эпик с другим id");

    }

    @Test
    public void deleteTasks() {
        taskManager.addTask(new Task("Задача", "Описание"));
        taskManager.addTask(new Task("Задача 2", "Описание 2"));
        taskManager.deleteTasks();
        List<Task> tasks = taskManager.getTasks();
        assertTrue(tasks.isEmpty(), "После удаления задач список должен быть пуст.");
    }

    @Test
    public void deleteEpics() {
        taskManager.addEpic(new Epic("Купить холодильник", "Посмотреть варианты"));
        taskManager.deleteEpics();
        List<Epic> epics = taskManager.getEpics();
        assertTrue(epics.isEmpty(), "После удаления эпиков список должен быть пуст.");
    }

    @Test
    public void deleteSubtasks() {
        Epic flatRenovation = new Epic("Починить машину", "Успеть за выходной");
        taskManager.addEpic(flatRenovation);
        taskManager.addSubtask(new Subtask("Заменить фары", "Узнать как",
                flatRenovation.getId()));
        taskManager.addSubtask(new Subtask("Проверить электрику", "В сервисе",
                flatRenovation.getId()));
        taskManager.addSubtask(new Subtask("Поменять резину ", "Убрать старую в гараж ",
                flatRenovation.getId()));

        taskManager.deleteSubtasks();
        List<Subtask> subtasks = taskManager.getSubtasks();
        assertTrue(subtasks.isEmpty(), "После удаления подзадач список должен быть пуст.");
    }

    @Test
    void deleteTaskById() {
        taskManager.addTask(new Task(1, "Задача", "Описание", Status.NEW));
        taskManager.addTask(new Task(2, "Задача 2", "Описание 2", Status.DONE));
        taskManager.deleteTask(2);
        assertNull(taskManager.deleteTask(2));
    }

    @Test
    public void deleteEpicById() {
        taskManager.addEpic(new Epic(1, "Убрать в квартире", "Помыть окна", Status.IN_PROGRESS));
        taskManager.deleteEpic(1);
        assertNull(taskManager.deleteEpic(1));
    }

    @Test
    public void deleteSubtaskById() {
        Epic test = new Epic("Установить ванну", "Начать завтра");
        taskManager.addEpic(test);
        taskManager.addSubtask(new Subtask("Демонтировать плитку", "Нанять кого то",
                test.getId()));
        taskManager.addSubtask(new Subtask("Положить плитку", "Попробовать самому",
                test.getId()));
        taskManager.addSubtask(new Subtask("Подобрать ванну", "Сделать замеры",
                test.getId()));
        assertNull(taskManager.deleteSubtask(5));
    }


    @Test
    void TaskList() {
        Task expected = new Task(1, "Купить молоко", "Обезжиренное", Status.DONE);
        taskManager.addTask(expected);
        List<Task> list = taskManager.getTasks();
        Task actual = list.getFirst();
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getStatus(), actual.getStatus());
    }

}