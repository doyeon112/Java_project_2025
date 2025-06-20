import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.*;
// 소리 관련(3개)
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FocusSessionPanel extends JPanel {
    private JTextField focusInput, restInput;
    private JLabel timerLabel;
    private Timer timer;
    private int totalSeconds;
    private boolean isFocusTime;
    private int restSeconds;
    private int maxFocusMinutes;
    private JFrame parentFrame;

    public FocusSessionPanel(JFrame parent) {
        this.parentFrame = parent;
        
        //  배경 이미지 추가
        ImageIcon icon = new ImageIcon(getClass().getResource("/img/bg.jpg"));
        Image bgImage = icon.getImage();

    BackgroundPanel bgPanel = new BackgroundPanel(bgImage);
    bgPanel.setLayout(new BoxLayout(bgPanel, BoxLayout.Y_AXIS));
    bgPanel.setPreferredSize(new Dimension(360, 640));

    //  기존 setLayout() 코드 대신 bgPanel에 추가
    setLayout(new BorderLayout());
    add(bgPanel, BorderLayout.CENTER);
//버튼들들

    RoundedGradientButton startButton = new RoundedGradientButton("Start Timer");
RoundedGradientButton backButton = new RoundedGradientButton("Back");

    //  Glass Panel 생성
RoundedGlassPanel glassPanel = new RoundedGlassPanel();
glassPanel.setLayout(new BoxLayout(glassPanel, BoxLayout.Y_AXIS));
glassPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 안쪽 여백
glassPanel.setPreferredSize(new Dimension(360, 300));
glassPanel.setMaximumSize(new Dimension(360, 600));  // 크기 조절
glassPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

backButton.addActionListener(e -> {
    parentFrame.getContentPane().removeAll();
    parentFrame.getContentPane().add(new DigitalFatigueAppPanel(parentFrame));
    parentFrame.revalidate();
    parentFrame.repaint();
});

    // 타이머 라벨 (맨 위 중앙)
    timerLabel = new JLabel("00:00", SwingConstants.CENTER);
    timerLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 40));
    timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    timerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
     // bgPanel.add(timerLabel);

    // 입력 패널
    JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
    inputPanel.setOpaque(false);   //  투명하게 해서 배경 보이게
    inputPanel.setMaximumSize(new Dimension(400, 100));

    JLabel focusLabel = new JLabel("Study Time (minutes) — between 20 and 60:");
    focusInput = new JTextField(5);
    focusInput.setMaximumSize(new Dimension(100, 30));

    JLabel breakLabel = new JLabel("Break Time (minutes) — between 1 and 20:");
    restInput = new JTextField(5);
    restInput.setMaximumSize(new Dimension(100, 30));

    JPanel buttonPanel = new JPanel(new FlowLayout());
buttonPanel.setOpaque(false);
buttonPanel.add(startButton);
buttonPanel.add(backButton);


    inputPanel.add(focusLabel);
    inputPanel.add(focusInput);
    inputPanel.add(breakLabel);
    inputPanel.add(restInput);

    bgPanel.add(inputPanel);




    // glassPanel 조립
glassPanel.add(timerLabel);
glassPanel.add(Box.createRigidArea(new Dimension(0, 10)));
glassPanel.add(inputPanel);
glassPanel.add(Box.createRigidArea(new Dimension(0, 10)));
glassPanel.add(buttonPanel);
glassPanel.add(buttonPanel); // 뒤로가기 버튼 생성



//  glassPanel을 bgPanel 중앙에 추가
bgPanel.add(Box.createVerticalGlue());
bgPanel.add(glassPanel);
bgPanel.add(Box.createVerticalGlue());

    bgPanel.add(Box.createRigidArea(new Dimension(0, 20))); // 간격

    // 버튼 기능
    startButton.addActionListener(e -> startSession());
    backButton.addActionListener(e -> {
        parentFrame.getContentPane().removeAll();
        parentFrame.getContentPane().add(new DigitalFatigueAppPanel(parentFrame));
        parentFrame.revalidate();
        parentFrame.repaint();
        
    });

}

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
            0, 0, new Color(255, 210, 180),
            0, getHeight(), new Color(230, 200, 100)
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


    private void startSession() {
        try {
            int focusMin = Integer.parseInt(focusInput.getText());
            restSeconds = Integer.parseInt(restInput.getText()) * 60;

            if (focusMin < 1 || focusMin > 60 || restSeconds / 60 < 1 || restSeconds / 60 > 15) {
                JOptionPane.showMessageDialog(this, "집중은 1~60분, 휴식은 1~15분 사이로 설정하세요.");
                return;
            }

            isFocusTime = true;
            totalSeconds = focusMin * 60;
            runTimer();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "숫자를 올바르게 입력하세요.");
        }
    }

        // 소리 관련
