package com.plenituz.ramdam;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.RelativeLayout;

import com.plenituz.ramdam.utils.P;

/**
 * Created by Plenituz on 20/01/2016 for Ramdam.
 */
public class PlayView extends RelativeLayout {
    private Paint paintCircle;
    private Paint paintBg;

    public static final int PAUSE_PLAY_MASK = 1;
    public static final int LOADING_MASK = 1 << 1;

    public static final int PAUSED = 1;
    public static final int PLAYING = 0;
    public static final int LOADING = 1 << 1;
    public static final int NOT_LOADING = 0;

    /**
     * 00 = not loading, playing
     * 01 = not loading, paused
     * 10 = loading, playing
     * 11 = loading, paused
     */
    private int state = 0;
    private float sweep = 360f;
    private float startAngle = 0f;
    private RectF r;
    private ValueAnimator sweepAnim;
    private ValueAnimator startAnim;
    PlayPause p;
    View v;


    public PlayView(Context context, int width, int height) {
        super(context);
        paintCircle = new Paint(Paint.DITHER_FLAG|Paint.ANTI_ALIAS_FLAG);
        paintCircle.setStyle(Paint.Style.STROKE);
        paintCircle.setColor(Color.BLACK);
        paintCircle.setStrokeJoin(Paint.Join.ROUND);
        paintCircle.setStrokeCap(Paint.Cap.ROUND);
        paintCircle.setStrokeWidth(P.poc(0.01f)[0]);

        paintBg = new Paint(Paint.DITHER_FLAG|Paint.ANTI_ALIAS_FLAG);
        paintBg.setStyle(Paint.Style.FILL);
        paintBg.setColor(Color.parseColor("#B74E4E"));

        sweepAnim = ValueAnimator.ofFloat(360f, 0f);
        sweepAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                sweep = (Float) animation.getAnimatedValue();
                v.invalidate();
            }
        });
        sweepAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        sweepAnim.setDuration(1000);
        sweepAnim.setRepeatMode(ValueAnimator.REVERSE);
        sweepAnim.setRepeatCount(ValueAnimator.INFINITE);

        startAnim = ValueAnimator.ofFloat(0f, 360f);
        startAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                startAngle = (Float) animation.getAnimatedValue();
            }
        });
        startAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        startAnim.setDuration(1000);
        startAnim.setRepeatCount(ValueAnimator.INFINITE);

        v = new View(context){
            @Override
            protected void onDraw(Canvas canvas) {
                if(r == null)
                    r = new RectF(paintCircle.getStrokeWidth()/2, paintCircle.getStrokeWidth()/2, getWidth()-paintCircle.getStrokeWidth()/2, getWidth()-paintCircle.getStrokeWidth()/2);
                canvas.drawCircle(getWidth()/2, getHeight()/2, getWidth()/2 - P.poc(0.03f)[0]/2, paintBg);
                canvas.drawArc(r, startAngle, sweep, false, paintCircle);
            }
        };
        v.setLayoutParams(new LayoutParams(width, height));
        addView(v);

        p = new PlayPause(getContext());
        p.setLayoutParams(new LayoutParams((int)(width*0.8f), (int) (height*0.8f)));
        ((RelativeLayout.LayoutParams)p.getLayoutParams()).addRule(CENTER_IN_PARENT);
        ((RelativeLayout.LayoutParams)p.getLayoutParams()).setMargins(P.poc(0.05f)[0], 0, 0, 0);
        addView(p);
    }

    public int getState() {
        return state;
    }

    /**
     *
     * @param state a combinaison of {@code(LOADING|NOT_LOADING)|(PAUSED|PLAYING)}
     */
    public void setState(int state){
        if((this.state & PAUSE_PLAY_MASK) == PAUSED){
            if((state & PAUSE_PLAY_MASK) == PLAYING) {
                p.animatePath(true, 400);
                paintCircle.setColor(Color.parseColor("#B74E4E"));
            }
        }else{
            if((state & PAUSE_PLAY_MASK) == PAUSED) {
                p.animatePath(false, 400);
                paintCircle.setColor(Color.BLACK);
                v.invalidate();
            }
        }

        if((this.state & LOADING_MASK) == LOADING){
            if((state & LOADING_MASK) == NOT_LOADING){
                sweepAnim.cancel();
                startAnim.cancel();
                ValueAnimator resetSweep = ValueAnimator.ofFloat(sweep, 360f);
                resetSweep.setInterpolator(new BounceInterpolator());
                resetSweep.setDuration(400);
                resetSweep.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        sweep = (Float) animation.getAnimatedValue();
                        v.invalidate();
                    }
                });
                resetSweep.start();

                ValueAnimator resetStart = ValueAnimator.ofFloat(startAngle, 0f);
                resetStart.setInterpolator(new BounceInterpolator());
                resetStart.setDuration(400);
                resetStart.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        startAngle = (Float) animation.getAnimatedValue();
                    }
                });
                resetStart.start();
            }
        }else{
            if((state & LOADING_MASK) == LOADING){
                sweepAnim.start();
                startAnim.start();
            }
        }

        this.state = state;
    }
}
