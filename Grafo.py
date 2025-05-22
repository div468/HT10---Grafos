#Se importan las librerías necesarias
import networkx as nx
import numpy as np
import os
"""
Universidad del Valle de Guatemala
Algoritmos y Estructuras de Datos
Ing. Douglas Barrios
@author: Marcelo Detlefsen, Julián Divas
Creación: 20/05/2025
última modificación: 20/05/2025
File Name: Grafo..py
Descripción: Programa que implementa lo solicitado en la hoja de trabajo con el módulo NetworkX
 """
#Se carga el grafo existente desde un .txt
def cargar_grafo(archivo):
    #Se inicializa el grafo direccionado vacío
    grafo = nx.DiGraph()
    #Se abre el archivo en modo lectura "r"
    with open(archivo, 'r') as f:
        for linea in f:
            #Para cada linea se eliminan los espacios y se separa la informacion
            datos = linea.strip().split()
            #Verificacion de la existencia de todos los datos necesarios
            if len(datos) == 6:
                ciudad1 = datos[0]
                ciudad2 = datos[1]
                tiempos = {
                    'normal': int(datos[2]),
                    'lluvia': int(datos[3]),
                    'nieve': int(datos[4]),
                    'tormenta': int(datos[5]),
                    'clima_actual': 'normal'} 
                #Se crea el nodo con la información leída
                grafo.add_edge(ciudad1, ciudad2, **tiempos)
    return grafo

#creacion de matrices de distancias y de rutas
def matrices_distancia_ruta(grafo):
    #Se obtienen las ciudades ordenadas y la cantidad de estas
    ciudades = sorted(grafo.nodes())
    n = len(ciudades)

    #Matriz de distancias con infinito
    dist = np.full((n, n), np.inf)
    #Diagonal con 0 (distancia de una matriz a ella misma)
    np.fill_diagonal(dist, 0)
    #Matriz para almacenar los caminos proximos nodos de la ruta más corta
    nodo_siguiente = np.full((n, n), -1, dtype=int)
    #Asigna a cada ciudad un indice para la matriz
    ciudad_a_indice = {ciudad: i for i, ciudad in enumerate(ciudades)}
    
    #Llena las matrices con los datos del grafo
    for origen, destino, datos in grafo.edges(data=True):
        #con los indices asociados a cada matriz se trabaja el origen y el destino
        i = ciudad_a_indice[origen]
        j = ciudad_a_indice[destino]
        #Para ir de I a J (considerando rutas directas inicialmente) el tiempo necesario es el del clima actual en esa arista
        dist[i][j] = datos[datos['clima_actual']]
        #El siguiente nodo para ir de I a J es J
        nodo_siguiente[i][j] = j
    
    return ciudades, ciudad_a_indice, dist, nodo_siguiente

#Algoritmo Floyd-Warshall para caminos más cortos
def floyd_warshall(dist, nodo_siguiente, n):
    #Nodo intermedio
    for k in range(n):
        #nodo origen
        for i in range(n):
            #nodo destino
            for j in range(n):
                #Si la distancia de  la ruta directa es mayor a la distanciia pasando por el intermedio k
                if dist[i][j] > dist[i][k] + dist[k][j]:
                    #Se actualiza la distancia minima con el intermedio
                    dist[i][j] = dist[i][k] + dist[k][j]
                    #Se actualiza el camino para indicar que antes se debe ir a k para ir de i a j
                    nodo_siguiente[i][j] = nodo_siguiente[i][k]
    return dist, nodo_siguiente

#reconstruir la ruta con las matrices
def reconstruir_ruta(origen_id, destino_id, nodo_siguiente, ciudades):
    #Si el valor en la matriz nodo_siguiente es 01, no existe ruta
    if nodo_siguiente[origen_id][destino_id] == -1:
        #no devuelve ninguna ruta
        return []
    #Se inicia la ruta con la ciudad de origen, transformando del indice al nombre de la ciudad nuevamente
    ruta = [ciudades[origen_id]]
    #Mientras no se llegue al destino
    while origen_id != destino_id:
        #Se actualiza origen_id para saber a donde se desplazó en la ruta para llegar a destino
        #por eso se detendrá el while cuando oriden_id == destion_id
        origen_id = nodo_siguiente[origen_id][destino_id]
        #Se añade a la ruta la ciudad por la que se pasó
        ruta.append(ciudades[origen_id])
    #devuelve la lista con las ciudades a visitar en la ruta del origen al destino
    return ruta

