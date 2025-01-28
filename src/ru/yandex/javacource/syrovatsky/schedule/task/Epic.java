package ru.yandex.javacource.syrovatsky.schedule.task;

import ru.yandex.javacource.syrovatsky.schedule.enums.Status;
import ru.yandex.javacource.syrovatsky.schedule.enums.TaskType;

import java.util.ArrayList;
import java.util.Objects;
import java.time.Instant;

public class Epic extends Task {

    private ArrayList<Integer> subtaskId = new ArrayList<>();
    private Instant endTime;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public Epic(String name, String description, Status status, Instant startTime, long duration) {
        super(name, description, status, startTime, duration);
        this.endTime = super.getEndTime();
    }

    @Override
    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public void addSubtaskId(int subtaskID) {
        subtaskId.add(subtaskID);
    }

    public void clearSubtasks() {
        subtaskId.clear();
    }

    public ArrayList<Integer> getSubtaskId() {
        return new ArrayList<>(subtaskId);
    }

    public void setSubtaskId(ArrayList<Integer> subtaskId) {
        this.subtaskId = new ArrayList<>(subtaskId);
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Epic epic = (Epic) obj;
        return Objects.equals(subtaskId, epic.subtaskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskId);
    }

    @Override
    public String toString() {
        return "Эпик: {" +
                "Название: '" + getName() + '\'' +
                ", Примечание: '" + getDescription() + '\'' +
                ", ID: " + getId() +
                ", Статус задачи: " + getStatus() +
                ", ID подзадач: " + subtaskId +
                ", Время начала: " + getStartTime() +
                ", Длительность: " + getDuration() + " минут" +
                '}';
    }

}