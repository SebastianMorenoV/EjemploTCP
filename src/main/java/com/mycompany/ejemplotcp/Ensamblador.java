package com.mycompany.ejemplotcp;
public class Ensamblador {

    /**
     * Crea un objeto capaz de enviar mensajes intermitentes.
     */
    public iDespachador crearDespachador() {
        return new ClienteTCP();
    }

    /**
     * Crea un objeto capaz de escuchar y procesar mensajes.
     * @param puerto El puerto en el que escuchará
     * @param logicaDeNegocio La implementación de qué hacer con los mensajes
     */
    public ServerTCP crearListener(int puerto, iProcesador logicaDeNegocio) {
        return new ServerTCP(puerto, logicaDeNegocio);
    }
}