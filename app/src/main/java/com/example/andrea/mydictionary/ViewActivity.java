package com.example.andrea.mydictionary;

/* Links:
http://stackoverflow.com/questions/21585326/implementing-searchview-in-action-bar searchview in actionbar
http://www.html.it/pag/48850/creare-un-menu/ menù
http://www.html.it/pag/48856/actionbar/ action bar nel menù
http://www.anddev.it/index.php?topic=6022.0 searcview in actionbar in italiano
 */

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import database.DbAdapter;

//Dichiaro la classe dell'Activity dove verranno visualizzati i dati interni al database
public class ViewActivity extends AppCompatActivity {

    //Le solite dichiarazioni dei componenti visivi dell'activity
    private EditText editTextCerca;
    private Button buttonCerca;
    private ListView list;
    private String scegliVisualizzazione; //Variabile per settare il tipo di dizionario (Inglese: Italiano o Italiano: Inglese)

    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    DbAdapter db;

    //Modifico il metodo onCreate della superClasse per creare l'activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        scegliVisualizzazione = "parola"; //Imposto di vedere prima la Parola e poi il Significato come autoset

        ActionBar ab = getActionBar(); //Dichiaro l'ActionBar per creare un menù di opzioni in alto
        //Setto i componenti con quelli dichairati nel file xml dell'activity
        editTextCerca = (EditText) findViewById(R.id.editTextCerca);
        buttonCerca = (Button) findViewById(R.id.buttonCerca);
        list = (ListView) findViewById(R.id.listView);

        //Inizializzo la ListView come un contenitore di elementi string
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        list.setAdapter(adapter);
        registerForContextMenu(list); //Attribuisco ad ogni elemento della ListView un menù contestuale

        //Faccio visualizzare tutti gli elementi all'avvio dell'activity, a meno che non arrivi un valore esterno
        visualizzaElementi(scegliVisualizzazione, "");

