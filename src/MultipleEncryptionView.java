import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MultipleEncryptionView extends JPanel {
    private JTextField keyField, originalField, secretField;
    private JTextField doubleEncodeRes, doubleDecodeRes, trebleEncodeRes, trebleDecodeRes;
    private JButton doubleEncodeButton, doubleDecodeButton, trebleEncodeButton, trebleDecodeButton;
    private Encoder encoder = new Encoder();
    private Decoder decoder = new Decoder();
    private Common common = new Common();  // 初始化通用工具类

    public MultipleEncryptionView() {
        init();
    }

    private void init() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 标题
        JLabel titleLabel = new JLabel("多重AES加解密", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // 明文输入框
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
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

        // 密钥输入框
        gbc.gridy = 3;
        gbc.gridx = 0;
        add(new JLabel("密钥："), gbc);

        keyField = new JTextField(40);
        keyField.setBorder(BorderFactory.createTitledBorder("请输入32位二进制密钥"));
        gbc.gridx = 1;
        add(keyField, gbc);

        // 双重加密结果框
        gbc.gridy = 4;
        gbc.gridx = 0;
        add(new JLabel("双重加密结果："), gbc);

        doubleEncodeRes = new JTextField(40);
        doubleEncodeRes.setEditable(false);
        doubleEncodeRes.setBorder(BorderFactory.createTitledBorder("显示双重加密结果"));
        gbc.gridx = 1;
        add(doubleEncodeRes, gbc);

        // 双重解密结果框
        gbc.gridy = 5;
        gbc.gridx = 0;
        add(new JLabel("双重解密结果："), gbc);

        doubleDecodeRes = new JTextField(40);
        doubleDecodeRes.setEditable(false);
        doubleDecodeRes.setBorder(BorderFactory.createTitledBorder("显示双重解密结果"));
        gbc.gridx = 1;
        add(doubleDecodeRes, gbc);

        // 三重加密结果框
        gbc.gridy = 6;
        gbc.gridx = 0;
        add(new JLabel("三重加密结果："), gbc);

        trebleEncodeRes = new JTextField(40);
        trebleEncodeRes.setEditable(false);
        trebleEncodeRes.setBorder(BorderFactory.createTitledBorder("显示三重加密结果"));
        gbc.gridx = 1;
        add(trebleEncodeRes, gbc);

        // 三重解密结果框
        gbc.gridy = 7;
        gbc.gridx = 0;
        add(new JLabel("三重解密结果："), gbc);

        trebleDecodeRes = new JTextField(40);
        trebleDecodeRes.setEditable(false);
        trebleDecodeRes.setBorder(BorderFactory.createTitledBorder("显示三重解密结果"));
        gbc.gridx = 1;
        add(trebleDecodeRes, gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        Dimension buttonSize = new Dimension(150, 40);

        // 双重加密按钮
        doubleEncodeButton = createButton("双重加密", buttonSize, e -> doubleEncode());
        buttonPanel.add(doubleEncodeButton);

        // 双重解密按钮
        doubleDecodeButton = createButton("双重解密", buttonSize, e -> doubleDecode());
        buttonPanel.add(doubleDecodeButton);

        // 三重加密按钮
        trebleEncodeButton = createButton("三重加密", buttonSize, e -> trebleEncode());
        buttonPanel.add(trebleEncodeButton);

        // 三重解密按钮
        trebleDecodeButton = createButton("三重解密", buttonSize, e -> trebleDecode());
        buttonPanel.add(trebleDecodeButton);

        // 添加按钮面板
        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);
    }

    private JButton createButton(String text, Dimension size, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Serif", Font.PLAIN, 18));
        button.setPreferredSize(size);
        button.addActionListener(action);
        return button;
    }

    private void doubleEncode() {
        String key = keyField.getText().trim();
        String original = originalField.getText().trim();

        if (validateKey(key) && validateBinary(original, "明文内容不规范")) {
            String middle = encoder.encode(original, key.substring(0, 16));
            doubleEncodeRes.setText(encoder.encode(middle, key.substring(16)));
        }
    }

    private void doubleDecode() {
        String key = keyField.getText().trim();
        String secret = secretField.getText().trim();

        if (validateKey(key) && validateBinary(secret, "密文内容不规范")) {
            String middle = decoder.decode(secret, key.substring(16));
            doubleDecodeRes.setText(decoder.decode(middle, key.substring(0, 16)));
        }
    }

    private void trebleEncode() {
        String key = keyField.getText().trim();
        String original = originalField.getText().trim();

        if (validateKey(key) && validateBinary(original, "明文内容不规范")) {
            String middle = encoder.encode(original, key.substring(0, 16));
            middle = encoder.encode(middle, key.substring(16));
            trebleEncodeRes.setText(encoder.encode(middle, key.substring(0, 16)));
        }
    }

    private void trebleDecode() {
        String key = keyField.getText().trim();
        String secret = secretField.getText().trim();

        if (validateKey(key) && validateBinary(secret, "密文内容不规范")) {
            String middle = decoder.decode(secret, key.substring(0, 16));
            middle = decoder.decode(middle, key.substring(16));
            trebleDecodeRes.setText(decoder.decode(middle, key.substring(0, 16)));
        }
    }

    private boolean validateKey(String key) {
        if (key.length() != 32 || !common.checkBinary(key)) {
            keyField.setText("密钥内容不规范");
            return false;
        }
        return true;
    }

    private boolean validateBinary(String text, String errorMessage) {
        if (text.length() != 16 || !common.checkBinary(text)) {
            JOptionPane.showMessageDialog(this, errorMessage, "输入错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
