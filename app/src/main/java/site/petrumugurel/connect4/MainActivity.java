package site.petrumugurel.connect4;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout mBoardGrid;
    private LinearLayout   mLl1;
    private LinearLayout   mLl2;
    private LinearLayout   mLl3;
    private LinearLayout   mLl4;
    private Board          mBoard;
    private EditText       mPlayerNameET;
    private EditText mAINameET;

    public void dropIn(Board.PLAYERS player, int row, int col) {
        ViewGroup currLine = (ViewGroup) mBoardGrid.getChildAt(col);

        ImageView diskPosition = (ImageView) currLine.getChildAt(row);

        diskPosition.setTranslationY(-1000);

        int diskDrawable = (player == Board.PLAYERS.PLAYER ? R.drawable.white
                                                           : R.drawable.red);
        diskPosition.setImageResource(diskDrawable);

        diskPosition.animate().translationYBy(1000).rotation(-480).setDuration(500);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ImageView viewAx0 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece0x0);
//        ImageView viewAx1 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece0x1);
//        ImageView viewAx2 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece0x2);
//        ImageView viewAx3 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece0x3);
//        ImageView viewBx0 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece1x0);
//        ImageView viewBx1 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece1x1);
//        ImageView viewBx2 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece1x2);
//        ImageView viewBx3 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece1x3);
//        ImageView viewCx0 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece2x0);
//        ImageView viewCx1 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece2x1);
//        ImageView viewCx2 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece2x2);
//        ImageView viewCx3 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece2x3);
//        ImageView viewDx0 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece3x0);
//        ImageView viewDx1 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece3x1);
//        ImageView viewDx2 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece3x2);
//        ImageView viewDx3 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece3x3);

//        viewAx0.setOnClickListener(this);
//        viewAx1.setOnClickListener(this);
//        viewAx2.setOnClickListener(this);
//        viewAx3.setOnClickListener(this);
//        viewBx0.setOnClickListener(this);
//        viewBx1.setOnClickListener(this);
//        viewBx2.setOnClickListener(this);
//        viewBx3.setOnClickListener(this);
//        viewCx0.setOnClickListener(this);
//        viewCx1.setOnClickListener(this);
//        viewCx2.setOnClickListener(this);
//        viewCx3.setOnClickListener(this);
//        viewDx0.setOnClickListener(this);
//        viewDx1.setOnClickListener(this);
//        viewDx2.setOnClickListener(this);
//        viewDx3.setOnClickListener(this);



        mBoardGrid = (RelativeLayout) findViewById(R.id.mainA_RL_FR_RL_grid);
        checkIfValidDisksNumber();

        mLl1 = (LinearLayout) findViewById(R.id.mainA_RL_FR_RL_LL_col1);
        mLl2 = (LinearLayout) findViewById(R.id.mainA_RL_FR_RL_LL_col2);
        mLl3 = (LinearLayout) findViewById(R.id.mainA_RL_FR_RL_LL_col3);
        mLl4 = (LinearLayout) findViewById(R.id.mainA_RL_FR_RL_LL_col4);

        mLl1.setOnClickListener(this);
        mLl2.setOnClickListener(this);
        mLl3.setOnClickListener(this);
        mLl4.setOnClickListener(this);

        mPlayerNameET = (EditText) findViewById(R.id.mainA_RL_TV_playerName);
        mAINameET = (EditText) findViewById(R.id.mainA_RL_TV_AIName);
        
        

//        mBoard = Board.INSTANCE;
//        mBoard.init(4, 4, 3);
        mBoard = new Board(4, 4, 3);

        clearBoard();
