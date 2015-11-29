package levelup.sportshack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Andy W on 2015-11-28.
 */
public class TWOQuestionsActivity extends Activity {
    Boolean active = false;
    String answer0, answer1, question, team, questionID, object_id;
    int game_id, timer,
            position, // Position means the current position of button
            point0, point1, point2, point3, numberofanswers, currentTime;

    TextView tv_question, tv_timer;
    CircleImageView btn_answer0, btn_answer1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_questions_activity);
        active = true;
        //Get data from previous activity
        Intent previousIntent = getIntent();
        Bundle previousBunde = previousIntent.getExtras();
        game_id = previousBunde.getInt("game_id");
        object_id = previousBunde.getString("objectId");

        //Initialize
        tv_timer = (TextView) findViewById(R.id.timer);
        tv_question = (TextView)findViewById(R.id.question);
        btn_answer0 = (CircleImageView)findViewById(R.id.button0);
        btn_answer1 = (CircleImageView)findViewById(R.id.button1);


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Questionbank");
        /*TODO Should query the questions based on a very complex formula.
        TODO This stuff really should be on the server side, but for the purpose of the hackathon we're gonna put everything IN HERE LOL*/
        /** right now, it is by game_id
         * Later on, add in depending on ParseUser. have ParseUser track questions answered**/
        query.whereEqualTo("game_id", game_id);
        query.whereEqualTo("objectId", object_id);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null){

                    // instead of getting all the parseobjects, we get only ONE parseobject according to the formula defined above
                    // (supposedly))
                    for (final ParseObject parseObject : objects){
                        question = parseObject.getString("question");
                        answer0 = parseObject.getString("answer0");
                        answer1 = parseObject.getString("answer1");
                        team = parseObject.getString("team");
                        timer = parseObject.getInt("timer");
                        questionID = parseObject.getObjectId();
                        point0 = parseObject.getInt("point0");
                        point1 = parseObject.getInt("point1");

                        Log.e("questionQueried", question);
                        // No need to set it here because yes/no is pretty self explanatory
                        tv_timer.setText(String.valueOf(timer));
                        tv_question.setText(question);
                        /* btn_answer0.setText(answer0);
                        btn_answer1.setText(answer1); */

                        new CountDownTimer(timer*1000, 1000){
                            @Override
                            public void onTick(long millisUntilFinished) {
                                tv_timer.setText(String.valueOf((millisUntilFinished / 1000)));
                                currentTime = (int) (millisUntilFinished/1000);
                            }

                            @Override
                            public void onFinish() {
                                if (active == true) {
                                    // Go to next screen.
                                    Intent intent = new Intent(getApplicationContext(), ChartActivity.class);
                                    intent.putExtra("game_id", game_id);
                                    intent.putExtra("objectID", questionID);
                                    startActivity(intent);
                                    active = false;
                                    finish();
                                } else {
                                    //This means the user ran out of time
                                    String newData = "trigger";
                                    Intent broadcastIntent = new Intent();
                                    broadcastIntent.setAction("ServiceToActivityAction");
                                    broadcastIntent.putExtra("ServiceToActivityKey", newData);
                                    sendBroadcast(broadcastIntent);

                                }
                            }
                        }.start();

                        // Click listeners
                        btn_answer0.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                position = 0;
                                answerLogic(position);
                            }
                        });
                        btn_answer1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                position = 1;
                                answerLogic(position);
                            }
                        });
                    }
                }
            }
        });
    }

    public void answerLogic(final int position){
        final int pointworth = 5;
        // Get position clicked. Upload data to server in user's history.
        ParseUser parseUser = ParseUser.getCurrentUser();

        // Create object player answer
        ParseObject playerObject = new ParseObject("PlayerAnswer");
        playerObject.put("questionID", questionID);
        playerObject.put("answer", position);

        // Create relation question ID
        ParseRelation<ParseObject> parseRelation = parseUser.getRelation("questionID");
        parseRelation.add(playerObject);

        parseUser.put("questionKey", playerObject);
        parseUser.saveInBackground();

        // Do a second query to update the score PROPERLY
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Questionbank");
        query.whereEqualTo("game_id", game_id);
        query.whereEqualTo("objectId", questionID);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                for (ParseObject parseObject : objects) {
                    point0 = parseObject.getInt("point0");
                    point1 = parseObject.getInt("point1");
                    point2 = parseObject.getInt("point2");
                    point3 = parseObject.getInt("point3");
                    // Get position clicked. Upload data to server in the question to track points
                    if (position == 0) {
                        parseObject.put("point0", point0 += pointworth);
                    } else if (position == 1) {
                        parseObject.put("point1", point1 += pointworth);
                    } else if (position == 2) {
                        parseObject.put("point2", point2 += pointworth);
                    } else if (position == 3) {
                        parseObject.put("point3", point3 += pointworth);
                    }
                    parseObject.saveInBackground();
                }

                // Push to other devices.
                ParsePush parsePush = new ParsePush();

                // Lets not push to self... EDIT Pushing to self is OK!
                // Associate the device with a user
                ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                installation.put("user", ParseUser.getCurrentUser());
                try {
                    installation.save();
                    ParseQuery pushQuery = ParseInstallation.getQuery();
                    pushQuery.whereNotEqualTo("user", ParseUser.getCurrentUser());

                    // Create JSONObject
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("answer", position);
                        jsonObject.put("point", pointworth);
                        jsonObject.put("questionID", questionID);
                        jsonObject.put("team", team);
                    } catch (JSONException f) {
                        f.printStackTrace();
                    }
                    parsePush.setData(jsonObject);
                    parsePush.setQuery(pushQuery);
                    parsePush.sendInBackground();

                    // Go to next screen.
                    Intent intent = new Intent(getApplicationContext(), ChartActivity.class);
                    intent.putExtra("game_id", game_id);
                    intent.putExtra("numberofanswers", numberofanswers);
                    intent.putExtra("objectID", questionID);
                    intent.putExtra("currentTime", currentTime);
                    startActivity(intent);
                    active = false;
                    finish();
                } catch (ParseException g) {
                    g.printStackTrace();
                    Log.e("PushError", e.toString());
                }
            }
        });
    }
}
