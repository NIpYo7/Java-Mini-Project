package dao;

import model.*;
import java.sql.*;
import java.util.*;

/**
 * UserDAO - all user database operations
 * NEW: changePassword(), updateProfilePic(), getStudentDetail()
 */
public class UserDAO {

    private Connection conn;
    public UserDAO() { conn = DBConnection.getConnection(); }

    /** Login - returns correct User subclass or null */
    public User login(String username, String password) {
        try {
            PreparedStatement s = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            s.setString(1, username); s.setString(2, password);
            ResultSet rs = s.executeQuery();
            if (rs.next()) return buildUser(rs);
        } catch (SQLException e) { System.out.println("Login error: " + e.getMessage()); }
        return null;
    }

    /** Get all users */
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM users ORDER BY role,full_name");
            while (rs.next()) list.add(buildUser(rs));
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return list;
    }

    /** Get all students */
    public List<User> getAllStudents() {
        List<User> list = new ArrayList<>();
        try {
            PreparedStatement s = conn.prepareStatement(
                "SELECT * FROM users WHERE role='student' ORDER BY full_name");
            ResultSet rs = s.executeQuery();
            while (rs.next()) list.add(buildUser(rs));
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return list;
    }

    /** Get students enrolled to a lecturer's courses (optionally filtered by course). */
    public List<User> getStudentsForLecturer(int lecturerId, Integer courseId) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT DISTINCT u.* " +
                     "FROM users u " +
                     "JOIN student_courses sc ON u.id = sc.student_id " +
                     "JOIN courses c ON c.id = sc.course_id " +
                     "WHERE u.role='student' AND c.lecturer_id=? ";
        if (courseId != null) sql += "AND c.id=? ";
        sql += "ORDER BY u.full_name";
        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, lecturerId);
            if (courseId != null) s.setInt(2, courseId);
            ResultSet rs = s.executeQuery();
            while (rs.next()) list.add(buildUser(rs));
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return list;
    }

    /** Count users with the given role (e.g. {@code student}). */
    public int countByRole(String role) {
        try {
            PreparedStatement s = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE role=?");
            s.setString(1, role);
            ResultSet rs = s.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return 0;
    }

    /** Get user by ID */
    public User getUserById(int id) {
        try {
            PreparedStatement s = conn.prepareStatement("SELECT * FROM users WHERE id=?");
            s.setInt(1, id);
            ResultSet rs = s.executeQuery();
            if (rs.next()) return buildUser(rs);
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return null;
    }

    /** Add a new user */
    public boolean addUser(User user) {
        try {
            PreparedStatement s = conn.prepareStatement(
                "INSERT INTO users (username,password,full_name,email,phone,address,role,department,reg_number,batch,is_repeat) VALUES(?,?,?,?,?,?,?,?,?,?,?)");
            s.setString(1, user.getUsername()); s.setString(2, user.getPassword());
            s.setString(3, user.getFullName()); s.setString(4, user.getEmail());
            s.setString(5, user.getPhone());    s.setString(6, user.getAddress());
            s.setString(7, user.getRole());     s.setString(8, user.getDepartment());
            s.setString(9, user.getRegNumber());
            s.setInt(10, user.getBatch());
            s.setInt(11, user.isRepeat() ? 1 : 0);
            return s.executeUpdate() > 0;
        } catch (SQLException e) { System.out.println("addUser error: " + e.getMessage()); return false; }
    }

    /** Update profile (name, email, phone, address, department) */
    public boolean updateProfile(int id, String fullName, String email, String phone, String address, String dept) {
        try {
            PreparedStatement s = conn.prepareStatement(
                "UPDATE users SET full_name=?,email=?,phone=?,address=?,department=? WHERE id=?");
            s.setString(1,fullName); s.setString(2,email); s.setString(3,phone);
            s.setString(4,address); s.setString(5,dept);   s.setInt(6,id);
            return s.executeUpdate() > 0;
        } catch (SQLException e) { System.out.println(e.getMessage()); return false; }
    }

    /**
     * CHANGE PASSWORD
     * Verifies old password first, then updates.
     * Used by Student, Lecturer, TechOfficer
     */
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        try {
            // Check old password is correct first
            PreparedStatement check = conn.prepareStatement(
                "SELECT id FROM users WHERE id=? AND password=?");
            check.setInt(1, userId);
            check.setString(2, oldPassword);
            ResultSet rs = check.executeQuery();
            if (!rs.next()) return false; // old password wrong

            // Update to new password
            PreparedStatement update = conn.prepareStatement(
                "UPDATE users SET password=? WHERE id=?");
            update.setString(1, newPassword);
            update.setInt(2, userId);
            return update.executeUpdate() > 0;
        } catch (SQLException e) { System.out.println("changePassword error: " + e.getMessage()); return false; }
    }

    /**
     * UPDATE PROFILE PICTURE PATH
     * Stores the file path of the uploaded image
     */
    public boolean updateProfilePic(int userId, String picPath) {
        try {
            PreparedStatement s = conn.prepareStatement("UPDATE users SET profile_pic=? WHERE id=?");
            s.setString(1, picPath); s.setInt(2, userId);
            return s.executeUpdate() > 0;
        } catch (SQLException e) { System.out.println(e.getMessage()); return false; }
    }

    /** Delete user */
    public boolean deleteUser(int id) {
        try {
            PreparedStatement s = conn.prepareStatement("DELETE FROM users WHERE id=?");
            s.setInt(1,id); return s.executeUpdate() > 0;
        } catch (SQLException e) { System.out.println(e.getMessage()); return false; }
    }

    /**
     * POLYMORPHISM: creates correct subclass from DB row
     */
    public User buildUser(ResultSet rs) throws SQLException {
        int    id   = rs.getInt("id");
        String un   = rs.getString("username");
        String pw   = rs.getString("password");
        String name = rs.getString("full_name");
        String em   = rs.getString("email");
        String ph   = rs.getString("phone");
        String addr = rs.getString("address");
        String role = rs.getString("role");
        String dept = rs.getString("department");
        String pic  = rs.getString("profile_pic");
        String reg  = rs.getString("reg_number");
        int batch   = rs.getInt("batch");
        boolean rep = rs.getInt("is_repeat") == 1;

        User u;
        switch (role) {
            case "admin":       u = new Admin(id,un,pw,name,em,ph,addr,dept); break;
            case "lecturer":    u = new Lecturer(id,un,pw,name,em,ph,addr,dept,""); break;
            case "student":     u = new Student(id,un,pw,name,em,ph,addr,dept); break;
            case "tech_officer":u = new TechOfficer(id,un,pw,name,em,ph,addr,dept,""); break;
            default:            u = new Admin(); u.setId(id); u.setFullName(name);
        }
        u.setProfilePic(pic);
        u.setRegNumber(reg);
        u.setBatch(batch);
        u.setRepeat(rep);
        return u;
    }
}
