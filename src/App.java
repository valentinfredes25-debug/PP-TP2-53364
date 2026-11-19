import exepciones.CupoExcedidoException;
import hilos.EnvioTicketsThread;
import modelo.Inscripcion;
import modelo.actividades.Actividad;
import modelo.Estudiante;
import modelo.EventoUniversitario;
import modelo.Sala;
import modelo.actividades.Charla;
import modelo.actividades.Curso;
import modelo.actividades.Taller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class App {
    public static void main(String[] args) {

        // Creamos el evento.
        EventoUniversitario eventouni1 =
                new EventoUniversitario("1", "clase jueves", 500.0, false);

        Sala sala1 = new Sala(1, "pequeña");
        eventouni1.asignarSala(sala1);

        // Creamos las actividades del evento.
        eventouni1.crearActividad(1, "Taller Java", 1, "Taller", true);
        eventouni1.crearActividad(2, "Charla Java", 20, "Charla", false);
        eventouni1.crearActividad(3, "Curso de Java", 15, "Curso", false);

        // Creamos los estudiantes.
        Estudiante estudiante1 = new Estudiante("51959", "Alexander");
        Estudiante estudiante2 = new Estudiante("51960", "Franco");
        Estudiante estudiante3 = new Estudiante("51961", "Santiago");

        // Obtenemos las actividades creadas.
        Actividad actividad1 = eventouni1.getActividades().get(0);
        Actividad actividad2 = eventouni1.getActividades().get(1);
        Actividad actividad3 = eventouni1.getActividades().get(2);

        // Inscribimos estudiantes y confirmamos algunas inscripciones.
        
        try {
            Inscripcion inscripcion = actividad1.inscribir(estudiante1);
            inscripcion.confirmarInscripcion();
        } catch (CupoExcedidoException e) {
            System.out.println("No se pudo inscribir a Alexander en Taller Java: " + e.getMessage());
        }

        try {
            // Esta inscripción puede no realizarse si el taller ya alcanzó su cupo.
            Inscripcion inscripcion = actividad1.inscribir(estudiante2);
            // La dejamos sin confirmar como ejemplo de inscripción no confirmada.
        } catch (CupoExcedidoException e) {
            System.out.println("No se pudo inscribir a Franco en Taller Java: " + e.getMessage());
        }

        try {
            Inscripcion inscripcion = actividad2.inscribir(estudiante2);
            inscripcion.confirmarInscripcion();
        } catch (CupoExcedidoException e) {
            System.out.println("No se pudo inscribir a Franco en Charla Java: " + e.getMessage());
        }

        try {
            Inscripcion inscripcion = actividad2.inscribir(estudiante3);
            // Esta inscripción queda pendiente, sin confirmar.
        } catch (CupoExcedidoException e) {
            System.out.println("No se pudo inscribir a Santiago en Charla Java: " + e.getMessage());
        }

        try {
            Inscripcion inscripcion = actividad3.inscribir(estudiante3);
            inscripcion.confirmarInscripcion();
        } catch (CupoExcedidoException e) {
            System.out.println("No se pudo inscribir a Santiago en Curso de Java: " + e.getMessage());
        }

        try {
            Inscripcion inscripcion = actividad3.inscribir(estudiante1);
            // Esta inscripción queda pendiente, sin confirmar.
        } catch (CupoExcedidoException e) {
            System.out.println("No se pudo inscribir a Alexander en Curso de Java: " + e.getMessage());
        }


        System.out.println(" Iniciando proceso de envío...");
        EnvioTicketsThread envio = new EnvioTicketsThread(eventouni1);
        envio.start();

        // Mostramos los datos del evento y sus inscripciones desde el hilo principal.
        System.out.println("DATOS DEL EVENTO");
        eventouni1.mostrarDatos();

        actividad1.mostrarInscripciones();
        actividad2.mostrarInscripciones();
        actividad3.mostrarInscripciones();

        System.out.println("Cantidad total de eventos: "
                + EventoUniversitario.getCantidadEventos());

        // EJERCICIO 1:
        try {
            eventouni1.persistir();
        } catch (FileNotFoundException e) {
            System.out.println("¡¡ No se encontró el archivo !!");
        } catch (IOException e) {
            System.out.println("¡¡ Error de entrada/salida !!");
        } finally {
            System.out.println("Finalizó el proceso de persistir.");
        }

        EventoUniversitario eventorecuperado = null;
        try {
            eventorecuperado = EventoUniversitario.recuperar();
        } catch (FileNotFoundException e) {
            System.out.println("¡¡ Error: archivo no encontrado !!");
        } catch (IOException e) {
            System.out.println("¡¡ Error de entrada/salida !!");
        } catch (ClassNotFoundException e) {
            System.out.println("¡¡ Error: no se encontró la clase !!");
        } finally {
            System.out.println("Finalizó el proceso de recuperar.");
        }

        if (eventorecuperado != null) {
            eventorecuperado.mostrarDatos();
            for (Actividad actividad : eventorecuperado.getActividades()) {
                actividad.mostrarInscripciones();
            }
        }

        // Mostramos los certificados de Talleres y Cursos.
        for (Actividad actividad : eventouni1.getActividades()) {
            for (Inscripcion inscripcion : actividad.getInscripciones()) {
                Estudiante estudiante = inscripcion.getEstudiante();

                if (actividad.getTipo().equals("Taller")) {
                    Taller taller = (Taller) actividad;
                    System.out.println(taller.generarCertificado(estudiante));
                } else if (actividad.getTipo().equals("Curso")) {
                    Curso curso = (Curso) actividad;
                    System.out.println(curso.generarCertificado(estudiante));
                }
            }
        }

        // EJERCICIO 3
        List<Charla> charlas =
                eventouni1.filtrarActividadesPorTipo(Charla.class);

        List<Taller> talleres =
                eventouni1.filtrarActividadesPorTipo(Taller.class);

        List<Curso> cursos =
                eventouni1.filtrarActividadesPorTipo(Curso.class);

        System.out.println("\nCantidad de Charla: " + charlas.size());
        System.out.println("Cantidad de Taller: " + talleres.size());
        System.out.println("Cantidad de Curso: " + cursos.size());

        System.out.println("\nCosto de materiales de las Charlas: "
                + eventouni1.calcularCostoMateriales(charlas));
        System.out.println("Costo de materiales de los Talleres: "
 
