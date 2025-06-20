import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class RoundedGradientButton extends JButton {
    public RoundedGradientButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setFont(new Font("Malgun Gothic", Font.PLAIN, 16));
        setForeground(Color.DARK_GRAY);
        setPreferredSize(new Dimension(180, 40));
        setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(0, 0, 0), 1, true),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gp = new GradientPaint(
            0, 0, new Color(200, 220, 240),
            0, getHeight(), new Color(240, 245, 250)
        );
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(140, 150, 170));
        g2.setStroke(new BasicStroke(0.8f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
        g2.dispose();
    }
}
