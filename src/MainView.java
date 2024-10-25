import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    public MainView() {
        setTitle("S-AES 系统");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("欢迎使用 S-AES 系统", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        add(titleLabel, BorderLayout.CENTER);

        // 创建主按钮面板，使用垂直 BoxLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // 原按钮面板，使用 2x2 的网格布局
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));  // 添加间距

        JButton encryptionButton = new JButton("进入加密");
        encryptionButton.setFont(new Font("Serif", Font.PLAIN, 18));
        encryptionButton.addActionListener(e -> openNewFrame("加密", new EncryptionView()));

        JButton decryptionButton = new JButton("进入解密");
        decryptionButton.setFont(new Font("Serif", Font.PLAIN, 18));
        decryptionButton.addActionListener(e -> openNewFrame("解密", new DecryptionView()));

        JButton multipleButton = new JButton("进入多重加密");
        multipleButton.setFont(new Font("Serif", Font.PLAIN, 18));
        multipleButton.addActionListener(e -> openNewFrame("多重加密模式", new MultipleEncryptionView()));

        JButton cbcButton = new JButton("进入工作模式加密");
        cbcButton.setFont(new Font("Serif", Font.PLAIN, 18));
        cbcButton.addActionListener(e -> openNewFrame("CBC 模式", new CBCView()));

        buttonPanel.add(encryptionButton);
        buttonPanel.add(decryptionButton);
        buttonPanel.add(multipleButton);
        buttonPanel.add(cbcButton);

        // 将按钮面板添加到主面板
        mainPanel.add(buttonPanel);

        // 添加 Meet-in-the-Middle 按钮，占据整个宽度
        JButton meetInTheMidButton = new JButton("进入中间相遇攻击");
        meetInTheMidButton.setFont(new Font("Serif", Font.PLAIN, 18));
        meetInTheMidButton.setAlignmentX(Component.CENTER_ALIGNMENT);  // 居中对齐
        meetInTheMidButton.setMaximumSize(new Dimension(200, 40));  // 限制按钮大小

        meetInTheMidButton.addActionListener(e -> openMidFrame("中间相遇攻击", new MeetInTheMidView()));

        // 添加垂直间距和按钮到主面板
        mainPanel.add(Box.createVerticalStrut(15));  // 添加间距
        mainPanel.add(meetInTheMidButton);

        // 将主面板添加到窗口底部
        add(mainPanel, BorderLayout.SOUTH);
    }

    private void openNewFrame(String title, JPanel panel) {
        JFrame frame = new JFrame(title);
        frame.setContentPane(panel);
        frame.setSize(700, 650);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void openMidFrame(String title, JPanel panel) {
        JFrame frame = new JFrame(title);
        frame.setContentPane(panel);
        frame.setSize(700, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MainView().setVisible(true);
        });
    }
}
