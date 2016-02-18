package site.petrumugurel.connect4;

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

    public static final  int PLAYER_DISK      = 0x01;  // the color of the player's disks
    public static final  int AI_DISK          = 0x10;  // the color of the AI's disks
    private static final int IS_FREE          = 0x00;
    private static final int MIN_ROWS         = 4;
    private static final int MAX_ROWS         = 10;
    private static final int MIN_COLUMNS      = 4;
    private static final int MAX_COLUMNS      = 10;
    private static final int MIN_DISKS_TO_WIN = 3;

    // Following are to be inputted by the user
    private int mNumberOfRows;
    private int mNumberOfColumns;
    private int mDisksNeededForWin;
    private int mMovesNumber;   // Todo Dont forget to increment this for every move

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

        mNumberOfRows = rows - 1;   // -1 to account for the array starting at 0
        mNumberOfColumns = columns - 1;
        mDisksNeededForWin = disksNeededForWin;
        mMovesNumber = 0;

        mDisksOnBoard = new int[mNumberOfRows][mNumberOfColumns];
        for (int x = mNumberOfColumns; x >= 0; x--) {
            for (int y = mNumberOfRows; y >= 0; y--) {
                mDisksOnBoard[y][x] = IS_FREE;
            }
        }
    }

    public void checkForWinner() {
        if (mMovesNumber >= 7) {
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
                                // TODO: 17-Feb-16 do something when player won
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
                            // TODO: 17-Feb-16 do something when player won
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
                                // TODO: 17-Feb-16 do something when player won
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
                            // TODO: 17-Feb-16 do something when player won
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
                        // TODO: 17-Feb-16 do something when player won
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
                        // TODO: 17-Feb-16 do something when player won
                    }
                }   //  check for AI disks
            }   //  big loop that checks the diagonals
        } //  if (mMovesNumber >= 7)
    }   // method end

}
