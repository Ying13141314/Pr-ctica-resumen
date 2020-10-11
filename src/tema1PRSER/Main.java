package tema1PRSER;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
	
	//Estado
	static Controlador miControlador = new Controlador();
	
	private static boolean salir = false;
	//Constructor 
	
		
	//Comportamiento
	
	public static void main(String args[]) throws IOException {
		menu(); 
		
	}
	
	/**
	 * M�todo segun haya conexi�n o no muestra un mensaje u otro.
	 * @param conexion
	 * @return
	 */
	public static boolean imprimirConexion(boolean conexion) {
		if(conexion) {
			System.out.println("La conexi�n es correcta \n");
		}else {
			System.out.println("No hay conexi�n \n");
		}
		return conexion;
	}
	
	/**M�todo que me controla para finalizar el proyecto.
	 * 
	 */
	public static void salir() {
		salir=true;
	}
		
	/**
	* M�todo men� donde puedo elegir a que m�todo quiero utilizar o ejecutar primero.
	* @throws IOException
	*/
	private static void menu() throws IOException {
		Scanner sn = new Scanner(System.in);
	    
	    int opcion; //Guardaremos la opcion del usuario
	    
	    while (!salir) {
	 
	    	System.out.println("Opcion 1.Crear una carpeta dada una ruta y el nombre.");
	    	System.out.println("Opcion 2.Crear un fichero dada la ruta y el nombre.");
	    	System.out.println("Opcion 3.Listar todas las interfaces de red de nuestro ordenador.");
	    	System.out.println("Opcion 4.Mostrar la IP del ordenador dado el nombre de la interfaz de red.");
	    	System.out.println("Opcion 5.Mostrar la direcci�n MAC dado el nombre de la interfaz de red.");
	    	System.out.println("Opcion 6.Comprobar conectividad con internet.");
	    	System.out.println("7. Salir");
	 
	        	try {
	 
	                System.out.println("Escribe una de las opciones");
	                opcion = sn.nextInt();
	                
	                miControlador.ejecutar(opcion);
	                
	            //Obliga introducir n�mero.
	            } catch (InputMismatchException e) {
	                System.out.println("Debes insertar un n�mero");
	                sn.next();
	            }catch(IOException e) {
	            	System.out.println("ERROR VUELVE A INTENTAR");
	            }
	        }
	    	System.out.println("He terminado");
	    	sn.close();
		}

}