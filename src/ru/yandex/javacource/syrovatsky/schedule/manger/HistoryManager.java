package ru.yandex.javacource.syrovatsky.schedule.manger;

import ru.yandex.javacource.syrovatsky.schedule.task.Task;

import java.util.List;

public interface HistoryManager {
    void addHistory(Task task);

    List<Task> getHistory();
}