#Para indicar el centro del grafo
def calcular_centro_grafo(ciudades, dist):
    #Se cuentan las ciudades existentes
    n = len(ciudades)
    #Para almacenar y comparar las excentricidades de cada nodo
    excentricidades = []
    for j in range(n): # Para cada destino (columnas)
        max_dist = 0
        for i in range(n): # Para cada origen (filas)
            #Si existe camino de i a j y la distancia sea mayor al max_dist actual
            if dist[i][j] != np.inf and dist[i][j] > max_dist:
                #Se actualiza el máximo
                max_dist = dist[i][j]
        #Guarda la maxima distancia en excentricidades
        excentricidades.append(max_dist if max_dist != 0 else np.inf)
    #Se devuelve el índice del valor con excentricidad mínima
    id_centro = np.argmin(excentricidades)
    #Devuelve una tupla con el nombre de la ciudad y su excentricidad
    return ciudades[id_centro], excentricidades[id_centro]

#Muestra las rutas con los climas que poseen en ese momento
def mostrar_estado_grafo(grafo):
    print("\nEstado actual de las rutas:")
    for origen, destino, datos in grafo.edges(data=True):
        clima = datos['clima_actual']
        tiempo = datos[clima]
        print(f"De {origen} a {destino} hay un clima {clima}, y toma un tiempo de llegada de {tiempo} horas")

#Agregar conexiones entre ciudades existentes o no existentes
def agregar_conexion(grafo, ciudad1, ciudad2, tiempos):
    if ciudad1 not in grafo:
        grafo.add_node(ciudad1)
    if ciudad2 not in grafo:
        grafo.add_node(ciudad2)
    
    #Se establece completamente la ruta entre ammbas ciudades con sus tiempos
    grafo.add_edge(ciudad1, ciudad2, 
                  normal=tiempos['normal'],
                  lluvia=tiempos['lluvia'],
                  nieve=tiempos['nieve'],
                  tormenta=tiempos['tormenta'],
                  clima_actual='normal')
    
    #Se recalculan las matrices para tomar en cuenta la nueva ciudad
    ciudades, ciudad_a_indice, dist, next_node = matrices_distancia_ruta(grafo)
    dist, next_node = floyd_warshall(dist, next_node, len(ciudades))
    
    return grafo, ciudades, ciudad_a_indice, dist, next_node

