package cn.sumile.progress;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActivityChooserView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private TextView tv;
    private ProgressBar progress;
    private int i = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (i > 100) {
                return;
            }
            progress.setProgress(msg.arg1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progress = (ProgressBar) findViewById(R.id.progress);
        new Thread() {
            @Override
            public void run() {
                while (i <= 100) {
                    Message msg = new Message();
                    msg.arg1 = i;
                    mHandler.dispatchMessage(msg);
                    i++;
                    try {
                        sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
