package ui;

import model.User;
import ui.theme.AppTheme;
import ui.panels.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.*;


public class MainFrame extends JFrame {

    private JPanel    contentPanel;   // CardLayout holder
    private CardLayout cardLayout;
    private JPanel    sidebar;
    private JLabel    headerUserLbl;
    private JLabel    headerRoleLbl;
    private final Map<String, JButton> navBtns = new LinkedHashMap<>();
    private String    activeCard = "";
    private User      currentUser;

    public MainFrame() {
        setTitle("Faculty Management System");
        setSize(1280, 780);
        setMinimumSize(new Dimension(1000, 650));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        AppTheme.apply();
        showLogin();
    }

    // ── Show login screen ─────────────────────────────────────
    private void showLogin() {
        getContentPane().removeAll();
        LoginPanel login = new LoginPanel(user -> {
            this.currentUser = user;
            showDashboard(user);
        });
        getContentPane().add(login);
        revalidate(); repaint();
    }

    // ── Build main dashboard layout ───────────────────────────
    private void showDashboard(User user) {
        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout(0,0));

        // Sidebar
        sidebar = buildSidebar(user);
        getContentPane().add(sidebar, BorderLayout.WEST);

        // Right side
        JPanel right = new JPanel(new BorderLayout(0,0));
        right.setBackground(AppTheme.BG_PAGE);
        right.add(buildHeader(user), BorderLayout.NORTH);

        // Content area
        cardLayout   = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(AppTheme.BG_PAGE);
        right.add(contentPanel, BorderLayout.CENTER);
        getContentPane().add(right, BorderLayout.CENTER);

        // Register panels based on role
        registerPanels(user);

        revalidate(); repaint();

