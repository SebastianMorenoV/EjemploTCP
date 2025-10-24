/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ejemplotcp;

import java.io.IOException;

/**
 *
 * @author Sebastian Moreno
 */
public class CrearDespachador {

    iDespachador despachador;

    public CrearDespachador() {
    }

    public void enviar(String host, int puerto, String mensaje) throws IOException {
        despachador.enviar(host, puerto, mensaje);
    }

    public iDespachador getDespachador() {
        return despachador;
    }

    public void setDespachador(iDespachador despachador) {
        this.despachador = despachador;
    }

}
