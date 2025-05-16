// GameController.java 完整代码
package controller;

import model.Direction;
import model.MapModel;
import view.game.GamePanel;

public class GameController {
    private final GamePanel view;
    private final MapModel model;

    public GameController(GamePanel view, MapModel model) {
        this.view = view;
        this.model = model;
        view.setController(this);
    }

    public void restartGame() {
        model.reset();
        view.reset();
        System.out.println("Game restarted.");
    }

    public boolean doMove(int row, int col, Direction direction) {
        int currentId = getCompleteBoxId(row, col);
        if (currentId == 0) return false;

        int currentUniqueId = model.getUniqueId(row, col);
        int currentWidth = getCurrentWidth(currentId);
        int currentHeight = getCurrentHeight(currentId);

        int newRow = row + direction.getRow();
        int newCol = col + direction.getCol();

        if (!isMoveValid(newRow, newCol, currentWidth, currentHeight, currentUniqueId)) {
            return false;
        }

        return performMove(row, col, newRow, newCol, currentWidth, currentHeight, currentUniqueId);
    }

    // GameController.java 修改后的 getCompleteBoxId 方法
    private int getCompleteBoxId(int row, int col) {
        int baseId = model.getId(row, col);
        if (baseId <= 0) return 0;

        switch (baseId) {
            case 2: // 关羽（横向2x1）
                // 检查左右相邻是否存在2
                boolean rightValid = (col + 1 < model.getWidth()) && (model.getId(row, col + 1) == 2);
                boolean leftValid = (col - 1 >= 0) && (model.getId(row, col - 1) == 2);
                return (rightValid || leftValid) ? 2 : 0;
            case 3: case 4: case 5: case 6: // 纵向1x2
                // 检查上下相邻是否存在相同ID
                boolean downValid = (row + 1 < model.getHeight()) && (model.getId(row + 1, col) == baseId);
                boolean upValid = (row - 1 >= 0) && (model.getId(row - 1, col) == baseId);
                return (downValid || upValid) ? baseId : 0;
            case 7: // 曹操（2x2）
                // 寻找可能的左上角
                int startRow = row;
                while (startRow > 0 && model.getId(startRow - 1, col) == 7) startRow--;
                int startCol = col;
                while (startCol > 0 && model.getId(row, startCol - 1) == 7) startCol--;
                // 验证2x2区域
                if (startRow + 1 >= model.getHeight() || startCol + 1 >= model.getWidth()) return 0;
                return (model.getId(startRow, startCol) == 7 &&
                        model.getId(startRow, startCol + 1) == 7 &&
                        model.getId(startRow + 1, startCol) == 7 &&
                        model.getId(startRow + 1, startCol + 1) == 7) ? 7 : 0;
            default:
                return baseId; // 1x1方块
        }
    }

    private boolean isMoveValid(int newRow, int newCol,
                                int width, int height,
                                int currentUniqueId) {
        // 边界检查
        if (newRow < 0 || newRow + height > model.getHeight()) return false;
        if (newCol < 0 || newCol + width > model.getWidth()) return false;

        // 碰撞检测
        for (int r = newRow; r < newRow + height; r++) {
            for (int c = newCol; c < newCol + width; c++) {
                int cellUniqueId = model.getUniqueId(r, c);
                if (cellUniqueId != 0 && cellUniqueId != currentUniqueId) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean performMove(int oldRow, int oldCol,
                                int newRow, int newCol,
                                int width, int height,
                                int uniqueId) {
        // 备份原始数据（包含ID和uniqueId）
        BackupData backup = backupArea(oldRow, oldCol, width, height);

        // 尝试清空原区域
        if (!clearOldPosition(oldRow, oldCol, width, height, uniqueId)) {
            restoreBackup(oldRow, oldCol, backup); // 恢复完整数据
            return false;
        }

        // 尝试写入新位置
        if (!setNewPosition(newRow, newCol, width, height, backup.idBackup[0][0], uniqueId)) {
            restoreBackup(oldRow, oldCol, backup); // 恢复完整数据
            return false;
        }

        return true;
    }

    private BackupData backupArea(int row, int col, int width, int height) {
        int[][] idBackup = new int[height][width];
        int[][] uniqueIdBackup = new int[height][width];
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                int actualRow = row + r;
                int actualCol = col + c;
                if (model.checkInHeightSize(actualRow) && model.checkInWidthSize(actualCol)) {
                    idBackup[r][c] = model.getId(actualRow, actualCol);
                    uniqueIdBackup[r][c] = model.getUniqueId(actualRow, actualCol);
                }
            }
        }
        return new BackupData(idBackup, uniqueIdBackup);
    }

    private static class BackupData {
        final int[][] idBackup;
        final int[][] uniqueIdBackup;

        BackupData(int[][] idBackup, int[][] uniqueIdBackup) {
            this.idBackup = idBackup;
            this.uniqueIdBackup = uniqueIdBackup;
        }
    }

    private boolean clearOldPosition(int row, int col,
                                     int width, int height,
                                     int uniqueId) {
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                int actualRow = row + r;
                int actualCol = col + c;
                if (model.checkInHeightSize(actualRow) && model.checkInWidthSize(actualCol)) {
                    if (model.getUniqueId(actualRow, actualCol) == uniqueId) {
                        if (!model.setIdSafely(actualRow, actualCol, 0)) return false;
                        model.setUniqueId(actualRow, actualCol, 0);
                    }
                }
            }
        }
        return true;
    }

    private boolean setNewPosition(int row, int col,
                                   int width, int height,
                                   int id, int uniqueId) {
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                int actualRow = row + r;
                int actualCol = col + c;
                if (!model.checkInHeightSize(actualRow) || !model.checkInWidthSize(actualCol)) {
                    return false;
                }
                if (!model.setIdSafely(actualRow, actualCol, id)) return false;
                model.setUniqueId(actualRow, actualCol, uniqueId);
            }
        }
        return true;
    }

    private void restoreBackup(int row, int col, BackupData backup) {
        for (int r = 0; r < backup.idBackup.length; r++) {
            for (int c = 0; c < backup.idBackup[0].length; c++) {
                int actualRow = row + r;
                int actualCol = col + c;
                if (model.checkInHeightSize(actualRow) && model.checkInWidthSize(actualCol)) {
                    model.setId(actualRow, actualCol, backup.idBackup[r][c]);
                    model.setUniqueId(actualRow, actualCol, backup.uniqueIdBackup[r][c]);
                }
            }
        }
    }

    private int getCurrentWidth(int id) {
        return switch (id) {
            case 2, 7 -> 2;
            default -> 1;
        };
    }

    private int getCurrentHeight(int id) {
        return switch (id) {
            case 3, 4, 5, 6, 7 -> 2;
            default -> 1;
        };
    }
}