        // Show first panel (dashboard)
        if (!navBtns.isEmpty()) {
            String first = navBtns.keySet().iterator().next();
            switchTo(first);
        }
    }

    // ── Sidebar ───────────────────────────────────────────────
    private JPanel buildSidebar(User user) {
        JPanel s = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=AppTheme.setup(g);
                g2.setColor(AppTheme.BG_SIDEBAR); g2.fillRect(0,0,getWidth(),getHeight());
                // Shadow line on right
                g2.setColor(new Color(0,0,0,30));
                g2.drawLine(getWidth()-1,0,getWidth()-1,getHeight());
                g2.dispose();
            }
        };
        s.setPreferredSize(new Dimension(230,0));
        s.setLayout(new BorderLayout()); s.setOpaque(false);

        // Top: logo area
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false); top.setBorder(new EmptyBorder(0,0,8,0));
        JPanel logoBar = new JPanel(new FlowLayout(FlowLayout.LEFT,16,16));
        logoBar.setOpaque(false);
        JLabel logoIcon = new JLabel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=AppTheme.setup(g);
                g2.setColor(AppTheme.PRIMARY); g2.fillRoundRect(0,0,32,32,8,8);
                g2.setColor(Color.WHITE); g2.setFont(new Font("Segoe UI",Font.BOLD,16));
                g2.drawString("F",9,22); g2.dispose();
            }
        };
        logoIcon.setPreferredSize(new Dimension(32,32));
        JLabel appName = new JLabel("FacultyMS");
        appName.setFont(new Font("Segoe UI",Font.BOLD,16)); appName.setForeground(Color.WHITE);
        logoBar.add(logoIcon); logoBar.add(appName);
        top.add(logoBar, BorderLayout.CENTER);
        top.add(new JSeparator(){{setForeground(new Color(255,255,255,20));}}, BorderLayout.SOUTH);
        s.add(top, BorderLayout.NORTH);

        // Nav items (populated after panels are registered)
        JPanel navPanel = new JPanel();
        navPanel.setOpaque(false);
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBorder(new EmptyBorder(12,0,0,0));
        s.putClientProperty("navPanel", navPanel);
        JScrollPane navScroll = new JScrollPane(navPanel);
        navScroll.setOpaque(false); navScroll.getViewport().setOpaque(false);
        navScroll.setBorder(null);
        navScroll.getVerticalScrollBar().setUI(new AppTheme.SlimScrollBar());
        navScroll.getVerticalScrollBar().setPreferredSize(new Dimension(4,0));
        s.add(navScroll, BorderLayout.CENTER);

        // Bottom: user info
        JPanel bottom = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=AppTheme.setup(g);
                g2.setColor(new Color(255,255,255,8)); g2.fillRect(0,0,getWidth(),getHeight()); g2.dispose();
            }
        };
        bottom.setOpaque(false); bottom.setBorder(new EmptyBorder(12,16,16,16));
        // Avatar
        JPanel avatar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=AppTheme.setup(g);
                BufferedImage img = buildAvatarImage(user.getProfilePic(), 36, user.getFullName());
                g2.drawImage(img, 0, 0, null);
                g2.dispose();
            }
            @Override public boolean isOpaque(){return false;}
        };
        avatar.setPreferredSize(new Dimension(36,36)); avatar.setOpaque(false);
        JPanel namePanel = new JPanel(); namePanel.setOpaque(false);
        namePanel.setLayout(new BoxLayout(namePanel,BoxLayout.Y_AXIS));
        String displayName = user.getFullName().length()>15 ? user.getFullName().substring(0,13)+"…" : user.getFullName();
        JLabel nameLbl=new JLabel(displayName); nameLbl.setFont(new Font("Segoe UI",Font.BOLD,12)); nameLbl.setForeground(Color.WHITE);
        JLabel roleLbl=new JLabel(formatRole(user.getRole())); roleLbl.setFont(AppTheme.F_SMALL); roleLbl.setForeground(new Color(0x94A3B8));
        namePanel.add(nameLbl); namePanel.add(roleLbl);
        bottom.add(avatar,BorderLayout.WEST);
        JPanel nameWrap=new JPanel(new GridBagLayout()); nameWrap.setOpaque(false); nameWrap.add(namePanel); nameWrap.setBorder(new EmptyBorder(0,8,0,0));
        bottom.add(nameWrap,BorderLayout.CENTER);
        JSeparator sep2=new JSeparator(); sep2.setForeground(new Color(255,255,255,15));
        JPanel botWrap=new JPanel(new BorderLayout()); botWrap.setOpaque(false);
        botWrap.add(sep2,BorderLayout.NORTH); botWrap.add(bottom,BorderLayout.CENTER);
        s.add(botWrap,BorderLayout.SOUTH);
        return s;
    }

    private void addNavItem(String key, String label, String icon) {
        JPanel navPanel = (JPanel) sidebar.getClientProperty("navPanel");
        JButton btn = new JButton() {
            boolean hov=false;
            { addMouseListener(new MouseAdapter(){
                public void mouseEntered(MouseEvent e){hov=true;repaint();}
                public void mouseExited(MouseEvent e){hov=false;repaint();}
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=AppTheme.setup(g);
                boolean act = key.equals(activeCard);
                if (act) {
                    g2.setColor(new Color(37,99,235,30));
                    g2.fill(new RoundRectangle2D.Float(8,2,getWidth()-16,getHeight()-4,8,8));
                    g2.setColor(AppTheme.PRIMARY);
                    g2.setStroke(new BasicStroke(3f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
                    g2.drawLine(3,6,3,getHeight()-6);
                } else if (hov) {
                    g2.setColor(new Color(255,255,255,10));
                    g2.fill(new RoundRectangle2D.Float(8,2,getWidth()-16,getHeight()-4,8,8));
                }
                g2.dispose(); super.paintComponent(g);
            }
        };
        btn.setText("  " + icon + "  " + label);
        btn.setForeground(key.equals(activeCard) ? Color.WHITE : AppTheme.TEXT_SIDEBAR);
        btn.setFont(key.equals(activeCard) ? AppTheme.F_BOLD : AppTheme.F_BODY);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setContentAreaFilled(false); btn.setBorderPainted(false); btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(11,12,11,12));
        btn.setMaximumSize(new Dimension(230,44));
        btn.addActionListener(e -> switchTo(key));
        navBtns.put(key, btn);
        navPanel.add(btn);

        // Section separator
        if (key.equals("notices")) {
            JPanel sep = new JPanel(); sep.setOpaque(false); sep.setMaximumSize(new Dimension(230,1));
            sep.setBorder(BorderFactory.createMatteBorder(0,16,0,16,new Color(255,255,255,15)));
            navPanel.add(sep); navPanel.add(Box.createVerticalStrut(4));
        }
    }

    private void switchTo(String key) {
        activeCard = key;
        cardLayout.show(contentPanel, key);
        // Update nav button styles
        navBtns.forEach((k, btn) -> {
            boolean act = k.equals(key);
            btn.setForeground(act ? Color.WHITE : AppTheme.TEXT_SIDEBAR);
            btn.setFont(act ? AppTheme.F_BOLD : AppTheme.F_BODY);
            btn.repaint();
        });
        // Update header
        headerUserLbl.setText(currentUser.getFullName() + "  ·  " + formatRole(currentUser.getRole()));
    }

    public void navigateTo(String key) {
        if (navBtns.containsKey(key)) {
            switchTo(key);
        }
    }

    private BufferedImage buildAvatarImage(String imagePath, int size, String fullName) {
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
            g2.setColor(AppTheme.PRIMARY);
            g2.fillOval(0,0,size,size);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI",Font.BOLD,14));
            String ini = fullName != null && fullName.length()>=2 ? fullName.substring(0,2).toUpperCase() : "??";
            FontMetrics fm=g2.getFontMetrics();
            g2.drawString(ini,size/2-fm.stringWidth(ini)/2,size/2+fm.getAscent()/3);
        }

        g2.dispose();
        return canvas;
    }

    // ── Top header ────────────────────────────────────────────
    private JPanel buildHeader(User user) {
        JPanel h = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=AppTheme.setup(g);
                g2.setColor(Color.WHITE); g2.fillRect(0,0,getWidth(),getHeight());
                g2.setColor(AppTheme.BORDER); g2.drawLine(0,getHeight()-1,getWidth(),getHeight()-1);
                g2.dispose();
            }
        };
        h.setOpaque(false); h.setPreferredSize(new Dimension(0,60)); h.setBorder(new EmptyBorder(0,24,0,24));

        JLabel titleLbl = new JLabel("Faculty Management System");
        titleLbl.setFont(AppTheme.F_H2); titleLbl.setForeground(AppTheme.TEXT_DARK);

        headerUserLbl = new JLabel(user.getFullName()+" · "+formatRole(user.getRole()));
        headerUserLbl.setFont(AppTheme.F_BODY); headerUserLbl.setForeground(AppTheme.TEXT_MUTED);
        headerRoleLbl = new JLabel(formatRole(user.getRole()));

        // Logout
        JButton logoutBtn = new JButton("Logout") {
            boolean hov=false;
            { addMouseListener(new MouseAdapter(){
                public void mouseEntered(MouseEvent e){hov=true;repaint();}
                public void mouseExited(MouseEvent e){hov=false;repaint();}
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=AppTheme.setup(g);
                g2.setColor(hov ? AppTheme.DANGER_LIGHT : AppTheme.BG_PAGE);
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),8,8));
                g2.dispose(); super.paintComponent(g);
            }
        };
        logoutBtn.setFont(AppTheme.F_BTN); logoutBtn.setForeground(AppTheme.DANGER);
        logoutBtn.setContentAreaFilled(false); logoutBtn.setBorderPainted(false); logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.setBorder(new EmptyBorder(8,14,8,14));
        logoutBtn.addActionListener(e -> {
            navBtns.clear(); currentUser=null;
            showLogin();
        });

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0)); right.setOpaque(false);
        right.add(headerUserLbl); right.add(logoutBtn);
        h.add(titleLbl,BorderLayout.WEST); h.add(right,BorderLayout.EAST);
        return h;
    }

    // ── Register panels ───────────────────────────────────────
    private void registerPanels(User user) {
        String role = user.getRole();
        switch (role) {
            case "admin":
                reg("dashboard","Dashboard","⊞", new AdminDashboardPanel(user));
                reg("users",    "Users",    "👤", new UsersPanel(user));
                reg("courses",  "Courses",  "📚", new CoursesPanel(user));
                reg("marks",    "Marks",    "📊", new MarksPanel(user));
                reg("attendance","Attendance","✓", new AttendancePanel(user));
                reg("medicals", "Medicals", "🏥", new MedicalsPanel(user));
                reg("timetable","Timetable","🕐", new TimetablePanel(user));
                reg("notices",  "Notices",  "📌", new NoticesPanel(user));
                reg("profile",  "Profile",  "⚙", new ProfilePanel(user));
                break;
            case "lecturer":
                reg("dashboard","Dashboard","⊞", new LecturerDashboardPanel(user));
                reg("courses",  "Courses",  "📚", new CoursesPanel(user));
                reg("students", "Students", "👥", new LecturerStudentsPanel(user));
                reg("marks",    "Marks",    "📊", new MarksPanel(user));
                reg("attendance","Attendance","✓", new AttendancePanel(user));
                reg("materials","Materials","📄", new LectureMaterialsPanel(user));
                reg("notices",  "Notices",  "📌", new NoticesPanel(user));
                reg("profile",  "Profile",  "⚙", new ProfilePanel(user));
                break;
            case "student":
                reg("dashboard","Dashboard","⊞", new StudentDashboardPanel(user));
                reg("courses",  "Courses",  "📚", new CoursesPanel(user));
                reg("marks",    "Marks",    "📊", new MarksPanel(user));
                reg("attendance","Attendance","✓", new AttendancePanel(user));
                reg("materials","Materials","📄", new LectureMaterialsPanel(user));
                reg("medicals", "Medicals", "🏥", new MedicalsPanel(user));
                reg("timetable","Timetable","🕐", new TimetablePanel(user));
                reg("notices",  "Notices",  "📌", new NoticesPanel(user));
                reg("profile",  "Profile",  "⚙", new ProfilePanel(user));
                break;
            case "tech_officer":
                reg("dashboard","Dashboard","⊞", new TechOfficerDashboardPanel(user));
                reg("attendance","Attendance","✓", new AttendancePanel(user));
                reg("medicals", "Medicals", "🏥", new MedicalsPanel(user));
                reg("timetable","Timetable","🕐", new TimetablePanel(user));
                reg("notices",  "Notices",  "📌", new NoticesPanel(user));
                reg("profile",  "Profile",  "⚙", new ProfilePanel(user));
                break;
        }
    }

    private void reg(String key, String label, String icon, JPanel panel) {
        contentPanel.add(panel, key);
        addNavItem(key, label, icon);
    }

    private String formatRole(String role) {
        switch(role){
            case "admin": return "Administrator";
            case "lecturer": return "Lecturer";
            case "student": return "Student";
            case "tech_officer": return "Technical Officer";
            default: return role;
        }
    }
}
