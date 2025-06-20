import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

public class RecordViewerPanel extends JPanel {
    private List<Integer> history;

    public RecordViewerPanel(JFrame parentFrame) {
        // 기록 데이터 불러오기
        history = FocusHistoryManager.load();

        //  배경 이미지 설정
        ImageIcon icon = new ImageIcon("img/bg.jpg");
        Image bgImage = icon.getImage();
        BackgroundPanel bgPanel = new BackgroundPanel(bgImage);
        bgPanel.setLayout(new BoxLayout(bgPanel, BoxLayout.Y_AXIS));
        bgPanel.setPreferredSize(new Dimension(360, 640)); // 기본 화면 크기

        // 이 패널 전체에 배경 패널 넣기
        setLayout(new BorderLayout());
        add(bgPanel, BorderLayout.CENTER);

        // 유리 느낌의 Glass Panel 생성
        RoundedGlassPanel glassPanel = new RoundedGlassPanel();
        glassPanel.setLayout(new BoxLayout(glassPanel, BoxLayout.Y_AXIS));
        glassPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        glassPanel.setMaximumSize(new Dimension(360, 500));
        glassPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 차트 패널: 기록 데이터 시각화
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // 차트 타이틀
                String title = "Focus Time History Chart";
                Font titleFont = new Font("Malgun Gothic", Font.BOLD, 20);
                g.setFont(titleFont);
                FontMetrics metrics = g.getFontMetrics(titleFont);
                int titleWidth = metrics.stringWidth(title);
                int titleX = (getWidth() - titleWidth) / 2;
                g.drawString(title, titleX, 40);

                // 기록 막대 그래프
                int baseY = 250;
                for (int i = 0; i < history.size(); i++) {
                    int value = history.get(i);
                    int barHeight = value * 3;
                    int x = 40 + i * 40;
                    int y = baseY - barHeight - 5;

                    // 막대
                    g.setColor(Color.BLUE);
                    g.fillRect(x, baseY - barHeight, 30, barHeight);

                    // 숫자
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
                    g.drawString(value + "m", x, y);
                }
            }
        };
        chartPanel.setOpaque(false);
        chartPanel.setPreferredSize(new Dimension(360, 300));

        // 뒤로 가기 버튼
        RoundedGradientButton backButton = new RoundedGradientButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> {
            parentFrame.getContentPane().removeAll();
            parentFrame.getContentPane().add(new DigitalFatigueAppPanel(parentFrame));
            parentFrame.revalidate();
            parentFrame.repaint();
        });

        // glassPanel에 구성요소 추가
        glassPanel.add(chartPanel);
        glassPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        glassPanel.add(backButton);

        // glassPanel을 가운데 배치
        bgPanel.add(Box.createVerticalGlue());
        bgPanel.add(glassPanel);
        bgPanel.add(Box.createVerticalGlue());
    }

    // 배경 이미지를 표시하는 패널
    public class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(Image backgroundImage) {
            this.backgroundImage = backgroundImage;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    // 둥글고 반투명한 유리 박스 패널
    class RoundedGlassPanel extends JPanel {
        public RoundedGlassPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 255, 255, 150)); // 반투명 흰색
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // 둥근 버튼 클래스 (그라디언트 배경)
    class RoundedGradientButton extends JButton {
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
                0, 0, new Color(255, 220, 130),         // 머스타드색 위쪽
                0, getHeight(), new Color(255, 235, 150) // 밝은 노랑 아래쪽
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
}