package universe.sk.syndriveapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;

public class AlertActivity extends AppCompatActivity {

    private TextView tvTime;
    private FloatingActionButton fabSend, fabDismiss;

    private boolean isDismissed = false;
    private boolean isSent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        tvTime = findViewById(R.id.tvTime);
        fabSend = findViewById(R.id.fabSend);
        fabDismiss = findViewById(R.id.fabDismiss);

        fabDismiss.setEnabled(true);
        fabSend.setEnabled(true);

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
                fabDismiss.setEnabled(false);
                fabSend.setEnabled(false);
            }
        }.start();

        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSent = true;
                tvTime.setText("SOS sent");
                fabSend.setEnabled(false);
                fabDismiss.setEnabled(false);
            }
        }); //end of Send button

        fabDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDismissed = true;
                tvTime.setText("Alert Dismissed");
                fabSend.setEnabled(false);
                fabDismiss.setEnabled(false);
            }
        }); //end of Dismiss button

    }
}
