package ui.panels;

import ui.theme.AppTheme;
import dao.*;
import model.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;


public class AttendancePanel extends BasePanel {

    private final AttendanceDAO attendDAO  = new AttendanceDAO();
    private final CourseDAO     courseDAO  = new CourseDAO();
    private final UserDAO       userDAO    = new UserDAO();

    public AttendancePanel(User user) {
        super(user);
        build();
    }

    private void build() {
        JPanel content = pageContent();

        JLabel title = AppTheme.sectionTitle("Attendance Management");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        boolean isStudent = "student".equals(user.getRole());
        JLabel sub = new JLabel(isStudent
            ? "View your attendance by course and session."
            : "View, record, and analyse student attendance across all sessions.");
        sub.setFont(AppTheme.F_BODY); sub.setForeground(AppTheme.TEXT_MUTED); sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title); content.add(sub); vgap(content,20);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(AppTheme.F_BOLD);
        tabs.setBackground(AppTheme.BG_PAGE);

        if (!isStudent) {
            tabs.addTab("  Batch Summary  ", buildBatchTab());
        }
        tabs.addTab("  Individual View  ", buildIndividualTab());

        boolean canRecord = "tech_officer".equals(user.getRole()) || "admin".equals(user.getRole());
        if (canRecord) {
            tabs.addTab("  Record Attendance  ", buildRecordTab());
            tabs.addTab("  Mark Class Attendance  ", buildBatchMarkTab());
        }