        buttonCerca.setOnClickListener(new View.OnClickListener() { //Il metodo listener per il tap del button
            public void onClick(View view) {

                String filtro = editTextCerca.getText().toString();

                visualizzaElementi(scegliVisualizzazione, filtro);

            }
        });

    }

    //Cerco tutti gli elementi nel database e li visualizzo in ordine alfabetico
    protected void visualizzaElementi(String visualizazione, String filtro) {

        Cursor cur;
        String text = "";
        db = new DbAdapter(getApplicationContext());

        db.open();

        if (filtro.equals("")) //Tutti gli elementi
            cur = db.fetchAllContactsAbOrder(scegliVisualizzazione); //Cerco in ordine alfabetico nel database
        else
            cur = db.fetchContactsByFilterAbOrder(visualizazione, filtro); //Filtro gli elementi e li visualizzo in ordine alfabetico

        cur.moveToFirst();

        listItems.clear();
        adapter.notifyDataSetChanged();

        if (visualizazione.equals("parola")) {

            while (cur.isAfterLast() == false) {

                String parola = cur.getString(1);
                String significato = cur.getString(2);
                text = parola + ": " + significato;
                listItems.add(text);
                adapter.notifyDataSetChanged();
                cur.moveToNext();

            }
        } else if (visualizazione.equals("significato")) {

            while (cur.isAfterLast() == false) {

                String parola = cur.getString(1);
                String significato = cur.getString(2);
                text = significato + ": " + parola;
                listItems.add(text);
                adapter.notifyDataSetChanged();
                cur.moveToNext();

            }
        }

        cur.close();
        db.close();

    }

    //Modifico il metodo onCreateOptionMenu della superClasse per creare il menù nell'ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        menu.getItem(0).setVisible(true);
        menu.getItem(0).setTitle("Aggiungi parola"); //Aggiungo una opzione nel menù

        menu.getItem(1).setVisible(true);
        menu.getItem(1).setTitle("Inverti visualizzazione"); //Aggiungo una opzione nel menù

        /* Impostazioni da implementare
        menu.getItem(2).setVisible(true);
        menu.getItem(2).setTitle("Impostazioni"); //Aggiungo una opzione nel menù
        */

        return true;
    }

    //Un metodo listener per gli elementi del menù appena dichiarato
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) { //Uno switch per capire quale elemento è stato selezionato
            case R.id.item0:

                //Avvio l'activity per l'inserimento di un nuovo elemento
                Intent i = new Intent(getApplicationContext(), InputActivity.class);
                startActivity(i);
                finish();

                break;

            case R.id.item1:

                if (scegliVisualizzazione.equals("parola")) scegliVisualizzazione = "significato";
                else if (scegliVisualizzazione.equals("significato"))
                    scegliVisualizzazione = "parola";
                visualizzaElementi(scegliVisualizzazione, "");
                editTextCerca.setText("");

                break;

            case R.id.item2:

                //Avvio l'activity per la gestione delle impostazioni
                i = new Intent(getApplicationContext(), ImpostazioniActivity.class);
                startActivity(i);
                finish();

        }

        return false;

    }

    //Metodo per la gestione del menù contestuale degli elementi della ListView
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, 0, 0, "Modifica");
        menu.add(0, 1, 0, "Elimina");
    }

    //Un metodo listener per gli elementi del menù appena dichiarato
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        //Acquisisco l'indice dell'elemento della ListView cliccato
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int itemPosition = info.position;

        int id = item.getItemId();

        String itemString, key;

        itemString = listItems.get(itemPosition); //Acquisisco la stringa dell'elemento della ListView
        key = selezionaFiltro(itemString); //Estrapolo solo la prima parola della lista

        switch (id) { //Uno switch per capire quale elemento è stato selezionato
            case 0:
                modificaElemento(key);

                break;

            case 1:
                cancellaElemento(key);

        }

        return false;

    }

    protected void cancellaTutto() {

        db = new DbAdapter(getApplicationContext());

        db.open();
        db.removeAll();
        db.close();

        listItems.clear();
        adapter.notifyDataSetChanged();

        Toast toast = Toast.makeText(getApplicationContext(), "Database eliminato", Toast.LENGTH_SHORT);
        toast.show();

    }

    protected void cancellaElemento(String key) {

        Cursor cur;
        db = new DbAdapter(getApplicationContext());
        db.open();
        cur = db.fetchContactsByFilter(scegliVisualizzazione, key);

        cur.moveToFirst();

        long idSelect = Integer.parseInt(cur.getString(0));
        db.deleteContact(idSelect);
        cur.close();

        listItems.clear();
        adapter.notifyDataSetChanged();

        Toast toast = Toast.makeText(getApplicationContext(), "Eliminato", Toast.LENGTH_SHORT);
        toast.show();

        visualizzaElementi(scegliVisualizzazione, "");

    }

    protected void modificaElemento(String key) {

        Cursor cur;
        db = new DbAdapter(getApplicationContext());
        db.open();
        cur = db.fetchContactsByFilter(scegliVisualizzazione, key);

        cur.moveToFirst();

        long idSelect = Integer.parseInt(cur.getString(0));
        String parolaSelect = cur.getString(1);
        String significatoSelect = cur.getString(2);

        cur.close();

        //Avvio l'activity per l'inserimento di un nuovo elemento
        Intent i = new Intent(getApplicationContext(), ModificaActivity.class);
        i.putExtra("_id", idSelect);
        i.putExtra("Parola", parolaSelect);
        i.putExtra("Significato", significatoSelect);
        startActivity(i);
        finish();

    }

    //Metodo per selezionare solo la prima parola
    public String selezionaFiltro(String st) {
        int inizio = 0; //L'indice iniziale
        int fine = st.indexOf(":"); //L'indice corrispondente al carattere ':'
        String key = st.substring(inizio, fine); //substring ha bisogno di 2 indici per identificare la sottostringa interessata
        return key;
    }


    @Override //Metodo di gestione del tasto indietro
    public void onBackPressed() {

        //Avvio l'activity per l'inserimento di un nuovo elemento
        Intent i = new Intent(getApplicationContext(), MenuActivity.class);
        startActivity(i);
        finish();

    }


}
