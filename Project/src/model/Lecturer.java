package model;
public class Lecturer extends User {
    private String specialization;
    public Lecturer(int id,String username,String password,String fullName,String email,String phone,String address,String department,String spec){
        super(id,username,password,fullName,email,phone,address,department);this.specialization=spec;}
    public Lecturer(){}
    @Override public String getRole(){return "lecturer";}
    public String getSpecialization(){return specialization;}
    public void setSpecialization(String s){specialization=s;}
}
