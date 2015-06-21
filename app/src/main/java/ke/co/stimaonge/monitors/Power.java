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
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Date;

import ke.co.stimaonge.R;
import ke.co.stimaonge.SettingsActivity;
import ke.co.stimaonge.notification.SMS;
import ke.co.stimaonge.storage.Preference;

/**
 * Created by jrogena on 20/06/2015.
 */
public class Power extends BroadcastReceiver{
    private static final String TAG = "StimaOnge.Power";
    private String phoneNumber;
    private boolean isPing;
    public Power(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        isPing = true;
    }

    public Power() {
        this.phoneNumber = null;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        isPing = false;
        sendStatusSMS(context);
    }

    public void sendStatusSMS(Context context) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent intent = context.registerReceiver(null, ifilter);
        SMS smsNotification = new SMS();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences otherPreferences = context.getSharedPreferences(Preference.FILE_KEY, Context.MODE_APPEND);
        Date date = new Date();
        String number = phoneNumber;
        if(number == null) {
            number = preferences.getString(Preference.PREF_CONTACT_NUMBER, null);
        }
        if(isCharging(intent) == true) {//is charging via ac
            if(isPing) {//power has been there for some time. User is just pinging
                date.setTime(otherPreferences.getLong(Preference.PREF_LAST_RESTORE_TIME, date.getTime()));
                Log.d(TAG, "Phone charging");
                String sms = String.format(context.getString(R.string.power_back), date.toString())+".";
                smsNotification.sendSMS(number, sms);
            }
            else {//power has just come back
                long lastReport = otherPreferences.getLong(Preference.PREF_LAST_RESTORE_TIME, -1);
                if(lastReport == -1 || (date.getTime() - lastReport) > 60000) {
                    SharedPreferences.Editor editor = otherPreferences.edit();
                    editor.putLong(Preference.PREF_LAST_RESTORE_TIME, date.getTime());
                    Log.d(TAG, "Phone charging");
                    String sms = String.format(context.getString(R.string.power_back), date.toString())+".";
                    smsNotification.sendSMS(number, sms);
                }
                else {
                    Log.d(TAG, "Last report for plug into power was less than 60 seconds ago");
                    Log.d(TAG, String.valueOf(lastReport));
                }
            }
        }
        else if(isCharging(intent) == false) {
            if(isPing) {//power has been lost for some time. User is just pinging
                date.setTime(otherPreferences.getLong(Preference.PREF_LAST_BLACKOUT_TIME, date.getTime()));
                Log.d(TAG, "Phone stopped charging");
                String sms = String.format(context.getString(R.string.power_lost), date.toString())
                        +". "+String.format(context.getString(R.string.battery_level), String.valueOf(getBatteryPercent(intent)+"%"));
                smsNotification.sendSMS(number, sms);
            }
            else {//power just went out
                long lastReport = otherPreferences.getLong(Preference.PREF_LAST_BLACKOUT_TIME, -1);
                if(lastReport == -1 || (date.getTime() - lastReport) > 60000){
                    SharedPreferences.Editor editor = otherPreferences.edit();
                    editor.putLong(Preference.PREF_LAST_BLACKOUT_TIME, date.getTime());
                    Log.d(TAG, "Phone stopped charging");
                    String sms = String.format(context.getString(R.string.power_lost), date.toString())
                            +". "+String.format(context.getString(R.string.battery_level), String.valueOf(getBatteryPercent(intent)+"%"));
                    smsNotification.sendSMS(number, sms);
                }
                else {
                    Log.d(TAG, "Last report for unplug was less than 60 seconds ago");
                    Log.d(TAG, String.valueOf(lastReport));
                }
            }
        }
    }

    public boolean isCharging(Intent intent) {
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;
        return isCharging;
    }

    public float getBatteryPercent(Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        float batteryPct = level/(float)scale;
        return batteryPct*100;
    }

    public boolean isChargingAC(Intent intent) {
        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        return acCharge;
    }
 }
