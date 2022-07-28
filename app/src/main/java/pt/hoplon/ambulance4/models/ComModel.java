package pt.hoplon.ambulance4.models;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Observable;
import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_Device;

public class ComModel extends Observable {
    private D2xxManager ftdid2xx;
    private FT_Device mDevice;
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
        Log.i("Number listed: " , String.valueOf(ftdid2xx.createDeviceInfoList(mContext)));
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
            }
        } else{
            Log.i("FTDID2xx: ","No connection");
        }
    }

    public void getData()
    {
        if(mDevice != null)
        {
            if(mDevice.getQueueStatus() > 0)
            {
                Log.i("FT_DEVICE: ", "new array");
                mDevice.read(this.rx,64,0);
                if(this.rx[0] != 1 &&  this.rx[1] != 16 && this.rx[2] != 85 && this.rx[63] != 4)
                {
                    resetBuffer();
                }
            }
        }
        else
        {
            ComConnect();
        }
    }

    private void resetBuffer() {
        if( mDevice != null)
        {
            mDevice.purge((byte) 0x01);
            mDevice.purge((byte) 0x02);
            if(mDevice.getQueueStatus() > 0)
            {
                Log.i("Buffer: ", "Resetting Buffer");
                getData();
            }
        }
    }

    public ArrayList<Double> getBat()
    {
        //calc bat1 and bat 2
        double vol1 = rx[33];
        vol1 = vol1 + (rx[32] * 256);
        vol1 = vol1 * (2.056 / 4096);
        vol1 = vol1 * 10;
        vol1 = vol1 / 1.04;

        double vol2 = rx[35];
        vol2 = vol2 + (rx[34] * 256);
        vol2 = vol2 * (2.056 / 4096);
        vol2 = vol2 * 10;
        vol2 = vol2 / 1.04;
        ArrayList<Double> volts = new ArrayList<>();
        volts.add(vol1);
        volts.add(vol2);
        return(volts);
    }



    public Boolean GetFBit( int Function)
    {
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

    public void SendData(byte cmd, byte subcmd, byte onoff)
    {
        int retornoWrite;
        Log.w("Ambulance", "Enviou os valores "+cmd+ ", "+ subcmd+", "+onoff);
        byte[] msgBuffer = new byte[64];

        msgBuffer[0] = 1;
        msgBuffer[1] = 16;
        msgBuffer[2] = 85;
        msgBuffer[3] = 0;
        msgBuffer[4] = 0;
        msgBuffer[5] = 0;
        msgBuffer[6] = 0;
        msgBuffer[7] = 0;
        msgBuffer[8] = 0;
        msgBuffer[9] = 0;
        msgBuffer[10] = 0;
        msgBuffer[11] = 0;
        msgBuffer[12] = 0;
        msgBuffer[13] = 0;
        msgBuffer[14] = cmd;
        msgBuffer[15] = onoff;
        msgBuffer[16] = subcmd;//funcao
        msgBuffer[17] = (byte)255;
        msgBuffer[18] = (byte)255;
        msgBuffer[19] = (byte)255;
        msgBuffer[20] = (byte)255;
        msgBuffer[21] = (byte)255;
        msgBuffer[22] = (byte)255;
        msgBuffer[23] = (byte)255;
        msgBuffer[24] = (byte)255;
        msgBuffer[25] = (byte)255;
        msgBuffer[26] = (byte)255;
        msgBuffer[27] = (byte)255;
        msgBuffer[28] = (byte)255;
        msgBuffer[29] = (byte)255;
        msgBuffer[30] = (byte)255;
        msgBuffer[31] = (byte)255;
        msgBuffer[32] = (byte)255;
        msgBuffer[33] = (byte)255;
        msgBuffer[34] = (byte)255;
        msgBuffer[35] = (byte)255;
        msgBuffer[36] = (byte)255;
        msgBuffer[37] = (byte)255;
        msgBuffer[38] = (byte)255;
        msgBuffer[39] = (byte)255;
        msgBuffer[40] = (byte)255;
        msgBuffer[41] = (byte)255;
        msgBuffer[42] = (byte)255;
        msgBuffer[43] = (byte)255;
        msgBuffer[44] = (byte)255;
        msgBuffer[45] = (byte)255;
        msgBuffer[46] = (byte)255;
        msgBuffer[47] = (byte)255;
        msgBuffer[48] = (byte)255;
        msgBuffer[49] = (byte)255;
        msgBuffer[50] = (byte)255;
        msgBuffer[51] = (byte)255;
        msgBuffer[52] = (byte)255;
        msgBuffer[53] = (byte)255;
        msgBuffer[54] = (byte)255;
        msgBuffer[55] = (byte)255;
        msgBuffer[56] = (byte)255;
        msgBuffer[57] = (byte)255;
        msgBuffer[58] = (byte)255;
        msgBuffer[59] = (byte)255;
        msgBuffer[60] = (byte)255;
        msgBuffer[61] = (byte)255;
        msgBuffer[62] = 0;
        msgBuffer[63] = 4;

        int chk = 0;
        for (int i = 1; i <= 62; i++)
        {
            chk = chk + msgBuffer[i];
            if (chk > 255) chk = chk - 256;
        }
        msgBuffer[62] = (byte)chk;
        try
        {
            retornoWrite = mDevice.write(msgBuffer);
            if(retornoWrite>0)
            {
                Log.i("SENDDATA: ","OK");
            }
            else
            {
                Log.e("SENDDATA: ","FAILED");
                mDevice = null;
            }
        }
        catch (Exception ex)
        {
            Log.e("SENDDATA: ","FAILED");
        }
    }

    public void runCheck() {
        Handler hand = new Handler();

        Runnable runCheckCom = new Runnable()
        {
            final Handler hand = new Handler();
            @Override
            public void run()
            {
                int devCount;
                com.ftdi.j2xx.D2xxManager ftdid2xx;
                try
                {
                    ftdid2xx = D2xxManager.getInstance(mContext);
                    devCount = ftdid2xx.createDeviceInfoList(mContext);
                    if(!(devCount > 0 ))
                    {
                        mDevice.close();
                        SendData((byte) 2,(byte)0,(byte)4);
                    }
                    else
                    {
                        Toast.makeText(mContext, "board conectada", Toast.LENGTH_SHORT).show();
                        ComConnect();
                    }
                }
                catch(Exception e)
                {
                    //nothing
                }
                hand.postDelayed(this,500);
            }
        };
        hand.post(runCheckCom);

    }

}
