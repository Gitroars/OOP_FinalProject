package com.company;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class LoginForm extends Component {
    private RegistrationForm rF = new RegistrationForm(false);
    private ArrayList<Operator> operatorArrayList = new ArrayList<>();
    private ArrayList<Admin> adminArrayList = new ArrayList<>();
    private ArrayList<Superadmin> superadminArrayList = new ArrayList<>() ;
    private String operatorAccountsTextFile = "operatorAccounts.txt";
    private String adminAccountsTextFile = "adminAccounts.txt";
    private String superadminAccountsTextFile = "superadminAccounts.txt";


    private JPanel loginPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registryButton;

    private Operator currentOperator = null;
    private Admin currentAdmin = null;
    private Superadmin currentSuperadmin = null;



    private HashMap<String,ArrayList<String>> operatorAccounts = new HashMap<String,ArrayList<String>>();
    private HashMap<String,ArrayList<String>> adminAccounts = new HashMap<String,ArrayList<String>>();
    private HashMap<String,ArrayList<String>> superadminAccounts = new HashMap<String,ArrayList<String>>();



    public LoginForm(){
        //Create GUI
        JFrame frame = new JFrame();
        frame.setContentPane(loginPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    boolean isLoginSuccess = loginAccount();
                    if(isLoginSuccess){ //close the Login Form upon successful login
                        frame.setVisible(false);
                    }
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
        registryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrationForm registrationForm = new RegistrationForm(true);
                frame.setVisible(false);
            }


        });
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
    public void setAccounts(int groupIndex, HashMap<String,ArrayList<String>> accountsHashMap){
        switch (groupIndex){
            case 0:
                for(Map.Entry<String,ArrayList<String>> entry: accountsHashMap.entrySet()){
                    Operator operator = new Operator(entry.getKey(),entry.getValue().get(0),Boolean.parseBoolean(entry.getValue().get(1)));
                    operatorArrayList.add(operator);
                }
                System.out.println("Current Operators:" + operatorArrayList.size());
                break;
            case 1:
                for(Map.Entry<String,ArrayList<String>> entry: accountsHashMap.entrySet()){
                    Admin admin = new Admin(entry.getKey(),entry.getValue().get(0),Boolean.parseBoolean(entry.getValue().get(1)));
                    adminArrayList.add(admin);
                }
                System.out.println("Current Admins:" + adminArrayList.size());
                break;
            case 2:
                for(Map.Entry<String,ArrayList<String>> entry: accountsHashMap.entrySet()){
                    Superadmin superadmin = new Superadmin(entry.getKey(),entry.getValue().get(0),Boolean.parseBoolean(entry.getValue().get(1)));
                    superadminArrayList.add(superadmin);
                }
                System.out.println("Current Superadmins:" + superadminArrayList.size());
                break;
            default: break;
        }
    }

    private boolean loginAccount() throws FileNotFoundException {
        boolean isLoginSuccessful = false;
        System.out.println("Login Attempt");
        // If account array list is empty, get data from text file

//        if(rF.getOperatorArrayList()!=null){
//            operatorArrayList = rF.getOperatorArrayList();}

            operatorAccounts = getAccounts(operatorAccountsTextFile);
            setAccounts(0,operatorAccounts);


//        if(rF.getAdminArrayList()!=null){
//            adminArrayList = rF.getAdminArrayList();
//        }

            adminAccounts = getAccounts(adminAccountsTextFile);
            setAccounts(1,adminAccounts);


//        if(rF.getSuperadminArrayList()!=null){
//            superadminArrayList = rF.getSuperadminArrayList();
//        }

            superadminAccounts = getAccounts(superadminAccountsTextFile);
            setAccounts(2,superadminAccounts);





        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());

        // Warns the user of the presence of empty text fields
//        if(username.isEmpty()||password.isEmpty()){
//            JOptionPane.showMessageDialog(this,"Enter all fields","Missing Input",JOptionPane.ERROR_MESSAGE);
//        }


        // If there exist an operator account with the same username and password unbanned, iterate through the list and set the current operator to that account

            for(Operator operator: operatorArrayList){
                if(Objects.equals(operator.getUsername(), username) && Objects.equals(operator.getPassword(), password) && !operator.isBanned()){
                    System.out.println("Valid Operator Login");
                    currentOperator = operator;
                    isLoginSuccessful = true;
                    FilePage();
                    break;
                }
                else{
                    System.out.println("Invalid login");
                }
            }

        // If there exist an admin account with the same username and password unbanned, iterate through the list and set the current admin to that account


            for(Admin admin: adminArrayList){
                if(Objects.equals(admin.getUsername(), username) && Objects.equals(admin.getPassword(), password)&& !admin.isBanned()){
                    System.out.println("Valid Admin Login");
                    currentAdmin = admin;
                    isLoginSuccessful = true;
                    FilePage();
                }
                else{
                    System.out.println("Invalid login");
                }
            }

        // If there exist superadmin account with the same username and password unbanned, iterate through the list and set the current superadmin to that account
            for(Superadmin superadmin: superadminArrayList){
                if(superadmin.getUsername() == username && superadmin.getPassword() == password && !superadmin.isBanned()){
                    System.out.println("Valid Superadmin Login");
                    currentSuperadmin = superadmin;
                    isLoginSuccessful = true;
                    FilePage();
                    break;
                }
                else{
                    System.out.println("Invalid login");
                }
            }
            return isLoginSuccessful;

    }
    void FilePage(){
        FileForm fileForm = new FileForm(currentOperator,currentAdmin,currentSuperadmin);
    }



}
