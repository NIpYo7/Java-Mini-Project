package ui.panels;

import ui.theme.AppTheme;
import dao.*;
import model.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.*;
import java.io.File;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;


public class MedicalsPanel extends BasePanel {

    private final AttendanceDAO attendDAO = new AttendanceDAO();
    private final CourseDAO     courseDAO = new CourseDAO();
    private final UserDAO       userDAO   = new UserDAO();

    public MedicalsPanel(User user) { super(user); build(); }

    private void build() {
        JPanel content = pageContent();
        JLabel title = AppTheme.sectionTitle("Medical Records");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel sub = new JLabel("Submit, view, and manage student medical certificates.");
        sub.setFont(AppTheme.F_BODY); sub.setForeground(AppTheme.TEXT_MUTED);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title); content.add(sub); vgap(content, 20);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(AppTheme.F_BOLD); tabs.setBackground(AppTheme.BG_PAGE);
        tabs.setAlignmentX(Component.LEFT_ALIGNMENT);
        tabs.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        boolean canManage = "tech_officer".equals(user.getRole()) || "admin".equals(user.getRole());
        if (canManage) tabs.addTab("  Manage Medicals  ", buildManageTab());
        tabs.addTab("  Submit Medical  ", buildSubmitTab());
        tabs.addTab("  My Medicals  ", buildMyMedicalsTab());

