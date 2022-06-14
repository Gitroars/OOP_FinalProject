package com.company;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class BookForm extends Component {
    private JTable table1;
    private JPanel BookPanel;
    private JComboBox dayComboBox;
    private JComboBox monthComboBox;
    private JComboBox yearComboBox;
    private JComboBox typeComboBox;
    private JTextField cashTextField;
    private JTextField descriptionTextField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JLabel userLabel;
    private JLabel bookLabel;
    private JButton EXITButton;

    private ArrayList<Transaction> transactionArrayList = new ArrayList<>();
    private String currentFile, displayName;
    private boolean canAdd,canUpdate,canDeleteClear;

    BookForm(String textFile,Operator operator,Admin admin,Superadmin superadmin) throws FileNotFoundException {
        //Create GUI
        JFrame frame = new JFrame();
        frame.setContentPane(BookPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(720,720);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        bookLabel.setText(textFile);
        currentFile = "AppData/Books/"+textFile+".txt";
        OpenBook(currentFile);
        // Create Table of transactions
        TableModel dataTable = new TableModel(transactionArrayList);
        table1.setModel(dataTable);
        dataTable.fireTableDataChanged();
        //Determine the current user's level of authority and name
        if(operator!=null){
            displayName = "Operator "+operator.getUsername();
            canAdd = operator.isCanAdd();
            canUpdate = operator.isCanUpdate();
            canDeleteClear = operator.isCanDelete();
        }
        if(admin!=null){
            displayName = "Admin "+admin.getUsername();
            canAdd = admin.isCanAdd();
            canUpdate = admin.isCanUpdate();
            canDeleteClear = admin.isCanDelete();
        }
        if(superadmin!=null){
            displayName = "Superadmin " + superadmin.getUsername();
            canAdd = superadmin.isCanAdd();
            canUpdate = superadmin.isCanUpdate();
            canDeleteClear = superadmin.isCanDelete();
        }
        userLabel.setText(displayName); //Display the user's name
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(canAdd){
                    if(!isTextFieldEmpty()){ AddTransaction(); dataTable.fireTableDataChanged();
                        try {
                            SaveTransactions();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(canUpdate && !isTextFieldEmpty()){
                    UpdateTransaction();
                    dataTable.fireTableDataChanged();
                    try {
                        SaveTransactions();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null,"You do not have access to this command");
                }

            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(canDeleteClear){
                    DeleteTransaction();
                    dataTable.fireTableDataChanged();
                    try {
                        SaveTransactions();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null,"You do not have access to this command");
                }

            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(canDeleteClear){
                    ClearTransactions();
                    dataTable.fireTableDataChanged();
                    try {
                        SaveTransactions();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null,"You do not have access to this command");
                }
            }
        });
        EXITButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    FileForm fileForm = new FileForm(operator,admin,superadmin); //Go back to the FILE screen
                    frame.setVisible(false); //Remove current screen
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }

            }
        });
    }


    void OpenBook(String textFile) throws FileNotFoundException {
        ArrayList<Transaction> temporaryArrayList = new ArrayList<>();
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
            temporaryArrayList.add(transaction); //Put the newly created transaction into a temporary list
        }
        transactionArrayList = temporaryArrayList; //Set the current list's value to whatever the temporary list has
        for(Transaction transaction:transactionArrayList){
            System.out.println(transaction.toString());
        }
    }

    boolean isTextFieldEmpty(){
        if(cashTextField.getText().isEmpty()){
            JOptionPane.showMessageDialog(this,"Enter the amount of cash","Missing Input",JOptionPane.ERROR_MESSAGE);
            return true;
        }
        return false;
    }

    private int ComboBoxToInteger(Object comboBox){ //Convert a ComboBox to String to Integer
        return Integer.parseInt(String.valueOf(comboBox));
    }

    private int CalculateNumber(int currentDay,int currentMonth,int currentYear){
        int newNumber = 1;
        for(Transaction transaction: transactionArrayList){
            int previousYear = transaction.getYear();
            int previousMonth = transaction.getMonth();
            int previousDay = transaction.getDay();
            if(currentYear==previousYear){
                System.out.println("Same year");
                if(currentMonth==previousMonth){
                    System.out.println("Same month");
                    if(currentDay>=previousDay){
                        System.out.println("NUM++");
                        newNumber++;
                    }
                    else{
                        break;
                    }
                }
                else if(currentMonth>previousMonth){
                    System.out.println("NUM++");
                    newNumber++;
                }
                else{
                    break;
                }
            }
            else if(currentYear>previousYear){
                System.out.println("NUM++");
                newNumber++;
            }
            else{
                break;
            }
        }
        // Move

        return newNumber;
    }

    private void RecalculateNumberAndBalance(){
        int numberIndex = 1;
        double balance = 0;
        for(Transaction transaction: transactionArrayList){ //Iterate through the whole list,
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
    void AddTransaction(){
        int day = ComboBoxToInteger(dayComboBox.getSelectedItem());
        int month = ComboBoxToInteger(monthComboBox.getSelectedItem());
        int year = ComboBoxToInteger(yearComboBox.getSelectedItem());
        String date = day+"/"+month+"/"+year;
        String type = (String) typeComboBox.getSelectedItem();
        double amount = Double.parseDouble(cashTextField.getText());
        String description = descriptionTextField.getText();

        double debit = 0,credit=0;

        if(Objects.equals(type, "Debit")){debit = amount;}
        else if(Objects.equals(type, "Credit")){credit = amount;}
        Transaction transaction = new Transaction(day,month,year,date,description,debit,credit);
        if(!transactionArrayList.isEmpty()){
            int number = CalculateNumber(day,month,year);
            transaction.setNumber(number);
            int index = number-1; //index starts at zero, while numbers starts at one
            System.out.println("New transaction");
            transactionArrayList.add(index,transaction);
        }
        else {
            System.out.println("First Transaction");
            transactionArrayList.add(transaction);
        }
        RecalculateNumberAndBalance();
    }
    void UpdateTransaction(){
        int index = table1.getSelectedRow();
        if(index>-1){
            transactionArrayList.remove(index);
            int day = ComboBoxToInteger(dayComboBox.getSelectedItem());
            int month = ComboBoxToInteger(monthComboBox.getSelectedItem());
            int year = ComboBoxToInteger(yearComboBox.getSelectedItem());
            String date = day+"/"+month+"/"+year;
            String description = descriptionTextField.getText();
            String type = (String) typeComboBox.getSelectedItem();
            double amount = Double.parseDouble(cashTextField.getText());
            double debit = 0,credit=0;
            if(Objects.equals(type, "Debit")){
                debit = amount;
            }
            else if(Objects.equals(type, "Credit")){
                credit = amount;
            }
            Transaction transaction = new Transaction(day,month,year,date,description,debit,credit); //Create a new transaction according to the available data
            int number = CalculateNumber(day,month,year);
            transaction.setNumber(number);
            int newIndex = number-1; //index starts at zero, while numbers starts at one
            System.out.println("New transaction");
            System.out.println("Updated Row");
            // Remove the old transaction and add the new one
            transactionArrayList.add(newIndex,transaction);
            RecalculateNumberAndBalance();

        }
    }
    void DeleteTransaction(){
        int index = table1.getSelectedRow(); //the index of current selected row
        if(index>-1){ //It's impossible for the index to be beneath zero, thus it's safe to say the row exist for removal
            transactionArrayList.remove(index);
            RecalculateNumberAndBalance();
        }
    }
    void ClearTransactions(){
        if(!transactionArrayList.isEmpty()){
            transactionArrayList.clear();
        }
    }
    void SaveTransactions() throws IOException {
        FileWriter fileWriter = new FileWriter(currentFile);
        for(Transaction transaction: transactionArrayList){
            fileWriter.write(transaction.getNumber()+","+transaction.getDay()+","+transaction.getMonth()+","+transaction.getYear()+","+transaction.getDate()+","+transaction.getDescription()+","
                    +transaction.getDebit()+","+transaction.getCredit()+","+transaction.getBalance()+"\n");
        }
        fileWriter.close();
    }


    private static class TableModel extends AbstractTableModel{
        private ArrayList<Transaction> transactionArrayList;
        private final String[] COLUMNS = {"NO","DATE","DESCRIPTION","DEBIT","CREDIT","BALANCE"};
        private TableModel(ArrayList<Transaction> transactionArrayList){this.transactionArrayList=transactionArrayList;}

        @Override
        public int getRowCount() {
            return transactionArrayList.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMNS.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return switch (columnIndex){
                case 0 -> transactionArrayList.get(rowIndex).getNumber();
                case 1 -> transactionArrayList.get(rowIndex).getDate();
                case 2-> transactionArrayList.get(rowIndex).getDescription();
                case 3-> transactionArrayList.get(rowIndex).getDebit();
                case 4-> transactionArrayList.get(rowIndex).getCredit();
                case 5-> transactionArrayList.get(rowIndex).getBalance();
                default -> "";
            };
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNS[column];
        }

    }
}
