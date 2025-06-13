import data.DataManager;
import ui.Menu;

import static data.ExcelWriter.updateEmployees;
import static data.ExcelWriter.updateTasks;

public class Main {
    public static void main(String[] args) {
        DataManager.initialize();

        Menu menu = new Menu();
        menu.start();

        updateTasks();
        updateEmployees();
    }
}