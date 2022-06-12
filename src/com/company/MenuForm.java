package com.company;



import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

public class MenuForm {
    private JButton bukuKasButton;
    private JButton adminSystemButton;
    private JLabel displayName;
    private JButton LOGOUTButton;
    private JPanel menuPanel;

    MenuForm(Operator operator,Admin admin,Superadmin superadmin){
        JFrame frame = new JFrame();
        frame.setContentPane(menuPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(300,300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        if(operator!=null){displayName.setText("Operator "+operator.getUsername());}
        if(admin!=null){displayName.setText("Admin "+admin.getUsername());}
        if(superadmin!=null){displayName.setText("Superadmin "+superadmin.getUsername());}

        bukuKasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    FileForm fileForm = new FileForm(operator,admin,superadmin);
                    frame.setVisible(false);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
        adminSystemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(operator!= null && !operator.isStaff()){
                   JOptionPane.showMessageDialog(null,"You do not have access to this panel");
                }
                else if(admin != null && admin.isStaff()){
                        try {
                            StaffForm staffForm = new StaffForm(operator,admin,superadmin);
                            frame.setVisible(false);
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    }
                else if(superadmin != null && superadmin.isStaff()){
                            try {
                                StaffForm staffForm = new StaffForm(operator,admin,superadmin);
                                frame.setVisible(false);
                            } catch (FileNotFoundException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });


        LOGOUTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    LoginForm loginForm = new LoginForm(true);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
                frame.setVisible(false);
            }
        });
    }
}
