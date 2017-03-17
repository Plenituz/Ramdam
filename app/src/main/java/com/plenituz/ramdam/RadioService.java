package com.plenituz.ramdam;

import android.animation.ValueAnimator;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.plenituz.ramdam.utils.P;
import com.plenituz.ramdam.utils.SoundPlayer;
import com.plenituz.ramdam.utils.ToppedList;

import java.util.ArrayList;

/**
 * Created by Plenituz on 20/01/2016 for Ramdam.
 */
public class RadioService extends Service {

    public static final String ACTION_STOP = "STOP";
    public static final String ACTION_NEW_BUBBLE = "NEW";

    public static boolean isActive = false;
    private static WindowManager wm;
    private static WindowManager.LayoutParams paramsWindow;
    private static PlayView p;
    private static MediaPlayer ramdamMp;
    private static ValueAnimator movingThread;
    private static ToppedList<Integer>[] posLog = new ToppedList[2];
    private static ToppedList<Long> posTimes = new ToppedList<>(2);
    private static ArrayList<View> addedViews = new ArrayList<View>();

    private static Point vectorDirection = new Point(0, 0);
    private static float speed;
    private static final float airResistance = 0.2f;
   private static boolean hasSpeedup = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null || intent.getAction() == null)
            return START_NOT_STICKY;
        switch (intent.getAction()){
            case ACTION_NEW_BUBBLE:
                if(!isActive){
                    P.init(getApplicationContext());
                    isActive = true;
                    int x = intent.getIntExtra("posX", 0);
                    int y = intent.getIntExtra("posY", 0);

                    paramsWindow = new WindowManager.LayoutParams(
                            P.poc(0.2f)[0],
                            P.poc(0.2f)[0],
                            WindowManager.LayoutParams.TYPE_PHONE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH|
                                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM|
                                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                            PixelFormat.TRANSPARENT);
                    paramsWindow.gravity = Gravity.TOP | Gravity.LEFT;
                    paramsWindow.x = x;
                    paramsWindow.y = y;
                    wm = (WindowManager) getBaseContext().getSystemService(WINDOW_SERVICE);
                    p = new PlayView(getBaseContext(), P.poc(0.2f)[0], P.poc(0.2f)[0]);
                    p.setOnClickListener(getPlayButtonClickListener());
                    p.setOnTouchListener(getPlayButtonTouchListener());
                    p.setState(PlayView.NOT_LOADING | PlayView.PAUSED);
                    addedViews.add(p);
                    wm.addView(p, paramsWindow);

                    Intent stopIntent = new Intent(this, RadioService.class);
                    stopIntent.setAction(ACTION_STOP);
                    PendingIntent pendingStopIntent = PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_ONE_SHOT);

                    Notification.Builder builder = new Notification.Builder(getBaseContext());
                    builder.setContentTitle("Ramdam")
                            .setContentText("Cette notification garde la radio active")
                            .setSmallIcon(R.drawable.ramdam_logo)
                            .setContentIntent(null)
                            .setOngoing(true)
                            .addAction(android.R.drawable.ic_delete, "DETRUIRE LA BULLE", pendingStopIntent);
                    startForeground(1, builder.build());

                    posLog[0] = new ToppedList<>(2);
                    posLog[1] = new ToppedList<>(2);
                    if(movingThread != null)
                        movingThread.end();
                    movingThread = ValueAnimator.ofInt(0, 10);
                    movingThread.setRepeatCount(ValueAnimator.INFINITE);
                    movingThread.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            onTick();
                            //j'utilise l'animation comme un thread
                            //parceque j'avais la flemme d'ecrire une AsyncTask
                        }
                    });
                    movingThread.start();
                    hasSpeedup = true;
                    MainActivity.handler.sendEmptyMessage(MainActivity.HIDE_PLAY);
                    if(p != null)
                        p.performClick();
                }
                break;
            case ACTION_STOP:
                for(View v : addedViews){
                    try{
                        wm.removeView(v);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                SoundPlayer.stopSounds();
                stopForeground(true);
                isActive = false;
                if(ramdamMp != null){
                    ramdamMp.release();
                    ramdamMp = null;
                }
                MainActivity.handler.sendEmptyMessage(MainActivity.SHOW_PLAY);
                break;
        }
        return START_STICKY;
    }

    void onTick(){
        posLog[0].add(paramsWindow.x);
        posLog[1].add(paramsWindow.y);
        posTimes.add(System.currentTimeMillis());
        if (paramsWindow.x > P.poc(0.8f)[0] || paramsWindow.x < 0) {
            vectorDirection.x *= -1;
        }
        if (paramsWindow.y > P.poc(0.8f)[1] || paramsWindow.y < 0) {
            vectorDirection.y *= -1;
        }

        if (speed > 0) {
            //int[] nPos = P.getPointOnLineAtDistance(paramsWindow.x, paramsWindow.y, slope, speed);
            Point nPos = P.getPointOnVector(new Point(paramsWindow.x, paramsWindow.y), vectorDirection, speed);
            speed -= airResistance;
            paramsWindow.x = nPos.x;
            paramsWindow.y = nPos.y;
            try {//ya tjrs des pb avec ca
                wm.updateViewLayout(p, paramsWindow);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (hasSpeedup && speed <= 0) {
            hasSpeedup = false;
            Path edgePath = new Path();
            edgePath.addRect(-P.poc(0.05f)[0], 0, P.poc(0.85f)[0], P.poc(0.85f)[1], Path.Direction.CCW);
            Point edge = P.getClosestPointOnPath(new Point(paramsWindow.x, paramsWindow.y), edgePath, 0.01f);
            ValueAnimator v = ValueAnimator.ofInt(paramsWindow.x, edge.x);
            v.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    paramsWindow.x = (int) animation.getAnimatedValue();
                }
            });
            v.setDuration(500);
            v.setInterpolator(new OvershootInterpolator());

            ValueAnimator v2 = ValueAnimator.ofInt(paramsWindow.y, edge.y);
            v2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    paramsWindow.y = (int) animation.getAnimatedValue();
                    try {
                        wm.updateViewLayout(p, paramsWindow);
                    } catch (Exception e) {
                    }

                }
            });
            v2.setDuration(500);
            v2.setInterpolator(new OvershootInterpolator());
            v.start();
            v2.start();
        }
    }

    View.OnTouchListener getPlayButtonTouchListener(){
        return new View.OnTouchListener() {
            int[] downPos = {0, 0};
            boolean hasMoved = false;
            long downTime;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        downPos = new int[]{(int) event.getRawX(), (int) event.getRawY()};
                        hasMoved = false;
                        speed = 0;
                        hasSpeedup = false;
                        downTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(P.getDistance(paramsWindow.x, paramsWindow.y, (int)event.getRawX(), (int)event.getRawY()) > P.poc(0.11f)[0]){
                            paramsWindow.x = (int) event.getRawX() - P.poc(0.2f)[0]/2;
                            paramsWindow.y = (int) event.getRawY() - P.poc(0.2f)[0];
                            try{
                                wm.updateViewLayout(p, paramsWindow);
                            }catch (Exception e){}
                            hasMoved = true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if(System.currentTimeMillis() - downTime < 100){
                            p.performClick();
                        }
                        if(event.getRawX() >= P.poc(0.9f)[0] || event.getRawX() <= P.poc(0.1f)[0] || event.getRawY() <= P.poc(0.1f)[1] || event.getRawY() >= P.poc(0.9f)[1]){
                            hasSpeedup = true;
                            speed = 0;
                            break;
                        }
                        if(hasMoved && (posTimes.get(1) - posTimes.get(0) > 0)){
                            vectorDirection = new Point(posLog[0].get(1) - posLog[0].get(0), posLog[1].get(1) - posLog[1].get(0));
                            speed = P.getDistance(posLog[0].get(0), posLog[1].get(0), posLog[0].get(1), posLog[1].get(1))/(posTimes.get(1) - posTimes.get(0));
                            speed *= 5;
                            hasSpeedup = true;
                        }
                        break;
                }
                return true;
            }
        };
    }

    View.OnClickListener getPlayButtonClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ramdamMp != null && ramdamMp.isPlaying()){
                    SoundPlayer.stopSounds();
                    ramdamMp = null;
                    p.setState(PlayView.NOT_LOADING|PlayView.PAUSED);
                }else{
                    if((p.getState() & PlayView.LOADING_MASK) == PlayView.NOT_LOADING){
                        p.setState(PlayView.LOADING|PlayView.PAUSED);
                        SoundPlayer.playSoundThreaded(getBaseContext(), MainActivity.RADIO_URL, null, getOnError(), getOnSoundCreated());
                    }
                }
            }
        };
    }

    SoundPlayer.onSoundCreatedListener getOnSoundCreated(){
        return new SoundPlayer.onSoundCreatedListener() {
            @Override
            public void onSoundCreated(MediaPlayer mp) {
                ramdamMp = mp;
                p.setState(PlayView.NOT_LOADING|PlayView.PLAYING);
            }
        };
    }

    Runnable getOnError(){
        return new Runnable() {
            @Override
            public void run() {
                SoundPlayer.stopSounds();
                p.setState(PlayView.NOT_LOADING | PlayView.PAUSED);
                Toast.makeText(getBaseContext(), getString(R.string.errorText), Toast.LENGTH_LONG).show();
                Log.v("eeror", "erriorr");
            }
        };
    }


}
