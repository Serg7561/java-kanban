package ru.yandex.javacource.syrovatsky.schedule;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.javacource.syrovatsky.schedule.manger.InMemoryTaskManager;
import ru.yandex.javacource.syrovatsky.schedule.manger.Managers;
import ru.yandex.javacource.syrovatsky.schedule.manger.FileBackedTaskManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    public static final Path path = Path.of("test.csv");
    File file = new File(String.valueOf(path));

    @BeforeEach
    public void beforeEach() {
        manager = new FileBackedTaskManager(Managers.getDefaultHistory(), file);
    }

    @AfterEach
    public void afterEach() {
        try {
            Files.delete(path);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }


        /*@Test
        public void shouldCorrectlySaveAndLoadTasks() {

            // Создаем задачи и эпики
            Task task1 = new Task("Описание1", "Заголовок1", Status.NEW, Instant.now(), 0);
            Task task2 = new Task("Описание2", "Заголовок2", Status.IN_PROGRESS, Instant.now(), 10);
            Epic epic = new Epic("Описание3", "Заголовок3", Status.DONE, Instant.now(), 20);
            Subtask subtask = new Subtask("Описание4", "Заголовок4", Status.NEW, epic.getId(), Instant.now(), 30);

            // Добавляем задачи и эпики в менеджер
            manager.addTask(task1);
            manager.addTask(task2);
            manager.addEpic(epic);
            manager.addSubtask(subtask);

            // Сохраняем задачи в файл
            manager.save();

            // Создаем новый менеджер для загрузки из файла
            FileBackedTaskManager fileManager = new FileBackedTaskManager(new InMemoryHistoryManager(), file);
            fileManager.loadFromFile();

            // Проверяем, что задачи и эпики правильно загружены
            List<Task> loadedTasks = fileManager.getTasks();
            List<Epic> loadedEpics = fileManager.getEpics();
            List<Subtask> loadedSubtasks = fileManager.getSubtasks();

            assertEquals(2, loadedTasks.size());
            assertEquals(1, loadedEpics.size());
            assertEquals(1, loadedSubtasks.size());

            Task loadedTask1 = loadedTasks.get(0);
            Task loadedTask2 = loadedTasks.get(1);
            Epic loadedEpic = loadedEpics.get(0);
            Subtask loadedSubtask = loadedSubtasks.get(0);

            assertEquals(task1.getName(), loadedTask1.getName());
            assertEquals(task1.getDescription(), loadedTask1.getDescription());
            assertEquals(task1.getStatus(), loadedTask1.getStatus());
            assertEquals(task1.getStartTime(), loadedTask1.getStartTime());
            assertEquals(task1.getDuration(), loadedTask1.getDuration());

            assertEquals(task2.getName(), loadedTask2.getName());
            assertEquals(task2.getDescription(), loadedTask2.getDescription());
            assertEquals(task2.getStatus(), loadedTask2.getStatus());
            assertEquals(task2.getStartTime(), loadedTask2.getStartTime());
            assertEquals(task2.getDuration(), loadedTask2.getDuration());

            assertEquals(epic.getName(), loadedEpic.getName());
            assertEquals(epic.getDescription(), loadedEpic.getDescription());
            assertEquals(epic.getStatus(), loadedEpic.getStatus());
            assertEquals(epic.getStartTime(), loadedEpic.getStartTime());
            assertEquals(epic.getDuration(), loadedEpic.getDuration());

            assertEquals(subtask.getName(), loadedSubtask.getName());
            assertEquals(subtask.getDescription(), loadedSubtask.getDescription());
            assertEquals(subtask.getStatus(), loadedSubtask.getStatus());
            assertEquals(subtask.getStartTime(), loadedSubtask.getStartTime());
            assertEquals(subtask.getDuration(), loadedSubtask.getDuration());
            assertEquals(subtask.getEpicId(), loadedSubtask.getEpicId());
        }*/

    @Test
    public void shouldSaveAndLoadEmptyTasksEpicsSubtasks() {
        FileBackedTaskManager fileManager = new FileBackedTaskManager(Managers.getDefaultHistory(), file);
        fileManager.save();
        fileManager.loadFromFile();
        assertEquals(Collections.EMPTY_LIST, manager.getTasks());
        assertEquals(Collections.EMPTY_LIST, manager.getEpics());
        assertEquals(Collections.EMPTY_LIST, manager.getSubtasks());
    }

    @Test
    public void shouldSaveAndLoadEmptyHistory() {
        FileBackedTaskManager fileManager = new FileBackedTaskManager(Managers.getDefaultHistory(), file);
        fileManager.save();
        fileManager.loadFromFile();
        assertEquals(Collections.EMPTY_LIST, manager.getHistory());
    }
}
