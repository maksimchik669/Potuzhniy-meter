import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Potuzhnometr {

    private static int progressStatus = 0;
    private static boolean isHolding = false;
    private static boolean isExploded = false;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Потужний Метр");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        // Дозволяємо розгортати вікно на весь екран
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(true);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(0, 87, 183));

        JLabel titleLabel = new JLabel("ПОТУЖНИЙ МЕТР", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);

        JLabel percentLabel = new JLabel("0%", SwingConstants.CENTER);
        percentLabel.setFont(new Font("Arial", Font.BOLD, 56));
        percentLabel.setForeground(Color.WHITE);
        panel.add(percentLabel);

        // Потоньше шкала прогресу (висота 35 пікселів, як у мобільному додатку)
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        panel.add(progressBar);

        JLabel hintLabel = new JLabel("Затисни мишку в будь-якому місці вікна!", SwingConstants.CENTER);
        hintLabel.setFont(new Font("Arial", Font.BOLD, 15));
        hintLabel.setForeground(Color.WHITE);
        panel.add(hintLabel);

        frame.add(panel);

        // Автоматичне вирівнювання елементів по центру при зміні розміру вікна чи на весь екран
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int panelWidth = panel.getWidth();
                int panelHeight = panel.getHeight();

                int centerX = panelWidth / 2;
                int centerY = panelHeight / 2;

                titleLabel.setBounds(centerX - 250, centerY - 150, 500, 40);
                percentLabel.setBounds(centerX - 250, centerY - 80, 500, 70);
                progressBar.setBounds(centerX - 200, centerY + 10, 400, 35); // Тонка шкала
                hintLabel.setBounds(centerX - 250, centerY + 70, 500, 30);
            }
        });

        Timer chargeTimer = new Timer(40, e -> {
            if (isHolding && progressStatus < 100 && !isExploded) {
                progressStatus += 2;
                if (progressStatus > 100) progressStatus = 100;

                progressBar.setValue(progressStatus);
                percentLabel.setText(progressStatus + "%");

                if (progressStatus < 50) {
                    percentLabel.setForeground(new Color(76, 175, 80));
                } else if (progressStatus < 85) {
                    percentLabel.setForeground(new Color(255, 235, 59));
                } else {
                    percentLabel.setForeground(new Color(244, 67, 54));
                }

                if (progressStatus == 100) {
                    isExploded = true;
                    percentLabel.setText("БАБАХ! 💥");
                    hintLabel.setText("Максимальне перевантаження!");

                    Timer resetTimer = new Timer(3000, ev -> {
                        progressStatus = 0;
                        isExploded = false;
                        progressBar.setValue(0);
                        percentLabel.setText("0%");
                        percentLabel.setForeground(Color.WHITE);
                        hintLabel.setText("Затисни мишку в будь-якому місці вікна!");
                        ((Timer) ev.getSource()).stop();
                    });
                    resetTimer.setRepeats(false);
                    resetTimer.start();
                }
            }
        });

        Timer dischargeTimer = new Timer(40, e -> {
            if (!isHolding && progressStatus > 0 && !isExploded) {
                progressStatus -= 3;
                if (progressStatus < 0) progressStatus = 0;

                progressBar.setValue(progressStatus);
                percentLabel.setText(progressStatus + "%");

                if (progressStatus < 50) {
                    percentLabel.setForeground(new Color(76, 175, 80));
                } else if (progressStatus < 85) {
                    percentLabel.setForeground(new Color(255, 235, 59));
                } else {
                    percentLabel.setForeground(new Color(244, 67, 54));
                }
            }
        });
        dischargeTimer.start();

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!isExploded) {
                    isHolding = true;
                    chargeTimer.start();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isHolding = false;
                chargeTimer.stop();
            }
        };

        panel.addMouseListener(mouseAdapter);
        titleLabel.addMouseListener(mouseAdapter);
        percentLabel.addMouseListener(mouseAdapter);
        hintLabel.addMouseListener(mouseAdapter);

        frame.setVisible(true);
    }
}