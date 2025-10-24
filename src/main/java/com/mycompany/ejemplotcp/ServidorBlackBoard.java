package com.mycompany.ejemplotcp;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementación de la LÓGICA DEL SERVIDOR (El "Chef"). Sabe qué hacer con los
 * mensajes "REGISTRAR" y "MOVER". Depende de un iDespachador para poder hacer
 * broadcast.
 */
public class ServidorBlackBoard implements iProcesador {

    // Directorio de clientes conectados (ID -> Info)
    private final Map<String, ClienteInfo> directorioClientes = new ConcurrentHashMap<>();

    // El "Repartidor" que usará para enviar broadcasts
    private final iDespachador despachador;

    /**
     * Constructor que inyecta la dependencia del despachador.
     *
     * @param despachador El objeto (ClienteTCP) que sabe cómo enviar mensajes.
     */
    public ServidorBlackBoard(iDespachador despachador) {
        if (despachador == null) {
            throw new IllegalArgumentException("El despachador no puede ser nulo");
        }
        this.despachador = despachador;
    }

    /**
     * Método del contrato iProcesador. Esta es la lógica principal del
     * servidor.
     */
    @Override
    public String procesar(String ipCliente, String mensaje) {
        String[] partes = mensaje.split(":");
        if (partes.length < 2) {
            return "ERROR: Mensaje malformado";
        }

        String idCliente = partes[0];
        String comando = partes[1];
        String payload = (partes.length > 2) ? partes[2] : "";

        if (comando.equals("REGISTRAR")) {
            try {
                int puertoCliente = Integer.parseInt(payload);
                // Guardamos la IP y Puerto reales del remitente
                directorioClientes.put(idCliente, new ClienteInfo(ipCliente, puertoCliente));
                System.out.println("[BlackBoard] Cliente " + idCliente + " registrado en **" + ipCliente + ":" + puertoCliente + "**");
                return "REGISTRADO_OK";
            } catch (NumberFormatException e) {
                return "ERROR: Puerto inválido";
            }
        }

        if (comando.equals("MOVER")) {
            System.out.println("[BlackBoard] " + idCliente + " (" + ipCliente + ") se movió. Notificando a todos...");

            // Lógica de Broadcast: Usar el despachador inyectado
            String msgUpdate = "[Servidor] Actualización: " + idCliente + " se movió.";

            for (Map.Entry<String, ClienteInfo> entry : directorioClientes.entrySet()) {
                if (entry.getKey().equals(idCliente)) {
                    continue; // No notificar a sí mismo
                }

                String idDestino = entry.getKey();
                ClienteInfo infoDestino = entry.getValue();

                // Usamos un hilo nuevo para no bloquear el bucle principal del servidor
                new Thread(() -> {
                    try {
                        System.out.println("[BlackBoard] Reenviando a " + idDestino + " en " + infoDestino.getHost() + ":" + infoDestino.getPuerto());
                        // El Blackboard (Lógica) usa el Despachador (Transporte)
                        this.despachador.enviar(infoDestino.getHost(), infoDestino.getPuerto(), msgUpdate);
                    } catch (IOException e) {
                        System.err.println("[BlackBoard] Error al notificar a " + idDestino + ": " + e.getMessage());
                    }
                }).start();
            }
            return "MOVIMIENTO_RECIBIDO_OK";
        }

        return "COMANDO_DESCONOCIDO";
    }

    /**
     * Clase interna para almacenar la información de conexión de un cliente.
     */
    private static class ClienteInfo {

        private final String host;
        private final int puerto;

        public ClienteInfo(String host, int puerto) {
            this.host = host;
            this.puerto = puerto;
        }

        public String getHost() {
            return host;
        }

        public int getPuerto() {
            return puerto;
        }
    }
}
