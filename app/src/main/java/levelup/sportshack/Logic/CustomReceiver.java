package levelup.sportshack.Logic;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andy W on 2015-11-28.
 */
public class CustomReceiver extends ParsePushBroadcastReceiver {
    JSONObject jsonObject;
    int point, answer;
    @Override
    protected Notification getNotification(Context context, Intent intent) {
        String data = intent.getExtras().getString("com.parse.Data");
        try {
            jsonObject = new JSONObject(data);
            point = jsonObject.getInt("point");
            answer = jsonObject.getInt("answer");
            Intent sendIntent = new Intent("broadcast"); // broadcast is the name we're sending to ChartActivity and receiving from ChartActivity
            sendIntent.putExtra("point", point);
            sendIntent.putExtra("answer", answer);
            context.sendBroadcast(sendIntent);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Toast.makeText(context, String.valueOf(point), Toast.LENGTH_LONG).show();
        return null;
    }
}
