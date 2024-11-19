package ru.yandex.javacource.syrovatsky.schedule.manger;

import ru.yandex.javacource.syrovatsky.schedule.task.Task;

import java.util.ArrayList;

public interface HistoryManager {
    void addHistory(Task task);

    ArrayList<Task> getHistory();
}

