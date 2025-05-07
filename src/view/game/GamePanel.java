package view.game;

import controller.GameController;
import model.Direction;
import model.MapModel;
import tool.tool;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
/**
 * It is the subclass of ListenerPanel, so that it should implement those four methods: do move left, up, down ,right.
 * The class contains a grids, which is the corresponding GUI view of the matrix variable in MapMatrix.
 */
public class GamePanel extends ListenerPanel {
    private List<BoxComponent> boxes;
    private MapModel model;
    private GameController controller;
    private JLabel stepLabel;
    private int steps;
    private final int GRID_SIZE = tool.GRID_SIZE;
    private BoxComponent selectedBox;
    private boolean victory = false;
    private BufferedImage backgroundImage;


    public GamePanel(MapModel model) {

        boxes = new ArrayList<>();
        this.setVisible(true);
        this.setFocusable(true);
        this.setLayout(null);
        this.setSize(model.getWidth() * GRID_SIZE + (int)(GRID_SIZE*7/5), model.getHeight() * GRID_SIZE +(int)(GRID_SIZE*12/5));
        this.model = model;
        this.selectedBox = null;
        backgroundImage = tool.BACKGROUND;//new ImageIcon("resources/background.png").getImage();
        initialGame();  // 初始化游戏棋盘
    }

