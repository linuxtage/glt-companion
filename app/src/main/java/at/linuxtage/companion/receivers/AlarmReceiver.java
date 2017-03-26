package at.linuxtage.companion.receivers;

import at.linuxtage.companion.alarms.FosdemAlarmManager;
import at.linuxtage.companion.services.AlarmIntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import at.linuxtage.companion.BuildConfig;

/**
 * Entry point for system-generated events: boot complete and alarms.
 *
 * @author Christophe Beyls
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

	public static final String ACTION_NOTIFY_EVENT = BuildConfig.APPLICATION_ID + ".action.NOTIFY_EVENT";

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if (ACTION_NOTIFY_EVENT.equals(action)) {

			// Forward the intent to the AlarmIntentService for background processing of the notification
			Intent serviceIntent = new Intent(context, AlarmIntentService.class)
					.setAction(ACTION_NOTIFY_EVENT)
					.setData(intent.getData())
					.putExtra(AlarmIntentService.EXTRA_WITH_WAKE_LOCK, true);
			startWakefulService(context, serviceIntent);

		} else if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {

			String serviceAction = FosdemAlarmManager.getInstance().isEnabled()
					? AlarmIntentService.ACTION_UPDATE_ALARMS : AlarmIntentService.ACTION_DISABLE_ALARMS;
			Intent serviceIntent = new Intent(context, AlarmIntentService.class)
					.setAction(serviceAction)
					.putExtra(AlarmIntentService.EXTRA_WITH_WAKE_LOCK, true);
			startWakefulService(context, serviceIntent);
		}
	}

}
