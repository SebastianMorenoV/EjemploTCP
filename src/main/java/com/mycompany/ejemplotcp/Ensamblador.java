package com.mycompany.ejemplotcp;

import java.io.IOException;

/**
 * El "Gerente" o "Inyector de Dependencias".
 * Es la única clase que sabe cómo "construir" un sistema completo.
 * Conecta las implementaciones de Lógica (El "Qué") con las de Transporte (El "Cómo").
 */
public class Ensamblador {

    // --- Componentes ---
    private iDespachador despachador; // El "Repartidor"
    private iListener listener;      // El "Mesero"

    /**
     * Configura esta instancia del Ensamblador para que funcione como un 
     * SERVIDOR BLACKBOARD CENTRAL.
     */
    public void configurarComoServidor() {
        System.out.println("[Ensamblador] Configurando como SERVIDOR...");

        // 1. Crear el "Repartidor" (Transporte de Envío)
        this.despachador = new ClienteTCP(); 
        
        // 2. Crear el "Chef" (Lógica de Servidor) y darle el Repartidor
        iProcesador logicaServidor = new ServidorBlackBoard(this.despachador);
        
        // 3. Crear el "Mesero" (Transporte de Escucha) y darle la Lógica
        this.listener = new ServerTCP(logicaServidor);
        
        System.out.println("[Ensamblador] Configuración de SERVIDOR lista.");
    }
    
    /**
     * Configura esta instancia del Ensamblador para que funcione como un 
     * CLIENTE INDIVIDUAL.
     * @param miId El ID de este cliente (ej. "Cliente-A")
     */
    public void configurarComoCliente(String miId) {
        System.out.println("[Ensamblador] Configurando como CLIENTE (" + miId + ")...");
        
        // 1. Crear el "Repartidor" (para hablar con el servidor)
        this.despachador = new ClienteTCP();
        
        // 2. Crear la "Lógica de Cliente" (para recibir broadcasts)
        iProcesador logicaCliente = new ProcesadorCliente(miId);
        
        // 3. Crear el "Mesero" (para escuchar broadcasts del servidor)
        //    Se le inyecta la lógica del cliente.
        this.listener = new ServerTCP(logicaCliente);
        
        System.out.println("[Ensamblador] Configuración de CLIENTE lista.");
    }

    // --- Métodos de Acción ---

    /**
     * Usa el DESPACHADOR configurado para enviar un mensaje.
     * (Usado por el Cliente para hablar con el Servidor).
     */
    public void enviar(String host, int puerto, String mensaje) throws IOException {
        if (this.despachador == null) {
            throw new IllegalStateException("El ensamblador no tiene un despachador configurado.");
        }
        this.despachador.enviar(host, puerto, mensaje);
    }

    /**
     * Usa el LISTENER configurado para empezar a escuchar.
     * (Usado por el Servidor y por el Cliente).
     */
    public void iniciarListener(int puerto) throws IOException {
        if (this.listener == null) {
            throw new IllegalStateException("El ensamblador no tiene un listener configurado.");
        }
        this.listener.iniciar(puerto); 
    }
}