package model;
public class Student extends User {
    public Student(int id,String username,String password,String fullName,String email,String phone,String address,String department){
        super(id,username,password,fullName,email,phone,address,department);}
    public Student(){}
    @Override public String getRole(){return "student";}
}
