import java.util.*;
import services.*;
import models.*;
import utils.InputUtils;

public class Main {
    public static void main(String[] args) {
        LibraryService library = new LibraryService();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== سیستم مدیریت کتابخانه دانشگاه =====");
            System.out.println("1. ورود به عنوان مهمان");
            System.out.println("2. ورود به عنوان دانشجو");
            System.out.println("3. ورود به عنوان کارمند");
            System.out.println("4. ورود به عنوان مدیر");
            System.out.println("0. خروج");
            System.out.print("انتخاب شما: ");
            int choice = InputUtils.getInt(sc);

            switch (choice) {
                case 1 -> new GuestService(library).menu(sc);
                case 2 -> new StudentService(library).menu(sc);
                case 3 -> new EmployeeService(library).menu(sc);
                case 4 -> new AdminService(library).menu(sc);
                case 0 -> {
                    System.out.println("خروج از برنامه. موفق باشید!");
                    return;
                }
                default -> System.out.println("گزینه نامعتبر!");
            }
        }
    }
}
