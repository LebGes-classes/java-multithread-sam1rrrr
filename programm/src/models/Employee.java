package models;

import data.DataManager;
import data.ExcelReader;
import data.ExcelWriter;

import javax.xml.crypto.Data;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Employee implements Runnable {
    private int id;
    private String name;
    private int taskId;
    private int workedHoursToday;
    private int workedHours;
    private int totalHours;

    public Employee() {}

    public Employee(int id, String name) {
        this.id = id;
        this.name = name;
        this.taskId = -1;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getTaskId() { return taskId; }
    public int getWorkedHoursToday() { return workedHoursToday; }
    public int getWorkedHours() { return workedHours; }
    public int getTotalHours() { return totalHours; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setTaskId(int taskId) { this.taskId = taskId; }
    public void setWorkedHoursToday(int workedHoursToday) { this.workedHoursToday = workedHoursToday; }
    public void setWorkedHours(int workedHours) { this.workedHours = workedHours; }
    public void setTotalHours(int totalHours) { this.totalHours = totalHours; }

    // короче перепиши логику
    // вместо hashmap arraylist
    public void startDay() throws InterruptedException {
        this.workedHoursToday = 0;

        System.out.println(this.getName() + " начал рабочий день");
        int hours = 0;

        Task currentTask = findAssignedTask();
        if (currentTask == null) {
            currentTask = findAvailableTask();
            if (currentTask != null) {
                assignTask(currentTask);
            }
        }

        while (currentTask != null && hours  < 8) {
            Task task = currentTask;

            int taskTime = Math.min(task.getTotalHours() - task.getActualHours(), 8 - hours);

            if (taskTime <= 0) break;
            if (hours + taskTime > 8) {
                break;
            }
            System.out.println(this.getName() + " работает над задачей №" + currentTask.getId() + " (" + taskTime + " часов)");

            taskTime = Math.min(task.getTotalHours() - task.getActualHours(), 8 - hours);

            TimeUnit.SECONDS.sleep(taskTime);
            hours += taskTime;

            synchronized (Task.class) {
                task.setActualHours(task.getActualHours() + taskTime);
                if (task.getActualHours() == task.getTotalHours()) {
                    task.setStatus("completed");
                    System.out.println(this.getName() + " завершил задачу №" + task.getId());
                    this.taskId = -1;
                } else {
                    task.setStatus("started");
                }
            }

            if (currentTask.getStatus().equals("completed") && hours < 8) {
                currentTask = findAvailableTask();
                if (currentTask != null) {
                    assignTask(currentTask);
                }
            }
        }

        synchronized (this) {
            this.workedHoursToday = hours;
        }

        System.out.println(this.getName() + " завершил рабочий день (отработано " + hours + " часов)");
    }

    private Task findAssignedTask() {
        synchronized (Task.class) {
            for (Task task : DataManager.getTasks()) {
                if (task.getEmployeeId() == this.id && !task.getStatus().equals("completed")) {
                    return task;
                }
            }
        }
        return null;
    }

    private Task findAvailableTask() {
        synchronized (Task.class) {
            for (Task task : DataManager.getTasks()) {
                if (task.getStatus().equals("not_started") && task.getEmployeeId() == -1) {
                    return task;
                }
            }
        }
        return null;
    }

    private void assignTask(Task task) {
        synchronized (Task.class) {
            task.setEmployeeId(this.id);
            task.setStatus("started");
            this.taskId = task.getId();
            System.out.println(this.getName() + " взял задачу №" + task.getId());
        }
    }

    @Override
    public void run() {
        try {
            startDay();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
