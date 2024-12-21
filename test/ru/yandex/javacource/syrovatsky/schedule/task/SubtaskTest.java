package ru.yandex.javacource.syrovatsky.schedule.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import ru.yandex.javacource.syrovatsky.schedule.enums.Status;

class SubtaskTest {

    @Test
    void testEquals() {
        Subtask s1 = new Subtask(8, "Выкинуть мусор", "Не забыть!!!", Status.NEW);
        Subtask s2 = new Subtask(8, "Запустить посудомойку", "Проверить остатки моющего", Status.DONE);
        assertEquals(s1, s2,
                "Ошибка! Если равны ID то должны быть равны и подтаски");
    }
}