import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;

public class DataReader {
    private static HashMap <String, Integer> currentBillsMap = new HashMap<>();
    private static String negativeTransactionSumMessage = " ошибка во время перевода, неверная сумма транзакции";
    private static String invalidBillMessage =  " ошибка во время перевода, неверный номер счета";
    private static String notEnoughMoneyMessage = " ошибка во время перевода, недостаточно средств";
    private static String successfulTransactionMessage = " успешно обработан";

    public static void setCurrentBills() {
        File currentBillsFile = new File("Data/Current Bills");
        try (BufferedReader currentDataReader = new BufferedReader(new FileReader(currentBillsFile))){
            for (;;){
                String line = currentDataReader.readLine();
                if (line == null){
                    break;
                }
                String [] dataSet = line.split("\\|");
                String number = dataSet[0].trim();
                Integer balance = Integer.parseInt(dataSet[1].trim());
                if (!number.equals("") && balance >= 0) {
                    currentBillsMap.put(number, balance);
                } else if (balance < 0){
                    throw new NegativeBalanceExeption();
                }
            }
        } catch (IOException | NegativeBalanceExeption e){
            System.out.println(e);
        }
    }

    public static void parsBills() {
        try (BufferedReader inputReader = new BufferedReader(new FileReader("Data/Input"));
             FileWriter reportWriter = new FileWriter("Data/Report", true)){
            for (;;) {
                String line = inputReader.readLine();
                if (line == null){
                    break;
                }
                String [] dataSet = line.split("\\|");
                if (Integer.parseInt(dataSet[2]) < 0) { //Проверка на неотрицательную сумму перевода
                    String reportLine = LocalDateTime.now() + " " + line;
                    reportWriter.write(reportLine + negativeTransactionSumMessage + "\n");
                } else if (!currentBillsMap.containsKey(dataSet[0]) || !currentBillsMap.containsKey(dataSet[1]) ) { //проаерка на коректные номера счетов
                    String reportLine = LocalDateTime.now() + " " + line;
                    reportWriter.write(reportLine + invalidBillMessage + "\n");
                } else if (Integer.parseInt(dataSet[2]) > currentBillsMap.get(dataSet[0])) { //проверка на наличие средств для перевода
                    String reportLine = LocalDateTime.now() + " " + line;
                    reportWriter.write(reportLine + notEnoughMoneyMessage + "\n");
                } else {
                    currentBillsMap.replace(dataSet[0], currentBillsMap.get(dataSet[0]) - (Integer.parseInt(dataSet[2])));
                    currentBillsMap.replace(dataSet[1], currentBillsMap.get(dataSet[1]) + (Integer.parseInt(dataSet[2])));
                    String reportLine = LocalDateTime.now() + " " + line;
                    reportWriter.write(reportLine + successfulTransactionMessage + "\n");
                }
            }
            reportWriter.flush();
        } catch (IOException e){
            System.out.println(e);
        }
        /*try {
            Path temp = Files.move(Paths.get("Data/Input"),
                    Paths.get("Busket/Used Input"));
        } catch (IOException e){
            System.out.println(e);
        }*/
    }

    public static HashMap<String, Integer> getCurrentBillsMap() {
        return currentBillsMap;
    }
}
