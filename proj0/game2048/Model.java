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
        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.
        this.board.setViewingPerspective(side);
        // Writing logic only for north => The move is UP => each row increments by one
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

        /* for(Tile t : this.board) {
            int n = 1;
            while(this.board.move(t.col(), t.row() + n, t)) {

                n++;
            }
        } */

        // Merge Step
        // Find the topmost non-empty tile in the board
        // If the current tile hasn't been merged before:
        //   If current tile value is equal to the topmost tile perform merge with the
        //   new tile in topmost row
        // Repeat for all rows

        for(Tile t : this.board) {
            if (t == null) continue;
            System.out.println("Processing tile: " + t);
            Tile topMostTile = findTopMostTile(side, t);
            if (topMostTile != null && notMerged(side, topMostTile, mergedArr) && (t.value() == topMostTile.value())) {
                System.out.println("\t Merged" + t + " and " + topMostTile);
                int col = topMostTile.col();
                int row = topMostTile.row();
                mergedArr[getCol(side, col, row)][getRow(side, col, row)] = this.board.move(col, row, t);
                this.score += 2 * t.value();
                changed = true;
            }
        }
        System.out.println("-----");


        // Move Step
        // Find the topmost empty tile in the board
        // Move the tile to that tile
//        for(Tile t : this.board) {
//            if (t == null) continue;
//            // Tile topMostTile = findTopMostRow(t.col(), t.row());
//            int row = findTopMostEmptyRow(side, t.col(), t.row());
//            this.board.move(t.col(), row, t);
//            changed = true;
//        }


//        for(Tile t : this.board) {
//            int col = t.col(), row = t.row();
//            if (row == 0) contine;
//            if(isAboveEmpty()) {
//                this.board.move(col, row + 1, t);
//            } else {
//                if (t.value() == getTileValue(this.board, col, row + 1)) {
//
//                }
//            }
//
//        }

        this.board.setViewingPerspective(Side.NORTH);
        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    public int getCol(Side side, int col, int row) {
        return side.col(col, row, size());
    }

    public int getRow(Side side, int col, int row) {
        return side.col(col, row, size());
    }

    /** Finds the closest non-empty tile in the board */
    private Tile findTopMostTile(Side s, Tile t) {
        int col = getCol(s, t.col(), t.row());
        int r = getRow(s, t.col(), t.row() + 1);
        System.out.println("Initial Coordinates: (" + col + ", " + r + ") ");
        Tile tile = tile(col, r);
        while(r < size() && r >= 0) {
            System.out.print("\t Coordinates: (" + col + ", " + r + ") " + tile);
            System.out.println("\t " + r);
            if (this.board.tile(col, r) != null) return tile;
            tile = this.board.tile(col, r);
            r = getRow(s, col, r + 1);
        }
        return tile;
    }

    /** Finds the topmost empty tile in the board before another non-empty tile */
    private int findTopMostEmptyRow(Side s, int col, int curRow) {
        int r;
        for(r = getRow(s,col, curRow + 1); r < size() && r > 0; r++) {
            if (this.board.tile(col, r) != null) return r - 1;
        }
        return r - 1;
    }
//    public static boolean notMerged(Tile t) {
//        return !(t.next().value() == 2 * t.value());
//    }
    public boolean notMerged(Side s, Tile t, boolean[][] arr) {
        return !arr[getCol(s, t.col(), t.row())][getRow(s, t.col(), t.row())];
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
