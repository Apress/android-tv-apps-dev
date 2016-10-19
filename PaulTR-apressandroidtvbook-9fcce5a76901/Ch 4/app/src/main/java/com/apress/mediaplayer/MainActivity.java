package com.apress.mediaplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Paul on 10/11/15.
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, RecommendationService.class));
    }

    @Override
    public boolean onSearchRequested() {
        startActivity(new Intent(this, MediaSearchActivity.class));
        return true;
    }
}
