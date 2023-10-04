package agenda;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

public class Agenda {

	public static String csvFile = "Agenda.csv";
	
	public static void main(String[] args) {
		
		boolean bucle = true;
		Scanner sc =new Scanner(System.in);
		
		while(bucle) {
			System.out.println("1: Buscar por código\t2: Buscar por nombre\n3: Añadir contacto\t4: Borrar contacto\n5: Mostrar agenda");
			int opcion=sc.nextInt();
			sc.nextLine();
			
			switch (opcion) {
				case 1:
					System.out.println("Introduzca código");
					String codigo=sc.nextLine();
					buscaCodigo(codigo);
					break;
				case 2:
					System.out.println("Introduzca nombre");
					String nombre=sc.nextLine();
					buscaNombre(nombre);
					break;
				case 3:
					System.out.println("Nombre:");
					String nombreContacto=sc.nextLine();
					System.out.println("Teléfono:");
					String telefonoContacto=sc.nextLine();
					String edadContacto="";
					int edad=0;
					while(true) {
						try {
							System.out.println("Edad:");
							String aux=sc.nextLine();
							if(aux.equals("")) {
								break;
							}else{
								edad=Integer.parseInt(aux);
							}
							edadContacto=String.valueOf(edad);
							break;
						}catch(Exception ex) {
							
						}
					}
					agregarContacto(nombreContacto,telefonoContacto,edadContacto);
					break;
				case 4:
					System.out.println("Contacto a borrar: ");
					String codigoBorrar=sc.nextLine();
					borrarContacto(codigoBorrar);
					break;
				case 5:
					mostrarAgenda();
					break;
				default:
					bucle = false;
					break;
				}
			
		}
		sc.close();
	}

	
	/**
	 * Muestra los contactos de la agenda
	 */
	private static void mostrarAgenda() {
		BufferedReader br = null;
		String line = "";
		
		try {
			if(existeFichero(csvFile)) {
			    br = new BufferedReader(new FileReader(csvFile));
			    while ((line = br.readLine()) != null) {                
			        System.out.println(line.replace(",","|"));
			    }
			    System.out.println();
			}
			
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    cerrarBuffer(br);
		}
		
	}

	
	/**
	 * Borra un contacto de la agenda
	 * @param codigoBorrar
	 */
	private static void borrarContacto(String codigoBorrar) {

		int linea = buscaCodigo(codigoBorrar);
		
		if(linea!=-1) {
			try {
				updateCSV(csvFile, "borrado 00000000-0000-0000-0000-000000000000", linea, 0);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CsvException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	/**
	 * Agrega un nuevo contacto a la agenda
	 * @param nombreContacto
	 * @param telefonoContacto
	 * @param edadContacto
	 */
	private static void agregarContacto(String nombreContacto, String telefonoContacto, String edadContacto) {
		
		try {
			BufferedWriter writer=new BufferedWriter(new FileWriter("Agenda.csv",true));
			writer.write(UUID.randomUUID().toString()+","+nombreContacto+","+telefonoContacto+","+edadContacto + "\n");
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Busca los contactos de la agenda por nombre
	 * @param nombre
	 */
	private static void buscaNombre(String nombre) {
		BufferedReader br = null;
		String line = "";
		boolean encontrado=false;
		
		try {
			if(existeFichero(csvFile)) {
				br = new BufferedReader(new FileReader(csvFile));
				
				while ((line = br.readLine()) != null) {
					if(!line.contains("borrado")) {
						String[] datos = line.split(",");
						if(datos[1].toLowerCase().contains(nombre.toLowerCase())) {
							System.out.println(line.replace(",","|"));
							encontrado=true;
						}
					}
				}
				if(!encontrado) {
					System.out.println("Nadie con ese nombre\n");
				}
			}
			
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    cerrarBuffer(br);
		}
		
	}

	
	/**
	 * Busca los contactos de la agenda por código
	 * @param codigo
	 */
	private static int buscaCodigo(String codigo) {
		String csvFile = "Agenda.csv";
		BufferedReader br = null;
		String line = "";
		int indexLinea = 0;
		
		try {
			if(existeFichero(csvFile)) {
				br = new BufferedReader(new FileReader(csvFile));
				
				while ((line = br.readLine()) != null) { 
					indexLinea++;
					String[] datos = line.split(",");
					if(Objects.equals(datos[0], codigo)) {
						System.out.println(line.replace(",","|")+"\n");
						return indexLinea-1;
					}
				}
				
			}
			
		    
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    cerrarBuffer(br);
		}
		return indexLinea = -1;
		
	}
	
	
	/**
	 * Comprueba si existe un fichero
	 * @param path
	 * @return boolean
	 */
	private static boolean existeFichero(String path) {
		File f = new File(path);
		if(f.exists() && !f.isDirectory()) { 
		    return true;
		}else {
			System.out.println("No se ha encontrado el fichero");
			return false;
		}
	}
	
	
	/**
	 * Cierra BufferedReader
	 * @param br
	 */
	private static void cerrarBuffer(BufferedReader br) {
		if (br != null) {
	        try {
	            br.close();
	        } catch (IOException e) {
	        	System.out.println("Error al cerrar el Buffered");
	            e.printStackTrace();
	        }
	    }
	}
	
	
	
	/**
	 * Modifica el contenido de una celda del CSV
	 * @param fileToUpdate
	 * @param replace
	 * @param row
	 * @param col
	 * @throws IOException
	 * @throws CsvException
	 */
	public static void updateCSV(String fileToUpdate, String replace,
		    int row, int col) throws IOException, CsvException {

		File inputFile = new File(fileToUpdate);

		// Read existing file 
		CSVReader reader = new CSVReader(new FileReader(inputFile));
		List<String[]> csvBody = reader.readAll();
		// get CSV row column  and replace with by using row and column
		csvBody.get(row)[col] = replace;
		reader.close();
		
		// Write to CSV file which is open
		CSVWriter writer = new CSVWriter(new FileWriter(inputFile));
		writer.writeAll(csvBody);
		writer.flush();
		writer.close();
		}

}