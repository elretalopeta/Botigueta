package com.example.eric.botigueta;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ArrayList<Producto> productos = new ArrayList<>(); //Array list de objetos(Producto)
    ArrayList<Producto> carrito = new ArrayList<>(); //Array list para añadir los productos seleccionados del spinner a un listado
    ListView lista;
    Spinner opciones;
    ImageButton añadir;
    TextView resultado;
    int cont = 0;
    double preciofinal = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Indicamo en que vista trabajaremos
        setContentView(R.layout.activity_main);

        //Asociamos los diferentes tipos de variables con los componentes de la vista(activity_main) por el Id
        opciones = (Spinner) findViewById(R.id.spinner);
        añadir = (ImageButton) findViewById(R.id.añadir);
        lista = (ListView) findViewById(R.id.listView);
        resultado = (TextView) findViewById(R.id.total);

        //Inserto todos los productos del array "pruebas" creados en el archivo "strings.xml" en un array de string llamado materies
        String[] materies = getResources().getStringArray(R.array.pruebas);
        //ejemplo resultado materies = [01:manzana:0,45][02:pera:5,0]...

        //Creo un array de string donde añadire cada componente de la materies en un index nuevo. (utilizando ":" como referencia para pasar un elemento a otro)
        //ejemplo resultado item = [01][manzana][0,45]
        for (int i = 0; i < materies.length; i++) {
            String[] item = materies[i].split(":");
            Producto producto = new Producto(item[1], Double.parseDouble(item[3]), item[4], 0);
            productos.add(producto);
        }
        // una vez creado el ArrayList de objetos(Producto) creo un Adapter para adaptar ese ArrayList al spinner
        opciones.setAdapter(new ProductosAdapter(this, productos));

        //funcion onclick cuando pulse en el boton añadir llamaremos a la funcion suma()
        añadir.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               suma();
               //ver();
            }
        });
    }

    //Metodos creados por Android Studio son para ver el menu superior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void ver(){
        suma();
        Producto producto = productos.get(opciones.getSelectedItemPosition());
        //ver.setImageURI(Uri.parse(producto.img));
        //ver.setImageDrawable("img000.jpg");
        Log.i("Tag:", producto.getImg());
        //ver.setImageResource(getResources().getIdentifier(producto.getImg(), "drawable", MainActivity.this.getPackageName()));
        //Drawable icon = ResourcesCompat.getDrawable(getResources(), img000.jpg, null);
    }

    //Funcion suma
    public void suma() {
        Producto producto = productos.get(opciones.getSelectedItemPosition()); // selecciono el producto del spineer
        if (carrito.contains(producto)) { //compruebo que en el array de carrito existe el producto
            for (int i = 0; i < carrito.size(); i++) { // hago un bucle para conseguir en que posicion esta el producto
                if (carrito.get(i).nombre == producto.nombre) { //busco en que posicion esta el producto dentro del array
                    carrito.get(i).setCantidad(1); //añado +1 a cantidad de ese producto
                    lista.setAdapter(new CarritoAdapter(this, carrito)); //actualizo
                }
            }
        } else { //no existe el producto
            producto.setCantidad(1);
            carrito.add(producto); //añado el objeto al ArrayList de objetos para la lista
            //carrito.get(producto.cantidad).setCantidad(1); // añado +1 a la cantidad de ese objeto
            //carrito.get(producto);
            lista.setAdapter(new CarritoAdapter(this, carrito)); // actulizo el adapter de la listacarrito
        }

        //Ahora calculamos el precio de todos los productos seleccionados
        for (int i = 0; i < carrito.size(); i++) {
            preciofinal += (carrito.get(i).precio * carrito.get(i).cantidad);
            cont += carrito.get(i).cantidad;
        }

        //Mostramos el precio final y la cantidad de todos los productos sumados
       resultado.setText(String.valueOf("Precio: " + String.format("%.2f", preciofinal)+ "€ (" + cont + " Productos)"));
        preciofinal=0;
        cont=0;
    }

    //Funcion resta
    public void restar(Producto producto){
        if(producto.cantidad>1){
            producto.cantidad = producto.cantidad-1;
        }else{
            carrito.remove(producto);
            producto.delCantidad(1);
        }
        lista.setAdapter(new CarritoAdapter(this, carrito));
        for (int i = 0; i < carrito.size(); i++) {
            preciofinal += (carrito.get(i).precio * carrito.get(i).cantidad);
            cont += carrito.get(i).cantidad;
        }
        resultado.setText(String.valueOf("Precio: " + String.format("%.2f", preciofinal) + "€ (" + cont + " Productos)"));
        preciofinal=0;
        cont=0;
    }

    /****** ADAPTERS *******/

    //Adapter Spinner
    private class ProductosAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<Producto> productos;

        //Constructor
        ProductosAdapter(Context context, ArrayList<Producto> productos) {
            this.context = context;
            this.productos = productos;
        }

        //Funciones obligatorias de la clase Adapter
        @Override
        public int getCount() {
            return productos.size();
        }
        @Override
        public Object getItem(int position) {
            return productos.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //crea un producto
            Producto producto = productos.get(position);

            //optimiza scrool
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //inyecto el objeo en la plantilla voy a trabajar
            View rowView = inflater.inflate(R.layout.spinner, parent, false);

            //almacenamos la plantilla
            rowView.setTag(producto);
            TextView text1 = (TextView) rowView.findViewById(R.id.spinner_producto);
            TextView text2 = (TextView) rowView.findViewById(R.id.spinner_precio);
            text1.setText(producto.nombre);
            text2.setText(String.valueOf(producto.precio));
            return rowView;
        }
    }

    // Adapter lista compra
    private class CarritoAdapter extends BaseAdapter{
        private Context context;
        private ArrayList<Producto> productos;

        CarritoAdapter(Context context, ArrayList<Producto> productos) {
            this.context = context;
            this.productos = productos;
        }
        @Override
        public int getCount() {
            return productos.size();
        }
        @Override
        public Object getItem(int position) {
            return productos.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //Averiguo que objeto de producto he elegido
            Producto producto = productos.get(position);

            //Inflador que nos servira para ir añadiendo elementos a nuestra vista
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //Indico en que vista del layout (plantilla) voy a trabjar
            View rowView = inflater.inflate(R.layout.plantilla, parent, false);

            //Buscamos los elementos de la vista
            TextView text1 = (TextView) rowView.findViewById(R.id.plantilla_producto);
            TextView text2 = (TextView) rowView.findViewById(R.id.plantilla_cantidad);
            ImageView img = (ImageView) rowView.findViewById(R.id.plantilla_imagen);
            ImageButton borrar = (ImageButton) rowView.findViewById(R.id.plantilla_eliminar);

            //Añado los datos a los elementos
            text1.setText(producto.nombre);
            text2.setText(String.valueOf(producto.cantidad) + " x " + String.valueOf(producto.precio));

           img.setImageResource(getResources().getIdentifier(producto.getImg(), "drawable", MainActivity.this.getPackageName()));

            //Log.i("Imagen Producto:", String.valueOf(img));

            //Asocio ese contendio al boton de la vista
            //rowView.setTag(producto);borrar.setTago(rowView); ???? Aclarar
            borrar.setTag(producto);

            //Cuando pulse en el boton del producto indicado me ejecutar la funcion restar()
            borrar.setOnClickListener(new View.OnClickListener(){
               @Override
               public void onClick(View v) {
                   restar((Producto) v.getTag());
               }
            });

            //devolvemos la vista ya creada a nuestra plantilla principal (activity_main)
            return rowView;
        }
    }
}