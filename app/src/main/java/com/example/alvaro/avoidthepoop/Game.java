package com.example.alvaro.avoidthepoop;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Collections;
import java.util.Random;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Iterator;

public class Game extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener, FinishFragment.OnFragmentInteractionListener {
    //Variables para los botones de la toolbar
    ImageView imageViewRefresh;
    ImageView imageViewResume;
    ImageView imageViewPause;
    ImageView imageViewExit;
    //Imagen que aparecerá en la pausa del juego
    ImageView pause;
    //El gridLayout que se generará
    GridLayout gridLayout;
    //El texto que contendrá el contador
    TextView textViewTime;
    //El número de minas que tiene que buscar el usuario
    int numPoops=10;
    //Variable que contiene el valor a poner en el texto de contador de minas
    int finds;
    //Variable que contendra el número total de casillas en el tablero
    int elements;
    //Variable que contiene el tiempo en segundos
    int count=0;
    //El nivel del juego
    int level;
    //El color de la mina, siendo 1 o 2 para marrón y rosa
    int poop;
    //dependiendo de estas variables habrá música o no
    boolean music;
    //Texto que mostrará las minas que tiene que encontrar aun el usuario
    TextView textViewFinds;
    //Timer para el contador
    Timer T;
    //Array que contiene las imagenes
    private Integer[] images ={R.drawable.bush,R.drawable.bush_two,R.drawable.tree,R.drawable.grass_three,R.drawable.grass,R.drawable.grass_four,R.drawable.grass_five};
    //Tablero que contendrá el juego
    int[][] game;
    int[][] showed;
    //MediaPlayer para el sonido principal de la aplicación
    public static MediaPlayer player;
    //MediaPlayer para el sonido de los botones
    public static MediaPlayer clickSound;
    //Vibrator para hacer que vibre cuando se programe
    public static Vibrator vibrator;
    //creamos un nuevo objeto de SharedPreferences, pero esta vez para contener las variables con los mejores tiempos
    SharedPreferences prefs;
    //Con esta variable, editaremos las preferencias si batimos un nuevo record
    SharedPreferences.Editor editor;
    int[] bestScores=new int[3];
    //estas variables contendrán las primeras posiciones de cada nivel
    String first,second,thrid;
    // Si se bate un nuevo record, esta variable se enviará a la actividad finish para mostrar la medalla de la posicion obtenida
    String position;
    Bundle bundle;
    Typeface face;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        bundle = new Bundle();

        face= Typeface.createFromAsset(getAssets(),"Sketch 3D.otf");

        vibrator = (Vibrator) Game.this.getSystemService(getApplicationContext().VIBRATOR_SERVICE);
        //Inicializaciónes
        gridLayout=(GridLayout)findViewById(R.id.gridLayoutGame);
        textViewFinds=(TextView)findViewById(R.id.textViewFinds);
        pause = (ImageView)findViewById(R.id.imagePause);
        textViewTime=(TextView)findViewById(R.id.textViewTime);
        textViewFinds.setTypeface(face);
        textViewTime.setTypeface(face);

        //ocultar la imagen de pause
        pause.setVisibility(View.INVISIBLE);

        //Tratamiento para la toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Inicializar las preferencias
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        level = getIntent().getExtras().getInt("level", 8);
        player= MediaPlayer.create(Game.this, R.raw.game);
        clickSound = MediaPlayer.create(Game.this, R.raw.button);

        music = sharedPref.getBoolean("music", true);
        if(music){
            player.start();
            player.setLooping(true);
        }

        prefs = this.getSharedPreferences("score", Context.MODE_PRIVATE);
        editor = prefs.edit();

