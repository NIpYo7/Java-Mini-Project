package ui.panels;

import ui.theme.AppTheme;
import dao.*;
import model.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;

public class UsersPanel extends BasePanel {
    private final UserDAO userDAO=new UserDAO();
    private DefaultTableModel tm;
    public UsersPanel(User user){super(user);build();}

    private void build(){
        JPanel content=pageContent();
        JLabel title=AppTheme.sectionTitle("User Management"); title.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title); vgap(content,16);

        JButton add=AppTheme.primaryBtn("+ Add User"); JButton del=AppTheme.dangerBtn("Delete"); JButton ref=AppTheme.ghostBtn("Refresh");
        JPanel btns=AppTheme.buttonRow(add,del,ref); btns.setAlignmentX(Component.LEFT_ALIGNMENT); btns.setMaximumSize(new Dimension(Integer.MAX_VALUE,44));
        content.add(btns); vgap(content,12);

        String[] cols={"ID","Username","Full Name","Role","Department","Reg No"};
        tm=model(cols);
        JTable t=AppTheme.styledTable(tm); loadUsers();

        JPanel card=AppTheme.card(12); card.setLayout(new BorderLayout()); card.setBorder(new EmptyBorder(0,0,0,0));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(AppTheme.scrollTable(t),BorderLayout.CENTER);
        content.add(card);
        add(scrollWrap(content));

        add.addActionListener(e->showAddDialog());
        del.addActionListener(e->{
            int row=t.getSelectedRow(); if(row<0)return;
            int id=(int)tm.getValueAt(row,0);
            if(JOptionPane.showConfirmDialog(this,"Delete?","Confirm",JOptionPane.YES_NO_OPTION)==0){userDAO.deleteUser(id);loadUsers();}
        });
        ref.addActionListener(e->loadUsers());
    }

    private void loadUsers(){
        tm.setRowCount(0);
        userDAO.getAllUsers().forEach(u->tm.addRow(new Object[]{u.getId(),u.getUsername(),u.getFullName(),u.getRole(),u.getDepartment(),u.getRegNumber()!=null?u.getRegNumber():"—"}));
    }

    private void showAddDialog(){
        JTextField fu=AppTheme.textField("username"); JTextField fp=AppTheme.textField("password");
        JTextField fn=AppTheme.textField("full name"); JTextField fe=AppTheme.textField("email");
        JTextField fd=AppTheme.textField("ICT"); JTextField fr=AppTheme.textField("reg number"); JTextField fb=AppTheme.textField("2023");
        JComboBox<String> role=AppTheme.comboBox("student","lecturer","tech_officer","admin");
        Object[] inputs={"Username:",fu,"Password:",fp,"Full Name:",fn,"Email:",fe,"Department:",fd,"Role:",role,"Reg Number:",fr,"Batch:",fb};
        if(JOptionPane.showConfirmDialog(this,inputs,"Add User",JOptionPane.OK_CANCEL_OPTION)==0){
            User nu; switch((String)role.getSelectedItem()){
                case "admin": nu=new Admin(); break; case "lecturer": nu=new Lecturer(); break;
                case "tech_officer": nu=new TechOfficer(); break; default: nu=new Student();
            }
            nu.setUsername(fu.getText().trim()); nu.setPassword(fp.getText().trim());
            nu.setFullName(fn.getText().trim());  nu.setEmail(fe.getText().trim());
            nu.setDepartment(fd.getText().trim()); nu.setRegNumber(fr.getText().trim());
            try{nu.setBatch(Integer.parseInt(fb.getText().trim()));}catch(Exception e){nu.setBatch(0);}
            if(userDAO.addUser(nu)) loadUsers(); else JOptionPane.showMessageDialog(this,"Username exists.");
        }
    }
}

