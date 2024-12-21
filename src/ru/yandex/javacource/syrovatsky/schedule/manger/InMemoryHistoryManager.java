package ru.yandex.javacource.syrovatsky.schedule.manger;

import ru.yandex.javacource.syrovatsky.schedule.task.Task;

import java.util.ArrayList;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {

    //private static final int MAX_HISTORY_LIMIT = 10;
    private final List<Task> history = new ArrayList<>();


    @Override
    public void addHistory(Task task) {
        for (Task t : history) {
            if (t.getId() == task.getId()) {
                history.remove(t);
                break;
            }
        }
        history.add(task);
    }

    @Override
    public void remove(int id) {
        Task taskToRemove = null;
        for (Task task : history) {
            if (task.getId() == id) {
                taskToRemove = task;
                break;
            }
        }
        if (taskToRemove != null) {
            history.remove(taskToRemove);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}