package com.socratesdiaz.snake;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class MainMenu extends AppCompatActivity {

    private enum TITLE { LEFT, MIDDLE, RIGHT }
    private enum BUTTON { CLASSIC, NO_WALLS, BOMB, SETTINGS }

    private RelativeLayout snakeLayout;
    private Animation compileAnim;
    private AdView mAdView;
    private ImageView classicBtn;
    private ImageView noWallsBtn;
    private ImageView bombBtn;
    private ImageView settingsBtn;
    private TextView titleLeft;
    private TextView titleMiddle;
    private TextView titleRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        snakeLayout = (RelativeLayout) findViewById(R.id.snake_layout);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAdView = new AdView(this);
        mAdView.setAdSize(AdSize.SMART_BANNER);
        mAdView.setAdUnitId(GameSettings.MY_AD_UNIT_ID);
        snakeLayout.addView(mAdView);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);

        initClassic();
        initNoWalls();
        initBomb();
        initTitle();
        initSettings();
    }

    private void initClassic() {
        classicBtn = (ImageView) findViewById(R.id.classic);
        compileAnim = AnimationUtils.loadAnimation(MainMenu.this, R.anim.anim_for_classic_button);
        compileAnim.setDuration(GameSettings.ANIMATION_OPEN_BUTTON_DURATION);
        compileAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                classicBtn.setImageResource(R.drawable.classic);
                classicBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentClassic = new Intent(MainMenu.this, ClassicSnake.class);
                        intentClassic.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intentClassic);
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        classicBtn.startAnimation(compileAnim);
    }

    private void initNoWalls() {
        noWallsBtn = (ImageView) findViewById(R.id.no_walls);
        compileAnim = AnimationUtils.loadAnimation(MainMenu.this, R.anim.anim_for_no_button);
        compileAnim.setDuration(GameSettings.ANIMATION_OPEN_BUTTON_DURATION);
        compileAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                noWallsBtn.setImageResource(R.drawable.no_walls);
                noWallsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentNoWalls = new Intent(MainMenu.this, NoWallsSnake.class);
                        intentNoWalls.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intentNoWalls);
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        noWallsBtn.startAnimation(compileAnim);
    }

    private void initBomb() {
        bombBtn = (ImageView) findViewById(R.id.bomb);
        compileAnim = AnimationUtils.loadAnimation(MainMenu.this, R.anim.anim_for_bomb_button);
        compileAnim.setDuration(GameSettings.ANIMATION_OPEN_BUTTON_DURATION);
        compileAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bombBtn.setImageResource(R.drawable.bombsnake);
                bombBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent bombSnakeIntent = new Intent(MainMenu.this, BombSnake.class);
                        bombSnakeIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(bombSnakeIntent);
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        bombBtn.startAnimation(compileAnim);
    }

    private void initTitle() {
        titleLeft = (TextView) findViewById(R.id.snake_left);
        titleMiddle = (TextView) findViewById(R.id.snake_middle);
        titleRight = (TextView) findViewById(R.id.snake_right);

        int animResource = 0;
        TextView textViewSelected = null;

        for(TITLE title : TITLE.values()) {
            switch (title) {
                case LEFT:
                    animResource = R.anim.back_anim_for_title_left;
                    textViewSelected = titleLeft;
                    break;
                case MIDDLE:
                    animResource = R.anim.back_anim_for_title_middle;
                    textViewSelected = titleMiddle;
                    break;
                case RIGHT:
                    animResource = R.anim.back_anim_for_title_right;
                    textViewSelected = titleRight;
                    break;
            }

            compileAnim = AnimationUtils.loadAnimation(MainMenu.this, animResource);
            compileAnim.setDuration(GameSettings.ANIMATION_SHOW_TITLE_DURATION);
            compileAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            textViewSelected.startAnimation(compileAnim);
        }
    }

    private void initSettings() {
        settingsBtn = (ImageView) findViewById(R.id.settings);
        compileAnim = AnimationUtils.loadAnimation(MainMenu.this, R.anim.anim_for_settings_button);
        compileAnim.setDuration(GameSettings.ANIMATION_OPEN_BUTTON_DURATION);
        compileAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                settingsBtn.setImageResource(R.drawable.settings);
                settingsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        int animResource = 0;
                        View viewSelected = null;

                        for(BUTTON button : BUTTON.values()) {
                            switch (button) {
                                case CLASSIC:
                                    animResource = R.anim.reverse_for_classic_button;
                                    viewSelected = classicBtn;
                                    break;
                                case NO_WALLS:
                                    animResource = R.anim.reverse_for_no_button;
                                    viewSelected = noWallsBtn;
                                    break;
                                case BOMB:
                                    animResource = R.anim.reverse_for_bomb_button;
                                    viewSelected = bombBtn;
                                    break;
                                case SETTINGS:
                                    animResource = R.anim.reverse_for_settings_button;
                                    viewSelected = settingsBtn;
                                    break;
                            }

                            ((ImageView)viewSelected).setImageResource(R.drawable.menu_options);
                            Animation endAnimation = AnimationUtils.loadAnimation(MainMenu.this,
                                    animResource);
                            endAnimation.setDuration(GameSettings.ANIMATION_CLOSE_BUTTON_DURATION);
                            viewSelected.startAnimation(endAnimation);
                        }

                        for(TITLE title : TITLE.values()) {
                            switch (title) {
                                case LEFT:
                                    animResource = R.anim.anim_for_title_left;
                                    viewSelected = titleLeft;
                                    break;
                                case MIDDLE:
                                    animResource = R.anim.anim_for_title_middle;
                                    viewSelected = titleMiddle;
                                    break;
                                case RIGHT:
                                    animResource = R.anim.anim_for_title_right;
                                    viewSelected = titleRight;
                                    break;
                            }

                            Animation endAnimation = AnimationUtils.loadAnimation(MainMenu.this,
                                    animResource);
                            endAnimation.setDuration(GameSettings.ANIMATION_CLOSE_BUTTON_DURATION);
                            viewSelected.startAnimation(endAnimation);
                        }

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent settingsIntent = new Intent(MainMenu.this, Settings.class);
                                settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(settingsIntent);
                            }
                        }, GameSettings.START_NEW_ACTIVITY_DURATION);
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        settingsBtn.startAnimation(compileAnim);
    }
}
