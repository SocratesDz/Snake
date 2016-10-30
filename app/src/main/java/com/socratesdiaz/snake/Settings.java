package com.socratesdiaz.snake;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    private TextView titleLeft;
    private TextView titleRight;
    private TextView titleMiddle;

    private ImageView swipeButton;
    private ImageView musicButton;
    private ImageView homeButton;

    private Animation compileAnimation;
    private boolean isMusicOn;
    private boolean isSwipeOn;
    private RelativeLayout settingsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        settingsLayout = (RelativeLayout) findViewById(R.id.settings_layout);
        initSwipeButton();
        initMusicSwitch();
        initHomeButton();
        title();
    }

    private void initSwipeButton() {

    }
}
