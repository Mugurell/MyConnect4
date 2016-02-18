package site.petrumugurel.connect4;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final Pattern PATTERN = Pattern.compile("[A-Za-z]+\\d");  // to be reused

    public void dropIn(View view) {
        ImageView counterInPosition = (ImageView) view;

        counterInPosition.setTranslationY(-1000);
        counterInPosition.setImageResource(R.drawable.white);

        counterInPosition.animate().translationYBy(1000).rotation(-480).setDuration(500);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView viewAx0 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece0x0);
        ImageView viewAx1 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece0x1);
        ImageView viewAx2 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece0x2);
        ImageView viewAx3 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece0x3);
        ImageView viewBx0 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece1x0);
        ImageView viewBx1 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece1x1);
        ImageView viewBx2 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece1x2);
        ImageView viewBx3 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece1x3);
        ImageView viewCx0 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece2x0);
        ImageView viewCx1 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece2x1);
        ImageView viewCx2 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece2x2);
        ImageView viewCx3 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece2x3);
        ImageView viewDx0 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece3x0);
        ImageView viewDx1 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece3x1);
        ImageView viewDx2 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece3x2);
        ImageView viewDx3 = (ImageView) findViewById(R.id.mainA_RL_FR_GR_IV_piece3x3);

        viewAx0.setOnClickListener(this);
        viewAx1.setOnClickListener(this);
        viewAx2.setOnClickListener(this);
        viewAx3.setOnClickListener(this);
        viewBx0.setOnClickListener(this);
        viewBx1.setOnClickListener(this);
        viewBx2.setOnClickListener(this);
        viewBx3.setOnClickListener(this);
        viewCx0.setOnClickListener(this);
        viewCx1.setOnClickListener(this);
        viewCx2.setOnClickListener(this);
        viewCx3.setOnClickListener(this);
        viewDx0.setOnClickListener(this);
        viewDx1.setOnClickListener(this);
        viewDx2.setOnClickListener(this);
        viewDx3.setOnClickListener(this);


        Board board = Board.INSTANCE;
        Toast.makeText(MainActivity.this, Integer.toString(2), Toast.LENGTH_SHORT)
             .show();
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

//        if (id == R.id.mainM_I_newGame) {
//            // // TODO: 15-Feb-16 implement the new game selected option
//        }


        return true;    // we've dealt with this, no need for other listeners to check it
    }

    @Override
    public void onClick(View v) {

    }
}
