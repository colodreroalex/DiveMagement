package com.example.divemagement.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
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





    //Metodo para chequear si el usuario existe
    public boolean checkUser(String username, String password) {

        String[] columnas = { "username" };
        SQLiteDatabase db = getReadableDatabase();
        String seleccion = "username=? and password=?";
        String[] selectionArgs = { username, password };
        Cursor cursor = db.query(TABLE_CLIENTES, columnas, seleccion, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if(count > 0)
            return true;
        else
            return false;
    }

    public void addInmersion(Inmersion inmersion) {
        SQLiteDatabase db = this.getWritableDatabase();
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

    public void addClient(String username, String password, String telefono, String correo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        
        values.put("username", username);
        values.put("password", password);
        values.put("telefono", telefono);
        values.put("correo", correo);
        db.insert(TABLE_CLIENTES, null, values);

    }

    //Create database
    @Override
    public void onCreate(SQLiteDatabase db) {



        //CREACION TABLA PARA EL ADMIN
        db.execSQL("CREATE TABLE admin(usuario TEXT PRIMARY KEY, password TEXT NOT NULL)");
        String usuario = "admin";
        String password = "1234";
        ContentValues datosAdmin = new ContentValues();
        datosAdmin.put("usuario", usuario);
        datosAdmin.put("password", password);
        db.insert("admin", "(usuario,password)", datosAdmin);



        //CREACION DE LA TABLA DE CLIENTES
        db.execSQL("CREATE TABLE " + TABLE_CLIENTES + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL, " +
                "password TEXT NOT NULL, " +
                "telefono TEXT NOT NULL, " +
                "correo TEXT )");

        //CREACION DE LA TABLA DE INMERSIONES
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
                "FOREIGN KEY (id) REFERENCES t_clientes (id))");

        //CREACION DE LA TABLA DE REGISTRO DE INMERSIONES

        db.execSQL("CREATE TABLE " + TABLE_REGISTRO_INMERSIONES + "(" +
                "id_registro INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_cliente INTEGER NOT NULL, " +
                "id_inmersion INTEGER NOT NULL, " +
                "FOREIGN KEY (id_cliente) REFERENCES t_clientes (id), " +
                "FOREIGN KEY (id_inmersion) REFERENCES t_inmersiones (id))");
        
        
        addInitialInmersiones(db);
    }

    private void addInitialInmersiones(SQLiteDatabase db) {
        List<Inmersion> inmersionesList = getInitialInmersiones();

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

    public List<Inmersion> getInitialInmersiones() {
        InmersionesProvider inmersionesProvider = new InmersionesProvider();
        return inmersionesProvider.getInmersionesList();
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



}
