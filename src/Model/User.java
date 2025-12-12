package Model;

public class User {
    private int id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private String password;
    private String role;

    User(String fullName, String email, String phoneNumber, String address, String password, String role) {
        fullName = this.fullName;
        email = this.email;
        phoneNumber = this.phoneNumber;
        address = this.address;
        password = this.password;
        role = this.role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
