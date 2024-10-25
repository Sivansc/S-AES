import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DecryptionView extends JPanel {
    private JTextField plainTextField; // 普通明文输出框
    private JTextField cipherTextField; // 密文输入框
    private JTextField keyField; // 普通密钥输入框
    private JTextField ascPlainTextField; // ASCII 明文输出框
    private JTextField ascCipherTextField; // ASCII 密文输入框
    private JTextField ascKeyField; // ASCII 密钥输入框
    private JButton decryptButton; // 普通解密按钮
    private JButton ascDecryptButton; // ASCII 解密按钮
    private Decoder decoder = new Decoder(); // 使用 Decoder 类
    private Common common = new Common(); // 使用 Common 类

    public DecryptionView() {
        init();
    }

    private void init() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // 组件之间的间距

        JLabel titleLabel = new JLabel("S-AES 解密", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 26));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // 密文输入框
        gbc.gridwidth = 1;
        JLabel cipherLabel = new JLabel("密文：");
        gbc.gridy = 1;
        add(cipherLabel, gbc);

        cipherTextField = new JTextField(40);
        cipherTextField.setBorder(BorderFactory.createTitledBorder("请输入密文"));
        gbc.gridx = 1;
        add(cipherTextField, gbc);

        // 密钥输入框
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel keyLabel = new JLabel("密钥：");
        add(keyLabel, gbc);

        keyField = new JTextField(40);
        keyField.setBorder(BorderFactory.createTitledBorder("请输入16位二进制密钥"));
        gbc.gridx = 1;
        add(keyField, gbc);

        // 明文输出框
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel plainLabel = new JLabel("明文：");
        add(plainLabel, gbc);

        plainTextField = new JTextField(40);
        plainTextField.setEditable(false);
        plainTextField.setBorder(BorderFactory.createTitledBorder("解密后的明文"));
        gbc.gridx = 1;
        add(plainTextField, gbc);

        // 解密按钮
        decryptButton = new JButton("解密");
        decryptButton.setFont(new Font("Serif", Font.PLAIN, 18));
        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String ciphertext = cipherTextField.getText().trim();
                    String key = keyField.getText().trim();

                    // 验证输入规范
                    validateInput(ciphertext, key);

                    // 进行解密
                    String decoded = decoder.decode(ciphertext, key);
                    plainTextField.setText(decoded);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(decryptButton, gbc);

        // ASCII 密文输入框
        gbc.gridwidth = 1;
        gbc.gridy = 5;
        JLabel ascCipherLabel = new JLabel("ASCII 密文：");
        add(ascCipherLabel, gbc);

        ascCipherTextField = new JTextField(40);
        ascCipherTextField.setBorder(BorderFactory.createTitledBorder("请输入ASCII密文"));
        gbc.gridx = 1;
        add(ascCipherTextField, gbc);

        // ASCII 密钥输入框
        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel ascKeyLabel = new JLabel("ASCII 密钥：");
        add(ascKeyLabel, gbc);

        ascKeyField = new JTextField(40);
        ascKeyField.setBorder(BorderFactory.createTitledBorder("请输入16位二进制密钥"));
        gbc.gridx = 1;
        add(ascKeyField, gbc);

        // ASCII 明文输出框
        gbc.gridx = 0;
        gbc.gridy = 7;
        JLabel ascPlainLabel = new JLabel("ASCII 明文：");
        add(ascPlainLabel, gbc);

        ascPlainTextField = new JTextField(40);
        ascPlainTextField.setEditable(false);
        ascPlainTextField.setBorder(BorderFactory.createTitledBorder("解密后的ASCII明文"));
        gbc.gridx = 1;
        add(ascPlainTextField, gbc);

        // ASCII 解密按钮
        ascDecryptButton = new JButton("ASCII 解密");
        ascDecryptButton.setFont(new Font("Serif", Font.PLAIN, 18));
        ascDecryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String ascCipherText = ascCipherTextField.getText().trim();
                    String ascKey = ascKeyField.getText().trim();

                    // 验证输入规范
                    validateAscInput(ascCipherText, ascKey);

                    // 进行ASCII解密
                    StringBuilder res = new StringBuilder();
                    String[] ascArray = common.ascToBin(ascCipherText); // 转换为二进制数组
                    for (String encoded : ascArray) {
                        String decoded = decoder.decode(encoded, ascKey);
                        res.append((char) Integer.parseInt(decoded.substring(0, 8), 2)); // 转换为 char 字符写回
                        res.append((char) Integer.parseInt(decoded.substring(8, 16), 2));
                    }
                    ascPlainTextField.setText(res.toString());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        add(ascDecryptButton, gbc);
    }

    private void validateInput(String ciphertext, String key) throws Exception {
        boolean valid = true;

        // 检查密钥是否有效
        if (key.length() != 16 || !common.checkBinary(key)) {
            keyField.setText("密钥内容不规范（必须为16位二进制）");
            valid = false;
        }

        // 检查密文是否有效
        if (ciphertext.isEmpty()) {
            cipherTextField.setText("密文内容不规范（不能为空）");
            valid = false;
        }

        // 如果输入不规范，抛出异常
        if (!valid) {
            throw new Exception("输入不规范，请检查密钥和密文的格式！");
        }
    }

    private void validateAscInput(String ascCipherText, String ascKey) throws Exception {
        boolean valid = true;

        // 检查 ASCII 密钥是否有效
        if (ascKey.length() != 16 || !common.checkBinary(ascKey)) {
            ascKeyField.setText("密钥内容不规范（必须为16位二进制）");
            valid = false;
        }

        // 检查 ASCII 密文是否有效
        if (ascCipherText.isEmpty()) {
            ascCipherTextField.setText("密文内容不规范（不能为空）");
            valid = false;
        }

        // 如果输入不规范，抛出异常
        if (!valid) {
            throw new Exception("输入不规范，请检查ASCII密钥和密文的格式！");
        }
    }
}
