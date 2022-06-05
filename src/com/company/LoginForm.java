package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class LoginForm extends Component {
    RegistrationForm rF = new RegistrationForm(false);
    ArrayList<Operator> operatorArrayList;
    ArrayList<Admin> adminArrayList;
    ArrayList<Superadmin> superadminArrayList ;
    String operatorAccountsTextFile = rF.getOperatorAccountsTextFile();
    String adminAccountsTextFile = rF.getAdminAccountsTextFile();
    String superadminAccountsTextFile = rF.getSuperadminAccountsTextFile();


    private JPanel loginPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registryButton;

    Operator currentOperator = null;
    Admin currentAdmin = null;
    Superadmin currentSuperadmin = null;

//    public Operator getCurrentOperator() {return currentOperator;}
//    public Admin getCurrentAdmin() {return currentAdmin;}
//    public Superadmin getCurrentSuperadmin() {return currentSuperadmin;}

    HashMap<String,String> operatorAccounts = new HashMap<String,String>();
    HashMap<String,String> adminAccounts = new HashMap<String,String>();
    HashMap<String,String> superadminAccounts = new HashMap<String,String>();



    public LoginForm(){
        JFrame frame = new JFrame();
        frame.setContentPane(loginPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(false);



        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loginAccount();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }

            }
        });
        registryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registryPage();

            }


        });
    }
    private HashMap<String,String> getAccounts(String textFile) throws FileNotFoundException {
        HashMap<String,String> accountsHashMap = new HashMap<String,String>();
        File keysFile = new File(textFile);
        Scanner scanner = new Scanner(keysFile);
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] lineArray = line.split(",");
            accountsHashMap.put(lineArray[0],lineArray[1]);
        }
        return accountsHashMap;
    }
    private void setAccounts(int groupIndex, HashMap<String,String> accountsHashMap){
        switch (groupIndex){
            case 0:
                for(Map.Entry<String,String> entry: accountsHashMap.entrySet()){
                    Operator operator = new Operator(entry.getKey(),entry.getValue());
                    operatorArrayList.add(operator);
                }
                break;
            case 1:
                for(Map.Entry<String,String> entry: accountsHashMap.entrySet()){
                    Admin admin = new Admin(entry.getKey(),entry.getValue());
                    adminArrayList.add(admin);
                }
                break;
            case 2:
                for(Map.Entry<String,String> entry: accountsHashMap.entrySet()){
                    Superadmin superadmin = new Superadmin(entry.getKey(),entry.getValue());
                    superadminArrayList.add(superadmin);
                }
                break;
            default: break;
        }
    }


    private void loginAccount() throws FileNotFoundException {
        // If account array list is empty, get data from text file

        if(rF.getOperatorArrayList()!=null){
            operatorArrayList = rF.getOperatorArrayList();}
        else{
            operatorAccounts = getAccounts(operatorAccountsTextFile);
            setAccounts(0,operatorAccounts);
        }

        if(rF.getAdminArrayList()!=null){
            adminArrayList = rF.getAdminArrayList();
        }
        else{
            adminAccounts = getAccounts(adminAccountsTextFile);
            setAccounts(1,adminAccounts);
        }

        if(rF.getSuperadminArrayList()!=null){
            superadminArrayList = rF.getSuperadminArrayList();
        }
        else{
            superadminAccounts = getAccounts(superadminAccountsTextFile);
            setAccounts(2,superadminAccounts);
        }




        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());

        if(username.isEmpty()||password.isEmpty()){
            JOptionPane.showMessageDialog(this,"Enter all fields","Missing Input",JOptionPane.ERROR_MESSAGE);
            return;
        }

        if(Objects.equals(operatorAccounts.get(username), password)){
            System.out.println("Valid Operator Login");
        }
        else if(Objects.equals(operatorAccounts.get(username), password)){
            System.out.println("Valid Admin Login");
        }
        else if(Objects.equals(operatorAccounts.get(username), password)){
            System.out.println("Valid Superadmin Login");
        }
        else{
            System.out.println("Invalid login");
        }




    }
    private void registryPage() {
        RegistrationForm registrationForm = new RegistrationForm(true);
    }


}
