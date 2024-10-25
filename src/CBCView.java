import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CBCView extends JPanel {
    private JTextField keyField, ivField, originalField, secretField, encodedField, decodedField;
    private JButton encodeButton, decodeButton;
    private CBC cbc = new CBC();  // 初始化 CBC 类
    private Common common = new Common();  // 初始化通用类

    public CBCView() {
        init();
    }

    private void init() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // 组件之间的间距
        gbc.fill = GridBagConstraints.HORIZONTAL;  // 让组件水平填充

        // 标题
        JLabel titleLabel = new JLabel("CBC 模式加解密", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // 明文输入框
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel originalLabel = new JLabel("明文：");
        add(originalLabel, gbc);

        originalField = new JTextField(30);
        originalField.setBorder(BorderFactory.createTitledBorder("请输入待加密的明文"));
        gbc.gridx = 1;
        add(originalField, gbc);

        // 密文输入框
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel secretLabel = new JLabel("密文：");
        add(secretLabel, gbc);

        secretField = new JTextField(30);
        secretField.setBorder(BorderFactory.createTitledBorder("请输入待解码的密文"));
        gbc.gridx = 1;
        add(secretField, gbc);

        // 密钥输入框
        gbc.gridy = 3;
        gbc.gridx = 0;
        JLabel keyLabel = new JLabel("密钥：");
        add(keyLabel, gbc);

        keyField = new JTextField(30);
        keyField.setBorder(BorderFactory.createTitledBorder("请输入16位二进制密钥"));
        gbc.gridx = 1;
        add(keyField, gbc);

        // 初始向量 (IV) 输入框
        gbc.gridy = 4;
        gbc.gridx = 0;
        JLabel ivLabel = new JLabel("初始向量 (IV)：");
        add(ivLabel, gbc);

        ivField = new JTextField(30);
        ivField.setBorder(BorderFactory.createTitledBorder("请输入16位二进制IV"));
        gbc.gridx = 1;
        add(ivField, gbc);

        // 编码结果输出框
        gbc.gridy = 5;
        gbc.gridx = 0;
        JLabel encodedLabel = new JLabel("编码结果：");
        add(encodedLabel, gbc);

        encodedField = new JTextField(30);
        encodedField.setEditable(false);
        encodedField.setBorder(BorderFactory.createTitledBorder("显示编码后的密文"));
        gbc.gridx = 1;
        add(encodedField, gbc);

        // 解码结果输出框
        gbc.gridy = 6;
        gbc.gridx = 0;
        JLabel decodedLabel = new JLabel("解码结果：");
        add(decodedLabel, gbc);

        decodedField = new JTextField(30);
        decodedField.setEditable(false);
        decodedField.setBorder(BorderFactory.createTitledBorder("显示解码后的明文"));
        gbc.gridx = 1;
        add(decodedField, gbc);

        // 按钮容器，用于保持按钮大小一致
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));  // 使用 GridLayout
        Dimension buttonSize = new Dimension(150, 40);  // 设置按钮大小

        encodeButton = new JButton("CBC 编码");
        encodeButton.setFont(new Font("Serif", Font.PLAIN, 18));
        encodeButton.setPreferredSize(buttonSize);
        encodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performEncoding();
            }
        });

        decodeButton = new JButton("CBC 解码");
        decodeButton.setFont(new Font("Serif", Font.PLAIN, 18));
        decodeButton.setPreferredSize(buttonSize);
        decodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performDecoding();
            }
        });

        // 将按钮添加到按钮容器中
        buttonPanel.add(encodeButton);
        buttonPanel.add(decodeButton);

        // 将按钮容器添加到主布局中
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 2;  // 占据两列
        add(buttonPanel, gbc);
    }

    private void performEncoding() {
        String key = keyField.getText().trim();
        String iv = ivField.getText().trim();
        String original = originalField.getText().trim();

        if (validateInput(key, iv)) {
            String encoded = cbc.cbcEncode(common.chnToBinStr(original), key, iv);
            encodedField.setText(encoded);
        }
    }

    private void performDecoding() {
        String key = keyField.getText().trim();
        String iv = ivField.getText().trim();
        String secret = secretField.getText().trim();  // 从密文输入框获取密文

        if (validateInput(key, iv)) {
            String decoded = cbc.cbcDecode(common.chnToBinStr(secret), key, iv);
            decodedField.setText(decoded);
        }
    }

    private boolean validateInput(String key, String iv) {
        boolean valid = true;

        if (key.length() != 16 || !common.checkBinary(key)) {
            keyField.setText("密钥内容不规范");
            valid = false;
        }
        if (iv.length() != 16 || !common.checkBinary(iv)) {
            ivField.setText("初始向量不规范");
            valid = false;
        }

        return valid;
    }
}