        tabs.setAlignmentX(Component.LEFT_ALIGNMENT);
        tabs.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        content.add(tabs);
        add(scrollWrap(content));
    }

    // ── Batch Summary Tab ─────────────────────────────────────
    private JPanel buildBatchTab() {
        JPanel p = new JPanel(new BorderLayout(0,12));
        p.setBackground(AppTheme.BG_PAGE); p.setBorder(new EmptyBorder(16,0,0,0));

        List<Course> courses = courseDAO.getAllCourses();
        String[] cItems = courses.stream().map(c->c.getId()+" | "+c.getCourseCode()+" — "+c.getCourseName()).toArray(String[]::new);
        JComboBox<String> cmbCourse = AppTheme.comboBox(cItems.length > 0 ? cItems : new String[]{"No courses"});
        JComboBox<String> cmbType   = AppTheme.comboBox("Combined","Theory Only","Practical Only");

        String[] cols = {"Username","Full Name","Theory %","Practical %","Combined %","Status"};
        DefaultTableModel tm = model(cols);
        JTable table = buildAttendanceTable(tm);

        JButton load = AppTheme.primaryBtn("Load Summary");
        load.addActionListener(e -> {
            if (cmbCourse.getSelectedItem()==null || courses.isEmpty()) return;
            int cid = Integer.parseInt(cmbCourse.getSelectedItem().toString().split("\\|")[0].trim());
            String selectedType = cmbType.getSelectedItem().toString();
            tm.setRowCount(0);
            for (Object[] row : attendDAO.getBatchAttendanceSummary(cid)) {
                String theory = row[2].toString();
                String practical = row[3].toString();
                String combined = row[4].toString();
                String statusVal = row[5].toString();
                String displayPct = combined;
                if ("Theory Only".equals(selectedType)) {
                    displayPct = theory;
                    statusVal = parsePercent(theory) >= 80 ? "YES" : "NO";
                } else if ("Practical Only".equals(selectedType)) {
                    displayPct = practical;
                    statusVal = parsePercent(practical) >= 80 ? "YES" : "NO";
                }
                tm.addRow(new Object[]{row[0], row[1], theory, practical, displayPct, statusVal});
            }
        });

        JPanel legend = buildLegend();

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT,10,4)); topBar.setOpaque(false);
        topBar.add(AppTheme.fieldLabel("COURSE:")); topBar.add(cmbCourse);
        topBar.add(AppTheme.fieldLabel("TYPE:"));   topBar.add(cmbType);
        topBar.add(load);

        JPanel south = new JPanel(new BorderLayout()); south.setOpaque(false); south.add(legend,BorderLayout.WEST);

        p.add(topBar,BorderLayout.NORTH);
        p.add(AppTheme.scrollTable(table),BorderLayout.CENTER);
        p.add(south,BorderLayout.SOUTH);
        return p;
    }

    // ── Individual View Tab ───────────────────────────────────
    private JPanel buildIndividualTab() {
        JPanel p = new JPanel(new BorderLayout(0,12));
        p.setBackground(AppTheme.BG_PAGE); p.setBorder(new EmptyBorder(16,0,0,0));

        List<User> students = userDAO.getAllStudents();
        String[] sItems = students.stream().map(s->s.getId()+" | "+s.getUsername()+" — "+s.getFullName()).toArray(String[]::new);
        JComboBox<String> cmbStu = AppTheme.comboBox(sItems.length>0?sItems:new String[]{"No students found"});

        List<Course> courses = courseDAO.getAllCourses();
        String[] cItems = courses.stream().map(c->c.getId()+" | "+c.getCourseCode()+" — "+c.getCourseName()).toArray(String[]::new);
        JComboBox<String> cmbCourse = AppTheme.comboBox(cItems.length>0?cItems:new String[]{"No courses found"});
        JComboBox<String> cmbType = AppTheme.comboBox("All","Theory","Practical");

        String[] cols = {"Session #","Type","Status","Session Date","Medical"};
        DefaultTableModel tm = model(cols);
        JTable table = buildSessionTable(tm);

        JLabel pctLbl = new JLabel(" ", SwingConstants.CENTER);
        pctLbl.setFont(new Font("Segoe UI",Font.BOLD,14));
        pctLbl.setBorder(new EmptyBorder(10,0,0,0));

        JButton load = AppTheme.primaryBtn("Load Attendance");
        load.addActionListener(e -> {
            try {
                int sid = "student".equals(user.getRole())
                    ? user.getId()
                    : Integer.parseInt(cmbStu.getSelectedItem().toString().split("\\|")[0].trim());
                int cid = Integer.parseInt(cmbCourse.getSelectedItem().toString().split("\\|")[0].trim());
                String tp = cmbType.getSelectedItem().toString().toLowerCase();
                if (tp.equals("all")) tp="all";
                tm.setRowCount(0);

                List<Attendance> list = attendDAO.getAttendance(sid,cid,tp);
                list.forEach(a -> {
                    String medical = "—";
                    tm.addRow(new Object[]{a.getSessionNumber(),a.getSessionType(),a.getStatus(),a.getSessionDate(),medical});
                });
                double pct = attendDAO.getAttendancePercent(sid,cid,tp);
                pctLbl.setText("Attendance: "+String.format("%.1f%%",pct)+"  |  "+
                    (pct>80?"✓ Above 80%":pct==80?"= Exactly 80%":"✗ Below 80%"));
                pctLbl.setForeground(pct>80?AppTheme.SUCCESS:pct==80?AppTheme.WARNING:AppTheme.DANGER);
            } catch (Exception ex) { pctLbl.setText("Error loading."); pctLbl.setForeground(AppTheme.DANGER); }
        });

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT,10,4)); topBar.setOpaque(false);
        if (!"student".equals(user.getRole())) {
            topBar.add(AppTheme.fieldLabel("STUDENT:")); topBar.add(cmbStu);
        }
        topBar.add(AppTheme.fieldLabel("COURSE:")); topBar.add(cmbCourse);
        topBar.add(AppTheme.fieldLabel("TYPE:"));   topBar.add(cmbType);
        topBar.add(load);

        JPanel south=new JPanel(new BorderLayout()); south.setOpaque(false); south.add(pctLbl,BorderLayout.WEST);
        p.add(topBar,BorderLayout.NORTH); p.add(AppTheme.scrollTable(table),BorderLayout.CENTER); p.add(south,BorderLayout.SOUTH);
        return p;
    }

    // ── Record Single Attendance Tab ──────────────────────────
    private JPanel buildRecordTab() {
        JPanel outer = new JPanel();
        outer.setBackground(AppTheme.BG_PAGE);
        outer.setLayout(new BoxLayout(outer, BoxLayout.Y_AXIS));
        outer.setBorder(new EmptyBorder(16, 0, 0, 0));

        JPanel row1 = new JPanel(new GridLayout(1, 2, 20, 0));
        row1.setOpaque(false);
        row1.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        row1.setAlignmentX(Component.LEFT_ALIGNMENT);

        // === Form card ===
        JPanel formCard = AppTheme.card(12);
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(new EmptyBorder(24,28,24,28));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6,4,6,4);

        List<User> stus = userDAO.getAllStudents();
        String[] si = stus.stream().map(s->s.getId()+" | "+s.getUsername()+" — "+s.getFullName()).toArray(String[]::new);
        List<Course> courses = courseDAO.getAllCourses();
        String[] ci = courses.stream().map(c->c.getId()+" | "+c.getCourseCode()+" "+c.getCourseName()).toArray(String[]::new);

        JComboBox<String> cmbStu    = AppTheme.comboBox(si.length>0?si:new String[]{"No students"});
        JComboBox<String> cmbCourse = AppTheme.comboBox(ci.length>0?ci:new String[]{"No courses"});
        JSpinner           spnSess  = new JSpinner(new SpinnerNumberModel(1,1,15,1));
        spnSess.setFont(AppTheme.F_BODY);
        JComboBox<String>  cmbType  = AppTheme.comboBox("Theory","Practical");
        JComboBox<String>  cmbStat  = AppTheme.comboBox("Present","Absent");
        JTextField          txtDate = AppTheme.textField(LocalDate.now().toString());

        String[] labels = {"STUDENT","COURSE","SESSION #","TYPE","STATUS","DATE (YYYY-MM-DD)"};
        JComponent[] fields = {cmbStu, cmbCourse, spnSess, cmbType, cmbStat, txtDate};
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx=0; gbc.gridy=i; gbc.weightx=0.35;
            JLabel lbl = AppTheme.fieldLabel(labels[i]);
            formCard.add(lbl, gbc);
            gbc.gridx=1; gbc.weightx=0.65;
            formCard.add(fields[i], gbc);
        }

        JLabel msg = new JLabel(" ");
        msg.setFont(AppTheme.F_SMALL);

        JButton save = AppTheme.primaryBtn("Save Attendance");
        save.addActionListener(e -> {
            try {
                if (si.length == 0 || ci.length == 0) {
                    msg.setForeground(AppTheme.DANGER); msg.setText("✗ No students or courses available."); return;
                }
                int sid = Integer.parseInt(cmbStu.getSelectedItem().toString().split("\\|")[0].trim());
                int cid = Integer.parseInt(cmbCourse.getSelectedItem().toString().split("\\|")[0].trim());
                int sess = (int)spnSess.getValue();
                String type = cmbType.getSelectedItem().toString().toLowerCase();
                String stat = cmbStat.getSelectedItem().toString().toLowerCase();
                String date = txtDate.getText().trim();
                if (date.isEmpty()) { msg.setForeground(AppTheme.DANGER); msg.setText("✗ Enter a date."); return; }
                boolean ok = attendDAO.saveAttendance(sid,cid,sess,type,stat,date);
                msg.setForeground(ok?AppTheme.SUCCESS:AppTheme.DANGER);
                msg.setText(ok ? "✓ Attendance saved!" : "✗ Error saving — check DB connection.");
            } catch (Exception ex) {
                msg.setForeground(AppTheme.DANGER);
                msg.setText("✗ Error: " + ex.getMessage());
            }
        });

        gbc.gridx=0; gbc.gridy=labels.length; gbc.gridwidth=2; gbc.weightx=1.0;
        gbc.insets=new Insets(16,4,4,4);
        formCard.add(save, gbc);
        gbc.gridy=labels.length+1; gbc.insets=new Insets(4,4,4,4);
        formCard.add(msg, gbc);

        // === Info card ===
        JPanel infoCard = AppTheme.card(12);
        infoCard.setLayout(new BoxLayout(infoCard,BoxLayout.Y_AXIS));
        infoCard.setBorder(new EmptyBorder(24,24,24,24));
        JLabel il=new JLabel("Session Guide"); il.setFont(AppTheme.F_H3); il.setForeground(AppTheme.TEXT_DARK);
        il.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoCard.add(il); infoCard.add(Box.createVerticalStrut(16));
        for (String[] r : new String[][]{
            {"Theory sessions","1–15 per course"},
            {"Practical sessions","1–15 per course"},
            {"80% rule","Present ÷ Total × 100"},
            {"Medical impact","Approved medicals counted"},
            {"Date format","YYYY-MM-DD"},
        }) {
            JPanel rp=new JPanel(new BorderLayout(12,0)); rp.setOpaque(false); rp.setMaximumSize(new Dimension(Integer.MAX_VALUE,32));
            rp.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel k=new JLabel(r[0]); k.setFont(AppTheme.F_BOLD); k.setForeground(AppTheme.TEXT_BODY); k.setPreferredSize(new Dimension(160,20));
            JLabel v=new JLabel(r[1]); v.setFont(AppTheme.F_BODY); v.setForeground(AppTheme.TEXT_MUTED);
            rp.add(k,BorderLayout.WEST); rp.add(v,BorderLayout.CENTER);
            infoCard.add(rp); infoCard.add(Box.createVerticalStrut(8));
        }

        row1.add(formCard);
        row1.add(infoCard);
        outer.add(row1);
        return outer;
    }

    // ── Batch Mark Attendance Tab (mark entire class at once) ─
    private JPanel buildBatchMarkTab() {
        JPanel p = new JPanel(new BorderLayout(0,12));
        p.setBackground(AppTheme.BG_PAGE); p.setBorder(new EmptyBorder(16,0,0,0));

        List<Course> courses = courseDAO.getAllCourses();
        String[] ci = courses.stream().map(c->c.getId()+" | "+c.getCourseCode()+" — "+c.getCourseName()).toArray(String[]::new);

        JComboBox<String> cmbCourse = AppTheme.comboBox(ci.length>0?ci:new String[]{"No courses"});
        JSpinner spnSess = new JSpinner(new SpinnerNumberModel(1,1,15,1));
        spnSess.setFont(AppTheme.F_BODY); spnSess.setPreferredSize(new Dimension(80,34));
        JComboBox<String> cmbType = AppTheme.comboBox("Theory","Practical");
        JTextField txtDate = AppTheme.textField(LocalDate.now().toString());
        txtDate.setPreferredSize(new Dimension(140,34));

        // Table: student list with attendance toggle
        String[] cols = {"ID","Student","Default Status"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return c == 2; }
            public Class<?> getColumnClass(int c) { return c==2 ? Boolean.class : String.class; }
        };
        JTable table = new JTable(tm);
        table.setFont(AppTheme.F_TABLE); table.setRowHeight(34);
        table.setShowGrid(false); table.setShowHorizontalLines(true); table.setGridColor(AppTheme.BORDER);
        table.setFillsViewportHeight(true);
        table.getColumnModel().getColumn(2).setHeaderValue("Present? (check = Present)");
        JTableHeader hdr = table.getTableHeader(); hdr.setFont(AppTheme.F_TABLE_H);
        hdr.setBackground(new Color(0xF8FAFC)); hdr.setForeground(AppTheme.TEXT_MUTED);

        JLabel statusLbl = new JLabel(" ");
        statusLbl.setFont(AppTheme.F_SMALL);
        statusLbl.setBorder(new EmptyBorder(4,0,0,0));

        JButton loadStudents = AppTheme.primaryBtn("Load Students");
        loadStudents.addActionListener(e -> {
            if (courses.isEmpty()) return;
            int cid = Integer.parseInt(cmbCourse.getSelectedItem().toString().split("\\|")[0].trim());
            // Load students enrolled in this course
            List<User> studs = userDAO.getAllStudents();
            tm.setRowCount(0);
            // Filter by enrollment — use all students as fallback
            for (User s : studs) {
                // Check if enrolled by trying to get their courses
                List<model.Course> enrolled = courseDAO.getCoursesForStudent(s.getId());
                boolean enr = enrolled.stream().anyMatch(c -> c.getId() == cid);
                if (enr) {
                    tm.addRow(new Object[]{s.getId(), s.getUsername()+" — "+s.getFullName(), true});
                }
            }
            if (tm.getRowCount() == 0) {
                statusLbl.setForeground(AppTheme.WARNING);
                statusLbl.setText("⚠ No enrolled students found for this course.");
            } else {
                statusLbl.setForeground(AppTheme.TEXT_MUTED);
                statusLbl.setText(tm.getRowCount() + " students loaded. Check/uncheck to mark present/absent.");
            }
        });

        JButton saveAll = AppTheme.primaryBtn("Save All Attendance");
        saveAll.setBackground(AppTheme.SUCCESS);
        saveAll.addActionListener(e -> {
            if (tm.getRowCount() == 0) {
                statusLbl.setForeground(AppTheme.DANGER); statusLbl.setText("✗ Load students first."); return;
            }
            String date = txtDate.getText().trim();
            if (date.isEmpty()) { statusLbl.setForeground(AppTheme.DANGER); statusLbl.setText("✗ Enter date."); return; }
            int sess = (int) spnSess.getValue();
            String type = cmbType.getSelectedItem().toString().toLowerCase();
            int cid = Integer.parseInt(cmbCourse.getSelectedItem().toString().split("\\|")[0].trim());
            int saved = 0, failed = 0;
            for (int row = 0; row < tm.getRowCount(); row++) {
                int sid = (int) tm.getValueAt(row, 0);
                boolean present = (Boolean) tm.getValueAt(row, 2);
                String stat = present ? "present" : "absent";
                if (attendDAO.saveAttendance(sid, cid, sess, type, stat, date)) saved++;
                else failed++;
            }
            statusLbl.setForeground(failed==0 ? AppTheme.SUCCESS : AppTheme.WARNING);
            statusLbl.setText(String.format("✓ Saved %d records%s", saved,
                failed>0 ? " | ✗ "+failed+" failed" : ""));
        });

        JButton markAll = new JButton("✓ Mark All Present");
        markAll.setFont(AppTheme.F_SMALL);
        markAll.addActionListener(e -> { for(int i=0;i<tm.getRowCount();i++) tm.setValueAt(true,i,2); });

        JButton clearAll = new JButton("✗ Mark All Absent");
        clearAll.setFont(AppTheme.F_SMALL);
        clearAll.addActionListener(e -> { for(int i=0;i<tm.getRowCount();i++) tm.setValueAt(false,i,2); });

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT,10,4)); topBar.setOpaque(false);
        topBar.add(AppTheme.fieldLabel("COURSE:")); topBar.add(cmbCourse);
        topBar.add(AppTheme.fieldLabel("SESSION #:")); topBar.add(spnSess);
        topBar.add(AppTheme.fieldLabel("TYPE:")); topBar.add(cmbType);
        topBar.add(AppTheme.fieldLabel("DATE (YYYY-MM-DD):")); topBar.add(txtDate);
        topBar.add(loadStudents);

        JPanel actBar = new JPanel(new FlowLayout(FlowLayout.LEFT,10,4)); actBar.setOpaque(false);
        actBar.add(markAll); actBar.add(clearAll); actBar.add(Box.createHorizontalStrut(20)); actBar.add(saveAll);
        actBar.add(statusLbl);

        JPanel center = new JPanel(new BorderLayout(0,8)); center.setOpaque(false);
        center.add(AppTheme.scrollTable(table), BorderLayout.CENTER);
        center.add(actBar, BorderLayout.SOUTH);

        p.add(topBar, BorderLayout.NORTH);
        p.add(center, BorderLayout.CENTER);
        return p;
    }

    // ── Colour-coded attendance table ─────────────────────────
    private JTable buildAttendanceTable(DefaultTableModel tm) {
        JTable t = new JTable(tm) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r,row,col);
                Object pctObj = tm.getValueAt(row,4);
                if (pctObj!=null && !isRowSelected(row)) {
                    try {
                        String s=pctObj.toString().replace("%","").trim();
                        double pct=Double.parseDouble(s);
                        Color rowBg = pct>80 ? new Color(0xF0FDF4) : pct==80 ? new Color(0xFFFBEB) : new Color(0xFFF1F2);
                        c.setBackground(rowBg);
                    } catch (Exception ignored) {}
                }
                if (col==5 && tm.getValueAt(row,5)!=null) {
                    String v=tm.getValueAt(row,5).toString();
                    c.setForeground(v.equals("YES")?AppTheme.SUCCESS:AppTheme.DANGER);
                    if(c instanceof JLabel){((JLabel)c).setFont(AppTheme.F_BOLD);}
                }
                if(c instanceof JLabel) ((JLabel)c).setBorder(new EmptyBorder(0,12,0,12));
                return c;
            }
        };
        t.setFont(AppTheme.F_TABLE); t.setRowHeight(34);
        t.setShowGrid(false); t.setShowHorizontalLines(true); t.setGridColor(AppTheme.BORDER);
        t.setFillsViewportHeight(true); t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableHeader hdr=t.getTableHeader(); hdr.setFont(AppTheme.F_TABLE_H); hdr.setBackground(new Color(0xF8FAFC)); hdr.setForeground(AppTheme.TEXT_MUTED);
        hdr.setDefaultRenderer(new DefaultTableCellRenderer(){
            @Override public Component getTableCellRendererComponent(JTable tb,Object v,boolean sel,boolean foc,int row,int col){
                super.getTableCellRendererComponent(tb,v,sel,foc,row,col);
                setText(v!=null?v.toString().toUpperCase():""); setBackground(new Color(0xF8FAFC)); setForeground(AppTheme.TEXT_MUTED);
                setFont(AppTheme.F_TABLE_H); setBorder(new EmptyBorder(0,12,0,12)); return this;
            }
        });
        return t;
    }

    private JTable buildSessionTable(DefaultTableModel tm) {
        JTable t = new JTable(tm) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c=super.prepareRenderer(r,row,col);
                Object status=tm.getValueAt(row,2);
                if (status!=null && !isRowSelected(row)) {
                    c.setBackground("present".equalsIgnoreCase(status.toString()) ? new Color(0xF0FDF4) : new Color(0xFFF1F2));
                }
                if (col==2 && status!=null) {
                    boolean pres="present".equalsIgnoreCase(status.toString());
                    c.setForeground(pres?AppTheme.SUCCESS:AppTheme.DANGER);
                    if(c instanceof JLabel) ((JLabel)c).setFont(AppTheme.F_BOLD);
                }
                if(c instanceof JLabel) ((JLabel)c).setBorder(new EmptyBorder(0,12,0,12));
                return c;
            }
        };
        t.setFont(AppTheme.F_TABLE); t.setRowHeight(34);
        t.setShowGrid(false); t.setShowHorizontalLines(true); t.setGridColor(AppTheme.BORDER);
        t.setFillsViewportHeight(true);
        return t;
    }

    private JPanel buildLegend() {
        JPanel p=new JPanel(new FlowLayout(FlowLayout.LEFT,16,8)); p.setOpaque(false);
        for (Object[] item : new Object[][]{{">80%","Eligible",AppTheme.SUCCESS},{"=80%","Border",AppTheme.WARNING},{"<80%","Not Eligible",AppTheme.DANGER}}) {
            JPanel dot=new JPanel(){
                @Override protected void paintComponent(Graphics g){
                    AppTheme.setup(g).setColor((Color)item[2]); g.fillOval(0,4,10,10);
                }
            };
            dot.setPreferredSize(new Dimension(12,18)); dot.setOpaque(false);
            JLabel l=new JLabel(item[0]+" "+item[1]); l.setFont(AppTheme.F_SMALL); l.setForeground(AppTheme.TEXT_BODY);
            JPanel row=new JPanel(new FlowLayout(FlowLayout.LEFT,4,0)); row.setOpaque(false); row.add(dot); row.add(l);
            p.add(row);
        }
        return p;
    }

    private double parsePercent(String value) {
        try { return Double.parseDouble(value.replace("%","").trim()); } catch (Exception e) { return 0.0; }
    }
}
