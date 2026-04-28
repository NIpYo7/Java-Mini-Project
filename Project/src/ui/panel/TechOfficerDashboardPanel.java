package ui.panels;

import ui.theme.AppTheme;
import dao.*;
import model.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.*;
import java.time.LocalDate;

public class TechOfficerDashboardPanel extends BasePanel {

    private final AttendanceDAO attendDAO = new AttendanceDAO();
    private final UserDAO userDAO = new UserDAO();

    public TechOfficerDashboardPanel(User user) { super(user); build(); }

    private void build() {
        JPanel content=pageContent();

        JLabel greet=new JLabel("Technical Officer Portal — "+user.getFullName().split(" ")[0]);
        greet.setFont(AppTheme.F_TITLE); greet.setForeground(AppTheme.TEXT_DARK); greet.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel sub=new JLabel("Record attendance, manage medicals, and monitor student presence.");
        sub.setFont(AppTheme.F_BODY); sub.setForeground(AppTheme.TEXT_MUTED); sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(greet); vgap(content,4); content.add(sub); vgap(content,24);

        // Stats (live from database)
        int studentCount = userDAO.countByRole("student");
        int enrollments = attendDAO.countStudentCourseEnrollments();
        double avgPct = enrollments > 0 ? attendDAO.getAverageAttendanceAcrossEnrollments() : 0;
        int pendingMedicals = attendDAO.countPendingMedicals();
        String today = LocalDate.now().toString();
        int sessionsToday = attendDAO.countDistinctSessionsOnDate(today);

        String avgLabel = enrollments > 0 ? String.format("%.0f%%", avgPct) : "—";
        JPanel stats=new JPanel(new GridLayout(1,4,16,0)); stats.setOpaque(false);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE,100)); stats.setAlignmentX(Component.LEFT_ALIGNMENT);
        stats.add(AppTheme.statCard(String.valueOf(studentCount),"Students","Registered in system",AppTheme.PRIMARY,AppTheme.PRIMARY_LIGHT));
        stats.add(AppTheme.statCard(avgLabel,"Avg Attendance","Mean across enrollments",AppTheme.SUCCESS,AppTheme.SUCCESS_LIGHT));
        stats.add(AppTheme.statCard(String.valueOf(pendingMedicals),"Pending Medicals","Awaiting approval",AppTheme.WARNING,AppTheme.WARNING_LIGHT));
        stats.add(AppTheme.statCard(String.valueOf(sessionsToday),"Sessions Today","Recorded for "+today,AppTheme.TEAL,AppTheme.TEAL_LIGHT));
        content.add(stats); vgap(content,24);

        // Quick access buttons
        JPanel btnGrid=new JPanel(new GridLayout(2,3,14,14)); btnGrid.setOpaque(false);
        btnGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE,120)); btnGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGrid.add(bigActionBtn("Record Attendance","Mark today's session",AppTheme.PRIMARY, "attendance"));
        btnGrid.add(bigActionBtn("Approve Medical","Review pending certificates",AppTheme.SUCCESS, "medicals"));
        btnGrid.add(bigActionBtn("Batch Summary","View all attendance",AppTheme.TEAL, "attendance"));
        btnGrid.add(bigActionBtn("Add Medical","Add student medical",AppTheme.WARNING, "medicals"));
        btnGrid.add(bigActionBtn("View Timetable","Today's schedule",AppTheme.PURPLE, "timetable"));
        btnGrid.add(bigActionBtn("View Notices","Latest announcements",AppTheme.DANGER, "notices"));
        content.add(btnGrid);
        add(scrollWrap(content));
    }

    private JPanel bigActionBtn(String title, String sub, Color color, String cardKey) {
        JPanel card=new JPanel() {
            boolean hov=false;
            { addMouseListener(new java.awt.event.MouseAdapter(){
                public void mouseEntered(java.awt.event.MouseEvent e){hov=true;repaint();}
                public void mouseExited(java.awt.event.MouseEvent e){hov=false;repaint();}
                public void mouseClicked(java.awt.event.MouseEvent e){ goTo(cardKey); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=AppTheme.setup(g);
                g2.setColor(hov?new Color(color.getRed(),color.getGreen(),color.getBlue(),220):color);
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),12,12));
                g2.dispose();
            }
            @Override public boolean isOpaque(){return false;}
        };
        card.setLayout(new BoxLayout(card,BoxLayout.Y_AXIS)); card.setBorder(new EmptyBorder(18,20,18,20));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JLabel t=new JLabel(title); t.setFont(AppTheme.F_BOLD); t.setForeground(Color.WHITE);
        JLabel s=new JLabel(sub); s.setFont(AppTheme.F_SMALL); s.setForeground(new Color(255,255,255,180));
        card.add(t); card.add(Box.createVerticalStrut(4)); card.add(s);
        return card;
    }
}
