package com.mycompany.ejemplotcp;


import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// --- Clase de lógica para el Servidor Central ---
class ProcesadorServidor implements iProcesador {
    
    // --- CAMBIO AQUÍ ---
    // El "directorio" ahora guarda el ID y un objeto ClienteInfo (IP + Puerto)
    private Map<String, ClienteInfo> directorioClientes = new ConcurrentHashMap<>();
    
    private iDespachador despachador;
    
    public ProcesadorServidor(iDespachador despachador) {
        this.despachador = despachador;
    }
    
    // --- CAMBIO AQUÍ ---
    // El método ahora recibe la IP real del remitente
    @Override
    public String procesar(String ipCliente, String mensaje) {
        
        String[] partes = mensaje.split(":");
        String idCliente = partes[0];
        String comando = partes[1];
        String payload = (partes.length > 2) ? partes[2] : "";
        
        // --- CAMBIO AQUÍ ---
        // **BORRAMOS la línea 'String ipCliente = "localhost";'**

        if (comando.equals("REGISTRAR")) {
            int puertoCliente = Integer.parseInt(payload);
            
            // --- CAMBIO AQUÍ ---
            // Guardamos la IP y Puerto reales en el directorio
            directorioClientes.put(idCliente, new ClienteInfo(ipCliente, puertoCliente));
            System.out.println("[Servidor] Cliente " + idCliente + " registrado en **" + ipCliente + ":" + puertoCliente + "**");
            return "REGISTRADO";
        }
        
        // Simulación del juego
        if (comando.equals("MOVER")) {
            System.out.println("[Servidor] " + idCliente + " (" + ipCliente + ") hizo un movimiento. Notificando a todos...");
            
            // Reenviar a todos los demás clientes (Broadcast)
            // --- CAMBIO AQUÍ ---
            for (Map.Entry<String, ClienteInfo> entry : directorioClientes.entrySet()) {
                
                if (entry.getKey().equals(idCliente)) {
                    continue; 
                }
                
                String idDestino = entry.getKey();
                // --- CAMBIO AQUÍ ---
                // Obtenemos la IP y Puerto del destinatario
                ClienteInfo infoDestino = entry.getValue();
                String ipDestino = infoDestino.getHost();
                int puertoDestino = infoDestino.getPuerto();
                
                String msgUpdate = "[Servidor] Actualización: " + idCliente + " se movió.";

                try {
                    new Thread(() -> {
                        try {
                            // --- CAMBIO CRÍTICO AQUÍ ---
                            // Enviamos a la IP y Puerto del DESTINO
                            despachador.enviar(ipDestino, puertoDestino, msgUpdate);
                        } catch (IOException e) {
                            System.err.println("[Servidor] Error al notificar a " + idDestino);
                        }
                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return "MOVIMIENTO_RECIBIDO";
        }
        
        return "COMANDO_DESCONOCIDO";
    }

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