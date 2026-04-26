package dao;
import model.*; import java.sql.*; import java.util.*;
public class NoticeDAO {
    private Connection conn;
    public NoticeDAO(){conn=DBConnection.getConnection();}
    public List<Notice> getAllNotices(){
        List<Notice> l=new ArrayList<>();
        try{ResultSet rs=conn.createStatement().executeQuery("SELECT * FROM notices ORDER BY created_at DESC");
        while(rs.next())l.add(new Notice(rs.getInt("id"),rs.getString("title"),rs.getString("content"),rs.getInt("created_by"),rs.getString("created_at")));}
        catch(SQLException e){System.out.println(e.getMessage());}return l;}
    public boolean addNotice(String title,String content,int createdBy){
        try{PreparedStatement s=conn.prepareStatement("INSERT INTO notices(title,content,created_by) VALUES(?,?,?)");
        s.setString(1,title);s.setString(2,content);s.setInt(3,createdBy);return s.executeUpdate()>0;}
        catch(SQLException e){return false;}}
    public boolean deleteNotice(int id){
        try{PreparedStatement s=conn.prepareStatement("DELETE FROM notices WHERE id=?");s.setInt(1,id);return s.executeUpdate()>0;}
        catch(SQLException e){return false;}}
    public Object[][] getTimetable(String dept){
        List<Object[]> rows=new ArrayList<>();
        try{PreparedStatement s=conn.prepareStatement(
            "SELECT c.course_code,c.course_name,t.day_of_week,t.start_time,t.end_time,t.room FROM timetables t JOIN courses c ON t.course_id=c.id WHERE t.department=? ORDER BY FIELD(t.day_of_week,'Monday','Tuesday','Wednesday','Thursday','Friday'),t.start_time");
        s.setString(1,dept);ResultSet rs=s.executeQuery();
        while(rs.next())rows.add(new Object[]{rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)});}
        catch(SQLException e){System.out.println(e.getMessage());}return rows.toArray(new Object[0][]);}
    public boolean addTimetable(int courseId,String day,String start,String end,String room,String dept){
        try{PreparedStatement s=conn.prepareStatement("INSERT INTO timetables(course_id,day_of_week,start_time,end_time,room,department) VALUES(?,?,?,?,?,?)");
        s.setInt(1,courseId);s.setString(2,day);s.setString(3,start);s.setString(4,end);s.setString(5,room);s.setString(6,dept);return s.executeUpdate()>0;}
        catch(SQLException e){return false;}}
}
