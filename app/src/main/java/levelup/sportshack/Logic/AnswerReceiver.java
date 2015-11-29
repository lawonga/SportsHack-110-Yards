package levelup.sportshack.Logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Andy W on 2015-11-28.
 */
public class AnswerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle notificationData = intent.getExtras();
        String newData  = notificationData.getString("ServiceToActivityKey");
        Intent sendIntent = new Intent("Broadcast");
        sendIntent.putExtra("notify", newData);
        context.sendBroadcast(sendIntent);

        Log.e("ping", "PING");
    }
}
