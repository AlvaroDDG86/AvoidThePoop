package com.example.alvaro.avoidthepoop;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class Finish extends Activity {
    //Variables para la clase
    ImageView imageViewNewGame;
    ImageView imageViewExit;
    String condition,position;
    TextView textViewState;
    ImageView imageViewCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        imageViewCondition=(ImageView)findViewById(R.id.imageViewCondition);

        condition=getIntent().getExtras().getString("condition");
        textViewState=(TextView)findViewById(R.id.textViewState);
        if(condition.equalsIgnoreCase("win")){
            position=getIntent().getExtras().getString("position");
            switch (position){
                case "first":
                    textViewState.setText("YOU ARE THE FIRST!!!");
                    imageViewCondition.setImageResource(R.drawable.gold);
                    break;
                case "second":
                    textViewState.setText("YOU ARE THE SECOND!!!");
                    imageViewCondition.setImageResource(R.drawable.silver);
                    break;
                case "thrid":
                    textViewState.setText("YOU ARE THE THRID!!!");
                    imageViewCondition.setImageResource(R.drawable.bronze);
                    break;
                case "noRecord":
                    textViewState.setText("YOU WIN!!!");
                    imageViewCondition.setImageResource(R.drawable.winner);
            }
        }else if(condition.equalsIgnoreCase("error")){
            textViewState.setText("You are wrong");
            imageViewCondition.setImageResource(R.drawable.confused);
        }else{
            textViewState.setText("The dog has bitten you");
            imageViewCondition.setImageResource(R.drawable.smell);
        }

        imageViewNewGame=(ImageView)findViewById(R.id.imageViewNewGame);
        imageViewNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Game.class);
                startActivity(intent);
                Game.player.stop();
                finish();
            }
        });
        imageViewExit=(ImageView)findViewById(R.id.imageViewExit);
        imageViewExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Main.class);
                startActivity(intent);
                Game.player.stop();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        Game.player.stop();
        super.onBackPressed();  // optional depending on your needs
    }
}
