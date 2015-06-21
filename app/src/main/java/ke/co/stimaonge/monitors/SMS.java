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

package ke.co.stimaonge.monitors;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import ke.co.stimaonge.R;
import ke.co.stimaonge.SettingsActivity;
import ke.co.stimaonge.storage.Preference;

/**
 * Created by jrogena on 20/06/2015.
 */
public class SMS extends BroadcastReceiver {
    private static final String TAG = "StimaOnge.SMS";
    private final SmsManager smsManager = SmsManager.getDefault();
    @Override
    public void onReceive(Context context, Intent intent) {
        // Retrieves a map of extended data from the intent.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean allowPing = preferences.getBoolean(Preference.PREF_ALLOW_PING, false);
        String pingKeyword = preferences.getString(Preference.PREF_PING_KEYWORD, context.getString(R.string.pref_default_ping_keyword)).toLowerCase().trim();
        if(allowPing) {
            final Bundle bundle = intent.getExtras();
            try {
                if (bundle != null) {
                    final Object[] pdusObj = (Object[]) bundle.get("pdus");
                    for (int i = 0; i < pdusObj.length; i++) {
                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                        String message = currentMessage.getDisplayMessageBody().toLowerCase().trim();
                        if(message.equals(pingKeyword)) {
                            Log.d(TAG, "Ping SMS for power received");
                            Power power = new Power(phoneNumber);
                            power.sendStatusSMS(context);
                        }
                    } // end for loop
                } // bundle is null

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "Pinging not allowed");
    }
}
