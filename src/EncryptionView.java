import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EncryptionView extends JPanel {
    private JTextField plainTextField; // 普通明文输入框
    private JTextField cipherTextField; // 密文输出框
    private JTextField keyField; // 普通密钥输入框
    private JTextField ascPlainTextField; // ASCII 明文输入框
    private JTextField ascCipherTextField; // ASCII 密文输出框
    private JTextField ascKeyField; // ASCII 密钥输入框
    private JButton encryptButton; // 普通加密按钮
    private JButton ascEncryptButton; // ASCII 加密按钮
    private Encoder encoder = new Encoder(); // 使用 Encoder 类
    private Common common = new Common(); // 使用 Common 类

    public EncryptionView() {
        init();
    }

    private void init() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // 组件之间的间距

        JLabel titleLabel = new JLabel("S-AES 加密", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 26));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // 明文输入框
        gbc.gridwidth = 1;
        JLabel plainLabel = new JLabel("明文：");
        gbc.gridy = 1;
        add(plainLabel, gbc);

        plainTextField = new JTextField(40);
        plainTextField.setBorder(BorderFactory.createTitledBorder("请输入明文"));
        gbc.gridx = 1;
        add(plainTextField, gbc);

        // 密钥输入框
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel keyLabel = new JLabel("密钥：");
        add(keyLabel, gbc);

        keyField = new JTextField(40);
        keyField.setBorder(BorderFactory.createTitledBorder("请输入16位二进制密钥"));
        gbc.gridx = 1;
        add(keyField, gbc);

        // 密文输入框
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel cipherLabel = new JLabel("密文：");
        add(cipherLabel, gbc);

        cipherTextField = new JTextField(40);
        cipherTextField.setEditable(false);
        cipherTextField.setBorder(BorderFactory.createTitledBorder("加密后的密文"));
        gbc.gridx = 1;
        add(cipherTextField, gbc);

        // 加密按钮
        encryptButton = new JButton("加密");
        encryptButton.setFont(new Font("Serif", Font.PLAIN, 18));
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String plaintext = plainTextField.getText().trim();
                    String key = keyField.getText().trim();
                    StringBuilder ciphertext = new StringBuilder();

                    // 验证输入规范
                    validateInput(plaintext, key);

                    // 处理明文的编码
                    String encoded = encoder.encode(plaintext, key);
                    ciphertext.append(encoded);
                    cipherTextField.setText(ciphertext.toString());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(encryptButton, gbc);

        // ASCII 明文输入框
        gbc.gridwidth = 1;
        gbc.gridy = 5;
        JLabel ascPlainLabel = new JLabel("ASCII 明文：");
        add(ascPlainLabel, gbc);

        ascPlainTextField = new JTextField(40);
        ascPlainTextField.setBorder(BorderFactory.createTitledBorder("请输入ASCII明文"));
        gbc.gridx = 1;
        add(ascPlainTextField, gbc);

        // ASCII 密钥输入框
        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel ascKeyLabel = new JLabel("ASCII 密钥：");
        add(ascKeyLabel, gbc);

        ascKeyField = new JTextField(40);
        ascKeyField.setBorder(BorderFactory.createTitledBorder("请输入16位二进制密钥"));
        gbc.gridx = 1;
        add(ascKeyField, gbc);

        // ASCII 密文输入框
        gbc.gridx = 0;
        gbc.gridy = 7;
        JLabel ascCipherLabel = new JLabel("ASCII 密文：");
        add(ascCipherLabel, gbc);

        ascCipherTextField = new JTextField(40);
        ascCipherTextField.setEditable(false);
        ascCipherTextField.setBorder(BorderFactory.createTitledBorder("加密后的ASCII密文"));
        gbc.gridx = 1;
        add(ascCipherTextField, gbc);

        // ASCII 加密按钮
        ascEncryptButton = new JButton("ASCII 加密");
        ascEncryptButton.setFont(new Font("Serif", Font.PLAIN, 18));
        ascEncryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String ascPlainText = ascPlainTextField.getText().trim();
                    String ascKey = ascKeyField.getText().trim();

                    // 验证输入规范
                    validateAscInput(ascPlainText, ascKey);

                    // 进行ASCII加密
                    StringBuilder res = new StringBuilder();
                    String[] ascArray = common.ascToBin(ascPlainText); // 转换为二进制数组
                    for (String origin : ascArray) {
                        String encoded = encoder.encode(origin, ascKey);
                        res.append((char) Integer.parseInt(encoded.substring(0, 8), 2));//转换为char字符写回
                        res.append((char) Integer.parseInt(encoded.substring(8, 16), 2));
                    }
                    ascCipherTextField.setText(res.toString());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        add(ascEncryptButton, gbc);
    }

    private void validateInput(String plaintext, String key) throws Exception {
        boolean valid = true;

        // 检查密钥是否有效
        if (key.length() != 16 || !common.checkBinary(key)) {
            keyField.setText("密钥内容不规范（必须为16位二进制）");
            valid = false;
        }

        // 检查明文是否有效
        if ((plaintext.length() != 16 || !common.checkBinary(plaintext))) {
            plainTextField.setText("明文内容不规范（必须为16位二进制）");
            valid = false;
        }

        // 如果输入不规范，抛出异常
        if (!valid) {
            throw new Exception("输入不规范，请检查密钥和明文的格式！");
        }
    }

    private void validateAscInput(String ascPlainText, String ascKey) throws Exception {
        boolean valid = true;

        // 检查 ASCII 密钥是否有效
        if (ascKey.length() != 16 || !common.checkBinary(ascKey)) {
            ascKeyField.setText("密钥内容不规范（必须为16位二进制）");
            valid = false;
        }

        // 检查 ASCII 明文是否有效
        if (ascPlainText.isEmpty()) {
            ascPlainTextField.setText("明文内容不规范（不能为空）");
            valid = false;
        }

        // 如果输入不规范，抛出异常
        if (!valid) {
            throw new Exception("输入不规范，请检查ASCII密钥和明文的格式！");
        }
    }
}
