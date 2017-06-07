package byenkov.com.dices.diceroller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Random;


public class RollActivity extends Activity implements View.OnClickListener{
    private ImageButton k4;
    private ImageButton k6;
    private ImageButton k8;
    private ImageButton k20;
    private Button reset;
    private TextView[] textViews = new TextView[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll);

        textViews[0] = (TextView) findViewById(R.id.k4_text);
        textViews[1] = (TextView) findViewById(R.id.k6_text);
        textViews[2] = (TextView) findViewById(R.id.k8_text);
        textViews[3] = (TextView) findViewById(R.id.k20_text);

        k4 =(ImageButton) findViewById(R.id.k4_button);
        k4.setOnClickListener(this);

        k6 =(ImageButton) findViewById(R.id.k6_button);
        k6.setOnClickListener(this);

        k8 =(ImageButton) findViewById(R.id.k8_button);
        k8.setOnClickListener(this);

        k20 =(ImageButton) findViewById(R.id.k20_button);
        k20.setOnClickListener(this);

        reset =(Button)  findViewById(R.id.reset_button);
        reset.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.roller:
                System.out.println("Roller");
                return true;
            case R.id.characters:
                Intent intent = new Intent(this,CharacterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        TextView resultTextView;
        int number;
        switch (v.getId()) {
            case R.id.reset_button:
                for (int i = 0; i < 4; i++){
                    textViews[i].setText("");
                }
                break;
            case R.id.k4_button:
                try {
                    resultTextView = (TextView) findViewById(R.id.k4_text);
                    number = 4;
                    resultTextView.append("" + (1 + new Random().nextInt(number)) + " ");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.k6_button:
                try {
                    resultTextView = (TextView) findViewById(R.id.k6_text);
                    number = 6;
                    resultTextView.append("" + (1 + new Random().nextInt(number)) + " ");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.k8_button:
                try {
                    resultTextView = (TextView) findViewById(R.id.k8_text);
                    number = 8;
                    resultTextView.append("" + (1 + new Random().nextInt(number)) + " ");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.k20_button:
                try {
                    resultTextView = (TextView) findViewById(R.id.k20_text);
                    number = 20;
                    resultTextView.append("" + (1 + new Random().nextInt(number)) + " ");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

}
