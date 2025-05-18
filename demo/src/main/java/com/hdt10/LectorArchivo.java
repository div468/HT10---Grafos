/*
 * Universidad del Valle de Guatemala
 * Algoritmos y Estructuras de Datos
 * Ing. Douglas Barrios
 * @author: Marcelo Detlefsen, Julián Divas
 * Creación: 17/05/2025
 * última modificación: 17/05/2025
 * File Name: LectorArchivo.java
 * Descripción: Clase que se encarga de leer un archivo de texto con información de ciudades y conexiones.
 * Formato del archivo:
 * Ciudad1 Ciudad2 tiempoNormal tiempoLluvia tiempoNieve tiempoTormenta
 */

package com.hdt10;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LectorArchivo {
    
    /**
     * Lee un archivo de texto con información de ciudades y sus conexiones
     * @param rutaArchivo Ruta del archivo a leer
     * @param grafo Grafo donde se cargarán los datos
     * @return true si la lectura fue exitosa, false en caso contrario
     */
    public static boolean cargarDesdeArchivo(String rutaArchivo, Grafo grafo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Ignorar líneas vacías
                if (linea.trim().isEmpty()) {
                    continue;
                }
                
                // Dividir la línea en tokens
                String[] tokens = linea.trim().split("\\s+");
                
                // Verificar que hay suficientes tokens
                if (tokens.length < 6) {
                    System.err.println("Error: Formato de línea incorrecto: " + linea);
                    continue;
                }
                
                try {
                    // Extraer información
                    String ciudad1 = tokens[0];
                    String ciudad2 = tokens[1];
                    double tiempoNormal = Double.parseDouble(tokens[2]);
                    double tiempoLluvia = Double.parseDouble(tokens[3]);
                    double tiempoNieve = Double.parseDouble(tokens[4]);
                    double tiempoTormenta = Double.parseDouble(tokens[5]);
                    
                    // Agregar la conexión al grafo
                    grafo.agregarConexion(ciudad1, ciudad2, tiempoNormal, tiempoLluvia, tiempoNieve, tiempoTormenta);
                    
                    System.out.println("Conexión agregada: " + ciudad1 + " -> " + ciudad2);
                } catch (NumberFormatException e) {
                    System.err.println("Error: Formato numérico incorrecto en línea: " + linea);
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            return false;
        }
    }
}