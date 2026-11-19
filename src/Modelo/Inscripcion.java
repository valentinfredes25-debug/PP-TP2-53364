package modelo;

import modelo.actividades.Actividad;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Inscripcion implements Serializable {

    private LocalDate fecha;
    private String estado;
    private Estudiante estudiante;
    private Actividad actividad;

    public Inscripcion(LocalDate fecha, String estado,
                       Estudiante estudiante, Actividad actividad) {
        this.fecha = fecha;
        this.estado = estado;
        this.estudiante = estudiante;
        this.actividad = actividad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    // Confirma la inscripción.
    public void confirmarInscripcion() {
        this.estado = "confirmada";
    }

    // Verifica si la inscripción está confirmada.
    public boolean estaConfirmada() {
        return estado != null && estado.equalsIgnoreCase("confirmada");
    }

    // Genera un ticket únicamente si la inscripción está confirmada.
    public TicketDeAcceso generarTicket() {
        if (!estaConfirmada()) {
            throw new IllegalStateException(
                    "No se puede generar un ticket para una inscripción no confirmada."
            );
        }

        return new TicketDeAcceso();
    }

    // Clase miembro interna: TicketDeAcceso.
    public class TicketDeAcceso implements Serializable {

        private String codigo;
        private LocalDateTime fechaEmision;

        public TicketDeAcceso() {
            this.codigo = UUID.randomUUID().toString();
            this.fechaEmision = LocalDateTime.now();
        }

        public String getCodigo() {
            return codigo;
        }

        public LocalDateTime getFechaEmision() {
            return fechaEmision;
        }

        @Override
        public String toString() {
            return "Ticket de Acceso {" +
                    "codigo='" + codigo + '\'' +
                    ", estudiante=" + estudiante.getNombre() +
                    ", actividad=" + actividad.getNombre() +
                    ", fechaEmision=" + fechaEmision +
                    '}';
        }
    }
}
