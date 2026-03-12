package simulation;
import java.util.ArrayList;
import java.util.List;

public final class Map {
    private int n;
    private int m;
    private Cell[][] cells;
    private static final int NR_OF_NEIGHBOURS = 4;
    public Map(final int n, final int m, final Cell[][] cells) {
        this.n = n;
        this.m = m;
        this.cells = new Cell[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                this.cells[i][j] = new Cell(i, j);
            }
        }
    }
    public int getN() {
        return n;
    }
    public int getM() {
        return m;
    }

    /**
     * Checks if cell positions are within the map itself.
     * @param x index row
     * @param y index column
     * @return true if cell positions are in map, false if not
     */
    public boolean checkCellPos(final int x, final int y) {
        return x >= 0 && y >= 0 && x < n && y < m;
    }

    /**
     * Returns cell data from receiving its coordinates.
     * @param x index row
     * @param y index column
     * @return the Cell type cell that is at the received coordinates.2
     */
    public Cell getCell(final int x, final int y) {
        if (checkCellPos(x, y)) {
            return cells[x][y];
        }
        return null;
    }

    /**
     * Returns neighbours of a cell, as a list, sorted in the correct order
     * @param x index row
     * @param y index column
     * @return a List containing the cell's neighbours in the requested order
     */
    public List<Cell> getNeighbours(final int x, final int y) {
        List<Cell> neighbours = new ArrayList<>();
        for (int i = 0; i < NR_OF_NEIGHBOURS; i++) {
            if (i == 0 && checkCellPos(x, y + 1)) {
                neighbours.add(cells[x][y + 1]);
            } else if (i == 1 && checkCellPos(x + 1, y)) {
                neighbours.add(cells[x + 1][y]);
            } else if (i == 2 && checkCellPos(x, y - 1)) {
                neighbours.add(cells[x][y - 1]);
            } else if (i == NR_OF_NEIGHBOURS - 1 && checkCellPos(x - 1, y)) {
                neighbours.add(cells[x - 1][y]);
            }
        }
        return neighbours;
    }





}
