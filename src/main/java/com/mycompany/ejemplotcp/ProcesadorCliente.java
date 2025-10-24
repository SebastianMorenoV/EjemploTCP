package com.mycompany.ejemplotcp;


/**
 * Implementación de la LÓGICA DEL CLIENTE.
 * Sabe qué hacer cuando RECIBE un mensaje (ej. un broadcast del servidor).
 */
public class ProcesadorCliente implements iProcesador {

    private final String miId;

    /**
     * @param miId El ID de este cliente (ej. "Cliente-A") para los logs.
     */
    public ProcesadorCliente(String miId) {
        this.miId = miId;
    }

    /**
     * Método del contrato iProcesador.
     * Esta es la lógica del cliente al recibir un mensaje.
     */
    @Override
    public String procesar(String ipServidor, String mensaje) {
        // El cliente simplemente imprime la notificación que le llegó.
        System.out.println("\n\n*** [" + miId + " NOTIFICACIÓN RECIBIDA] ***");
        System.out.println("   Mensaje de " + ipServidor + ": " + mensaje);
        System.out.println("*** [Fin de notificación] ***\n");
        
        // Responde "OK" al servidor que le envió el broadcast
        return "CLIENTE_RECIBIDO_OK"; 
    }
}