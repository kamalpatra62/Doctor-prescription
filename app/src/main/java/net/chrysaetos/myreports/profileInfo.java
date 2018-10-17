package net.chrysaetos.myreports;

import android.net.Uri;

public class profileInfo {

    private String phno;
    private String imagePath;

    public profileInfo() {
    }

    public profileInfo(String phno, String imagePath) {
        this.phno = phno;
        this.imagePath = imagePath;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
