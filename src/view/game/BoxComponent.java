package view.game;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BoxComponent extends JComponent {
    private Color color;
    private int row;
    private int col;
    private boolean isSelected;
    private BufferedImage characterImage;

    public BoxComponent(Color color, int row, int col, BufferedImage characterImage) {
        this.color = color;
        this.row = row;
        this.col = col;
        this.isSelected = false;
        this.characterImage=characterImage;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);  // 设置颜色
        g.fillRect(0, 0, getWidth(), getHeight());  // 绘制方块

        if(characterImage!=null){
            g.drawImage(characterImage, 0, 0, getWidth(), getHeight(), this);
        }
        // 绘制边框
        Border border = isSelected ? BorderFactory.createLineBorder(Color.RED, 3) :
                BorderFactory.createLineBorder(Color.DARK_GRAY, 1);
        this.setBorder(border);
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
        this.repaint();
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
