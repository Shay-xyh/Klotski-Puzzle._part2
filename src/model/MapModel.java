// MapModel.java 完整代码（新增关键方法）
package model;

import java.util.Arrays;

public class MapModel {
    private int[][] map;
    private int[][] uniqueIds;
    private int[][] initialMap;
    private final int height;
    private final int width;

    public MapModel(int[][] map) {
        this.height = map.length;
        this.width = map[0].length;
        initializeMaps(map);
    }

    private void initializeMaps(int[][] sourceMap) {
        this.map = new int[height][width];
        this.uniqueIds = new int[height][width];
        this.initialMap = new int[height][width];

        for (int i = 0; i < height; i++) {
            System.arraycopy(sourceMap[i], 0, map[i], 0, width);
            System.arraycopy(sourceMap[i], 0, initialMap[i], 0, width);
        }
    }

    public synchronized boolean setIdSafely(int row, int col, int id) {
        if (checkInHeightSize(row) && checkInWidthSize(col)) {
            if (id == 0 || map[row][col] == 0) {
                map[row][col] = id;
                return true;
            }
        }
        return false;
    }

    // 保留原有方法并增加同步控制
    public synchronized int getId(int row, int col) {
        return checkInHeightSize(row) && checkInWidthSize(col) ? map[row][col] : -1;
    }

    public synchronized void setId(int row, int col, int id) {
        if (checkInHeightSize(row) && checkInWidthSize(col)) {
            map[row][col] = id;
        }
    }

    public synchronized int getUniqueId(int row, int col) {
        return checkInHeightSize(row) && checkInWidthSize(col) ? uniqueIds[row][col] : 0;
    }

    public synchronized void setUniqueId(int row, int col, int uniqueId) {
        if (checkInHeightSize(row) && checkInWidthSize(col)) {
            uniqueIds[row][col] = uniqueId;
        }
    }

    // 其他原有方法保持同步控制
    public synchronized void reset() {
        for (int i = 0; i < height; i++) {
            System.arraycopy(initialMap[i], 0, map[i], 0, width);
            Arrays.fill(uniqueIds[i], 0);
        }
    }

    public boolean checkInHeightSize(int row) {
        return row >= 0 && row < height;
    }

    public boolean checkInWidthSize(int col) {
        return col >= 0 && col < width;
    }

    // 新增辅助方法
    public synchronized void printDebugInfo() {
        System.out.println("Current Map State:");
        for (int[] row : map) {
            System.out.println(Arrays.toString(row));
        }
        System.out.println("Unique IDs:");
        for (int[] row : uniqueIds) {
            System.out.println(Arrays.toString(row));
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}