package duviwin.compudocapp;

import android.graphics.Color;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Duviwin on 6/25/2015.
 */
public class CSSData {
    public static final HashMap<String,String> KLEUR_MAP=new HashMap<>();
    static{
        KLEUR_MAP.put("5","#ff6600");
        KLEUR_MAP.put("4","#ff9900");
        KLEUR_MAP.put("3","#ffcc00");
        KLEUR_MAP.put("2","#b3bcc1");
        KLEUR_MAP.put("","#00ffffff");
        KLEUR_MAP.put("1",KLEUR_MAP.get(""));
        KLEUR_MAP.put("0",KLEUR_MAP.get(""));

        KLEUR_MAP.put("_zelfst","#66cccc");
        KLEUR_MAP.put("_repost","#b3bcc1");
        KLEUR_MAP.put("_tripost","#ffcc00");
        KLEUR_MAP.put("_quatropost","#ff9900");
        KLEUR_MAP.put("_quintopost","#ff6600");
        KLEUR_MAP.put("_spoed","#FFFF66");
        KLEUR_MAP.put("lid","#8EAFDD");
        KLEUR_MAP.put("geannuleerd","#ff0000");
        KLEUR_MAP.put("Loading","#ffffff");


    }
    public static int getKleur(String tag){
        Log.d("CSSData", tag);
        return Color.parseColor(KLEUR_MAP.get(tag));
    }
}
