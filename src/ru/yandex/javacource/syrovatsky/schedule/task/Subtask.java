package ru.yandex.javacource.syrovatsky.schedule.task;

import ru.yandex.javacource.syrovatsky.schedule.enums.Status;
import ru.yandex.javacource.syrovatsky.schedule.enums.TaskType;

public class Subtask extends Task {

    private int epicId; //

    public Subtask(String name, String description) {
        super(name, description);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, String description, Status status) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int epicId) {
        super(name, description);
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
    public String toString() {
        return "Подзадача" + " : {" +
                "Название: '" + getName() + '\'' +
                ", Примечание: '" + getdescription() + '\'' +
                ", id: " + getId() +
                ", id эпика: " + getEpicId() +
                ", Статус задачи: " + getStatus() +
                '}';
    }
}