package com.mycompany.ejemplotcp;
import java.io.IOException;

/**
 * Define el contrato para ENVIAR un mensaje de forma intermitente.
 * Se conecta, envía y se desconecta.
 */
public interface iDespachador {
    
    /**
     * Envía un mensaje a un destino específico.
     * @param host El host del destinatario (IP)
     * @param puerto El puerto del destinatario
     * @param mensaje El mensaje a enviar
     * @throws IOException Si ocurre un error de red
     */
    void enviar(String host, int puerto, String mensaje) throws IOException;
}