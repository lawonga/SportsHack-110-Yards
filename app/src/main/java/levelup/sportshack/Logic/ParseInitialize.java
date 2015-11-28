package levelup.sportshack.Logic;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

/**
 * Created by Andy W on 2015-11-27.
 */
public class ParseInitialize extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "mYv0mweKCSgpXcbMejRKzmT7sUN49xvI6HxJgiIH", "iAmc12yiMu25E1MF3UKx1v0OMgyU8FcDpZWcnaxz");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
    }
}