        //Creación del tablero dependiendo del nivel, y asignando el valor de la clave para saber las mejores marcas de cada nivel
        elements= level*level;
            switch(elements){
                case 64:
                    gridLayout.setRowCount(level);
                    gridLayout.setColumnCount(level);
                    numPoops=5;
                    first="firstEasy";
                    second="secondEasy";
                    thrid="thridEasy";
                    break;
                case 100:
                    gridLayout.setRowCount(level);
                    gridLayout.setColumnCount(level);
                    numPoops=10;
                    first="firstMed";
                    second="secondMed";
                    thrid="thridMed";
                    break;
                case 196:
                    gridLayout.setRowCount(level);
                    gridLayout.setColumnCount(level);
                    numPoops=20;
                    first="firstHard";
                    second="secondHard";
                    thrid="thridHard";
            }
        //Ponemos el texto con el número de minas a buscar
        finds=numPoops;
        textViewFinds.setText(String.valueOf(finds));

        //Inicializamos las arrays bidimensionales que contendrán el juego
        game=new int[level][level];
        showed=new int[level][level];

        //Inicialización y codificación de los botones de la toolbar
        imageViewRefresh=(ImageView)findViewById(R.id.imageViewRefresh);
        imageViewRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(music){
                    clickSound.start();
                }
                //Llamada al método que reinicia la partida
                reloadGame();
            }
        });
        imageViewResume=(ImageView)findViewById(R.id.imageViewResume);
        imageViewResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(music){
                    clickSound.start();
                }
                player.start();
                gridLayout.setVisibility(View.VISIBLE);
                pause.setVisibility(View.INVISIBLE);
                textViewFinds.setText(String.valueOf(finds));
                contador();
            }
        });
        imageViewPause=(ImageView)findViewById(R.id.imageViewPause);
        imageViewPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(music){
                    clickSound.start();
                }
                textViewFinds.setText("");
                gridLayout.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);
                player.pause();
                imageViewResume.setEnabled(true);
                T.cancel();
            }
        });
        imageViewExit=(ImageView)findViewById(R.id.imageViewExit);
        imageViewExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(music){
                    clickSound.start();
                }
                //Llamada al método de salida
                exit();
            }
        });
        imageViewExit.setEnabled(true);
        imageViewRefresh.setEnabled(true);
        imageViewResume.setEnabled(true);
        imageViewPause.setEnabled(true);
        contador();
        createArrayGame(); //Crear el array de ceros y fijar las minas
    }

    //Rellenamos todas las casillas con un 0
    private void createArrayGame(){
        for (int i=0;i<this.game.length;i++){
            for (int j=0;j<this.game[i].length;j++){
                this.game[i][j] = 0;
            }
        }
        //llamada al método para fijar las minas
        setMines();
    }

    //Método que coloca las minas aleatoriamente
    private void setMines(){
        int randomI,randomJ;
        Random r = new Random();
        for (int i = 0; i < numPoops; i++) {
            randomI = r.nextInt(this.game.length);
            randomJ = r.nextInt(this.game[0].length);
            if(this.game[randomI][randomJ] == -1){
                i--;
            }else {
                this.game[randomI][randomJ] = -1;
            }
        }
        //llamada al método para saber las minas que hay al rededor de cada casilla
        setInfo();
    }

    //Método que pone un valor numérico dependiendo de las minas que tenga a su alrededor
    public void setInfo() {
        int cont;
        for (int i = 0; i < game.length; i++) {
            for (int j = 0; j < game[i].length; j++) {
                cont = 0;
                if (game[i][j] != -1) {
                    if (valido(i + 1, j) && game[i + 1][j] == -1) cont++;
                    if (valido(i - 1, j) && game[i - 1][j] == -1) cont++;
                    if (valido(i, j + 1) && game[i][j + 1] == -1) cont++;
                    if (valido(i, j - 1) && game[i][j - 1] == -1) cont++;
                    if (valido(i - 1, j - 1) && game[i - 1][j - 1] == -1) cont++;
                    if (valido(i - 1, j + 1) && game[i - 1][j + 1] == -1) cont++;
                    if (valido(i + 1, j - 1) && game[i + 1][j - 1] == -1) cont++;
                    if (valido(i + 1, j + 1) && game[i + 1][j + 1] == -1) cont++;
                    game[i][j] = cont;
                }
            }
        }
        createGame(); //Crear el tablero que vera el usuario
    }

    //Método que crea el table que verá el usuario
    public void createGame() {
        int position=0;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width;
        int height;
        width=(size.x)/(level+2);
        height=(size.y)/(level+2);
        ArrayList<Integer> imagenes=createImages();
        for (int i=0;i<this.game.length;i++){
            for(int j=0;j<this.game[i].length;j++){
                OwnImageButton b = new OwnImageButton(this,i,j,position);
                if(this.game[i][j] == -1)b.setTag("poop");
                b.setImageResource(imagenes.get(position));
                b.setMinimumWidth(width);
                b.setMinimumHeight(height);
                b.setMaxWidth(width);
                b.setMaxHeight(height);
                b.setAdjustViewBounds(true);
                b.setPadding(0, 0, 0, 0);
                b.setOnClickListener(this);
                b.setOnLongClickListener(this);
                gridLayout.addView(b);
                ++position;
            }
        }
    }

    //Método para poner una imagen aleatoria a cada casilla
    public ArrayList<Integer> createImages(){
        ArrayList<Integer> aImages=new ArrayList<>();
        int imgImagen;
        for(int i=0;i<(elements);i++) {
            imgImagen = 0 + (int) (Math.random() * images.length);
            aImages.add(images[imgImagen]);
        }
        Collections.shuffle(aImages);
        return aImages;
    }


    @Override
    public boolean onLongClick(View v) {
        ImageButton imageLongClicked=(ImageButton)v;
        if(imageLongClicked.getTag()!=null) {
            vibrator.vibrate(300);
            imageLongClicked.setImageResource(R.drawable.dog);
            --finds;
            textViewFinds.setText(String.valueOf(finds));
            imageLongClicked.setEnabled(false);
            if(finds==0){
                String condition="win";
                position="noRecord";
                    if(count<prefs.getInt(first, 10000))
                    {
                        editor.putInt(first, count);
                        editor.putInt(second, prefs.getInt(first, 10000));
                        editor.putInt(thrid, prefs.getInt(second, 10000));
                        editor.commit();
                        position="first";
                    }else if(count<prefs.getInt(second, 10000))
                    {
                        editor.putInt(second, count);
                        editor.putInt(thrid, prefs.getInt(second, 10000));
                        editor.commit();
                        position="second";
                    }else if(count<prefs.getInt(thrid, 10000))
                    {
                        editor.putInt(thrid, count);
                        editor.commit();
                        position="thrid";
                    }
                finishGame(condition);
            }
        }else{
            String condition="error";
            if(music) {
                MediaPlayer fart = MediaPlayer.create(Game.this, R.raw.fart);
                fart.start();
            }
            finishGame(condition);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        OwnImageButton imagenClicked=(OwnImageButton)v;
        if(imagenClicked.getTag()!=null) {
            String condition="lose";
            vibrator.vibrate(1000);
            if(music) {
                MediaPlayer fart = MediaPlayer.create(Game.this, R.raw.fart);
                fart.start();
            }
            finishGame(condition);
        }else{
            if(music){
                clickSound.start();
            }
            int Ivalue=imagenClicked.getIvalue();
            int Jvalue=imagenClicked.getJvalue();
            int position=imagenClicked.getPosition();
            switch (game[Ivalue][Jvalue]){
                case 0:
                    vecino(Ivalue, Jvalue, position);
                    break;
                case 1:
                    imagenClicked.setImageResource(R.drawable.one);
                    break;
                case 2:
                    imagenClicked.setImageResource(R.drawable.two);
                    break;
                case 3:
                    imagenClicked.setImageResource(R.drawable.three);
                    break;
                case 4:
                    imagenClicked.setImageResource(R.drawable.four);
                    break;
                case 5:
                    imagenClicked.setImageResource(R.drawable.five);
                    break;
                case 6:
                    imagenClicked.setImageResource(R.drawable.six);
                    break;
                case 7:
                    imagenClicked.setImageResource(R.drawable.seven);
                    break;
                case 8:
                    imagenClicked.setImageResource(R.drawable.eigth);
            }
            imagenClicked.setEnabled(false);
        }
    }

    //Método que lanza una nueva actividad informando al usuario, si ha ganado, perdido o se ha confuncido, como parametro se pasa la condicion por la que se lanza
    //la activadad  error=marcar una bomba donde no la hay, lose=tocar sobre una bomba win=cuando el usuario encuentra todas las bombas
    private void finishGame(String condition){
        FinishFragment finishFragment = new FinishFragment();
        bundle.putString("condition", condition);
        finishFragment.setArguments(bundle);
        T.cancel();
        imageViewExit.setEnabled(false);
        imageViewRefresh.setEnabled(false);
        imageViewResume.setEnabled(false);
        imageViewPause.setEnabled(false);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.pop_enter, R.animator.pop_exit);
        fragmentTransaction.add(R.id.game,finishFragment);
        fragmentTransaction.commit();
    }

    //Método que incrementa el valor de count en 1 cada segundo
    private void contador(){
        imageViewResume.setEnabled(false);
        T=new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (count < 10) {
                            textViewTime.setText(" 0" + count + "'");
                            count++;
                        } else if (count < 100) {
                            textViewTime.setText(" " + count + "'");
                            count++;
                        } else {
                            textViewTime.setText(count + "'");
                            count++;
                        }

                    }
                });
            }
        }, 1000, 1000);
    }

    //Método que evalua si una casilla está dentro del tablero
    public boolean valido(int i, int j) {
        if (i >= 0 && i < this.game.length && j >= 0 && j < this.game[0].length) {
            return true;
        }
        else return false;
    }

    //Método que evalua si las casillas que hay al rededor contienen una mina o no, si no la hay se pone la casilla en blaco
    public void vecino(int i, int j,int position){
        if (game[i][j]==0 && position >= 0 && position < elements){
            OwnImageButton button=(OwnImageButton)gridLayout.getChildAt(position);
            button.setImageResource(R.drawable.grass_two);
            game[i][j]= -2;
            button.setEnabled(false);
            if (valido(i + 1, j)) vecino(i + 1,j,(position+level));
            if (valido(i - 1, j)) vecino(i - 1, j,(position-level));
            if (valido(i, j + 1)) vecino(i, j + 1,position+1);
            if (valido(i, j - 1)) vecino(i, j - 1,position-1);
            if (valido(i - 1, j - 1)) vecino(i - 1, j - 1,((position-level)-1));
            if (valido(i - 1, j + 1)) vecino(i - 1, j + 1,((position-level)+1));
            if (valido(i + 1, j - 1)) vecino(i + 1, j - 1,((position+level)-1));
            if (valido(i + 1, j + 1)) vecino(i + 1, j + 1,((position+level)+1));
        }else if(game[i][j]>0 && position >= 0 && position < elements) {
            OwnImageButton button = (OwnImageButton) gridLayout.getChildAt(position);
            switch (game[i][j]) {
                case 1:
                    button.setImageResource(R.drawable.one);
                    break;
                case 2:
                    button.setImageResource(R.drawable.two);
                    break;
                case 3:
                    button.setImageResource(R.drawable.three);
                    break;
                case 4:
                    button.setImageResource(R.drawable.four);
                    break;
                case 5:
                    button.setImageResource(R.drawable.five);
                    break;
                case 6:
                    button.setImageResource(R.drawable.six);
            }
            button.setEnabled(false);
        }
    }

    @Override
    public void onBackPressed()
    {
        exit();
    }

    //Método para tratar la salida del juego
     public void exit(){
         new AlertDialog.Builder(this)
                 .setMessage("Are you sure you want to exit?")
                 .setCancelable(false)
                 .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int id) {
                         Intent intent=new Intent(getApplicationContext(),Main.class);
                         startActivity(intent);
                         player.stop();
                         finish();
                     }
                 })
                 .setNegativeButton("No", null)
                 .show();
     }

    //Método para tratar el reload del juego
    public void reloadGame(){
        new AlertDialog.Builder(this)
                .setMessage("Do you want to reload the game?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        player.stop();
                        finish();
                        startActivity(getIntent());
                    }
                })
                .setNegativeButton("No", null)
                .show();
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
