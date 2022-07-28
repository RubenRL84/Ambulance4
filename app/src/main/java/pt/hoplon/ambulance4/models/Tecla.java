package pt.hoplon.ambulance4.models;
import android.widget.ImageView;

public class Tecla {
    public final byte Function;
    public final ImageView icon;
    public final int imgOn;
    public final int imgOff;
    public final String onOff;
    public final String dea;
    public byte state; //indica o estado da tecla 0-off 1-on 2-deactivated
    public final byte Tipo; //0 - Standard, 1 - Shortcut, 2 - Setpoint
    public final byte Direction; //0 - up, 1 - down
    public final int limit;
    public final String variable;
    public final byte Step;
    public final byte JustOff;

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
