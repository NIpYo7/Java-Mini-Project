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



public class LecturerDashboardPanel extends BasePanel {

    private final CourseDAO    courseDAO  = new CourseDAO();
    private final MarksDAO     marksDAO   = new MarksDAO();
    private final AttendanceDAO attendDAO = new AttendanceDAO();

    public LecturerDashboardPanel(User user) { super(user); build(); }

    private void build() {
        JPanel content = pageContent();

        JLabel greet=new JLabel("Lecturer Portal — "+user.getFullName().split(" ")[0]);
        greet.setFont(AppTheme.F_TITLE); greet.setForeground(AppTheme.TEXT_DARK); greet.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel sub=new JLabel("Manage your courses, marks, and student eligibility.");
        sub.setFont(AppTheme.F_BODY); sub.setForeground(AppTheme.TEXT_MUTED); sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(greet); vgap(content,4); content.add(sub); vgap(content,24);

        // Quick-action buttons
        JPanel qBtns=new JPanel(new GridLayout(2,2,12,12)); qBtns.setOpaque(false);
        qBtns.setMaximumSize(new Dimension(Integer.MAX_VALUE,120)); qBtns.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton bMark=AppTheme.primaryBtn("Upload Marks");
        JButton bMat=AppTheme.ghostBtn("Course files");
        JButton bStu=AppTheme.ghostBtn("View Students");
        JButton bElig=AppTheme.successBtn("Check Eligibility");
        bMark.addActionListener(e -> goTo("marks"));
        bMat.addActionListener(e -> goTo("materials"));
        bStu.addActionListener(e -> goTo("students"));
        bElig.addActionListener(e -> goTo("attendance"));
        qBtns.add(bMark); qBtns.add(bMat); qBtns.add(bStu); qBtns.add(bElig);
        content.add(qBtns); vgap(content,24);

        // Stats row
        JPanel statsRow=new JPanel(new GridLayout(1,3,16,0)); statsRow.setOpaque(false);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE,100)); statsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsRow.add(AppTheme.statCard("72.4","Avg Mark","Semester average",AppTheme.PRIMARY,AppTheme.PRIMARY_LIGHT));
        statsRow.add(AppTheme.statCard("84%","Avg Attendance","Combined theory+practical",AppTheme.SUCCESS,AppTheme.SUCCESS_LIGHT));
        statsRow.add(AppTheme.statCard("18/20","Eligibility","Students eligible for final",AppTheme.TEAL,AppTheme.TEAL_LIGHT));
        content.add(statsRow); vgap(content,24);

        // Courses table
        JPanel cCard=AppTheme.card(12); cCard.setLayout(new BorderLayout(0,12)); cCard.setBorder(new EmptyBorder(20,20,20,20));
        cCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel cl=AppTheme.sectionTitle("My Courses"); cl.setBorder(new EmptyBorder(0,0,0,0));
        String[] cols={"Code","Course Name","Theory Cr.","Practical Cr.","Has Practical"};
        DefaultTableModel tm=model(cols);
        courseDAO.getCoursesForLecturer(user.getId()).forEach(c->
            tm.addRow(new Object[]{c.getCourseCode(),c.getCourseName(),c.getCreditTheory(),c.getCreditPractical(),c.isHasPractical()?"Yes":"No"}));
        JTable t=AppTheme.styledTable(tm);
        cCard.add(cl,BorderLayout.NORTH); cCard.add(AppTheme.scrollTable(t),BorderLayout.CENTER);
        cCard.setMaximumSize(new Dimension(Integer.MAX_VALUE,280));
        content.add(cCard);
        add(scrollWrap(content));
    }
}


