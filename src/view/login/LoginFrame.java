// view/login/LoginFrame.java
package view.login;

import view.FrameUtil;
import view.game.GameFrame;
import model.UserManager;
import model.MapModel;
import view.start.LoginSelectionFrame;
import tool.tool;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField username;
    private JPasswordField password;
    private JButton submitBtn;
    private JButton registerBtn;
    private JButton returnBtn;
    private UserManager userManager;

    public LoginFrame(int width, int height) {
        this.setTitle("Login");
        this.setLayout(null);
        this.setSize(width, height);

        userManager = new UserManager();// 初始化用户管理器
        JLabel userLabel = FrameUtil.createJLabel(this, new Point(tool.USERNAME_PASSWORD_X, tool.USERNAME_Y), tool.W_USERNAME_PASSWORD, tool.H_ALL, "Username:");//username位置
        JLabel passLabel = FrameUtil.createJLabel(this, new Point(tool.USERNAME_PASSWORD_X, tool.PASSWORD_Y), tool.W_USERNAME_PASSWORD, tool.H_ALL, "Password:");//password位置
        username = FrameUtil.createJTextField(this, new Point(tool.USERNAME_PASSWORD_X+tool.W_USERNAME_PASSWORD, tool.USERNAME_Y), tool.W_USERNAME_PASSWORD_WINDOW, tool.H_ALL);//username窗口位置
        password = new JPasswordField();
        password.setBounds(tool.USERNAME_PASSWORD_X+tool.W_USERNAME_PASSWORD, tool.PASSWORD_Y, tool.W_USERNAME_PASSWORD_WINDOW, tool.H_ALL);//password窗口位置
        this.add(password);

        submitBtn = FrameUtil.createButton(this, "Login", new Point(tool.USERNAME_PASSWORD_X,tool.BUTTON_Y ), tool.W_BUTTON, tool.H_ALL);
        registerBtn = FrameUtil.createButton(this, "Register", new Point(tool.REGISTER_X, tool.BUTTON_Y), tool.W_BUTTON, tool.H_ALL);
        returnBtn = FrameUtil.createButton(this, "Return",new Point(tool.RETURN_X,tool.H_ALL),tool.W_BUTTON,tool.H_ALL);

        submitBtn.addActionListener(e -> {
            String user = username.getText();
            String pass = new String(password.getPassword());

            if (userManager.login(user, pass)) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                // 启动游戏界面
                int[][] matrix = {{1, 4, 4, 1}, {1, 4, 4, 1}, {3, 2, 2, 3}, {3, 0, 0, 3}, {1, 1, 1, 1}};
                GameFrame gameFrame = new GameFrame(600, 450, new MapModel(matrix));
                gameFrame.setVisible(true);
                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password");
            }
        });

        registerBtn.addActionListener(e -> {
            new RegistrationFrame(this).setVisible(true); // 打开注册页面
            this.setVisible(false);
        });

        returnBtn.addActionListener(e -> {
            // 返回到 LoginSelectionFrame
            new LoginSelectionFrame(tool.LOGIN_FRAME_W, tool.LOGIN_FRAME_H).setVisible(true);
            this.setVisible(false); // 隐藏当前登录界面
        });

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
