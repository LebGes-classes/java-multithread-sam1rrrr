package data;

import models.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ExcelReader {
    private final static String FILE_PATH = "programm/resources/DataBase.xlsx";

    public static void readEmployeesFromExcel() {
        int id;
        String name;
        int workedHours;
        int totalHours;
        try {
            FileInputStream fls = new FileInputStream(FILE_PATH);
            XSSFWorkbook workbook = new XSSFWorkbook(fls);
            XSSFSheet sheet = workbook.getSheet("Employees");
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                id = (int) row.getCell(0).getNumericCellValue();
                name = row.getCell(1).getStringCellValue();
                workedHours = (int) row.getCell(2).getNumericCellValue();
                totalHours = (int) row.getCell(3).getNumericCellValue();
                Employee employee = new Employee(id, name);
                employee.setTaskId(-1);
                employee.setWorkedHours(workedHours);
                employee.setTotalHours(totalHours);

                DataManager.addEmployee(employee);
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static void readTasksFromExcel() {
        int id;
        String title;
        String status;
        int employeeId;
        int actualHours;
        int totalHours;

        try {
            FileInputStream fls = new FileInputStream(FILE_PATH);
            XSSFWorkbook workbook = new XSSFWorkbook(fls);
            XSSFSheet sheet = workbook.getSheet("Tasks");
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                id = (int) row.getCell(0).getNumericCellValue();
                title = row.getCell(1).getStringCellValue();
                status = row.getCell(2).getStringCellValue();
                try {
                    employeeId = (int) row.getCell(3).getNumericCellValue();
                } catch (NullPointerException e) {
                    employeeId = -1;
                }
                try {
                    actualHours = (int) row.getCell(4).getNumericCellValue();
                } catch (NullPointerException e) {
                    actualHours = 0;
                }
                totalHours = (int) row.getCell(5).getNumericCellValue();

                Task task = new Task(id, title, status, employeeId, actualHours, totalHours);
                DataManager.addTask(task);
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}

