package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner; // Import the Scanner class to read text files


public class RegistrationForm extends JDialog {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton loginButton;
    private JTextField keyField;
    private JPanel registrationPanel;
    // Location Path of Serial Keys text file
    private final String operatorKeysTextFile = "AppData/Accounts/operatorKeys.txt";
    private final String adminKeysTextFile = "AppData/Accounts/adminKeys.txt";
    private final String superadminKeysTextFile = "AppData/Accounts/superadminKeys.txt";
    // Location path of Accounts text file
    private final String operatorAccountsTextFile = "AppData/Accounts/operatorAccounts.txt";
    private final String adminAccountsTextFile = "AppData/Accounts/adminAccounts.txt";
    private final String superadminAccountsTextFile = "AppData/Accounts/superadminAccounts.txt";
   //Getter of location path of accounts
    public String getOperatorAccountsTextFile(){return operatorAccountsTextFile;}
    public String getAdminAccountsTextFile(){return adminAccountsTextFile;}
    public String getSuperadminAccountsTextFile(){return superadminAccountsTextFile;}

    private ArrayList<String> operatorKeys = new ArrayList<>();
    private ArrayList<String> adminKeys= new ArrayList<>();
    private ArrayList<String> superadminKeys = new ArrayList<>();

    private HashMap<String,ArrayList<String>> operatorMap = new HashMap<String,ArrayList<String>>();
    private HashMap<String,ArrayList<String>> adminMap = new HashMap<String,ArrayList<String>>();
    private HashMap<String,ArrayList<String>> superadminMap = new HashMap<String,ArrayList<String>>();
    private ArrayList<Operator> operatorArrayList = new ArrayList<>();
    private ArrayList<Admin> adminArrayList = new ArrayList<>();
    private ArrayList<Superadmin> superadminArrayList = new ArrayList<>();


