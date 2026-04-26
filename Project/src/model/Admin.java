package model;
public class Admin extends User {
    public Admin(int id,String username,String password,String fullName,String email,String phone,String address,String department){
        super(id,username,password,fullName,email,phone,address,department);}
    public Admin(){}
    @Override public String getRole(){return "admin";}
}
