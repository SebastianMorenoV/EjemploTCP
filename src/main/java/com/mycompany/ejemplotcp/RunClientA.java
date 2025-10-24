package com.mycompany.ejemplotcp;

import java.io.IOException;
import java.util.Scanner;

/**
 * Punto de entrada para ejecutar un CLIENTE (Ej. Cliente A).
 */
public class RunClientA {

    // --- Configuración ---
    private static final String SERVIDOR_IP = "localhost"; // O la IP del servidor
    private static final int SERVIDOR_PUERTO = 8000;
    
    private static final String MI_ID = "Cliente-A";
    private static final int MI_PUERTO_DE_ESCUCHA = 9001; // Puerto donde ESTE cliente escucha
    // ---------------------

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Iniciando " + MI_ID + "...");
        
        // 1. Crear el ensamblador
        Ensamblador ensamblador = new Ensamblador();
        
        // 2. Configurarlo como CLIENTE
        ensamblador.configurarComoCliente(MI_ID);

        // 3. Iniciar el LISTENER del cliente en un HILO NUEVO
        //    (Esto es para recibir los broadcasts del servidor)
        new Thread(() -> {
            try {
                System.out.println(MI_ID + " iniciando su listener en puerto " + MI_PUERTO_DE_ESCUCHA);
                ensamblador.iniciarListener(MI_PUERTO_DE_ESCUCHA);
            } catch (IOException e) {
                System.err.println(MI_ID + ": Nuestro listener falló: " + e.getMessage());
            }
        }).start();
        
        // Dar tiempo a que el listener inicie
        Thread.sleep(1000); 

        // 4. Usar el DESPACHADOR del ensamblador para HABLAR con el servidor
        try (Scanner scanner = new Scanner(System.in)) {
            // 4a. Registrarse con el servidor
            System.out.println(MI_ID + ": Registrando con el servidor en " + SERVIDOR_IP + "...");
            String msgRegistro = MI_ID + ":REGISTRAR:" + MI_PUERTO_DE_ESCUCHA;
            ensamblador.enviar(SERVIDOR_IP, SERVIDOR_PUERTO, msgRegistro);
            
            // 4b. Esperar para simular un movimiento
            System.out.println("\n*** " + MI_ID + " registrado. Presiona ENTER para simular un movimiento... ***");
            scanner.nextLine();
            
            // 4c. Enviar un movimiento
            System.out.println(MI_ID + ": Enviando movimiento al servidor...");
            String msgMovimiento = MI_ID + ":MOVER:A4"; // Ejemplo de payload
            ensamblador.enviar(SERVIDOR_IP, SERVIDOR_PUERTO, msgMovimiento);
            
            System.out.println(MI_ID + ": Movimiento enviado. El programa seguirá escuchando actualizaciones.");
            System.out.println("(Puedes lanzar RunClientB ahora. Presiona Ctrl+C para salir)");

        } catch (IOException e) {
            System.err.println(MI_ID + ": Error al contactar al servidor: " + e.getMessage());
        }
    }
}