//
//        while (!mBoard.haveWinner()) {
//            // make user move
//            // make AI move
//            // check for winner
//            // prints a dialog informing about result and ask if user wants new game
//        }
    }

    /**
     * Double check if each row of the board has exactly the same number of disks positions.
     */
    private void checkIfValidDisksNumber() {
        // Double check if each row has exactly the same number of disk positions
        int newphews = ((ViewGroup) mBoardGrid.getChildAt(0)).getChildCount();
        for (int x = mBoardGrid.getChildCount() - 1; x >= 0; x--) {
            ViewGroup currLine = (ViewGroup) mBoardGrid.getChildAt(x);
            if (newphews != currLine.getChildCount()) {
                throw new RuntimeException("All lines must have same number of disks.");
            }
        }
    }


    /**
     * Set all disks images to blank - transparent and reset all board counters for the disks.
     */
    private void clearBoard() {
        for (int x = mBoardGrid.getChildCount() - 1; x >= 0; x--) {
            ViewGroup currLine = (ViewGroup) mBoardGrid.getChildAt(x);

            for (int y = currLine.getChildCount() - 1; y >= 0; y--) {
                ((ImageView) currLine.getChildAt(y)).setImageDrawable(null);
            }
        }

        // Reset all board counters
        mBoard.clearBoard();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;    // signal that we've dealt with the menu and want it displayed.
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.mainM_I_settings) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                                              "No available settings atm.", Snackbar.LENGTH_SHORT);
            snackbar.show();

        }

        if (id == R.id.mainM_I_newGame) {
            // // TODO: 15-Feb-16 implement the new game selected option
            clearBoard();
        }


        return true;    // we've dealt with this, no need for other listeners to check it
    }


    private boolean playerMoveToColumn(int column) {
        Integer row = mBoard.makePlayerMove(column);

        if (row != null) {
            ViewGroup currLine = (ViewGroup) mBoardGrid.getChildAt(column);

            Log.d("Player disk stored at", "[" + Integer.toString(row) + "]["
                                           + Integer.toString(column) + "]");
            dropIn(Board.PLAYERS.PLAYER, row, column);
            Log.d("player disk", "dropped");
//            return true;
        }

        Integer[] AIMovePosition =  mBoard.makeAIMove();
        Log.d("AI disk stored at", "[" + Integer.toString(AIMovePosition[0]) + "]["
                                   + Integer.toString(AIMovePosition[1]) + "]");
        if (AIMovePosition != null) {
            int row2 = AIMovePosition[0];
            int col = AIMovePosition[1];
            ViewGroup currLine = (ViewGroup) mBoardGrid.getChildAt(col);

            dropIn(Board.PLAYERS.AI, row2, col);
//            return true;
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        String clickedLine = "Line ";
        switch (v.getId()) {
            case R.id.mainA_RL_FR_RL_LL_col1:
                playerMoveToColumn(0);
                clickedLine += "0";
                break;
            case R.id.mainA_RL_FR_RL_LL_col2:
                playerMoveToColumn(1);
                clickedLine += "1";
                break;
            case R.id.mainA_RL_FR_RL_LL_col3:
                clickedLine += "2";
                playerMoveToColumn(2);
                break;
            case R.id.mainA_RL_FR_RL_LL_col4:
                clickedLine += "3";
                playerMoveToColumn(3);
                break;
        }
        boolean gameOver = false;
        String winner = "";
        mBoard.checkForWinner();
        if (mBoard.mHaveWinner) {
            winner = mBoard.mPlayerWon ? mPlayerNameET.getText().toString()
                                              : mAINameET.getText().toString();
            Toast.makeText(MainActivity.this, winner + " won!", Toast.LENGTH_SHORT).show();
            gameOver = true;
        }
        else if (mBoard.mIsDraw) {
            Toast.makeText(MainActivity.this, "It's a draw!", Toast.LENGTH_SHORT).show();
            gameOver = true;
        }

        if (gameOver) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this)
                            .setTitle(winner + " won")
                            .setMessage("Wanna test your luck again?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    clearBoard();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(MainActivity.this, "BBye!",
                                                   Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
            builder.show();
        }
    }
}
