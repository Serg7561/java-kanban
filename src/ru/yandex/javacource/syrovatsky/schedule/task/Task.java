package ru.yandex.javacource.syrovatsky.schedule.task;

import ru.yandex.javacource.syrovatsky.schedule.enums.Status;
import ru.yandex.javacource.syrovatsky.schedule.enums.TaskType;

import java.util.Objects;
import java.time.Instant;

public class Task {

    private int id;
    private String name;
    private String description;
    private Status status;
    private Instant startTime;
    private long duration;

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(int id, String name, String description, Status status, Instant startTime, long duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String description, Status status, Instant startTime, long duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        if (startTime == null) {
            throw new IllegalArgumentException("Время начала не может быть null");
        }
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        if (duration < 0) {
            throw new IllegalArgumentException("Длительность не может быть отрицательной");
        }
        this.duration = duration;
    }

    public Instant getEndTime() {
        long secondsInMinute = 60L;
        return startTime.plusSeconds(duration * secondsInMinute);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return id == task.id && Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) &&
                status == task.status;
    }

    @Override
    public String toString() {
        return "Задача: {" +
                "Название: '" + name + '\'' +
                ", Описание: '" + description + '\'' +
                ", ID: " + id +
                ", Статус: " + status +
                ", Время начала: " + startTime +
                ", Длительность: " + duration + " минут" +
                '}';
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + id;
        hash = 31 * hash + (name != null ? name.hashCode() : 0);
        hash = 31 * hash + (description != null ? description.hashCode() : 0);
        hash = 31 * hash + (status != null ? status.hashCode() : 0);
        return hash;
    }
}