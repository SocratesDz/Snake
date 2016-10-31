package com.socratesdiaz.snake;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
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

public class BombSnake extends AppCompatActivity {

    private enum DIRECTION { UP, RIGHT, DOWN, LEFT }

    private boolean playMusic;
    private MediaPlayer musicPlayer;

    private RelativeLayout bombSnakeLayout;
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
    private ArrayList<ImageView> bombs;
    private boolean isCollide = false;

    private Handler mHandler;
    private ImageView head;

    private TextView textScore;

    private int speedX = 17;
    private int speedY = 17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bomb_snake);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        musicOnOff();
        bombSnakeLayout = (RelativeLayout) findViewById(R.id.bomb_snake_layout);
        bombSnakeLayout.setBackgroundResource(R.drawable.background_for_snake);
        bombSnakeLayout.setPaddingRelative(GameSettings.LAYOUT_PADDING, GameSettings.LAYOUT_PADDING,
                GameSettings.LAYOUT_PADDING, GameSettings.LAYOUT_PADDING);
        isInitialized = false;
        textScore = (TextView) findViewById(R.id.score);

    }

    private void musicOnOff() {
        SharedPreferences preferences = getApplicationContext()
                .getSharedPreferences(GameSettings.PREFS_NAME, Context.MODE_PRIVATE);
        playMusic = preferences.getBoolean(GameSettings.PLAY_MUSIC, true);
        musicPlayer = MediaPlayer.create(BombSnake.this, R.raw.music);
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

    private void clickLeft() {
        if(!clickLeft && !clickRight)
            changeDirection(DIRECTION.LEFT);
    }

    private void clickRight() {
        if(!clickLeft && !clickRight)
            changeDirection(DIRECTION.RIGHT);
    }

    private void clickUp() {
        if(!clickUp && !clickDown)
            changeDirection(DIRECTION.UP);
    }

    private void clickDown() {
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
                clickUp();
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickRight();
            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickLeft();
            }
        });

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickDown();
            }
        });

        SharedPreferences preferences = getApplicationContext()
                .getSharedPreferences(GameSettings.PREFS_NAME, Context.MODE_PRIVATE);
        useButtons = preferences.getBoolean(GameSettings.USE_BUTTON_CONTROLS, true);
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
        bombSnakeLayout = (RelativeLayout) findViewById(R.id.bomb_snake_layout);
        bombSnakeLayout.setBackgroundResource(R.drawable.background_for_snake);
        bombSnakeLayout.startAnimation(shake);
    }

    private void fadeAnim() {
        if(playerScore % GameSettings.POINTS_ANIMATION == 0) {
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            bombSnakeLayout = (RelativeLayout) findViewById(R.id.bomb_snake_layout);
            bombSnakeLayout.setBackgroundResource(R.drawable.background_for_snake_change);
            bombSnakeLayout.startAnimation(fadeIn);
            fadeIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Animation fadeOut = AnimationUtils.loadAnimation(BombSnake.this, R.anim.fade_out);
                    bombSnakeLayout = (RelativeLayout) findViewById(R.id.bomb_snake_layout);
                    bombSnakeLayout.setBackgroundResource(R.drawable.background_for_snake);
                    bombSnakeLayout.setAnimation(fadeOut);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    private void checkBitten() {
        ImageView snakeHead = parts.get(0);
        ImageView snakeTile;

        for (int i = 1; i < parts.size(); i++) {
            snakeTile = parts.get(i);
            if(snakeHead.getX() == snakeTile.getX() && snakeHead.getY() == snakeTile.getY()) {
                gameOver();
                break;
            }
        }
    }

    private void gameOver() {
        ImageView snakeHead = new ImageView(this);
        gameOver = true;
        SharedPreferences preferences = getApplicationContext()
                .getSharedPreferences(GameSettings.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(GameSettings.PLAYER_SCORE, playerScore);
        editor.apply();
        Intent intentScore = new Intent(BombSnake.this, BombScore.class);
        intentScore.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intentScore);
    }

    private void addTail() {
        //RelativeLayout snakeLayout = (RelativeLayout) findViewById(R.id.snake_layout);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.head);
        LinearLayout.LayoutParams layoutParams = new
                LinearLayout.LayoutParams(((screenWidth * 20) / 450), ((screenHeight * 30) / 450));
        imageView.setLayoutParams(layoutParams);
        bombSnakeLayout.addView(imageView);
        parts.add(imageView);
    }

    private void setNewPoint() {
        Random random = new Random();
        ImageView newPoint = new ImageView(BombSnake.this);
        float x = random.nextFloat() * (screenWidth - newPoint.getWidth());
        float y = random.nextFloat() * (screenHeight - newPoint.getHeight());
        newPoint.setImageResource(R.drawable.food);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ((screenWidth * 20) / 450), ((screenHeight * 30)/ 450));
        newPoint.setLayoutParams(layoutParams);
        newPoint.setX(x);
        newPoint.setY(y);
        isCollide = false;
        bombSnakeLayout.addView(newPoint);
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
            bombSnakeLayout.addView(foodItem);
            points.add(i, foodItem);
        }
    }

    private void setBombs() {
        Random rand = new Random();
        for (int i = 0; i < GameSettings.NUMBER_BOMBS; i++) {
            ImageView bomb = new ImageView(this);
            float x = rand.nextFloat() * (screenWidth - bomb.getWidth());
            float y = rand.nextFloat() * (screenHeight - bomb.getHeight());
            bomb.setImageResource(R.drawable.food_poison);

            RelativeLayout.LayoutParams layoutParamsBomb = new
                    RelativeLayout.LayoutParams((screenWidth*20)/450, (screenHeight*30)/450);

            bomb.setLayoutParams(layoutParamsBomb);
            bomb.setX(x);
            bomb.setY(y);
            bombSnakeLayout.addView(bomb);
            bombs.add(i, bomb);
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
                                            bombSnakeLayout.removeView(p);
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
                                for(int i = 0; i < bombs.size(); i++) {
                                    ImageView bomb = bombs.get(i);

                                    final float left2 = bomb.getX() - bomb.getWidth();
                                    final float top2 = bomb.getY() - bomb.getHeight();
                                    final float right2 = bomb.getX() + bomb.getWidth();
                                    final float bottom2 = bomb.getY() + bomb.getHeight();

                                    Rect rc1 = new Rect();
                                    rc1.set((int) left, (int) top, (int) right, (int) bottom);
                                    head.getHitRect(rc1);
                                    Rect rc2 = new Rect();
                                    rc2.set((int) left2, (int) top2, (int) right2, (int) bottom2);
                                    bomb.getHitRect(rc2);
                                    if(Rect.intersects(rc1, rc2)) {
                                        if(!isCollide) {
                                            isCollide = true;
                                            gameOver();
                                        }
                                    }
                                }
                                if(isGoingRight || clickRight) {
                                    for(int i = parts.size() - 1; i>=0; i--) {
                                        ImageView imageView = parts.get(i);
                                        if(i > 0) {
                                            ImageView imageView2 = parts.get(i - 1);
                                            imageView.setX(imageView2.getX());
                                            imageView.setY(imageView2.getY());
                                        } else {
                                            imageView.setX(imageView.getX() + speedX);
                                            if(imageView.getX() + imageView.getWidth() >= screenWidth) {
                                                imageView.setX(screenWidth - imageView.getWidth() / 2);
                                                gameOver();
                                            }
                                        }
                                    }
                                } else if(isGoingLeft || clickLeft) {
                                    for (int i = parts.size() - 1; i >= 0; i--) {
                                        ImageView imageView = parts.get(i);
                                        if(i > 0) {
                                            ImageView imageView2 = parts.get(i - 1);
                                            imageView.setX(imageView2.getX());
                                            imageView.setY(imageView2.getY());
                                        } else {
                                            imageView.setX(imageView.getX() - speedX);
                                            if(imageView.getX() <= 0) {
                                                imageView.setX(0);
                                                gameOver();
                                            }
                                        }
                                    }
                                } else if(isGoingDown || clickDown) {
                                    for (int i = parts.size()-1; i >= 0; i--) {
                                        ImageView imageView = parts.get(i);
                                        if(i > 0) {
                                            ImageView imageView2 = parts.get(i - 1);
                                            imageView.setX(imageView2.getX());
                                            imageView.setY(imageView2.getY());
                                        } else {
                                            imageView.setY(imageView.getY() + speedY);
                                            if((imageView.getY() + imageView.getHeight() >= screenHeight)) {
                                                imageView.setY(screenHeight - imageView.getHeight()/2);
                                                gameOver();
                                            }
                                        }
                                    }
                                } else if(isGoingUp || clickUp) {
                                    for (int i = parts.size()-1; i >= 0; i--) {
                                        ImageView imageView = parts.get(i);
                                        if(i > 0) {
                                            ImageView imageView2 = parts.get(i - 1);
                                            imageView.setX(imageView2.getX());
                                            imageView.setY(imageView2.getY());
                                        } else {
                                            imageView.setY(imageView.getY() - speedY);
                                            if(imageView.getY() <= 0) {
                                                imageView.setY(0);
                                                gameOver();
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }).start();
    }

    public class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;

            if(!useButtons) {
                try {
                    float diffX = e2.getX() - e1.getX();
                    float diffY = e2.getY() - e1.getY();
                    if(Math.abs(diffX) > Math.abs(diffY)) {
                        // Horizontal swipe
                        if(Math.abs(diffX) > GameSettings.SWIPE_THRESH_HOLD
                                && Math.abs(velocityX) > GameSettings.SWIPE_VELOCITY_THRESH_HOLD) {
                            if(diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                            result = true;
                        }
                    } else if(Math.abs(diffY) > GameSettings.SWIPE_THRESH_HOLD
                            && Math.abs(velocityY) > GameSettings.SWIPE_VELOCITY_THRESH_HOLD) {
                        // Vertical swipe
                        if(diffY > 0) {
                            onSwipeDown();
                        } else {
                            onSwipeUp();
                        }
                        result = true;

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return result;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(!isInitialized) {
            isInitialized = true;
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
            screenHeight = size.y;
            mHandler = new Handler();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mGestureDetector = new GestureDetector(null, new SwipeGestureDetector());
            head = new ImageView(this);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    ((screenWidth * 20) / 450), ((screenHeight * 30) / 450)
            );
            head.setImageResource(R.drawable.head);
            head.setLayoutParams(layoutParams);
            head.setX(screenWidth / 2 - head.getWidth());
            head.setY(screenHeight / 2 - head.getHeight());
            bombSnakeLayout.addView(head);

            parts = new ArrayList<>();
            points = new ArrayList<>();
            parts.add(0, head);

            layoutParams.setMargins(GameSettings.LAYOUT_MARGIN,
                    GameSettings.LAYOUT_MARGIN,
                    GameSettings.LAYOUT_MARGIN,
                    GameSettings.LAYOUT_MARGIN);

            setFoodPoints();

            bombs = new ArrayList<>();
            setBombs();
            buttonsDirectionInit();
            if(hasFocus) {
                isPaused = false;
                update();
            }
            super.onWindowFocusChanged(hasFocus);
        }
    }

    @Override
    public void onBackPressed() {

    }
}
