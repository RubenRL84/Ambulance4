package pt.hoplon.ambulance4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_Device;

public class MainActivity extends AppCompatActivity {

    private D2xxManager mManager;
    private FT_Device mDevice;
    private final Handler mHandler = new Handler();
    private Runnable mRunner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        com.ftdi.j2xx.D2xxManager ftdid2xx = null;
        try {
            ftdid2xx = D2xxManager.getInstance(this);
        } catch (D2xxManager.D2xxException e) {
            e.printStackTrace();
        }

        assert ftdid2xx != null;
        int devCount = ftdid2xx.createDeviceInfoList(this);
        Toast.makeText(this,"Number listed: : "+ ftdid2xx.createDeviceInfoList(this) ,Toast.LENGTH_SHORT).show();

        if(devCount > 0){
            mDevice = ftdid2xx.openByIndex(this, 0);
            if(mDevice.isOpen()) {
                Toast.makeText(this,"Connected",Toast.LENGTH_SHORT).show();
                // reset to UART mode for 232 devices
             //   ftDevice.setBitMode((byte) 0, com.ftdi.j2xx.D2xxManager.FT_BITMODE_RESET);

                // set 230400 baud rate
                mDevice.setBaudRate(115200);

                // set 8 data bits, 1 stop bit, no parity
                mDevice.setDataCharacteristics(com.ftdi.j2xx.D2xxManager.FT_DATA_BITS_8,
                        com.ftdi.j2xx.D2xxManager.FT_STOP_BITS_1, com.ftdi.j2xx.D2xxManager.FT_PARITY_NONE);

                // set RTS/CTS flow control
                mDevice.setFlowControl(com.ftdi.j2xx.D2xxManager.FT_FLOW_NONE, (byte) 0x0b, (byte) 0x0d);

                mRunner = () -> {
                    getData();
                    mHandler.postDelayed(mRunner, 3000);
                };

                mHandler.post(mRunner);


            }


        } else{
            Toast.makeText(this,"No connection",Toast.LENGTH_SHORT).show();
        }


    }

    private void getData() {
        byte[] rx = new byte[64];
        final int[] rx1 = new int[64];
        mDevice.read(rx,64);

        StringBuilder resp = new StringBuilder();
        int i = 0;
        while(i < rx.length){
            rx1[i] = (rx[i] & 0xFF);
            resp.append(rx1[i]).append("-");
            i++;
        }

        Toast.makeText(this,"Data: " + resp,Toast.LENGTH_SHORT).show();

    }
}