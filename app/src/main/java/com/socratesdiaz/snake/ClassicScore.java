package com.socratesdiaz.snake;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


public class ClassicScore extends AppCompatActivity {

    private TextView scoreTextView;
    private TextView highScoreTextView;
    private ImageView playAgainImageView;
    private ImageView mainMenuImageView;

    private Animation animation;
    private TextView mGameOverTitleLeftTextView;
    private TextView mGameOverTitleRightTextView;
    private TextView mGameOverTitleMiddleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic_score);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        if(getSupportActionBar() != null)
            getSupportActionBar().hide();

        initTitle();
        initScore();
        initHighScore();
        initPlayAgain();
        initMainMenu();
    }

    public void initScore() {
        scoreTextView = (TextView) findViewById(R.id.player_score);
        animation = AnimationUtils.loadAnimation(this, R.anim.anim_for_classic_button);
        animation.setDuration(GameSettings.ANIMATION_OPEN_BUTTON_DURATION);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                SharedPreferences preferences = getApplicationContext().getSharedPreferences(
                        GameSettings.PREFS_NAME, Context.MODE_PRIVATE
                );
                int playerScore = preferences.getInt("Score", 0);
                scoreTextView.setText("Score: " + String.valueOf(playerScore));
                scoreTextView.setTextColor(Color.WHITE);
                scoreTextView.setGravity(Gravity.CENTER);
                scoreTextView.setBackgroundResource(R.drawable.menu_options);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        scoreTextView.startAnimation(animation);
    }

    private void initHighScore() {
        highScoreTextView = (TextView) findViewById(R.id.mode_high_score);
        animation = AnimationUtils.loadAnimation(this, R.anim.anim_for_no_button);
        animation.setDuration(GameSettings.ANIMATION_OPEN_BUTTON_DURATION);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setHighScore();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        highScoreTextView.startAnimation(animation);
    }

    private void setHighScore() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(
                GameSettings.PREFS_NAME, Context.MODE_PRIVATE
        );
        SharedPreferences.Editor editor = preferences.edit();
        int highScore = preferences.getInt("HighScoreClassic", 0);
        int lastScore = preferences.getInt("Score", 0);
        if(lastScore > highScore) {
            editor.putInt("HighScoreClassic", lastScore);
            editor.apply();
            highScore = lastScore;
        }

        highScoreTextView = (TextView) findViewById(R.id.mode_high_score);
        highScoreTextView.setText("High: " + String.valueOf(highScore));
        highScoreTextView.setTextColor(Color.WHITE);
        highScoreTextView.setGravity(Gravity.CENTER);
        highScoreTextView.setBackgroundResource(R.drawable.menu_options);
    }

    private void initPlayAgain() {
        playAgainImageView = (ImageView) findViewById(R.id.play_again);
        animation = AnimationUtils.loadAnimation(this, R.anim.anim_for_settings_button);
        animation.setDuration(GameSettings.ANIMATION_OPEN_BUTTON_DURATION);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                playAgainImageView.setImageResource(R.drawable.again);
                playAgainImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentClassic = new Intent(ClassicScore.this, ClassicSnake.class);
                        intentClassic.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intentClassic);
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        playAgainImageView.startAnimation(animation);
    }

    private void initMainMenu() {
        mainMenuImageView = (ImageView) findViewById(R.id.goto_main_menu);
        animation = AnimationUtils.loadAnimation(ClassicScore.this, R.anim.anim_for_bomb_button);
        animation.setDuration(GameSettings.ANIMATION_OPEN_BUTTON_DURATION);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mainMenuImageView.setImageResource(R.drawable.menu);
                mainMenuImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        scoreTextView.setBackgroundResource(R.drawable.menu_options);
                        scoreTextView.setText("");
                        scoreTextView.setTextColor(Color.BLACK);

                        highScoreTextView.setBackgroundResource(R.drawable.menu_options);
                        highScoreTextView.setText("");
                        highScoreTextView.setTextColor(Color.BLACK);

                        playAgainImageView.setBackgroundResource(R.drawable.menu_options);
                        mainMenuImageView.setBackgroundResource(R.drawable.menu_options);

                        // Reverse animation
                        Animation animationScore = AnimationUtils.loadAnimation(ClassicScore.this, R.anim.reverse_for_classic_button);
                        animationScore.setDuration(GameSettings.ANIMATION_CLOSE_BUTTON_DURATION);

                        Animation animationHighScore = AnimationUtils.loadAnimation(ClassicScore.this, R.anim.reverse_for_no_button);
                        animationHighScore.setDuration(GameSettings.ANIMATION_CLOSE_BUTTON_DURATION);

                        Animation animationPlayAgain = AnimationUtils.loadAnimation(ClassicScore.this, R.anim.reverse_for_settings_button);
                        animationPlayAgain.setDuration(GameSettings.ANIMATION_CLOSE_BUTTON_DURATION);

                        Animation animationMainMenu = AnimationUtils.loadAnimation(ClassicScore.this, R.anim.reverse_for_bomb_button);
                        animationMainMenu.setDuration(GameSettings.ANIMATION_CLOSE_BUTTON_DURATION);


                        scoreTextView.startAnimation(animationScore);
                        highScoreTextView.startAnimation(animationHighScore);
                        playAgainImageView.startAnimation(animationPlayAgain);
                        mainMenuImageView.startAnimation(animationMainMenu);

                        Handler myHandler = new Handler();
                        myHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intentMain = new Intent(ClassicScore.this, MainMenu.class);
                                intentMain.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intentMain);
                            }
                        }, GameSettings.START_NEW_ACTIVITY_DURATION);
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mainMenuImageView.startAnimation(animation);
    }

    private void initTitle() {
        mGameOverTitleLeftTextView = (TextView) findViewById(R.id.gameover_left);
        mGameOverTitleRightTextView = (TextView) findViewById(R.id.gameover_right);
        mGameOverTitleMiddleTextView = (TextView) findViewById(R.id.gameover_middle);

        Animation animationTitleLeft = AnimationUtils.loadAnimation(ClassicScore.this, R.anim.back_anim_for_title_left);
        animationTitleLeft.setDuration(GameSettings.ANIMATION_HIDE_TITLE_DURATION);
        mGameOverTitleLeftTextView.startAnimation(animationTitleLeft);

        Animation animationTitleRight = AnimationUtils.loadAnimation(ClassicScore.this, R.anim.back_anim_for_title_right);
        animationTitleRight.setDuration(GameSettings.ANIMATION_HIDE_TITLE_DURATION);
        mGameOverTitleRightTextView.startAnimation(animationTitleRight);

        Animation animationTitleMiddle = AnimationUtils.loadAnimation(ClassicScore.this, R.anim.back_anim_for_title_middle);
        animationTitleMiddle.setDuration(GameSettings.ANIMATION_HIDE_TITLE_DURATION);
        mGameOverTitleMiddleTextView.startAnimation(animationTitleMiddle);


    }
}

