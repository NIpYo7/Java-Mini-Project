package ui.panels;

import ui.theme.AppTheme;
import dao.*;
import model.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;

public class ProfilePanel extends BasePanel {
    private final UserDAO userDAO=new UserDAO();
    public ProfilePanel(User user){super(user);build();}

    private void build(){
        JPanel content=pageContent();
        JLabel title=AppTheme.sectionTitle("My Profile"); title.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title); vgap(content,16);

        JPanel row=new JPanel(new GridLayout(1,2,20,0)); row.setOpaque(false); row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE,520));

        // Left: photo + info card
        JPanel photoCard=AppTheme.card(12); photoCard.setLayout(new BoxLayout(photoCard,BoxLayout.Y_AXIS)); photoCard.setBorder(new EmptyBorder(28,24,28,24));
        JLabel photo=new JLabel();
        photo.setPreferredSize(new Dimension(110,110)); photo.setMaximumSize(new Dimension(110,110));
        photo.setAlignmentX(Component.CENTER_ALIGNMENT);
        refreshProfilePhoto(photo);
        JButton uploadBtn=AppTheme.ghostBtn("Upload Photo");
        uploadBtn.setAlignmentX(Component.CENTER_ALIGNMENT); uploadBtn.setMaximumSize(new Dimension(160,36));
        uploadBtn.addActionListener(e->{
            JFileChooser fc=new JFileChooser();
            fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images","jpg","jpeg","png"));
            if(fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
                try{File d=new File("profile_pics"); if(!d.exists())d.mkdirs();
                    File dest=new File(d,"user_"+user.getId()+".jpg");
                    Files.copy(fc.getSelectedFile().toPath(),dest.toPath(),StandardCopyOption.REPLACE_EXISTING);
                    userDAO.updateProfilePic(user.getId(),dest.getPath());
                    user.setProfilePic(dest.getPath());
                    refreshProfilePhoto(photo);
                    JOptionPane.showMessageDialog(this,"Photo updated!");}
                catch(Exception ex){JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage());}
            }
        });

        JLabel nameL=new JLabel(user.getFullName(),SwingConstants.CENTER); nameL.setFont(AppTheme.F_H2); nameL.setForeground(AppTheme.TEXT_DARK); nameL.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel roleL=new JLabel(user.getRole(),SwingConstants.CENTER); roleL.setFont(AppTheme.F_BODY); roleL.setForeground(AppTheme.TEXT_MUTED); roleL.setAlignmentX(Component.CENTER_ALIGNMENT);

        photoCard.add(Box.createVerticalStrut(8)); photoCard.add(photo);
        photoCard.add(Box.createVerticalStrut(14)); photoCard.add(nameL); photoCard.add(roleL);
        photoCard.add(Box.createVerticalStrut(16)); photoCard.add(uploadBtn);

        // Right: edit form
        JPanel editCard=AppTheme.card(12); editCard.setLayout(new BoxLayout(editCard,BoxLayout.Y_AXIS)); editCard.setBorder(new EmptyBorder(24,28,24,28));

        boolean isStudent="student".equals(user.getRole());
        JTextField fUser=ro(user.getUsername()); JTextField fName=isStudent?ro(user.getFullName()):tf(user.getFullName());
        JTextField fEmail=tf(user.getEmail()!=null?user.getEmail():""); JTextField fPhone=tf(user.getPhone()!=null?user.getPhone():"");
        JTextField fAddr=tf(user.getAddress()!=null?user.getAddress():"");
        JTextField fDept=isStudent?ro(user.getDepartment()!=null?user.getDepartment():""):tf(user.getDepartment()!=null?user.getDepartment():"");

        for(Object[][] r:new Object[][][]{
            {{"USERNAME (READ-ONLY)",fUser}},{{"FULL NAME",fName}},
            {{"EMAIL",fEmail}},{{"PHONE",fPhone}},{{"ADDRESS",fAddr}},{{"DEPARTMENT",fDept}}}) {
            JPanel fr=AppTheme.formRow((String)r[0][0],(JComponent)r[0][1]);
            fr.setAlignmentX(Component.LEFT_ALIGNMENT); fr.setMaximumSize(new Dimension(Integer.MAX_VALUE,66));
            editCard.add(fr); editCard.add(Box.createVerticalStrut(12));
        }

        JButton save=AppTheme.primaryBtn("Save Changes");
        save.setAlignmentX(Component.LEFT_ALIGNMENT); save.setMaximumSize(new Dimension(Integer.MAX_VALUE,44));
        JLabel msg=new JLabel(" "); msg.setFont(AppTheme.F_SMALL); msg.setAlignmentX(Component.LEFT_ALIGNMENT);
        save.addActionListener(e->{
            boolean ok=userDAO.updateProfile(user.getId(),fName.getText().trim(),fEmail.getText().trim(),fPhone.getText().trim(),fAddr.getText().trim(),fDept.getText().trim());
            if(ok){user.setFullName(fName.getText().trim());user.setEmail(fEmail.getText().trim());msg.setForeground(AppTheme.SUCCESS);msg.setText("✓ Profile updated!");}
            else{msg.setForeground(AppTheme.DANGER);msg.setText("✗ Error saving.");}
        });
        editCard.add(save); editCard.add(Box.createVerticalStrut(8)); editCard.add(msg);

        row.add(photoCard); row.add(editCard);
        content.add(row); vgap(content,20);

        // Password change
        JPanel pwCard=AppTheme.card(12); pwCard.setLayout(new BoxLayout(pwCard,BoxLayout.Y_AXIS)); pwCard.setBorder(new EmptyBorder(24,28,24,28));
        pwCard.setAlignmentX(Component.LEFT_ALIGNMENT); pwCard.setMaximumSize(new Dimension(Integer.MAX_VALUE,220));
        JLabel pl=new JLabel("Change Password"); pl.setFont(AppTheme.F_H3); pl.setForeground(AppTheme.TEXT_DARK); pl.setAlignmentX(Component.LEFT_ALIGNMENT);
        pwCard.add(pl); pwCard.add(Box.createVerticalStrut(16));

        JPanel pwRow=new JPanel(new GridLayout(1,3,16,0)); pwRow.setOpaque(false); pwRow.setAlignmentX(Component.LEFT_ALIGNMENT); pwRow.setMaximumSize(new Dimension(Integer.MAX_VALUE,66));
        JPasswordField old=AppTheme.passwordField("Current"); JPasswordField nw=AppTheme.passwordField("New (min 6)"); JPasswordField conf=AppTheme.passwordField("Confirm");
        pwRow.add(AppTheme.formRow("CURRENT PASSWORD",old)); pwRow.add(AppTheme.formRow("NEW PASSWORD",nw)); pwRow.add(AppTheme.formRow("CONFIRM",conf));
        pwCard.add(pwRow); pwCard.add(Box.createVerticalStrut(14));

        JButton pwSave=AppTheme.primaryBtn("Update Password"); pwSave.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel pwMsg=new JLabel(" "); pwMsg.setFont(AppTheme.F_SMALL); pwMsg.setAlignmentX(Component.LEFT_ALIGNMENT);
        pwSave.addActionListener(e->{
            String op=new String(old.getPassword()).trim(),np=new String(nw.getPassword()).trim(),cp=new String(conf.getPassword()).trim();
            if(op.isEmpty()||np.isEmpty()){pwMsg.setForeground(AppTheme.DANGER);pwMsg.setText("Fill all fields.");return;}
            if(!np.equals(cp)){pwMsg.setForeground(AppTheme.DANGER);pwMsg.setText("Passwords do not match.");return;}
            if(np.length()<6){pwMsg.setForeground(AppTheme.DANGER);pwMsg.setText("Min 6 characters.");return;}
            boolean ok=userDAO.changePassword(user.getId(),op,np);
            if(ok){user.setPassword(np);pwMsg.setForeground(AppTheme.SUCCESS);pwMsg.setText("✓ Password updated!");old.setText("");nw.setText("");conf.setText("");}
            else{pwMsg.setForeground(AppTheme.DANGER);pwMsg.setText("Current password incorrect.");}
        });
        pwCard.add(pwSave); pwCard.add(Box.createVerticalStrut(6)); pwCard.add(pwMsg);
        content.add(pwCard);
        add(scrollWrap(content));
    }

    private JTextField ro(String v){JTextField f=AppTheme.textField(null);f.setText(v);f.setEditable(false);f.setBackground(new Color(0xF3F4F6));f.setForeground(AppTheme.TEXT_MUTED);return f;}
    private JTextField tf(String v){JTextField f=AppTheme.textField(null);f.setText(v);return f;}

    private void refreshProfilePhoto(JLabel photoLabel) {
        ImageIcon icon = buildProfileIcon(user.getProfilePic(), 110, user.getFullName());
        photoLabel.setIcon(icon);
        photoLabel.setText("");
    }

    private ImageIcon buildProfileIcon(String imagePath, int size, String fullName) {
        BufferedImage canvas = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = canvas.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Shape clip = new Ellipse2D.Float(0, 0, size, size);
        g2.setClip(clip);

        boolean drawn = false;
        if (imagePath != null && !imagePath.trim().isEmpty()) {
            try {
                BufferedImage src = ImageIO.read(new File(imagePath));
                if (src != null) {
                    double scale = Math.max((double) size / src.getWidth(), (double) size / src.getHeight());
                    int drawW = (int) Math.round(src.getWidth() * scale);
                    int drawH = (int) Math.round(src.getHeight() * scale);
                    int x = (size - drawW) / 2;
                    int y = (size - drawH) / 2;
                    g2.drawImage(src, x, y, drawW, drawH, null);
                    drawn = true;
                }
            } catch (IOException ignored) {}
        }

        if (!drawn) {
            g2.setColor(AppTheme.PRIMARY_LIGHT);
            g2.fillOval(0,0,size,size);
            g2.setColor(AppTheme.PRIMARY);
            g2.setFont(new Font("Segoe UI",Font.BOLD,40));
            String ini=fullName!=null && fullName.length()>=2?fullName.substring(0,2).toUpperCase():"??";
            FontMetrics fm=g2.getFontMetrics();
            g2.drawString(ini,size/2-fm.stringWidth(ini)/2,size/2+fm.getAscent()/3);
        }

        g2.dispose();
        return new ImageIcon(canvas);
    }
}
