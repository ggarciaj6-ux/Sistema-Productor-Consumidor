public class ProductorPacientes extends Thread {
    private final BufferEmergencia buffer;
    private volatile boolean running = true;
    private int contador = 1;

    public ProductorPacientes(BufferEmergencia buffer) {
        this.buffer = buffer;
    }

    public void detener() {
        running = false;
        interrupt();
    }

    @Override
    public void run() {
        while (running) {
            try {
                String paciente = "Paciente #" + contador++;
                buffer.agregarPaciente(paciente);
                Thread.sleep(500 + (long)(Math.random() * 500)); // Llega cada 1.5 - 3 seg
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}