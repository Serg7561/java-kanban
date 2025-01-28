package ru.yandex.javacource.syrovatsky.schedule.manger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ManagersTest {

    @Test
    void getDefault() {
        assertInstanceOf(InMemoryTaskManager.class, Managers.getDefault(), "Объект-менеджер не возвращается");
    }

    @Test
    void getDefaultHistory() {
        assertInstanceOf(InMemoryHistoryManager.class, Managers.getDefaultHistory(), "Объект не создается");
    }
}