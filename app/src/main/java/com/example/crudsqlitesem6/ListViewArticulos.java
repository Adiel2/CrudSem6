package com.example.crudsqlitesem6;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent; import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListViewArticulos extends AppCompatActivity  {
    ListView listViewPersonas;
    ArrayAdapter adaptador;
    SearchView searchView;
    ListView IistView;
    ArrayList<String> list;
    ArrayAdapter adapter;

    String[] version =
            {"Aestro","Blender","CupCake","Donut","Eclair","Froyo","GingerBread","HoneyComb", "IceCream Sandwich",
                    "Jelly Bean","Kitkat","Lolipop","Marshmallow","Nought","Oreo"};

ConexionSQLite conexion = new ConexionSQLite(this); Dto datos = new Dto();

    @Override
    protected void onCreate(Bundle savedlnstancestate){
        super.onCreate(savedlnstancestate);
        setContentView(R.layout.activity_list_view_articulos);
        listViewPersonas= (ListView) findViewById(R.id.listViewPersonas);
        searchView = (SearchView) findViewById(R.id.searchView);

        adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, conexion.consultaListaArticulos1());
        listViewPersonas.setAdapter(adaptador);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s){
                String text = s; adaptador.getFilter().filter(text); return false;
            }

        });
        listViewPersonas.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long I){
                String informacion="Cédigo: "+conexion.consultaListaArticulos().get(pos).getDescripcion()+"\n";
                informacion+="Descripcién: "+conexion.consultaListaArticulos().get(pos).getDescripcion()+"\n";
                informacion+="Precio: "+conexion.consultaListaArticulos().get(pos).getPrecio();

                Dto articulos = conexion.consultaListaArticulos().get(pos);
                Intent intent = new Intent(ListViewArticulos.this, detalles_articulos.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("articulo", articulos);
                intent.putExtras(bundle); startActivity(intent);

            }

        });
    }
}
