package com.mycompany.ejemplotcp;
public interface iProcesador {
    /**
     * @param ipRemitente La IP de quien envió el mensaje
     * @param mensaje El mensaje de texto recibido.
     * @return Una respuesta para el remitente.
     */
    String procesar(String ipRemitente, String mensaje);
}