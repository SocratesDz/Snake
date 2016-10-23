package com.socratesdiaz.snake;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Handler;


public class ClassicSnake extends AppCompatActivity {

    private enum DIRECTION { UP, RIGHT, DOWN, LEFT }

    private boolean playMusic;
    private MediaPlayer musicPlayer;

    private RelativeLayout classicSnakeLayout;
    private boolean isInitialized;

    private GestureDetector mGestureDetector;
    private boolean isPaused;

    private boolean isGoingLeft = false;
    private boolean isGoingRight = false;
    private boolean isGoingUp = false;
    private boolean isGoingDown = false;

    private boolean clickLeft = false;
    private boolean clickRight = false;
    private boolean clickUp = false;
    private boolean clickDown = false;

    private ImageView btnLeft, btnRight, btnUp, btnDown;

    private boolean useButtons;

    private int playerScore;

    private boolean gameOver = false;

    private ArrayList<ImageView> parts;

    private int screenHeight, screenWidth;

    private ArrayList<ImageView> points;
    private boolean isCollide = false;

    private Handler mHandler;
    private ImageView head;

    private TextView textScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic_snake);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        musicOnOff();
        classicSnakeLayout = (RelativeLayout) findViewById(R.id.classic_snake_layout);
        classicSnakeLayout.setBackgroundResource(R.drawable.background_for_snake);
        classicSnakeLayout.setPaddingRelative(GameSettings.LAYOUT_PADDING, GameSettings.LAYOUT_PADDING,
                GameSettings.LAYOUT_PADDING, GameSettings.LAYOUT_PADDING);
        isInitialized = false;
        textScore = (TextView) findViewById(R.id.score);

    }

    private void musicOnOff() {
        SharedPreferences preferences = getApplicationContext()
                .getSharedPreferences(GameSettings.PREFS_NAME, Context.MODE_PRIVATE);
        playMusic = preferences.getBoolean("PlayMusic", true);
        musicPlayer = MediaPlayer.create(ClassicSnake.this, R.raw.music);
        if(playMusic) {
            musicPlayer.setLooping(true);
            musicPlayer.start();
        } else {
            musicPlayer.stop();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mGestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
        musicPlayer.release();
    }

    private void onSwipeLeft() {
        if(!isGoingLeft && !isGoingRight)
            changeDirection(DIRECTION.LEFT);
    }

    private void onSwipeRight() {
        if(!isGoingLeft && !isGoingRight)
            changeDirection(DIRECTION.RIGHT);
    }

    private void onSwipeUp() {
        if(!isGoingUp && !isGoingDown)
            changeDirection(DIRECTION.UP);
    }

    private void onSwipeDown() {
        if(!isGoingUp && !isGoingDown)
            changeDirection(DIRECTION.DOWN);
    }

    private void onClickLeft() {
        if(!clickLeft && !clickRight)
            changeDirection(DIRECTION.LEFT);
    }

    private void onClickRight() {
        if(!clickLeft && !clickRight)
            changeDirection(DIRECTION.RIGHT);
    }

    private void onClickUp() {
        if(!clickUp && !clickDown)
            changeDirection(DIRECTION.UP);
    }

    private void onClickDown() {
        if(!clickUp && !clickDown)
            changeDirection(DIRECTION.DOWN);
    }

    private void changeDirection(DIRECTION direction) {
        switch (direction) {
            case UP:
                isGoingUp = clickUp = true;
                isGoingDown = isGoingLeft = isGoingRight =
                        clickDown = clickLeft = clickRight = false;
                break;
            case RIGHT:
                isGoingRight = clickRight = true;
                isGoingUp = isGoingDown = isGoingLeft =
                        clickUp = clickDown = clickLeft = false;
                break;
            case DOWN:
                isGoingDown = clickDown = true;
                isGoingRight = isGoingUp = isGoingLeft =
                        clickRight = clickUp = clickLeft = false;
                break;
            case LEFT:
                isGoingLeft = clickLeft = true;
                isGoingUp = isGoingDown = isGoingRight =
                        clickUp = clickDown = clickRight = false;
                break;
            default:
                break;
        }
    }

    private void buttonsDirectionInit() {
        btnUp = (ImageView) findViewById(R.id.btn_up);
        btnDown = (ImageView) findViewById(R.id.btn_down);
        btnLeft = (ImageView) findViewById(R.id.btn_left);
        btnRight = (ImageView) findViewById(R.id.btn_right);

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUp();
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRight();
            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickLeft();
            }
        });

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDown();
            }
        });

        SharedPreferences preferences = getApplicationContext()
                .getSharedPreferences(GameSettings.PREFS_NAME, Context.MODE_PRIVATE);
        useButtons = preferences.getBoolean("UseButtonControls", true);
        if(useButtons) {
            btnUp.setVisibility(View.VISIBLE);
            btnDown.setVisibility(View.VISIBLE);
            btnLeft.setVisibility(View.VISIBLE);
            btnRight.setVisibility(View.VISIBLE);
        } else {
            btnUp.setVisibility(View.INVISIBLE);
            btnDown.setVisibility(View.INVISIBLE);
            btnLeft.setVisibility(View.INVISIBLE);
            btnRight.setVisibility(View.INVISIBLE);
        }
    }

    private void shake() {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        shake.setDuration(GameSettings.SHAKE_DURATION);
        classicSnakeLayout = (RelativeLayout) findViewById(R.id.classic_snake_layout);
        classicSnakeLayout.setBackgroundResource(R.drawable.background_for_snake);
        classicSnakeLayout.startAnimation(shake);
    }

    private void fadeAnim() {
        if(playerScore % GameSettings.POINTS_ANIMATION == 0) {
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            classicSnakeLayout = (RelativeLayout) findViewById(R.id.classic_snake_layout);
            classicSnakeLayout.setBackgroundResource(R.drawable.background_for_snake_change);
            classicSnakeLayout.startAnimation(fadeIn);
            fadeIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Animation fadeOut = AnimationUtils.loadAnimation(ClassicSnake.this, R.anim.fade_out);
                    classicSnakeLayout = (RelativeLayout) findViewById(R.id.classic_snake_layout);
                    classicSnakeLayout.setBackgroundResource(R.drawable.background_for_snake);
                    classicSnakeLayout.setAnimation(fadeOut);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    private void collide() {
        ImageView snakeHead = new ImageView(this);
        gameOver = true;
        SharedPreferences preferences = getApplicationContext()
                 .getSharedPreferences(GameSettings.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("Score", playerScore);
        editor.apply();
        Intent intentScore = new Intent(ClassicSnake.this, ClassicScore.class);
        intentScore.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intentScore);
    }

    private void checkBitten() {
        ImageView snakeHead = parts.get(0);
        ImageView snakeTile = new ImageView(this);

        for (int i = 1; i < parts.size(); i++) {
            snakeTile = parts.get(i);
            if(snakeHead.getX() == snakeTile.getX() && snakeHead.getY() == snakeTile.getY()) {
                collide();
                break;
            }
        }
    }

    private void addTail() {
        RelativeLayout snakeLayout = (RelativeLayout) findViewById(R.id.snake_layout);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.head);
        LinearLayout.LayoutParams layoutParams = new
                LinearLayout.LayoutParams(((screenWidth * 20) / 450), ((screenHeight * 30) / 450));
        imageView.setLayoutParams(layoutParams);
        snakeLayout.addView(imageView);
        parts.add(imageView);
    }

    private void setNewPoint() {
        Random random = new Random();
        ImageView newPoint = new ImageView(ClassicSnake.this);
        float x = random.nextFloat() * (screenWidth - newPoint.getWidth());
        float y = random.nextFloat() * (screenHeight - newPoint.getHeight());
        newPoint.setImageResource(R.drawable.food);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ((screenWidth * 20) / 450), ((screenHeight * 30)/ 450));
        newPoint.setLayoutParams(layoutParams);
        newPoint.setX(x);
        newPoint.setY(y);
        isCollide = false;
        classicSnakeLayout.addView(newPoint);
        points.add(points.size(), newPoint);
    }

    private void setFoodPoints() {
        for (int i = 0; i < GameSettings.FOOD_POINTS; i++) {
            Random random = new Random();

            ImageView foodItem = new ImageView(this);
            float x = random.nextFloat() * (screenWidth - foodItem.getWidth());
            float y = random.nextFloat() * (screenHeight - foodItem.getHeight());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    ((screenWidth * 20) / 450), ((screenHeight * 30) / 450));
            foodItem.setImageResource(R.drawable.food);
            foodItem.setLayoutParams(layoutParams);
            foodItem.setX(x);
            foodItem.setY(y);
            classicSnakeLayout.addView(foodItem);
            points.add(i, foodItem);
        }
    }

    private void update() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!gameOver && !isPaused) {
                    try {
                        Thread.sleep(GameSettings.GAME_THREAD);
                        mHandler.post(new Runnable(){
                            @Override
                            public void run() {
                                float left = head.getX() - head.getWidth();
                                float top = head.getY() - head.getHeight();
                                float right = head.getX() + head.getWidth();
                                float bottom = head.getY() + head.getHeight();

                                for (int i= 0; i < points.size(); i++) {
                                    if(!isCollide) {
                                        ImageView p = points.get(i);
                                        float leftOther = p.getX() - p.getWidth();
                                        float topOther = p.getY() - p.getHeight();
                                        float rightOther = p.getX() + p.getWidth();
                                        float bottomOther = p.getY() + p.getHeight();

                                        Rect playerRect = new Rect();
                                        playerRect.set((int) left, (int) top, (int) right, (int) bottom);

                                        Rect foodRect = new Rect();
                                        foodRect.set((int)leftOther, (int)topOther, (int)rightOther, (int)bottomOther);

                                        p.getHitRect(foodRect);
                                        if(Rect.intersects(playerRect, foodRect)) {
                                            classicSnakeLayout.removeView(p);
                                            points.remove(i);
                                            playerScore++;
                                            isCollide = true;
                                            textScore.setText("Score: " + playerScore);
                                            setNewPoint();
                                            addTail();
                                            shake();
                                            fadeAnim();
                                        }
                                        checkBitten();
                                    }
                                }
                                isCollide = false;
                            }
                        });
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }).start();
    }

}
