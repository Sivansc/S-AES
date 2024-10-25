import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MeetInTheMidView extends JPanel {
    private JTextField originalField, secretField;
    private JTextArea messagePresent, keyResult;
    private JButton appendButton, decodeButton, clearButton;
    private List<String> MimOriginal = new ArrayList<>();
    private List<String> MimSecret = new ArrayList<>();
    private Common common = new Common(); // 假设这是你的通用工具类
    private MeetInTheMid attacker = new MeetInTheMid(); // 假设这是你的攻击者类

    public MeetInTheMidView() {
        init();
    }

    private void init() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 标题
        JLabel titleLabel = new JLabel("中间相遇攻击模块", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1; // 给标题一些垂直空间
        add(titleLabel, gbc);

        // 明文输入框
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weighty = 0; // 重置为0
        add(new JLabel("明文："), gbc);

        originalField = new JTextField(40);
        originalField.setBorder(BorderFactory.createTitledBorder("请输入16位二进制明文"));
        gbc.gridx = 1;
        add(originalField, gbc);

        // 密文输入框
        gbc.gridy = 2;
        gbc.gridx = 0;
        add(new JLabel("密文："), gbc);

        secretField = new JTextField(40);
        secretField.setBorder(BorderFactory.createTitledBorder("请输入16位二进制密文"));
        gbc.gridx = 1;
        add(secretField, gbc);

        // 显示已存储明密文对的文本区
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 1; // 让文本区占据更多空间
        messagePresent = new JTextArea(10, 50); // 设置高度和宽度
        messagePresent.setEditable(false);
        messagePresent.setBorder(BorderFactory.createTitledBorder("已存储明密文对"));
        add(new JScrollPane(messagePresent), gbc);

        // 可能的密钥展示区
        gbc.gridy = 4;
        keyResult = new JTextArea(10, 50); // 设置高度和宽度
        keyResult.setEditable(false);
        keyResult.setBorder(BorderFactory.createTitledBorder("可能的密钥"));
        add(new JScrollPane(keyResult), gbc);

        // 按钮面板
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weighty = 0; // 重置为0
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        // 添加明密文对按钮
        appendButton = createButton("添加明密文对", e -> appendMessage());
        buttonPanel.add(appendButton);

        // 解码密钥按钮
        decodeButton = createButton("解码密钥", e -> MimDecode());
        buttonPanel.add(decodeButton);

        // 清空按钮
        clearButton = createButton("清空", e -> clearMessage());
        buttonPanel.add(clearButton);

        // 添加按钮面板
        add(buttonPanel, gbc);
    }

    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Serif", Font.PLAIN, 18));
        button.addActionListener(action);
        return button;
    }

    private void appendMessage() {
        String originalText = originalField.getText().trim();
        String secretText = secretField.getText().trim();

        if (originalText.length() == 16 && secretText.length() == 16
                && common.checkBinary(originalText) && common.checkBinary(secretText)) {
            messagePresent.append("明文：" + originalText + "\n");
            messagePresent.append("密文：" + secretText + "\n\n");
            MimOriginal.add(originalText);
            MimSecret.add(secretText);
            originalField.setText("");
            secretField.setText("");
        } else {
            if (secretText.length() != 16 || !common.checkBinary(secretText)) {
                JOptionPane.showMessageDialog(this, "密文内容不规范", "输入错误", JOptionPane.ERROR_MESSAGE);
            }
            if (originalText.length() != 16 || !common.checkBinary(originalText)) {
                JOptionPane.showMessageDialog(this, "明文内容不规范", "输入错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void MimDecode() {
        keyResult.setText(""); // 清空之前的结果

        if (messagePresent.getText().isEmpty()) {
            keyResult.setText("请至少输入一对明密文对");
        } else if (MimOriginal.size() == 1) {
            String iniOri = MimOriginal.get(0);
            String iniSec = MimSecret.get(0);
            List<String> keyArray = attacker.singleMsgAttack(iniOri, iniSec);
            keyResult.append("密钥数量过于庞大，只展示以下十条\n");
            for (int i = 0; i < Math.min(10, keyArray.size()); i++) {
                keyResult.append(keyArray.get(i) + "\n");
            }
        } else {
            String iniOri = MimOriginal.get(0);
            String iniSec = MimSecret.get(0);
            List<String> keyArray = attacker.singleMsgAttack(iniOri, iniSec);
            keyArray = attacker.multiMsgAttack(MimOriginal, MimSecret, keyArray);
            if (keyArray.isEmpty()) {
                keyResult.setText("该组明密文对不存在可行密钥");
            } else {
                keyResult.append("可能的密钥：\n");
                for (String key : keyArray) {
                    keyResult.append(key + "\n");
                }
            }
        }
    }

    private void clearMessage() {
        messagePresent.setText("");
        keyResult.setText("");
        MimSecret.clear();
        MimOriginal.clear();
        originalField.setText("");
        secretField.setText("");
    }
}
