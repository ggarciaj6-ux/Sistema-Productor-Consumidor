import java.awt.*;
import javax.swing.*;

public class VentanaEmergencia extends JFrame {
    private JButton btnPlay, btnStop;
    private JProgressBar barraBuffer;
    private JLabel lblEstadoBuffer;
    private JLabel lblSemProd, lblSemMed1, lblSemMed2;
    private JLabel lblMed1, lblMed2;

    private BufferEmergencia buffer;
    private ProductorPacientes productor;
    private MedicoConsumidor medico1, medico2;
    private final int CAPACIDAD_MAXIMA = 6;

    public VentanaEmergencia() {
        setTitle("Sala de Emergencias - Productor / Consumidor");
        setSize(750, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initUI();
    }

    private void initUI() {
        // Panel Superior: Botones Play / Stop
        JPanel panelControl = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPlay = new JButton("▶ Iniciar");
        btnStop = new JButton("■ Detener");
        btnStop.setEnabled(false);

        btnPlay.addActionListener(e -> iniciarSimulacion());
        btnStop.addActionListener(e -> detenerSimulacion());

        panelControl.add(btnPlay);
        panelControl.add(btnStop);
        add(panelControl, BorderLayout.NORTH);

        // Panel Central: Productor, Buffer y Médicos
        JPanel panelCentro = new JPanel(new GridLayout(1, 3, 15, 10));
        panelCentro.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 1. Recepción (Productor)
        JPanel pnlProd = new JPanel(new GridLayout(3, 1));
        pnlProd.setBorder(BorderFactory.createTitledBorder("Recepción (Productor)"));
        pnlProd.add(new JLabel("Ingreso de Pacientes", SwingConstants.CENTER));
        lblSemProd = new JLabel("● Semáforo", SwingConstants.CENTER);
        lblSemProd.setForeground(Color.GRAY);
        pnlProd.add(lblSemProd);
        panelCentro.add(pnlProd);

        // 2. Sala de Espera (Buffer)
        JPanel pnlBuff = new JPanel(new GridLayout(3, 1, 5, 5));
        pnlBuff.setBorder(BorderFactory.createTitledBorder("Sala de Espera (Buffer)"));
        barraBuffer = new JProgressBar(0, CAPACIDAD_MAXIMA);
        barraBuffer.setStringPainted(true);
        lblEstadoBuffer = new JLabel("Pacientes en espera: 0/" + CAPACIDAD_MAXIMA, SwingConstants.CENTER);
        pnlBuff.add(new JLabel("Capacidad", SwingConstants.CENTER));
        pnlBuff.add(barraBuffer);
        pnlBuff.add(lblEstadoBuffer);
        panelCentro.add(pnlBuff);

        // 3. Médicos (Consumidores)
        JPanel pnlCons = new JPanel(new GridLayout(4, 1));
        pnlCons.setBorder(BorderFactory.createTitledBorder("Consultorios (Consumidores)"));
        lblMed1 = new JLabel("Médico 1: Inactivo");
        lblSemMed1 = new JLabel("● Semáforo M1");
        lblSemMed1.setForeground(Color.GRAY);
        lblMed2 = new JLabel("Médico 2: Inactivo");
        lblSemMed2 = new JLabel("● Semáforo M2");
        lblSemMed2.setForeground(Color.GRAY);
        pnlCons.add(lblMed1);
        pnlCons.add(lblSemMed1);
        pnlCons.add(lblMed2);
        pnlCons.add(lblSemMed2);
        panelCentro.add(pnlCons);

        add(panelCentro, BorderLayout.CENTER);
    }

    private void iniciarSimulacion() {
        buffer = new BufferEmergencia(CAPACIDAD_MAXIMA, this);
        productor = new ProductorPacientes(buffer);
        medico1 = new MedicoConsumidor(1, buffer, this);
        medico2 = new MedicoConsumidor(2, buffer, this);

        productor.start();
        medico1.start();
        medico2.start();

        btnPlay.setEnabled(false);
        btnStop.setEnabled(true);
    }

    private void detenerSimulacion() {
        if (productor != null) productor.detener();
        if (medico1 != null) medico1.detener();
        if (medico2 != null) medico2.detener();

        lblSemProd.setForeground(Color.GRAY);
        lblSemMed1.setForeground(Color.GRAY);
        lblSemMed2.setForeground(Color.GRAY);

        btnPlay.setEnabled(true);
        btnStop.setEnabled(false);
    }

    public void actualizarBuffer(int cantidad, int total) {
        barraBuffer.setValue(cantidad);
        barraBuffer.setString(cantidad + " / " + total + " (" + (cantidad * 100 / total) + "%)");
        lblEstadoBuffer.setText("Pacientes en espera: " + cantidad + "/" + total);
    }

    public void setSemaforoProductor(boolean activo) {
        SwingUtilities.invokeLater(() -> lblSemProd.setForeground(activo ? Color.GREEN : Color.RED));
    }

    public void setSemaforoConsumidor(int medicoId, boolean atendiendo) {
        SwingUtilities.invokeLater(() -> {
            Color color = atendiendo ? Color.GREEN : Color.RED;
            if (medicoId == 1) lblSemMed1.setForeground(color);
            else lblSemMed2.setForeground(color);
        });
    }

    public void setEstadoMedico(int id, String texto) {
        if (id == 1) lblMed1.setText("Médico 1: " + texto);
        else lblMed2.setText("Médico 2: " + texto);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaEmergencia().setVisible(true));
    }
}