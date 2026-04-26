package dao;
import model.*; import java.sql.*; import java.util.*;
public class CourseDAO {
    private Connection conn;
    public CourseDAO(){conn=DBConnection.getConnection();}
    public List<Course> getAllCourses(){
        List<Course> l=new ArrayList<>();
        try{ResultSet rs=conn.createStatement().executeQuery("SELECT * FROM courses ORDER BY course_code");
        while(rs.next())l.add(build(rs));}catch(SQLException e){System.out.println(e.getMessage());}return l;}
    public List<Course> getCoursesForStudent(int sid){
        List<Course> l=new ArrayList<>();
        try{PreparedStatement s=conn.prepareStatement("SELECT c.* FROM courses c JOIN student_courses sc ON c.id=sc.course_id WHERE sc.student_id=? ORDER BY c.course_code");
        s.setInt(1,sid);ResultSet rs=s.executeQuery();while(rs.next())l.add(build(rs));}catch(SQLException e){System.out.println(e.getMessage());}return l;}
    public List<Course> getCoursesForLecturer(int lid){
        List<Course> l=new ArrayList<>();
        try{PreparedStatement s=conn.prepareStatement("SELECT * FROM courses WHERE lecturer_id=? ORDER BY course_code");
        s.setInt(1,lid);ResultSet rs=s.executeQuery();while(rs.next())l.add(build(rs));}catch(SQLException e){System.out.println(e.getMessage());}return l;}
    public boolean addCourse(Course c){
        try{PreparedStatement s=conn.prepareStatement("INSERT INTO courses(course_code,course_name,department,credit_theory,credit_practical,lecturer_id,semester,has_practical) VALUES(?,?,?,?,?,?,?,?)");
        s.setString(1,c.getCourseCode());s.setString(2,c.getCourseName());s.setString(3,c.getDepartment());
        s.setInt(4,c.getCreditTheory());s.setInt(5,c.getCreditPractical());s.setInt(6,c.getLecturerId());
        s.setInt(7,c.getSemester());s.setInt(8,c.isHasPractical()?1:0);return s.executeUpdate()>0;}
        catch(SQLException e){System.out.println(e.getMessage());return false;}}
    public boolean deleteCourse(int id){
        try{PreparedStatement s=conn.prepareStatement("DELETE FROM courses WHERE id=?");s.setInt(1,id);return s.executeUpdate()>0;}
        catch(SQLException e){return false;}}
    public boolean enrollStudent(int sid,int cid){
        try{PreparedStatement s=conn.prepareStatement("INSERT IGNORE INTO student_courses(student_id,course_id) VALUES(?,?)");
        s.setInt(1,sid);s.setInt(2,cid);return s.executeUpdate()>0;}catch(SQLException e){return false;}}
    private Course build(ResultSet rs) throws SQLException{
        return new Course(rs.getInt("id"),rs.getString("course_code"),rs.getString("course_name"),
        rs.getString("department"),rs.getInt("credit_theory"),rs.getInt("credit_practical"),
        rs.getInt("lecturer_id"),rs.getInt("semester"),rs.getInt("has_practical")==1);}
}
