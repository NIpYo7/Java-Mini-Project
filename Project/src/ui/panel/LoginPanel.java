package ui.panels;

import ui.theme.AppTheme;
import dao.UserDAO;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * LoginPanel.java
 * Clean centered login card with role selector and animated feedback.
 * Shown inside MainFrame before authentication.
 */
public class LoginPanel extends JPanel {

    private JTextField     txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbRole;
    private JButton        btnLogin;
    private JLabel         lblError;
    private final UserDAO  userDAO = new UserDAO();

    // Callback: called when login succeeds with the User object
    private LoginCallback callback;

    public interface LoginCallback {
        void onLogin(User user);
    }

    public LoginPanel(LoginCallback cb) {
        this.callback = cb;
        setLayout(new BorderLayout());
        setBackground(new Color(0xF0F4F8));
        add(buildLeft(),   BorderLayout.WEST);
        add(buildCenter(), BorderLayout.CENTER);
    }

    // ── Left branding strip ───────────────────────────────────
    private JPanel buildLeft() {
        JPanel p = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = AppTheme.setup(g);
                // Deep blue gradient
                GradientPaint gp = new GradientPaint(
                    0,0,new Color(0x1E40AF),
                    getWidth(),getHeight(),new Color(0x1D4ED8));
                g2.setPaint(gp); g2.fillRect(0,0,getWidth(),getHeight());
                // Decorative circles
                g2.setColor(new Color(255,255,255,18));
                g2.fillOval(-40,-40,220,220);
                g2.fillOval(100,getHeight()-180,260,260);
                g2.dispose();
            }
        };
        p.setPreferredSize(new Dimension(360,0));
        p.setOpaque(false);

        JPanel inner = new JPanel(); inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(new EmptyBorder(0,40,0,40));

        // Logo
        JLabel logo = new JLabel("FMS") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=AppTheme.setup(g);
                g2.setColor(new Color(255,255,255,40)); g2.fillOval(0,0,64,64);
                g2.setColor(Color.WHITE); g2.setFont(new Font("Segoe UI",Font.BOLD,24));
                FontMetrics fm=g2.getFontMetrics();
                g2.drawString("F",32-fm.stringWidth("F")/2,36);
                g2.dispose();
            }
        };
        logo.setPreferredSize(new Dimension(64,64)); logo.setMaximumSize(new Dimension(64,64));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel("Faculty Management System");
        title.setFont(new Font("Segoe UI",Font.BOLD,20));
        title.setForeground(Color.WHITE); title.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.setMaximumSize(new Dimension(280,30));

        JLabel sub = new JLabel("<html>University of Ruhuna<br>Faculty of Technology</html>");
        sub.setFont(new Font("Segoe UI",Font.PLAIN,13));
        sub.setForeground(new Color(255,255,255,160)); sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        inner.add(Box.createVerticalStrut(30));
        inner.add(logo); inner.add(Box.createVerticalStrut(20));
        inner.add(title); inner.add(Box.createVerticalStrut(8));
        inner.add(sub);   inner.add(Box.createVerticalStrut(40));

        // Feature list
        String[][] features = {
            {"Students","Marks, GPA, Attendance"},
            {"Lecturers","Upload marks, Check eligibility"},
            {"Admins","Full system control"},
            {"Tech Officers","Attendance & Medical"},
        };
        for (String[] feat : features) {
            JPanel row = new JPanel(new BorderLayout(12,0)); row.setOpaque(false);
            row.setMaximumSize(new Dimension(280,40)); row.setAlignmentX(Component.LEFT_ALIGNMENT);
            JPanel dot = new JPanel() {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2=AppTheme.setup(g);
                    g2.setColor(new Color(255,255,255,50)); g2.fillOval(0,4,12,12);
                    g2.setColor(Color.WHITE); g2.fillOval(3,7,6,6); g2.dispose();
                }
                @Override public boolean isOpaque() { return false; }
            };
            dot.setPreferredSize(new Dimension(14,20)); dot.setOpaque(false);
            JPanel txt = new JPanel(); txt.setOpaque(false);
            txt.setLayout(new BoxLayout(txt,BoxLayout.Y_AXIS));
            JLabel t1=new JLabel(feat[0]); t1.setFont(new Font("Segoe UI",Font.BOLD,13)); t1.setForeground(Color.WHITE);
            JLabel t2=new JLabel(feat[1]); t2.setFont(new Font("Segoe UI",Font.PLAIN,11)); t2.setForeground(new Color(255,255,255,150));
            txt.add(t1); txt.add(t2);
            row.add(dot,BorderLayout.WEST); row.add(txt,BorderLayout.CENTER);
            inner.add(row); inner.add(Box.createVerticalStrut(12));
        }

        p.add(inner); return p;
    }

    // ── Center login card ─────────────────────────────────────
    private JPanel buildCenter() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(new Color(0xF0F4F8));

        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=AppTheme.setup(g);
                // Shadow
                for (int i=0;i<8;i++) {
                    g2.setColor(new Color(0,0,0,4));
                    g2.fill(new RoundRectangle2D.Float(i,i+2,getWidth()-i*2,getHeight()-i*2,16,16));
                }
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth()-6,getHeight()-6,16,16));
                g2.setColor(new Color(0xE5E7EB)); g2.setStroke(new BasicStroke(0.8f));
                g2.draw(new RoundRectangle2D.Float(0.5f,0.5f,getWidth()-7,getHeight()-7,16,16));
                g2.dispose();
            }
            @Override public boolean isOpaque() { return false; }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(40,44,40,44));
        card.setPreferredSize(new Dimension(440, 520));
        card.setMaximumSize(new Dimension(440, 520));

        // Header
        JLabel hdr = new JLabel("Welcome back");
        hdr.setFont(new Font("Segoe UI",Font.BOLD,26)); hdr.setForeground(AppTheme.TEXT_DARK);
        hdr.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel sub = new JLabel("Sign in to your account to continue");
        sub.setFont(AppTheme.F_BODY); sub.setForeground(AppTheme.TEXT_MUTED);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Role selector tabs
        JPanel roleTabs = buildRoleTabs();
        roleTabs.setAlignmentX(Component.LEFT_ALIGNMENT);
        roleTabs.setMaximumSize(new Dimension(Integer.MAX_VALUE,44));

        // Username
        JLabel lblU = AppTheme.fieldLabel("USERNAME");
        lblU.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtUsername = AppTheme.textField("Enter your username");
        txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE,44));

        // Password
        JLabel lblP = AppTheme.fieldLabel("PASSWORD");
        lblP.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtPassword = AppTheme.passwordField("Enter your password");
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE,44));

        // Error
        lblError = new JLabel(" ");
        lblError.setFont(AppTheme.F_SMALL); lblError.setForeground(AppTheme.DANGER);
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Login button
        btnLogin = AppTheme.primaryBtn("Sign In  →");
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE,46));
        btnLogin.setBorder(new EmptyBorder(12,20,12,20));
        btnLogin.addActionListener(e -> doLogin());
        txtPassword.addActionListener(e -> doLogin());

        // Hint
        JPanel hintRow = new JPanel(new FlowLayout(FlowLayout.LEFT,6,0));
        hintRow.setOpaque(false); hintRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        hintRow.setMaximumSize(new Dimension(Integer.MAX_VALUE,28));
        JLabel hintLbl = new JLabel("Test accounts:"); hintLbl.setFont(AppTheme.F_SMALL); hintLbl.setForeground(AppTheme.TEXT_MUTED);
        hintRow.add(hintLbl);
        for (String[] ac : new String[][]{{"admin01","admin"},{"lec01","lec"},{"stu001","stu"},{"tech01","tech"}}) {
            JButton chip = buildChip(ac[0], ac[1]);
            hintRow.add(chip);
        }

        card.add(hdr); card.add(Box.createVerticalStrut(4));
        card.add(sub); card.add(Box.createVerticalStrut(24));
        card.add(roleTabs); card.add(Box.createVerticalStrut(20));
        card.add(lblU); card.add(Box.createVerticalStrut(4));
        card.add(txtUsername); card.add(Box.createVerticalStrut(16));
        card.add(lblP); card.add(Box.createVerticalStrut(4));
        card.add(txtPassword); card.add(Box.createVerticalStrut(8));
        card.add(lblError); card.add(Box.createVerticalStrut(12));
        card.add(btnLogin); card.add(Box.createVerticalStrut(20));
        card.add(AppTheme.separator()); card.add(Box.createVerticalStrut(16));
        card.add(hintRow);

        outer.add(card); return outer;
    }

    // ── Role tab strip ────────────────────────────────────────
    private final String[] ROLES = {"Admin","Lecturer","Student","Tech Officer"};
    private final String[] ROLE_KEYS = {"admin","lecturer","student","tech_officer"};
    private int selectedRole = 2; // default Student
    private JButton[] roleTabBtns;

    private JPanel buildRoleTabs() {
        JPanel p = new JPanel(new GridLayout(1,4,4,0)); p.setOpaque(false);
        roleTabBtns = new JButton[ROLES.length];
        for (int i=0; i<ROLES.length; i++) {
            final int idx = i;
            roleTabBtns[i] = new JButton(ROLES[i]) {
                boolean hov=false;
                { addMouseListener(new MouseAdapter(){
                    public void mouseEntered(MouseEvent e){hov=true;repaint();}
                    public void mouseExited(MouseEvent e){hov=false;repaint();}
                }); }
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2=AppTheme.setup(g);
                    boolean act = selectedRole==idx;
                    g2.setColor(act ? AppTheme.PRIMARY : (hov ? AppTheme.PRIMARY_LIGHT : new Color(0xF3F4F6)));
                    g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),8,8));
                    g2.dispose(); super.paintComponent(g);
                }
            };
            roleTabBtns[i].setFont(new Font("Segoe UI",Font.BOLD,11));
            roleTabBtns[i].setForeground(i==selectedRole ? Color.WHITE : AppTheme.TEXT_BODY);
            roleTabBtns[i].setContentAreaFilled(false); roleTabBtns[i].setBorderPainted(false);
            roleTabBtns[i].setFocusPainted(false);
            roleTabBtns[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            roleTabBtns[i].setBorder(new EmptyBorder(8,4,8,4));
            roleTabBtns[i].addActionListener(e -> {
                selectedRole = idx;
                for (int j=0;j<ROLES.length;j++) {
                    roleTabBtns[j].setForeground(j==idx ? Color.WHITE : AppTheme.TEXT_BODY);
                    roleTabBtns[j].repaint();
                }
            });
            p.add(roleTabBtns[i]);
        }
        return p;
    }

    private JButton buildChip(String username, String pwdPrefix) {
        JButton b = new JButton(username) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=AppTheme.setup(g);
                g2.setColor(getModel().isRollover() ? AppTheme.PRIMARY_LIGHT : new Color(0xF3F4F6));
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),20,20));
                g2.dispose(); super.paintComponent(g);
            }
        };
        b.setFont(new Font("Consolas",Font.PLAIN,11)); b.setForeground(AppTheme.PRIMARY);
        b.setContentAreaFilled(false); b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); b.setBorder(new EmptyBorder(2,8,2,8));
        b.addActionListener(e -> {
            txtUsername.setText(username);
            String pw = username.equals("admin01") ? "admin123" :
                        username.equals("lec01") ? "lec123" :
                        username.equals("stu001") ? "stu123" : "tech123";
            txtPassword.setText(pw);
        });
        return b;
    }

    private void doLogin() {
        String u = txtUsername.getText().trim();
        String p = new String(txtPassword.getPassword()).trim();
        String selectedRoleKey = ROLE_KEYS[selectedRole];
        lblError.setText(" ");
        if (u.isEmpty() || p.isEmpty()) { lblError.setText("Please enter username and password."); shake(); return; }
        User user = userDAO.login(u, p);
        if (user == null) { lblError.setText("Incorrect username or password."); txtPassword.setText(""); shake(); return; }
        if (!selectedRoleKey.equals(user.getRole())) {
            lblError.setText("Selected role does not match this account.");
            txtPassword.setText("");
            shake();
            return;
        }
        callback.onLogin(user);
    }

    /** Shake animation */
    private void shake() {
        Point orig = getLocationOnScreen();
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        Timer t = new Timer(30, null);
        int[] s={0}; int[] offs={-8,8,-6,6,-4,4,-2,2,0};
        t.addActionListener(e -> {
            if (s[0]<offs.length) frame.setLocation(frame.getX()+offs[s[0]++], frame.getY());
            else t.stop();
        });
        t.start();
    }
}
