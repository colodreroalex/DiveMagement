package com.example.divemagement.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import android.util.Log;

import com.example.divemagement.Inmersion;
import com.example.divemagement.InmersionesProvider;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DiveManagement.db";

    public static final String TABLE_CLIENTES = "t_clientes";
    public static final String TABLE_INMERSIONES = "t_inmersiones";
    public static final String TABLE_REGISTRO_INMERSIONES = "t_registro_inmersiones";

    //Constructor
    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void addClient(String user, String password, String telefono, String correo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put("username", user);
        values.put("password", password);
        values.put("telefono", telefono);
        values.put("correo", correo);
        db.insert(TABLE_CLIENTES, null, values);

        db.close();

    }

    //Create database
    @Override
    public void onCreate(SQLiteDatabase db) {


        // CREACION DE LA TABLA DE CLIENTES
        db.execSQL("CREATE TABLE " + TABLE_CLIENTES + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL, " +
                "password TEXT NOT NULL, " +
                "telefono TEXT NOT NULL, " +
                "correo TEXT )");

        // CREACION DE LA TABLA DE INMERSIONES
        db.execSQL("CREATE TABLE " + TABLE_INMERSIONES + "(" +
                "id INTEGER PRIMARY KEY , " +
                "nombre TEXT NOT NULL, " +
                "profundidad FLOAT NOT NULL, " +
                "fecha TEXT NOT NULL, " +
                "hora TEXT NOT NULL, " +
                "temperatura FLOAT , " +
                "visibilidad TEXT , " +
                "lugar TEXT , " +
                "descripcion TEXT , " +
                "photo TEXT , " +
                "FOREIGN KEY (id) REFERENCES t_clientes (id))");

        // CREACION DE LA TABLA DE REGISTRO DE INMERSIONES
        db.execSQL("CREATE TABLE " + TABLE_REGISTRO_INMERSIONES + "(" +
                "id_registro INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_cliente INTEGER NOT NULL, " +
                "id_inmersion INTEGER NOT NULL, " +
                "FOREIGN KEY (id_cliente) REFERENCES t_clientes (id), " +
                "FOREIGN KEY (id_inmersion) REFERENCES t_inmersiones (id))");


        addInitialInmersiones(db);
    }

    public void addInitialInmersiones(SQLiteDatabase db) {
        List<Inmersion> inmersionesList = InmersionesProvider.Companion.getInmersionesList();

        // Insertamos las inmersiones iniciales
        if (inmersionesList != null && !inmersionesList.isEmpty()) {
            for (Inmersion inmersion : inmersionesList) {
                ContentValues values = new ContentValues();
                values.put("nombre", inmersion.getNombre());
                values.put("profundidad", inmersion.getProfundidad());
                values.put("fecha", inmersion.getFecha());
                values.put("hora", inmersion.getHora());
                values.put("temperatura", inmersion.getTemperatura());
                values.put("visibilidad", inmersion.getVisibilidad());
                values.put("lugar", inmersion.getLugar());
                values.put("descripcion", inmersion.getDescripcion());
                db.insert(TABLE_INMERSIONES, null, values);
            }
        }

    }

    public List<Inmersion> getInmersiones() {
        List<Inmersion> inmersionesList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_INMERSIONES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Inmersion inmersion = new Inmersion(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getFloat(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getFloat(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        null
                );

                inmersionesList.add(inmersion);
            } while (cursor.moveToNext());
        }

        return inmersionesList;
    }


    //Update database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Eliminamos la version anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INMERSIONES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTRO_INMERSIONES);

        //Creamos la nueva version de la tabla
        onCreate(db);


    }


    public void insertInmersion(@NotNull final Inmersion inmersion) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("nombre", inmersion.getNombre());
                values.put("profundidad", inmersion.getProfundidad());
                values.put("fecha", inmersion.getFecha());
                values.put("hora", inmersion.getHora());
                values.put("temperatura", inmersion.getTemperatura());
                values.put("visibilidad", inmersion.getVisibilidad());
                values.put("lugar", inmersion.getLugar());
                values.put("descripcion", inmersion.getDescripcion());

                db.insert(TABLE_INMERSIONES, null, values);
                db.close();

                InmersionesProvider.Companion.getInmersionesList().add(inmersion);


            }
        }).start();
    }

    public void deleteInmersionPorNombre(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Agrega una declaración de registro antes de la operación de eliminación
        Log.d("DbHelper", "Intentando eliminar la inmersión con nombre: " + name);

        int rowsDeleted = db.delete(TABLE_INMERSIONES, "nombre = ?", new String[]{name});

        // Agrega una declaración de registro después de la operación de eliminación
        if (rowsDeleted > 0) {
            Log.d("DbHelper", "Inmersión eliminada correctamente");
        } else {
            Log.d("DbHelper", "No se encontró ninguna inmersión con el nombre: " + name);
        }

        db.close();
    }

    public boolean existeInmersionOno(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Comprueba si existe una inmersión con el nombre proporcionado
        Cursor cursor = db.query(TABLE_INMERSIONES, new String[]{"nombre"}, "nombre = ?", new String[]{name}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }
}
