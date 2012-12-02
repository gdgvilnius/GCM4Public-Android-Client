package lt.andro.gcm4public;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

/**
 * Intent which is handling GCM messages and registrations. <br/>
 * <br/>
 * {@link GCMIntentService#onMessage} will handle message from GCM4Public server
 * - show up a notification and vibrate the phone.
 * {@link GCMIntentService#onRegistered} will send the registrationId and
 * SENDER_ID constant to the server.<br/>
 * <br/>
 * In order this class to work, don't forget to copy the gcm.jar file to libs
 * folder.
 * 
 * @author Vilius Kraujutis
 * @since 2012-12-01
 */
public class GCMIntentService extends GCMBaseIntentService {

	@Override
	protected void onError(Context context, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMessage(Context context, Intent messageIntent) {
		makeVibration(context);
		Log.d(TAG, "onMessage: " + messageIntent);
		Bundle extras = messageIntent.getExtras();

		String title = extras.getString("title");
		String message = extras.getString("message");
		String url = extras.getString("url");

		showNotification(title, message, url);
	}

	private static void makeVibration(Context context) {
		// Get instance of Vibrator from current Context
		Vibrator v = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);

		// Vibrate for 30 milliseconds
		v.vibrate(30);
	}

	private void showNotification(String title, String message, String url) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(title).setContentText(message);
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, MainActivity.class);
		resultIntent.putExtra("title", title);
		resultIntent.putExtra("message", message);
		resultIntent.putExtra("url", url);

		// The stack builder object will contain an artificial back stack for
		// the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(1, mBuilder.build());
	}

	@Override
	protected void onRegistered(Context arg0, String registrationId) {
		// registrationId is something like this:
		// APA91bH6fqNq7-MmMdDaQLcegqa8vbqoPXcvqwso_owIFaUR794cl0gmRJr3n_nQEPqUwfR_HvxERUgQvVKXPN3HQoTt5_k8BMmeeWunKHsg8dBCxvMcIM0K6YndMX2DU4ne3STyOFRJjkeBynXL19yy7Dqn53UbNA
		registerGCMClient(registrationId, MainActivity.SENDER_ID);
	}

	/**
	 * This is called when application first time registers for the GCM.<br/>
	 * <br/>
	 * This method registers on the opensource GCM4Public server
	 * 
	 * @param registrationId
	 * @param senderId
	 */
	private void registerGCMClient(String registrationId, String senderId) {
		String url = "http://gcm4public.appspot.com/registergcmclient?senderId="
				+ senderId + "&registrationId=" + registrationId;
		Log.d(TAG, url);
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		try {
			httpclient.execute(httpget);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub

	}

}
