package domain;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private Statuses status = Statuses.NEW;

    private ArrayList<Integer> subtaskIds;

    public Epic(String name, String description) {
        super(name, description);
        subtaskIds = new ArrayList<>();
    }

    public void addSubTaskId(int subTaskId) {
        subtaskIds.add(subTaskId);
    }

    public ArrayList<Integer> getSubTasksIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(int id) {
        subtaskIds.remove(id);

    }

    @Override
    public String toString() {
        return "domain.Epic{" +
                "id=" + id +
                ", subtaskIds.size=" + subtaskIds.size() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return status == epic.status && Objects.equals(subtaskIds, epic.subtaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), status, subtaskIds);
    }
}
