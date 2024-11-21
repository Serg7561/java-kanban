package ru.yandex.javacource.syrovatsky.schedule.manger;

import ru.yandex.javacource.syrovatsky.schedule.task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int MAX_HISTORY_LIMIT = 10;
    private final List<Task> history = new ArrayList<>();

    @Override
    public void addHistory(Task task) {
        if (history.size() == MAX_HISTORY_LIMIT) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}