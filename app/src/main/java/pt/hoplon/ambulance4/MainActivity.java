package pt.hoplon.ambulance4;


import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.ArrayList;
import pt.hoplon.ambulance4.models.ComModel;
import pt.hoplon.ambulance4.models.Tecla;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final Handler mHandler = new Handler();
    private Runnable mRunner;
    private SeekBar sBar;
    private  ImageView bat1Bar,bat2Bar;
    private TextView acValue,bat1,bat2;
    Tecla estribo,luzPenumbra,luzPenumbraDir,luzPenumbraEsq,coluna,ventilador,extrator;
    ComModel comTest;
    ArrayList<Tecla> teclas;
    private int pval = 0;

    @SuppressLint("SetTextI18n")
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

        comTest = new ComModel(context);
        comTest.ComConnect();

        bat1 = findViewById(R.id.bat1);
        bat2 = findViewById(R.id.bat2);
        bat1Bar = findViewById(R.id.bat1Bar);
        bat2Bar = findViewById(R.id.bat2Bar);

        ImageView ac_minus = findViewById(R.id.ac_minus);
        ac_minus.setOnClickListener(this);
        ImageView ac_plus = findViewById(R.id.ac_plus);
        ac_plus.setOnClickListener(this);

        ImageView estriboIcon = findViewById(R.id.estribo);
        estriboIcon.setOnClickListener(this);
        ImageView luz_int_esqIcon = findViewById(R.id.luz_int_esq);
        luz_int_esqIcon.setOnClickListener(this);
        ImageView luz_int_penunbraIcon = findViewById(R.id.luz_int_penunbra);
        luz_int_penunbraIcon.setOnClickListener(this);
        ImageView luz_int_dirIcon = findViewById(R.id.luz_int_dir);
        luz_int_dirIcon.setOnClickListener(this);
        ImageView colunaIcon = findViewById(R.id.coluna);
        colunaIcon.setOnClickListener(this);
        ImageView ventiladorIcon = findViewById(R.id.ventilador);
        ventiladorIcon.setOnClickListener(this);
        ImageView extratorIcon = findViewById(R.id.extrator);
        extratorIcon.setOnClickListener(this);

        teclas = new ArrayList<>();

        estribo = new Tecla((byte)30,estriboIcon,R.drawable.estribo_on,R.drawable.estribo_off,"Off","off",(byte)0,(byte)0,(byte)0,(byte)1,"oi",(byte)1,(byte)0);
        teclas.add(estribo);
        estriboIcon.setOnClickListener(this);
        luzPenumbra = new Tecla((byte)20,luz_int_penunbraIcon,R.drawable.luz_penumbra_on,R.drawable.penumbra_off,"Off","off",(byte)0,(byte)0,(byte)0,(byte)1,"oi",(byte)1,(byte)0);
        teclas.add(luzPenumbra);
        luzPenumbraDir = new Tecla((byte)22,luz_int_dirIcon,R.drawable.luz_direita_on,R.drawable.luz_direita_off,"Off","off",(byte)0,(byte)0,(byte)0,(byte)1,"oi",(byte)1,(byte)0);
        teclas.add(luzPenumbraDir);
        luzPenumbraEsq = new Tecla((byte)21,luz_int_esqIcon,R.drawable.luz_esquerda_on,R.drawable.luz_esquerda_off,"Off","off",(byte)0,(byte)0,(byte)0,(byte)1,"oi",(byte)1,(byte)0);
        teclas.add(luzPenumbraEsq);
        coluna = new Tecla((byte)20,colunaIcon,R.drawable.coluna_on,R.drawable.coluna_off,"Off","off",(byte)0,(byte)0,(byte)0,(byte)1,"oi",(byte)1,(byte)0);
        teclas.add(coluna);
        ventilador = new Tecla((byte)1,ventiladorIcon,R.drawable.ventilador_on,R.drawable.ventilador_off,"Off","off",(byte)0,(byte)0,(byte)0,(byte)1,"oi",(byte)1,(byte)0);
        teclas.add(ventilador);
        extrator = new Tecla((byte)20,extratorIcon,R.drawable.extrator_on,R.drawable.extrator_off,"Off","off",(byte)0,(byte)0,(byte)0,(byte)1,"oi",(byte)1,(byte)0);
        teclas.add(extrator);

        sBar = findViewById(R.id.seekBar);
        acValue = findViewById(R.id.acValue);
        acValue.setText(sBar.getProgress()+"º" );
        System.out.println(sBar.getProgress());
        sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //pval = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                 pval = progress  ;
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

    mRunner = () -> runOnUiThread((this::checkTeclas));
        mHandler.post(mRunner);

        comTest.runCheck();
    }
    private void changeTecla(Tecla T){
        for(int i =0; i < teclas.size();i++){
            if(T.icon == teclas.get(i).icon){
                switch (T.state){
                    case 0:
                        int finalI = i;
                        runOnUiThread((() -> T.icon.setImageResource(teclas.get(finalI).imgOff)));
                        break;
                    case 1:
                        int finalI1 = i;
                        runOnUiThread((() -> T.icon.setImageResource(teclas.get(finalI1).imgOn)));

                        break;
                }
            }
        }

    }



    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void checkTeclas() {
        mRunner = () -> {
            comTest.getData();
            try{

                for(int i =0; i < teclas.size();i++){
                    if(comTest.GetFBit(teclas.get(i).Function )){
                        int finalI = i;
                        runOnUiThread((() -> {

                            teclas.get(finalI).icon.setImageResource(teclas.get(finalI).imgOn);
                            teclas.get(finalI).state = 1;
                        }));



                    }else{
                        int finalI1 = i;
                        runOnUiThread((() -> {
                            teclas.get(finalI1).icon.setImageResource(teclas.get(finalI1).imgOff);
                            teclas.get(finalI1).state = 0;
                        }));

                    }

                }
                double value1 = comTest.getBat().get(0);
                double value2  = comTest.getBat().get(1);
                bat1.setText(String.format("%.1f", comTest.getBat().get(0))+" V");
                if(value1 >= 12 && value1 <= 40){
                    bat1Bar.setImageResource(R.drawable.bat1_12);
                }else if(value1 >= 11 && value1 < 12){
                    bat1Bar.setImageResource(R.drawable.bat1_11_12);
                }else if(value1 >= 10 && value1 < 11){
                    bat1Bar.setImageResource(R.drawable.bat1_10_11);
                }else if(value1 < 10  && value1 > 1){
                    bat1Bar.setImageResource(R.drawable.bat1_10);
                }else{
                    bat1Bar.setImageResource(R.drawable.bat_1_empty);
                }
                bat2.setText(String.format("%.1f", comTest.getBat().get(1))+" V");
                if(value2 >= 12 && value2 <= 40){
                    bat2Bar.setImageResource(R.drawable.bat1_12);
                }else if(value2 >= 11 && value2 < 12){
                    bat2Bar.setImageResource(R.drawable.bat1_11_12);
                }else if(value2 >= 10 && value2 < 11){
                    bat2Bar.setImageResource(R.drawable.bat1_10_11);
                }else if(value2 < 10  && value2 > 1){
                    bat2Bar.setImageResource(R.drawable.bat1_10);
                }else{
                    bat2Bar.setImageResource(R.drawable.bat_1_empty);
                }
                mHandler.postDelayed(mRunner,500 );
            }catch (Exception e){
                Log.e("Error: ", String.valueOf(e));
            }

        };
        mHandler.post(mRunner);
    }



    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public void onClick(View view) {

                        ImageView b = (ImageView) view;
                        switch (b.getId()){
                            case R.id.estribo:
                                comTest.SendData((byte)1,estribo.Function,getState(estribo.state));
                                estribo.state = getState(estribo.state);
                                changeTecla(estribo);
                                break;
                            case R.id.luz_int_esq:
                                comTest.SendData((byte)1,luzPenumbraEsq.Function,getState(luzPenumbraEsq.state));
                                luzPenumbraEsq.state = getState(luzPenumbraEsq.state);
                                changeTecla(luzPenumbraEsq);
                                break;
                            case R.id.luz_int_penunbra:
                                comTest.SendData((byte)1,luzPenumbra.Function,getState(luzPenumbra.state));
                                luzPenumbra.state = getState(luzPenumbra.state);
                                changeTecla(luzPenumbra);
                                break;
                            case R.id.luz_int_dir:
                                comTest.SendData((byte)1,luzPenumbraDir.Function,getState(luzPenumbraDir.state));
                                luzPenumbraDir.state = getState(luzPenumbraDir.state);
                                changeTecla(luzPenumbraDir);
                                break;
                            case R.id.coluna:
                                comTest.SendData((byte)1,coluna.Function,getState(coluna.state));
                                coluna.state = getState(coluna.state);
                                changeTecla(coluna);
                                break;
                            case R.id.ventilador:
                                comTest.SendData((byte)1,ventilador.Function,getState(ventilador.state));
                                ventilador.state = getState(ventilador.state);
                                changeTecla(ventilador);
                                break;
                            case R.id.extrator:
                                comTest.SendData((byte)1,extrator.Function,getState(extrator.state));
                                extrator.state = getState(extrator.state);
                                changeTecla(extrator);
                                break;
                            case R.id.ac_minus:
                                    sBar.setProgress(sBar.getProgress() - 1,true);
                                    acValue.setText(sBar.getProgress()+"º");

                                break;
                            case R.id.ac_plus:
                                if( sBar.getProgress() <= sBar.getMax() ){
                                    sBar.setProgress(sBar.getProgress() + 1,true);
                                    acValue.setText(sBar.getProgress()+"º");
                                }


                                break;
                        }
                    }

    private byte getState(byte state) {
        if( state == 1) {
            state = 0;
        }else{
            state = 1;
        }
        return state;

    }


}



