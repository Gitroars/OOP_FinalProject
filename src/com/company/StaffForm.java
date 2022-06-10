package com.company;

import org.apache.poi.sl.draw.geom.GuideIf;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StaffForm {
    private Admin currentAdmin;
    private Superadmin currentSuperadmin;

    private int currentUserRankPower;



    private RegistrationForm registrationForm = new RegistrationForm(false);
    private LoginForm loginForm = new LoginForm();
    private HashMap<String, ArrayList<String>> operatorMap = new HashMap<>();
    private HashMap<String, ArrayList<String>> adminMap = new HashMap<>();
    private HashMap<String, ArrayList<String>> superadminMap = new HashMap<>();
    private ArrayList<User> userList = new ArrayList<>();
    private JTable table1;
    private JPanel StaffPanel;
    private JButton promoteButton;
    private JButton demoteButton;
    private JButton banUnbanButton;
    TableModel dataTable = new TableModel(userList);

    public StaffForm(Operator operator,Admin admin,Superadmin superadmin) throws FileNotFoundException {
        // Determine the current user's level of authority
        if(operator!=null){
            currentUserRankPower = operator.getRankPower();
        }
        else if(admin!= null){
            currentUserRankPower = admin.getRankPower();
        }
        else if(superadmin!=null){
            currentUserRankPower = superadmin.getRankPower();
        }

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
                PromoteRank();
            }
        });
        demoteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DemoteRank();
            }
        });
        banUnbanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {BanAndUnban();}
        });
    }

    void SetUserList() throws FileNotFoundException {
        operatorMap = loginForm.getAccounts(registrationForm.getOperatorAccountsTextFile());
        adminMap = loginForm.getAccounts(registrationForm.getAdminAccountsTextFile());
        superadminMap = loginForm.getAccounts(registrationForm.getSuperadminAccountsTextFile());
        for(Map.Entry<String, ArrayList<String>> entry: superadminMap.entrySet()){
            User user = new User(entry.getKey(),"Superadmin",Boolean.parseBoolean(entry.getValue().get(1)),entry.getValue().get(0),2);
            userList.add(user);
        }
        for(Map.Entry<String, ArrayList<String>> entry: adminMap.entrySet()){
            User user = new User(entry.getKey(),"Admin",Boolean.parseBoolean(entry.getValue().get(1)),entry.getValue().get(0),1);
            userList.add(user);
        }
        for(Map.Entry<String, ArrayList<String>> entry: operatorMap.entrySet()) {
            User user = new User(entry.getKey(),"Operator",Boolean.parseBoolean(entry.getValue().get(1)),entry.getValue().get(0),0);
            userList.add(user);
        }
        UpdateTable();
    }
    void SaveHashmap(String rank,User user){
        ArrayList<String> valueList = new ArrayList<>();
        valueList.add(user.getPassword());
        valueList.add(user.getIsBanned());
        if(rank=="Operator"){operatorMap.put(user.getName(),valueList);}
        else if(rank=="Admin"){adminMap.put(user.getName(),valueList);}
        else if(rank=="Superadmin"){superadminMap.put(user.getName(),valueList);}
    }
    void SaveToFile(){
        operatorMap.clear();
        adminMap.clear();
        superadminMap.clear();
        for(User user:userList){
            SaveHashmap(user.getRank(),user);
        }
        loginForm.setAccounts(0,operatorMap);
        loginForm.setAccounts(1,adminMap);
        loginForm.setAccounts(2,superadminMap);
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







    void PromoteRank(){
        int index = table1.getSelectedRow();
        if(index>-1 && isTargetValid(index)){ //Check whether target do exist and is valid
            String currentRank = userList.get(index).getRank();
            userList.get(index).setRank(getNextRank(currentRank)); //Set target rank one level higher from current rank
            UpdateTable();
            SaveToFile();
        }

    }
    void DemoteRank(){
        int index = table1.getSelectedRow();
        if(index>-1 && isTargetValid(index)){ //Check whether target do exist and is valid
            String currentRank = userList.get(index).getRank();
            userList.get(index).setRank(getPreviousRank(currentRank)); //Set target rank one level lower from current rank
            UpdateTable();
            SaveToFile();
        }
    }

    void BanAndUnban(){
        int index = table1.getSelectedRow();
        if(index>-1 && isTargetValid(index)){ //Check whether target do exist and is valid
            userList.get(index).setBanned(!userList.get(index).isBanned()); //Set target banned status the opposite of current banned status
            UpdateTable();
            SaveToFile();
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
