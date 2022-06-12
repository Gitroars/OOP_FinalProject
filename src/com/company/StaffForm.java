package com.company;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StaffForm {

    private int currentUserRankPower;



    private RegistrationForm registrationForm = new RegistrationForm(false);
    private LoginForm loginForm = new LoginForm(false);
    private HashMap<String, ArrayList<String>> operatorHashMap = new HashMap<>();
    private HashMap<String, ArrayList<String>> adminHashMap = new HashMap<>();
    private HashMap<String, ArrayList<String>> superadminHashMap = new HashMap<>();
    private ArrayList<User> userList = new ArrayList<>();
    private JTable table1;
    private JPanel StaffPanel;
    private JButton promoteButton;
    private JButton demoteButton;
    private JButton banUnbanButton;
    private JButton EXITButton;
    private JLabel titleLabel;
    private JLabel nameLabel;
    TableModel dataTable = new TableModel(userList);

    public StaffForm(Operator operator,Admin admin,Superadmin superadmin) throws FileNotFoundException {
        // Determine the current user's level of authority
        String displayName = null;
        if(operator!=null){
            displayName = "Operator "+operator.getUsername();
            currentUserRankPower = operator.getRankPower();}
        else if(admin!= null){
            displayName = "Admin "+admin.getUsername();
            currentUserRankPower = admin.getRankPower();
        }
        else if(superadmin!=null){
            displayName = "Superadmin "+superadmin.getUsername();
            currentUserRankPower = superadmin.getRankPower();
        }
        nameLabel.setText(displayName);

        //Create the GUI and manage the settings
        JFrame frame = new JFrame();
        frame.setContentPane(StaffPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        table1.setModel(dataTable);
        SetUserList();




        promoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PromoteRank();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        demoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DemoteRank();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        banUnbanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    BanAndUnban();
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

    void SetUserList() throws FileNotFoundException {
        operatorHashMap = registrationForm.getAccounts(registrationForm.getOperatorAccountsTextFile());
        adminHashMap = registrationForm.getAccounts(registrationForm.getAdminAccountsTextFile());
        superadminHashMap = registrationForm.getAccounts(registrationForm.getSuperadminAccountsTextFile());
        for(Map.Entry<String, ArrayList<String>> entry: superadminHashMap.entrySet()){
            User user = new User(entry.getKey(),"Superadmin",Boolean.parseBoolean(entry.getValue().get(1)),entry.getValue().get(0),2);
            userList.add(user);
        }
        for(Map.Entry<String, ArrayList<String>> entry: adminHashMap.entrySet()){
            User user = new User(entry.getKey(),"Admin",Boolean.parseBoolean(entry.getValue().get(1)),entry.getValue().get(0),1);
            userList.add(user);
        }
        for(Map.Entry<String, ArrayList<String>> entry: operatorHashMap.entrySet()) {
            User user = new User(entry.getKey(),"Operator",Boolean.parseBoolean(entry.getValue().get(1)),entry.getValue().get(0),0);
            userList.add(user);
        }
        UpdateTable();
    }
    void SaveHashmap(String rank,User user){
        ArrayList<String> valueList = new ArrayList<>();
        valueList.add(user.getPassword());
        valueList.add(user.getIsBanned());
        if(rank=="Operator"){
            operatorHashMap.put(user.getName(),valueList);}
        else if(rank=="Admin"){
            adminHashMap.put(user.getName(),valueList);}
        else if(rank=="Superadmin"){
            superadminHashMap.put(user.getName(),valueList);}
    }
    void SaveToFile() throws IOException {
        operatorHashMap.clear();
        adminHashMap.clear();
        superadminHashMap.clear();
        for(User user:userList){
            SaveHashmap(user.getRank(),user);
        }


        FileWriter fileWriter = new FileWriter(registrationForm.getOperatorAccountsTextFile());
        for(Map.Entry<String,ArrayList<String>> entry: operatorHashMap.entrySet()){
            fileWriter.write(entry.getKey()+","+entry.getValue().get(0)+","+entry.getValue().get(1)+"\n");
        }
        fileWriter.close();

        fileWriter = new FileWriter(registrationForm.getAdminAccountsTextFile());
        for(Map.Entry<String,ArrayList<String>> entry: adminHashMap.entrySet()){
            fileWriter.write(entry.getKey()+","+entry.getValue().get(0)+","+entry.getValue().get(1)+"\n");
        }
        fileWriter.close();

        fileWriter = new FileWriter(registrationForm.getSuperadminAccountsTextFile());
        for(Map.Entry<String,ArrayList<String>> entry: superadminHashMap.entrySet()){
            fileWriter.write(entry.getKey()+","+entry.getValue().get(0)+","+entry.getValue().get(1)+"\n");
        }
        fileWriter.close();
    }


    String getNextRank(String currentRank){
        String nextRank = "Superadmin";
        if(currentRank=="Operator") {
            nextRank = "Admin";
        }
        return nextRank;
    }
    String getPreviousRank(String currentRank){
        String previousRank = "Operator";

        if(currentRank=="Superadmin"){
            return "Admin";
        }
        return previousRank;
    }

    boolean isTargetValid(int index){
        // returns true if user level is higher. returns false if lower.
         return currentUserRankPower>userList.get(index).getRankPower();
    }







    void PromoteRank() throws IOException {
        int index = table1.getSelectedRow();
        if(index>-1 && isTargetValid(index)){ //Check whether target do exist and is valid
            String currentRank = userList.get(index).getRank();
            userList.get(index).setRank(getNextRank(currentRank)); //Set target rank one level higher from current rank
            UpdateTable();
            SaveToFile();
        }
        else if (!isTargetValid(index)) {
            JOptionPane.showMessageDialog(null,"You cannot target this user");
        }

    }
    void DemoteRank() throws IOException {
        int index = table1.getSelectedRow();
        if(index>-1 && isTargetValid(index)){ //Check whether target do exist and is valid
            String currentRank = userList.get(index).getRank();
            userList.get(index).setRank(getPreviousRank(currentRank)); //Set target rank one level lower from current rank
            UpdateTable();
            SaveToFile();
        }
        else if (!isTargetValid(index)) {
            JOptionPane.showMessageDialog(null,"You cannot target this user");
        }
    }

    void BanAndUnban() throws IOException {
        int index = table1.getSelectedRow();
        if(index>-1 && isTargetValid(index)){ //Check whether target do exist and is valid
            userList.get(index).setBanned(!userList.get(index).isBanned()); //Set target banned status the opposite of current banned status
            UpdateTable();
            SaveToFile();
        }
        else if (!isTargetValid(index)) {
            JOptionPane.showMessageDialog(null,"You cannot target this user");
        }
    }






    private static class TableModel extends AbstractTableModel {
        private final String[] COLUMNS = {"Name","Rank","Banned"}; //the column header content
        private ArrayList<User> userList;
        private TableModel(ArrayList<User> userList){
            this.userList = userList;
        }

        @Override
        public int getRowCount() {
            return userList.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMNS.length;
        }



        @Override
        public String getColumnName(int column) {return COLUMNS[column];} //Display column header

        public Object getValueAt(int rowIndex, int columnIndex) { //Display names and ranks
            return switch(columnIndex){
                case 0 -> userList.get(rowIndex).getName();
                case 1 -> userList.get(rowIndex).getRank();
                case 2 -> userList.get(rowIndex).getIsBanned();
                default->"";
            };
        }
    }
    void UpdateTable(){
        dataTable.fireTableDataChanged();
    }
}
