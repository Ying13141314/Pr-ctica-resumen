package tema1PRSER;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Controlador {
	
	//Estado 
	private static Controlador singleton;
	private static ProcessBuilder procesoBuil = new ProcessBuilder();
	
	//He creado un estado que me controla si estoy en windows o no.
	private static boolean esWindows;
	
	//Constructor
	public Controlador(){
		esWindows=System.getProperty("os.name").contains("Windows");
	}
	
	//Getter del singleton
	public static Controlador getSingleton() {

		return singleton;

	}
	//Comportamiento
	
	/**
	 * Método que ejecuta el comando que toca segun lo que escoja el usuario.
	 * @param opcion
	 * @throws IOException
	 */
	public void ejecutar(int opcion) throws IOException {
		switch (opcion) {
        	case 1:
        		crearCarpeta();
        		break;
	        case 2:
	        	crearfichero();
	            break;
	        case 3:
	        	listarInterRed ();
	            break;
	        case 4:
	        	mostarInterfaces();
	            break;
	        case 5:
	        	mostrarDiMac();
	            break;
	        case 6:
	        	boolean conexion=comprobarConexion();
	        	Main.imprimirConexion(conexion);
	            break;
	        case 7:
	        	Main.salir();
	            //salir = true;
	            break;
	        default:
        	//Obliga a que sea los número del 1 al 7.
	        	System.out.println("Solo números entre 1 y 7\n");
		}
	}
	
	/**
	* Método que crea una carpeta dada la ruta y el nombre.
	* @throws IOException
	*/
	public static void crearCarpeta() throws IOException {
		String rutaCarpeta,nombre,crearCarp,crearCarpLinux;
			
		Scanner snn = new Scanner(System.in);
		
		//Comprobar si la ruta existe o no.
		do {
			System.out.println("Introduce la ruta de la carpeta");
			rutaCarpeta = snn.nextLine();
		}while(!checkRuta(rutaCarpeta));

		System.out.println("Introduce el nombre de la carpeta");
		nombre = snn.nextLine();
		
		//Comprueba si estoy en wimdows o linus.
		if(esWindows) {
			crearCarp = "MKDIR "+rutaCarpeta+"\\"+nombre;
			procesoBuil.command("cmd.exe","/c",crearCarp);
		}else {
			crearCarpLinux = "mkdir "+rutaCarpeta+"//"+nombre;
			procesoBuil.command("bash", "-c",crearCarpLinux);
		}
			
		procesoBuil.start();

	}
	
	/**
	* Método que crea un fichero dada la ruta y el nombre.  
	* @throws IOException
	*/
	public static void crearfichero() throws IOException {
		String rutafichero,nombre,crearfiche,crearFichLinux;
			
		Scanner snn = new Scanner(System.in);
		
		//Comprobar si la ruta existe o no	    
		do {
			System.out.println("Introduce la ruta del fichero");
			rutafichero = snn.nextLine();
		}while(!checkRuta(rutafichero));
			   
		System.out.println("Introduce el nombre del fichero y añade una extensión que quieras");
		nombre = snn.nextLine();
		
		//Comprueba si estoy en wimdows o linus.	    
		if(esWindows) {
			crearfiche = "type nul > "+rutafichero+"\\"+nombre;
			procesoBuil.command("cmd.exe","/c",crearfiche);
		}else {
			crearFichLinux = "touch "+rutafichero+"//"+nombre;
			procesoBuil.command("bash", "-c",crearFichLinux);
		}
		procesoBuil.start();
	}
	

	/**Método que listar todas las interfaces de red de nuestro ordenador.
	 * 
	 * @throws IOException
	*/
	public static void listarInterRed () throws IOException {
		String linea;
		Process proceso;
		StringBuilder sb;
		BufferedReader reader;
		
		//Comprueba si estoy en wimdows o linus.
		if(esWindows) {
			
			//Muestra toda la informacion con el comando de powershell y firtrando por su nombre
			//a traves de select.
			procesoBuil.command("powershell.exe","Get-NetIPConfiguration | select -expandproperty InterfaceAlias");
			proceso = procesoBuil.start();
			sb = new StringBuilder();
			//Se añade Cp850 para que me muestre texto en formato legible.
			reader = new BufferedReader(
					new InputStreamReader(proceso.getInputStream(),"Cp850"));

			while ((linea = reader.readLine()) != null) {
				sb.append(linea + "\n");
			}
		}else {		
			//En linux utilizo comando ip porque no me funciona muy bien ifconfig
			//He intentado filtrar a traves de comando linux 
			//Utilizando -o para que me muestre en una sola linea.
			//mostrar segunda palabra y diferente del lo.
			procesoBuil.command("bash", "-c","ip -o link | awk '$2 !=\"lo:\" {print $2}'");
			proceso = procesoBuil.start();
			sb = new StringBuilder();
			
			//Se añade Cp850 para que me muestre texto en formato legible.
			reader = new BufferedReader(
					new InputStreamReader(proceso.getInputStream(),"Cp850"));
			
			//Aqui utilizo substring porque al filtrar interfaces en linux me sale dos punto al final.
			while ((linea = reader.readLine()) != null) {
				sb.append(linea.substring(0,linea.length()-1) + "\n");
			}
		}

		try {
			if (proceso.waitFor() == 0) {
				System.out.println("Las interfaces son : \n"+sb);
			} else {
				System.out.println("Errorr corrigelo rápido!!!!");
					
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			    
	}
		  
		
	/**
	* Método que muestra la IP del ordenador dado el nombre de la interfaz de red
	* @throws IOException
	*/
	public static void mostarInterfaces() throws IOException {
		//variable boolean para comprobar si el nombre de interfaces existe o no
		boolean comprobar=false;
		listarInterRed ();
		String nombreIP;
		  		
		Scanner snn = new Scanner(System.in);
		System.out.println("Introduce el nombre de la interfaz del red que quieres saber su IP");
			    
		nombreIP = snn.nextLine();
		
		//Comprobación de sistema operativo.
		if(esWindows) {
			//Obtengo la ip a traves del nombre que le paso por consola.
			procesoBuil.command("powershell.exe","(Get-NetAdapter -Name "+nombreIP+" | Get-NetIPAddress).IPv4Address");
		}else {
			//He intentado filtrar a traves de comando linux con tuberia 
			//Utilizando -4 para que me muestre ipv4.
			//mostrar segunda palabra y buscando inet que es la linea que contiene ip.
			procesoBuil.command("bash", "-c","ip -4 addr show "+nombreIP+" | grep inet |awk '$2 {print $2}'");
		}
		Process procesoIP = procesoBuil.start();
		StringBuilder sb = new StringBuilder();
				
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(procesoIP.getInputStream(),"Cp850"));

		//Si linea no es null(cuando le introducimos un nombre valido) entra al while 
		//y cambia el comprobar a true.
		String linea;
		while ((linea = reader.readLine()) != null) {
			sb.append(linea.split("/")[0] + "\n");
			comprobar=true;
		}
		try {
			if (procesoIP.waitFor() == 0) {
				System.out.println(sb);
				//si no comprobar significa que hemos introducido un nombre que no existe entonces
				//me muesta que no existe y vuelve a ejecutar el método.
				if(!comprobar) {
					System.out.print("Este nombre de interfaces no existe,introduce de nuevo");
					mostarInterfaces();
				}
			} else {
				System.out.println("Errorr corrigelo rápido!!!!");
					
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  		
	}
		  	
	/**Método que muestra la dirección MAC dado el nombre de la interfaz de red
	* 
	* @throws IOException
	*/
	public static void mostrarDiMac() throws IOException {
		listarInterRed ();
		boolean comprobarMac=false;  		
		String nombreInterfIp;
		Scanner snn = new Scanner(System.in);
		System.out.println("Introduce el nombre de la interfaz del red");
			    
		nombreInterfIp = snn.nextLine();
		
		//Comprobar windows o linux
		if(esWindows) {
			//Muestra toda la informacion con el comando de powershell y firtrando por su dirección Mac
			//a traves de select.
			procesoBuil.command("powershell.exe","Get-NetAdapter -Name *"+ nombreInterfIp +"* | select MacAddress");
		}else {
			//He intentado filtrar a traves de comando linux con tuberia 
			//Utilizando IP link para que me muestre toda la información
			//mostrar segunda palabra y buscando ether que es la linea que contiene la dirección Mac.
			procesoBuil.command("bash", "-c","ip link show "+ nombreInterfIp+" | grep ether | awk '$2 {print$2}'");
		}
			   
		Process procesoMac = procesoBuil.start();
				
		StringBuilder sb = new StringBuilder();
				
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(procesoMac.getInputStream(),"Cp850"));

		//Si linea no es null(cuando le introducimos un nombre valido) entra al while 
		//y cambia el comprobar a true.
		String linea;
		while ((linea = reader.readLine()) != null) {
			sb.append(linea + "\n");
			comprobarMac=true;
		}

		try {
			if (procesoMac.waitFor() == 0) {
				System.out.println(sb);
				//si no comprobar significa que hemos introducido un nombre que no existe entonces
				//me muesta que no existe y vuelve a ejecutar el método.
				if(!comprobarMac) {
					System.out.println("Este nombre de interfaces no existe,introduce de nuevo");
					mostrarDiMac();
				}
			} else {
				System.out.println("Errorr corrigelo rápido!!!!");
					
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		  		
	}
		  	
	/**Método que comprueba la conectividad con internet.
	 * @return 
	* 
	* @throws IOException
	 * @throws InterruptedException 
	*/
	public static boolean comprobarConexion() throws IOException {
		
		//Comprobamos el sistema.
		if(esWindows) {
			//Comprueba la conexión de internet devuelve true si está conectado y false si no está.
			procesoBuil.command("powershell.exe","Test-NetConnection | select PingSucceeded -ExpandProperty PingSucceeded");
		}else {
			//Comprueba la conexión en linux conectando al servidor de google.
			procesoBuil.command("bash", "-c","ping -c 1 google.com");
		}
			  	
		Process procesoConecion = procesoBuil.start();
		
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(procesoConecion.getInputStream(),"Cp850"));

		//Guardamos en un string lo que hemos pedido por la consola
		String linea = reader.readLine();


		//Si linea es igual a true o contiene 100% packet loss significa que la conexión está conectado.
		return linea.equals("True") | !linea.contains("100% packet loss");		
	}
	
	/**
	 * Método que comprueba si la ruta introducida es correcta o no.
	 * @param ruta
	 * @return
	 */
	public static boolean checkRuta(String ruta) {
		return new File(ruta).exists();
	}
	

}
