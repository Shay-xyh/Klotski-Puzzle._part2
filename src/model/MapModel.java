package model;

/**
 * This class is to record the map of one game. For example:
 */
public class MapModel {
    int[][] matrix;
    private int[][] initialMatrix;

    public MapModel(int[][] matrix) {
        this.matrix = matrix;
        this.initialMatrix = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, initialMatrix[i], 0, matrix[0].length);
        }
    }
    public void reset() {
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(initialMatrix[i], 0, matrix[i], 0, matrix[0].length);
        }
    }
    public int getWidth() {
        return this.matrix[0].length;
    }

    public int getHeight() {
        return this.matrix.length;
    }

    public int getId(int row, int col) {
        return matrix[row][col];
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public boolean checkInWidthSize(int col) {
        return col >= 0 && col < matrix[0].length;
    }

    public boolean checkInHeightSize(int row) {
        return row >= 0 && row < matrix.length;
    }
}
