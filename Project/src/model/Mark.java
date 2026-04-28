package model;
public class Mark {
    private int id,studentId,courseId,enteredBy; private String examType; private double mark;
    public Mark(int id,int sid,int cid,String et,double m,int eb){this.id=id;studentId=sid;courseId=cid;examType=et;mark=m;enteredBy=eb;}
    public Mark(){}
    public int getId(){return id;} public int getStudentId(){return studentId;}
    public int getCourseId(){return courseId;} public String getExamType(){return examType;}
    public double getMark(){return mark;} public int getEnteredBy(){return enteredBy;}
    public void setId(int i){id=i;} public void setStudentId(int s){studentId=s;}
    public void setCourseId(int c){courseId=c;} public void setExamType(String e){examType=e;}
    public void setMark(double m){mark=m;} public void setEnteredBy(int e){enteredBy=e;}
}
