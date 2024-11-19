package ru.yandex.javacource.syrovatsky.schedule.manger;

import ru.yandex.javacource.syrovatsky.schedule.task.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int maxHistoryLimit = 10;
    private final ArrayList<Task> history = new ArrayList<>();

    @Override
    public void addHistory(Task task) {
        if (history.size() == maxHistoryLimit) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }
}