#Funcion principal del programa
def main():
    datos_ejemplo = """BuenosAires SaoPaulo 10 15 20 50
    BuenosAires Lima 15 20 30 70
    Lima Quito 10 12 15 20
    SaoPaulo Quito 25 30 40 60"""
    #Se escriben los datos de ejemplo en caso de no haber un archivo
    if not os.path.exists("guategrafo.txt"):
        with open('guategrafo.txt', 'w') as f:
            f.write(datos_ejemplo)
        print("Se creo un archivo con datos de ejemplo")
    else:
        print("Se utilizó el archivo existente")
    grafo = cargar_grafo('guategrafo.txt')
    programa = True
    #Menu inicial
    while programa:
        print("\nMENÚ PRINCIPAL")
        print("1. Consultar ruta más corta entre dos ciudades")
        print("2. Cambiar clima en una ruta")
        print("3. Ver clima actual en las rutas")
        print("4. Mostrar centro del grafo")
        print("5. Agregar nueva ruta")
        print("6. Eliminar ruta existente")
        print("7. Salir")
        
        opcion = input("Seleccione una opción: ")
        
        #Para encontrar la ruta más corta entre dos ciudades
        if opcion == '1':
            #Se solicita de donde salir
            origen = input("Ciudad origen: ")
            #Se solicita a donde ir
            destino = input("Ciudad destino: ")
            
            ciudades, ciudad_a_indice, dist, nodo_siguiente = matrices_distancia_ruta(grafo)
            dist, nodo_siguiente = floyd_warshall(dist, nodo_siguiente, len(ciudades))
            
            if origen in ciudad_a_indice and destino in ciudad_a_indice:
                i = ciudad_a_indice[origen]
                j = ciudad_a_indice[destino]
                
                if dist[i][j] != np.inf:
                    ruta = reconstruir_ruta(i, j, nodo_siguiente, ciudades)
                    print(f"\nRuta más corta: {' = '.join(ruta)}")
                    print(f"Tiempo total: {dist[i][j]} horas")
                    
                else:
                    print("\nNo existe ruta entre las ciudades especificadas")
            else:
                print("\nUna o ambas ciudades no existen en la red")
        #Cambiar el clima en una ruta
        elif opcion == '2':
            print("\nCambiar clima en una ruta:")
            ciudad1 = input("Ciudad origen: ")
            ciudad2 = input("Ciudad destino: ")
            #Si existen las ciudades
            if grafo.has_edge(ciudad1, ciudad2):
                print("\nClimas disponibles:")
                print("1. Normal")
                print("2. Lluvia")
                print("3. Nieve")
                print("4. Tormenta")
                op = input("Seleccione clima (1-4): ")
                
                climas = {'1': 'normal', '2': 'lluvia', '3': 'nieve', '4': 'tormenta'}
                if op in climas:
                    grafo[ciudad1][ciudad2]['clima_actual'] = climas[op]
                    print(f"\nClima actualizado a {climas[op]} en la ruta de {ciudad1} a {ciudad2}")
                else:
                    print("\nOpción no válida")
            else:
                print("\nLa ruta especificada no existe")
        #Se muestra el estado del grafo
        elif opcion == '3':
            mostrar_estado_grafo(grafo)
        #Se muestra el centro de la ciudad
        elif opcion == '4':
            #Se obtienen las matrices necesarias
            ciudades, ciudad_a_indice, dist, nodo_siguiente = matrices_distancia_ruta(grafo)
            #Se actualian las mejores distancias y las rutas
            dist, nodo_siguiente = floyd_warshall(dist, nodo_siguiente, len(ciudades))
            #se obtiene la distancia maxima del centro y el respectivo centro
            centro, distancia = calcular_centro_grafo(ciudades, dist)
            print(f"\nLa ciudad centro es: {centro}")
            print(f"Distancia máxima a cualquier otra ciudad: {distancia} horas")

        #Se añade una nueva ruta
        elif opcion == '5':
            ciudad1 = input("Ciudad origen: ")
            ciudad2 = input("Ciudad destino: ")
            normal = int(input("Tiempo normal (horas): "))
            lluvia = int(input("Tiempo con lluvia (horas): "))
            nieve = int(input("Tiempo con nieve (horas): "))
            tormenta = int(input("Tiempo con tormenta (horas): "))
            
            tiempos = {'normal': normal, 'lluvia': lluvia, 'nieve': nieve, 'tormenta': tormenta}
            
            grafo, ciudades, ciudad_a_indice, dist, next_node = agregar_conexion(
                grafo, ciudad1, ciudad2, tiempos)
            
            print(f"\nConexión de {ciudad1} a {ciudad2} agregada")
        
        #Eliminar rutas
        elif opcion == '6':
            ciudad1 = input("Ciudad origen de la ruta a eliminar: ")
            ciudad2 = input("Ciudad destino de la ruta a eliminar: ")
            if grafo.has_edge(ciudad1, ciudad2):
                grafo.remove_edge(ciudad1, ciudad2)
                print("\nRuta eliminada exitosamente")
            else:
                print("\nLa ruta especificada no existe")
        #Se termina el programa
        elif opcion == '7':
            print("\nSaliendo del sistema...")
            programa = False
        
        else:
            print("\nOpción no válida, por favor intente nuevamente")
main()