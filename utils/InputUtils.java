package utils;

import java.util.*;

public class InputUtils {
    public static int getInt(Scanner sc) {
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            return -1;
        }
    }
}
