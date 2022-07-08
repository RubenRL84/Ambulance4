package pt.hoplon.ambulance4.models;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import java.util.Observable;
import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_Device;



public class ComModel extends Observable {
    private D2xxManager ftdid2xx;
    private FT_Device mDevice;
    private final Handler mHandler = new Handler();
    private Runnable mRunner;
    private final Context mContext;
    private final int devCount;
    private final byte[] rx = new byte[64];

    public ComModel(Context mContext) {
        this.mContext = mContext;
        this.ftdid2xx = null;
        try {
            ftdid2xx = D2xxManager.getInstance(mContext);
        } catch (D2xxManager.D2xxException e) {
            e.printStackTrace();
        }
        assert ftdid2xx != null;
        this.devCount = ftdid2xx.createDeviceInfoList(mContext);
        Toast.makeText(mContext,"Number listed: : "+ ftdid2xx.createDeviceInfoList(mContext) ,Toast.LENGTH_SHORT).show();
    }

    public void ComConnect(){

        if(this.devCount > 0){
            mDevice = this.ftdid2xx.openByIndex(mContext, 0);
            if(mDevice.isOpen()) {
                Toast.makeText(mContext,"Connected",Toast.LENGTH_SHORT).show();
                // reset to UART mode for 232 devices
                mDevice.setBitMode((byte) 0, (byte)0x00);
                // set 230400 baud rate
                mDevice.setBaudRate(115200);
                // set 8 data bits, 1 stop bit, no parity
                mDevice.setDataCharacteristics((byte)8,
                        (byte)0, (byte)0);
                // set RTS/CTS flow control
                mDevice.setFlowControl((short)0x0000, (byte) 0x0b, (byte) 0x0d);

                mRunner = () -> {
                    getData();
                    mHandler.postDelayed(mRunner, 2000);
                };
                mHandler.post(mRunner);
            }
        } else{
            Toast.makeText(mContext,"No connection",Toast.LENGTH_SHORT).show();
        }
    }

    private void getData()
    {
        final int[] rx1 = new int[1024];
        mDevice.read(this.rx,64,0);

        StringBuilder resp = new StringBuilder();
        int i = 0;
        while(i < this.rx.length){

            rx1[i] = (this.rx[i] & 0xFF);
            resp.append(rx1[i]).append("-");
            i++;
        }
        resp = new StringBuilder(resp.substring(0, resp.length() - 1));




      // Toast.makeText(mContext,"Data: " + resp,Toast.LENGTH_SHORT).show();

    }
    public Boolean GetFBit( int Function)
    {

        if(this.rx[0] == 1 &&  this.rx[1] == 16 && this.rx[2] == 85 && this.rx[63] == 4)
        {
            System.out.println("Array ok");
        }
        int chk = 0;
        for (int j = 1; j <= 61; j++)
        {
            chk += (this.rx[j] & 0xFF);
            if (chk > 255)
                chk = chk - 256;
        }
        if((this.rx[62] & 0xFF) == chk)
        {
            System.out.println("checksum ok");
        }

        int [] rx1 = new int[64];
        for(int j = 0; j < this.rx.length;j++)
        {
            rx1[j] = (Byte.toUnsignedInt(this.rx[j]));
            System.out.print(( rx1[j] & 0xFF)+"-");
        }
        System.out.println(" ");

        int B;
        Function--;

        if (Function < 8)
        {
            B = 0;
        }
        else
        {
            B = Function / 8;
        }
        int b = Function - (8 * B);
        return ( this.rx[19 - B]  & (1 << b)) != 0;
    }
}
