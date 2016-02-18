package site.petrumugurel.connect4;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

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
}