    public RegistrationForm(boolean isVisible) throws FileNotFoundException {
        JFrame frame = new JFrame();
        if(isVisible){
            frame.setContentPane(registrationPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setSize(300, 300);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
        // Read data from text file, then convert it to hashmap, then to array list
        operatorMap = getAccounts(operatorAccountsTextFile); operatorArrayList = getOperatorAccounts(operatorMap);
        adminMap = getAccounts(adminAccountsTextFile); adminArrayList = getAdminAccounts(adminMap);
        superadminMap = getAccounts(superadminAccountsTextFile); superadminArrayList = getSuperadminAccounts(superadminMap);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    boolean isRegisterSuccess = registerUser();
                    if(isRegisterSuccess){ //Confirming the registration is a success, return to the Login Form Page and close registration screen
                        LoginPage();
                        frame.setVisible(false);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    LoginPage(); frame.setVisible(false); //Open login screen and close registration screen
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }

            }


        });
    }
    void LoginPage() throws FileNotFoundException {
        LoginForm loginForm = new LoginForm(true); //open login screen
    }
    private ArrayList<String> getKeys(String textFile) throws FileNotFoundException { //Read a key text file and add each key to the arraylist of keys
        ArrayList<String> keysArrayList =new ArrayList<>();
        File keysFile = new File(textFile);
        Scanner scanner = new Scanner(keysFile);
        while(scanner.hasNextLine()){
            String key = scanner.nextLine();
            keysArrayList.add(key);
        }
        return keysArrayList;
    }
    private void setKeys(ArrayList<String> keysArrayList, String textFile) throws IOException { //Write the key tet file according to current arraylist of keys
        FileWriter fileWriter = new FileWriter(textFile);
        for(String key:keysArrayList){
            fileWriter.write(key+"\n");
        }
        fileWriter.close();
    }
    public HashMap<String,ArrayList<String>> getAccounts(String textFile) throws FileNotFoundException {
        HashMap<String, ArrayList<String>> accountsHashMap = new HashMap<String, ArrayList<String>>();
        File keysFile = new File(textFile);
        Scanner scanner = new Scanner(keysFile);
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] lineArray = line.split(",");
            ArrayList<String> valueList = new ArrayList<>();
            valueList.add(lineArray[1]);
            valueList.add(lineArray[2]);
            accountsHashMap.put(lineArray[0],valueList);
        }
        return accountsHashMap;
    }
    public ArrayList<Operator> getOperatorAccounts(HashMap<String,ArrayList<String>> accountsHashMap){ //Convert the account data in hashmap form into arraylist of operators
        ArrayList<Operator> temporaryOperatorList = new ArrayList<>();
        for (Map.Entry<String, ArrayList<String>> entry : accountsHashMap.entrySet()) {
            Operator operator = new Operator(entry.getKey(), entry.getValue().get(0), Boolean.parseBoolean(entry.getValue().get(1)));
            temporaryOperatorList.add(operator);
        }
        System.out.println("Current Operator Counts:" + temporaryOperatorList.size());
        return temporaryOperatorList;
    }
    public ArrayList<Admin> getAdminAccounts(HashMap<String,ArrayList<String>> accountsHashMap) { //Convert the account data in hashmap form into arraylist of admins
        ArrayList<Admin> temporaryAdminList = new ArrayList<>();
        for (Map.Entry<String, ArrayList<String>> entry : accountsHashMap.entrySet()) {
            Admin admin = new Admin(entry.getKey(), entry.getValue().get(0), Boolean.parseBoolean(entry.getValue().get(1)));
            temporaryAdminList.add(admin);
        }
        System.out.println("Current Admin Counts:" + temporaryAdminList.size());
        return temporaryAdminList;
    }
    public ArrayList<Superadmin> getSuperadminAccounts(HashMap<String,ArrayList<String>> accountsHashMap) { //Convert the account data in hashmap form into arraylist of superadmins
        ArrayList<Superadmin> temporarySuperadminList = new ArrayList<>();
        for (Map.Entry<String, ArrayList<String>> entry : accountsHashMap.entrySet()) {
            Superadmin superadmin = new Superadmin(entry.getKey(), entry.getValue().get(0), Boolean.parseBoolean(entry.getValue().get(1)));
            temporarySuperadminList.add(superadmin);
        }
        System.out.println("Current Admin Counts:" + temporarySuperadminList.size());
        return temporarySuperadminList;
    }
    public void saveAccount(int groupIndex, String textFile) throws IOException { //Save the account data from arraylists into their respective text files
        FileWriter fileWriter = new FileWriter(textFile);
        switch (groupIndex){
            case 0:
                for(Operator operator: operatorArrayList){
                    fileWriter.write(operator.toString()+"\n");
                }
                fileWriter.close();
                break;
            case 1:
                for(Admin admin: adminArrayList){
                    fileWriter.write(admin.toString()+"\n");
                }
                fileWriter.close();
                break;
            case 2:
                for(Superadmin superadmin: superadminArrayList){
                    fileWriter.write(superadmin.toString()+"\n");
                }
                fileWriter.close();
                break;
            default: break;
        }
    }
    private boolean registerUser() throws IOException {
        //Get the registered available keys
        operatorKeys = getKeys(operatorKeysTextFile);
        adminKeys = getKeys(adminKeysTextFile);
        superadminKeys = getKeys(superadminKeysTextFile);
        //Get the user input
        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());
        String key = keyField.getText();

        if(username.isEmpty()||password.isEmpty()||key.isEmpty()){ //Reject empty user inputs
            JOptionPane.showMessageDialog(this,"Enter all fields","Missing Input",JOptionPane.ERROR_MESSAGE);
        }
        else{
            //Check if an arraylist of keys has inputted key. And should it do, remove it from the text file and add the new account
            if(operatorKeys.contains(key)){
                System.out.println("Valid Operator Key");
                operatorKeys.remove(key);
                setKeys(operatorKeys,operatorKeysTextFile);
                Operator operator = new Operator(username,password);
                operatorArrayList.add(operator);
                saveAccount(0,operatorAccountsTextFile);
                return true;
            }
            else if(adminKeys.contains(key)){
                System.out.println("Valid Admin Key");
                adminKeys.remove(key);
                setKeys(adminKeys,adminKeysTextFile);
                Admin admin = new Admin(username,password);
                adminArrayList.add(admin);
                saveAccount(1,adminAccountsTextFile);
                return true;
            }
            else if(superadminKeys.contains(key)){
                System.out.println("Valid Superadmin Key");
                superadminKeys.remove(key);
                setKeys(superadminKeys, superadminKeysTextFile);
                Superadmin superadmin = new Superadmin(username,password);
                superadminArrayList.add(superadmin);
                saveAccount(2,superadminAccountsTextFile);
                return true;
            }
            else{ //Key is determined to be invalied as it doesn't exist in any of the arraylists of keys
                System.out.println("Invalid key");
            }
        }
        return false;
    }





}

