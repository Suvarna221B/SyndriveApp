package universe.sk.syndriveapp;

import android.net.Uri;

import com.google.android.gms.tasks.Task;


public class Userinfo {
    public String username;
    public String uemail;
    public String udate;
    public String bloodgroup;
    public String cname1;
    public String cnum1;
    public String cname2;
    public  String cnum2;
    public String cname3;
    public String cnum3;
    // public Uri imageUri;
    public  Userinfo(){

    }

    public Userinfo(String username, String uemail, String udate, String bloodgroup, String cname1, String cnum1, String cname2, String cnum2, String cname3, String cnum3) {
        this.username = username;
        this.uemail = uemail;
        this.udate = udate;
        this.bloodgroup = bloodgroup;
        this.cname1 = cname1;
        this.cnum1 = cnum1;
        this.cname2 = cname2;
        this.cnum2 = cnum2;
        this.cname3 = cname3;
        this.cnum3 = cnum3;
    }

    public String getUsername() {
        return username;
    }

    public String getUemail() {
        return uemail;
    }

    public String getUdate() {
        return udate;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public String getCname1() {
        return cname1;
    }

    public String getCnum1() {
        return cnum1;
    }

    public String getCname2() {
        return cname2;
    }

    public String getCnum2() {
        return cnum2;
    }

    public String getCname3() {
        return cname3;
    }

    public String getCnum3() {
        return cnum3;
    }
}

