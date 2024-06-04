public class Identification {
    private String email;
    private String password;

    public Identification(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Identification() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
