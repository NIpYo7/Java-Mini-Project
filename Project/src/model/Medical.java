package model;
public class Medical {
    private int id,studentId,courseId,sessionNum;
    private String reason,docPath,sessionDate,submittedAt;
    private int approved;
    public Medical(int id,int sid,int cid,int sn,String sd,String r,String dp,int app,String sat){
        this.id=id;studentId=sid;courseId=cid;sessionNum=sn;sessionDate=sd;reason=r;docPath=dp;approved=app;submittedAt=sat;}
    public Medical(){}
    public int getId(){return id;} public int getStudentId(){return studentId;}
    public int getCourseId(){return courseId;} public int getSessionNum(){return sessionNum;}
    public String getSessionDate(){return sessionDate;} public String getReason(){return reason;}
    public String getDocPath(){return docPath;} public int getApproved(){return approved;}
    public String getSubmittedAt(){return submittedAt;}
    public String getApprovedLabel(){
        if(approved==1)return "Approved";
        if(approved==2)return "Rejected";
        return "Pending";
    }
    public void setId(int i){id=i;} public void setStudentId(int s){studentId=s;}
    public void setCourseId(int c){courseId=c;} public void setSessionNum(int n){sessionNum=n;}
    public void setSessionDate(String d){sessionDate=d;} public void setReason(String r){reason=r;}
    public void setDocPath(String d){docPath=d;} public void setApproved(int a){approved=a;}
    public void setSubmittedAt(String s){submittedAt=s;}
}
