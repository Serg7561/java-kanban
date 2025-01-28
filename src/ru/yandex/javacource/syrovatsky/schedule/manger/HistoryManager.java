package ru.yandex.javacource.syrovatsky.schedule.manger;

import ru.yandex.javacource.syrovatsky.schedule.task.Task;

import java.util.List;

public interface HistoryManager {
    void addHistory(Task task);

    void remove(int id);

    List<Task> getHistory();

}

