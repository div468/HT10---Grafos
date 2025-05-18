/*
 * Universidad del Valle de Guatemala
 * Algoritmos y Estructuras de Datos
 * Ing. Douglas Barrios
 * Creación: 17/05/2025
 * última modificación: 17/05/2025
 * File Name: LectorArchivo.java
 * Descripción: Clase para leer archivos de conexiones entre ciudades
 * y cargarlos en el grafo.
 */

package com.hdt10;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LectorArchivo {
    
    /**
     * Lee un archivo de conexiones y carga los datos en el grafo
     * Formato del archivo:
     * Ciudad1 Ciudad2 tiempoNormal tiempoLluvia tiempoNieve tiempoTormenta
     * 
     * @param nombreArchivo Ruta del archivo a leer
     * @param grafo Grafo donde se cargarán los datos
     * @return true si la lectura fue exitosa, false en caso contrario
     */
    public static boolean cargarConexiones(String nombreArchivo, Grafo grafo) {
        File archivo = new File(nombreArchivo);
        if (!archivo.exists() || !archivo.isFile()) {
            System.out.println("Error: El archivo " + nombreArchivo + " no existe o no es un archivo válido.");
            return false;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            int numeroLinea = 0;
            
            while ((linea = br.readLine()) != null) {
                numeroLinea++;
                // Omitir líneas vacías o comentarios
                if (linea.trim().isEmpty() || linea.trim().startsWith("#")) {
                    continue;
                }
                
                // Dividir la línea en partes separadas por espacios
                String[] partes = linea.trim().split("\\s+");
                
                // Verificar que tenga el formato correcto
                if (partes.length < 6) {
                    System.out.println("Error en línea " + numeroLinea + ": formato incorrecto. Se esperan al menos 6 valores.");
                    continue;
                }
                
                try {
                    String ciudad1 = partes[0];
                    String ciudad2 = partes[1];
                    double tiempoNormal = Double.parseDouble(partes[2]);
                    double tiempoLluvia = Double.parseDouble(partes[3]);
                    double tiempoNieve = Double.parseDouble(partes[4]);
                    double tiempoTormenta = Double.parseDouble(partes[5]);
                    
                    // Agregar la conexión al grafo
                    grafo.agregarConexion(ciudad1, ciudad2, tiempoNormal, tiempoLluvia, tiempoNieve, tiempoTormenta);
                    
                } catch (NumberFormatException e) {
                    System.out.println("Error en línea " + numeroLinea + ": formato de número incorrecto.");
                }
            }
            
            // Ejecutar el algoritmo de Floyd-Warshall después de cargar todas las conexiones
            grafo.floyd();
            return true;
            
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            return false;
        }
    }
}