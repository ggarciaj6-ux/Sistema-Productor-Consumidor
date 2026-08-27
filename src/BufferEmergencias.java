import java.util.LinkedList;
import java.util.Queue;
import javax.swing.SwingUtilities;

class BufferEmergencia {
    private final int capacidad;
    private final Queue<String> colaPacientes = new LinkedList<>();
    private final VentanaEmergencia gui;

    public BufferEmergencia(int capacidad, VentanaEmergencia gui) {
        this.capacidad = capacidad;
        this.gui = gui;
    }

    public synchronized void agregarPaciente(String paciente) throws InterruptedException {
        while (colaPacientes.size() == capacidad) {
            gui.setSemaforoProductor(false); // Rojo: buffer lleno
            wait();
        }
        colaPacientes.add(paciente);
        gui.setSemaforoProductor(true);  // Verde: paciente ingresado
        actualizarGUI();
        notifyAll();
    }

    public synchronized String atenderPaciente(int medicoId) throws InterruptedException {
        while (colaPacientes.isEmpty()) {
            gui.setSemaforoConsumidor(medicoId, false); // Rojo: sin pacientes
            wait();
        }
        String paciente = colaPacientes.poll();
        gui.setSemaforoConsumidor(medicoId, true);  // Verde: atendiendo
        actualizarGUI();
        notifyAll();
        return paciente;
    }

    private void actualizarGUI() {
        int ocupacion = colaPacientes.size();
        SwingUtilities.invokeLater(() -> gui.actualizarBuffer(ocupacion, capacidad));
    }
}