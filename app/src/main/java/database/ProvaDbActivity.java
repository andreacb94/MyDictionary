package database;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.andrea.mydictionary.R;

import java.util.ArrayList;

import database.DbAdapter;

//Activity per provare le funzioni di gestione del database
public class ProvaDbActivity extends AppCompatActivity {

    private EditText et1, et2;
    private Button b1, b2, b3;
    private ListView list;

    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    DbAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prova_db);

        et1 = (EditText) findViewById(R.id.editTextParola);
        et2 = (EditText) findViewById(R.id.editTextSignificato);
        b1 = (Button) findViewById(R.id.buttonSalva);
        b2 = (Button) findViewById(R.id.buttonCerca);
        b3 = (Button) findViewById(R.id.buttonCancella);
        list = (ListView) findViewById(R.id.listView);

        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);

        list.setAdapter(adapter);

        //Aggiungo un elemento
        b1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String Parola, Significato;
                db = new DbAdapter(getApplicationContext());

                Parola = et1.getText().toString();
                Significato = et2.getText().toString();

                db.open();
                db.createContact(Parola, Significato);
                db.close();

            }

        });

        //Cerco tutti gli elementi nel database e li visualizzo ordinati
        b2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Cursor cur;
                String text = "";
                db = new DbAdapter(getApplicationContext());

                //Occhio fare identico da qui...
                db.open();
                cur = db.fetchAllContactsAbOrder("parola");

                cur.moveToFirst();

                listItems.clear();
                adapter.notifyDataSetChanged();

                while(cur.isAfterLast() == false) {

                    String parola = cur.getString(1);
                    String significato = cur.getString(2);
                    text = parola + ": " + significato;
                    listItems.add(text);
                    adapter.notifyDataSetChanged();
                    cur.moveToNext();

                }

                cur.close();
                db.close();

                //... a qui

            }

        });

        //Elimina tutti gli elementi del database
        b3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                db = new DbAdapter(getApplicationContext());

                db.open();
                db.removeAll();
                db.close();

                listItems.clear();
                adapter.notifyDataSetChanged();

            }

        });


    }

}
