package com.example.crudsqlitesem6;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.R;
//import android. app. AlertDialog,

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import  java.util.Calendar;

public class ConexionSQLite extends SQLiteOpenHelper {

    boolean estadoDelete = true;

    ArrayList<String> listaArticulos; //Va a representar la informacion que se va a mostrar en el combo
    ArrayList<Dto> articulosList;    //Entidad que representa a los datos de la tabla, en esta caso la tabla Articulos

    /*
    public ConexionSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
    super(context, name, factory, version);

    */
    public ConexionSQLite(Context context) {
        super(context, "administracion.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table articulos(codigo integer not null primary key, descripcion text, precio real)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists articulos");
        onCreate(db);
    }

    public SQLiteDatabase bd() {
        SQLiteDatabase bd = this.getWritableDatabase();
        return bd;
    }

    public boolean InserTradicional(Dto datos) {
        boolean estado = true;
        int resultado;
        //SQLiteDatabase bd —— this.getWritableDatabase(),
        try {
            int codigo = datos.getCodigo();
            String descripcion = datos.getDescripcion();
            double precio = datos.getPrecio();

            //Cursor fila —— this.getWritableDatabase().rawQuery(”select codigo from adiculos where codigo——”’+codigo+ ’, null),
            Cursor fila = bd().rawQuery("select codigo from articulos where codigo='" + codigo + "'", null);
            if (fila.moveToFirst() == true) {
                estado = false;
            } else {
                //estado —— (boolean)this.getWritableDatabase().insed(”datos”,”nombre, correo, telefono”, registro),
                //resultado —— (int) this.getWritableDatabase().insert(”usuarios”, "nombres, apellidos, usuario, clave,pregunta,respuesta", registro),
                String SQL = "INSERT INTO articulos \n" + "(codigo,descripcion,precio)\n" + "VALUES \n" +
                        "('" + String.valueOf(codigo) + "', '" + descripcion + "', '" + String.valueOf(precio) + "');";
                bd().execSQL(SQL);
                bd().close();

        /*
        this.getWritableDatabase().execSQL(SQL):
        this.getWritabIeDatabase(). close();

        */

                estado = true;
            }

        } catch (Exception e) {
            estado = false;
            Log.e("error.", e.toString());
        }
        return estado;
    }

    public boolean insertardatos(Dto datos) {
        boolean estado = true;
        int resultado;
        ContentValues registro = new ContentValues();
        try {

            //registro.put(”codigo",datos.getCodigo());
            registro.put("codigo", datos.getCodigo());
            registro.put("descripcion", datos.getDescripcion());
            registro.put("precio", datos.getPrecio());
            /*adicione estas lineas para verificar sino existe un usuario con el mismo correo INICIO. */
            //Cursor fila —— this.getWritableDatabase().rawQuery(”select codigo from adiculos where codigo——”’+datos.getCodigo()+”’”, null),
            Cursor fila = bd().rawQuery("selectcodigo from articulos where codigo='" + datos.getCodigo() + "'", null);
            if (fila.moveToFirst() == true) {
                estado = false;
            } else {
                /*FIN*/
                //estado —— (boolean)this.getWritableDatabase().insed(”datos”,”nombre, correo, telefono”,registro),
                //resultado —— (int) this.getWritableDatabase().insed(”adiculos”, "codigo, descripcion,precio", registro),"
                resultado = (int) bd().insert("articulos", null, registro);
                //resultado —— (int) this.getWritableDatabase().insert(”articulos", "codigo", registro),
                if (resultado > 0) estado = true;
                else estado = false;

            }
        } catch (Exception e) {
            estado = false;
            Log.e("error.", e.toString());

        }
        return estado;
    }

