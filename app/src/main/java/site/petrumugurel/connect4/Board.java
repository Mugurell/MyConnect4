package site.petrumugurel.connect4;

import android.util.Log;
import android.view.View;

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


    /**
     * The ImageView representing the playing disk.
     * <br>Allows to set various properties, useful for animation.
     */
    public static View disk;

    private static final int     PLAYER_DISK      = R.drawable.red;
    private static final int     AI_DISK          = R.drawable.white;
    private static final int     IS_FREE          = 0x00;
    private static final int     MIN_ROWS         = 4;
    private static final int     MAX_ROWS         = 10;
    private static final int     MIN_COLUMNS      = 4;
    private static final int     MAX_COLUMNS      = 10;
    private static final int     MIN_DISKS_TO_WIN = 3;
    protected            boolean mHaveWinner      = false;
    protected            boolean mIsDraw          = false;
    protected            boolean mPlayerWon       = false;
    protected            boolean mAIWon           = false;


    // Following are to be inputted by the user
    private int mNumberOfRows;
    private int mNumberOfColumns;
    private int mDisksNeededForWin;
    private int mMovesNumber;   // Todo Don't forget to increment this for every move

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
        mIsDraw = mHaveWinner = false;
    }

    /**
     * Try to store a new disk on behalf of player at the indicated column.
     * @param columnToInsertInto column into which to store a new disk, into the lowest free row.
     * @return {@code true} if a new disk was inserted at the indicated column.<br>
     *         {@code false} if there are no free spaces in the column. Must select another.
     */
    protected Integer makePlayerMove(int columnToInsertInto) {
        return storeNewDisk(PLAYERS.PLAYER, columnToInsertInto);
    }

    /**
     * <p>To be called after every move to check for winners or a draw.</p>
     * <p>Will traverse every position on the board to check if there are
     * {@link Board#mDisksNeededForWin} of the same colour in any horizontal/vertical
     * or diagonal line.</p>
     */
    // // TODO: 18-Feb-16 Needs refactoring badly
    public void checkForWinner() {
        // no point in checking if there are not enough moves made
        if (mMovesNumber >= (mDisksNeededForWin * 2) - 1) {
            mPlayerWon = false;
            mAIWon = false;
            int noOfPlayersDisks;
            int noOfAIsDisks;
            int x;  // counter for the columns
            int y;  // counter for the rows


            // Check each row for winners
            for (y = mNumberOfRows - 1; y >= 0; y--) {
                noOfPlayersDisks = noOfAIsDisks = 0;
                // if not found a viable disk until "- mDisksNeededForWin" there's no point in tryin
                for (x = 0; x <= mNumberOfColumns - mDisksNeededForWin; x++) {
                    if (mDisksOnBoard[y][x] == PLAYER_DISK) {
                        noOfPlayersDisks++;
                        x += 1;
                        // check for a continuous mDisksNeededToWin line of same colour disks
                        for (int disksNeeded = x + mDisksNeededForWin; x != disksNeeded; x++) {
                            if (mDisksOnBoard[y][x] == PLAYER_DISK) {
                                noOfPlayersDisks++;
                                if (noOfPlayersDisks >= mDisksNeededForWin) {
                                    mHaveWinner = mPlayerWon =true;
                                    return;
                                }
                            }
                            else {
                                noOfPlayersDisks = 0;
                                break;
                            }
                        }
                    }

                    else if (x <= mNumberOfColumns - mDisksNeededForWin &&
                             mDisksOnBoard[y][x] == AI_DISK) {
                        noOfAIsDisks++;
                        x += 1;
                        // check for a continuous mDisksNeededToWin line of same colour disks
                        for (int disksNeeded = x + mDisksNeededForWin; x != disksNeeded; x++) {
                            if (mDisksOnBoard[y][x] == AI_DISK) {
                                noOfAIsDisks++;
                                if (noOfAIsDisks == mDisksNeededForWin) {
                                    mHaveWinner = mAIWon = true;
                                    return;
                                }
                            }
                            else {
                                noOfAIsDisks = 0;
                                break;
                            }
                        }
                    }   // check for AI disks
                }   // columns iteration
            }   // rows iteration


            // Check each column for winners
            for (x = 0; x < mNumberOfColumns; x++) {
                noOfPlayersDisks = noOfAIsDisks = 0;
                // if not found a viable disk until "mDisksNeededForWin - 1"
                // there's no point in trying
                for (y = mNumberOfRows - 1; y >= mDisksNeededForWin - 1; y--) {
                    if (mDisksOnBoard[y][x] == PLAYER_DISK) {
                        noOfPlayersDisks++;
                        y -= 1;
                        // check for a continuous mDisksNeededToWin line of same colour disks
                        for (int disksNeeded = y - mDisksNeededForWin; y != disksNeeded; y--) {
                            if (mDisksOnBoard[y][x] == PLAYER_DISK) {
                                noOfPlayersDisks++;
                                if (noOfPlayersDisks == mDisksNeededForWin) {
                                    mHaveWinner = mPlayerWon = true;
                                    return;
                                }
                            }
                            else {
                                noOfPlayersDisks = 0;
                                break;
                            }
                        }
                    }
                    else if (y >= mDisksNeededForWin - 1 && mDisksOnBoard[y][x] == AI_DISK) {
                        noOfAIsDisks++;
                        y -= 1;
                        // check for a continuous mDisksNeededToWin line of same colour disks
                        for (int disksNeeded = y - mDisksNeededForWin; y != disksNeeded; y--) {
                            if (mDisksOnBoard[y][x] == AI_DISK) {
                                noOfAIsDisks++;
                                if (noOfAIsDisks == mDisksNeededForWin) {
                                    mHaveWinner = mAIWon = true;
                                    return;
                                }
                            }
                            else {
                                noOfAIsDisks = 0;
                                break;
                            }
                        }
                    }   // check for AI disks
                }   // rows iteration
            }   // columns iteration


            // Check upwards diagonals for winners
            int startingRow = mNumberOfRows - 1;
            int startingCol = 0;
            // First by incrementing the starting row number
            while (startingRow >= mDisksNeededForWin - 1) {
                for (y = startingRow, x = startingCol;
                     y >= mDisksNeededForWin - 1 &&
                     x <= mNumberOfColumns - mDisksNeededForWin;
                     y--, x++) {
                    noOfPlayersDisks = noOfAIsDisks = 0;
                    if (mDisksOnBoard[y][x] == PLAYER_DISK) {
                        noOfPlayersDisks++;

                        while (--y >= 0 && ++x <= mNumberOfColumns - 1) {
                            if (mDisksOnBoard[y][x] == PLAYER_DISK) {
                                noOfPlayersDisks++;

                                if (noOfPlayersDisks >= mDisksNeededForWin) {
                                    mHaveWinner = mPlayerWon = true;
                                    return;
                                }
                            }
                            else {
                                break;
                            }
                        }
                    }
                }

                // Starting again from bottom left corner
                for (y = startingRow, x = startingCol;
                     y >= mDisksNeededForWin - 1 &&
                     x <= mNumberOfColumns - mDisksNeededForWin;
                     y--, x++) {
                    noOfPlayersDisks = noOfAIsDisks = 0;
                    if (mDisksOnBoard[y][x] == AI_DISK) {
                        noOfAIsDisks++;

                        while (--y >= 0 && ++x <= mNumberOfColumns - 1) {
                            if (mDisksOnBoard[y][x] == AI_DISK) {
                                noOfAIsDisks++;

                                if (noOfAIsDisks >= mDisksNeededForWin) {
                                    mHaveWinner = mAIWon = true;
                                    return;
                                }
                            }
                            else {
                                break;
                            }
                        }
                    }
                }

                startingRow--;      // move to next upper diagonal based on starting row
            }



            // Second by incrementing the starting column number
            startingRow = mNumberOfRows - 1;
            startingCol = 1;
            while (startingCol <= mNumberOfColumns - mDisksNeededForWin) {
                for (y = startingRow, x = startingCol;
                     y >= mDisksNeededForWin - 1 &&
                     x <= mNumberOfColumns - mDisksNeededForWin;
                     y--, x++) {

                    noOfPlayersDisks = noOfAIsDisks = 0;
                    if (mDisksOnBoard[y][x] == PLAYER_DISK) {
                        noOfPlayersDisks++;

                        while (--y >= 0 && ++x <= mNumberOfColumns - 1) {
                            if (mDisksOnBoard[y][x] == PLAYER_DISK) {
                                noOfPlayersDisks++;

                                if (noOfPlayersDisks >= mDisksNeededForWin) {
                                    mHaveWinner = mPlayerWon = true;
                                    return;
                                }
                            }
                            else {
                                break;
                            }
                        }
                    }
                }

                for (y = startingRow, x = startingCol;
                     y >= mDisksNeededForWin - 1 &&
                     x <= mNumberOfColumns - mDisksNeededForWin;
                     y--, x++) {

                    noOfPlayersDisks = noOfAIsDisks = 0;
                    if (mDisksOnBoard[y][x] == AI_DISK) {
                        noOfAIsDisks++;

                        while (--y >= 0 && ++x <= mNumberOfColumns - 1) {
                            if (mDisksOnBoard[y][x] == AI_DISK) {
                                noOfAIsDisks++;

                                if (noOfAIsDisks >= mDisksNeededForWin) {
                                    mHaveWinner = mAIWon = true;
                                    return;
                                }
                            }
                            else {
                                break;
                            }
                        }
                    }
                }

                startingCol++;      // move to the next upper diagonal based on starting column
            }



            // Check downwards diagonals for winners
            startingRow = mNumberOfRows - mDisksNeededForWin;
            startingCol = 0;
            // First by incrementing the starting row number
            while (startingRow >= 0) {
                for (y = startingRow, x = startingCol;
                     y <= mNumberOfRows - 1 && x <= mNumberOfColumns - 1;
                     y++, x++) {

                    noOfPlayersDisks = noOfAIsDisks = 0;
                    if (mDisksOnBoard[y][x] == PLAYER_DISK) {
                        noOfPlayersDisks++;
                        while (++y <= mNumberOfRows - 1 && ++x <= mNumberOfColumns - 1) {
                            if (mDisksOnBoard[y][x] == PLAYER_DISK) {
                                noOfPlayersDisks++;

                                if (noOfPlayersDisks >= mDisksNeededForWin) {
                                    mHaveWinner = mPlayerWon = true;
                                    return;
                                }
                            }
                            else {
                                break;  // because don't have an uninterrupted line of disks
                            }
                        }
                    }
                }

                for (y = startingRow, x = startingCol;
                     y <= mNumberOfRows - 1 && x <= mNumberOfColumns - 1;
                     y++, x++) {

                    noOfPlayersDisks = noOfAIsDisks = 0;
                    if (mDisksOnBoard[y][x] == AI_DISK) {
                        noOfAIsDisks++;
                        while (++y <= mNumberOfRows - 1 && ++x <= mNumberOfColumns - 1) {
                            if (mDisksOnBoard[y][x] == AI_DISK) {
                                noOfAIsDisks++;

                                if (noOfAIsDisks >= mDisksNeededForWin) {
                                    mHaveWinner = mAIWon = true;
                                    return;
                                }
                            }
                            else {
                                break;
                            }
                        }
                    }
                }

                startingRow--;      // move to next upper diagonal based on starting row
            }



            // Second by incrementing the starting column number
            startingRow = 0;
            startingCol = 1;    // the downwards diagonal from 0,0 was already checked
            while (startingCol <= mNumberOfColumns - mDisksNeededForWin) {
                for (y = startingRow, x = startingCol;
                     y <= mNumberOfRows - mDisksNeededForWin &&
                     x <= mNumberOfColumns - mDisksNeededForWin;
                     y++, x++) {

                    noOfPlayersDisks = noOfAIsDisks = 0;
                    if (mDisksOnBoard[y][x] == PLAYER_DISK) {
                        noOfPlayersDisks++;
                        while (++y <= mNumberOfRows - 1 && ++x <= mNumberOfColumns - 1) {
                            if (mDisksOnBoard[y][x] == PLAYER_DISK) {
                                noOfPlayersDisks++;

                                if (noOfPlayersDisks >= mDisksNeededForWin) {
                                    mHaveWinner = mPlayerWon = true;
                                    return;
                                }
                            }
                            else {
                                break;
                            }
                        }
                    }
                }

                for (y = startingRow, x = startingCol;
                     y <= mNumberOfRows - mDisksNeededForWin &&
                     x <= mNumberOfColumns - mDisksNeededForWin;
                     y++, x++) {

                    noOfPlayersDisks = noOfAIsDisks = 0;
                    if (mDisksOnBoard[y][x] == AI_DISK) {
                        noOfAIsDisks++;
                        while (++y <= mNumberOfRows - 1 && ++x <= mNumberOfColumns - 1) {
                            if (mDisksOnBoard[y][x] == AI_DISK) {
                                noOfAIsDisks++;

                                if (noOfAIsDisks >= mDisksNeededForWin) {
                                    mHaveWinner = mAIWon = true;
                                    return;
                                }
                            }
                            else {
                                break;
                            }
                        }
                    }
                }

                startingCol++;      // move to the next upper diagonal based on starting column
            }


            if ((mMovesNumber == mNumberOfRows * mNumberOfColumns) && !mHaveWinner) {
                mIsDraw = true;
                return;
            }

        }

    }




    /**
     * A simple method which will insert a new disk on behalf of the AI into a random column.
     * @return position where the AI's disk was inserted {row, column}.
     */
    protected Integer[] makeAIMove() {
        Integer AIDiskRow = null;
        int columnToInsertInto = 0;
        while (!mHaveWinner && AIDiskRow == null) {
            Random r = new Random();
            columnToInsertInto = r.nextInt(mNumberOfColumns);
            AIDiskRow = storeNewDisk(PLAYERS.AI, columnToInsertInto);

            if (AIDiskRow == null) {
                Log.d("annd AIDiskRow is", "null");
            }
        }
        return new Integer[] {AIDiskRow, columnToInsertInto};
    }


    /**
     * This method will actually insert a new disk in the indicated column if there's available
     * space.
     * <p>If the move is made on behalf of the AI and the indicated column is already full it
     * will store a new disk into the next available column, starting from 0 if last is full.</p>
     * @param player on behalf of whom this move is made.
     * @param columnToInsertInto where to insert a new disk.
     * @return row number where the disk was stored on the board.<br>
     */

    /**
     * This method will actually insert a new disk in the indicated column if there's available
     * space.
     * @param player on behalf of whom the move is made, will designate the color of the stored disk
     * @param columnToInsertInto board index of the column where to try to insert the disk
     * @return  board index of the row where the disk was stored
     *          <br>{@code null} if on the indicated column there are no free spaces available
     */
    protected Integer storeNewDisk(PLAYERS player, int columnToInsertInto) {
//        if (mMovesNumber == mNumberOfRows * mNumberOfColumns) {
//            return false;
//        }

        for (int y = mNumberOfRows - 1; y >= 0 ; y--) {
            if (mDisksOnBoard[y][columnToInsertInto] == IS_FREE) {
                mDisksOnBoard[y][columnToInsertInto]
                        = (player == PLAYERS.PLAYER ? PLAYER_DISK : AI_DISK);

                mMovesNumber++;
                return y;
            }
        }

        return null;   // the column is filled with disks
    }


    public boolean haveWinner() {
        if (mHaveWinner == true) {
            // TODO: 18-Feb-16 What should we do if we have a winner
        }

        return true;
    }

    public boolean haveDraw() {
        if (mIsDraw == true) {
            // TODO: 18-Feb-16 What should we do in case of a draw
        }

        return true;
    }

}
