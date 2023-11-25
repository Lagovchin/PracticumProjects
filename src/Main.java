import domain.Statuses;
import domain.Task;
import domain.Epic;
import domain.SubTask;
import manager.*;

import java.sql.SQLOutput;


public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Убраться", "Помыть пол в зале", Statuses.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("Заняться спортом", "Отжаться 15 раз", Statuses.NEW);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Помочь родителям", "В субботу прийти к родителям и помочь");
        taskManager.addEpic(epic1);
        Epic epic2 = new Epic("Дом", "Помочь по дому");
        taskManager.addEpic(epic2);


        SubTask subTask1 = new SubTask(epic1.getId(),  "Газон", "Подстричь газон", Statuses.NEW);
        taskManager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask(epic1.getId(),  "Помочь маме", "Полить огород", Statuses.NEW);
        taskManager.addSubTask(subTask2);
        SubTask subTask3 = new SubTask(epic1.getId(), "Сделать  ремонт", "Поменять смеситель", Statuses.NEW);
        taskManager.addSubTask(subTask3);

        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getSubTaskById(subTask3.getId());
        taskManager.getEpicById(epic2.getId());
        System.out.println(taskManager.getHistory());
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        System.out.println(taskManager.getHistory());
        taskManager.getSubTaskById(subTask2.getId());
        taskManager.getSubTaskById(subTask3.getId());
        taskManager.getSubTaskById(subTask1.getId());
        taskManager.getEpicById(epic1.getId());
        System.out.println(taskManager.getHistory());
        taskManager.deleteTaskById(task1.getId());
        System.out.println(taskManager.getHistory());
        taskManager.deleteEpicById(epic1.getId());
        System.out.println(taskManager.getHistory());
        
















    }
}