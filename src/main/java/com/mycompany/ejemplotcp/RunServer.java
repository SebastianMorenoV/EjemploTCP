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
        // 3. Iniciar el servidor (esto es un bucle infinito)
        try {
            ensamblador.iniciarServidor(PUERTO_SERVIDOR);
        } catch (IOException e) {
            System.err.println("El servidor principal falló: " + e.getMessage());
        }
    }
}