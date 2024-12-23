package ru.yandex.javacource.syrovatsky.schedule.manger;

import ru.yandex.javacource.syrovatsky.schedule.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {

    public static class NewList {
        public Map<Integer, Node> table = new HashMap<>();
        public Node head;
        public Node tail;


        public void listLast(Task task) {
            Node element = new Node(task);
            element.setTask(task);

            if (table.containsKey(task.getId())) {
                removeNode(table.get(task.getId()));
            }

            if (head == null) {
                tail = element;
                head = element;
                element.setNext(null);
                element.setPrev(null);
            } else {
                element.setPrev(tail);
                element.setNext(null);
                tail.setNext(element);
                tail = element;
            }

            table.put(task.getId(), element);
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
            if (node != null) {
                table.remove(node.getTask().getId());
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
        }

        public Node getNode(int id) {
            return table.get(id);
        }
    }

    public final NewList list = new NewList();

    @Override
    public void addHistory(Task task) {
        list.listLast(task);
    }

    @Override
    public void remove(int id) {
        list.removeNode(list.getNode(id));
    }

    @Override
    public List<Task> getHistory() {
        return list.getTasks();
    }
}