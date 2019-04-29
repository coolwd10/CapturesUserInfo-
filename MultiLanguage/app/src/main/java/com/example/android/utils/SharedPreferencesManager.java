package com.example.android.utils;

import android.content.SharedPreferences;

import java.util.Set;


public class SharedPreferencesManager {


    public static final String REGISTERED = "registered";

    public static final String UNIQUE_ID = "uniqueid";

    public SharedPreferences mSharedPreferences;

    public SharedPreferencesManager(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    /**
     * Set an int value in the preferences.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     */
    private void putInt(String key, int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * Set String Set in the preferences
     *
     * @param key
     * @param value
     */
    public void putStringSet(String key, Set<String> value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    /**
     * Set a float value in the preferences.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     */
    private void putFloat(String key, float value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    /**
     * Set a long value in the preferences.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     */
    private void putLong(String key, long value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * Set a boolean value in the preferences.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     */
    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * Set a string value in the preferences.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.  Supplying {@code null}
     *              as the value is equivalent to calling {@link #remove(String)} with
     *              this key.
     */
    private void putString(String key, String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Retrieve an int value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.
     */
    private int getInt(String key, int defValue) {
        SharedPreferences sharedPreferences = mSharedPreferences;
        return sharedPreferences.getInt(key, defValue);
    }

    /**
     * Retrieve a long value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.
     */
    private long getLong(String key, long defValue) {
        SharedPreferences sharedPreferences = mSharedPreferences;
        return sharedPreferences.getLong(key, defValue);
    }

    /**
     * Retrive the store String set for the Key
     *
     * @param key
     * @param defaultValue
     * @return Returns the preference value if it exists, or defValue.
     */
    private Set<String> getStringSet(String key, Set<String> defaultValue) {
        SharedPreferences sharedPreferences = mSharedPreferences;
        return sharedPreferences.getStringSet(key, defaultValue);
    }

    /**
     * Retrieve a float value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.
     */
    private float getFloat(String key, float defValue) {
        SharedPreferences sharedPreferences = mSharedPreferences;
        return sharedPreferences.getFloat(key, defValue);
    }

    /**
     * Retrieve a boolean value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.
     */
    public boolean getBoolean(String key, boolean defValue) {
        SharedPreferences sharedPreferences = mSharedPreferences;
        return sharedPreferences.getBoolean(key, defValue);
    }

    /**
     * Retrieve a string value from the preferences.
     *
     * @param key      The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defValue.
     */
    private String getString(String key, String defValue) {
        SharedPreferences sharedPreferences = mSharedPreferences;
        return sharedPreferences.getString(key, defValue);
    }

    /**
     * Remove preference value.
     *
     * @param key The name of the preference to remove.
     */
    public void remove(String key) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * Remove all the preference values.
     */
    public void removeAll() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * Checks whether the preferences contains a preference.
     *
     * @param key The name of the preference to check.
     * @return Returns true if the preference exists in the preferences,
     * otherwise false.
     */
    public boolean contains(String key) {
        return mSharedPreferences.contains(key);
    }

}
