package com.example.guessflag;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Clase principal de la aplicacion. Contiene la logica de funcionamiento de una partida y el estado
 * general de partida iniciada o no.
 *
 * @author Jose Javier Bailón Ortiz
 */
public class Aplicacion extends Application {


    /**
     * Array de id/resource banderas
     */
    TypedArray banderas;
    /**
     * Array de nombres de paises
     */
    String[] paises;

    /**
     * Determina si la partida esta empezada o no
     */
    boolean partidaEmpezada;

    /**
     * Almacena el pais correcto para la pregunta actual
     */
    int correcto;

    /**
     * Almacena la id de los paises de la pregunta actual
     * indice 0 bandera de btn1, indice 1   bandera de btn2, indice  bandera de 2 btn3
     */
    ArrayList<Integer> idPaisesEnBotones;

    /**
     * Almacena los paises acertados
     */
    ArrayList<Integer> acertados;
    /**
     * Almacena el nivel maximo que se ha alcanzado
     */
    private int record;


    @Override
    public void onCreate() {
        super.onCreate();
        //inicializar los atributos
        iniciarAtributos();
    }

    /**
     * Inicializa los valores de los atributos
     */
    private void iniciarAtributos() {
        //Recoger banderas y paises desde los recursos
        //Los paises y sus banderas se almacenan en los recursos en res/values/paises.xml
        //donde hay especificados dos arrays: uno con el nombre y otro con la referencia a la imagen de su bandera.
        //El identificador de los paises en la logica de la partida es el indice del pais dentro
        //de estos arrays
        this.banderas = getResources().obtainTypedArray(R.array.banderas);
        this.paises = getResources().getStringArray(R.array.paises);

        //inicializa el almacen de las id de paises para usar en los botones
        this.idPaisesEnBotones = new ArrayList<Integer>();

        //inicializa el almacen de las id de paises acertados
        this.acertados = new ArrayList<Integer>();

        //inicializacion del estado de partida
        this.partidaEmpezada=false;

        //inicializa el pais correcto actual
        this.correcto = -1;

        //inicializa el nivel maximo alcanzado
        this.record =0;
    }


    /**
     * Gestiona el inicio de una partida
     */
    public void iniciarPartida() {
        //borrar los listados de acertados y paises de la pregunta actual
        this.acertados.clear();
        this.idPaisesEnBotones.clear();
        //elije los paises para la pregunta actual
        cambiarPais();
        //marca la partida como empezada
        this.partidaEmpezada=true;
    }


    /**
     * Define el nuevo pais a acertar y los paises cuyas banderas componen la proxima pregunta
     */
    @SuppressLint("ResourceType")
    private void cambiarPais() {
        //recoger pais a acertar(-1,-1 para incluir a todos los que no han sido ya acertados)
        this.correcto=nuevoPais(-1,-1);

        //recoger primer pais incorrecto(evita tambien el correcto seleccionado)
        int incorrecto1=nuevoPais(this.correcto,-1);

        //recoger el segundo pais incorrecto (evita el correcto seleccionado y el incorrecto1)
        int incorrecto2=nuevoPais(this.correcto,incorrecto1);

        //Mezcla al azar las posiciones de los paises
        this.idPaisesEnBotones = new ArrayList<Integer>();
        this.idPaisesEnBotones.add(this.correcto);
        this.idPaisesEnBotones.add(incorrecto1);
        this.idPaisesEnBotones.add(incorrecto2);
        Collections.shuffle(this.idPaisesEnBotones,new Random());
    }


    /**
     * Devuelve un pais elegido al azar que no aparezca entre los acertados y que no sea ninguno de
     * los paises pasados en los  parametros de invalidos para la seleccion
     * @param invalido1 Id del pais 1 invalido para la seleccion
     * @param invalido2 Id del pais 2 invalido para la seleccion
     * @return el pais seleccionado
     */
    private int nuevoPais(int invalido1,int invalido2) {
        Random r = new Random();
        boolean encontrado=false;
        int candidato=-1;
        while(!encontrado){
            candidato=r.nextInt(this.paises.length);
            if (!this.acertados.contains(candidato)&&candidato!=invalido1 && candidato!=invalido2){
                encontrado=true;
            }
        }
        return candidato;
    }


    /**
     * Gestiona una contestacion a la pregunta actual
     * @param i Indice de la respuesta seleccionada segun lo almacenado en el array: this.idPaisesEnBotones
     * @return true si es respuesta correcta y false si es incorrecta
     */
    public boolean contestacion(int i){
        if (this.idPaisesEnBotones.get(i)==this.correcto)  {
            //agrega el pais acertado al listado de acertados
            this.acertados.add(this.correcto);
            //genera los datos para la siguiente pregunta
            cambiarPais();
            return true;
        }
        else {
            //actualizar el nivel maximo si corresponde
            if (this.record <this.acertados.size())
                this.record =this.acertados.size();
            //da la partida por finalizada
            this.partidaEmpezada=false;
            return false;
        }
    }


    /**
     * Devuelve si la partida esta empezada
     * @return True si esta empezada. False si no esta empezada
     */
    public boolean isPartidaEmpezada() {
        return partidaEmpezada;
    }

    /**
     * Devuelve el identificador del recurso de imagen de la bandera del pais con la id suministrada
     * @param i Id del pais
     * @return ID de recurso de imagen de la bandera del pais
     */
    public int getBandera(int i) {
        return banderas.getResourceId(this.idPaisesEnBotones.get(i),-1);
    }

    /**
     * Devuelve el nombre del pais correcto para la pregunta actual
     * @return El nombre del pais
     */
    public String getNombrePaisCorrecto() {
        return this.paises[this.correcto];
    }

    /**
     * Devuelve el nivel de la partida en el que se esta. Se empieza en nivel 0 y se incrementara
     * en 1 por cada bandera acertada
     *
     * @return El nivel actual
     */
    public int getNivel() {
        return this.acertados.size();
    }

    public int getRecord(){
        return this.record;
    }
}
