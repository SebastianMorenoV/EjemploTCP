package com.mycompany.ejemplotcp;
import java.io.IOException;
import java.util.Scanner;

// --- Clase de Lógica del Cliente ---
// (Puedes poner la clase ProcesadorCliente aquí también,
// o si están en el mismo paquete, simplemente usarla)
// Para que sea autocontenido, la pegamos de nuevo:
class ProcesadorClienteB implements iProcesador {
    @Override
    public String procesar(String ipRemitente, String mensaje) {
        // Esta lógica es solo para cuando el Servidor nos habla
        System.out.println("\n=== [CLIENTE] ACTUALIZACIÓN RECIBIDA DE " + ipRemitente + " ===");
        System.out.println("    MENSAJE: " + mensaje);
        System.out.println("====================================================\n");
        return "ACTUALIZACION_OK"; // Devuelve una confirmación
    }
}

// --- Main del Cliente B ---
public class RunClientB {

    // ❗️❗️❗️ ATENCIÓN ❗️❗️❗️
    // ❗️❗️❗️ CAMBIA ESTA IP POR LA IP DE LA LAPTOP SERVIDORA ❗️❗️❗️
    private static final String SERVIDOR_IP = "192.168.100.3"; // O "localhost" si pruebas en la misma PC
    
    private static final int SERVIDOR_PUERTO = 8000;
    
    private static final String MI_ID = "Cliente-B";
    private static final int MI_PUERTO_DE_ESCUCHA = 9002; // Puerto diferente
    

    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println("Iniciando " + MI_ID + "...");
        Ensamblador ensamblador = new Ensamblador();

        // 1. Iniciar NUESTRO PROPIO listener (en puerto 9002) en un hilo separado
        iProcesador logicaCliente = new ProcesadorClienteB();
        ServerTCP listenerCliente = ensamblador.crearListener(MI_PUERTO_DE_ESCUCHA, logicaCliente);
        
        new Thread(() -> {
            try {
                listenerCliente.iniciar();
            } catch (IOException e) {
                System.err.println(MI_ID + ": Nuestro listener falló: " + e.getMessage());
            }
        }).start();
        
        System.out.println(MI_ID + " escuchando en puerto " + MI_PUERTO_DE_ESCUCHA);
        Thread.sleep(1000); // Dar tiempo a que el listener inicie

        // 2. Crear un despachador para HABLAR con el servidor
        iDespachador despachador = ensamblador.crearDespachador();
        
        try {
            // 3. Registrarnos con el Servidor Central
            System.out.println(MI_ID + ": Registrando con el servidor en " + SERVIDOR_IP + "...");
            String msgRegistro = MI_ID + ":REGISTRAR:" + MI_PUERTO_DE_ESCUCHA;
            despachador.enviar(SERVIDOR_IP, SERVIDOR_PUERTO, msgRegistro);
            
            System.out.println("\n*** " + MI_ID + " registrado. Esperando actualizaciones de otros jugadores... ***");
            System.out.println("(Presiona Ctrl+C para salir)");

        } catch (IOException e) {
            System.err.println(MI_ID + ": Error al contactar al servidor: " + e.getMessage());
        }
    }
}