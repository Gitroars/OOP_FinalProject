package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files


public class RegistrationForm extends JDialog {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton loginButton;
    private JTextField keyField;
    private JPanel registrationPanel;

    String operatorKeysTextFile = "operatorKeys.txt";
    String adminKeysTextFile = "adminKeys.txt";
    String superadminKeysTextFile = "superadminKeys.txt";

    String operatorAccountsTextFile = "operatorAccounts.txt";
    String adminAccountsTextFile = "adminAccounts.txt";
    String superadminAccountsTextFile = "superadminAccounts.txt";

    public String getOperatorAccountsTextFile(){return operatorAccountsTextFile;}
    public String getAdminAccountsTextFile(){return adminAccountsTextFile;}
    public String getSuperadminAccountsTextFile(){return superadminAccountsTextFile;}

    ArrayList<String> operatorKeys = new ArrayList<>();
    ArrayList<String> adminKeys= new ArrayList<>();
    ArrayList<String> superadminKeys = new ArrayList<>();

    ArrayList<Operator> operatorArrayList = new ArrayList<>();
    ArrayList<Admin> adminArrayList = new ArrayList<>();
    ArrayList<Superadmin> superadminArrayList = new ArrayList<>();

    public ArrayList<Operator> getOperatorArrayList(){return operatorArrayList;}
    public ArrayList<Admin> getAdminArrayList(){return adminArrayList;}
    public ArrayList<Superadmin> getSuperadminArrayList(){return superadminArrayList;}



    public RegistrationForm(boolean isVisible) {
        if(isVisible){
            JFrame frame = new JFrame();
            frame.setContentPane(registrationPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setSize(500, 500);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }


        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    registerUser();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private ArrayList<String> getKeys(String textFile) throws FileNotFoundException {
        ArrayList<String> keysArrayList =new ArrayList<>();
        File keysFile = new File(textFile);
        Scanner scanner = new Scanner(keysFile);
        while(scanner.hasNextLine()){
            String key = scanner.nextLine();
            keysArrayList.add(key);
        }
        return keysArrayList;
    }
    private void setKeys(ArrayList<String> keysArrayList, String textFile) throws IOException {
        FileWriter fileWriter = new FileWriter(textFile);
        for(String key:keysArrayList){
            fileWriter.write(key+"\n");
        }
        fileWriter.close();
    }
    private void saveAccount(int groupIndex, String textFile) throws IOException {
        FileWriter fileWriter = new FileWriter(textFile);
        switch (groupIndex){
            case 0:
                for(Operator operator: operatorArrayList){
                    fileWriter.write(operator.toString());
                }
                fileWriter.close();
                break;
            case 1:
                for(Admin admin: adminArrayList){
                    fileWriter.write(admin.toString());
                }
                fileWriter.close();
                break;
            case 2:
                for(Superadmin superadmin: superadminArrayList){
                    fileWriter.write(superadmin.toString());
                }
                fileWriter.close();
                break;
            default: break;
        }
    }


    private void registerUser() throws IOException {
        operatorKeys = getKeys(operatorKeysTextFile);
        adminKeys = getKeys(adminKeysTextFile);
        superadminKeys = getKeys(superadminKeysTextFile);

        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());
        String key = keyField.getText();

        if(username.isEmpty()||password.isEmpty()||key.isEmpty()){
            JOptionPane.showMessageDialog(this,"Enter all fields","Missing Input",JOptionPane.ERROR_MESSAGE);
            return;
        }
        else{
            if(operatorKeys.contains(key)){
                System.out.println("Valid Operator Key");
                operatorKeys.remove(key);
                setKeys(operatorKeys,operatorKeysTextFile);
                Operator operator = new Operator(username,password);
                operatorArrayList.add(operator);
                saveAccount(0,operatorAccountsTextFile);
            }
            else if(adminKeys.contains(key)){
                System.out.println("Valid Admin Key");
                adminKeys.remove(key);
                setKeys(adminKeys,adminKeysTextFile);
                Admin admin = new Admin(username,password);
                adminArrayList.add(admin);
                saveAccount(1,adminAccountsTextFile);
            }
            else if(superadminKeys.contains(key)){
                System.out.println("Valid Superadmin Key");
                superadminKeys.remove(key);
                setKeys(superadminKeys,superadminKeysTextFile);
                Superadmin superadmin = new Superadmin(username,password);
                superadminArrayList.add(superadmin);
                saveAccount(2,superadminAccountsTextFile);
            }
            else{
                System.out.println("Invalid key");
            }

        }





    }





}

