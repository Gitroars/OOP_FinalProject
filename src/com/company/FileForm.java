package com.company;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class FileForm {

    private JTable table1;
    private JButton newButton;
    private JButton openButton;
    private JTextField newTextField;
    private JButton renameButton;
    private JPanel FilePanel;
    private JButton deleteButton;
    private JLabel usernameLabel;
    private JButton EXITButton;

    ArrayList<String> fileList = new ArrayList<>();
    TableModel dataTable = new TableModel(fileList);

    Operator currentOperator;
    Admin currentAdmin;
    Superadmin currentSuperadmin;
    String currentUsername;
    private boolean canAdd,canUpdate,canDeleteClear;


    public FileForm(Operator operator,Admin admin,Superadmin superadmin) throws FileNotFoundException {
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
            canAdd = operator.isCanAdd();
            canUpdate = operator.isCanUpdate();
            canDeleteClear = operator.isCanDelete();
            usernameLabel.setText("Operator " + currentUsername); //Display the username
        }
        else if(admin!=null){
            currentUsername = admin.getUsername();
            canAdd = admin.isCanAdd();
            canUpdate = admin.isCanUpdate();
            canDeleteClear = admin.isCanDelete();
            usernameLabel.setText("Admin " + currentUsername); //Display the username
        }
        else if(superadmin!=null){
            currentUsername = superadmin.getUsername();
            canAdd = superadmin.isCanAdd();
            canUpdate = superadmin.isCanUpdate();
            canDeleteClear = superadmin.isCanDelete();
            usernameLabel.setText("Superadmin " + currentUsername); //Display the username
        }


        setFileList();

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
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        renameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    RenameFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DeleteFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        EXITButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuForm menuForm = new MenuForm(operator,admin,superadmin);
                frame.setVisible(false);
            }
        });
    }

    String getFileName(){
        return newTextField.getText();
    }

    void setFileList() throws FileNotFoundException { //Get a list of all existing files from a text file and add them to the array list
        File keysFile = new File("AppData/Books.txt");
        Scanner scanner = new Scanner(keysFile);
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] lineArray = line.split(",");
            fileList.add(lineArray[0]);
        }
    }
    void SaveFileNames() throws IOException {
        FileWriter fileWriter = new FileWriter("AppData/Books.txt");
        for(String file: fileList){
            fileWriter.write(file+"\n");
        }
        fileWriter.close();
    }


    void NewFile() throws IOException {
        String fileName = getFileName(); // The name is from user input field
        if(canAdd && !Objects.equals(fileName, "")){
            if(!fileList.contains(fileName)){ //As long as the name isn't used yet,
                fileList.add(fileName); // add to the arraylist
                // Create a file with that name
                File file = new File("AppData/Books/"+fileName+".txt");
                if(file.createNewFile()){
                    System.out.println("File created: " + fileName);
                    dataTable.fireTableDataChanged();
                    SaveFileNames();
                }
            }
        }
        else if(Objects.equals(getFileName(), "")){
            JOptionPane.showMessageDialog(null,"Please enter a name for the new file");
        }
        else{
            JOptionPane.showMessageDialog(null,"You do not have access to this command");
        }

    }

    boolean OpenFile() throws IOException {
        int index = table1.getSelectedRow(); // get index of current row
        if(index>-1){  //It's impossible for the index to be beneath zero, thus it's safe to say the row exist
            String chosenFile = fileList.get(index);
            BookForm bookForm = new BookForm(chosenFile,currentOperator,currentAdmin,currentSuperadmin);
            SaveFileNames();
            return true;
        }
        return  false;
    }
    void RenameFile() throws IOException {
        String fileName = getFileName(); // The name is from user input field
        if(canUpdate && !Objects.equals(fileName, "")){
            int index = table1.getSelectedRow();
            if(index>-1){
                // REFERENCE: https://www.geeksforgeeks.org/java-program-to-rename-a-file/
                //Get the name of old file from list and new name from input field
                File oldFile = new File("AppData/Books/"+fileList.get(index)+".txt");
                File newFile = new File("AppData/Books/"+fileName+".txt");
                oldFile.renameTo(newFile); //Rename the file accordingly
                fileList.set(index,getFileName()); //Replace the file's name with the new one in the list
                dataTable.fireTableDataChanged(); //Update the table's content to be displayed
                SaveFileNames();
            }
        }
        else if(Objects.equals(getFileName(), "")){
            JOptionPane.showMessageDialog(null,"Name is empty");
        }
        else{
            JOptionPane.showMessageDialog(null,"You do not have access to this command");
        }

    }

    void DeleteFile() throws IOException {
        if(canDeleteClear){ //Given sufficient permission authority,
            int index = table1.getSelectedRow(); //get the seleced row's index
            if(index>-1){ //Check if a row does exist
                String fileName = fileList.get(index); //get the selected file's name
                String pathName = "AppData/Books/"+fileName+".txt";
                File file = new File(pathName); //set the value of the file's path location
                if(file.delete()){ //If file is deleted, remove it from the list as well
                    fileList.remove(index);
                    dataTable.fireTableDataChanged();
                    SaveFileNames();
                }
                else{
                    System.out.println("File Deletion Fail");
                }
            }
        }
        else{
            JOptionPane.showMessageDialog(null,"You do not have access to this command");
        }

    }
    private static class TableModel extends AbstractTableModel {
        private final String[] COLUMNS = {"FILE NAME"}; //the column header's name
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
        public String getColumnName(int column) {return COLUMNS[column];} //name the column header

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) { //Display the file names at table
            return switch(columnIndex){
                case 0 -> fileList.get(rowIndex);
                default->"";
            };
        }


    }

}
