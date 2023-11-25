package manager;

import domain.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    Node first;
    Node last;
    Map<Integer, Node> nodes = new HashMap<>();

    private static class Node {
        Node prev;
        Node next;
        Task task;

        public Node(Node prev, Node next, Task task) {
            this.prev = prev;
            this.next = next;
            this.task = task;
        }
    }

    private void linkLast(Task task) {
        Node node = new Node(last, null, task);
        if (first == null) {
            first = node;
        } else {
            last.next = node;
        }
        last = node;
        nodes.put(task.getId(), node);
    }

    private void removeNode(Node node) {
        if (node == null)
            return;
        if (node.prev == null && node.next == null) {
            first = null;
            last = null;
        } else if (node.prev == null) {
            first = node.next;
            node.next.prev = null;
        } else if (node.next == null) {
            last = node.prev;
            node.prev.next = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        Node current = first;
        while (current != null) {
            history.add(current.task);
            current = current.next;
        }
        return history;
    }

    @Override
    public void addTask(Task task) {
        remove(task.getId());
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        removeNode(nodes.get(id));
    }
}
