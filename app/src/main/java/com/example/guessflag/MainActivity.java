package com.example.guessflag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Clase de vista principal. Gestiona los eventos de pulsado de los botones y la configuracion
 * del layout dependiendo del estado de la aplicacion.
 * @author Jose Javier Bailón Ortiz
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * Referencia a la aplicacion
     */
    Aplicacion aplicacion;

    /**
     * layout con los elementos del menu inicial
     */
    ConstraintLayout layoutMenu;

    /**
     * layout con los elementos de partida
     */
    ConstraintLayout layoutPartida;


    /**
     * Boton iniciar partida
     */
    Button btnInicarPartida;


    /**
     * TextView que muestra el pais a acertar
     */
    private TextView tvPais;
    /**
     * TextView que muestra el nivel actual
     */
    private TextView tvNivel;
    /**
     * TextView que muestra el record maximo alcanzado
     */
    private TextView tvRecord;

    /**
     * Boton de bandera superior/izquierdo
     */
    ImageButton btn1;

    /**
     * Boton bandera central
     */
    ImageButton btn2;

    /**
     * Boton bandera inferior/derecho
     */
    ImageButton btn3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //recoger referencia a la aplicacion
        this.aplicacion = (Aplicacion) getApplication();
        //define el layout a mostrar
        setContentView(R.layout.activity_main);
        //inicializa los atributos de la activity
        iniciarAtributos();
        //inicializa los eventos de la activity
        inicializarEventos();
        //actualiza el estado del layout
        actualizarEstado();
    }


    /**
     * Inicializa los atributos de la clase
     */
    private void iniciarAtributos() {
        //recoger referencias a elementos de interface
        this.layoutMenu = findViewById(R.id.layoutMenu);
        this.layoutPartida = findViewById(R.id.layoutPartida);
        this.btnInicarPartida = findViewById(R.id.btnIniciaPartida);
        this.btn1 = findViewById(R.id.btn1);
        this.btn2 = findViewById(R.id.btn2);
        this.btn3 = findViewById(R.id.btn3);
        this.tvPais = findViewById(R.id.tvPais);
        this.tvNivel = findViewById(R.id.tvNivel);
        this.tvRecord = findViewById(R.id.tvRecord);
    }


    /**
     * Inicializa los eventos de la activity
     */
    private void inicializarEventos() {
        //onclick de botones
        this.btnInicarPartida.setOnClickListener(this);
        this.btn1.setOnClickListener(this);
        this.btn2.setOnClickListener(this);
        this.btn3.setOnClickListener(this);
    }

    /**
     * Actualiza el estado del layout en funcion de si la partida esta empezada o no
     */
    private void actualizarEstado() {
        if (this.aplicacion.isPartidaEmpezada()) {
            mostrarLayoutPartida();
        } else {
            mostrarLayoutMenu();
        }

    }

    /**
     * Configura el layout en modo partida
     */
    private void mostrarLayoutPartida() {
        //activar layout de partida y ocultar el layout de menu
        this.layoutPartida.setVisibility(View.VISIBLE);
        this.layoutMenu.setVisibility(View.GONE);
        //actualizar imagenes de botones
        this.btn1.setImageResource(this.aplicacion.getBandera(0));
        this.btn2.setImageResource(this.aplicacion.getBandera(1));
        this.btn3.setImageResource(this.aplicacion.getBandera(2));
        //actualizar etiqueta de pais a acertar
        this.tvPais.setText(this.aplicacion.getNombrePaisCorrecto());
        //actualizar etiquetas de nivel y record
        this.tvNivel.setText("Nivel: " + this.aplicacion.getNivel());
        this.tvRecord.setText("Record: " + this.aplicacion.getRecord());

    }

    /**
     * Configura el layout en modo menu
     */
    private void mostrarLayoutMenu() {
        //activar layout de menu y ocultar el de partida
        this.layoutPartida.setVisibility(View.GONE);
        this.layoutMenu.setVisibility(View.VISIBLE);
    }


    /**
     * Gestiona el pulsado de botones
     * @param view Vista("boton") que ha generado el evento
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnIniciaPartida) {
            this.aplicacion.iniciarPartida();
            this.actualizarEstado();
        } else if (view.getId() == R.id.btn1)
            gestionarContestacion(0);
        else if (view.getId() == R.id.btn2)
            gestionarContestacion(1);
        else if (view.getId() == R.id.btn3)
            gestionarContestacion(2);
    }

    /**
     * Gestiona la contestacion a la pregunta
     * @param i indice de la respuesta elegida
     */
    private void gestionarContestacion(int i) {
        //pasar a la aplicacion la contestacion y recoger el resultado
        boolean acierto = this.aplicacion.contestacion(i);
        //Muestra el mensaje segun se acierte o no y actualiza el estado del layout
        if (acierto) {
            Toast.makeText(this, "¡Acertaste!", Toast.LENGTH_SHORT).show();
            actualizarEstado();
        } else {
            toast("¡GAME OVER! Acertaste "+this.aplicacion.getNivel()+" banderas. El record está en "+this.aplicacion.getRecord()+" ¡A estudiar toca!");
            actualizarEstado();
        }
    }

    /**
     * Muestra un toast con el texto pasado
     * @param texto Texto a mostrar en el toast
     */
    private void toast(String texto) {
        Toast t = Toast.makeText(this, texto, Toast.LENGTH_LONG);
        t.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
        t.show();
    }
}

