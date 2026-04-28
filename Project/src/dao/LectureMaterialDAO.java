package dao;

import model.LectureMaterial;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class LectureMaterialDAO {

    private final Connection conn;

    public LectureMaterialDAO() {
        conn = DBConnection.getConnection();
    }


    public static Path getStorageRoot() {
        return Paths.get(System.getProperty("user.home"), "FMSNEW", "lecture_materials");
    }


    static String extensionSuffix(String fileName) {
        if (fileName == null) return "";
        int i = fileName.lastIndexOf('.');
        if (i <= 0 || i >= fileName.length() - 1) return "";
        return fileName.substring(i).toLowerCase();
    }

    /** Check if the file type is allowed (PDF, image, text). */
    static boolean isAllowedFileType(String fileName) {
        if (fileName == null) return false;
        String ext = extensionSuffix(fileName);
        switch (ext) {
            case ".pdf":
            case ".png":
            case ".jpg":
            case ".jpeg":
            case ".gif":
            case ".bmp":
            case ".txt":
            case ".csv":
            case ".md":
                return true;
            default:
                return false;
        }
    }

    private boolean courseBelongsToLecturer(int courseId, int lecturerId) {
        try {
            PreparedStatement s = conn.prepareStatement(
                "SELECT 1 FROM courses WHERE id=? AND lecturer_id=?");
            s.setInt(1, courseId);
            s.setInt(2, lecturerId);
            ResultSet rs = s.executeQuery();
            boolean found = rs.next();
            if (!found) {
                System.out.println("LectureMaterialDAO: course " + courseId + " does not belong to lecturer " + lecturerId);
            }
            return found;
        } catch (SQLException e) {
            System.out.println("courseBelongsToLecturer error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Upload a PDF, image, or text file and insert a DB row.
     * Only the course's assigned lecturer may upload.
     * Returns false with console error if anything fails.
     */
    public boolean addMaterial(int courseId, String title, File sourceFile, int lecturerId) {
        // Validate file
        if (sourceFile == null) { System.out.println("addMaterial: sourceFile is null"); return false; }
        if (!sourceFile.exists()) { System.out.println("addMaterial: file does not exist: " + sourceFile.getAbsolutePath()); return false; }
        if (!sourceFile.isFile()) { System.out.println("addMaterial: not a regular file: " + sourceFile.getAbsolutePath()); return false; }
        if (!sourceFile.canRead()) { System.out.println("addMaterial: cannot read file: " + sourceFile.getAbsolutePath()); return false; }

        // Validate file type
        if (!isAllowedFileType(sourceFile.getName())) {
            System.out.println("addMaterial: unsupported file type: " + sourceFile.getName());
            return false;
        }

        // Validate lecturer-course ownership
        if (!courseBelongsToLecturer(courseId, lecturerId)) return false;

        Path dest = null;
        try {
            // Ensure storage directory exists
            Path root = getStorageRoot();
            Files.createDirectories(root);
            System.out.println("addMaterial: storage dir: " + root.toAbsolutePath());

            String ext = extensionSuffix(sourceFile.getName());
            String unique = UUID.randomUUID().toString().replace("-","") + ext;
            dest = root.resolve(unique);

            // Copy file
            Files.copy(sourceFile.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("addMaterial: copied to " + dest.toAbsolutePath());

            String displayTitle = (title == null || title.trim().isEmpty())
                ? stripExtension(sourceFile.getName()) : title.trim();

            PreparedStatement s = conn.prepareStatement(
                "INSERT INTO lecture_materials(course_id,title,file_name,stored_path,uploaded_by) VALUES(?,?,?,?,?)");
            s.setInt(1, courseId);
            s.setString(2, displayTitle);
            s.setString(3, sourceFile.getName());
            s.setString(4, dest.toAbsolutePath().toString());
            s.setInt(5, lecturerId);
            boolean inserted = s.executeUpdate() > 0;
            if (!inserted) System.out.println("addMaterial: DB insert returned 0 rows");
            return inserted;

        } catch (Exception e) {
            System.out.println("addMaterial: exception: " + e.getMessage());
            e.printStackTrace();
        }
        // Clean up copied file if DB insert failed
        if (dest != null) {
            try { Files.deleteIfExists(dest); } catch (Exception ignored) {}
        }
        return false;
    }

    private static String stripExtension(String name) {
        int i = name.lastIndexOf('.');
        return i > 0 ? name.substring(0, i) : name;
    }

    /** Delete material owned by lecturer — also removes file from disk. */
    public boolean deleteMaterial(int materialId, int lecturerId) {
        String pathStr = null;
        try {
            PreparedStatement q = conn.prepareStatement(
                "SELECT m.stored_path FROM lecture_materials m " +
                "JOIN courses c ON m.course_id = c.id " +
                "WHERE m.id=? AND c.lecturer_id=?");
            q.setInt(1, materialId);
            q.setInt(2, lecturerId);
            ResultSet rs = q.executeQuery();
            if (!rs.next()) return false;
            pathStr = rs.getString(1);

            PreparedStatement d = conn.prepareStatement("DELETE FROM lecture_materials WHERE id=?");
            d.setInt(1, materialId);
            if (d.executeUpdate() <= 0) return false;
        } catch (SQLException e) {
            System.out.println("deleteMaterial: " + e.getMessage());
            return false;
        }
        if (pathStr != null) {
            try { Files.deleteIfExists(Paths.get(pathStr)); } catch (Exception ignored) {}
        }
        return true;
    }

    public List<LectureMaterial> listForLecturer(int lecturerId) {
        List<LectureMaterial> list = new ArrayList<>();
        String sql = "SELECT m.id, m.course_id, c.course_code, c.course_name, m.title, m.file_name, " +
            "m.stored_path, m.uploaded_by, m.uploaded_at FROM lecture_materials m " +
            "JOIN courses c ON m.course_id = c.id WHERE c.lecturer_id=? ORDER BY m.uploaded_at DESC";
        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, lecturerId);
            ResultSet rs = s.executeQuery();
            while (rs.next()) list.add(row(rs));
        } catch (SQLException e) { System.out.println("listForLecturer: " + e.getMessage()); }
        return list;
    }

    public List<LectureMaterial> listForStudent(int studentId) {
        List<LectureMaterial> list = new ArrayList<>();
        String sql = "SELECT m.id, m.course_id, c.course_code, c.course_name, m.title, m.file_name, " +
            "m.stored_path, m.uploaded_by, m.uploaded_at FROM lecture_materials m " +
            "JOIN courses c ON m.course_id = c.id " +
            "JOIN student_courses sc ON sc.course_id = c.id AND sc.student_id=? " +
            "ORDER BY c.course_code, m.uploaded_at DESC";
        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, studentId);
            ResultSet rs = s.executeQuery();
            while (rs.next()) list.add(row(rs));
        } catch (SQLException e) { System.out.println("listForStudent: " + e.getMessage()); }
        return list;
    }

    private LectureMaterial row(ResultSet rs) throws SQLException {
        return new LectureMaterial(
            rs.getInt("id"),
            rs.getInt("course_id"),
            rs.getString("course_code"),
            rs.getString("course_name"),
            rs.getString("title"),
            rs.getString("file_name"),
            rs.getString("stored_path"),
            rs.getInt("uploaded_by"),
            rs.getString("uploaded_at")
        );
    }
}
