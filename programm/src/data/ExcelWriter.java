package data;

import models.Employee;
import models.Task;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelWriter {
    private final static String FILE_PATH = "programm/resources/DataBase.xlsx";

    private static XSSFWorkbook loadWorkbook() throws IOException {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                return new XSSFWorkbook(fis);
            }
        } else {
            return new XSSFWorkbook();
        }
    }

    public static void updateTasks() {
        try (XSSFWorkbook workbook = loadWorkbook()) {
            int index = workbook.getSheetIndex("Tasks");
            if (index != -1) {
                workbook.removeSheetAt(index);
            }

            XSSFSheet sheet = workbook.createSheet("Tasks");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("title");
            headerRow.createCell(2).setCellValue("status");
            headerRow.createCell(3).setCellValue("employee_id");
            headerRow.createCell(4).setCellValue("actual_hours");
            headerRow.createCell(5).setCellValue("total_hours");

            int rowNum = 1;
            for (Task task : DataManager.getTasks()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(task.getId());
                row.createCell(1).setCellValue(task.getTitle());
                if (task.getActualHours() != 0) {
                    row.createCell(2).setCellValue(task.getStatus());
                    row.createCell(3).setCellValue(task.getEmployeeId());
                } else {
                    row.createCell(2).setCellValue("not_started");
                    row.createCell(3).setCellValue(-1);
                }

                row.createCell(4).setCellValue(task.getActualHours());
                row.createCell(5).setCellValue(task.getTotalHours());
            }
            try (FileOutputStream outputStream = new FileOutputStream(FILE_PATH)) {
                workbook.write(outputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateEmployees() {
        try (XSSFWorkbook workbook = loadWorkbook()) {
            int index = workbook.getSheetIndex("Employees");
            if (index != -1) {
                workbook.removeSheetAt(index);
            }

            XSSFSheet sheet = workbook.createSheet("Employees");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("name");
            headerRow.createCell(2).setCellValue("worked_hours");
            headerRow.createCell(3).setCellValue("total_hours");
            headerRow.createCell(4).setCellValue("kpi");

            int rowNum = 1;
            for (Employee employee : DataManager.getEmployees()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(employee.getId());
                row.createCell(1).setCellValue(employee.getName());
                row.createCell(2).setCellValue(employee.getWorkedHours() + employee.getWorkedHoursToday());
                row.createCell(3).setCellValue(employee.getTotalHours() + 8);
                row.createCell(4).setCellValue(((double) (employee.getWorkedHours() + employee.getWorkedHoursToday()) / (employee.getTotalHours() + 8)) * 100);
            }
            try (FileOutputStream outputStream = new FileOutputStream(FILE_PATH)) {
                workbook.write(outputStream);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
