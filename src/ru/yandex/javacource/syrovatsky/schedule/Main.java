package ru.yandex.javacource.syrovatsky.schedule;

import ru.yandex.javacource.syrovatsky.schedule.manger.TasksManager;
import ru.yandex.javacource.syrovatsky.schedule.task.Epic;
import ru.yandex.javacource.syrovatsky.schedule.task.Status;
import ru.yandex.javacource.syrovatsky.schedule.task.Subtask;
import ru.yandex.javacource.syrovatsky.schedule.task.Task;

public class Main {

    public static void main(String[] args) {

        TasksManager taskManager = new TasksManager();

        Task awayTrash = new Task("Выбросить мусор", "И старое ведро");
        Task awayTrashCreated = taskManager.addTask(awayTrash);
        System.out.println(awayTrashCreated);

        Task Slep = new Task("Поспать", "Хотя бы пару часов");
        Task SlepCreated = taskManager.addTask(Slep);
        System.out.println(SlepCreated);

        Task awayTrashToUpdate = new Task(awayTrash.getId(), "Не забыть выбросить мусор",
                "Хотя бы пакет", Status.IN_PROGRESS);
        Task awayTrashUpdated = taskManager.updateTask(awayTrashToUpdate);
        System.out.println(awayTrashUpdated);

        Epic Test = new Epic("Провести тесты", "Нужно успеть за вечер");
        taskManager.addEpic(Test);
        System.out.println(Test);
        Subtask TestSubtask = new Subtask("Все тесты", "Хотя бы основные",
                Test.getId());
        taskManager.addSubtask(TestSubtask);
        System.out.println(TestSubtask);

        Epic repairCar = new Epic("Починить машину", "Нужно успеть на выходных");
        taskManager.addEpic(repairCar);
        System.out.println(repairCar);
        Subtask repairCarSubtask1 = new Subtask("Заменить фары", "Обязательно оригинал!",
                repairCar.getId());
        Subtask repairCarSubtask2 = new Subtask("Заменить резину", "Старую по возможности продать",
                repairCar.getId());
        taskManager.addSubtask(repairCarSubtask1);
        taskManager.addSubtask(repairCarSubtask2);
        System.out.println(repairCar);
        repairCarSubtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(repairCarSubtask2);
        System.out.println(repairCar);
        System.out.println(repairCarSubtask1);
        System.out.println(repairCarSubtask2);
        System.out.println(" ");
        taskManager.deleteTask(2);
        System.out.println(taskManager.getTasks());
        System.out.println(" ");
        taskManager.deleteSubtask(8);
        System.out.println(taskManager.getSubtasks());
        System.out.println(" ");
        taskManager.deleteEpic(4);
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getEpicSubtasks(2));
    }
}