package com.company;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;




public class KasForm {
    int groupIndex;
    Operator currentOperator;
    Admin currentAdmin;
    Superadmin currentSuperadmin;

    private JTable table1;
    private JPanel kasPanel;
    private JLabel welcomeLabel;
    private JComboBox dayComboBox;
    private JComboBox monthComboBox;
    private JComboBox yearComboBox;
    private JTextField cashTextField;
    private JComboBox typeComboBox;
    private JTextField descriptionTextField;
    private JButton addButton;
    private JButton saveButton;
    private JButton deleteButton;
    private javax.swing.JScrollPane JScrollPane;
    private JButton updateButton;
    private JButton EXITButton;
    private JTextField saveTextField;


    ArrayList<Transaction> transactionList = new ArrayList<>();
    TableModel dataTable = new TableModel(transactionList);
    // Find integer values of day,month and year
    int day,month,year;
    int number,index;


    String date,description,type;
    double amount,debit,credit,balance;




    public KasForm(String textFile,Operator operator, Admin admin, Superadmin superadmin) throws FileNotFoundException {
        OpenBook(textFile);
        table1.setModel(dataTable);



        currentOperator = operator;
        currentAdmin = admin;
        currentSuperadmin = superadmin;





        JFrame frame = new JFrame();
        frame.setContentPane(kasPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(720,720);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddTransaction();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeleteTransaction();
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UpdateTransaction();
            }
        });


        EXITButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    FileForm fileForm = new FileForm(currentOperator,currentAdmin,currentSuperadmin);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                frame.setVisible(false);
            }
        });
    }



    boolean isListEmpty(){
        return transactionList.isEmpty();
    }

    void ResetDouble(){ //reset the values to zero
        debit = 0;
        credit = 0;
        balance = 0;
    }

    void InputToData(){
        // Find integer values of day,month and year
        day = ComboBoxToInteger(dayComboBox.getSelectedItem());
        month = ComboBoxToInteger(monthComboBox.getSelectedItem());
        year = ComboBoxToInteger(yearComboBox.getSelectedItem());
        number = 1;
        index = 0;


        date = day+"/"+month+"/"+year;
        description = descriptionTextField.getText();
        type = (String) typeComboBox.getSelectedItem();

        ResetDouble();
        amount = Double.parseDouble(cashTextField.getText());
        if(type == "Debit"){
            debit = amount;
        }
        else if(type == "Credit"){
            credit = amount;
        }
    }

    void RecalculateNumberAndBalance(){
        int numberIndex = 1;
        double balance = 0;
        for(Transaction transaction: transactionList){ //Iterate through the whole list,
            transaction.setNumber(numberIndex);
            double debit = transaction.getDebit();
            double credit = transaction.getCredit();
            // Determine whether the transaction is of type debit or credit,
            if( debit> 0){ // For debit transactions, reduce the balance
                balance -= debit;
            }
            if(credit>0){ //For credit transactions, add the balance
                balance += credit;
            }
            transaction.setBalance(balance);
            numberIndex++; //increment index
        }
    }

    int CalculateNumber(int currentDay,int currentMonth,int currentYear){
        int number = 1;
        for(Transaction transaction: transactionList){
            int previousYear = transaction.getYear();
            int previousMonth = transaction.getMonth();
            int previousDay = transaction.getDay();
            if(currentYear==previousYear){
                System.out.println("Same year");
                if(currentMonth==currentMonth){
                    System.out.println("Same month");
                    if(currentDay>=previousDay){
                        System.out.println("NUM++");
                        number++;
                    }
                    else{
                        break;
                    }
                }
                else if(currentMonth>previousMonth){
                    System.out.println("NUM++");
                    number++;
                }
                else{
                    break;
                }
            }
            else if(currentYear>previousYear){
                System.out.println("NUM++");
                number++;
            }
            else{
                break;
            }
        }
        // Move

        return number;
    }



    int ComboBoxToInteger(Object comboBox){ //Convert a ComboBox to String to Integer
        return Integer.parseInt(String.valueOf(comboBox));
    }

    Transaction CreateTransaction(){ //Create a new transaction based on current data and then return it
        Transaction transaction = new Transaction(number,date,description,debit,credit,balance);
        transaction.setDay(day);
        transaction.setMonth(month);
        transaction.setYear(year);
        return transaction;
    }

    void AddTransaction(){
        boolean isListEmpty = isListEmpty();

        InputToData();



        // Determine the type of transaction, including the amount


        //Create a new transaction according to the available data
        Transaction transaction = CreateTransaction();

        if(!isListEmpty){
            number = CalculateNumber(day,month,year);
            transaction.setNumber(number);
            index = number-1;
            System.out.println("New transaction");
            transactionList.add(index,transaction);
        }
        else if(isListEmpty){
            System.out.println("First Transaction");
            transactionList.add(transaction);
        }
        RecalculateNumberAndBalance();
        dataTable.fireTableDataChanged();
    }



    void DeleteTransaction(){
        int index = table1.getSelectedRow(); //the index of current selected row
        if(index>-1){ //It's impossible for the index to be beneath zero, thus it's safe to say the row exist for removal
                transactionList.remove(index);
                RecalculateNumberAndBalance();
                dataTable.fireTableDataChanged();
        }
    }
    void UpdateTransaction(){
        InputToData();
        Transaction transaction = CreateTransaction();
        int index = table1.getSelectedRow(); //the index of current selected row
        if(index>-1){ //It's impossible for the index to be beneath zero, thus it's safe to say the row exist for update
            System.out.println("Updated Row");
            transactionList.set(index,transaction);
            RecalculateNumberAndBalance();
            dataTable.fireTableDataChanged();
            DisplayList();
        }
    }




    void SaveBook() throws IOException {
        FileWriter fileWriter = new FileWriter(saveTextField.getText());
        for(Transaction transaction: transactionList){
            fileWriter.write(transaction.getNumber()+","+transaction.getDay()+","+transaction.getMonth()+","+transaction.getYear()+","+transaction.getDate()+","+transaction.getDescription()+","
            +transaction.getDebit()+","+transaction.getCredit()+","+transaction.getBalance()+"\n");
        }
        fileWriter.close();
    }
    void OpenBook(String textFile) throws FileNotFoundException {

        ArrayList<Transaction> temporaryList = new ArrayList<>();
        //Access the text file
        File file = new File(textFile);
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()){ //Iterate through each line in the text file
            String line = scanner.nextLine();
            String[] lineArray = line.split(","); //Put the data separated by commas into array
            //Create a new transaction object and set the values according to the data from current line
            Transaction transaction = new Transaction();
            transaction.setNumber(Integer.parseInt(lineArray[0]));
            transaction.setDay(Integer.parseInt(lineArray[1]));
            transaction.setMonth(Integer.parseInt(lineArray[2]));
            transaction.setYear(Integer.parseInt(lineArray[3]));
            transaction.setDate(lineArray[4]);
            transaction.setDescription(lineArray[5]);
            transaction.setDebit(Double.parseDouble(lineArray[6]));
            transaction.setCredit(Double.parseDouble(lineArray[7]));
            transaction.setBalance(Double.parseDouble(lineArray[8]));
            temporaryList.add(transaction); //Put the newly created transaction into a temporary list
        }
        transactionList = temporaryList; //Set the current list's value to whatever the temporary list has

    }
    void DisplayList(){
        for(Transaction transaction:transactionList){
            System.out.println(transaction.getBalance());
        }
    }




    private static class TableModel extends AbstractTableModel{
    private final String[] COLUMNS = {"NO","DATE","DESCRIPTION","DEBIT","CREDIT","BALANCE"};
    private ArrayList<Transaction> transactions;
    private TableModel(ArrayList<Transaction> transactions){
        this.transactions = transactions;
    }


    @Override
    public int getRowCount() {
        return transactions.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return switch(columnIndex){
            case 0 -> transactions.get(rowIndex).getNumber();
            case 1 -> transactions.get(rowIndex).getDate();
            case 2-> transactions.get(rowIndex).getDescription();
            case 3-> transactions.get(rowIndex).getDebit();
            case 4-> transactions.get(rowIndex).getCredit();
            case 5-> transactions.get(rowIndex).getBalance();
            default->"";
        };
    }

    @Override
    public String getColumnName(int column) {return COLUMNS[column];}


}












}
