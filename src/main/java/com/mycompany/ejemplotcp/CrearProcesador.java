/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ejemplotcp;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Moreno
 */
public class CrearProcesador {

    iProcesador procesador;

    public CrearProcesador() {
    }

    public String procesar(String ipCliente, String mensaje) {
        return procesador.procesar(ipCliente, mensaje);
    }

    public void iniciarServidor(int puerto) throws IOException {

        procesador.iniciarServidor(puerto);

    }

    public iProcesador getProcesador() {
        return procesador;
    }

    public void setProcesador(iProcesador procesador) {
        this.procesador = procesador;
    }

}
