package pt.hoplon.ambulance4;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;


import pt.hoplon.ambulance4.models.ComModel;

public class MainActivity extends AppCompatActivity {
    private final Handler mHandler = new Handler();
    private Runnable mRunner;
    private SeekBar sBar;
    private TextView acValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        sBar = findViewById(R.id.seekBar);
        acValue = findViewById(R.id.acValue);

        // variable to hold context
        Context context = getApplicationContext();

        ComModel comTest = new ComModel(context);

        comTest.ComConnect();

        sBar = (SeekBar) findViewById(R.id.seekBar);
        acValue = (TextView) findViewById(R.id.acValue);
        acValue.setText(sBar.getProgress()+"º" );
        System.out.println(sBar.getProgress());
        sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int pval = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pval = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //write custom code to on start progress
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                acValue.setText(pval+"º" );
            }
        });



        mRunner = () -> {
            System.out.println(comTest.GetFBit((byte)14));
            mHandler.postDelayed(mRunner, 3000);
        };
        mHandler.post(mRunner);
    }





    }


