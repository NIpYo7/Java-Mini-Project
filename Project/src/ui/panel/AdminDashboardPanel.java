package ui.panels;

import ui.theme.AppTheme;
import dao.*;
import model.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.*;
import java.util.List;

/**
 * AdminDashboardPanel.java
 * All stat cards and bar chart pull live data from database.
 */
public class AdminDashboardPanel extends BasePanel {

    private final UserDAO       userDAO       = new UserDAO();
    private final CourseDAO     courseDAO     = new CourseDAO();
    private final NoticeDAO     noticeDAO     = new NoticeDAO();
    private final AttendanceDAO attendanceDAO = new AttendanceDAO();

    public AdminDashboardPanel(User user) {
        super(user);
        build();
    }

    private void build() {
        JPanel content = pageContent();

        JLabel greet = new JLabel("Good day, " + user.getFullName().split(" ")[0] + "  👋");
        greet.setFont(AppTheme.F_TITLE); greet.setForeground(AppTheme.TEXT_DARK);
        greet.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel sub = new JLabel("Here's what's happening in the faculty today.");
        sub.setFont(AppTheme.F_BODY); sub.setForeground(AppTheme.TEXT_MUTED);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(greet); vgap(content,4); content.add(sub); vgap(content,24);

        // Stat cards — all live from DB
        long students  = userDAO.getAllStudents().size();
        long lecturers = userDAO.getAllUsers().stream().filter(u->"lecturer".equals(u.getRole())).count();
        long courses   = courseDAO.getAllCourses().size();
        long notices   = noticeDAO.getAllNotices().size();

        JPanel stats = new JPanel(new GridLayout(1,4,16,0)); stats.setOpaque(false);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE,110));
        stats.setAlignmentX(Component.LEFT_ALIGNMENT);
        stats.add(AppTheme.statCard(String.valueOf(students),"Students","Enrolled this semester",AppTheme.PRIMARY,AppTheme.PRIMARY_LIGHT));
        stats.add(AppTheme.statCard(String.valueOf(lecturers),"Lecturers","Active faculty members",AppTheme.TEAL,AppTheme.TEAL_LIGHT));
        stats.add(AppTheme.statCard(String.valueOf(courses),"Courses","Active this semester",AppTheme.SUCCESS,AppTheme.SUCCESS_LIGHT));
        stats.add(AppTheme.statCard(String.valueOf(notices),"Notices","Active notices",AppTheme.WARNING,AppTheme.WARNING_LIGHT));
        content.add(stats); vgap(content,24);

        // Row 2: recent users + live attendance chart
        JPanel row2 = new JPanel(new GridLayout(1,2,16,0)); row2.setOpaque(false);
        row2.setAlignmentX(Component.LEFT_ALIGNMENT);
        row2.setMaximumSize(new Dimension(Integer.MAX_VALUE,320));

        // Recent users table
        JPanel usersCard = AppTheme.card(12); usersCard.setLayout(new BorderLayout(0,12)); usersCard.setBorder(new EmptyBorder(20,20,20,20));
        JLabel ul = AppTheme.sectionTitle("Recent Users"); ul.setBorder(new EmptyBorder(0,0,0,0));
        String[] cols={"Username","Full Name","Role","Department"};
        DefaultTableModel tm = model(cols);
        for (User u : userDAO.getAllUsers()) {
            if (tm.getRowCount()>=8) break;
            tm.addRow(new Object[]{u.getUsername(),u.getFullName(),u.getRole(),u.getDepartment()});
        }
        JTable table = AppTheme.styledTable(tm);
        usersCard.add(ul,BorderLayout.NORTH); usersCard.add(AppTheme.scrollTable(table),BorderLayout.CENTER);

        // Live bar chart — reads attendance from DB per course
        List<Course> allCourses = courseDAO.getAllCourses();
        String[] chartLabels;
        int[] chartValues;
        if (allCourses.isEmpty()) {
            chartLabels = new String[]{"No Data"};
            chartValues = new int[]{0};
        } else {
            int n = Math.min(allCourses.size(), 6); // show up to 6 courses
            chartLabels = new String[n];
            chartValues = new int[n];
            for (int i = 0; i < n; i++) {
                Course c = allCourses.get(i);
                chartLabels[i] = c.getCourseCode();
                // Average attendance % across all enrolled students for this course
                Object[][] summary = attendanceDAO.getBatchAttendanceSummary(c.getId());
                if (summary.length == 0) {
                    chartValues[i] = 0;
                } else {
                    double sum = 0;
                    for (Object[] row : summary) {
                        // combined % is index 4
                        try { sum += Double.parseDouble(row[4].toString().replace("%","").trim()); } catch (Exception ignored) {}
                    }
                    chartValues[i] = (int) Math.round(sum / summary.length);
                }
            }
        }

        JPanel chartCard = AppTheme.card(12); chartCard.setLayout(new BorderLayout(0,12)); chartCard.setBorder(new EmptyBorder(20,20,20,20));
        JLabel cl = AppTheme.sectionTitle("Attendance Overview (Live)"); cl.setBorder(new EmptyBorder(0,0,0,0));
        chartCard.add(cl,BorderLayout.NORTH);
        chartCard.add(new BarChartPanel(chartLabels, chartValues), BorderLayout.CENTER);

        row2.add(usersCard); row2.add(chartCard);
        content.add(row2);

        add(scrollWrap(content));
    }

    // Live bar chart panel — accepts data from DB
    static class BarChartPanel extends JPanel {
        static final Color[] COLORS = {AppTheme.PRIMARY,AppTheme.TEAL,AppTheme.SUCCESS,AppTheme.WARNING,AppTheme.DANGER,AppTheme.PURPLE};
        final String[] labels;
        final int[] values;

        BarChartPanel(String[] labels, int[] values) {
            this.labels = labels;
            this.values = values;
            setOpaque(false);
        }

        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = AppTheme.setup(g);
            int w = getWidth()-20, h = getHeight()-40, n = labels.length;
            if (n == 0) return;
            int bw = Math.max(20, (w/n)-16), bx = 10;
            // Grid lines
            g2.setStroke(new BasicStroke(0.5f));
            for (int pct : new int[]{25,50,75,100}) {
                int y = h-(h*pct/100)+10;
                g2.setColor(AppTheme.BORDER); g2.drawLine(10,y,w+10,y);
                g2.setColor(AppTheme.TEXT_MUTED); g2.setFont(AppTheme.F_SMALL);
                g2.drawString(pct+"%",0,y+4);
            }
            // Bars
            for (int i = 0; i < n; i++) {
                int bh = Math.max(2, (int)(h * Math.min(values[i], 100) / 100.0));
                int by = h - bh + 10;
                Color col = COLORS[i % COLORS.length];
                // Shadow
                g2.setColor(new Color(0,0,0,12));
                g2.fill(new RoundRectangle2D.Float(bx+3,by+3,bw,bh,6,6));
                // Bar
                g2.setColor(col);
                g2.fill(new RoundRectangle2D.Float(bx,by,bw,bh,6,6));
                // Value label
                g2.setColor(AppTheme.TEXT_BODY); g2.setFont(new Font("Segoe UI",Font.BOLD,11));
                FontMetrics fm = g2.getFontMetrics();
                String v = values[i]+"%";
                g2.drawString(v, bx+(bw-fm.stringWidth(v))/2, by-4);
                // X label
                g2.setColor(AppTheme.TEXT_MUTED); g2.setFont(AppTheme.F_SMALL);
                FontMetrics fm2 = g2.getFontMetrics();
                String lbl = labels[i].length()>8 ? labels[i].substring(0,8) : labels[i];
                g2.drawString(lbl, bx+(bw-fm2.stringWidth(lbl))/2, h+28);
                bx += bw+16;
            }
            g2.dispose();
        }
    }
}
