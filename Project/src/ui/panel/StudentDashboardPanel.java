package ui.panels;

import ui.theme.AppTheme;
import dao.*;
import model.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.*;
import java.util.List;

public class StudentDashboardPanel extends BasePanel {

    private final CourseDAO     courseDAO  = new CourseDAO();
    private final MarksDAO      marksDAO   = new MarksDAO();
    private final AttendanceDAO attendDAO  = new AttendanceDAO();

    public StudentDashboardPanel(User user) { super(user); build(); }

    private void build() {
        JPanel content = pageContent();

        JLabel greet=new JLabel("My Dashboard — "+user.getFullName().split(" ")[0]);
        greet.setFont(AppTheme.F_TITLE); greet.setForeground(AppTheme.TEXT_DARK); greet.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel sub=new JLabel("Track your academic progress, marks, and attendance.");
        sub.setFont(AppTheme.F_BODY); sub.setForeground(AppTheme.TEXT_MUTED); sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(greet); vgap(content,4); content.add(sub); vgap(content,24);

        // GPA + eligibility row
        JPanel top=new JPanel(new GridLayout(1,3,16,0)); top.setOpaque(false);
        top.setMaximumSize(new Dimension(Integer.MAX_VALUE,110)); top.setAlignmentX(Component.LEFT_ALIGNMENT);

        double sgpa = marksDAO.calculateSGPA(user.getId());
        double cgpa = marksDAO.calculateCGPA(user.getId());
        top.add(AppTheme.statCard(String.format("%.2f",sgpa),"SGPA","Semester GPA",AppTheme.PRIMARY,AppTheme.PRIMARY_LIGHT));
        top.add(AppTheme.statCard(String.format("%.2f",cgpa),"CGPA","Cumulative GPA",AppTheme.TEAL,AppTheme.TEAL_LIGHT));

        // Real eligibility check: all enrolled courses must have >= 80% attendance
        List<Course> courses = courseDAO.getCoursesForStudent(user.getId());
        boolean eligible = !courses.isEmpty();
        for (Course c : courses) {
            double pct = attendDAO.getAttendancePercent(user.getId(), c.getId(), "all");
            if (pct < 80) { eligible = false; break; }
        }
        if (courses.isEmpty()) eligible = false;

        JPanel eligCard=AppTheme.card(12); eligCard.setLayout(new BorderLayout(0,8)); eligCard.setBorder(new EmptyBorder(18,22,18,22));
        JLabel eligVal=new JLabel(eligible?"Eligible":"Not Eligible");
        eligVal.setFont(new Font("Segoe UI",Font.BOLD,22));
        eligVal.setForeground(eligible?AppTheme.SUCCESS:AppTheme.DANGER);
        JLabel eligSub=new JLabel("Final Exam Eligibility"); eligSub.setFont(AppTheme.F_SMALL); eligSub.setForeground(AppTheme.TEXT_MUTED);
        String badgeText = courses.isEmpty() ? "No courses enrolled" : (eligible ? "✓ All courses ≥ 80%" : "✗ Some courses < 80%");
        JLabel statusBadge=AppTheme.badge(badgeText,
            eligible?AppTheme.SUCCESS:AppTheme.DANGER, eligible?AppTheme.SUCCESS_LIGHT:AppTheme.DANGER_LIGHT);
        JPanel eText=new JPanel(); eText.setOpaque(false); eText.setLayout(new BoxLayout(eText,BoxLayout.Y_AXIS));
        eText.add(eligVal); eText.add(eligSub); eText.add(Box.createVerticalStrut(6)); eText.add(statusBadge);
        eligCard.add(eText,BorderLayout.CENTER);
        top.add(eligCard);
        content.add(top); vgap(content,24);

        // Course progress
        JPanel progCard=AppTheme.card(12); progCard.setLayout(new BorderLayout(0,16)); progCard.setBorder(new EmptyBorder(20,20,20,20));
        progCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel pl=AppTheme.sectionTitle("Course Attendance Progress"); pl.setBorder(new EmptyBorder(0,0,0,0));
        progCard.add(pl,BorderLayout.NORTH);
        JPanel bars=new JPanel(); bars.setOpaque(false); bars.setLayout(new BoxLayout(bars,BoxLayout.Y_AXIS));
        for (Course c : courses) {
            double pct = attendDAO.getAttendancePercent(user.getId(), c.getId(), "all");
            int p = (int) Math.round(Math.max(0, Math.min(100, pct)));
            JPanel pb = AppTheme.progressBar(c.getCourseCode() + " — " + c.getCourseName(), p);
            pb.setAlignmentX(Component.LEFT_ALIGNMENT);
            bars.add(pb);
            bars.add(Box.createVerticalStrut(12));
        }
        if (courses.isEmpty()) {
            JLabel empty = new JLabel("No enrolled courses yet.");
            empty.setFont(AppTheme.F_SMALL);
            empty.setForeground(AppTheme.TEXT_MUTED);
            empty.setAlignmentX(Component.LEFT_ALIGNMENT);
            bars.add(empty);
        }
        JScrollPane bScroll=new JScrollPane(bars); bScroll.setBorder(null); bScroll.setOpaque(false); bScroll.getViewport().setOpaque(false);
        bScroll.getVerticalScrollBar().setUI(new AppTheme.SlimScrollBar());
        bScroll.getVerticalScrollBar().setPreferredSize(new Dimension(6,0));
        progCard.add(bScroll,BorderLayout.CENTER);
        progCard.setMaximumSize(new Dimension(Integer.MAX_VALUE,300));
        content.add(progCard);
        add(scrollWrap(content));
    }
}
