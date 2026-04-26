package model;


public abstract class User {
    private int    id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String role;
    private String department;
    private String profilePic;
    private String regNumber;
    private int    batch;
    private boolean isRepeat;

    public User(int id, String username, String password,
                String fullName, String email, String phone,
                String address, String department) {
        this.id = id; this.username = username; this.password = password;
        this.fullName = fullName; this.email = email; this.phone = phone;
        this.address = address; this.department = department;
    }

    public User() {}

    public abstract String getRole();

    @Override
    public String toString() { return fullName + " (" + getRole() + ")"; }

    // Getters
    public int     getId()         { return id; }
    public String  getUsername()   { return username; }
    public String  getPassword()   { return password; }
    public String  getFullName()   { return fullName; }
    public String  getEmail()      { return email; }
    public String  getPhone()      { return phone; }
    public String  getAddress()    { return address; }
    public String  getDepartment() { return department; }
    public String  getProfilePic() { return profilePic; }
    public String  getRegNumber()  { return regNumber; }
    public int     getBatch()      { return batch; }
    public boolean isRepeat()      { return isRepeat; }

    // Setters
    public void setId(int id)               { this.id = id; }
    public void setUsername(String u)       { this.username = u; }
    public void setPassword(String p)       { this.password = p; }
    public void setFullName(String n)       { this.fullName = n; }
    public void setEmail(String e)          { this.email = e; }
    public void setPhone(String p)          { this.phone = p; }
    public void setAddress(String a)        { this.address = a; }
    public void setDepartment(String d)     { this.department = d; }
    public void setProfilePic(String pic)   { this.profilePic = pic; }
    public void setRegNumber(String r)      { this.regNumber = r; }
    public void setBatch(int b)             { this.batch = b; }
    public void setRepeat(boolean r)        { this.isRepeat = r; }
}
