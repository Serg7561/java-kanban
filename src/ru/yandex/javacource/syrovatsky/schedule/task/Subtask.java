package ru.yandex.javacource.syrovatsky.schedule.task;

import ru.yandex.javacource.syrovatsky.schedule.enums.Status;
import ru.yandex.javacource.syrovatsky.schedule.enums.TaskType;

import java.util.Objects;
import java.time.Instant;

public class Subtask extends Task {

    private int epicId;

    public Subtask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Status status, int epicId, Instant startTime, long duration) {
        super(name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }


    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Subtask subtask = (Subtask) obj;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return "Подзадача: {" +
                "Название: '" + getName() + '\'' +
                ", Примечание: '" + getDescription() + '\'' +
                ", ID: " + getId() +
                ", ID эпика: " + getEpicId() +
                ", Статус задачи: " + getStatus() +
                ", Время начала: " + getStartTime() +
                ", Длительность: " + getDuration() + " минут" +
                '}';
    }
}