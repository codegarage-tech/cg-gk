package com.customviews.library.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.customviews.library.R;

public class DialogMessage extends Dialog {

    private Activity context_;
    private String messageTitle = "", messageText = "";
    private boolean closeActivity = false;
    private Button btnOk, btnClose;
    private TextView tvMessageTitle, tvMessageText;

    public DialogMessage(Activity context, String messageTitle,
                         String messageText, boolean closeActivity) {
        super(context);
        this.context_ = context;
        this.messageTitle = messageTitle;
        this.messageText = messageText;
        this.closeActivity = closeActivity;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.dialog_message);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnClose = (Button) findViewById(R.id.btn_close);
        tvMessageTitle = (TextView) findViewById(R.id.message_title);
        tvMessageText = (TextView) findViewById(R.id.message_txt);

        tvMessageTitle.setText(messageTitle);
        tvMessageText.setText(messageText);

        setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub

                dialog.dismiss();
                if (closeActivity) {
                    context_.finish();
                    //context_.overridePendingTransition(R.anim.trans_left_in,
                    //R.anim.trans_left_out);
                }

            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dismiss();
                if (closeActivity) {
                    context_.finish();
                    //context_.overridePendingTransition(R.anim.trans_left_in,
                    //R.anim.trans_left_out);
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                if (closeActivity) {
                    context_.finish();
                    //context_.overridePendingTransition(R.anim.trans_left_in,
                    //R.anim.trans_left_out);
                }
            }
        });

    }

}
