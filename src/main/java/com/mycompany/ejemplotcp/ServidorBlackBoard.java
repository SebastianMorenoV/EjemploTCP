package com.mycompany.ejemplotcp;


import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// --- Clase de lógica para el ServidorBlackBoard Central ---
class ServidorBlackBoard  {
   
    // --- CAMBIO AQUÍ ---
    // El "directorio" ahora guarda el ID y un objeto ClienteInfo (IP + Puerto)
    private Map<String, ClienteInfo> directorioClientes = new ConcurrentHashMap<>();
    
    private iDespachador despachador;
    
    public ServidorBlackBoard(iDespachador despachador) {
        this.despachador = despachador;
    }
    
    // --- CAMBIO AQUÍ ---
    // El método ahora recibe la IP real del remitente
    

    // --- CLASE AUXILIAR AÑADIDA ---
    // Necesitamos una clase para guardar la IP y el Puerto juntos
    private static class ClienteInfo {
        private String host;
        private int puerto;
        public ClienteInfo(String host, int puerto) { this.host = host; this.puerto = puerto; }
        public String getHost() { return host; }
        public int getPuerto() { return puerto; }
    }
}