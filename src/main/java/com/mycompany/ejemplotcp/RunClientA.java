package com.mycompany.ejemplotcp;
import java.io.IOException;
import java.util.Scanner;


// --- Main del Cliente A ---
public class RunClientA {

    // ❗️❗️❗️ ATENCIÓN ❗️❗️❗️
    // ❗️❗️❗️ CAMBIA ESTA IP POR LA IP DE LA LAPTOP SERVIDORA ❗️❗️❗️
    private static final String SERVIDOR_IP = "192.168.100.3"; // O "localhost" si pruebas en la misma PC
    
    private static final int SERVIDOR_PUERTO = 8000;
    
    private static final String MI_ID = "Cliente-A";
    private static final int MI_PUERTO_DE_ESCUCHA = 9001;
    

    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println("Iniciando " + MI_ID + "...");
        Ensamblador ensamblador = new Ensamblador();

        new Thread(() -> {
            try {
                ensamblador.iniciarServidor(MI_PUERTO_DE_ESCUCHA);
            } catch (IOException e) {
                System.err.println(MI_ID + ": Nuestro listener falló: " + e.getMessage());
            }
        }).start();
        
        System.out.println(MI_ID + " escuchando en puerto " + MI_PUERTO_DE_ESCUCHA);
        Thread.sleep(1000); // Dar tiempo a que el listener inicie

        // 2. Crear un despachador para HABLAR con el servidor
      
        
        try {
            // 3. Registrarnos con el Servidor Central
            System.out.println(MI_ID + ": Registrando con el servidor en " + SERVIDOR_IP + "...");
            String msgRegistro = MI_ID + ":REGISTRAR:" + MI_PUERTO_DE_ESCUCHA;
            ensamblador.enviar(SERVIDOR_IP, SERVIDOR_PUERTO, msgRegistro);
            
            System.out.println("\n*** " + MI_ID + " registrado. Presiona ENTER para simular un movimiento... ***");
            
            // 4. Esperar a que el usuario presione Enter
            new Scanner(System.in).nextLine();
            
            // 5. Enviar un movimiento
            System.out.println(MI_ID + ": Enviando movimiento al servidor...");
            String msgMovimiento = MI_ID + ":MOVER";
            ensamblador.enviar(SERVIDOR_IP, SERVIDOR_PUERTO, msgMovimiento);
            
            System.out.println(MI_ID + ": Movimiento enviado. El programa seguirá escuchando actualizaciones.");
            System.out.println("(Presiona Ctrl+C para salir)");

        } catch (IOException e) {
            System.err.println(MI_ID + ": Error al contactar al servidor: " + e.getMessage());
        }
    }
}