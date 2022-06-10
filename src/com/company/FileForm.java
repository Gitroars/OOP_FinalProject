package com.company;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class FileForm {

    private JTable table1;
    private JButton newButton;
    private JButton openButton;
    private JTextField newTextField;
    private JButton renameButton;
    private JPanel FilePanel;
    private JButton deleteButton;
    private JLabel usernameLabel;
    private JButton LOGOUTButton;

    ArrayList<String> fileList = new ArrayList<>();
    TableModel dataTable = new TableModel(fileList);

    Operator currentOperator;
    Admin currentAdmin;;
    Superadmin currentSuperadmin;
    String currentUsername;

    public FileForm(Operator operator,Admin admin,Superadmin superadmin){
        //Create the GUI and manage the settings
        JFrame frame = new JFrame();
        frame.setContentPane(FilePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        table1.setModel(dataTable); // The table's model will be based off data table
        // Set the current groups' data value from parameter input
        currentOperator = operator;
        currentAdmin = admin;
        currentSuperadmin = superadmin;

        //Get the username from logged in account, which won't have null value
        if(operator!=null){
            currentUsername = operator.getUsername();
        }
        if(admin!=null){
            currentUsername = admin.getUsername();
        }
        if(superadmin!=null){
            currentUsername = superadmin.getUsername();
        }
        usernameLabel.setText("Hello " + currentUsername); //Display the username

        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    NewFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    boolean isOpenSuccessful = OpenFile();
                    if (isOpenSuccessful) {frame.setVisible(false);} //Close File Form on successful opening
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
        renameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RenameFile();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeleteFile();
            }
        });
        LOGOUTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginForm loginForm = new LoginForm();
                frame.setVisible(false);
            }
        });
    }

    private ArrayList<String> getFileList(){
        return fileList;
    }
    String getFileName(){
        return newTextField.getText();
    }



    void NewFile() throws IOException {
        String fileName = getFileName(); // The name is from user input field
        if(!fileList.contains(fileName)){ //As long as the name isn't used yet,
            fileList.add(fileName); // add to the arraylist
            // Create a file with that name
            File file = new File(fileName);
            if(file.createNewFile()){
                System.out.println("File created: " + fileName);
                dataTable.fireTableDataChanged();
            }
        }
    }


    boolean OpenFile() throws FileNotFoundException {
        int index = table1.getSelectedRow(); // get index of current row
        if(index>-1){  //It's impossible for the index to be beneath zero, thus it's safe to say the row exist
            String chosenFile = fileList.get(index);
            KasForm kasForm = new KasForm(chosenFile,currentOperator,currentAdmin,currentSuperadmin);
            return  true;
        }
        return  false;
    }
    void RenameFile(){
        int index = table1.getSelectedRow();
        if(index>-1){
            // REFERENCE: https://www.geeksforgeeks.org/java-program-to-rename-a-file/
            //Get the name of old file from list and new name from input field
            File oldFile = new File(fileList.get(index)+".txt");
            File newFile = new File(getFileName()+".txt");
            oldFile.renameTo(newFile); //Rename the file accordingly
            fileList.set(index,getFileName()); //Replace the file's name with the new one in the list
            dataTable.fireTableDataChanged(); //Update the table's content to be displayed
        }
    }

    void DeleteFile(){
        int index = table1.getSelectedRow();
        if(index>-1){
            fileList.remove(index);
            dataTable.fireTableDataChanged();
        }
    }

    private static class TableModel extends AbstractTableModel {
        private final String[] COLUMNS = {"FILE NAME"}; //the column header
        private ArrayList<String> fileList;
        private TableModel(ArrayList<String> fileList){
            this.fileList = fileList;
        }


        @Override
        public int getRowCount() {
            return fileList.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMNS.length;
        }



        @Override
        public String getColumnName(int column) {return COLUMNS[column];}

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return null;
        }


    }

}
