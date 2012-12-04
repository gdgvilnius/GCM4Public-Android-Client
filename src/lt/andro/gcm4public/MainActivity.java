
package lt.andro.gcm4public;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

/**
 * This is a demo application of GCM4Public open-source project.<br/>
 * <br/>
 * How to create this application, please visit
 * http://www.andro.lt/2012/11/google-cloud-messaging-for-android.html or the project's page:
 * gcm4public.appspot.com
 * 
 * @author Vilius Kraujutis viliusk@gmail.com
 */
public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    protected static final String SENDER_ID = "716163315987";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString("title");
            String message = extras.getString("message");
            String url = extras.getString("url");

            if (url != null && !"".equalsIgnoreCase(url)) {
                Uri uri = Uri.parse(url);
                String text = title + "\n" + message;
                if (text.length() != 1) {
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                }
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        }
        registerAtGCM();
    }

    private void registerAtGCM() {
        GCMRegistrar.checkDevice(MainActivity.this);
        GCMRegistrar.checkManifest(MainActivity.this);
        final String regId = GCMRegistrar.getRegistrationId(MainActivity.this);
        if (regId.equals("")) {
            GCMRegistrar.register(MainActivity.this, SENDER_ID);
        } else {
            Log.v(TAG, "Already registered: " + regId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void openUrl(View v) {
        Uri uri = Uri.parse("https://gcm4public.appspot.com");
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

}
