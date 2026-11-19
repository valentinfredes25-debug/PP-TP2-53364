package hilos;

import modelo.EventoUniversitario;
import modelo.Inscripcion;
import modelo.actividades.Actividad;

public class EnvioTicketsThread extends Thread {

    private EventoUniversitario evento;

    public EnvioTicketsThread(EventoUniversitario evento) {
        this.evento = evento;
        this.setName("Hilo-Envio-Tickets");
    }

    @Override
    public void run() {

        System.out.println("\n=== INICIO DEL ENVÍO DE TICKETS ===");

        for (Actividad actividad : evento.getActividades()) {

            for (Inscripcion inscripcion : actividad.getInscripciones()) {

                // Solo se envían tickets de inscripciones confirmadas.
                if (inscripcion.estaConfirmada()) {

                    Inscripcion.TicketDeAcceso ticket =
                            inscripcion.generarTicket();

                    System.out.println(
                            Thread.currentThread().getName()
                                    + " enviando: " + ticket
                    );

                    try {
                        // Pausa para poder observar el trabajo concurrente.
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("El envío de tickets fue interrumpido.");
                        return;
                    }
                }
            }
        }

        System.out.println("=== FINALIZÓ EL ENVÍO DE TICKETS ===");
    }
}