private void playAlarmSound() {
    try {
        InputStream is = getClass().getResourceAsStream("/sound/collect_item_hurry_up_alarm_warning_01.wav");
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(is);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
        e.printStackTrace();
    }
}

    private void runTimer() {
        if (timer != null) timer.stop();

        timer = new Timer(1000, e -> {
            int minutes = totalSeconds / 60;
            int seconds = totalSeconds % 60;
            timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
            totalSeconds--;

            if (totalSeconds < 0) {
                timer.stop();
                playAlarmSound(); 
                if (isFocusTime) {
                    JOptionPane.showMessageDialog(null, "Study session finished! Reaction speed test starts!");
                    runReactionTest();
                } else {
                    JOptionPane.showMessageDialog(null, "Break time is over!");
                }
            }
        });
        timer.start();
    }

    private void runReactionTest() {
        JFrame testFrame = new JFrame("Reaction Speed Test");
        testFrame.setSize(300, 200);
        testFrame.setLayout(new BorderLayout());

        JLabel instruction = new JLabel("Click when it turns green!!", SwingConstants.CENTER);
        JButton testButton = new JButton("Waiting...");
        testButton.setEnabled(false);
        testButton.setBackground(Color.LIGHT_GRAY);

        testFrame.add(instruction, BorderLayout.NORTH);
        testFrame.add(testButton, BorderLayout.CENTER);

        testFrame.setLocationRelativeTo(null);
        testFrame.setVisible(true);

        Timer delay = new Timer((int)(Math.random() * 3000) + 2000, new ActionListener() {
            long start;

            public void actionPerformed(ActionEvent e) {
                testButton.setText("지금 클릭!");
                testButton.setEnabled(true);
                testButton.setBackground(Color.GREEN);
                start = System.currentTimeMillis();

                testButton.addActionListener(ev -> {
                    long rt = System.currentTimeMillis() - start;
                    JOptionPane.showMessageDialog(testFrame, "반응 시간: " + rt + "ms");
                    testFrame.dispose();

                    if (rt > 400) {
                        maxFocusMinutes = Integer.parseInt(focusInput.getText());
                        FocusHistoryManager.save(maxFocusMinutes);
                        showAdvice(rt);
                    }

                    isFocusTime = false;
                    totalSeconds = restSeconds;
                    runTimer();
                });
            }
        });
        delay.setRepeats(false);
        delay.start();
    }

    private void showAdvice(long reactionTime) {
        String msg = "⏱ Your reaction was a bit slow (" + reactionTime + "ms).\n"
                   + "Estimated focus limit: " + maxFocusMinutes + " minutes\n\n"
                   + "✨ Smart Focus Tips\n"
                   + "1. 📋 Focus on one task at a time\n"
                   + "   → Multitasking reduces working memory\n"
                   + "2. 🧘 Pre-study ritual (music, stretch, sip of coffee)\n"
                   + "   → Helps brain transition into focus mode\n"
                   + "3. 💡 Same place, same time, same lighting\n"
                   + "   → Brain learns to associate environment with focus\n"
                   + "4. ❄️ Cool your brain (cold water or wrist cooling)\n"
                   + "   → Boosts response speed and mental clarity\n"
                   + "5. 🔵 Bright light in the morning, warm light at night\n"
                   + "   → Keeps your sleep and focus rhythm healthy\n"
                   + "6. ⏱ Set short deadlines (e.g. 15-min sprints)\n"
                   + "   → Pressure triggers urgency and boosts motivation\n\n"
                   + "☕ Focus-friendly snacks:\n"
                   + "• Dark chocolate (70%+) – caffeine + blood flow\n"
                   + "• Walnuts & almonds – brain-healthy fats\n"
                   + "• Green tea – calm + alert combo\n"
                   + "• Water + pinch of salt – hydration + electrolytes\n\n"
                   + "📚 Source: Harvard Health, MIT Neuroscience Lab";
        JOptionPane.showMessageDialog(this, msg);
    }

    // 모서리 둥글고 반투명한 유리 느낌 박스 배경
    class RoundedGlassPanel extends JPanel {
        public RoundedGlassPanel() {
            setOpaque(false);
        }
    
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
            // 반투명 흰색 (유리 느낌)
            g2.setColor(new Color(255, 255, 255, 150)); // A=150이면 반투명
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);  // 모서리 둥글게
    
            g2.dispose();
            super.paintComponent(g);
        }
    }

}
