package ru.yandex.javacource.syrovatsky.schedule.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import ru.yandex.javacource.syrovatsky.schedule.enums.Status;

class EpicTest {

    @Test
    void testEquals() {
        Epic e1 = new Epic(1, "Продать машину", "По дороже! ", Status.DONE);
        Epic e2 = new Epic(1, "Записаться к стоматологу", "На вечер после работы",
                Status.NEW);
        assertEquals(e1, e2,
                "Ошибка! Если равны ID то должны быть равны и эпики ");
    }
}