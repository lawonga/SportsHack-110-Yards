package levelup.sportshack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Andy W on 2015-11-27.
 */
public class QuestionsActivity extends Activity {
    String answer0, answer1, answer2, answer3, question, team;
    int game_id, timer, correctanswer,
            position; // Position means the current position of button
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
                    for (ParseObject parseObject : objects){
                        question = parseObject.getString("question");
                        answer0 = parseObject.getString("answer0");
                        answer1 = parseObject.getString("answer1");
                        answer2 = parseObject.getString("answer2");
                        answer3 = parseObject.getString("answer3");
                        team = parseObject.getString("team");
                        timer = parseObject.getInt("timer");

                        correctanswer = parseObject.getInt("correctanswer");

                        // SET the stuff after getting it, DUH!
                        tv_timer.setText(String.valueOf(timer));
                        tv_question.setText(question);
                        btn_answer0.setText(answer0);
                        btn_answer1.setText(answer1);
                        btn_answer2.setText(answer2);
                        btn_answer3.setText(answer3);

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

    public void answerLogic(int position){
        // Get position clicked. Upload data to server. Push to other devices.

        // Go to next screen.
        Intent intent = new Intent(getApplicationContext(), ChartActivity.class);
        startActivity(intent);
    }
}
