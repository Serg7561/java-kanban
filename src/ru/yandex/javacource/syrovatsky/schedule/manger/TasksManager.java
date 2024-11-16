package ru.yandex.javacource.syrovatsky.schedule.manger;

import ru.yandex.javacource.syrovatsky.schedule.task.Epic;
import ru.yandex.javacource.syrovatsky.schedule.task.Status;
import ru.yandex.javacource.syrovatsky.schedule.task.Subtask;
import ru.yandex.javacource.syrovatsky.schedule.task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TasksManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private int generatorId = 0;

    private int getGeneratorId() {
        return ++generatorId;
    }

    public Task addTask(Task task) { //Добавить задачу
        task.setId(getGeneratorId());
        tasks.put(task.getId(), task);
        return task;
    }

    public Epic addEpic(Epic epic) { //Добавить эпик
        epic.setId(getGeneratorId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Subtask addSubtask(Subtask subtask) { //Добавить подзадачу
        int id = getGeneratorId();
        subtask.setId(id);
        Epic epic = epics.get(subtask.getepicId());
        epic.addSubtaskId(id);
        subtasks.put(id, subtask);
        updateEpicStatus(epic.getId());
        return subtask;
    }

    public Task updateTask(Task task) { //Обновление задачи
        int id = task.getId();
        Task savedTask = tasks.get(id);
        if (savedTask == null) {
            return null;
        }
        tasks.put(id, task);
        return task;
    }

    public void updateEpic(Epic epic) { //Обновление эпика
        Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) {
            return;
        }
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
    }

    public void updateSubtask(Subtask subtask) { //Обновление подзадачи
        int id = subtask.getId();
        int epicId = subtask.getepicId();
        Subtask savedSubtask = subtasks.get(id);
        if (savedSubtask == null) {
            return;
        }
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }
        subtasks.put(id, subtask);
        updateEpicStatus(epicId);
    }

    public Task getTaskByID(int id) { //Получение задачи по ID
        return tasks.get(id);
    }

    public Epic getEpicByID(int id) { //Получение эпика по ID
        return epics.get(id);
    }

    public Subtask getSubtaskByID(int id) { //Получение подзадачи по ID
        return subtasks.get(id);
    }

    public void deleteTasks() { //Удаление задач
        tasks.clear();
    }

    public void deleteEpics() { //Удаление эпиков
        epics.clear();
        subtasks.clear();
    }

    public void deleteSubtasks() { //Удаление подзадач с изменением статуса эпика
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
            epic.setStatus(Status.NEW);
        }
    }

    public void deleteTask(int id) { //Удаление задачи
        tasks.remove(id);
    } //Удаления задач по ID

    public void deleteSubtask(int id) { //Удаление подзадачи по ID
        Subtask subtask = subtasks.remove(id);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getepicId());
        updateEpicStatus(epic.getId());
    }

    public void deleteEpic(int id) { //Удаления эпиков по ID
        final Epic epic = epics.remove(id);
        if (epic == null) {
            return;
        }
        for (Integer subtaskId : epic.getSubtaskId()) {
            subtasks.remove(subtaskId);
        }
    }

    public ArrayList<Task> getTasks() { //Получение всех задач
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() { //Получение всех эпиков
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getEpicSubtasks(int epicId) { // Получение подзадач по ID эпика
        ArrayList<Subtask> subtasksForId = new ArrayList<>();
        if (!epics.containsKey(epicId)) {
            return subtasksForId;
        }
        ArrayList<Integer> subtaskIds = epics.get(epicId).getSubtaskId();
        for (Integer sub : subtaskIds) {

            subtasksForId.add(subtasks.get(sub));
        }
        return subtasksForId;
    }

    public ArrayList<Subtask> getSubtasks() { //Получение всех подзадач
        return new ArrayList<>(subtasks.values());
    }

    private void updateEpicStatus(int epicId) {  //Метод для контроля статуса эпика при удалении или изменении подзадач
        Epic epic = epics.get(epicId);

        ArrayList<Integer> subtaskIds = epics.get(epicId).getSubtaskId();

        int doneCount = 0;
        int newCount = 0;
        for (Integer subtaskId : subtaskIds) {
            Task subtask = tasks.get(subtaskId);
            if (subtask == null) {
                continue;
            }
            if (subtask.getStatus() == Status.DONE) {
                doneCount++;
            } else if (subtask.getStatus() == Status.NEW) {
                newCount++;
            }
        }
        if (doneCount == subtaskIds.size()) {
            assert epic != null;
            epic.setStatus(Status.DONE);
        } else if (newCount == subtaskIds.size()) {
            assert epic != null;
            epic.setStatus(Status.NEW);
        } else {
            assert epic != null;
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}