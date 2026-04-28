package model;
public class TechOfficer extends User {
    private String labAssigned;
    public TechOfficer(int id,String username,String password,String fullName,String email,String phone,String address,String department,String lab){
        super(id,username,password,fullName,email,phone,address,department);this.labAssigned=lab;}
    public TechOfficer(){}
    @Override public String getRole(){return "tech_officer";}
    public String getLabAssigned(){return labAssigned;}
    public void setLabAssigned(String l){labAssigned=l;}
}
