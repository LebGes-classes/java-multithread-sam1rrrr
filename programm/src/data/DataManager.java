package data;

import models.*;

import java.util.ArrayList;
import java.util.Collection;

public class DataManager {
    private static ArrayList<Employee> employees = new ArrayList<>();
    private static ArrayList<Task> tasks = new ArrayList<>();

    public static void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public static void addTask(Task task) {
        tasks.add(task);
    }

    public static ArrayList<Employee> getEmployees() {
        return employees;
    }

    public static ArrayList<Task> getTasks() {
        return tasks;
    }

    public static void initialize() {
        ExcelReader.readEmployeesFromExcel();
        ExcelReader.readTasksFromExcel();
    }

}
