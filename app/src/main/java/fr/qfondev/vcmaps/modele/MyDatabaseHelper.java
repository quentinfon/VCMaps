package fr.qfondev.vcmaps.modele;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;


public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "RepereBdd";

    private static final String TABLE_REPERE = "Reperes";

    private static final String COLUMN_NOM ="REP_nom";
    private static final String COLUMN_POSLAT ="REP_PosLat";
    private static final String COLUMN_POSLON = "REP_PosLon";

    private static final String TABLE_GROUPE = "Groupes";

    private static final String COLUMN_GROUPE_NOM = "GRP_nom";
    private static final String COLUMN_DESC_GROUPE = "GRP_desc";




    public MyDatabaseHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");
        // Script.
        String scriptRepere = "CREATE TABLE " + TABLE_REPERE + "("
                + COLUMN_NOM + " TEXT PRIMARY KEY," + COLUMN_POSLAT + " TEXT," + COLUMN_POSLON + " TEXT,"
                + COLUMN_GROUPE_NOM + " TEXT" + ")";
        String scriptGroupe = "CREATE TABLE " + TABLE_GROUPE + "("
                + COLUMN_GROUPE_NOM + " TEXT PRIMARY KEY,"
                + COLUMN_DESC_GROUPE + " TEXT" + ")";
        // Execute Script.
        db.execSQL(scriptRepere);
        db.execSQL(scriptGroupe);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPERE);

        // Create tables again
        onCreate(db);
    }



    public void addRepere(RepereLieux lieu) {
        Log.i(TAG, "MyDatabaseHelper.addNote ... " + lieu.getNom());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOM, lieu.getNom());
        values.put(COLUMN_POSLAT, lieu.getLatitude());
        values.put(COLUMN_POSLON, lieu.getLongitude());
        values.put(COLUMN_GROUPE_NOM, lieu.getGroupe());

        // Inserting Row
        db.insert(TABLE_REPERE, null, values);

        // Closing database connection
        db.close();
    }


    public RepereLieux getLieu(String nom) {
        Log.i(TAG, "MyDatabaseHelper.getLieu ... " + nom);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_REPERE, new String[] { COLUMN_NOM,
                        COLUMN_POSLAT, COLUMN_POSLON, COLUMN_GROUPE_NOM }, COLUMN_NOM + "=?",
                new String[] { nom }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        RepereLieux lieu = new RepereLieux(cursor.getString(0), Double.parseDouble(cursor.getString(1)), Double.parseDouble(cursor.getString(2)), cursor.getString(3));
        // return lieu
        return lieu;
    }


    public List<RepereLieux> getAllLieux() {
        Log.i(TAG, "MyDatabaseHelper.getAllNotes ... " );

        List<RepereLieux> listeLieux = new ArrayList<RepereLieux>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REPERE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RepereLieux lieu = new RepereLieux(cursor.getString(0), Double.parseDouble(cursor.getString(1)), Double.parseDouble(cursor.getString(2)), cursor.getString(3));
                // Adding note to list
                listeLieux.add(lieu);
            } while (cursor.moveToNext());
        }

        // return note list
        return listeLieux;
    }

    public int getLieuxCount() {
        Log.i(TAG, "MyDatabaseHelper.getLieuxCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_REPERE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }


    public int updateLieu(RepereLieux lieu) {
        Log.i(TAG, "MyDatabaseHelper.updateLieu ... "  + lieu.getNom());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_POSLAT, lieu.getLatitude());
        values.put(COLUMN_POSLON, lieu.getLongitude());
        values.put(COLUMN_GROUPE_NOM, lieu.getGroupe());

        // updating row
        return db.update(TABLE_REPERE, values, COLUMN_NOM + " = ?",
                new String[]{lieu.getNom()});
    }

    public void deleteLieu(RepereLieux lieu) {
        Log.i(TAG, "MyDatabaseHelper.deleteLieu ... " + lieu.getNom() );

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REPERE, COLUMN_NOM + " = ?",
                new String[] { lieu.getNom() });
        db.close();
    }

    public void addGroupe(GroupeRepere groupe) {
        Log.i(TAG, "MyDatabaseHelper.addGroupe ... " + groupe.getNom());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_GROUPE_NOM, groupe.getNom());
        if (groupe.getDescription() == null) {
            values.put(COLUMN_DESC_GROUPE, "Aucune");
        }
        else {
            values.put(COLUMN_DESC_GROUPE, groupe.getDescription());
        }


        // Inserting Row
        db.insert(TABLE_GROUPE, null, values);

        // Closing database connection
        db.close();
    }


    public GroupeRepere getGroupe(String nom) {
        Log.i(TAG, "MyDatabaseHelper.getGroupe ... " + nom);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_GROUPE, new String[] { COLUMN_GROUPE_NOM,
                        COLUMN_DESC_GROUPE}, COLUMN_GROUPE_NOM + "=?",
                new String[] { nom }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        GroupeRepere groupe;

        if (cursor.getString(1) == null){
            groupe = new GroupeRepere(cursor.getString(0));
        }
        else{
            groupe = new GroupeRepere(cursor.getString(0), cursor.getString(1));
        }

        // return lieu
        return groupe;
    }


    public List<GroupeRepere> getAllGroupes() {
        Log.i(TAG, "MyDatabaseHelper.getAllGroupes ... " );

        List<GroupeRepere> listeGroupes = new ArrayList<GroupeRepere>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_GROUPE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                GroupeRepere groupe = new GroupeRepere(cursor.getString(0), cursor.getString(1));
                // Adding note to list
                listeGroupes.add(groupe);
            } while (cursor.moveToNext());
        }

        // return note list
        return listeGroupes;
    }

    public int getGroupesCount() {
        Log.i(TAG, "MyDatabaseHelper.getGroupesCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_GROUPE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }


    public int updateGroupe(GroupeRepere groupe) {
        Log.i(TAG, "MyDatabaseHelper.updateGroupe ... "  + groupe.getNom());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DESC_GROUPE, groupe.getDescription());


        // updating row
        return db.update(TABLE_GROUPE, values, COLUMN_GROUPE_NOM + " = ?",
                new String[]{groupe.getNom()});
    }

    public void deleteGroupe(GroupeRepere grp) {
        Log.i(TAG, "MyDatabaseHelper.deleteGroupe ... " + grp.getNom() );

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GROUPE, COLUMN_GROUPE_NOM + " = ?",
                new String[] { grp.getNom() });
        db.close();
    }

}