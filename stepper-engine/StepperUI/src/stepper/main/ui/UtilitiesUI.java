package stepper.main.ui;

import java.util.Scanner;

public class UtilitiesUI {
    public static int getNumberFromUser(Scanner scanner)throws  NumberFormatException, IndexOutOfBoundsException {
        int inputNumber;
        String s = scanner.next();

        try {
            inputNumber = Integer.parseInt(s);
        } catch (Exception e) {
            inputNumber = Integer.parseInt(s.substring(0, s.length() - 1));
        }
        return inputNumber;
    }
    public static double getDoubleFromUser(Scanner scanner)throws  NumberFormatException, IndexOutOfBoundsException {
        double inputNumber;
        String s = scanner.next();

        try {
            inputNumber = Double.parseDouble(s);
        } catch (Exception e) {
            inputNumber = Double.parseDouble(s.substring(0, s.length() - 1));
        }
        return inputNumber;
    }
}
