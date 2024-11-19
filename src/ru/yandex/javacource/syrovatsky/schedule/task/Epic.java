package ru.yandex.javacource.syrovatsky.schedule.task;

import ru.yandex.javacource.syrovatsky.schedule.enums.Status;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtaskId = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public void addSubtaskId(int subtaskID) {
        subtaskId.add(subtaskID);
    }

    public void clearSubtasks() {
        subtaskId.clear();
    }

    public ArrayList<Integer> getSubtaskId() {
        return subtaskId;
    }


    public void setSubtaskId(ArrayList<Integer> subtaskId) {
        this.subtaskId = subtaskId;
    }

    public void getSubtaskId(ArrayList<Integer> subtaskId) {
        this.subtaskId = subtaskId;
    }

    @Override
    public String toString() {
        return "Эпик" + " : {" +
                "Название: " + getName() + '\'' +
                ", Примечание: " + getdescription() + '\'' +
                ", id: " + getId() +
                ", Статус задачи: " + getStatus() +
                '}';
    }
}