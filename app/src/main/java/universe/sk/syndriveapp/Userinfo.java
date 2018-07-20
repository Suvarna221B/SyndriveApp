package universe.sk.syndriveapp;
public class Userinfo {
    public String username;
    public String uemail;
    public String udate;
    public String bloodgroup;
    public Userinfo(){

    }

    public Userinfo(String username, String uemail, String udate, String bloodgroup) {
        this.username = username;
        this.uemail = uemail;
        this.udate = udate;
        this.bloodgroup = bloodgroup;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUemail() {
        return uemail;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    public String getUdate(){
        return udate;
}

    public void setUdate(String udate) {
        this.udate = udate;
    }


    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }
}

