package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Andrea on 14/10/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
/*
    private static final String DATABASE_NAME = "databaseDiProva01.db";
    private static final int DATABASE_VERSION = 1;  */
    private static final String DATABASE_NAME = "databaseBeta0.1.db";
    private static final int DATABASE_VERSION = 1;
    // Lo statement SQL di creazione del database
    private static final String DATABASE_CREATE = "create table contact (_id integer primary key autoincrement, parola text not null, significato text not null);";

    // Costruttore
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Questo metodo viene chiamato durante la creazione del database
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    // Questo metodo viene chiamato durante l'upgrade del database, ad esempio quando viene incrementato il numero di versione
    @Override
    public void onUpgrade( SQLiteDatabase database, int oldVersion, int newVersion ) {

        database.execSQL("DROP TABLE IF EXISTS contact");
        onCreate(database);

    }
}
