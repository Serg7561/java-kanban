package ru.yandex.javacource.syrovatsky.schedule.manger;

import ru.yandex.javacource.syrovatsky.schedule.task.Epic;
import ru.yandex.javacource.syrovatsky.schedule.task.Subtask;
import ru.yandex.javacource.syrovatsky.schedule.task.Task;

import java.util.List;

public interface TaskManager {
    int getGeneratorId();

    Task addTask(Task task);

    Epic addEpic(Epic epic);

    Subtask addSubtask(Subtask subtask);

    Task updateTask(Task task);

    Epic updateEpic(Epic epic);

    Subtask updateSubtask(Subtask subtask);

    Task getTaskByID(int id);

    Epic getEpicByID(int id);

    Subtask getSubtaskByID(int id);

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    Task deleteTask(int id); //Удаления задач по ID

    Subtask deleteSubtask(int id);

    Epic deleteEpic(int id);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getEpicSubtasks(int epicId);

    List<Subtask> getSubtasks();

    void updateEpicStatus(int epicId);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();

}
