package game2048;

import java.sql.Array;
import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author TODO: YOUR NAME HERE
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        boolean[][] mergedArr = new boolean[size()][size()];
        for(int i = 0; i < size(); i++) {
            for(int j = 0; j < size(); j++) {
                mergedArr[i][j] = false;
            }
        }
        this.board.setViewingPerspective(side);
        // Two operations => Move and Merge
        // Merge First then Move
        // Example:
        // |   |   |   | 2 |
        // | 2 |   | 4 | 2 |
        // | 2 | 2 | 2 |   |
        // | 2 | 2 | 2 | 4 |

        // Merge
        // |   |   |   | 4 |
        // | 4 |   | 4 |   |
        // |   | 4 | 4 |   |
        // | 2 |   |   | 4 |

        // Move
        // | 4 | 4 | 4 | 4 |
        // | 2 |   | 4 | 4 |
        // |   |   |   |   |
        // |   |   |   |   |
        // Merge Step
        // Iterate row wise starting from the farthest row from perspective (e.g. for North go start from top most row, for South start from bottom most)
        // Find the topmost non-empty tile in the board
        // If the current tile hasn't been merged before:
        //   If current tile value is equal to the topmost tile perform merge with the
        //   new tile in topmost row
        // Repeat for all rows
        for(int row = size() - 1; row >= 0; row--) {
            for(int col = size() - 1; col >= 0; col--) {
                Tile t = tile(col, row);
                if (t == null) continue;
                // System.out.println("Processing Board Coordinates: (" + col + ", " + row + ")" + "[(" + getCol(side, col, row) + ", " + getRow(side, col, row) + ")]\t" + "Processing tile: " + tile(col, row));
                // Check if already merged
                int topMostRow = findTopMostRow(col, row);
                if(topMostRow < size() && canMerge(tile(col, topMostRow), t, mergedArr)) {
                    Tile topMostTile = tile(col, topMostRow);
                    // System.out.println("\t Merge performed between " + t + " and " + topMostTile);
                    mergedArr[topMostTile.col()][topMostTile.row()] = this.board.move(col, topMostRow, t);
                    this.score += 2 * t.value();
                    changed = true;
                }
            }
        }

        // Move Step
        // Move each tile into the top most empty tile possible
        for(int row = size() - 1; row >= 0; row--) {
            for(int col = size() - 1; col >= 0; col--) {
                Tile t = tile(col, row);
                if (t == null) continue;
                // System.out.println("Processing Board Coordinates: (" + col + ", " + row + ")" + "[(" + getCol(side, col, row) + ", " + getRow(side, col, row) + ")]\t" + "Processing tile: " + tile(col, row));
                int topMostRow = findTopMostRow(col, row) - 1;
                if(topMostRow > row && topMostRow < size()) {
                    // System.out.println("\t Moved " + t + "to (" + getCol(side, col, row) + ", " + getRow(side, col, row) );
                    this.board.move(col, topMostRow, t);
                    changed = true;
                }
            }
        }

        this.board.setViewingPerspective(Side.NORTH);
        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    /** Helper function for debugging purpose, retrieves tile column coordinates based on iteration values */
    public int getCol(Side side, int col, int row) {
        return side.col(col, row, size());
    }

    /** Helper function for debugging purpose, retrieves tile row coordinates based on iteration values */
    public int getRow(Side side, int col, int row) {
        return side.row(col, row, size());
    }

    /** Finds the closest non-empty tile in the board
     *  Returns the row index of the tile */
    private int findTopMostRow(int col, int row) {
        int r = row + 1;
        // System.out.println("\t Initial Coordinates: (" + col + ", " + r + ") ");
        Tile tile;
        while(r < size() && r >= 0) {
            tile = tile(col, r);
            // System.out.println("\t Coordinates: (" + col + ", " + r + ") " + tile);
            if (tile(col, r) != null) return r;
            r = r + 1;
        }
        return r;
    }

    /** Checks if the tile has already been merged or not */
    public boolean isMerged(Tile t, boolean[][] arr) {
        return arr[t.col()][t.row()];
    }

    /** Checks if two tiles can be merged based on their values and whether they have been merged before */
    public boolean canMerge(Tile mergeTile, Tile t, boolean[][] arr) {
        return !isMerged(mergeTile, arr) && (t.value() == mergeTile.value());
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        for(Tile t : b) {
            if(t == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        for(Tile t : b) {
            if(t != null && t.value() == MAX_PIECE) return true;
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        return emptySpaceExists(b) || adjacentWithSameValueExists(b);
    }

    public static boolean adjacentWithSameValueExists(Board b) {
        for(Tile t : b) {
            int tVal = t.value();
            int size = b.size();
            int col = t.col(), row = t.row();
            /* Looking for neighbours with same value */
            if (
                    (isInBoardRange(col, row + 1, size)
                            && (tVal == getTileValue(b, col, row + 1)))
                    || (isInBoardRange(col + 1, row, size)
                            && (tVal == getTileValue(b, col + 1, row)))
                    || (isInBoardRange(col, row - 1, size)
                            && (tVal == getTileValue(b, col, row - 1)))
                    || (isInBoardRange(col - 1, row, size)
                            && (tVal == getTileValue(b, col - 1, row)))
            ) return true;
        }
        return false;
    }

    public static boolean isInBoardRange(int col, int row, int size) {
        return (col > 0) && (col < size) && (row > 0) && (row < size);
    }

    public static int getTileValue(Board b, int col, int row) {
        return b.tile(col, row).value();
    }

    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}
