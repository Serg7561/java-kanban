package ru.yandex.javacource.syrovatsky.schedule.manger;

import ru.yandex.javacource.syrovatsky.schedule.task.Epic;
import ru.yandex.javacource.syrovatsky.schedule.task.Subtask;
import ru.yandex.javacource.syrovatsky.schedule.task.Task;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface TaskManager {
    int getGeneratorId();

    Task addTask(Task task);

    Epic addEpic(Epic epic);

    Subtask addSubtask(Subtask subtask);

    Task updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    Task getTaskByID(int id);

    Epic getEpicByID(int id);

    Subtask getSubtaskByID(int id);

    void deleteTasks();

    void deleteEpics();

    void deleteSubtasks();

    void deleteTask(int id) //Удаления задач по ID
    ;

    void deleteSubtask(int id);

    void deleteEpic(int id);

    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getEpicSubtasks(int epicId);

    ArrayList<Subtask> getSubtasks();

    void updateEpicStatus(int epicId);

    ArrayList<Task> getHistory();
}
