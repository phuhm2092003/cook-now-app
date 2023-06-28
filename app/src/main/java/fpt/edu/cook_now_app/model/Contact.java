package fpt.edu.cook_now_app.model;

public class Contact {
    private String id;
    private String fullname;
    private String phoneNumber;
    private String email;
    private String content;

    public Contact() {
    }

    public Contact(String id, String fullname, String phoneNumber, String email, String content) {
        this.id = id;
        this.fullname = fullname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
