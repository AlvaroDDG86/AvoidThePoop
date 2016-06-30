package com.example.alvaro.avoidthepoop;

import android.content.Context;
import android.widget.ImageButton;

/**
 * Created by Alvaro on 17/12/2015.
 * Clase que extiende de un ImageButton, al cuál se le han agregado las propiedades de la posición en i, j y su posición absoluta dentro del gridlayout
 */
public class OwnImageButton extends ImageButton {
    private int i;
    private int j;
    private int position;


    public OwnImageButton(Context context,int i,int j,int position) {
        super(context);
        this.i=i;
        this.j=j;
        this.position=position;
    }


    public int getIvalue() {
        return i;
    }

    public int getJvalue() {
        return j;
    }

    public int getPosition() {
        return position;
    }

}
