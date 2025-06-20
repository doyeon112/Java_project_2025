import javax.swing.*;

public class DigitalFatigueApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("디지털 집중 관리 시스템");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(440, 660);  // 9:16 비율
            frame.setResizable(false);

            frame.add(new DigitalFatigueAppPanel(frame));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
