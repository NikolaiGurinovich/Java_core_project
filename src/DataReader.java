import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;

public class DataReader {
    private static HashMap<String, Integer> currentBillsMap = new HashMap<>();

    private static String billRegex = "^[0-9]{5}-[0-9]{5}$";
    private static String negativeTransactionSumMessage = " ошибка во время перевода, неверная сумма транзакции";
    private static String invalidBillMessage = " ошибка во время перевода, неверный номер счета";
    private static String notEnoughMoneyMessage = " ошибка во время перевода, недостаточно средств";
    private static String successfulTransactionMessage = " успешно обработан";

    public static void setCurrentBills() {
        File currentBillsFile = new File("Data/Current Bills");
        try (BufferedReader currentDataReader = new BufferedReader(new FileReader(currentBillsFile))) {
            for (; ; ) {
                String line = currentDataReader.readLine();
                if (line == null) {
                    break;
                }
                String[] dataSet = line.split("\\|");
                String billNumber = dataSet[0].trim();
                Integer balance = Integer.parseInt(dataSet[1].trim());
                if (billNumber.matches(billRegex) && balance >= 0) {
                    currentBillsMap.put(billNumber, balance);
                } else if (balance < 0) {
                    throw new NegativeBalanceExeption();
                } else if (!billNumber.matches(billRegex)) {
                    throw new InvalidBillException();
                }
            }
        } catch (IOException | NegativeBalanceExeption | InvalidBillException e) {
            System.out.println(e);
        }
    }

    public static void parsBills() {
        File inputDirectory = new File("Data/Input");
        File[] inputList = inputDirectory.listFiles();
        for (int i = 0; i < inputList.length; i++) {
            if (inputList[i].getName().endsWith(".txt")) {
                try (BufferedReader inputReader = new BufferedReader(new FileReader(inputList[i].getAbsolutePath()));
                     FileWriter reportWriter = new FileWriter("Data/Report", true)) {
                    for (; ; ) {
                        String line = inputReader.readLine();
                        if (line == null) {
                            break;
                        }
                        String[] dataSet = line.split("\\|");
                        if (Integer.parseInt(dataSet[2]) < 0) { //Проверка на неотрицательную сумму перевода
                            String reportLine = LocalDateTime.now() + " " + line;
                            reportWriter.write(reportLine + negativeTransactionSumMessage + "\n");
                            throw new NegativeTransactionSumException();
                        } else if (!currentBillsMap.containsKey(dataSet[0]) || !currentBillsMap.containsKey(dataSet[1])) { //проаерка на коректные номера счетов
                            String reportLine = LocalDateTime.now() + " " + line;
                            reportWriter.write(reportLine + invalidBillMessage + "\n");
                            throw new InvalidBillException();
                        } else if (Integer.parseInt(dataSet[2]) > currentBillsMap.get(dataSet[0])) { //проверка на наличие средств для перевода
                            String reportLine = LocalDateTime.now() + " " + line;
                            reportWriter.write(reportLine + notEnoughMoneyMessage + "\n");
                            throw new NotEnoughMoneyException();
                        } else {
                            currentBillsMap.replace(dataSet[0], currentBillsMap.get(dataSet[0]) - (Integer.parseInt(dataSet[2])));
                            currentBillsMap.replace(dataSet[1], currentBillsMap.get(dataSet[1]) + (Integer.parseInt(dataSet[2])));
                            String reportLine = LocalDateTime.now() + " " + line;
                            reportWriter.write(reportLine + successfulTransactionMessage + "\n");
                        }
                    }
                    reportWriter.flush();
                } catch (IOException | NegativeTransactionSumException | InvalidBillException | NotEnoughMoneyException e) {
                    System.out.println(e);
                }
                    inputList[i].renameTo(new File("Bucket/" + inputList[i].getName()));
            }
        }
    }
    public static HashMap<String, Integer> getCurrentBillsMap () {
        return currentBillsMap;
    }
}
