package levelup.sportshack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Andy W on 2015-11-27.
 */
public class RoadActivity extends Activity {
    public static boolean triggered = false;
    int game_id = 10599;
    int questionType;
    String questionId;
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

        // Get the question first to see if its a two or four answer one...
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Questionbank");
        query.whereEqualTo("game_id", game_id);
        query.whereEqualTo("isactive", true);
        query.whereEqualTo("numberofanswers", 2);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject parseObject : objects) {
                        questionType = parseObject.getInt("numberofanswers");
                        questionId = parseObject.getObjectId();
                        Log.e("numberofanswers", String.valueOf(questionType));
                    }
                    hotButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = null;
                            if(questionType == 4) {
                                intent = new Intent(getApplicationContext(), FOURQuestionsActivity.class);
                            } else if (questionType == 2){
                                intent = new Intent(getApplicationContext(), TWOQuestionsActivity.class);
                            }
                            //TODO add functional game ID"s
                            intent.putExtra("game_id", game_id);
                            intent.putExtra("objectId", questionId);
                            finish();
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }
}
