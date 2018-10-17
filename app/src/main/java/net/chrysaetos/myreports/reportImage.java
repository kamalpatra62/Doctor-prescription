package net.chrysaetos.myreports;

import android.graphics.Bitmap;
import android.net.Uri;

public class reportImage {

    private Uri imageReportUri;
    String text;
    private Bitmap bitmap;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Uri getImageReportUri() {
        return imageReportUri;
    }

    public void setImageReportUri(Uri imageReportUri) {
        this.imageReportUri = imageReportUri;
    }

    public reportImage() {

    }




}
