/*
 Stima Onge is a simple Android application for monitoring electricity in your home. Copyright (c) 2015. Jason Rogena

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ke.co.stimaonge;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by jrogena on 20/06/2015.
 */
public class PersistentService extends IntentService {
    private static final String TAG = "StimaOnge.PersistentService";

    public static final long RESTART_INTERVAL = 30000l;//30 seconds
    private static int NOTIFICATION_ID = 9232;
    private static String ODK_COLLECT_PACKAGE_NAME = "org.odk.collect.android";

    /**
     * Default constructor. Make sure this is there or less android will be unable to call the service
     */
    public PersistentService() {
        super(TAG);
    }

    /**
     * Another constructor.
     * In case you want to call this service by another name e.g. if you create a class that is a sub-class
     * of this class
     *
     * @param name
     */
    public PersistentService(String name) {
        super(name);
    }

    /**
     * This method is called whenever the service is called. Note that the service might have already been running when
     * it was called
     *
     * @param intent The intent with which the service was called
     */
    @Override
    protected void onHandleIntent(Intent intent) {

    }

    /**
     * This method updates/creates the notification for this service.
     * Note that the notification generated here is a compact notification
     *
     * @param title The title for the notification
     * @param details The details of the notification
     */
    private void updateNotification(String title, String details){
        Intent intent = new Intent(this, SettingsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(details)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(details))
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
