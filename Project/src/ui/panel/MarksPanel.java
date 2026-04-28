package ui.panels;

import ui.theme.AppTheme;
import dao.*;
import model.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.List;


public class MarksPanel extends BasePanel {

    private final MarksDAO     marksDAO  = new MarksDAO();
    private final CourseDAO    courseDAO = new CourseDAO();
    private final UserDAO      userDAO   = new UserDAO();

    public MarksPanel(User user) {
        super(user);
        build();
    }

    private void build() {
        JPanel content = pageContent();

        // Header
        JLabel title = AppTheme.sectionTitle("Marks & GPA");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel sub = new JLabel("View and manage student marks, grades, and GPA calculations.");
        sub.setFont(AppTheme.F_BODY); sub.setForeground(AppTheme.TEXT_MUTED);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title); content.add(sub); vgap(content,20);

        // Tabs
        JTabbedPane tabs = buildTabs();
        tabs.setAlignmentX(Component.LEFT_ALIGNMENT);
        tabs.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        content.add(tabs);

        add(scrollWrap(content));
    }

    private JTabbedPane buildTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(AppTheme.F_BOLD);
        tabs.setBackground(AppTheme.BG_PAGE);
        tabs.setForeground(AppTheme.TEXT_BODY);
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        boolean isAdmin = "admin".equals(user.getRole());
        boolean isLecturer = "lecturer".equals(user.getRole());

        if (isAdmin || isLecturer) {
            tabs.addTab("  Upload Marks  ", buildUploadTab());
            tabs.addTab("  Batch Summary  ", buildBatchSummaryTab());
        }
        tabs.addTab("  My Marks  ", buildMyMarksTab());
        tabs.addTab("  GPA Calculator  ", buildGPATab());

        return tabs;
    }

    // ── Upload Marks Tab ──────────────────────────────────────
    private JPanel buildUploadTab() {
        JPanel p = new JPanel(new BorderLayout(20,0)); p.setBackground(AppTheme.BG_PAGE);
        p.setBorder(new EmptyBorder(20,0,0,0));

        // Left: form
        JPanel formCard = AppTheme.card(12); formCard.setLayout(new BoxLayout(formCard,BoxLayout.Y_AXIS));
        formCard.setBorder(new EmptyBorder(24,28,24,28)); formCard.setPreferredSize(new Dimension(400,0));

        JLabel ftitle = new JLabel("Enter Mark"); ftitle.setFont(AppTheme.F_H2); ftitle.setForeground(AppTheme.TEXT_DARK);
        ftitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(ftitle); formCard.add(Box.createVerticalStrut(20));

        List<User> stus = userDAO.getAllStudents();
        String[] stuItems = stus.stream().map(s->s.getId()+" | "+s.getUsername()+" — "+s.getFullName()).toArray(String[]::new);
        List<Course> courses = courseDAO.getCoursesForLecturer(user.getId());
        if (courses.isEmpty()) courses = courseDAO.getAllCourses(); // admin sees all
        String[] cItems = courses.stream().map(c->c.getId()+" | "+c.getCourseCode()+" "+c.getCourseName()).toArray(String[]::new);

        JComboBox<String> cmbStu   = AppTheme.comboBox(stuItems);
        JComboBox<String> cmbCourse= AppTheme.comboBox(cItems);
        JComboBox<String> cmbType  = AppTheme.comboBox("CA1","CA2","Assignment","Final");
        JTextField txtMark = AppTheme.textField("Enter mark 0 – 100");

        for (Object[] row : new Object[][]{
            {"STUDENT", cmbStu}, {"COURSE",cmbCourse}, {"EXAM TYPE",cmbType}, {"MARK",txtMark}}) {
            JPanel fr = AppTheme.formRow((String)row[0],(JComponent)row[1]);
            fr.setAlignmentX(Component.LEFT_ALIGNMENT); fr.setMaximumSize(new Dimension(Integer.MAX_VALUE,66));
            formCard.add(fr); formCard.add(Box.createVerticalStrut(14));
        }

        JButton save = AppTheme.primaryBtn("Save Mark");
        save.setAlignmentX(Component.LEFT_ALIGNMENT); save.setMaximumSize(new Dimension(Integer.MAX_VALUE,44));
        JLabel msg = new JLabel(" "); msg.setFont(AppTheme.F_SMALL); msg.setAlignmentX(Component.LEFT_ALIGNMENT);

        save.addActionListener(e -> {
            try {
                int sid = Integer.parseInt(cmbStu.getSelectedItem().toString().split("\\|")[0].trim());
                int cid = Integer.parseInt(cmbCourse.getSelectedItem().toString().split("\\|")[0].trim());
                double mk = Double.parseDouble(txtMark.getText().trim());
                if (mk<0||mk>100) { msg.setForeground(AppTheme.DANGER); msg.setText("Mark must be between 0 and 100."); return; }
                String type = cmbType.getSelectedItem().toString().toLowerCase().replace(" ","");
                boolean ok = marksDAO.saveMark(sid,cid,type,mk,user.getId());
                msg.setForeground(ok?AppTheme.SUCCESS:AppTheme.DANGER);
                msg.setText(ok ? "✓ Mark saved successfully!" : "✗ Error saving mark.");
                if (ok) txtMark.setText("");
            } catch (NumberFormatException ex) { msg.setForeground(AppTheme.DANGER); msg.setText("✗ Enter a valid number."); }
        });

        formCard.add(save); formCard.add(Box.createVerticalStrut(8)); formCard.add(msg);

        // Right: grade reference card
        JPanel gradeCard = AppTheme.card(12); gradeCard.setLayout(new BorderLayout(0,12));
        gradeCard.setBorder(new EmptyBorder(24,24,24,24));
        JLabel gl = new JLabel("UGC Grade Reference"); gl.setFont(AppTheme.F_H3); gl.setForeground(AppTheme.TEXT_DARK);
        gradeCard.add(gl, BorderLayout.NORTH);
        gradeCard.add(buildGradeTable(), BorderLayout.CENTER);

        p.add(formCard, BorderLayout.WEST);
        p.add(gradeCard, BorderLayout.CENTER);
        return p;
    }

    private JScrollPane buildGradeTable() {
        String[][] rows = {
            {"A+","85 – 100","4.0"},{"A","75 – 84","4.0"},{"A-","70 – 74","3.7"},
            {"B+","65 – 69","3.3"},{"B","60 – 64","3.0"},{"B-","55 – 59","2.7"},
            {"C+","50 – 54","2.3"},{"C","45 – 49","2.0"},{"C-","40 – 44","1.7"},
            {"D+","35 – 39","1.3"},{"D","30 – 34","1.0"},{"E","0 – 29","0.0"},
        };
        DefaultTableModel tm = model(new String[]{"Grade","Mark Range","GPA Points"});
        for (String[] r : rows) tm.addRow(r);

        JTable t = new JTable(tm) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r,row,col);
                if (!isRowSelected(row)) {
                    Color bg = row%2==0 ? AppTheme.BG_CARD : AppTheme.BG_TABLE_ALT;
                    c.setBackground(bg); c.setForeground(AppTheme.TEXT_BODY);
                    // Colour grade column
                    if (col==0) {
                        String grade = (String)getValueAt(row,0);
                        c.setForeground(gradeColor(grade));
                        if (c instanceof JLabel) ((JLabel)c).setFont(AppTheme.F_BOLD);
                    }
                }
                if (c instanceof JLabel) ((JLabel)c).setBorder(new EmptyBorder(0,12,0,12));
                return c;
            }
        };
        t.setFont(AppTheme.F_TABLE); t.setRowHeight(30); t.setShowGrid(false);
        t.setShowHorizontalLines(true); t.setGridColor(AppTheme.BORDER);
        t.setFillsViewportHeight(true);
        JTableHeader hdr=t.getTableHeader(); hdr.setFont(AppTheme.F_TABLE_H); hdr.setBackground(new Color(0xF8FAFC)); hdr.setForeground(AppTheme.TEXT_MUTED);
        return AppTheme.scrollTable(t);
    }

    // ── Batch Summary Tab ─────────────────────────────────────
    private JPanel buildBatchSummaryTab() {
        JPanel p = new JPanel(new BorderLayout(0,12)); p.setBackground(AppTheme.BG_PAGE); p.setBorder(new EmptyBorder(20,0,0,0));

        List<Course> courses = courseDAO.getAllCourses();
        String[] cItems = courses.stream().map(c->c.getId()+" | "+c.getCourseCode()+" "+c.getCourseName()).toArray(String[]::new);
        JComboBox<String> cmbCourse = AppTheme.comboBox(cItems);

        String[] cols = {"Username","Full Name","CA1","CA2","Assignment","CA Avg","CA Eligible","Final Mark","Grade"};
        DefaultTableModel tm = model(cols);
        JTable t = AppTheme.styledTable(tm);

        // Override renderer for grade column with colours
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable tbl, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(tbl,v,sel,foc,row,col);
                setBackground(row%2==0 ? AppTheme.BG_CARD : AppTheme.BG_TABLE_ALT);
                setBorder(new EmptyBorder(0,12,0,12)); setFont(AppTheme.F_TABLE);
                if (col==8 && v!=null) {
                    setForeground(gradeColor(v.toString())); setFont(AppTheme.F_BOLD);
                } else if (col==6 && v!=null) {
                    setForeground(v.toString().equals("YES") ? AppTheme.SUCCESS : AppTheme.DANGER);
                    setFont(AppTheme.F_BOLD);
                } else {
                    setForeground(AppTheme.TEXT_BODY);
                }
                if (sel) { setBackground(AppTheme.BG_TABLE_SEL); }
                return this;
            }
        });

        JButton load = AppTheme.primaryBtn("Load Summary");
        load.addActionListener(e -> {
            if (cmbCourse.getSelectedItem()==null) return;
            int cid=Integer.parseInt(cmbCourse.getSelectedItem().toString().split("\\|")[0].trim());
            tm.setRowCount(0);
            for (Object[] row : marksDAO.getBatchMarksSummary(cid)) tm.addRow(row);
        });

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT,10,0)); topBar.setOpaque(false);
        topBar.add(AppTheme.fieldLabel("SELECT COURSE:")); topBar.add(cmbCourse); topBar.add(load);

        p.add(topBar, BorderLayout.NORTH);
        p.add(AppTheme.scrollTable(t), BorderLayout.CENTER);
        return p;
    }

    // ── My Marks Tab ──────────────────────────────────────────
    private JPanel buildMyMarksTab() {
        JPanel p = new JPanel(new BorderLayout(0,12)); p.setBackground(AppTheme.BG_PAGE); p.setBorder(new EmptyBorder(20,0,0,0));

        List<Course> courses = courseDAO.getCoursesForStudent(user.getId());
        if (courses.isEmpty()) courses = courseDAO.getAllCourses();
        String[] cItems = courses.stream().map(c->c.getId()+" | "+c.getCourseCode()+" — "+c.getCourseName()).toArray(String[]::new);
        JComboBox<String> cmbCourse = AppTheme.comboBox(cItems);
        JComboBox<String> cmbStu = null;
        if (!"student".equals(user.getRole())) {
            List<User> students = userDAO.getAllStudents();
            String[] sItems = students.stream()
                .map(s -> s.getId() + " | " + s.getUsername() + " — " + s.getFullName())
                .toArray(String[]::new);
            cmbStu = AppTheme.comboBox(sItems);
        }

        String[] cols = {"Exam Type","Mark","Grade","Grade Points"};
        DefaultTableModel tm = model(cols);
        JTable t = AppTheme.styledTable(tm);

        JLabel caInfo = new JLabel(" "); caInfo.setFont(AppTheme.F_BODY); caInfo.setForeground(AppTheme.TEXT_MUTED);
        caInfo.setBorder(new EmptyBorder(8,4,0,0));

        // Colour renderer for grade + eligibility
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable tbl, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(tbl,v,sel,foc,row,col);
                setBackground(row%2==0 ? AppTheme.BG_CARD : AppTheme.BG_TABLE_ALT);
                setBorder(new EmptyBorder(0,12,0,12)); setFont(AppTheme.F_TABLE);
                setForeground(col==2 && v!=null ? gradeColor(v.toString()) : AppTheme.TEXT_BODY);
                if (col==2) setFont(AppTheme.F_BOLD);
                if (sel) setBackground(AppTheme.BG_TABLE_SEL);
                return this;
            }
        });

        JButton load = AppTheme.primaryBtn("Load Marks");
        JComboBox<String> finalCmbStu = cmbStu;
        load.addActionListener(e -> {
            if (cmbCourse.getSelectedItem()==null) return;
            int cid=Integer.parseInt(cmbCourse.getSelectedItem().toString().split("\\|")[0].trim());
            tm.setRowCount(0);
            int uid = "student".equals(user.getRole())
                ? user.getId()
                : Integer.parseInt(finalCmbStu.getSelectedItem().toString().split("\\|")[0].trim());
            marksDAO.getMarksForStudent(uid, cid).forEach(mk -> {
                String grade = MarksDAO.getGrade(mk.getMark());
                double gp    = MarksDAO.getGradePoint(grade);
                tm.addRow(new Object[]{mk.getExamType().toUpperCase(), mk.getMark(), grade, gp});
            });
            double ca = marksDAO.getCAAverage(uid,cid);
            boolean elig = marksDAO.isEligibleForFinal(uid,cid);
            caInfo.setText("CA Average: " + String.format("%.1f",ca) +
                "  |  Final Exam: " + (elig ? "✓ ELIGIBLE" : "✗ NOT ELIGIBLE (CA < 40%)"));
            caInfo.setForeground(elig ? AppTheme.SUCCESS : AppTheme.DANGER);
        });

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT,10,0)); topBar.setOpaque(false);
        if (!"student".equals(user.getRole())) {
            topBar.add(AppTheme.fieldLabel("SELECT STUDENT:"));
            topBar.add(cmbStu);
        }
        topBar.add(AppTheme.fieldLabel("SELECT COURSE:")); topBar.add(cmbCourse); topBar.add(load);

        JPanel south = new JPanel(new BorderLayout()); south.setOpaque(false); south.add(caInfo);
        p.add(topBar,BorderLayout.NORTH); p.add(AppTheme.scrollTable(t),BorderLayout.CENTER); p.add(south,BorderLayout.SOUTH);
        return p;
    }

    // ── GPA Tab ───────────────────────────────────────────────
    private JPanel buildGPATab() {
        JPanel p = new JPanel(new BorderLayout(20,0)); p.setBackground(AppTheme.BG_PAGE); p.setBorder(new EmptyBorder(20,0,0,0));

        // GPA summary card
        JPanel gpaCard = AppTheme.card(12); gpaCard.setLayout(new BorderLayout(0,12)); gpaCard.setBorder(new EmptyBorder(24,24,24,24));
        gpaCard.setPreferredSize(new Dimension(340,0));

        JTextArea txt = new JTextArea(); txt.setEditable(false); txt.setFont(AppTheme.F_MONO);
        txt.setBackground(new Color(0xF8FAFC)); txt.setForeground(AppTheme.TEXT_BODY);
        txt.setBorder(new EmptyBorder(12,14,12,14));

        JLabel gl = new JLabel("GPA Calculator"); gl.setFont(AppTheme.F_H3); gl.setForeground(AppTheme.TEXT_DARK);

        JComboBox<String> cmbStu;
        if ("student".equals(user.getRole())) {
            cmbStu = AppTheme.comboBox(user.getId()+" | "+user.getUsername()+" — "+user.getFullName());
        } else {
            List<User> stus = userDAO.getAllStudents();
            String[] si = stus.stream().map(s->s.getId()+" | "+s.getUsername()+" — "+s.getFullName()).toArray(String[]::new);
            cmbStu = AppTheme.comboBox(si);
        }
        cmbStu.setMaximumSize(new Dimension(Integer.MAX_VALUE,36));

        JButton calc = AppTheme.primaryBtn("Calculate GPA");
        calc.addActionListener(e -> {
            int sid = Integer.parseInt(cmbStu.getSelectedItem().toString().split("\\|")[0].trim());
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-34s %-6s %-8s %-5s %-5s%n","COURSE","CREDITS","MARK","GRADE","GP"));
            sb.append("─".repeat(62)).append("\n");
            courseDAO.getCoursesForStudent(sid).forEach(c->{
                double fm=marksDAO.getFinalMark(sid,c.getId());
                double ca=marksDAO.getCAAverage(sid,c.getId());
                double mark=fm>=0?fm:ca;
                String grade=mark>0?MarksDAO.getGrade(mark):"—";
                double gp=mark>0?MarksDAO.getGradePoint(grade):0;
                int cr=c.getCreditTheory()+c.getCreditPractical();
                sb.append(String.format("%-34s %-6d %-8.1f %-5s %-5.1f%n",
                    c.getCourseCode()+" "+c.getCourseName(),cr,mark,grade,gp));
            });
            double sgpa=marksDAO.calculateSGPA(sid);
            double cgpa=marksDAO.calculateCGPA(sid);
            sb.append("─".repeat(62)).append("\n");
            sb.append(String.format("SGPA (Semester GPA)  : %.2f%n",sgpa));
            sb.append(String.format("CGPA (Cumulative GPA): %.2f%n",cgpa));
            txt.setText(sb.toString());
        });

        gpaCard.add(gl,BorderLayout.NORTH);
        JPanel top2=new JPanel(new GridLayout(2,1,0,8)); top2.setOpaque(false);
        top2.add(AppTheme.formRow("SELECT STUDENT",cmbStu)); top2.add(calc);
        gpaCard.add(top2,BorderLayout.CENTER);

        JPanel txtCard=AppTheme.card(12); txtCard.setLayout(new BorderLayout()); txtCard.setBorder(new EmptyBorder(16,16,16,16));
        JLabel rl=new JLabel("GPA Report"); rl.setFont(AppTheme.F_H3); rl.setForeground(AppTheme.TEXT_DARK); rl.setBorder(new EmptyBorder(0,0,10,0));
        JScrollPane sp=new JScrollPane(txt); sp.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER,1,true));
        sp.getVerticalScrollBar().setUI(new AppTheme.SlimScrollBar());
        txtCard.add(rl,BorderLayout.NORTH); txtCard.add(sp,BorderLayout.CENTER);

        p.add(gpaCard,BorderLayout.WEST); p.add(txtCard,BorderLayout.CENTER);
        return p;
    }

    // ── Grade helpers ─────────────────────────────────────────
    static Color gradeColor(String grade) {
        if (grade==null) return AppTheme.TEXT_MUTED;
        switch(grade) {
            case "A+": case "A":  return new Color(0x065F46);
            case "A-": case "B+": return AppTheme.SUCCESS;
            case "B":  case "B-": return AppTheme.TEAL;
            case "C+": case "C":  return AppTheme.PRIMARY;
            case "C-": case "D+": return AppTheme.WARNING;
            case "D":             return new Color(0xD97706);
            default:              return AppTheme.DANGER;
        }
    }
}
