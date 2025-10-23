package com.mycompany.ejemplotcp;
import java.io.*;
import java.net.Socket;

public class DespachadorIntermitente implements iDespachador {

    @Override
    public void enviar(String host, int puerto, String mensaje) throws IOException {
        System.out.println("[Despachador] Conectando a " + host + ":" + puerto + " para enviar...");
        Socket socket = null;
        DataOutputStream out = null;
        DataInputStream in = null;
        
        try {
            // 1. Conectar
            socket = new Socket(host, puerto);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

            // 2. Enviar mensaje
            out.writeUTF(mensaje);
            System.out.println("[Despachador] Enviado -> " + mensaje);
            
            // 3. Recibir confirmación/respuesta
            String respuesta = in.readUTF();
            System.out.println("[Despachador] Recibido <- " + respuesta);

        } finally {
            // 4. Desconectar (INTERMITENTE)
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
            System.out.println("[Despachador] Conexión cerrada.");
        }
    }
}