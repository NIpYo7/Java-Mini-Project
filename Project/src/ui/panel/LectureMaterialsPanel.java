package ui.panels;

import dao.CourseDAO;
import dao.LectureMaterialDAO;
import model.Course;
import model.LectureMaterial;
import model.User;
import ui.theme.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;


public class LectureMaterialsPanel extends BasePanel {

    private final LectureMaterialDAO materialDAO = new LectureMaterialDAO();
    private final CourseDAO courseDAO = new CourseDAO();

    private final boolean lecturerMode;
    private DefaultTableModel tableModel;
    private JTable table;

    public LectureMaterialsPanel(User user) {
        super(user);
        this.lecturerMode = "lecturer".equals(user.getRole());
        build();
    }

    private void build() {
        JPanel content = pageContent();
        JLabel title = AppTheme.sectionTitle("Lecture Materials");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel sub = new JLabel(lecturerMode
            ? "Upload PDF, text files, or images for your courses. Enrolled students can open them."
            : "Files your lecturers shared for the courses you are enrolled in.");
        sub.setFont(AppTheme.F_BODY);
        sub.setForeground(AppTheme.TEXT_MUTED);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title);
        vgap(content, 4);
        content.add(sub);
        vgap(content, 20);

        if (lecturerMode) {
            content.add(buildLecturerUploadRow());
            vgap(content, 16);
        }

        String[] cols = {"Course", "Title", "File", "Type", "Uploaded"};
        tableModel = model(cols);
        table = AppTheme.styledTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel card = AppTheme.card(12);
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(new EmptyBorder(16, 16, 16, 16));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(AppTheme.scrollTable(table), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);
        JButton openBtn = AppTheme.primaryBtn("Open File");
        openBtn.addActionListener(e -> openSelected());
        actions.add(openBtn);
        if (lecturerMode) {
            JButton del = AppTheme.ghostBtn("Delete Selected");
            del.addActionListener(e -> deleteSelected());
            actions.add(del);
        }
        card.add(actions, BorderLayout.SOUTH);

