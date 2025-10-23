package com.mycompany.ejemplotcp;
public class Ensamblador {

    /**
     * Crea un objeto capaz de enviar mensajes intermitentes.
     */
    public iDespachador crearDespachador() {
        return new DespachadorIntermitente();
    }

    /**
     * Crea un objeto capaz de escuchar y procesar mensajes.
     * @param puerto El puerto en el que escuchará
     * @param logicaDeNegocio La implementación de qué hacer con los mensajes
     */
    public ListenerIntermitente crearListener(int puerto, iProcesador logicaDeNegocio) {
        return new ListenerIntermitente(puerto, logicaDeNegocio);
    }
}