package site.petrumugurel.connect4;

import android.util.Log;

import java.util.Random;


/**********************************************************************************
 *
 *      The Board can take any reasonable size
 *      (between MIN_ROWS - MAX_ROWS and MIN_COLUMNS - MAX_COLUMNS)
 *
 *
 *                   |  0  |  1  |  2  |  3    MAX_COLUMNS
 *                -------------------------- ....   |
 *                0  |     |     |     |     ....   |
 *                1  |     |     |     |     ....   |
 *                2  |     |     |     |     ....   |
 *                3  |     |     |     |     ....   |
 *                .  |     .     .     .     ....   |
 *                .  |     .     .     .     ....   |
 *         MAX_ROWS  |     .     .     .     ....   |
 *               ------------------------------------
 *
 *
 *
 *      It has all the required members and provides all needed methods to make it possible to
 *          - create a board for a game similar to "Connect 4" with any reasonable number of rows
 *          and columns and also any number of disks needed to win;
 *          - store two different types of disks into any of its free positions;
 *          - check for winners on any row/column/diagonal;
 *
 *      In short this is to contain all the underlying logic of the game.
 *
 ************************************************************************************/




/**
 * <p>A basic board of connect 4.</p>
 * <p>Makes us of the singleton pattern.<br>Handles everything about the board.<br>Can easily adapt to any reasonable board size and number of required disks in a line to
 * win.</p>
 * <p>For the time being uses a very  naive AI, but can be rewritten in the future.</p>
 */
public class Board {
    /**
     * <p><br>Only way to get an instance of the {@link Board} - singleton class.</p>
     * <p>For to use it you must first call {@link Board#init(int, int, int)}.</p>
     */
//    public static final Board INSTANCE = new Board();

//    private static Board getInstance() {
//        return INSTANCE;
//    }


    public enum PLAYERS {
        PLAYER, AI
    }


    private static final int PLAYER_DISK      = R.drawable.red;
    private static final int AI_DISK          = R.drawable.white;
    private static final int IS_FREE          = 0x00;
    private static final int MIN_ROWS         = 4;
    private static final int MAX_ROWS         = 10;
    private static final int MIN_COLUMNS      = 4;
    private static final int MAX_COLUMNS      = 10;
    private static final int MIN_DISKS_TO_WIN = 3;

    /**
     * To be used in checks for if we have a winner for the current game.
     * <p>It will either: <br> &#09; be {@code null} signaling we don't have a winner &nbsp; or,
     * <br>&#09; contain the name of the winner from {@link site.petrumugurel.connect4.Board
     * PLAYERS} as a {@code String}. </p>
     */
    protected String  mHaveWinner  = null;
    protected boolean mIsDraw      = false;
    private   int     mMovesNumber = 0;

    // Following are to be inputted by the user when creating the Board.
    private int mNumberOfRows;
    private int mNumberOfColumns;

    private int mDisksNeededForWin;

    /**
     * Keep track of the position of disks on the board.
     * <br>In the &nbsp;[row][column]&nbsp; array it will store whether that position
     * {@link #IS_FREE} / {@link #PLAYER_DISK} / {@link #AI_DISK}.
     */
    private int[][] mDisksOnBoard;  // keep the position of disks on the board

    /**
     * Must be called immediatly after constructing the singleton to initialize the board.
     * Will allow the board to know the exact configuration of rows and columns and allow it to keep
     * track of the state for each board position.
     * Must match the drawn layout.
     *
     * @param rows how many rows the board will have?
     *             {@link Board#MIN_ROWS} = {@value Board#MIN_ROWS}
     *             {@link Board#MAX_ROWS} = {@value Board#MAX_ROWS}
     * @param columns how many columns the board will have?
     *                {@link Board#MIN_COLUMNS} = {@value Board#MIN_COLUMNS}
     *                {@link Board#MAX_COLUMNS} = {@value Board#MAX_COLUMNS}
     * @param disksNeededForWin how many disks in a straght line needed to win?
     *                          {@link Board#MIN_DISKS_TO_WIN} = {@value Board#MIN_DISKS_TO_WIN}
     *
     * @throws IllegalArgumentException if asked fewer/more rows/columns than needed or if the
     * user sets an invalid (too low/big) number for the winning line of disks.
     */
    public void init(int rows, int columns, int disksNeededForWin) throws IllegalArgumentException {
        if (!(rows >= MIN_ROWS && columns >= MIN_COLUMNS)) {
            throw new IllegalArgumentException("Minimum " + MIN_ROWS + " rows and "
                                               + MIN_COLUMNS + " columns");
        }
        if (!(rows <= MAX_ROWS && columns <= MAX_COLUMNS)) {
            throw new IllegalArgumentException("Maximum " + MAX_ROWS + " rows and "
                                               + MAX_COLUMNS + " columns.");
        }
        if (!(disksNeededForWin >= MIN_DISKS_TO_WIN)) {
            throw new IllegalArgumentException("A line of at least 3 disks is normally needed");
        }
        if (disksNeededForWin >= rows || disksNeededForWin >= columns) {
            throw new IllegalArgumentException("More disks needed to win than spaces on the board");
        }

        mNumberOfRows = rows;
        mNumberOfColumns = columns;
        mDisksNeededForWin = disksNeededForWin;
        mMovesNumber = 0;

        clearBoard();
    }

