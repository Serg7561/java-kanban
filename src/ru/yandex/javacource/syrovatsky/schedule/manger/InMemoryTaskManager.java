package ru.yandex.javacource.syrovatsky.schedule.manger;

import ru.yandex.javacource.syrovatsky.schedule.task.Epic;
import ru.yandex.javacource.syrovatsky.schedule.enums.Status;
import ru.yandex.javacource.syrovatsky.schedule.task.Subtask;
import ru.yandex.javacource.syrovatsky.schedule.task.Task;

import java.util.*;
import java.time.Instant;


public class InMemoryTaskManager implements TaskManager {

    final Map<Integer, Task> tasks = new HashMap<>();
    final Map<Integer, Epic> epics = new HashMap<>();
    final Map<Integer, Subtask> subtasks = new HashMap<>();
    HistoryManager historyManager = Managers.getDefaultHistory();
    final Comparator<Task> taskComparator = Comparator.comparing(Task::getStartTime);
    protected Set<Task> prioritizedTasks = new TreeSet<>(taskComparator);

    public InMemoryTaskManager() {
        this.historyManager = historyManager;
    }

    int generatorId = 0;

    public InMemoryTaskManager(HistoryManager defaultHistory) {
    }

    public void updateTimeEpic(Epic epic) {
        updateTimeEpics(epic);
    }

    private void addNewPrioritizedTask(Task task) {
        prioritizedTasks.add(task);
        if (!validateTaskPriority()) {
            prioritizedTasks.remove(task);
            throw new ManagerValidateException("Задачи пересекаются по времени.");
        }
    }

    private boolean validateTaskPriority() {
        List<Task> tasks = getPrioritizedTasks();

        for (int i = 1; i < tasks.size(); i++) {
            Task task = tasks.get(i);

            boolean taskHasIntersections = checkTime(task);

            if (taskHasIntersections) {
                throw new ManagerValidateException(
                        "Задачи #" + task.getId() + " и #" + tasks.get(i - 1).getId() + " пересекаются");
            }
        }
        return true;
    }

    public boolean checkTime(Task task) {
        List<Task> tasks = List.copyOf(prioritizedTasks);
        int sizeTimeNull = 0;
        if (tasks.size() > 0) {
            for (Task taskSave : tasks) {
                if (taskSave.getStartTime() != null && taskSave.getEndTime() != null) {
                    if (task.getStartTime().isBefore(taskSave.getStartTime())
                            && task.getEndTime().isBefore(taskSave.getStartTime())) {
                        return true;
                    } else if (task.getStartTime().isAfter(taskSave.getEndTime())
                            && task.getEndTime().isAfter(taskSave.getEndTime())) {
                        return true;
                    }
                } else {
                    sizeTimeNull++;
                }

            }
            return sizeTimeNull == tasks.size();
        } else {
            return true;
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }

    @Override
    public Task addTask(Task task) { //Добавить задачу
        if (task == null) {
            return null;
        }
        task.setId(getGeneratorId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic addEpic(Epic epic) { //Добавить эпик
        if (epic == null) {
            return null;
        }
        epic.setId(getGeneratorId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Subtask addSubtask(Subtask subtask) {
        if (subtask == null) {
            return null;
        }
        int id = getGeneratorId();
        subtask.setId(id);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtaskId(id);
        } else {
            throw new IllegalArgumentException("Epic not found for subtask: " + subtask.getName());
        }
        subtasks.put(id, subtask);
        updateEpicStatus(epic.getId());
        if (epic != null) {
            updateTimeEpic(epic);
        }
        return subtask;
    }

    @Override
    public Task updateTask(Task task) { //Обновление задачи
        if (task == null) {
            return null;
        }
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
        if (epic == null) {
            return null;
        }
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
        if (subtask == null) {
            return null;
        }
        int id = subtask.getId();
        int epicId = subtask.getEpicId();
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
        if (epic != null) {
            updateTimeEpic(epic);
        }
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
    public void deleteAllTasks() { //Удаление задач
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() { //Удаление эпиков
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasks() { //Удаление подзадач с изменением статуса эпика
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public Task deleteTask(int id) { //Удаления задач по ID
        return tasks.remove(id);
    }

    @Override
    public Subtask deleteSubtask(int id) { //Удаление подзадачи по ID
        Subtask subtask = subtasks.remove(id);
        if (subtask == null) {
            return null;
        }
        Epic epic = epics.get(subtask.getEpicId());
        updateEpicStatus(epic.getId());
        if (epic != null) {
            updateTimeEpic(epic);
        }
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

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public void addToHistory(int id) {
        if (epics.containsKey(id)) {
            historyManager.addHistory(epics.get(id));
        } else if (subtasks.containsKey(id)) {
            historyManager.addHistory(subtasks.get(id));
        } else if (tasks.containsKey(id)) {
            historyManager.addHistory(tasks.get(id));
        }
    }

    private int getGeneratorId() {
        return ++generatorId;
    }

    private void updateTimeEpics(Epic epic) {
        List<Subtask> subtasks = getEpicSubtasks(epic.getId());
        Instant startTime = subtasks.get(0).getStartTime();
        Instant endTime = subtasks.get(0).getEndTime();

        for (Subtask subtask : subtasks) {
            if (subtask.getStartTime().isBefore(startTime)) startTime = subtask.getStartTime();
            if (subtask.getEndTime().isAfter(endTime)) endTime = subtask.getEndTime();
        }

        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        long duration = (endTime.toEpochMilli() - startTime.toEpochMilli());
        epic.setDuration(duration);
    }
}