package site.petrumugurel.connect4;

import android.view.View;

import java.util.Random;


// TODO: 18-Feb-16 MAJOR REWORK
// To let the disk fall into the last free space we I'll use an array similar to mDisksOnBopard
// array - lets call it mPositionsOnBoard which will hold a reference to each imageView on the
// board.
// Already find the lowest free space in a column, just need to call that imageView to animate
// etc, and that will be the last of it.







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
    public static final Board INSTANCE = new Board();

    private static Board getInstance() {
        return INSTANCE;
    }

    public enum Players {PLAYER, AI}
    
    /**
     * The ImageView representing the playing disk.
     * <br>Allows to set various properties, useful for animation.
     */
    public static View disk;
    
    public static final  int     PLAYER_DISK      = 0x01;  // the color of the player's disks
    public static final  int     AI_DISK          = 0x10;  // the color of the AI's disks
    private static final int     IS_FREE          = 0x00;
    private static final int     MIN_ROWS         = 4;
    private static final int     MAX_ROWS         = 10;
    private static final int     MIN_COLUMNS      = 4;
    private static final int     MAX_COLUMNS      = 10;
    private static final int     MIN_DISKS_TO_WIN = 3;
    private static       boolean mHaveWinner      = false;
    private              boolean mIsDraw          = false;

    // Following are to be inputted by the user
    private int mNumberOfRows;
    private int mNumberOfColumns;
    private int mDisksNeededForWin;
    private int mMovesNumber;   // Todo Don't forget to increment this for every move

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
        if (!(disksNeededForWin >= rows) || !(disksNeededForWin >= columns)) {
            throw new IllegalArgumentException("More disks needed to win than spaces on the board");
        }

        mNumberOfRows = rows;
        mNumberOfColumns = columns;
        mDisksNeededForWin = disksNeededForWin;
        mMovesNumber = 0;

        mDisksOnBoard = new int[mNumberOfRows][mNumberOfColumns];
        for (int x = mNumberOfColumns; x >= 0; x--) {
            for (int y = mNumberOfRows; y >= 0; y--) {
                mDisksOnBoard[y][x] = IS_FREE;
            }
        }
    }

    /**
     * <p>If all possible moves were made - all positions are full of disks // todo draw</p>
     * <p>Else will traverse every position to check if there are {@link Board#mDisksNeededForWin}
     * of the same colour in a horizontal, vertical or diagonal(any) line.</p>
     */
    // // TODO: 18-Feb-16 Needs refactoring badly
    public void checkForWinner() {
        if (mMovesNumber == mNumberOfRows * mNumberOfColumns) {
            mIsDraw = true;
            return;
        }

        // no point in checking if there are not enough moves made
        else if (mMovesNumber >= (mDisksNeededForWin * 2) - 1) {
            boolean playerWon = true;
            boolean AIWon = true;
            int noOfRedDisks;
            int noOfWhiteDisks;
            int x;  // counter for the columns
            int y;  // counter for the rows


            // Check each row for winners
            for (y = 0; y < mNumberOfRows; y++) {
                noOfRedDisks = noOfWhiteDisks = 0;
                for (x = 0; x < mNumberOfColumns - mDisksNeededForWin; x++) {
                    if (mDisksOnBoard[y][x] == PLAYER_DISK) {
                        noOfRedDisks++;
                        // check for a continuous mDisksNeededToWin line of same colour disks
                        for (int disksNeeded = x + mDisksNeededForWin; x != disksNeeded; x++) {
                            if (mDisksOnBoard[y][x] == PLAYER_DISK) {
                                noOfRedDisks++;
                            }
                            else {
                                noOfRedDisks = 0;
                                break;
                            }
                        }
                        if (noOfRedDisks == mDisksNeededForWin) {
                            if (noOfRedDisks == mDisksNeededForWin) {
                                mHaveWinner = true;
                                return;
                            }
                        }
                    }
                    else if (mDisksOnBoard[y][x] == AI_DISK) {
                        noOfWhiteDisks++;
                        // check for a continuous mDisksNeededToWin line of same colour disks
                        for (int disksNeeded = x + mDisksNeededForWin; x != disksNeeded; x++) {
                            if (mDisksOnBoard[y][x] == AI_DISK) {
                                noOfWhiteDisks++;
                            }
                            else {
                                noOfWhiteDisks = 0;
                                break;
                            }
                        }
                        if (noOfWhiteDisks == mDisksNeededForWin) {
                            mHaveWinner = true;
                            return;
                        }
                    }   // check for AI disks
                }   // columns iteration
            }   // rows iteration


            // Check each column for winners
            for (x = 0; x < mNumberOfColumns; x++) {
                noOfRedDisks = noOfWhiteDisks = 0;
                for (y = 0; y < mNumberOfRows - mDisksNeededForWin; y++) {
                    if (mDisksOnBoard[y][x] == PLAYER_DISK) {
                        noOfRedDisks++;
                        // check for a continuous mDisksNeededToWin line of same colour disks
                        for (int disksNeeded = y + mDisksNeededForWin; y != disksNeeded; y++) {
                            if (mDisksOnBoard[y][x] == PLAYER_DISK) {
                                noOfRedDisks++;
                            }
                            else {
                                noOfRedDisks = 0;
                                break;
                            }
                        }
                        if (noOfRedDisks == mDisksNeededForWin) {
                            if (noOfRedDisks == mDisksNeededForWin) {
                                mHaveWinner = true;
                                return;
                            }
                        }
                    }
                    else if (mDisksOnBoard[y][x] == AI_DISK) {
                        noOfWhiteDisks++;
                        // check for a continuous mDisksNeededToWin line of same colour disks
                        for (int disksNeeded = y + mDisksNeededForWin; y != disksNeeded; y++) {
                            if (mDisksOnBoard[y][x] == AI_DISK) {
                                noOfWhiteDisks++;
                            }
                            else {
                                noOfWhiteDisks = 0;
                                break;
                            }
                        }
                        if (noOfWhiteDisks == mDisksNeededForWin) {
                            mHaveWinner = true;
                            return;
                        }
                    }   // check for AI disks
                }   // rows iteration
            }   // columns iteration


            // Check upwards diagonals for winners
            for (y = 0, x = 0; y < mNumberOfRows - mDisksNeededForWin
                               && x < mNumberOfColumns - mDisksNeededForWin; y++, x++) {
                noOfRedDisks = noOfWhiteDisks = 0;
                if (mDisksOnBoard[y][x] == PLAYER_DISK) {
                    noOfRedDisks++;
                    for (int disksNeeded = y + mDisksNeededForWin; y != disksNeeded; y++, x++) {
                        if (mDisksOnBoard[y][x] == PLAYER_DISK) {
                            noOfRedDisks++;
                        }
                        else {
                            noOfRedDisks = 0;
                            break;  // from the inner for loop
                        }
                    }
                    if (noOfRedDisks == mDisksNeededForWin) {
                        mHaveWinner = true;
                        return;
                    }
                }
                else if (mDisksOnBoard[y][x] == AI_DISK) {
                    noOfWhiteDisks++;
                    // check for a continuous mDisksNeededToWin line of same colour disks
                    for (int disksNeeded = y + mDisksNeededForWin; y != disksNeeded; y++, x++) {
                        if (mDisksOnBoard[y][x] == AI_DISK) {
                            noOfWhiteDisks++;
                        }
                        else {
                            noOfWhiteDisks = 0;
                            break;
                        }
                    }
                    if (noOfWhiteDisks == mDisksNeededForWin) {
                        mHaveWinner = true;
                        return;
                    }
                }   //  check for AI disks
            }   //  big loop that checks the diagonals
        } //  if (mMovesNumber >= 7)
    }   // method end

    /**
     * Try to store a new disk on behalf of player at the indicated column.
     * @param columnToInsertInto column into which to store a new disk, into the lowest free row.
     * @return {@code true} if a new disk was inserted at the indicated column.<br>
     *         {@code false} if there are no free spaces in the column. Must select another.
     */
    public boolean makePlayerMove(int columnToInsertInto) {
        return storeNewDisk(Players.PLAYER, columnToInsertInto);
    }

    /**
     * A simple method which will insert a new disk on behalf of the AI into a random column.
     * @return
     */
    private boolean makeAIMove() {
        if (!mHaveWinner) {
            Random r = new Random();
            int columnToInsertInto = r.nextInt(mNumberOfColumns);
            storeNewDisk(Players.AI, columnToInsertInto);
        }

        return true;
    }

    /**
     * This method will actually insert a new disk in the indicated column if there's available
     * space.
     * <p>If the move is made on behalf of the AI and the indicated column is already full it
     * will store a new disk into the next available column, starting from 0 if last is full.</p>
     * @param player on behalf of whom this move is made.
     * @param columnToInsertInto where to insert a new disk.
     * @return {@code true} - if a new disk was inserted at the indicated column.<br>
     *         {@code false} - if there are no free spaces in the column. Must select another.
     */
    private boolean storeNewDisk(Players player, int columnToInsertInto) {
        if (mMovesNumber == mNumberOfRows * mNumberOfColumns) {
            return false;
        }

        for (int y = 0; y < mNumberOfRows; y++) {
            if (mDisksOnBoard[y][columnToInsertInto] == IS_FREE) {
                mDisksOnBoard[y][columnToInsertInto]
                        = (player == Players.PLAYER ? PLAYER_DISK : AI_DISK);

                mMovesNumber++;
                disk.animate().start();     // // TODO: 18-Feb-16 Needs duplicated, and checked
                return true;
            }
        }

        // If we're here, means the intended column was full with disks.
        // But the AI must make a move, so try the next column.
        if (mMovesNumber < 1 && player == Players.AI) {
            // careful to not get a seg fault, must return to 0 after last column
            if (columnToInsertInto == mNumberOfColumns - 1) {
                storeNewDisk(Players.AI, 0);
            }
            else {
                storeNewDisk(Players.AI, ++columnToInsertInto);
            }
        }

        return false;   // the column is filled with disks
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
