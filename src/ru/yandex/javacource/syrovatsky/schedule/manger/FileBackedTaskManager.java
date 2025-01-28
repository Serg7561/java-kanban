package ru.yandex.javacource.syrovatsky.schedule.manger;

import ru.yandex.javacource.syrovatsky.schedule.enums.Status;
import ru.yandex.javacource.syrovatsky.schedule.task.Epic;
import ru.yandex.javacource.syrovatsky.schedule.task.Subtask;
import ru.yandex.javacource.syrovatsky.schedule.task.Task;
import ru.yandex.javacource.syrovatsky.schedule.enums.TaskType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private static final String HEADER_CSV_FILE = "id,type,name,status,description,startTime,duration,epic \n";
    private File file;

    public FileBackedTaskManager(HistoryManager defaultHistory, File file) {
        super();
        this.file = file;
    }

    static String historyToString(HistoryManager manager) {
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

    static List<Integer> historyFromString(String value) {
        List<Integer> toReturn = new ArrayList<>();

        if (value == null || value.isEmpty()) {
            return toReturn;
        }

        String[] idStrings = value.split(",");

        for (String idString : idStrings) {
            try {
                int id = Integer.parseInt(idString.trim());
                toReturn.add(id);
            } catch (NumberFormatException e) {
                System.err.println("Некорректный ID в истории: " + idString);
            }
        }

        return toReturn;
    }

    public void save() {
        try {
            if (Files.exists(file.toPath())) {
                Files.delete(file.toPath());
            }
            Files.createFile(file.toPath());
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось найти файл для записи", e);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(HEADER_CSV_FILE);
            for (Task task : getTasks()) {
                writer.write(toString(task));
                writer.newLine();
            }
            for (Epic epic : getEpics()) {
                writer.write(toString(epic));
                writer.newLine();
            }
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

    public void loadFromFile() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {

            String line = bufferedReader.readLine();
            while (bufferedReader.ready()) {
                line = bufferedReader.readLine();
                if (line == null || line.isEmpty()) {
                    break;
                }

                Task task = taskFromString(line);

                if (task instanceof Epic epic) {
                    addEpic(epic);
                } else if (task instanceof Subtask subtask) {
                    addSubtask(subtask);
                } else {
                    addTask(task);
                }
            }

            String lineWithHistory = bufferedReader.readLine();
            if (lineWithHistory != null && !lineWithHistory.isEmpty()) {
                for (int id : historyFromString(lineWithHistory)) {
                    addToHistory(id);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось считать данные из файла.");
        }
    }

    private static Task taskFromString(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Файл пуст");
        }

        String[] params = value.split(",");
        if (params.length < 7) {
            throw new IllegalArgumentException("Формат файла не верный");
        }

        int id = Integer.parseInt(params[0].trim());
        String type = params[1].trim();
        String name = params[2].trim();
        Status status = Status.valueOf(params[3].trim().toUpperCase());
        String description = params[4].trim();
        Instant startTime = params[5].trim().equals("null") ? null : Instant.parse(params[5].trim());
        long duration = Long.parseLong(params[6].trim());
        Integer epicId = type.equals("SUBTASK") ? Integer.parseInt(params[7].trim()) : null;

        if (type.equals("EPIC")) {
            Epic epic = new Epic(name, description, status, startTime, duration);
            epic.setId(id);
            return epic;
        } else if (type.equals("SUBTASK")) {
            Subtask subtask = new Subtask(name, description, status, epicId, startTime, duration);
            subtask.setId(id);
            return subtask;
        } else {
            Task task = new Task(name, description, status, startTime, duration);
            task.setId(id);
            return task;
        }
    }

    private String toString(Task task) {
        return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," +
                task.getDescription() + "," + (task.getStartTime() != null ? task.getStartTime() : "null") + "," +
                task.getDuration() + "," + (task.getType().equals(TaskType.SUBTASK) ?
                ((Subtask) task).getEpicId() : "");
    }

    @Override
    public Task addTask(Task task) {
        if (task == null) {
            return null;
        }
        super.addTask(task);
        return task;
    }

    @Override
    public Epic addEpic(Epic epic) {
        if (epic == null) {
            return null;
        }
        super.addEpic(epic);
        return epic;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        if (subtask == null) {
            return null;
        }
        super.addSubtask(subtask);
        return subtask;
    }

    @Override
    public Task updateTask(Task task) {
        if (task == null) {
            return null;
        }
        super.updateTask(task);
        save();
        return task;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        if (epic == null) {
            return null;
        }
        super.updateEpic(epic);
        save();
        return epic;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        if (subtask == null) {
            return null;
        }
        updateEpicStatus(subtask.getEpicId());
        return subtask;
    }

    @Override
    public Task getTaskByID(int id) {
        Task task = super.getTaskByID(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicByID(int id) {
        Epic epic = super.getEpicByID(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskByID(int id) {
        Subtask subtask = super.getSubtaskByID(id);
        save();
        return subtask;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public Task deleteTask(int id) {
        Task task = super.deleteTask(id);
        save();
        return task;
    }

    @Override
    public Subtask deleteSubtask(int id) {
        Subtask subtask = super.deleteSubtask(id);
        save();
        return subtask;
    }

    @Override
    public Epic deleteEpic(int id) {
        Epic epic = super.deleteEpic(id);
        save();
        return epic;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return super.getPrioritizedTasks();
    }
}