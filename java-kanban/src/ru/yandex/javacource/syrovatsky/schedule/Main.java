package ru.yandex.javacource.syrovatsky.schedule;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

import ru.yandex.javacource.syrovatsky.schedule.manger.Managers;
import ru.yandex.javacource.syrovatsky.schedule.task.Epic;
import ru.yandex.javacource.syrovatsky.schedule.enums.Status;
import ru.yandex.javacource.syrovatsky.schedule.task.Subtask;
import ru.yandex.javacource.syrovatsky.schedule.task.Task;
import ru.yandex.javacource.syrovatsky.schedule.manger.FileBackedTaskManager;

public class Main {

    public static void main(String[] args) {

        Path path = Paths.get("Save_Task.csv");
        File file = path.toFile();
        FileBackedTaskManager inMemoryTaskManager = new FileBackedTaskManager(Managers.getDefaultHistory(), file);

        Task awayTrash = new Task("Выбросить мусор", "И старое ведро", Status.NEW);
        Task awayTrashCreated = inMemoryTaskManager.addTask(awayTrash);
        System.out.println(awayTrashCreated);

        Task slep = new Task("Поспать", "Хотя бы пару часов", Status.NEW);
        Task slepCreated = inMemoryTaskManager.addTask(slep);
        System.out.println(slepCreated);

        Task awayTrashToUpdate = new Task("Не забыть выбросить мусор", "Хотя бы пакет",
                Status.IN_PROGRESS);
        awayTrashToUpdate.setId(awayTrash.getId());
        Task awayTrashUpdated = inMemoryTaskManager.updateTask(awayTrashToUpdate);
        System.out.println(awayTrashUpdated);

        Epic test = new Epic("Провести тесты", "Нужно успеть за вечер", Status.NEW, Instant.now(),
                120);
        inMemoryTaskManager.addEpic(test);
        System.out.println(test);
        Subtask testSubtask = new Subtask("Все тесты", "Хотя бы основные", Status.NEW, test.getId(),
                Instant.now(), 60);
        inMemoryTaskManager.addSubtask(testSubtask);
        System.out.println(testSubtask);

        Epic repairCar = new Epic("Починить машину", "Нужно успеть на выходных", Status.NEW,
                Instant.now(), 240);
        inMemoryTaskManager.addEpic(repairCar);
        System.out.println(repairCar);
        Subtask repairCarSubtask1 = new Subtask("Заменить фары", "Обязательно оригинал!", Status.NEW,
                repairCar.getId(), Instant.now(), 30);
        Subtask repairCarSubtask2 = new Subtask("Заменить резину", "Старую по возможности продать",
                Status.NEW, repairCar.getId(), Instant.now(), 45);
        inMemoryTaskManager.addSubtask(repairCarSubtask1);
        inMemoryTaskManager.addSubtask(repairCarSubtask2);
        System.out.println(repairCar);
        repairCarSubtask2.setStatus(Status.DONE);
        inMemoryTaskManager.updateSubtask(repairCarSubtask2);

        System.out.println(" ");
        System.out.println("Выводим все задачи:");
        System.out.println(" "); //Вывод задач
        for (Task task : inMemoryTaskManager.getTasks()) {
            System.out.println(task);
        }
        System.out.println(" "); //Вывод эпиков
        for (Epic epic : inMemoryTaskManager.getEpics()) {
            System.out.println(epic);
            for (Task task : inMemoryTaskManager.getEpicSubtasks(epic.getId())) { //Вывод подзадач эпика
                System.out.println("++> " + task);
            }
        }
        System.out.println(" "); //Вывод подзадач
        for (Task subtask : inMemoryTaskManager.getSubtasks()) {
            System.out.println(subtask);
        }

        //Создаем историю просмотров
        inMemoryTaskManager.getTaskByID(1); //1
        inMemoryTaskManager.getTaskByID(2); //2
        inMemoryTaskManager.getEpicByID(3); //3
        inMemoryTaskManager.getTaskByID(1); //4
        inMemoryTaskManager.getSubtaskByID(4); //5
        inMemoryTaskManager.getSubtaskByID(6); //6
        inMemoryTaskManager.getEpicByID(3); //7
        inMemoryTaskManager.getSubtaskByID(4); //8
        inMemoryTaskManager.getTaskByID(2); //9
        inMemoryTaskManager.getSubtaskByID(6); //10
        inMemoryTaskManager.getSubtaskByID(4); //11
        inMemoryTaskManager.getTaskByID(2); //12
        inMemoryTaskManager.getSubtaskByID(6); //13

        System.out.println();
        System.out.println("История просмотров:");
        for (Task task : inMemoryTaskManager.getHistory()) {
            System.out.println(task);
        }

        System.out.println();
        System.out.println("Чтение из файла:");
        inMemoryTaskManager.loadFromFile();

        System.out.println("Задачи:");
        System.out.println(inMemoryTaskManager.getTasks());

        System.out.println("Эпики:");
        System.out.println(inMemoryTaskManager.getEpics());

        System.out.println("Подзадачи:");
        System.out.println(inMemoryTaskManager.getSubtasks());

        System.out.println("История:");
        System.out.println(inMemoryTaskManager.getHistory());

        System.out.println("Приоритетные задачи:");
        for (Task task : inMemoryTaskManager.getPrioritizedTasks()) {
            System.out.println(task);
        }
    }
}