        content.add(card);
        refreshTable();
        add(scrollWrap(content));
    }

    private JPanel buildLecturerUploadRow() {
        List<Course> courses = courseDAO.getCoursesForLecturer(user.getId());
        String[] items = courses.stream()
            .map(c -> c.getId() + " | " + c.getCourseCode() + " — " + c.getCourseName())
            .toArray(String[]::new);

        JPanel card = AppTheme.card(12);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16,20,16,20));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel uploadTitle = new JLabel("Upload Lecture Material");
        uploadTitle.setFont(AppTheme.F_H3);
        uploadTitle.setForeground(AppTheme.TEXT_DARK);
        uploadTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(uploadTitle);
        card.add(Box.createVerticalStrut(12));

        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        row1.setOpaque(false);
        row1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<String> cmb = AppTheme.comboBox(items.length > 0 ? items : new String[]{"No courses assigned"});
        JTextField titleField = AppTheme.textField("Material title (optional)");
        titleField.setPreferredSize(new Dimension(240, 36));

        row1.add(AppTheme.fieldLabel("COURSE:"));
        row1.add(cmb);
        row1.add(AppTheme.fieldLabel("TITLE:"));
        row1.add(titleField);
        card.add(row1);
        card.add(Box.createVerticalStrut(8));

        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        row2.setOpaque(false);
        row2.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel selectedFileLbl = new JLabel("No file selected");
        selectedFileLbl.setFont(AppTheme.F_SMALL);
        selectedFileLbl.setForeground(AppTheme.TEXT_MUTED);

        // Three separate buttons for each file type
        JButton pickPdf = AppTheme.successBtn("Upload PDF");
        JButton pickImg = AppTheme.primaryBtn("Upload Image");
        JButton pickTxt = AppTheme.ghostBtn("Upload Text File");

        pickPdf.addActionListener(e -> {
            if (courses.isEmpty()) { JOptionPane.showMessageDialog(this,"No courses assigned to you.","Error",JOptionPane.ERROR_MESSAGE); return; }
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Select PDF File");
            fc.setAcceptAllFileFilterUsed(false);
            fc.addChoosableFileFilter(new FileNameExtensionFilter("PDF Files (*.pdf)", "pdf"));
            if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
            uploadFile(fc.getSelectedFile(), cmb, titleField, courses, selectedFileLbl);
        });

        pickImg.addActionListener(e -> {
            if (courses.isEmpty()) { JOptionPane.showMessageDialog(this,"No courses assigned to you.","Error",JOptionPane.ERROR_MESSAGE); return; }
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Select Image File");
            fc.setAcceptAllFileFilterUsed(false);
            fc.addChoosableFileFilter(new FileNameExtensionFilter(
                "Image Files (*.png, *.jpg, *.jpeg, *.gif, *.bmp)", "png","jpg","jpeg","gif","bmp"));
            if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
            uploadFile(fc.getSelectedFile(), cmb, titleField, courses, selectedFileLbl);
        });

        pickTxt.addActionListener(e -> {
            if (courses.isEmpty()) { JOptionPane.showMessageDialog(this,"No courses assigned to you.","Error",JOptionPane.ERROR_MESSAGE); return; }
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Select Text File");
            fc.setAcceptAllFileFilterUsed(false);
            fc.addChoosableFileFilter(new FileNameExtensionFilter(
                "Text Files (*.txt, *.csv, *.md)", "txt","csv","md"));
            if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
            uploadFile(fc.getSelectedFile(), cmb, titleField, courses, selectedFileLbl);
        });

        row2.add(pickPdf);
        row2.add(pickImg);
        row2.add(pickTxt);
        row2.add(selectedFileLbl);
        card.add(row2);

        JLabel hint = new JLabel("Supported: PDF documents, PNG/JPG/GIF images, TXT/CSV/MD text files");
        hint.setFont(AppTheme.F_SMALL);
        hint.setForeground(AppTheme.TEXT_MUTED);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        hint.setBorder(new EmptyBorder(4,0,0,0));
        card.add(hint);

        return card;
    }

    private void uploadFile(File f, JComboBox<String> cmb, JTextField titleField, List<Course> courses, JLabel statusLbl) {
        if (f == null || !f.isFile()) {
            JOptionPane.showMessageDialog(this, "Invalid file selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!f.canRead()) {
            JOptionPane.showMessageDialog(this,
                "Cannot read file: " + f.getAbsolutePath() + "\nCheck file permissions.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Validate extension
        String name = f.getName().toLowerCase();
        boolean allowed = name.endsWith(".pdf") || name.endsWith(".png") || name.endsWith(".jpg")
            || name.endsWith(".jpeg") || name.endsWith(".gif") || name.endsWith(".bmp")
            || name.endsWith(".txt") || name.endsWith(".csv") || name.endsWith(".md");
        if (!allowed) {
            JOptionPane.showMessageDialog(this,
                "Unsupported file type.\nPlease upload: PDF, PNG, JPG, GIF, BMP, TXT, CSV, or MD files.",
                "Unsupported File", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int cid;
        try {
            cid = Integer.parseInt(cmb.getSelectedItem().toString().split("\\|")[0].trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Select a course first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        statusLbl.setForeground(AppTheme.TEXT_MUTED);
        statusLbl.setText("Uploading: " + f.getName() + "...");

        boolean ok = materialDAO.addMaterial(cid, titleField.getText().trim(), f, user.getId());
        if (ok) {
            titleField.setText("");
            refreshTable();
            statusLbl.setForeground(AppTheme.SUCCESS);
            statusLbl.setText("✓ Uploaded: " + f.getName());
        } else {
            statusLbl.setForeground(AppTheme.DANGER);
            statusLbl.setText("✗ Upload failed — check file and course assignment.");
            JOptionPane.showMessageDialog(this,
                "Upload failed.\n\nPossible causes:\n" +
                "• You are not assigned to this course\n" +
                "• File could not be copied to storage\n" +
                "• Database error\n\n" +
                "File: " + f.getAbsolutePath(),
                "Upload Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getFileType(String fileName) {
        if (fileName == null) return "—";
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".pdf")) return "PDF";
        if (lower.endsWith(".png")||lower.endsWith(".jpg")||lower.endsWith(".jpeg")
            ||lower.endsWith(".gif")||lower.endsWith(".bmp")) return "Image";
        if (lower.endsWith(".txt")||lower.endsWith(".csv")||lower.endsWith(".md")) return "Text";
        return "File";
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<LectureMaterial> list = lecturerMode
            ? materialDAO.listForLecturer(user.getId())
            : materialDAO.listForStudent(user.getId());
        for (LectureMaterial m : list) {
            String when = m.getUploadedAt() != null ? m.getUploadedAt().replace('T',' ') : "";
            if (when.length() > 16) when = when.substring(0,16);
            tableModel.addRow(new Object[]{
                m.getCourseCode() + " — " + m.getCourseName(),
                m.getTitle(),
                m.getFileName(),
                getFileType(m.getFileName()),
                when
            });
        }
    }

    private void openSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,"Select a row first.","Materials",JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<LectureMaterial> list = lecturerMode
            ? materialDAO.listForLecturer(user.getId())
            : materialDAO.listForStudent(user.getId());
        if (row >= list.size()) return;
        File file = new File(list.get(row).getStoredPath());
        if (!file.isFile()) {
            JOptionPane.showMessageDialog(this,
                "File missing on disk:\n" + file.getAbsolutePath(),
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                Desktop.getDesktop().open(file);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Open this file manually:\n" + file.getAbsolutePath(),
                    "File Location", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Could not open file. Path:\n" + file.getAbsolutePath(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,"Select a material to delete.","Materials",JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(this,
            "Delete this material for all students?",
            "Confirm", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        List<LectureMaterial> list = materialDAO.listForLecturer(user.getId());
        if (row >= list.size()) return;
        int id = list.get(row).getId();
        if (materialDAO.deleteMaterial(id, user.getId())) {
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this,"Delete failed.","Error",JOptionPane.ERROR_MESSAGE);
        }
    }
}
