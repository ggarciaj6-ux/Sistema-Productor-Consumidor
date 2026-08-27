import javax.swing.SwingUtilities;

public class MedicoConsumidor extends Thread {
    private final int id;
    private final BufferEmergencia buffer;
    private final VentanaEmergencia gui;
    private volatile boolean running = true;

    public MedicoConsumidor(int id, BufferEmergencia buffer, VentanaEmergencia gui) {
        this.id = id;
        this.buffer = buffer;
        this.gui = gui;
    }

    public void detener() {
        running = false;
        interrupt();
    }

    @Override
    public void run() {
        while (running) {
            try {
                SwingUtilities.invokeLater(() -> gui.setEstadoMedico(id, "Zzz (Esperando)"));
                String paciente = buffer.atenderPaciente(id);

                SwingUtilities.invokeLater(() -> gui.setEstadoMedico(id, "Atendiendo a " + paciente));
                Thread.sleep(5000 + (long)(Math.random() * 3000)); // Atiende 3 - 5 seg
            } catch (InterruptedException e) {
                break;
            }
        }
        SwingUtilities.invokeLater(() -> gui.setEstadoMedico(id, "Inactivo"));
    }
}