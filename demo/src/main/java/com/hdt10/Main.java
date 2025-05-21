/*
 * Universidad del Valle de Guatemala
 * Algoritmos y Estructuras de Datos
 * Ing. Douglas Barrios
 * Creación: 17/05/2025
 * última modificación: 18/05/2025
 * File Name: Main.java
 * Descripción: Clase principal que implementa la interfaz del usuario
 *              y coordina las operaciones del grafo.
 */

package com.hdt10;

import java.io.File;
import java.util.Scanner;
import java.util.Set;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Grafo grafo;
    
    public static void main(String[] args) {
        System.out.println("----- Sistema de Rutas entre Ciudades -----");
        
        // Inicializar el grafo con una capacidad inicial de 20 ciudades
        grafo = new Grafo(20);
        
        // Cargar las conexiones desde el archivo
        System.out.print("Ingrese la ruta del archivo de conexiones (por defecto 'conexiones.txt'): ");
        String rutaArchivo = scanner.nextLine().trim();
        
        if (rutaArchivo.isEmpty()) {
            rutaArchivo = "conexiones.txt";
        }
        
        System.out.println("Buscando archivo en: " + rutaArchivo);
        
        // Verificar si el archivo existe, de lo contrario se crea uno de ejemplo
        File archivo = new File(rutaArchivo);
        if (!archivo.exists() || !archivo.isFile()) {
            System.out.println("No se encontró el archivo. Se creará un archivo de ejemplo.");
            if (!CreadorArchivo.crearArchivoEjemplo(rutaArchivo)) {
                System.out.println("Error al crear el archivo de ejemplo. El programa finalizará.");
                return;
            }
        }
        
        if (LectorArchivo.cargarConexiones(rutaArchivo, grafo)) {
            System.out.println("Conexiones cargadas exitosamente.");
            
            // Mostrar la matriz de adyacencia inicial
            grafo.imprimirMatriz();
            
            // Mostrar el centro del grafo inicial
            System.out.println("Centro del grafo: " + grafo.obtenerCentro());
            
            // Iniciar el menú principal
            menuPrincipal();
        } else {
            System.out.println("No se pudieron cargar las conexiones. El programa finalizará.");
        }
    }
    
    private static void menuPrincipal() {
        int opcion;
        
        do {
            System.out.println("\n----- Menú Principal -----");
            System.out.println("1. Consultar ruta entre dos ciudades");
            System.out.println("2. Consultar centro del grafo");
            System.out.println("3. Modificar grafo");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");
            
            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                opcion = 0;
            }
            
            switch (opcion) {
                case 1:
                    consultarRuta();
                    break;
                case 2:
                    consultarCentro();
                    break;
                case 3:
                    modificarGrafo();
                    break;
                case 4:
                    System.out.println("Gracias por utilizar el sistema. ¡Hasta pronto!");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
            
        } while (opcion != 4);
    }
    
    private static void consultarRuta() {
        System.out.println("\n----- Consulta de Ruta -----");
        
        // Mostrar ciudades disponibles
        mostrarCiudadesDisponibles();
        
        System.out.print("Ciudad origen: ");
        String origen = scanner.nextLine().trim();
        
        if (!grafo.contieneCiudad(origen)) {
            System.out.println("Error: La ciudad de origen no existe en el grafo.");
            return;
        }
        
        System.out.print("Ciudad destino: ");
        String destino = scanner.nextLine().trim();
        
        if (!grafo.contieneCiudad(destino)) {
            System.out.println("Error: La ciudad de destino no existe en el grafo.");
            return;
        }
        
        Ruta ruta = grafo.obtenerRuta(origen, destino);
        
        if (ruta == null) {
            System.out.println("No existe una ruta entre " + origen + " y " + destino + ".");
        } else {
            System.out.println("\nResultado:");
            System.out.println(ruta);
        }
    }
    
    private static void consultarCentro() {
        System.out.println("\n----- Centro del Grafo -----");
        
        String centro = grafo.obtenerCentro();
        System.out.println("La ciudad que se encuentra en el centro del grafo es: " + centro);
    }
    
    private static void modificarGrafo() {
        int opcion;
        
        do {
            System.out.println("\n----- Modificar Grafo -----");
            System.out.println("1. Establecer nueva conexión entre ciudades");
            System.out.println("2. Eliminar conexión entre ciudades");
            System.out.println("3. Cambiar condición climática");
            System.out.println("4. Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            
            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                opcion = 0;
            }
            
            switch (opcion) {
                case 1:
                    establecerConexion();
                    break;
                case 2:
                    eliminarConexion();
                    break;
                case 3:
                    cambiarClima();
                    break;
                case 4:
                    // Volver al menú principal
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
            
        } while (opcion != 4);
    }
    
    private static void eliminarConexion() {
        System.out.println("\n------ Eliminar Conexión -----");
        
        // Mostrar ciudades disponibles
        mostrarCiudadesDisponibles();
        
        System.out.print("Ciudad origen: ");
        String origen = scanner.nextLine().trim();
        
        if (!grafo.contieneCiudad(origen)) {
            System.out.println("Error: La ciudad de origen no existe en el grafo.");
            return;
        }
        
        System.out.print("Ciudad destino: ");
        String destino = scanner.nextLine().trim();
        
        if (!grafo.contieneCiudad(destino)) {
            System.out.println("Error: La ciudad de destino no existe en el grafo.");
            return;
        }
        
        grafo.eliminarConexion(origen, destino);
        System.out.println("Se ha eliminado la conexión entre " + origen + " y " + destino + ".");
        
        // Recalcular rutas
        grafo.floyd();
        
        // Mostrar matriz actualizada
        grafo.imprimirMatriz();
        
        // Mostrar nuevo centro
        System.out.println("Nuevo centro del grafo: " + grafo.obtenerCentro());
    }
    
    private static void establecerConexion() {
        System.out.println("\n----- Establecer Conexión -----");
        
        // Mostrar ciudades existentes o permitir agregar nuevas
        mostrarCiudadesDisponibles();
        
        System.out.print("Ciudad origen: ");
        String origen = scanner.nextLine().trim();
        
        System.out.print("Ciudad destino: ");
        String destino = scanner.nextLine().trim();
        
        try {
            System.out.print("Tiempo en condiciones normales: ");
            double tiempoNormal = Double.parseDouble(scanner.nextLine().trim());
            
            System.out.print("Tiempo con lluvia: ");
            double tiempoLluvia = Double.parseDouble(scanner.nextLine().trim());
            
            System.out.print("Tiempo con nieve: ");
            double tiempoNieve = Double.parseDouble(scanner.nextLine().trim());
            
            System.out.print("Tiempo con tormenta: ");
            double tiempoTormenta = Double.parseDouble(scanner.nextLine().trim());
            
            // Agregar la conexión
            grafo.agregarConexion(origen, destino, tiempoNormal, tiempoLluvia, tiempoNieve, tiempoTormenta);
            System.out.println("Conexión establecida exitosamente.");
            
            // Recalcular rutas
            grafo.floyd();
            
            // Mostrar matriz actualizada
            grafo.imprimirMatriz();
            
            // Mostrar nuevo centro
            System.out.println("Nuevo centro del grafo: " + grafo.obtenerCentro());
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Los tiempos deben ser valores numéricos.");
        }
    }
    
    private static void cambiarClima() {
        System.out.println("\n----- Cambiar Condición Climática -----");
        System.out.println("Condición climática actual: " + grafo.getCondicionActual());
        System.out.println("1. NORMAL");
        System.out.println("2. LLUVIA");
        System.out.println("3. NIEVE");
        System.out.println("4. TORMENTA");
        System.out.print("Seleccione la nueva condición climática: ");
        
        try {
            int opcion = Integer.parseInt(scanner.nextLine().trim());
            Grafo.CondicionClimatica nuevaCondicion;
            
            switch (opcion) {
                case 1:
                    nuevaCondicion = Grafo.CondicionClimatica.NORMAL;
                    break;
                case 2:
                    nuevaCondicion = Grafo.CondicionClimatica.LLUVIA;
                    break;
                case 3:
                    nuevaCondicion = Grafo.CondicionClimatica.NIEVE;
                    break;
                case 4:
                    nuevaCondicion = Grafo.CondicionClimatica.TORMENTA;
                    break;
                default:
                    System.out.println("Opción no válida. Se mantiene la condición actual.");
                    return;
            }
            
            grafo.cambiarCondicionClimatica(nuevaCondicion);
            System.out.println("Condición climática cambiada a: " + nuevaCondicion);
            
            // Mostrar matriz actualizada
            grafo.imprimirMatriz();
            
            // Mostrar nuevo centro
            System.out.println("Nuevo centro del grafo: " + grafo.obtenerCentro());
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Debe ingresar un número.");
        }
    }
    
    private static void mostrarCiudadesDisponibles() {
        Set<String> ciudades = grafo.getCiudades();
        System.out.println("Ciudades disponibles: " + String.join(", ", ciudades));
    }
}