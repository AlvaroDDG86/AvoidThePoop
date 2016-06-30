package com.example.alvaro.avoidthepoop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.Button;

import java.util.Set;

public class Main extends AppCompatActivity implements SelectGameFragment.OnFragmentInteractionListener,InfoFragment.OnFragmentInteractionListener{
    //Variables para los botones de la pantalla principal
    Button buttonPlay;
    Button buttonInfo;
    Button buttonSonido;
    Button buttonExit;
    //Variable para la música
    MediaPlayer player,clickSound;
    //variables boolean para los estados de la música y los efectos sonoros del botón
    boolean music;
    private Typeface face;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //OBjeto para las preferencias
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        music = true;
        music = sharedPref.getBoolean("music", true);
        player= MediaPlayer.create(Main.this, R.raw.music);
        clickSound = MediaPlayer.create(Main.this,R.raw.button);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        double textHeight2 = size.y*0.01;

        face=Typeface.createFromAsset(getAssets(),"Sketch 3D.otf");

        buttonPlay =(Button)findViewById(R.id.buttonPlay);
        buttonPlay.setTypeface(face);
        buttonPlay.setTextSize((float)textHeight2);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(music){
                    clickSound.start();
                }
                SelectGameFragment selectGameFragment = new SelectGameFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.animator.pop_enter, R.animator.pop_exit).add(R.id.main,selectGameFragment).commit();

            }
        });

        buttonInfo =(Button)findViewById(R.id.buttonInfo);
        buttonInfo.setTypeface(face);
        buttonInfo.setTextSize((float)textHeight2);
        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(music){
                    clickSound.start();
                }
                InfoFragment infoFragment = new InfoFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.animator.pop_enter, R.animator.pop_exit).add(R.id.main,infoFragment).commit();
            }
        });

        buttonSonido =(Button)findViewById(R.id.buttonSonido);
        buttonSonido.setTypeface(face);
        buttonSonido.setTextSize((float)textHeight2);
        buttonSonido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(music){
                    clickSound.start();
                }
                music = !music;
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putBoolean("music", music);
                editor.commit();
                if(music){
                    buttonSonido.setTextColor(Color.BLUE);
                    player.start();
                }else{
                    buttonSonido.setTextColor(Color.GRAY);
                    player.pause();
                }
            }
        });

        buttonExit =(Button)findViewById(R.id.buttonExit);
        buttonExit.setTypeface(face);
        buttonExit.setTextSize((float)textHeight2);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(music){
                    clickSound.start();
                }
                player.stop();
                finish();
            }
        });

        if(music){
            player.start();
            player.setLooping(true);
        }else{
            buttonSonido.setTextColor(Color.GRAY);
        }
    }

    //Método sobreescrito para controlar el boton Back del dispositivo
    @Override
    public void onBackPressed()
    {
        player.stop();
        super.onBackPressed();  // optional depending on your needs
    }



    @Override
    protected void onStop() {
        player.pause();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putBoolean("music", music);
        editor.commit();
        super.onStop();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }





}
