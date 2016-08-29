package com.example.ethan.share01;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Lai.OH on 2016-08-29.
 */
public class MessageDialogUtil extends Dialog{
    private Context mContext;
    private String mRecvName;
    private String mRecvSex;
    private String mRecvMsg;
    private int mRecvId;

    private RbPreference mPref;

    private final String SERVER_URL_SEND = "https://toycom96.iptime.org:1443/chat_send";

    private TextView dialog_title_tv;
    private TextView dialog_recvmsg_tv;
    private EditText dialog_message_edt;
    private Button dialog_send_btn;
    private Button dialog_cancel_btn;

    public MessageDialogUtil(Context context, String recvName, String recvSex, String recvMsg, int recvId) {
        super(context);
        this.mContext = context;
        this.mRecvName = recvName;
        this.mRecvSex = recvSex;
        this.mRecvMsg = recvMsg;
        this.mRecvId = recvId;

        mPref = new RbPreference(mContext);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_message_dialog);

        dialog_title_tv = (TextView) findViewById(R.id.dialog_title);
        dialog_recvmsg_tv = (TextView) findViewById(R.id.dialog_recvmsg);
        dialog_message_edt = (EditText) findViewById(R.id.dialog_message);
        dialog_send_btn = (Button) findViewById(R.id.dialog_send);
        dialog_cancel_btn = (Button) findViewById(R.id.dialog_cancel);

        dialog_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageSendUtil sendMsgTask = new MessageSendUtil(mContext);
                sendMsgTask.execute(SERVER_URL_SEND, String.valueOf(mRecvId), dialog_message_edt.getText().toString(), mPref.getValue("auth", ""));
                dismiss();
            }
        });

        dialog_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "보내기", Toast.LENGTH_SHORT);
                dismiss();
            }
        });

    }

    public void setTitle() {
        dialog_title_tv.setText(mRecvName + "(" + mRecvSex + ")");
        dialog_recvmsg_tv.setText( mRecvMsg);
    }

}