        content.add(tabs);
        add(scrollWrap(content));
    }

    // ── Manage tab (unchanged) ────────────────────────────────────────────────

    private JPanel buildManageTab() {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(AppTheme.BG_PAGE); p.setBorder(new EmptyBorder(16, 0, 0, 0));

        String[] cols = {"ID", "Student", "Course", "Session", "Session Date", "Reason", "Status", "Submitted At"};
        DefaultTableModel tm = model(cols);
        JTable table = new JTable(tm) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public java.awt.Component prepareRenderer(javax.swing.table.TableCellRenderer r, int row, int col) {
                java.awt.Component c = super.prepareRenderer(r, row, col);
                if (col == 6 && tm.getValueAt(row, 6) != null) {
                    String v = tm.getValueAt(row, 6).toString();
                    c.setForeground("Approved".equals(v) ? AppTheme.SUCCESS
                                  : "Rejected".equals(v) ? AppTheme.DANGER : AppTheme.WARNING);
                    if (c instanceof JLabel) ((JLabel) c).setFont(AppTheme.F_BOLD);
                } else {
                    c.setForeground(AppTheme.TEXT_BODY);
                }
                c.setBackground(row % 2 == 0 ? AppTheme.BG_CARD : AppTheme.BG_TABLE_ALT);
                if (c instanceof JLabel) ((JLabel) c).setBorder(new EmptyBorder(0, 12, 0, 12));
                if (isRowSelected(row)) c.setBackground(AppTheme.BG_TABLE_SEL);
                return c;
            }
        };
        table.setFont(AppTheme.F_TABLE); table.setRowHeight(34); table.setShowGrid(false);
        table.setShowHorizontalLines(true); table.setGridColor(AppTheme.BORDER);
        table.setFillsViewportHeight(true);
        JTableHeader hdr = table.getTableHeader();
        hdr.setFont(AppTheme.F_TABLE_H); hdr.setBackground(new Color(0xF8FAFC));
        hdr.setForeground(AppTheme.TEXT_MUTED);

        JButton load    = AppTheme.primaryBtn("Load All");
        JButton approve = AppTheme.successBtn("Approve");
        JButton reject  = AppTheme.dangerBtn("Reject");
        JLabel  msg     = new JLabel(" "); msg.setFont(AppTheme.F_SMALL);

        load.addActionListener(e -> {
            tm.setRowCount(0);
            for (Object[] row : attendDAO.getAllMedicalsWithStudentInfo()) tm.addRow(row);
        });
        approve.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Select a record."); return; }
            attendDAO.updateMedicalApproval((int) tm.getValueAt(row, 0), 1);
            msg.setForeground(AppTheme.SUCCESS); msg.setText("✓ Approved."); load.doClick();
        });
        reject.addActionListener(e -> {
            int row = table.getSelectedRow(); if (row < 0) return;
            attendDAO.updateMedicalApproval((int) tm.getValueAt(row, 0), 2);
            msg.setForeground(AppTheme.DANGER); msg.setText("✗ Rejected."); load.doClick();
        });

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)); topBar.setOpaque(false);
        topBar.add(load); topBar.add(approve); topBar.add(reject); topBar.add(msg);
        p.add(topBar, BorderLayout.NORTH);
        p.add(AppTheme.scrollTable(table), BorderLayout.CENTER);
        return p;
    }

    // ── Submit tab — AUTO-FILL added ──────────────────────────────────────────

    private JPanel buildSubmitTab() {
        JPanel p = new JPanel(new BorderLayout(20, 0));
        p.setBackground(AppTheme.BG_PAGE); p.setBorder(new EmptyBorder(16, 0, 0, 0));

        JPanel card = AppTheme.card(12);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(24, 28, 24, 28));
        card.setPreferredSize(new Dimension(460, 0));

        JLabel ftitle = new JLabel("Submit Medical Certificate");
        ftitle.setFont(AppTheme.F_H2); ftitle.setForeground(AppTheme.TEXT_DARK);
        ftitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(ftitle); card.add(Box.createVerticalStrut(20));

        // ── Course combo ──────────────────────────────────────────────────────
        List<Course> courses = courseDAO.getCoursesForStudent(user.getId());
        if (courses.isEmpty()) courses = courseDAO.getAllCourses();
        String[] ci = courses.stream()
            .map(c -> c.getId() + " | " + c.getCourseCode() + " — " + c.getCourseName())
            .toArray(String[]::new);

        boolean canManage = "tech_officer".equals(user.getRole()) || "admin".equals(user.getRole());

        JComboBox<String> cmbStu = null;
        if (canManage) {
            List<User> students = userDAO.getAllStudents();
            String[] sItems = students.stream()
                .map(s -> s.getId() + " | " + s.getUsername() + " — " + s.getFullName())
                .toArray(String[]::new);
            cmbStu = AppTheme.comboBox(sItems);
            JPanel studentRow = AppTheme.formRow("STUDENT", cmbStu);
            studentRow.setAlignmentX(Component.LEFT_ALIGNMENT);
            studentRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 66));
            card.add(studentRow); card.add(Box.createVerticalStrut(14));
        }

        JComboBox<String> cmbCourse = AppTheme.comboBox(ci);

        // ── Session spinner — max set from DB per course ───────────────────────
        int initialCourseId = courses.isEmpty() ? 1
            : Integer.parseInt(ci[0].split("\\|")[0].trim());
        int initialMax = attendDAO.getSessionCount(initialCourseId);
        SpinnerNumberModel spnModel = new SpinnerNumberModel(1, 1, Math.max(initialMax, 1), 1);
        JSpinner spn = new JSpinner(spnModel);
        spn.setFont(AppTheme.F_BODY);

        // ── Date field — auto-filled & read-only when a session is found ───────
        JTextField txtDate = AppTheme.textField("");
        txtDate.setEditable(false);  // default locked; unlocked only if lookup fails
        // Light-blue background signals auto-fill is active
        final Color AUTO_BG  = new Color(0xE8F4FD);
        final Color WARN_BG  = new Color(0xFFF8E1);
        txtDate.setBackground(AUTO_BG);

        // ── Hint label below the date field ──────────────────────────────────
        JLabel dateHint = new JLabel("Auto-filled from timetable");
        dateHint.setFont(AppTheme.F_SMALL);
        dateHint.setForeground(new Color(0x1976D2));

        JTextField txtReason = AppTheme.textField("Medical reason / diagnosis");

        // ── Auto-fill helper ──────────────────────────────────────────────────
        final List<Course> finalCourses = courses;
        Runnable autoFill = () -> {
            if (finalCourses.isEmpty()) return;
            try {
                int cid = Integer.parseInt(
                    cmbCourse.getSelectedItem().toString().split("\\|")[0].trim());
                int sn  = (int) spn.getValue();

                // Update spinner max for the newly selected course
                int maxSessions = attendDAO.getSessionCount(cid);
                spnModel.setMaximum(Math.max(maxSessions, 1));
                if (sn > maxSessions) spn.setValue(maxSessions);

                // Lookup date
                String date = attendDAO.getSessionDate(cid, sn);
                if (date != null) {
                    txtDate.setText(date);
                    txtDate.setEditable(false);
                    txtDate.setBackground(AUTO_BG);
                    dateHint.setText("Auto-filled from timetable  ✓");
                    dateHint.setForeground(new Color(0x1976D2));
                } else {
                    // Session not in lookup — let student type manually
                    txtDate.setText("");
                    txtDate.setEditable(true);
                    txtDate.setBackground(WARN_BG);
                    dateHint.setText("⚠ Session not found — enter date manually (YYYY-MM-DD)");
                    dateHint.setForeground(AppTheme.WARNING);
                }
            } catch (Exception ex) {
                txtDate.setText("");
                txtDate.setEditable(true);
                txtDate.setBackground(WARN_BG);
            }
        };

        // Trigger auto-fill when course changes
        cmbCourse.addActionListener(e -> autoFill.run());

        // Trigger auto-fill when session number changes
        spn.addChangeListener(e -> autoFill.run());

        // Initial fill on panel open
        autoFill.run();

        // ── Lay out form rows ─────────────────────────────────────────────────
        JPanel courseRow = AppTheme.formRow("COURSE", cmbCourse);
        courseRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        courseRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 66));
        card.add(courseRow); card.add(Box.createVerticalStrut(14));

        JPanel spnRow = AppTheme.formRow("SESSION #", spn);
        spnRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        spnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 66));
        card.add(spnRow); card.add(Box.createVerticalStrut(14));

        // Date + hint stacked together
        JPanel dateRow = AppTheme.formRow("ABSENCE DATE", txtDate);
        dateRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        dateRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 66));
        card.add(dateRow);
        dateHint.setAlignmentX(Component.LEFT_ALIGNMENT);
        dateHint.setBorder(new EmptyBorder(2, 4, 0, 0));
        card.add(dateHint); card.add(Box.createVerticalStrut(14));

        JPanel reasonRow = AppTheme.formRow("REASON", txtReason);
        reasonRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        reasonRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 66));
        card.add(reasonRow); card.add(Box.createVerticalStrut(20));

        // ── Submit button ─────────────────────────────────────────────────────
        JButton submit = AppTheme.primaryBtn("Submit Medical");
        submit.setAlignmentX(Component.LEFT_ALIGNMENT);
        submit.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        JLabel msg = new JLabel(" ");
        msg.setFont(AppTheme.F_SMALL); msg.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<String> finalCmbStu = cmbStu;
        submit.addActionListener(e -> {
            try {
                int sid = canManage
                    ? Integer.parseInt(finalCmbStu.getSelectedItem().toString().split("\\|")[0].trim())
                    : user.getId();
                int cid    = Integer.parseInt(cmbCourse.getSelectedItem().toString().split("\\|")[0].trim());
                int sn     = (int) spn.getValue();
                String date   = txtDate.getText().trim();
                String reason = txtReason.getText().trim();

                if (date.isEmpty() || reason.isEmpty()) {
                    msg.setForeground(AppTheme.DANGER);
                    msg.setText("✗ Fill all fields.");
                    return;
                }

                int res = attendDAO.addMedical(sid, cid, sn, date, reason);
                if (res == 0) {
                    msg.setForeground(AppTheme.SUCCESS);
                    msg.setText("✓ Medical submitted successfully!");
                    txtReason.setText("");
                } else if (res == 1) {
                    msg.setForeground(AppTheme.DANGER);
                    msg.setText("✗ Cannot submit — attendance already marked Present on " + date + ".");
                } else {
                    msg.setForeground(AppTheme.DANGER);
                    msg.setText("✗ Error. Try again.");
                }
            } catch (Exception ex) {
                msg.setForeground(AppTheme.DANGER);
                msg.setText("✗ Check inputs.");
            }
        });
        card.add(submit); card.add(Box.createVerticalStrut(8)); card.add(msg);

        // ── Info note ─────────────────────────────────────────────────────────
        JPanel note = AppTheme.card(10);
        note.setLayout(new BoxLayout(note, BoxLayout.Y_AXIS));
        note.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel nl = new JLabel("Important"); nl.setFont(AppTheme.F_BOLD);
        nl.setForeground(AppTheme.WARNING); nl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel ni = new JLabel("<html>" +
            "The absence date is <b>automatically filled</b> from the<br>" +
            "course timetable when you select a session number.<br><br>" +
            "You <b>cannot</b> submit a medical for a date where<br>" +
            "attendance is already recorded as <b>Present</b>." +
            "</html>");
        ni.setFont(AppTheme.F_BODY); ni.setForeground(AppTheme.TEXT_BODY);
        ni.setAlignmentX(Component.LEFT_ALIGNMENT);
        note.add(nl); note.add(Box.createVerticalStrut(6)); note.add(ni);

        JPanel right = new JPanel();
        right.setOpaque(false); right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.add(note);

        p.add(card, BorderLayout.WEST);
        p.add(right, BorderLayout.CENTER);
        return p;
    }

    // ── My Medicals tab (unchanged) ───────────────────────────────────────────

    private JPanel buildMyMedicalsTab() {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(AppTheme.BG_PAGE); p.setBorder(new EmptyBorder(16, 0, 0, 0));

        String[] cols = {"ID", "Course", "Session #", "Session Date", "Reason", "Status", "Submitted At"};
        DefaultTableModel tm = model(cols);
        JTable t = AppTheme.styledTable(tm);

        boolean canManage = "tech_officer".equals(user.getRole()) || "admin".equals(user.getRole());
        JComboBox<String> cmbStu = null;
        if (canManage) {
            List<User> students = userDAO.getAllStudents();
            String[] sItems = students.stream()
                .map(s -> s.getId() + " | " + s.getUsername() + " — " + s.getFullName())
                .toArray(String[]::new);
            cmbStu = AppTheme.comboBox(sItems);
        }

        JButton load = AppTheme.primaryBtn(canManage ? "Load Selected Student Medicals" : "Load My Medicals");
        JComboBox<String> finalCmbStu = cmbStu;
        load.addActionListener(e -> {
            int sid = canManage
                ? Integer.parseInt(finalCmbStu.getSelectedItem().toString().split("\\|")[0].trim())
                : user.getId();
            tm.setRowCount(0);
            attendDAO.getMedicalsForStudent(sid).forEach(m ->
                tm.addRow(new Object[]{
                    m.getId(), m.getCourseId(), m.getSessionNum(),
                    m.getSessionDate(), m.getReason(),
                    m.getApprovedLabel(), m.getSubmittedAt()
                }));
        });

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT)); top.setOpaque(false);
        if (canManage) { top.add(AppTheme.fieldLabel("STUDENT:")); top.add(cmbStu); }
        top.add(load);

        p.add(top, BorderLayout.NORTH);
        p.add(AppTheme.scrollTable(t), BorderLayout.CENTER);
        load.doClick();
        return p;
    }
}
