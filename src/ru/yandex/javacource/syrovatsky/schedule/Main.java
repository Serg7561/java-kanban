package ru.yandex.javacource.syrovatsky.schedule;

import ru.yandex.javacource.syrovatsky.schedule.manger.InMemoryTaskManager;
import ru.yandex.javacource.syrovatsky.schedule.manger.Managers;
import ru.yandex.javacource.syrovatsky.schedule.task.Epic;
import ru.yandex.javacource.syrovatsky.schedule.enums.Status;
import ru.yandex.javacource.syrovatsky.schedule.task.Subtask;
import ru.yandex.javacource.syrovatsky.schedule.task.Task;

public class Main {

    private static final InMemoryTaskManager inMemoryTaskManager = Managers.getDefault();

    public static void main(String[] args) {

        Task awayTrash = new Task("Выбросить мусор", "И старое ведро");
        Task awayTrashCreated = inMemoryTaskManager.addTask(awayTrash);
        System.out.println(awayTrashCreated);

        Task Slep = new Task("Поспать", "Хотя бы пару часов");
        Task SlepCreated = inMemoryTaskManager.addTask(Slep);
        System.out.println(SlepCreated);

        Task awayTrashToUpdate = new Task(awayTrash.getId(), "Не забыть выбросить мусор",
                "Хотя бы пакет", Status.IN_PROGRESS);
        Task awayTrashUpdated = inMemoryTaskManager.updateTask(awayTrashToUpdate);
        System.out.println(awayTrashUpdated);

        Epic Test = new Epic("Провести тесты", "Нужно успеть за вечер");
        inMemoryTaskManager.addEpic(Test);
        System.out.println(Test);
        Subtask TestSubtask = new Subtask("Все тесты", "Хотя бы основные",
                Test.getId());
        inMemoryTaskManager.addSubtask(TestSubtask);
        System.out.println(TestSubtask);

        Epic repairCar = new Epic("Починить машину", "Нужно успеть на выходных");
        inMemoryTaskManager.addEpic(repairCar);
        System.out.println(repairCar);
        Subtask repairCarSubtask1 = new Subtask("Заменить фары", "Обязательно оригинал!",
                repairCar.getId());
        Subtask repairCarSubtask2 = new Subtask("Заменить резину", "Старую по возможности продать",
                repairCar.getId());
        inMemoryTaskManager.addSubtask(repairCarSubtask1);
        inMemoryTaskManager.addSubtask(repairCarSubtask2);
        System.out.println(repairCar);
        repairCarSubtask2.setStatus(Status.DONE);
        inMemoryTaskManager.updateSubtask(repairCarSubtask2);
        /*System.out.println(repairCar);
        System.out.println(repairCarSubtask1);
        System.out.println(repairCarSubtask2);
        System.out.println(" ");
        inMemoryTaskManager.deleteTask(2);
        System.out.println(inMemoryTaskManager.getTasks());
        System.out.println(" ");
        inMemoryTaskManager.deleteSubtask(8);
        System.out.println(inMemoryTaskManager.getSubtasks());
        System.out.println(" ");
        inMemoryTaskManager.deleteEpic(4);
        System.out.println(inMemoryTaskManager.getEpics());
        System.out.println(inMemoryTaskManager.getEpicSubtasks(2));*/

        System.out.println(" ");
        System.out.println("Выводим все задачи:");
        System.out.println(" "); //Вывод задач
        for (Task task : Main.inMemoryTaskManager.getTasks()) {
            System.out.println(task);
        }
        System.out.println(" "); //Вывод эпиков
        for (Epic epic : Main.inMemoryTaskManager.getEpics()) {
            System.out.println(epic);
            for (Task task : Main.inMemoryTaskManager.getEpicSubtasks(epic.getId())) { //Вывод подзадач эпика
                System.out.println("++> " + task);
            }
        }
        System.out.println(" "); //Вывод подзадач
        for (Task subtask : Main.inMemoryTaskManager.getSubtasks()) {
            System.out.println(subtask);
        }

        //Создаем историю просмотров
        Main.inMemoryTaskManager.getTaskByID(1); //1
        Main.inMemoryTaskManager.getTaskByID(2); //2
        Main.inMemoryTaskManager.getEpicByID(3); //3
        Main.inMemoryTaskManager.getTaskByID(1); //4
        Main.inMemoryTaskManager.getSubtaskByID(4); //5
        Main.inMemoryTaskManager.getSubtaskByID(6); //6
        Main.inMemoryTaskManager.getEpicByID(3); //7
        Main.inMemoryTaskManager.getSubtaskByID(4); //8
        Main.inMemoryTaskManager.getTaskByID(2); //9
        Main.inMemoryTaskManager.getSubtaskByID(6); //10
        Main.inMemoryTaskManager.getSubtaskByID(4); //11
        Main.inMemoryTaskManager.getTaskByID(2); //12
        Main.inMemoryTaskManager.getSubtaskByID(6); //13


        System.out.println();
        System.out.println("История просмотров:");
        for (Task task : Main.inMemoryTaskManager.getHistory()) {
            System.out.println(task);
        }
    }
}