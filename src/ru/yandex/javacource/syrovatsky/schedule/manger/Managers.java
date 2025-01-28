
package ru.yandex.javacource.syrovatsky.schedule.manger;

public class Managers {
    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}

