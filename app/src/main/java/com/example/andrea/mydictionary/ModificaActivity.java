package com.example.andrea.mydictionary;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import database.DbAdapter;

//Classe dell'Activity per l'inserimento di un nuovo record nel database
public class ModificaActivity extends AppCompatActivity {

    private Button buttonSalva; //Button per salvare
    private EditText editTextParola, editTextSignificato; //Caselle di testo per inserire la parola e il significato

    DbAdapter db; //Creo un oggetto DbAdapter per gestire il database

    //Solito metodo modificato della superClasse per la creazione dell'Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        Bundle datipassati = getIntent().getExtras();
        final long id = datipassati.getLong("_id");
        String oldParola = datipassati.getString("Parola");
        String oldSignificato = datipassati.getString("Significato");

        ActionBar ab = getActionBar(); //ActionBar per menù di opzioni

        buttonSalva = (Button) findViewById(R.id.buttonSalva); //Button per salvare i dati immessi
        editTextParola = (EditText) findViewById(R.id.editTextParola);
        editTextSignificato = (EditText) findViewById(R.id.editTextSignificato);
        editTextParola.setText(oldParola);
        editTextSignificato.setText(oldSignificato);

        //Listener per salvare i dati inseriti
        buttonSalva.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String Parola, Significato;
                db = new DbAdapter(getApplicationContext());

                Parola = editTextParola.getText().toString();
                Significato = editTextSignificato.getText().toString();

                db.open();
                db.updateContact(id, Parola, Significato);
                db.close();

                Toast toast = Toast.makeText(getApplicationContext(), "Salvato", Toast.LENGTH_SHORT);
                toast.show();

            }

        });

    }

    //Inizializzo il menù dell'ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        menu.getItem(0).setVisible(true); //Rendo visibile il primo pulsante
        menu.getItem(0).setTitle("Home"); //Setto il testo visualizzato

        return true;
    }

    //Gestione degli elementi selezionati nel menù di opzioni
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item0: //Torno al'Activity Principale di visualizzazione

                Intent i = new Intent(getApplicationContext(), ViewActivity.class);
                startActivity(i);
                finish();

        }

        return false;

    }

    @Override //Metodo di gestione del tasto indietro
    public void onBackPressed() {

        //Avvio l'activity per l'inserimento di un nuovo elemento
        Intent i = new Intent(getApplicationContext(), ViewActivity.class);
        startActivity(i);
        finish();

    }
}
