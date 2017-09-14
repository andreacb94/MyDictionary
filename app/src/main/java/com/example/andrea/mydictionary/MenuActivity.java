package com.example.andrea.mydictionary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

// Activity per il menù dell'app

public class MenuActivity extends AppCompatActivity { //Dichiaro la classe

    private Button buttonInizia; //Dichiaro il buttonInizia

    //Inizializzo l'Activity
    @Override //Modifico il metodo onCreate della superclasse
    protected void onCreate(Bundle savedInstanceState) { //Protected perché è specifico di questa classe

        super.onCreate(savedInstanceState); //In questo caso richiamo il Metodo ononimo della superclasse perchè mi sta comodo usarlo
        setContentView(R.layout.activity_menu);

        buttonInizia = (Button) findViewById(R.id.buttonInizia); // buttonInizia lo inizializzo con l'ononimo del file xml dell'activity

        buttonInizia.setOnClickListener(new View.OnClickListener() { //Il metodo listener per il tap del button
            public void onClick(View view) {

                //Passo ad un'altra activity con il tap del button
                Intent i = new Intent(getApplicationContext(), ViewActivity.class);
              //  Intent i = new Intent(getApplicationContext(), ProvaDbActivity.class);
                startActivity(i);
                finish();

            }
        });

    }

    @Override //Metodo di gestione del tasto indietro
    public void onBackPressed() {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:

                        finish();

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Stai per uscire da MyDictionary, sei sicuro?").setNegativeButton("No", dialogClickListener).setPositiveButton("Sì", dialogClickListener).show();

    }
}
