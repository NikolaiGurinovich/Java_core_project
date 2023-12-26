package WorkWithFiles;

import WorkWithFiles.DataReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
public class DataWriter {

    public static void writeCurrentBills(){
        try (FileWriter currentBillsWriter = new FileWriter("Data/Current Bills", false)){
            for (String key : DataReader.getCurrentBillsMap().keySet()){
                String line = key +"|"+DataReader.getCurrentBillsMap().get(key);
                currentBillsWriter.write(line + "\n");
            }
        } catch (IOException e){
            System.out.println(e);
        }
    }

    public static StringBuilder printReport() {
        StringBuilder report = new StringBuilder();
        try (BufferedReader reportReader = new BufferedReader(new FileReader("Data/Report"))){
            for (;;){
                String line = reportReader.readLine();
                if (line == null){
                    break;
                }
                report.append(line + "\n") ;
            }
        } catch (IOException e){
            System.out.println(e);
        }
        return report;
    }
}
