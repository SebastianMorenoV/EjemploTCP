package com.mycompany.ejemplotcp;

import java.io.IOException;

/**
 * Contrato para el TRANSPORTE DE ENVÍO.
 * Define "cómo enviar" un mensaje a un destino.
 */
public interface iDespachador {
    /**
     * Envía un mensaje a un host y puerto específicos.
     * @param host La IP del destino.
     * @param puerto El puerto del destino.
     * @param mensaje El mensaje a enviar.
     * @throws IOException Si ocurre un error de red.
     */
    void enviar(String host, int puerto, String mensaje) throws IOException;
}