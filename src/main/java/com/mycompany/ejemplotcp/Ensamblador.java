package com.mycompany.ejemplotcp;

import java.io.IOException;

public class Ensamblador {

    /**
     *
     * @param host
     * @param puerto
     * @param mensaje
     * @throws IOException
     */
    public void enviar(String host, int puerto, String mensaje) throws IOException {
        CrearDespachador despachador = new CrearDespachador();
        ClienteTCP clienteTCP = new ClienteTCP();
        despachador.setDespachador(clienteTCP);
        despachador.enviar(host, puerto, mensaje);
    }

    public String procesar(String ipCliente, String mensaje) {
        CrearProcesador procesador = new CrearProcesador();
        ServerTCP serverTCP = new ServerTCP();
        procesador.setProcesador(serverTCP);
        return procesador.procesar(ipCliente, mensaje);
    }

 

    public void iniciarServidor(int puerto) throws IOException {
        CrearProcesador procesador = new CrearProcesador();
        ServerTCP serverTCP = new ServerTCP();
        procesador.setProcesador(serverTCP);
        procesador.iniciarServidor(puerto);
    }

    
}
