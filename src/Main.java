import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int num = scanner.nextInt();
        final int parseNum = 1;
        final int reportNum = 2;
        if (num == parseNum){
            DataReader.setCurrentBills();
            DataReader.parsBills();
            DataWriter.writeCurrentBills();
        } else if (num == reportNum) {
            System.out.println(DataWriter.printReport());
        } else {
            System.out.println("Введено неверное значение");
        }
    }
}