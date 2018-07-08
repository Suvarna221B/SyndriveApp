package universe.sk.syndriveapp;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AlertActivity extends AppCompatActivity {

    private TextView tvTime;
    private Button btnSend, btnDismiss;

    private boolean isDismissed = false;
    private boolean isSent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        tvTime = findViewById(R.id.tvTime);
        btnSend = findViewById(R.id.btnSend);
        btnDismiss = findViewById(R.id.btnDismiss);

        btnDismiss.setEnabled(true);
        btnSend.setEnabled(true);

        long millisInFuture = 25000; //15s
        long countDownInterval = 1000; //1s
        new CountDownTimer(millisInFuture, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (isDismissed || isSent) cancel();
                else {
                    tvTime.setText("" + millisUntilFinished / 1000);
                }
            }

            @Override
            public void onFinish() {
                tvTime.setText("SOS Sent");
                btnDismiss.setEnabled(false);
                btnSend.setEnabled(false);
            }
        }.start();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSent = true;
                tvTime.setText("SOS sent");
                btnSend.setEnabled(false);
                btnDismiss.setEnabled(false);
            }
        }); //end of Send button

        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDismissed = true;
                tvTime.setText("Alert Dismissed");
                btnSend.setEnabled(false);
                btnDismiss.setEnabled(false);
            }
        }); //end of Dismiss button

    }
}