    public Board(int rows, int columns, int disksNeededForWin)
            throws IllegalArgumentException {

        if (!(rows >= MIN_ROWS && columns >= MIN_COLUMNS)) {
            throw new IllegalArgumentException("Minimum " + MIN_ROWS + " rows and "
                                               + MIN_COLUMNS + " columns");
        }
        if (!(rows <= MAX_ROWS && columns <= MAX_COLUMNS)) {
            throw new IllegalArgumentException("Maximum " + MAX_ROWS + " rows and "
                                               + MAX_COLUMNS + " columns.");
        }
        if (!(disksNeededForWin >= MIN_DISKS_TO_WIN)) {
            throw new IllegalArgumentException("A line of at least 3 disks is normally needed");
        }
        if (disksNeededForWin >= rows || disksNeededForWin >= columns) {
            throw new IllegalArgumentException("More disks needed to win than spaces on the board");
        }

        mNumberOfRows = rows;
        mNumberOfColumns = columns;
        mDisksNeededForWin = disksNeededForWin;

        clearBoard();
    }

    protected void clearBoard() {
        mDisksOnBoard = new int[mNumberOfRows][mNumberOfColumns];
        for (int x = mNumberOfColumns - 1; x >= 0; x--) {
            for (int y = mNumberOfRows - 1; y >= 0; y--) {
                mDisksOnBoard[y][x] = IS_FREE;
            }
        }

        mMovesNumber = 0;
        mIsDraw = false;
        mHaveWinner = null;
    }


    /**
     * <p>To be called after every move to check for winners or a draw.</p>
     * <p>Will traverse every position on the board to check if there are
     * {@link Board#mDisksNeededForWin} of the same colour in any horizontal/vertical
     * or diagonal line.</p>
     */
    private void checkForWinner() {
        // no point in checking if there are not enough moves made
        if (mMovesNumber >= (mDisksNeededForWin * 2) - 1) {
            int x;  // counter for the columns
            int y;  // counter for the rows


            // Check each row for winners starting from bottom left corner of the board, going up.
            for (y = mNumberOfRows - 1; y >= 0; y--) {
                // Check every column from left to right while there are at least mDisksNeededForWin
                // number of columns. If less, can't have the needed number of disks to win.
                for (x = 0; x <= mNumberOfColumns - mDisksNeededForWin; x++) {
                    int lineOfDisks = 0;
                    if (mDisksOnBoard[y][x] != IS_FREE) {
                        lineOfDisks++;
                        // Now search if next disks are of the same, stopping when we have a winner
                        // or if, before that, we have a different disk than needed.
                        while (mDisksOnBoard[y][x] == mDisksOnBoard[y][x + 1]) {
                            x++;    // x+1 column is of the same, make it the current column.
                            if (++lineOfDisks == mDisksNeededForWin) {
                                mHaveWinner = mDisksOnBoard[y][x]
                                              == PLAYER_DISK ? PLAYERS.PLAYER.toString()
                                                             : PLAYERS.AI.toString();
                                return;
                            }
                        }
                    }
                }
            }


            // Check each column for winners starting from bottom left corner of the board
            // going to the right
            for (x = 0; x < mNumberOfColumns; x++) {
                // Check for a first disk on every row on current column from the bottom to the
                // mDisksNeededForWin - 1 th row so that we can have a continuous line of the same
                // colour disks, needed to win.
                for (y = mNumberOfRows - 1; y >= mDisksNeededForWin - 1; y--) {
                    int lineOfDisks = 0;
                    if (mDisksOnBoard[y][x] != IS_FREE) {
                        lineOfDisks++;
                        // Now search if next disks are of the same, stopping when we have a winner
                        // or if, before that, we have a different disk than needed.
                        while (mDisksOnBoard[y][x] == mDisksOnBoard[y - 1][x]) {
                            y--;
                            if (++lineOfDisks == mDisksNeededForWin) {
                                mHaveWinner = mDisksOnBoard[y][x]
                                              == PLAYER_DISK ? PLAYERS.PLAYER.toString()
                                                             : PLAYERS.AI.toString();
                                return;
                            }
                        }
                    }
                }
            }


            // Check upwards diagonals for winners starting from the bottom left corner.
            int startingRow = mNumberOfRows - 1;
            int startingCol = 0;
            boolean finishedWRows = false;
            while (true) {
                // until there's room for a winning streak
                for (y = startingRow, x = startingCol;
                     y >= mDisksNeededForWin - 1 && x <= mNumberOfColumns - mDisksNeededForWin;
                     y--, x++) {

                    int lineOfDisks = 0;
                    if (mDisksOnBoard[y][x] != IS_FREE) {
                        lineOfDisks++;
                        while (mDisksOnBoard[y][x] == mDisksOnBoard[y - 1][x + 1]) {
                            y--; x++;
                            if (++lineOfDisks == mDisksNeededForWin) {
                                mHaveWinner = mDisksOnBoard[y][x]
                                              == PLAYER_DISK ? PLAYERS.PLAYER.toString()
                                                             : PLAYERS.AI.toString();
                                return;
                            }
                        }
                    }
                }
                // First increment the starting row until we can still have a winning streak.
                if (startingRow >= mDisksNeededForWin - 1 && !finishedWRows) {
                    startingRow--;      // move to next upper diagonal based on starting row
                }
                // Then increment the starting column until we can still have a winning streak.
                else if (startingCol <= mNumberOfColumns - mDisksNeededForWin) {
                    startingRow = mNumberOfRows - 1;
                    finishedWRows = true;
                    startingCol++;
                }
                // Lastly, if all valid upwards diagonals were checked, our job is done, so break.
                else {
                    break;
                }
            }


            // Check downwards diagonals for winners starting from upper left corner.
            startingRow = 0;
            startingCol = 0;
            finishedWRows = false;
            while (true) {
                // until there's room for a winning streak
                for (y = startingRow, x = startingCol;
                     y <= mNumberOfRows - mDisksNeededForWin &&
                     x <= mNumberOfColumns - mDisksNeededForWin;
                     y++, x++) {

                    int lineOfDisks = 0;
                    if (mDisksOnBoard[y][x] != IS_FREE) {
                        lineOfDisks++;
                        while (mDisksOnBoard[y][x] == mDisksOnBoard[y + 1][x + 1]) {
                            y++; x++;
                            if (++lineOfDisks == mDisksNeededForWin) {
                                mHaveWinner = mDisksOnBoard[y][x]
                                              == PLAYER_DISK ? PLAYERS.PLAYER.toString()
                                                             : PLAYERS.AI.toString();
                                return;
                            }
                        }
                    }
                }
                // First increment the starting row until we can still have a winning streak.
                if (startingRow <= mNumberOfRows - mDisksNeededForWin && !finishedWRows) {
                    startingRow++;      // move to next upper diagonal based on starting row
                }
                // Then increment the starting column until we can still have a winning streak.
                else if (startingCol <= mNumberOfColumns - mDisksNeededForWin) {
                    startingRow = 0;
                    finishedWRows = true;
                    startingCol++;
                }
                // Lastly, if all valid upwards diagonals were checked, our job is done, so break.
                else {
                    break;
                }
            }


            // If we don't have a winner until now (case in which we should've returned)
            // check if all possible moves were made, case in which we have a draw.
            if ((mMovesNumber == mNumberOfRows * mNumberOfColumns) /*&& mHaveWinner == null*/) {
                mIsDraw = true;
                return;
            }
        }
    }


