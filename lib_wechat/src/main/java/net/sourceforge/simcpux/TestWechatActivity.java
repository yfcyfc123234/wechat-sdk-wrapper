package net.sourceforge.simcpux;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.JumpToOfflinePay;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class TestWechatActivity extends Activity {
    private Button gotoBtn, regBtn, launchBtn, scanBtn, subscribeMsgBtn, subscribeMiniProgramMsgBtn;
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        checkPermission();
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);

        regBtn = findViewById(R.id.reg_btn);
        regBtn.setOnClickListener(v -> api.registerApp(Constants.APP_ID));

        gotoBtn = findViewById(R.id.goto_send_btn);
        gotoBtn.setOnClickListener(v -> {
            startActivity(new Intent(TestWechatActivity.this, SendToWXActivity.class));
//		        finish();
        });

        launchBtn = findViewById(R.id.launch_wx_btn);
        launchBtn.setOnClickListener(v -> Toast.makeText(TestWechatActivity.this, "launch result = " + api.openWXApp(), Toast.LENGTH_LONG).show());

        subscribeMsgBtn = findViewById(R.id.goto_subscribe_message_btn);
        subscribeMsgBtn.setOnClickListener(v -> {
            startActivity(new Intent(TestWechatActivity.this, SubscribeMessageActivity.class));
//				finish();
        });

        subscribeMiniProgramMsgBtn = findViewById(R.id.goto_subscribe_mini_program_msg_btn);
        subscribeMiniProgramMsgBtn.setOnClickListener(v -> startActivity(new Intent(TestWechatActivity.this, SubscribeMiniProgramMsgActivity.class)));

        View jumpToOfflinePay = findViewById(R.id.jump_to_offline_pay);
        jumpToOfflinePay.setOnClickListener(v -> {
            int wxSdkVersion = api.getWXAppSupportAPI();
            if (wxSdkVersion >= Build.OFFLINE_PAY_SDK_INT) {
                api.sendReq(new JumpToOfflinePay.Req());
            } else {
                Toast.makeText(TestWechatActivity.this, "not supported", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.PERMISSIONS_REQUEST_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.PERMISSIONS_REQUEST_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(TestWechatActivity.this, "Please give me storage permission!", Toast.LENGTH_LONG).show();
            }
        }
    }
}