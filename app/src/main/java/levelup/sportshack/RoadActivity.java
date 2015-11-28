package levelup.sportshack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Andy W on 2015-11-27.
 */
public class RoadActivity extends Activity {
    ImageView hotButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent previousIntent = getIntent();
        Bundle previousBundle = previousIntent.getExtras();
        String chosenTeam = previousBundle.getString("name");
        Toast.makeText(this, chosenTeam, Toast.LENGTH_LONG).show();
        setContentView(R.layout.road_activity);
        hotButton = (ImageView)findViewById(R.id.game4);

        hotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QuestionsActivity.class);
                //TODO add functional game ID"s
                intent.putExtra("game_id", 10599);
                finish();
                startActivity(intent);
            }
        });


    }
}