    // 初始化棋盘
    // GamePanel.java
    public void initialGame() {
        this.steps = 0;



        // 创建一个与模型相同大小的二维数组，并初始化
        int[][] map = new int[model.getHeight()][model.getWidth()];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = model.getId(i, j);  // 从模型获取数据填充到地图
            }
        }

        // 遍历二维数组，构建方块
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                BoxComponent box = null;
                BufferedImage characterImage = null;
                // 根据map数组中的值来决定方块的颜色、大小及位置
                if (map[i][j] == 1) {  // 士兵块
                    characterImage = tool.SOLDIER_IMAGE;
                    box = new BoxComponent(Color.ORANGE, i, j,characterImage);
                    box.setSize(GRID_SIZE, GRID_SIZE);
                    map[i][j] = 0;  // 更新数组位置
                } else if (map[i][j] == 2) {  // 关羽块
                    characterImage = tool.GUANYU_IMAGE;
                    box = new BoxComponent(Color.PINK, i, j,characterImage);
                    box.setSize(GRID_SIZE * 2, GRID_SIZE);
                    map[i][j] = 0;
                    map[i][j + 1] = 0;  // 横向占用2格
                } else if (map[i][j] == 3) {  // 马超 1x2 方块
                    characterImage = tool.MACHAO_IMAGE;
                    box = new BoxComponent(Color.YELLOW, i, j,characterImage);
                    box.setSize(GRID_SIZE, GRID_SIZE * 2);
                    map[i][j] = 0;
                    map[i + 1][j] = 0;  // 纵向占用2格
                } else if (map[i][j] == 4) {  // 黄忠 1x2 方块
                    characterImage = tool.HUANGZHONG_IMAGE;
                    box = new BoxComponent(Color.BLUE, i, j,characterImage);
                    box.setSize(GRID_SIZE, GRID_SIZE * 2);
                    map[i][j] = 0;
                    map[i + 1][j] = 0; // 纵向占用2格
                }else if (map[i][j] == 5) {  // 赵云 1x2 方块
                    characterImage = tool.ZHAOYUN_IMAGE;
                    box = new BoxComponent(Color.RED, i, j,characterImage);
                    box.setSize(GRID_SIZE, GRID_SIZE * 2);
                    map[i][j] = 0;
                    map[i + 1][j] = 0;  // 纵向占用2格
                }else if (map[i][j] == 6) {  // 张飞 1x2 方块
                    characterImage = tool.ZHANGFEI_IMAGE;
                    box = new BoxComponent(Color.BLUE, i, j,characterImage);
                    box.setSize(GRID_SIZE, GRID_SIZE * 2);
                    map[i][j] = 0;
                    map[i + 1][j] = 0;  // 纵向占用2格
                } else if (map[i][j] == 7) {  // 曹操（2x2）
                    characterImage = tool.CAOCAO_IMAGE;
                    box = new BoxComponent(Color.GREEN, i, j,characterImage);
                    box.setSize(GRID_SIZE * 2, GRID_SIZE * 2);
                    map[i][j] = 0;
                    map[i + 1][j] = 0;
                    map[i][j + 1] = 0;
                    map[i + 1][j + 1] = 0;  // 占用2x2区域
                }

                // 如果创建了方块，设置位置并添加到面板
                if (box != null) {
                    box.setLocation(j * GRID_SIZE + (int)(0.7*GRID_SIZE), i * GRID_SIZE + (int)(3*GRID_SIZE/2) );  // 设置位置
                    boxes.add(box);  // 将方块添加到列表
                    this.add(box);  // 将方块添加到面板
                }
            }
        }

        // 在初始化后立即检查是否胜利
        checkVictory();  // 检查是否达到胜利条件
        this.repaint();  // 重绘面板，确保所有方块都显示
    }

    public int get_panel_Width(){
      return model.getWidth() * GRID_SIZE + (int)(GRID_SIZE*7/5) + GRID_SIZE;
    }



    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 绘制背景图片
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }

        // 绘制棋盘等其他组件
        g.setColor(Color.LIGHT_GRAY);
        int x=(int)(0.7*GRID_SIZE);
        int y=(int)(3*GRID_SIZE/2);
        g.fillRect(x, y, model.getWidth() * GRID_SIZE, model.getHeight() * GRID_SIZE);
        Border border = BorderFactory.createLineBorder(Color.DARK_GRAY, 2);
        this.setBorder(border);
        try{
            Thread.sleep(10);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        repaint();

    }

    public void reset() {
        this.removeAll();       // Remove all existing components
        boxes.clear();          // Clear the list of BoxComponents
        selectedBox = null;     // Deselect any selected box
        steps = 0;              // Reset the step count

        if (stepLabel != null) {
            stepLabel.setText("Step: 0"); // Reset the displayed step count
        }

        initialGame();          // Reinitialize the game board with initial layout
        this.repaint();         // Repaint the panel
        this.requestFocusInWindow(); // Focus on the panel to capture keyboard events
    }


    @Override
    public void doMouseClick(Point point) {
        Component component = this.getComponentAt(point);
        if (component instanceof BoxComponent clickedComponent) {
            if (selectedBox == null) {
                selectedBox = clickedComponent;
                selectedBox.setSelected(true);
            } else if (selectedBox != clickedComponent) {
                selectedBox.setSelected(false);
                clickedComponent.setSelected(true);
                selectedBox = clickedComponent;
            } else {
                clickedComponent.setSelected(false);
                selectedBox = null;
            }
        }
    }

    @Override
    public void doMoveRight() {
        System.out.println("Click VK_RIGHT");
        if (selectedBox != null) {
            if (controller.doMove(selectedBox.getRow(), selectedBox.getCol(), Direction.RIGHT)) {
                afterMove();
            }
        }
    }

    @Override
    public void doMoveLeft() {
        System.out.println("Click VK_LEFT");
        if (selectedBox != null) {
            if (controller.doMove(selectedBox.getRow(), selectedBox.getCol(), Direction.LEFT)) {
                afterMove();
            }
        }
    }

    @Override
    public void doMoveUp() {
        System.out.println("Click VK_Up");
        if (selectedBox != null) {
            if (controller.doMove(selectedBox.getRow(), selectedBox.getCol(), Direction.UP)) {
                afterMove();
            }
        }
    }

    @Override
    public void doMoveDown() {
        System.out.println("Click VK_DOWN");
        if (selectedBox != null) {
            if (controller.doMove(selectedBox.getRow(), selectedBox.getCol(), Direction.DOWN)) {
                afterMove();
            }
        }
    }

    public void afterMove() {
        this.steps++;
        this.stepLabel.setText(String.format("Step: %d", this.steps));
        checkVictory(); // 新增：每次移动后检查是否胜利
    }
    private void checkVictory() {
        if (victory) return;  // 已经胜利过，不再弹窗

        // 胜利位置：曹操块左上角在 row=3, col=1
        for (BoxComponent box : boxes) {
            // 绿色 2×2 为曹操块
            if (box.getWidth() == GRID_SIZE * 2 && box.getHeight() == GRID_SIZE * 2) {
                if (box.getRow() == 3 && box.getCol() == 1) {
                    victory = true;  // 标记已胜利
                    JOptionPane.showMessageDialog(
                            this,
                            String.format("🎉 Victory! You won in %d steps.", steps),
                            "Game Over",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    break;
                }
            }
        }
    }

    public void setStepLabel(JLabel stepLabel) {
        this.stepLabel = stepLabel;
    }


    public void setController(GameController controller) {
        this.controller = controller;
    }

    public BoxComponent getSelectedBox() {
        return selectedBox;
    }

    public int getGRID_SIZE() {
        return GRID_SIZE;
    }
}
