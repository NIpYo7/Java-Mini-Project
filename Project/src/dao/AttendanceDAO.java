package dao;

import model.*;
import java.sql.*;
import java.util.*;


public class AttendanceDAO {

    private Connection conn;
    public AttendanceDAO() { conn = DBConnection.getConnection(); }

    public List<Attendance> getAttendance(int studentId, int courseId, String type) {
        List<Attendance> list = new ArrayList<>();
        String sql = type.equals("all")
            ? "SELECT * FROM attendance WHERE student_id=? AND course_id=? ORDER BY session_type,session_number"
            : "SELECT * FROM attendance WHERE student_id=? AND course_id=? AND session_type=? ORDER BY session_number";
        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, studentId); s.setInt(2, courseId);
            if (!type.equals("all")) s.setString(3, type);
            ResultSet rs = s.executeQuery();
            while (rs.next()) list.add(new Attendance(
                rs.getInt("id"), rs.getInt("student_id"), rs.getInt("course_id"),
                rs.getInt("session_number"), rs.getString("session_type"),
                rs.getString("status"), rs.getString("session_date")));
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return list;
    }

    public double getAttendancePercent(int studentId, int courseId, String type) {
        int total = 0, present = 0;
        String sql = type.equals("all")
            ? "SELECT status FROM attendance WHERE student_id=? AND course_id=?"
            : "SELECT status FROM attendance WHERE student_id=? AND course_id=? AND session_type=?";
        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1,studentId); s.setInt(2,courseId);
            if (!type.equals("all")) s.setString(3, type);
            ResultSet rs = s.executeQuery();
            while (rs.next()) { total++; if (rs.getString("status").equals("present")) present++; }
            present += getApprovedMedicalCount(studentId, courseId);
            if (total == 0) return 0;
            return Math.min(((double) present / total) * 100, 100.0);
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return 0;
    }

    public int getApprovedMedicalCount(int studentId, int courseId) {
        try {
            PreparedStatement s = conn.prepareStatement(
                "SELECT COUNT(*) FROM medicals WHERE student_id=? AND course_id=? AND approved=1");
            s.setInt(1,studentId); s.setInt(2,courseId);
            ResultSet rs = s.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return 0;
    }


    public String getSessionDate(int courseId, int sessionNumber) {
        try {
            PreparedStatement s = conn.prepareStatement(
                "SELECT session_date FROM course_sessions " +
                "WHERE course_id = ? AND session_number = ? LIMIT 1");
            s.setInt(1, courseId);
            s.setInt(2, sessionNumber);
            ResultSet rs = s.executeQuery();
            if (rs.next()) return rs.getString("session_date");
        } catch (SQLException e) {
            System.out.println("getSessionDate: " + e.getMessage());
        }
        return null;
    }


    public int getSessionCount(int courseId) {
        try {
            PreparedStatement s = conn.prepareStatement(
                "SELECT COUNT(*) FROM course_sessions WHERE course_id = ?");
            s.setInt(1, courseId);
            ResultSet rs = s.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("getSessionCount: " + e.getMessage());
        }
        return 30;
    }

    public boolean saveAttendance(int studentId, int courseId, int sessionNum,
                                   String sessionType, String status, String sessionDate) {
        try {
            PreparedStatement chk = conn.prepareStatement(
                "SELECT id FROM attendance WHERE student_id=? AND course_id=? AND session_number=? AND session_type=?");
            chk.setInt(1,studentId); chk.setInt(2,courseId);
            chk.setInt(3,sessionNum); chk.setString(4,sessionType);
            ResultSet rs = chk.executeQuery();
            if (rs.next()) {
                int eid = rs.getInt("id");
                PreparedStatement upd = conn.prepareStatement(
                    "UPDATE attendance SET status=?,session_date=? WHERE id=?");
                upd.setString(1,status); upd.setString(2,sessionDate); upd.setInt(3,eid);
                return upd.executeUpdate() > 0;
            } else {
                PreparedStatement ins = conn.prepareStatement(
                    "INSERT INTO attendance(student_id,course_id,session_number,session_type,status,session_date) VALUES(?,?,?,?,?,?)");
                ins.setInt(1,studentId); ins.setInt(2,courseId); ins.setInt(3,sessionNum);
                ins.setString(4,sessionType); ins.setString(5,status); ins.setString(6,sessionDate);
                return ins.executeUpdate() > 0;
            }
        } catch (SQLException e) { System.out.println("saveAttendance: " + e.getMessage()); return false; }
    }

    public boolean attendanceExistsForDate(int studentId, int courseId, String sessionDate) {
        try {
            PreparedStatement s = conn.prepareStatement(
                "SELECT COUNT(*) FROM attendance WHERE student_id=? AND course_id=? AND session_date=? AND status='present'");
            s.setInt(1,studentId); s.setInt(2,courseId); s.setString(3,sessionDate);
            ResultSet rs = s.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return false;
    }

    public int addMedical(int studentId, int courseId, int sessionNum,
                           String sessionDate, String reason) {
        if (attendanceExistsForDate(studentId, courseId, sessionDate)) return 1;
        try {
            PreparedStatement s = conn.prepareStatement(
                "INSERT INTO medicals(student_id,course_id,session_num,session_date,reason,approved) VALUES(?,?,?,?,?,0)");
            s.setInt(1,studentId); s.setInt(2,courseId); s.setInt(3,sessionNum);
            s.setString(4,sessionDate); s.setString(5,reason);
            return s.executeUpdate() > 0 ? 0 : 2;
        } catch (SQLException e) { System.out.println("addMedical: " + e.getMessage()); return 2; }
    }

    public boolean updateMedicalApproval(int medicalId, int status) {
        try {
            PreparedStatement s = conn.prepareStatement("UPDATE medicals SET approved=? WHERE id=?");
            s.setInt(1,status); s.setInt(2,medicalId);
            return s.executeUpdate() > 0;
        } catch (SQLException e) { System.out.println(e.getMessage()); return false; }
    }

    public List<Medical> getMedicalsForStudent(int studentId) {
        List<Medical> list = new ArrayList<>();
        try {
            PreparedStatement s = conn.prepareStatement(
                "SELECT * FROM medicals WHERE student_id=? ORDER BY submitted_at DESC");
            s.setInt(1, studentId);
            ResultSet rs = s.executeQuery();
            while (rs.next()) list.add(new Medical(
                rs.getInt("id"), rs.getInt("student_id"), rs.getInt("course_id"),
                rs.getInt("session_num"), rs.getString("session_date"),
                rs.getString("reason"), rs.getString("doc_path"),
                rs.getInt("approved"), rs.getString("submitted_at")));
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return list;
    }

    public Object[][] getAllMedicalsWithStudentInfo() {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT m.id, u.username, u.full_name, c.course_code, " +
                     "m.session_num, m.session_date, m.reason, m.approved, m.submitted_at " +
                     "FROM medicals m " +
                     "JOIN users u ON m.student_id=u.id " +
                     "JOIN courses c ON m.course_id=c.id " +
                     "ORDER BY m.submitted_at DESC";
        try {
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                String approvedLabel = rs.getInt("approved")==1 ? "Approved" :
                                       rs.getInt("approved")==2 ? "Rejected" : "Pending";
                rows.add(new Object[]{
                    rs.getInt("id"),
                    rs.getString("username") + " – " + rs.getString("full_name"),
                    rs.getString("course_code"),
                    rs.getInt("session_num"),
                    rs.getString("session_date"),
                    rs.getString("reason"),
                    approvedLabel,
                    rs.getString("submitted_at")
                });
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return rows.toArray(new Object[0][]);
    }

    public Object[][] getBatchAttendanceSummary(int courseId) {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT DISTINCT u.id, u.username, u.full_name " +
                     "FROM users u JOIN student_courses sc ON u.id=sc.student_id " +
                     "WHERE sc.course_id=? ORDER BY u.full_name";
        try {
            PreparedStatement s = conn.prepareStatement(sql);
            s.setInt(1, courseId);
            ResultSet rs = s.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                double t = getAttendancePercent(id, courseId, "theory");
                double p = getAttendancePercent(id, courseId, "practical");
                double c = getAttendancePercent(id, courseId, "all");
                rows.add(new Object[]{
                    rs.getString("username"),
                    rs.getString("full_name"),
                    String.format("%.1f%%", t),
                    String.format("%.1f%%", p),
                    String.format("%.1f%%", c),
                    c >= 80 ? "YES" : "NO"
                });
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return rows.toArray(new Object[0][]);
    }

    public int countPendingMedicals() {
        try {
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT COUNT(*) FROM medicals WHERE approved=0");
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return 0;
    }

    public int countDistinctSessionsOnDate(String sessionDate) {
        try {
            PreparedStatement s = conn.prepareStatement(
                "SELECT COUNT(DISTINCT CONCAT(course_id, '-', session_number, '-', session_type)) " +
                "FROM attendance WHERE session_date=?");
            s.setString(1, sessionDate);
            ResultSet rs = s.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return 0;
    }

    public double getAverageAttendanceAcrossEnrollments() {
        try {
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT student_id, course_id FROM student_courses");
            double sum = 0; int n = 0;
            while (rs.next()) {
                sum += getAttendancePercent(rs.getInt(1), rs.getInt(2), "all");
                n++;
            }
            return n == 0 ? 0 : sum / n;
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return 0;
    }

    public int countStudentCourseEnrollments() {
        try {
            ResultSet rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM student_courses");
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return 0;
    }
}
