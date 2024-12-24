package ru.yandex.javacource.syrovatsky.schedule.manger;

import ru.yandex.javacource.syrovatsky.schedule.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {

    public static class Node {

        private Task task;
        private Node prev;
        private Node next;

        public Node(Task task) {
            this.task = task;
            this.prev = null;
            this.next = null;
        }

        public Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }

        public Task getTask() {
            return task;
        }

        public void setTask(Task task) {
            this.task = task;
        }

        public Node getPrev() {
            return prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }

    private Map<Integer, Node> history = new HashMap<>();
    private Node head;
    private Node tail;

    public void listLast(Task task) {
        final Node node = new Node(task, tail, null);
        if (head == null) {
            head = node;
        } else {
            tail.setNext(node);
        }
        tail = node;

    }

    public List<Task> getTasks() {
        List<Task> result = new ArrayList<>();
        Node element = head;
        while (element != null) {
            result.add(element.getTask());
            element = element.getNext();
        }
        return result;
    }

    public void removeNode(Node node) {

        Node prev = node.getPrev();
        Node next = node.getNext();

        if (head == node) {
            head = node.getNext();
        }
        if (tail == node) {
            tail = node.getPrev();
        }

        if (prev != null) {
            prev.setNext(next);
        }

        if (next != null) {
            next.setPrev(prev);
        }

    }

    public Node getNode(int id) {
        return history.get(id);
    }

    @Override
    public void addHistory(Task task) {
        if (task == null) {
            return;
        }
        final int id = task.getId();
        remove(id);
        listLast(task);
        history.put(id, tail);
    }

    @Override
    public void remove(int id) {
        final Node node = history.remove(id);
        if (node == null) {
            return;
        }
        removeNode(node);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}
