package ru.yandex.javacource.syrovatsky.schedule;

import ru.yandex.javacource.syrovatsky.schedule.manger.InMemoryTaskManager;
import ru.yandex.javacource.syrovatsky.schedule.manger.Managers;

import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager(Managers.getDefaultHistory());
    }
}
