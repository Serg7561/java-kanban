package ru.yandex.javacource.syrovatsky.schedule.manger;

import ru.yandex.javacource.syrovatsky.schedule.enums.Status;
import ru.yandex.javacource.syrovatsky.schedule.task.Epic;
import ru.yandex.javacource.syrovatsky.schedule.task.Subtask;
import ru.yandex.javacource.syrovatsky.schedule.task.Task;
import ru.yandex.javacource.syrovatsky.schedule.enums.TaskType;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();

    private static final String HEADER_CSV_FILE = "id,type,name,status,description,epic \n";
    private File file;


    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    static String historyToString(HistoryManager manager) { //Сохранение истории

        List<Task> history = manager.getHistory();
        StringBuilder str = new StringBuilder();

        if (history.isEmpty()) {
            return "";
        }

        for (Task task : history) {
            if (task != null) {
                str.append(task.getId()).append(",");
            }
        }
        if (str.length() > 0) {
            str.deleteCharAt(str.length() - 1);
        }

        return str.toString();

    }

    static List<Integer> historyFromString(String value) { //Считывание истории

        List<Integer> toReturn = new ArrayList<>();

        if (value == null || value.isEmpty()) {
            return toReturn;
        }

        String[] idStrings = value.split(",");

        int id = Integer.parseInt(idStrings[0].trim());
        toReturn.add(id);

        return toReturn;
    }

    public void save() { //Сохранять текущее состояние менеджера в указанный файл
        // Удаление файла и создание нового
        try {
            if (Files.exists(file.toPath())) {
                Files.delete(file.toPath());
            }
            Files.createFile(file.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось найти файл для записи ", e);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(HEADER_CSV_FILE);
            writer.newLine();
            // Запись таски
            for (Task task : getTasks()) {
                writer.write(toString(task));
                writer.newLine();
            }
            // Запись эпиков
            for (Epic epic : getEpics()) {
                writer.write(toString(epic));
                writer.newLine();
            }
            // Запись подзадач
            for (Subtask subtask : getSubtasks()) {
                writer.write(toString(subtask));
                writer.newLine();
            }
            writer.newLine();
            writer.write(historyToString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранить в файл", e);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        final FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        try {
            final String csv = Files.readString(file.toPath());
            final String[] lines = csv.split(System.lineSeparator());
            int generatorId = 0;
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                if (line.isEmpty()) {
                    break;
                }
                final Task task = taskFromString(line);
                final int id = task.getId();
                if (id > generatorId) {
                    generatorId = id;
                }
                taskManager.addAnyTask(task);
            }
            for (Subtask subtask : taskManager.getSubtasks()) {
                final Epic epic = taskManager.getEpics().get(subtask.getEpicId());
                if (epic != null) {
                    epic.addSubtaskId(subtask.getId());
                } else {
                    System.err.println("Эпик с ID " + subtask.getEpicId() + " не существует для " + subtask.getId());
                }
            }
            taskManager.generatorId = generatorId;
        } catch (IOException e) {
            throw new ManagerSaveException("Невозможно прочитать файл: " + file.getName(), e);
        }
        return taskManager;
    }

    protected void addAnyTask(Task task) {
        final int id = task.getId();
        switch (task.getType()) {
            case TASK:
                tasks.put(id, task);
                break;
            case SUBTASK:
                subtasks.put(id, (Subtask) task);
                break;
            case EPIC:
                epics.put(id, (Epic) task);
                break;
        }
    }

    private static Task taskFromString(String value) { //востановление из файла
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Файл пуст");
        }

        String[] params = value.split(",");
        if (params.length < 5) {
            throw new IllegalArgumentException("Формат файла не верный");
        }

        int id = Integer.parseInt(params[0]);
        String type = params[1];
        String name = params[2];
        Status status = Status.valueOf(params[3].toUpperCase());
        String description = params[4];

        switch (type) {
            case "EPIC":
                Epic epic = new Epic(id, name, description, status);
                epic.setId(id);
                return epic;
            case "SUBTASK":
                int epicId = Integer.parseInt(params[5]);
                Subtask subtask = new Subtask(name, description, epicId);
                subtask.setId(id);
                subtask.setStatus(status);
                return subtask;
            case "TASK":
            default:
                Task task = new Task(id, name, description, status);
                task.setId(id);
                return task;
        }
    }

    private String toString(Task task) {
        return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," +
                task.getDescription() + "," + (task.getType().equals(TaskType.SUBTASK) ?
                ((Subtask) task).getEpicId() : "");
    }

    @Override
    public Task addTask(Task task) { //Добавить задачу
        super.addTask(task);
        save();
        return task;
    }

    @Override
    public Epic addEpic(Epic epic) { //Добавить эпик
        super.addEpic(epic);
        save();
        return epic;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) { //Добавить подзадачу
        super.addSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public Task updateTask(Task task) { //Обновление задачи
        super.updateTask(task);
        save();
        return task;
    }

    @Override
    public Epic updateEpic(Epic epic) { //Обновление эпика
        super.updateEpic(epic);
        save();
        return epic;

    }

    @Override
    public Subtask updateSubtask(Subtask subtask) { //Обновление подзадачи
        super.updateSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public Task getTaskByID(int id) { //Получение задачи по ID
        Task task = super.getTaskByID(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicByID(int id) { //Получение эпика по ID
        Epic epic = super.getEpicByID(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskByID(int id) { //Получение подзадачи по ID
        Subtask subtask = super.getSubtaskByID(id);
        save();
        return subtask;
    }

    @Override
    public void deleteAllTasks() { //Удаление задач
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() { //Удаление эпиков
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() { //Удаление подзадач с изменением статуса эпика
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public Task deleteTask(int id) { //Удаления задач по ID
        super.deleteTask(id);
        save();
        return getTaskByID(id);
    }

    @Override
    public Subtask deleteSubtask(int id) { //Удаление подзадачи по ID
        super.deleteSubtask(id);
        save();
        return getSubtaskByID(id);
    }

    @Override
    public Epic deleteEpic(int id) { //Удаления эпиков по ID
        super.deleteEpic(id);
        save();
        return getEpicByID(id);
    }
}
