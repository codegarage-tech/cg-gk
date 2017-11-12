package com.customviews.library.sendemail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

/**
 * Created by rashed on 5/4/16.
 */
public class EmailManager {


    /*
    * Send email through intent
    *
    * single attachment is allowed here
    * */
    public static void sendEmail(Context context, String[] toEmail, String[] ccEmail, String subject, String body, String attachmentNameWithExtension, String attachmentDirectory) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, toEmail);
        email.putExtra(Intent.EXTRA_CC, ccEmail);
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, body);
        email.setType("message/rfc822");
        String targetFilePath = attachmentDirectory + File.separator + attachmentNameWithExtension;
        Uri attachmentUri = Uri.parse(targetFilePath);
        email.putExtra(android.content.Intent.EXTRA_STREAM, Uri.parse("file://" + attachmentUri));
        context.startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }
}
