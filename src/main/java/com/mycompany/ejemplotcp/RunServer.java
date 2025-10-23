package com.mycompany.ejemplotcp;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


// --- Main del ServidorBlackBoard ---
public class RunServer {
    public static void main(String[] args) {
        int PUERTO_SERVIDOR = 8000;
        
        System.out.println("Iniciando Servidor Central...");
        
        Ensamblador ensamblador = new Ensamblador();
        
        // El servidor también necesita un despachador para "hablarle" a los clientes
        iDespachador despachadorParaServidor = ensamblador.crearDespachador();
        
        // 1. Crear la lógica del servidor
        iProcesador logicaServidor = new ServidorBlackBoard(despachadorParaServidor);
        
        // 2. Crear el listener del servidor
        ServerTCP servidorCentral = ensamblador.crearListener(PUERTO_SERVIDOR, logicaServidor);
        
        // 3. Iniciar el servidor (esto es un bucle infinito)
        try {
            servidorCentral.iniciar();
        } catch (IOException e) {
            System.err.println("El servidor principal falló: " + e.getMessage());
        }
    }
}