package ru.yandex.javacource.syrovatsky.schedule.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import ru.yandex.javacource.syrovatsky.schedule.enums.Status;


class TaskTest {

    @Test
    void testEquals() {
        Task t1 = new Task(5, "Зайти на почту", "Сначала попробовать записаться",
                Status.IN_PROGRESS);
        Task t2 = new Task(5, "Купить кошачий корм", "Вспомнить точно какой", Status.DONE);
        assertNotEquals(t1, t2, "Задачи с разными ID не должны быть одинаковыми");
    }
}