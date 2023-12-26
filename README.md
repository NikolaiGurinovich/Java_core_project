To start work you should run program and choose one of two options: enter "1" in console (to parse bills) or enter "2" (to print report).
For work with the Files program has cataloger "WorkWithFiles" that has two classes: "DataReader" and "DataWriter". 
All fields and methods in this classes are static so they do his work without making an objects.
Class "DataReader" has static HashMap "currentBillsMap" so when user want to parse bills method "setCurrentBills" take data from file "Current Bills" and places it in HashMap.
Then method "parseBills" take parsing info from all .txt files of "Input" cataloge, check is this info correct, 
if there is any trouble method throw exception and write info about parsing in the "ReportFile" and if info has no mistakes method changes the bill balances in the HashMap
After that method "writeCurrentBills" from "DataWriter" class update info in "Current Bills" file. 
If user chooses print report (enter "2" in console) main calls method "printReport" from "DataWriter" class move text from file to StringBuilder and print it.

File "Classes Diagram" has diagram which represents classes relation in this program