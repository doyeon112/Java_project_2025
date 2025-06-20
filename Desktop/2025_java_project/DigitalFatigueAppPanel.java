import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class DigitalFatigueAppPanel extends JPanel {

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
                0, 0, new Color(255, 235, 150),
                0, getHeight(), new Color(255, 253, 230)
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

    public DigitalFatigueAppPanel(JFrame frame) {
        ImageIcon icon = new ImageIcon(getClass().getResource("/img/bg.jpg"));
        Image bgImage = icon.getImage();


        BackgroundPanel bgPanel = new BackgroundPanel(bgImage);
        bgPanel.setLayout(new BoxLayout(bgPanel, BoxLayout.Y_AXIS));
        bgPanel.setPreferredSize(new Dimension(360, 640));

        JLabel mainTitle = new JLabel("Digital Focus Management System", SwingConstants.CENTER);
        mainTitle.setFont(new Font("SansSerif", Font.PLAIN, 20));
        mainTitle.setForeground(new Color(255, 245, 210));
        mainTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subTitle = new JLabel("Breeze", SwingConstants.CENTER);
        subTitle.setFont(new Font("SansSerif", Font.BOLD, 32));
        subTitle.setForeground(new Color(255, 245, 210));
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedGradientButton focusBtn = new RoundedGradientButton("Start Focus Session");
        RoundedGradientButton recordBtn = new RoundedGradientButton("View History");

        focusBtn.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(new FocusSessionPanel(frame));
            frame.revalidate();
            frame.repaint();
        });

        recordBtn.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(new RecordViewerPanel(frame));
            frame.revalidate();
            frame.repaint();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(focusBtn);
        buttonPanel.add(recordBtn);

        // 전체 배치
bgPanel.add(Box.createVerticalStrut(60));    // 위 여백
bgPanel.add(mainTitle);
bgPanel.add(Box.createVerticalStrut(10));    // mainTitle과 subTitle 사이
bgPanel.add(subTitle);
bgPanel.add(Box.createVerticalStrut(240));   // 제목과 버튼 사이 여백
bgPanel.add(buttonPanel);
bgPanel.add(Box.createVerticalStrut(0));    // 아래 여백




        setLayout(new BorderLayout());
        add(bgPanel, BorderLayout.CENTER);
    }
}
