package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Andrea on 14/10/2015.
 */
public class DbAdapter {

    @SuppressWarnings("unused")
    private static final String LOG_TAG = DbAdapter.class.getSimpleName();

    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    // Database fields
    private static final String DATABASE_TABLE = "contact";

    public static final String KEY_CONTACTID = "_id";
    public static final String KEY_PAROLA = "parola";
    public static final String KEY_SIGNIFICATO = "significato";

    public DbAdapter(Context context) {
        this.context = context;
    }

    public DbAdapter open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    private ContentValues createContentValues(String parola, String significato) {
        ContentValues values = new ContentValues();
        values.put(KEY_PAROLA, parola);
        values.put(KEY_SIGNIFICATO, significato);

        return values;
    }

    //create a contact
    public long createContact(String parola, String significato) {
        ContentValues initialValues = createContentValues(parola, significato);
        return database.insertOrThrow(DATABASE_TABLE, null, initialValues);
    }

    //update a contact
    public boolean updateContact(long contactID, String parola, String significato) {
        ContentValues updateValues = createContentValues(parola, significato);
        return database.update(DATABASE_TABLE, updateValues, KEY_CONTACTID + "=" + contactID, null) > 0;
    }

    //delete a contact
    public void deleteContact(long key){
        database.delete(DATABASE_TABLE, KEY_CONTACTID + "=" + key, null);
    }

    //delete all contact
    public void removeAll()
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
        database.delete(DATABASE_TABLE, null, null);
    }

    //fetch all contacts
    public Cursor fetchAllContacts() {
     //   return database.query(DATABASE_TABLE, new String[]{KEY_CONTACTID, KEY_PAROLA, KEY_SIGNIFICATO}, null, null, null, null, null);
        return database.query(DATABASE_TABLE, null, null, null, null, null, null);
    }

    public Cursor fetchAllContactsAbOrder(String KEY_COLONNA) {
        return database.query(DATABASE_TABLE, null, null, null, null, null, KEY_COLONNA + " ASC");
    }

    //fetch contacts filter by a string
    public Cursor fetchContactsByFilter(String KEY_COLONNA, String filter) {
        Cursor mCursor = database.query(DATABASE_TABLE, null, KEY_COLONNA + " like '" + filter + "%'", null, null, null, null);

        return mCursor;

    }

    //fetch contacts filter by a string
    public Cursor fetchContactsByFilterAbOrder(String KEY_COLONNA, String filter) {
        Cursor mCursor = database.query(DATABASE_TABLE, null, KEY_COLONNA + " like '" + filter + "%'", null, null, null, KEY_COLONNA + " ASC");

        return mCursor;

    }

}
