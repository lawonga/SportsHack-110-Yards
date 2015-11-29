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
import com.parse.Parse;
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

/**
 * Created by Andy W on 2015-11-27.
 */
public class QuestionsActivity extends Activity {
    String answer0, answer1, answer2, answer3, question, team, questionID;
    int game_id, timer,
            position, // Position means the current position of button
            point0, point1, point2, point3, numberofanswers;

    TextView tv_question, tv_timer;
    Button btn_answer0, btn_answer1, btn_answer2, btn_answer3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_activity);

        //Get data from previous activity
        Intent previousIntent = getIntent();
        Bundle previousBunde = previousIntent.getExtras();
        game_id = previousBunde.getInt("game_id");

        //Initialize
        tv_timer = (TextView) findViewById(R.id.timer);
        tv_question = (TextView)findViewById(R.id.question);
        btn_answer0 = (Button)findViewById(R.id.button0);
        btn_answer1 = (Button)findViewById(R.id.button1);
        btn_answer2 = (Button)findViewById(R.id.button2);
        btn_answer3 = (Button)findViewById(R.id.button3);


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Questionbank");

        /*TODO Should query the questions based on a very complex formula.
        TODO This stuff really should be on the server side, but for the purpose of the hackathonw we're gonna put everything IN HERE LOL
        TODO Need to implement a way of keeping track of the question number answered*/
        /** right now, it is by game_id
         * Later on, add in depending on ParseUser. have ParseUser tracked questions answer**/
        query.whereEqualTo("game_id", game_id);
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
                        answer2 = parseObject.getString("answer2");
                        answer3 = parseObject.getString("answer3");
                        team = parseObject.getString("team");
                        timer = parseObject.getInt("timer");
                        questionID = parseObject.getObjectId();
                        point0 = parseObject.getInt("point0");
                        point1 = parseObject.getInt("point1");
                        point2 = parseObject.getInt("point2");
                        point3 = parseObject.getInt("point3");

                        // SET the stuff after getting it, DUH!
                        tv_timer.setText(String.valueOf(timer));
                        tv_question.setText(question);
                        btn_answer0.setText(answer0);
                        btn_answer1.setText(answer1);
                        btn_answer2.setText(answer2);
                        btn_answer3.setText(answer3);

                        new CountDownTimer(timer*1000, 1000){

                            @Override
                            public void onTick(long millisUntilFinished) {
                                tv_timer.setText(String.valueOf((millisUntilFinished / 1000)));
                            }

                            @Override
                            public void onFinish() {
                                // Go to next screen.
                                Intent intent = new Intent(getApplicationContext(), ChartActivity.class);
                                intent.putExtra("game_id", game_id);
                                startActivity(intent);
                                finish();
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
                        btn_answer2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                position = 2;
                                answerLogic(position);
                            }
                        });
                        btn_answer3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                position = 3;
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
                    startActivity(intent);
                    finish();
                } catch (ParseException g) {
                    g.printStackTrace();
                    Log.e("PushError", e.toString());
                }
            }
        });
    }
}
