package com.mycompany.ejemplotcp;

import java.io.IOException;

/**
 * Punto de entrada para ejecutar el SERVIDOR CENTRAL.
 */
public class RunServer {
    public static void main(String[] args) {
        int PUERTO_SERVIDOR = 8000;
        System.out.println("Iniciando Servidor Central...");
        
        // 1. Crear el ensamblador
        Ensamblador ensamblador = new Ensamblador();
        
        // 2. Configurarlo como SERVIDOR
        ensamblador.configurarComoServidor();
        
        // 3. Iniciar su listener (esto bloquea el hilo principal)
        try {
            ensamblador.iniciarListener(PUERTO_SERVIDOR);
        } catch (IOException e) {
            System.err.println("El servidor principal falló: " + e.getMessage());
        }
    }
}