    public boolean InsertRegister(Dto datos) {
        boolean estado = true;
        int resultado;

        try {

            int codigo = datos.getCodigo();
            String descripcion = datos.getDescripcion();
            double precio = datos.getPrecio();

            //getting the current time for joining date
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fecha1 = sdf.format(cal.getTime());

            Cursor fila = bd().rawQuery("select codigo from articulos where codigo='" +
                    datos.getCodigo() + "'", null);
            if (fila.moveToFirst() == true) {
                estado = false;

            } else {
                //estado —— (boolean)this.getWritableDatabase().insed(”datos”,”nombre, correo, telefono”, registro),
                //resultado —— (int) this.getWritableDatabase().insert(”usuarios”, "nombres, apellidos, usuario, clave,pregunta,respuesta", registro),
                String SQL = "INSERT INTO articulos \n" + "(codigo,descripcion,precio)\n" + "VALUES\n" + "(?,?,?);";

                //resultado —— (int) this.getWritableDatabase().insed(”usuarios”, nombres, apellidos, usuario, clave,pregunta,respuesta”, registro),
                bd().execSQL(SQL, new String[]{String.valueOf(codigo), descripcion, String.valueOf(precio)});
                //if (resultado > 0) estado —— true,’
                //else estado —— false,
                estado = true;
            }
        } catch (Exception e) {
            estado = false;
            Log.e("error.", e.toString());
        }
        return estado;
    }

