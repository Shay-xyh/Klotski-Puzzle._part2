// view/login/RegistrationFrame.java
package view.login;

import model.UserManager;
import tool.tool;
import view.FrameUtil;
import view.start.LoginSelectionFrame;

import javax.swing.*;
import java.awt.*;

public class RegistrationFrame extends JFrame {
    private JTextField username;
    private JPasswordField password;
    private JButton registerBtn;
    private UserManager userManager;
    private JButton returnBtn;

    public RegistrationFrame(JFrame parent) {
        this.setTitle("Register");
        this.setLayout(null);
        this.setSize(tool.LOGIN_FRAME_W, tool.LOGIN_FRAME_H);

        userManager = new UserManager();  // 初始化用户管理器

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(tool.USERNAME_PASSWORD_X, tool.USERNAME_Y, tool.W_USERNAME_PASSWORD, tool.H_ALL);//username位置
        this.add(userLabel);

        username = new JTextField();
        username.setBounds(tool.USERNAME_PASSWORD_X+tool.W_USERNAME_PASSWORD, tool.USERNAME_Y, tool.W_USERNAME_PASSWORD_WINDOW, tool.H_ALL);//username窗口位置
        this.add(username);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(tool.USERNAME_PASSWORD_X, tool.PASSWORD_Y, tool.W_USERNAME_PASSWORD, tool.H_ALL);//password位置
        this.add(passLabel);

        password = new JPasswordField();
        password.setBounds(tool.USERNAME_PASSWORD_X+tool.W_USERNAME_PASSWORD, tool.PASSWORD_Y, tool.W_USERNAME_PASSWORD_WINDOW, tool.H_ALL);//password窗口位置
        this.add(password);

        registerBtn = new JButton("Register");
        registerBtn.setBounds(tool.REGISTER_X, tool.BUTTON_Y, tool.W_BUTTON, tool.H_ALL);//register位置
        this.add(registerBtn);
        returnBtn = FrameUtil.createButton(this, "Return",new Point(20,40),tool.W_BUTTON,tool.H_ALL);//return位置

        returnBtn.addActionListener(e -> {
            // 返回到 LoginSelectionFrame
            new LoginFrame(tool.LOGIN_FRAME_W, tool.LOGIN_FRAME_H).setVisible(true);
            this.setVisible(false); // 隐藏当前登录界面
        });

        registerBtn.addActionListener(e -> {
            String user = username.getText();
            String pass = new String(password.getPassword());

            if (userManager.register(user, pass)) {
                JOptionPane.showMessageDialog(this, "Registration Successful!");
                parent.setVisible(true);  // 返回登录界面
                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "User already exists!");
            }
        });

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
