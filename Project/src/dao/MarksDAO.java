package dao;

import model.*;
import java.sql.*;
import java.util.*;


public class MarksDAO {

    private Connection conn;
    public MarksDAO() { conn = DBConnection.getConnection(); }

    /** Save or update a mark */
    public boolean saveMark(int studentId, int courseId, String examType, double mark, int enteredBy) {
        try {
            PreparedStatement chk = conn.prepareStatement(
                "SELECT id FROM marks WHERE student_id=? AND course_id=? AND exam_type=?");
            chk.setInt(1,studentId); chk.setInt(2,courseId); chk.setString(3,examType);
            ResultSet rs = chk.executeQuery();
            if (rs.next()) {
                PreparedStatement upd = conn.prepareStatement(
                    "UPDATE marks SET mark=?,entered_by=? WHERE id=?");
                upd.setDouble(1,mark); upd.setInt(2,enteredBy); upd.setInt(3,rs.getInt("id"));
                return upd.executeUpdate() > 0;
            } else {
                PreparedStatement ins = conn.prepareStatement(
                    "INSERT INTO marks(student_id,course_id,exam_type,mark,entered_by) VALUES(?,?,?,?,?)");
                ins.setInt(1,studentId); ins.setInt(2,courseId); ins.setString(3,examType);
                ins.setDouble(4,mark); ins.setInt(5,enteredBy);
                return ins.executeUpdate() > 0;
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); return false; }
    }

    public List<Mark> getMarksForStudent(int studentId, int courseId) {
        List<Mark> list = new ArrayList<>();
        try {
            PreparedStatement s = conn.prepareStatement(
                "SELECT * FROM marks WHERE student_id=? AND course_id=? ORDER BY exam_type");
            s.setInt(1,studentId); s.setInt(2,courseId);
            ResultSet rs = s.executeQuery();
            while (rs.next()) list.add(new Mark(rs.getInt("id"),rs.getInt("student_id"),
                rs.getInt("course_id"),rs.getString("exam_type"),rs.getDouble("mark"),rs.getInt("entered_by")));
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return list;
    }

    public double getCAAverage(int studentId, int courseId) {
        try {
            PreparedStatement s = conn.prepareStatement(
                "SELECT AVG(mark) FROM marks WHERE student_id=? AND course_id=? AND exam_type!='final'");
            s.setInt(1,studentId); s.setInt(2,courseId);
            ResultSet rs = s.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return 0;
    }

    public boolean isEligibleForFinal(int studentId, int courseId) {
        return getCAAverage(studentId, courseId) >= 40;
    }

    public double getFinalMark(int studentId, int courseId) {
        try {
            PreparedStatement s = conn.prepareStatement(
                "SELECT mark FROM marks WHERE student_id=? AND course_id=? AND exam_type='final'");
            s.setInt(1,studentId); s.setInt(2,courseId);
            ResultSet rs = s.executeQuery();
            if (rs.next()) return rs.getDouble("mark");
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return -1;
    }

    public double getSpecificMark(int studentId, int courseId, String examType) {
        try {
            PreparedStatement s = conn.prepareStatement(
                "SELECT mark FROM marks WHERE student_id=? AND course_id=? AND exam_type=?");
            s.setInt(1,studentId); s.setInt(2,courseId); s.setString(3,examType);
            ResultSet rs = s.executeQuery();
            if (rs.next()) return rs.getDouble("mark");
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return -1;
    }

    /** UGC Circular 12-2024 grading */
    public static String getGrade(double mark) {
        if (mark >= 85) return "A+";
        if (mark >= 75) return "A";
        if (mark >= 70) return "A-";
        if (mark >= 65) return "B+";
        if (mark >= 60) return "B";
        if (mark >= 55) return "B-";
        if (mark >= 50) return "C+";
        if (mark >= 45) return "C";
        if (mark >= 40) return "C-";
        if (mark >= 35) return "D+";
        if (mark >= 30) return "D";
        return "E";
    }

    public static double getGradePoint(String grade) {
        switch (grade) {
            case "A+": case "A": return 4.0;
            case "A-": return 3.7;
            case "B+": return 3.3;
            case "B":  return 3.0;
            case "B-": return 2.7;
            case "C+": return 2.3;
            case "C":  return 2.0;
            case "C-": return 1.7;
            case "D+": return 1.3;
            case "D":  return 1.0;
            default:   return 0.0;
        }
    }


    public double calculateSGPA(int studentId) {
        try {
            PreparedStatement s = conn.prepareStatement(
                "SELECT c.id, c.credit_theory+c.credit_practical AS credits " +
                "FROM courses c JOIN student_courses sc ON c.id=sc.course_id WHERE sc.student_id=?");
            s.setInt(1,studentId);
            ResultSet rs = s.executeQuery();
            double totalW = 0; int totalC = 0;
            while (rs.next()) {
                int cid = rs.getInt("id"); int credits = rs.getInt("credits");
                double fm = getFinalMark(studentId, cid);
                double m  = fm >= 0 ? fm : getCAAverage(studentId, cid);
                double gp = getGradePoint(getGrade(m));
                totalW += gp * credits; totalC += credits;
            }
            if (totalC == 0) return 0;
            return Math.round((totalW / totalC) * 100.0) / 100.0;
        } catch (SQLException e) { System.out.println(e.getMessage()); return 0; }
    }


    public double calculateCGPA(int studentId) {
        return calculateSGPA(studentId);
    }


    public Object[][] getBatchMarksSummary(int courseId) {
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
                double ca1    = getSpecificMark(id, courseId, "CA1");
                double ca2    = getSpecificMark(id, courseId, "CA2");
                double assign = getSpecificMark(id, courseId, "assignment");
                double caAvg  = getCAAverage(id, courseId);
                double finalM = getFinalMark(id, courseId);
                String grade  = finalM >= 0 ? getGrade(finalM) : (caAvg > 0 ? getGrade(caAvg) : "-");
                rows.add(new Object[]{
                    rs.getString("username"),
                    rs.getString("full_name"),
                    ca1 >= 0 ? String.valueOf(ca1) : "-",
                    ca2 >= 0 ? String.valueOf(ca2) : "-",
                    assign >= 0 ? String.valueOf(assign) : "-",
                    String.format("%.1f", caAvg),
                    isEligibleForFinal(id, courseId) ? "YES" : "NO",
                    finalM >= 0 ? String.valueOf(finalM) : "Not entered",
                    grade
                });
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return rows.toArray(new Object[0][]);
    }


    public Object[][] getBatchGPASummary() {
        List<Object[]> rows = new ArrayList<>();
        try {
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT id, username, full_name FROM users WHERE role='student' ORDER BY full_name");
            while (rs.next()) {
                int id = rs.getInt("id");
                double sgpa = calculateSGPA(id);
                double cgpa = calculateCGPA(id);
                rows.add(new Object[]{
                    rs.getString("username"),
                    rs.getString("full_name"),
                    String.format("%.2f", sgpa),
                    String.format("%.2f", cgpa)
                });
            }
        } catch (SQLException e) { System.out.println(e.getMessage()); }
        return rows.toArray(new Object[0][]);
    }
}
