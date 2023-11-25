package manager;

import domain.Epic;
import domain.Statuses;
import domain.SubTask;
import domain.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private int generatorId = 1;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void addTask(Task task) {
        task.setId(generatorId++);
        tasks.put(task.getId(),task);
    }

    @Override
    public  void addEpic(Epic epic) {
        epic.setId(generatorId++);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addSubTask(SubTask subTask) {
        int epicId = subTask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }
        subTask.setId(generatorId++);
        subTasks.put(subTask.getId(), subTask);
        epic.addSubTaskId(subTask.getId());
        updateEpicStatus(epic.getId());
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        subTasks.clear();
        for (Epic epicId : epics.values()) {
            epicId.getSubTasksIds().clear();
            updateEpicStatus(epicId.getId());
        }
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.addTask(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.addTask(epics.get(id));
        return epics.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        historyManager.addTask(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {  //Если удаляем эпик, то и удаляем все его подзадачи.
        Epic epic = epics.remove(id);
        historyManager.remove(id);
        ArrayList<Integer> subtasks = epic.getSubTasksIds();
            for (Integer subId : subtasks) {
                subTasks.remove(subId);
                historyManager.remove(subId);
            }
    }

    @Override
    public void deleteSubTaskById(int id) {
        SubTask subTask = subTasks.remove(id);
        Epic epic = epics.get(subTask.getEpicId());
        epic.getSubTasksIds().remove(Integer.valueOf(id));
        updateEpicStatus(epic.getId());
        historyManager.remove(id);
    }

    @Override
    public ArrayList<SubTask> getSubTasksOfEpic(int epicId) {
        ArrayList<SubTask> subTasksOfEpic = new ArrayList<>();
        for (Integer subtaskId : epics.get(epicId).getSubTasksIds()) {
            subTasksOfEpic.add(subTasks.get(subtaskId));
        }
        return subTasksOfEpic;
    }


    @Override
    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            System.out.println("Такой задачи нет в списке.");
        } else {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            System.out.println("Такой задачи нет в списке.");
        } else {
            String name = epic.getName();
            String description = epic.getDescription();
            epic = epics.get(epic.getId());
            epic.setName(name);
            epic.setDescription(description);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (!subTasks.containsKey(subTask.getId())) {
            System.out.println("Такой задачи нет в списке.");
        } else {
            subTasks.put(subTask.getId(), subTask);
            Epic epic = epics.get(subTask.getEpicId());
            updateEpicStatus(epic.getId());
        }
    }

    private void updateEpicStatus(int id) {
        Epic epic = epics.get(id);
        ArrayList<Integer> subTasksId = epic.getSubTasksIds();

        if (subTasksId.isEmpty()) {
            epic.setStatus(Statuses.NEW);
            return;
        }

        Statuses status = null;
        for (Integer subId : subTasksId) {
            SubTask subTask = subTasks.get(subId);

            if (status == null) {
                status = subTask.getStatus();
                continue;
            }

            if (status == (subTask.getStatus())
                    && !(status == (Statuses.IN_PROGRESS))) {
                continue;
            }
            epic.setStatus(Statuses.IN_PROGRESS);
            return;
        }
        epic.setStatus(status);
    }
}
