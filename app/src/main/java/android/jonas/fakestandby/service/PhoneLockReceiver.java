package android.jonas.fakestandby.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.jonas.fakestandby.utils.Constants;
import android.os.PowerManager;
import android.util.Log;

import java.util.Objects;

public class PhoneLockReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), Intent.ACTION_SCREEN_OFF)) {
            Intent new_intent = new Intent(context, AccessibilityOverlayService.class);
            new_intent.putExtra(Constants.Intent.Extra.OverlayAction.KEY, Constants.Intent.Extra.OverlayAction.SHOW);
            context.startService(new_intent);

            Log.i(
                    getClass().getName(),
                    "Sent intent to overlay: " + new_intent.getByteExtra(
                            Constants.Intent.Extra.OverlayAction.KEY,
                            Constants.Intent.Extra.OverlayAction.DEFAULT
                    )
            );

            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                    "fakestandby:overlaystopped"
            );
            wakeLock.acquire(10000);
            wakeLock.release();
        }
    }
}
