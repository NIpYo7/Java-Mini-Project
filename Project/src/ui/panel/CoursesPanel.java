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

public class CoursesPanel extends BasePanel {
    private final CourseDAO courseDAO=new CourseDAO();
    public CoursesPanel(User user){super(user);build();}

    private void build(){
        JPanel content=pageContent();
        JLabel title=AppTheme.sectionTitle("Courses"); title.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel sub=new JLabel("All courses for Semester II 2025."); sub.setFont(AppTheme.F_BODY); sub.setForeground(AppTheme.TEXT_MUTED); sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title); content.add(sub); vgap(content,20);

        String[] cols={"Code","Course Name","Theory Cr.","Practical Cr.","Has Practical","Semester"};
        DefaultTableModel tm=model(cols);
        List<Course> list="student".equals(user.getRole())?courseDAO.getCoursesForStudent(user.getId()):
                          "lecturer".equals(user.getRole())?courseDAO.getCoursesForLecturer(user.getId()):
                          courseDAO.getAllCourses();
        list.forEach(c->tm.addRow(new Object[]{c.getCourseCode(),c.getCourseName(),c.getCreditTheory(),c.getCreditPractical(),c.isHasPractical()?"Yes":"No",c.getSemester()}));
        JTable t=AppTheme.styledTable(tm);

        JPanel card=AppTheme.card(12); card.setLayout(new BorderLayout()); card.setBorder(new EmptyBorder(0,0,0,0));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(AppTheme.scrollTable(t),BorderLayout.CENTER);
        content.add(card); add(scrollWrap(content));
    }
}

