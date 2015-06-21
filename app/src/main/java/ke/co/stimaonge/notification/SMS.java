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

package ke.co.stimaonge.notification;

import android.telephony.SmsManager;
import android.util.Log;

/**
 * Created by jrogena on 20/06/2015.
 */
public class SMS {
    private final static String TAG = "StimaOnge.SMS";
    private final SmsManager smsManager;
    public SMS() {
        smsManager = SmsManager.getDefault();
    }

    public void sendSMS(String number, String text) {
        if(number != null && number.length() > 0
                && text != null && text.length() > 0) {
            Log.d(TAG, "Sending SMS '"+text+"' to '"+number+"'");
            smsManager.sendTextMessage(number, null, text, null, null);
        }
    }
}
