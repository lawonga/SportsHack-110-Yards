package levelup.sportshack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Andy W on 2015-11-28.
 */
public class SplashActivity extends Activity {
    public final int SPLASH_DISPLAY_LENGTH = 500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        Thread thread = new Thread(){
            public void run (){
                try{
                    sleep(SPLASH_DISPLAY_LENGTH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    overridePendingTransition(0, R.anim.fade_out);
                    finish();
                    Intent splashScreenIntent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(splashScreenIntent);
                }
            }
        };
        thread.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
