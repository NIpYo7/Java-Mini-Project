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

public class TimetablePanel extends BasePanel {
    private final NoticeDAO noticeDAO=new NoticeDAO();
    public TimetablePanel(User user){super(user);build();}

    private void build(){
        JPanel content=pageContent();
        JLabel title=AppTheme.sectionTitle("Weekly Timetable"); title.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel sub=new JLabel("Semester II 2025 — ICT Department"); sub.setFont(AppTheme.F_BODY); sub.setForeground(AppTheme.TEXT_MUTED); sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title); content.add(sub); vgap(content,20);

        JPanel card=AppTheme.card(12); card.setLayout(new BorderLayout(0,0)); card.setBorder(new EmptyBorder(20,20,20,20));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        String[] cols={"Day","Start","End","Course Code","Course Name","Room"};
        DefaultTableModel tm=model(cols);
        for(Object[] row:noticeDAO.getTimetable("ICT")) tm.addRow(row);
        JTable t=AppTheme.styledTable(tm);
        card.add(AppTheme.scrollTable(t),BorderLayout.CENTER);
        content.add(card);
        add(scrollWrap(content));
    }
}

