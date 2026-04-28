package model;
public class Attendance {
    private int id,studentId,courseId,sessionNumber;
    private String sessionType,status,sessionDate;
    public Attendance(int id,int sid,int cid,int sn,String st,String stat,String sd){
        this.id=id;studentId=sid;courseId=cid;sessionNumber=sn;sessionType=st;status=stat;sessionDate=sd;}
    public Attendance(){}
    public int getId(){return id;} public int getStudentId(){return studentId;}
    public int getCourseId(){return courseId;} public int getSessionNumber(){return sessionNumber;}
    public String getSessionType(){return sessionType;} public String getStatus(){return status;}
    public String getSessionDate(){return sessionDate;}
    public void setId(int i){id=i;} public void setStudentId(int s){studentId=s;}
    public void setCourseId(int c){courseId=c;} public void setSessionNumber(int n){sessionNumber=n;}
    public void setSessionType(String t){sessionType=t;} public void setStatus(String s){status=s;}
    public void setSessionDate(String d){sessionDate=d;}
}