    /**
     * Try to store a new disk on behalf of player at the indicated column.
     * @param columnToInsertInto column into which to store a new disk, into the lowest free row.
     * @return index of the row at which the disk was inserted<br>
     *         {@code null} if the indicated {@code columnToInsertInto} is already full.
     */
    protected Integer makePlayerMove(int columnToInsertInto) {
        return storeNewDisk(PLAYERS.PLAYER, columnToInsertInto);
    }


    /**
     * A simple method which will insert a new disk on behalf of the AI into a random column.
     * while there is not a winner or a draw.
     * @return position where the AI's disk was inserted {row, column}.
     */
    protected Integer[] makeAIMove() {
        Integer AIDiskRow = null;
        int columnToInsertInto = 0;
        if (mHaveWinner == null && !mIsDraw) {
            while (AIDiskRow == null) {
                Random r = new Random();
                columnToInsertInto = r.nextInt(mNumberOfColumns);
                AIDiskRow = storeNewDisk(PLAYERS.AI, columnToInsertInto);

                if (AIDiskRow == null) {
                    Log.d("annd AIDiskRow is", "null");
                }
            }
            return new Integer[] {AIDiskRow, columnToInsertInto};
        }
        return null;
    }


    /**
     * This method will actually insert a new disk in the indicated column if there's available
     * space.
     * @param player on behalf of whom the move is made, will designate the color of the stored disk
     * @param columnToInsertInto board index of the column where to try to insert the disk
     * @return  board index of the row where the disk was stored
     *          <br>{@code null} if on the indicated column there are no free spaces available
     */
    protected Integer storeNewDisk(PLAYERS player, int columnToInsertInto) {
        for (int y = mNumberOfRows - 1; y >= 0 ; y--) {
            if (mDisksOnBoard[y][columnToInsertInto] == IS_FREE) {
                mDisksOnBoard[y][columnToInsertInto]
                        = (player == PLAYERS.PLAYER ? PLAYER_DISK : AI_DISK);

                mMovesNumber++;
                checkForWinner();
                return y;
            }
        }

        return null;   // the column is filled with disks
    }

}
