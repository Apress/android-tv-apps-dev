package com.apress.helloworld;

import android.os.Bundle;
import android.support.v17.preference.LeanbackPreferenceFragment;
import android.view.View;

/**
 * Created by Paul on 9/16/15.
 */
public class SettingsFragment extends LeanbackPreferenceFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource( R.xml.preferences );

    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
