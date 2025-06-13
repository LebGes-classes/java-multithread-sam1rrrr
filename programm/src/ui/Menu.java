package ui;

import data.DataManager;
import models.Employee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static data.ExcelWriter.updateTasks;

public class Menu {
    private static final Scanner scanner = new Scanner(System.in);

    public static void start() {
        System.out.println();
        System.out.println("Нажмите Enter, чтобы начать рабочий день:");
        scanner.nextLine();

        ArrayList<Employee> employees = DataManager.getEmployees();
        ExecutorService executor = Executors.newFixedThreadPool(employees.size());
        
        for (Employee employee : employees) {
            executor.execute(employee);
        }
        executor.shutdown();

        try {
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
