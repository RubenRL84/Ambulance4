package pt.hoplon.ambulance4.models;


import android.widget.ImageView;

public class Tecla {
    public byte Function;
    public ImageView icon;
    public int imgOn;
    public int imgOff;
    public String onOff;
    public String dea;
    public byte state; //indica o estado da tecla 0-off 1-on 2-deactivated
    public byte Tipo; //0 - Standard, 1 - Shortcut, 2 - Setpoint
    public byte Direction; //0 - up, 1 - down
    public int limit;
    public String variable;
    public byte Step;
    public byte JustOff;

    public Tecla(byte function,ImageView icon, int imgOn,int imgOff, String onOff, String dea, byte state, byte tipo, byte direction, int limit, String variable, byte step, byte justOff) {
        Function = function;
        this.icon = icon;
        this.imgOn = imgOn;
        this.imgOff = imgOff;
        this.onOff = onOff;
        this.dea = dea;
        this.state = state;
        Tipo = tipo;
        Direction = direction;
        this.limit = limit;
        this.variable = variable;
        Step = step;
        JustOff = justOff;
    }
}
