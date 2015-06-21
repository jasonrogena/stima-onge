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

package ke.co.stimaonge.storage;

/**
 * Created by jrogena on 20/06/2015.
 */
public class Preference {
    public static final String FILE_KEY = "StimaOnge";
    public static final String PREF_CONTACT_NUMBER = "contact_number";
    public static final String PREF_ALLOW_PING = "allow_ping";
    public static final String PREF_PING_KEYWORD = "ping_keyword";
    public static final String PREF_PERSISTENT_NOTIFICATION = "persistent_notification";
    public static final String PREF_LAST_BLACKOUT_TIME = "last_blackout_time";
    public static final String PREF_LAST_RESTORE_TIME = "last_restore_time";
}