    public boolean consultaCodigo(Dto datos) {
        boolean estado = true;
        int resultado;
        SQLiteDatabase bd = this.getWritableDatabase();
        try {

            int codigo = datos.getCodigo();
            //Cursor fila —— bd.rawQuery(”select descripcion, precio from articulos where codigo——”’+ codigo +	', null),’
            //Cursor fila —— bd.rawQuery(”select descripcion, precio from articulos where codigo——” + codigo, null),”
            Cursor fila = bd.rawQuery("select codigo, descripcion, precio from articulos where codigo=" + codigo, null);
            if (fila.moveToFirst()) {
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));
                estado = true;
            } else {
                estado = false;
            }
            bd.close();
        } catch (Exception e) {
            estado = false;
            Log.e("error.", e.toString());
        }
        return estado;
    }
    //Otra forma de realizar una consulta. método opcional de realizar.

    public boolean consultaArticulos(Dto datos) {
        boolean estado = true;
        int resultado;

        //SQLiteDatabase bd —— this.getWritableDatabase(),
        SQLiteDatabase bd = this.getReadableDatabase();
        try {
            String[] parametros = {String.valueOf(datos.getCodigo())};
            String[] campos = {"codigo", "descripcion", "precio"};
            Cursor fila = bd.query("articulos", campos, "codigo=?", parametros, null, null, null);
            //fila. moveToFirst(),
            if (fila.moveToFirst()) {
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));
                estado = true;
            } else {
                estado = false;
            }
            fila.close();
            bd.close();
        } catch (Exception e) {
            estado = false;
            Log.e("error.", e.toString());
        }
        return estado;
    }
    //Fin del método opcional


    public boolean consultarDescripcion(Dto datos) {
        boolean estado = true;
        int resultado;
        SQLiteDatabase bd = this.getWritableDatabase();
        try {
            String descripcion = datos.getDescripcion();
            Cursor fila = bd.rawQuery("select codigo, descripcion, precio from articulos" +
                    "where descripcion='" + descripcion + "'", null);
            if (fila.moveToFirst()) {
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));
                estado = true;
            } else {
                estado = false;
            }
            bd.close();
        } catch (Exception e) {
            estado = false;
            Log.e("error.", e.toString());
        }
        return estado;
    }

    public boolean bajaCodigo(final Context context, final Dto datos) {
        //SQLiteDatabase bd —— this.getWritableDatabase(),
        estadoDelete = true;
        try {
            int codigo = datos.getCodigo();
            Cursor fila = bd().rawQuery("select * from articulos where codigo=" + codigo, null);
            if (fila.moveToFirst()) {
                datos.setCodigo(Integer.parseInt(fila.getString(0)));
                datos.setDescripcion(fila.getString(1));
                datos.setPrecio(Double.parseDouble(fila.getString(2)));
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.ic_borrar;
                builder.setTitle("Warning");
                builder.setMessage("¿Esta seguro de borrar el registro? \nCodigo: " + datos.getCodigo() + "\nDescripción: " + datos.getDescripcion());
                builder.setCancelable(false);
                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //String[] parametros —— {String. valueOf(datos.getCodigo())},
                        int codigo = datos.getCodigo();
                        int cant = bd().delete("articulos", "codigo=" + codigo, null);
                        //bd(). delete(”articulos”,”codigo——?”,parametros),
                        if (cant > 0) {
                            estadoDelete = true;
                            Toast.makeText(context, "Registro eliminado satisfactoriamente.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            estadoDelete = false;
                        }
                        bd().close();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                Toast.makeText(context, "No hay resultados encontrados para la busqueda especificada.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            estadoDelete = false;
            Log.e("Error.", e.toString());
        }
        return estadoDelete;
    }

    public boolean modificar(Dto datos) {
        boolean estado = true;
        int resultado;
        SQLiteDatabase bd = this.getWritableDatabase();
        try {
            int codigo = datos.getCodigo();
            String descripcion = datos.getDescripcion();
            double precio = datos.getPrecio();

            //String[j parametros —— {String. vaIueOf(datos.getCodigo())},

            ContentValues registro = new ContentValues();
            registro.put("codigo", codigo);
            registro.put("descripcion", descripcion);
            registro.put("precio", precio);
            //int cant —— (int) this.getWritableDatabase().update(”adiculos”, registro, "codigo——" + codigo, null),”
            int cant = (int) bd.update("articuIos", registro, "codigo=" + codigo, null);
            //bd.update("articulos”,registro,"codigo——?”,parametros),’
            bd.close();
            if (cant > 0) estado = true;
            else estado = false;
        } catch (Exception e) {
            estado = false;
            Log.e("error.", e.toString());
        }
        return estado;
    }

    //lnicio del Método para crear lista de datos de la BD en el Spinner.
    //public void consultaListaAdiculos(){
    public ArrayList<Dto> consultaListaArticulos() {
        boolean estado = false;
        //SQLiteDatabase bd —— this.getWritableDatabase(),
        SQLiteDatabase bd = this.getReadableDatabase();
        Dto articulos = null;    //Creamos la instancia vacia.
        articulosList = new ArrayList<Dto>();

        try {

            Cursor fila = bd.rawQuery("select * from articulos", null);
            while (fila.moveToNext()) {
                articulos = new Dto();
                articulos.setCodigo(fila.getInt(0));
                articulos.setDescripcion(fila.getString(1));
                articulos.setPrecio(fila.getDouble(2));

                articulosList.add(articulos);

                Log.i("codigo", String.valueOf(articulos.getCodigo()));
                Log.i("descripcion", articulos.getDescripcion().toString());
                Log.i("precio", String.valueOf(articulos.getPrecio()));
            }

            obtenerListaArticulos();

        } catch (Exception e) {

        }
        return articulosList;
    }

    public ArrayList<String> obtenerListaArticulos() {
        listaArticulos = new ArrayList<String>();
        //IistaArticulos —— new ArrayList<>(),
        listaArticulos.add("Seleccione");

        for (int i = 0; i < articulosList.size(); i++) {
            //IistaArticulos.add(String. valueOf(articulosList.get(i).getCodigo())),
            listaArticulos.add(articulosList.get(i).getCodigo() + " ~ " + articulosList.get(i).getDescripcion());
        }
        //bd(). close(),
        return listaArticulos;
    }
    //Fin del Spinner.


    //Inicio del Método para crear lista de datos de la BD en el ListView.

    public ArrayList<String> consultaListaArticulos1() {
        boolean estado = false;
        //SQLiteDatabase bd —— this.getWritableDatabase(),
        SQLiteDatabase bd = this.getReadableDatabase();

        Dto articulos = null;    //Creamos la instancia vacia.
        articulosList = new ArrayList<Dto>();

        try {
            Cursor fila = bd.rawQuery("select * from articulos", null);
            while (fila.moveToNext()) {
                articulos = new Dto();
                articulos.setCodigo(fila.getInt(0));
                articulos.setDescripcion(fila.getString(1));
                articulos.setPrecio(fila.getDouble(2));

                articulosList.add(articulos);
            }

            listaArticulos = new ArrayList<String>();
            //IistaArticulos —— new ArrayList<>(),
            //IistaArticulos.add(”Seleccione"),

            for (int i = 0; i <= articulosList.size(); i++) {
                //IistaArticulos. add(String. valueOf(adiculosList.get(i).getCodigo())),
                listaArticulos.add(articulosList.get(i).getCodigo() + " ~ " + articulosList.get(i).getDescripcion());
            }
            //bd(). close(),
            //return IistaArticulos,’

        } catch (Exception e) {

        }
        //return articulosList,”
        return listaArticulos;

    }
}
