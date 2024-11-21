package ru.yandex.javacource.syrovatsky.schedule.manger;

import ru.yandex.javacource.syrovatsky.schedule.task.Epic;
import ru.yandex.javacource.syrovatsky.schedule.enums.Status;
import ru.yandex.javacource.syrovatsky.schedule.task.Subtask;
import ru.yandex.javacource.syrovatsky.schedule.task.Task;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private int generatorId = 0;

    @Override
    public int getGeneratorId() {
        return ++generatorId;
    }

    @Override
    public Task addTask(Task task) { //Добавить задачу
        task.setId(getGeneratorId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic addEpic(Epic epic) { //Добавить эпик
        epic.setId(getGeneratorId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) { //Добавить подзадачу
        int id = getGeneratorId();
        subtask.setId(id);
        Epic epic = epics.get(subtask.getepicId());
        epic.addSubtaskId(id);
        subtasks.put(id, subtask);
        updateEpicStatus(epic.getId());
        return subtask;
    }

    @Override
    public Task updateTask(Task task) { //Обновление задачи
        int id = task.getId();
        Task savedTask = tasks.get(id);
        if (savedTask == null) {
            return null;
        }
        tasks.put(id, task);
        return task;
    }

    @Override
    public Epic updateEpic(Epic epic) { //Обновление эпика
        Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) {
            return null;
        }
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
        return epic;

    }

    @Override
    public Subtask updateSubtask(Subtask subtask) { //Обновление подзадачи
        int id = subtask.getId();
        int epicId = subtask.getepicId();
        Subtask savedSubtask = subtasks.get(id);
        if (savedSubtask == null) {
            return null;
        }
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        subtasks.put(id, subtask);
        updateEpicStatus(epicId);
        return subtask;
    }

    @Override
    public Task getTaskByID(int id) { //Получение задачи по ID
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.addHistory(task);
        }
        return task;
    }

    @Override
    public Epic getEpicByID(int id) { //Получение эпика по ID
        Task task = epics.get(id);
        if (task != null) {
            historyManager.addHistory(task);
        }
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskByID(int id) { //Получение подзадачи по ID
        Task task = subtasks.get(id);
        if (task != null) {
            historyManager.addHistory(task);
        }
        return subtasks.get(id);
    }

    @Override
    public void deleteTasks() { //Удаление задач
        tasks.clear();
    }

    @Override
    public void deleteEpics() { //Удаление эпиков
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteSubtasks() { //Удаление подзадач с изменением статуса эпика
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public Task deleteTask(int id) { //Удаление задачи
        return tasks.remove(id);
    } //Удаления задач по ID

    @Override
    public Subtask deleteSubtask(int id) { //Удаление подзадачи по ID
        Subtask subtask = subtasks.remove(id);
        if (subtask == null) {
            return null;
        }
        Epic epic = epics.get(subtask.getepicId());
        updateEpicStatus(epic.getId());
        return subtask;
    }

    @Override
    public Epic deleteEpic(int id) { //Удаления эпиков по ID
        final Epic epic = epics.remove(id);
        if (epic == null) {
            return null;
        }
        for (Integer subtaskId : epic.getSubtaskId()) {
            subtasks.remove(subtaskId);
        }
        return epics.remove(id);
    }

    @Override
    public ArrayList<Task> getTasks() { //Получение всех задач
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() { //Получение всех эпиков
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getEpicSubtasks(int epicId) { // Получение подзадач по ID эпика
        ArrayList<Subtask> subtasksForId = new ArrayList<>();
        if (!epics.containsKey(epicId)) {
            return subtasksForId;
        }
        List<Integer> subtaskIds = epics.get(epicId).getSubtaskId();
        for (Integer sub : subtaskIds) {

            subtasksForId.add(subtasks.get(sub));
        }
        return subtasksForId;
    }

    @Override
    public List<Subtask> getSubtasks() { //Получение всех подзадач
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void updateEpicStatus(int epicId) {  //Метод для контроля статуса эпика при удалении или изменении подзадач
        Epic epic = epics.get(epicId);

        List<Integer> subtaskIds = epics.get(epicId).getSubtaskId();

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

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}