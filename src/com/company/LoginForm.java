package com.company;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.*;


public class LoginForm extends Component {
    private RegistrationForm registrationForm = new RegistrationForm(false); //to access get and set account functions
    //store the user groups
    private ArrayList<Operator> operatorArrayList = new ArrayList<>();
    private ArrayList<Admin> adminArrayList = new ArrayList<>();
    private ArrayList<Superadmin> superadminArrayList = new ArrayList<>() ;
    // file's path directory
    private final String operatorAccountsTextFile = registrationForm.getOperatorAccountsTextFile();
    private final String adminAccountsTextFile = registrationForm.getAdminAccountsTextFile();
    private final String superadminAccountsTextFile = registrationForm.getSuperadminAccountsTextFile();


    private JPanel loginPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registryButton;

    //initialize values to allow parameter input to other forms
    private Operator currentOperator = null;
    private Admin currentAdmin = null;
    private Superadmin currentSuperadmin = null;


    //temporary store for conversion
    private HashMap<String,ArrayList<String>> operatorHashMap = new HashMap<String,ArrayList<String>>();
    private HashMap<String,ArrayList<String>> adminHashMap = new HashMap<String,ArrayList<String>>();
    private HashMap<String,ArrayList<String>> superadminHashMap = new HashMap<String,ArrayList<String>>();



    public LoginForm(boolean isVisible) throws FileNotFoundException {
        //Create GUI
        JFrame frame = new JFrame();
        frame.setContentPane(loginPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(isVisible);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    boolean isLoginSuccess = loginAccount();
                    if(isLoginSuccess){ //close the Login Form upon successful login
                        frame.setVisible(false); //close current screen
                    }
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
        registryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    RegistrationForm registrationForm = new RegistrationForm(true); //open the registration form
                    frame.setVisible(false); //close current screen
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }

            }


        });
    }



    private boolean loginAccount() throws FileNotFoundException {
        boolean isLoginSuccessful = false;
        boolean isAccountBanned = false;
        System.out.println("Login Attempt");
        // If account array list is not the latest version, get data from text file

        if(operatorHashMap.size()!= registrationForm.getAccounts(operatorAccountsTextFile).size()){
            operatorHashMap = registrationForm.getAccounts(operatorAccountsTextFile);
            operatorArrayList = registrationForm.getOperatorAccounts(operatorHashMap);
            System.out.println("Adjusted Operator List");
        }
        if(adminHashMap.size()!= registrationForm.getAccounts(adminAccountsTextFile).size()){
            adminHashMap = registrationForm.getAccounts(adminAccountsTextFile);
            adminArrayList = registrationForm.getAdminAccounts(adminHashMap);
            System.out.println("Adjusted Admin List");
        }
        if(superadminHashMap.size()!= registrationForm.getAccounts(superadminAccountsTextFile).size()){
            superadminHashMap = registrationForm.getAccounts(superadminAccountsTextFile);
            superadminArrayList = registrationForm.getSuperadminAccounts(superadminHashMap);
            System.out.println("Adjusted Superadmin List");
        }






        //Get the user's inputted data
        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());


        if(username.isEmpty()||password.isEmpty()){ //Warn if text fields are empty
            JOptionPane.showMessageDialog(this,"Enter all fields","Missing Input",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else{
            // If there exist a list with the same username and password , iterate through the list and check if it's unbanned,and if it does , set the current user type to that account, else display a message of ban status.



            for(Operator operator: operatorArrayList){
                if(Objects.equals(operator.getUsername(), username) && Objects.equals(operator.getPassword(), password)){
                    if(!operator.isBanned()){
                        System.out.println("Valid Operator Login");
                        currentOperator = operator;
                        isLoginSuccessful = true;
                        FilePage();
                        break;
                    }
                    else{
                        isAccountBanned = true;
                        JOptionPane.showMessageDialog(this,"Your account has been banned","System Notice",JOptionPane.ERROR_MESSAGE);
                        break;
                    }

                }
            }
            if(isLoginSuccessful){ //No need to check other list
                return true;
            }




            for(Admin admin: adminArrayList){
                if(Objects.equals(admin.getUsername(), username) && Objects.equals(admin.getPassword(), password)){
                    if(!admin.isBanned()){
                        System.out.println("Valid Admin Login");
                        currentAdmin = admin;
                        isLoginSuccessful = true;
                        FilePage();
                        break;
                    }
                    else{
                        isAccountBanned = true;
                        JOptionPane.showMessageDialog(this,"Your account has been banned","System Notice",JOptionPane.ERROR_MESSAGE);
                        break;
                    }

                }
            }
            if(isLoginSuccessful){ //No need to check other list
                return true;
            }


            for(Superadmin superadmin: superadminArrayList){
                if(Objects.equals(superadmin.getUsername(), username) && Objects.equals(superadmin.getPassword(), password)){
                    if(!superadmin.isBanned()){
                        System.out.println("Valid Superadmin Login");
                        currentSuperadmin = superadmin;
                        isLoginSuccessful = true;
                        FilePage();
                        break;
                    }
                    else{
                        isAccountBanned = true;
                        JOptionPane.showMessageDialog(this,"Your account has been banned","System Notice",JOptionPane.ERROR_MESSAGE);
                        break;
                    }

                }
            }
            if(isLoginSuccessful){ //No need to check other list
                return true;
            }
            if(!isAccountBanned){ //Since the account isn't banned, then the user login details must be wrong
                JOptionPane.showMessageDialog(this,"Wrong username or password","Login Failure",JOptionPane.ERROR_MESSAGE);
            }
            return false; // no list holds the same username and password
        }
    }
    void FilePage() throws FileNotFoundException { //Open the file screen with current user types' value
        MenuForm menuForm = new MenuForm(currentOperator,currentAdmin,currentSuperadmin);
    }



}
