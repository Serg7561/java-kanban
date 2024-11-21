package ru.yandex.javacource.syrovatsky.schedule.test.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import ru.yandex.javacource.syrovatsky.schedule.enums.Status;
import ru.yandex.javacource.syrovatsky.schedule.task.Task;

class TaskTest {

    @Test
    void testEquals() {
        Task t1 = new Task(5, "Зайти на почту", "Сначала попробовать записаться",
                Status.IN_PROGRESS);
        Task t2 = new Task(5, "Купить кошачий корм", "Вспомнить точно какой", Status.DONE);
        assertEquals(t1, t2,
                "Ошибка! Если равны ID то должны быть равны и таски ");
    }
}