package com.plenituz.ramdam;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;

import com.plenituz.ramdam.utils.Action;
import com.plenituz.ramdam.utils.PathAnimator3;
import com.plenituz.ramdam.utils.Point;

import java.util.ArrayList;

/**
 * Created by Plenituz on 29/06/2015.
 * J'ai codé un script qui transforme les .ai en View, et j'ai ajouté une option pour faire des
 * animation directement dans Adobe Illustrator
 */ 
public class PlayPause extends View {
 
    private static ArrayList<PathAnimator3> pathAnimators;
    private static ArrayList<Path> paths; 
    private static ArrayList<Paint> paints; 
    private static ArrayList<Paint> paints2; 
    private static ArrayList<ValueAnimator> listForward = new ArrayList<ValueAnimator>();
    private static ArrayList<ValueAnimator> listBackward = new ArrayList<ValueAnimator>();
 
 
    public PlayPause(Context context) {
        super(context); 
    }

    public PlayPause(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayPause(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override 
    protected void onDraw(Canvas canvas) { 
        if(pathAnimators == null){ 
            pathAnimators = new ArrayList<PathAnimator3>(); 
            paths = new ArrayList<Path>(); 
            paints = new ArrayList<Paint>(); 
            paints2 = new ArrayList<Paint>(); 
            Path path;
            PathAnimator3 pathAnimator;
			Paint paint;
			Paint p;

            path = new Path();
            pathAnimator = new PathAnimator3(path);
			pathAnimator.addAction(new Action(Action.SET_LAST_POINT, new Point()
			.ac( (int) (getWidth() * 0.32680410043353f), (int) (getHeight() * 0.24850987072433f))
			.ac( (int) (getWidth() * 0.32680410043353f), (int) (getHeight() * 0.19085305274566f))
			));
	pathAnimator.addAction(new Action(Action.CUBIC_TO, new Point()
			.ac((int) (getWidth() * 0.32680410043353f), (int) (getHeight() * 0.24850987072433f))
			.ac((int) (getWidth() * 0.32680410043353f), (int) (getHeight() * 0.19085305274566f))
			, new Point()
			.ac((int) (getWidth() * 0.22068054552023f), (int) (getHeight() * 0.19085305274566f))
			.ac((int) (getWidth() * 0.22068054552023f), (int) (getHeight() * 0.19085305274566f))
			, new Point()
			.ac((int) (getWidth() * 0.22068054552023f), (int) (getHeight() * 0.19085305274566f))
			.ac((int) (getWidth() * 0.22068054552023f), (int) (getHeight() * 0.19085305274566f))
		));

	pathAnimator.addAction(new Action(Action.CUBIC_TO, new Point()
			.ac((int) (getWidth() * 0.22068054552023f), (int) (getHeight() * 0.19085305274566f))
			.ac((int) (getWidth() * 0.22068054552023f), (int) (getHeight() * 0.19085305274566f))
			, new Point()
			.ac((int) (getWidth() * 0.22068054552023f), (int) (getHeight() * 0.78582234465318f))
			.ac((int) (getWidth() * 0.22068054552023f), (int) (getHeight() * 0.78582234465318f))
			, new Point()
			.ac((int) (getWidth() * 0.22068054552023f), (int) (getHeight() * 0.78582234465318f))
			.ac((int) (getWidth() * 0.22068054552023f), (int) (getHeight() * 0.78582234465318f))
		));

	pathAnimator.addAction(new Action(Action.CUBIC_TO, new Point()
			.ac((int) (getWidth() * 0.22068054552023f), (int) (getHeight() * 0.78582234465318f))
			.ac((int) (getWidth() * 0.22068054552023f), (int) (getHeight() * 0.78582234465318f))
			, new Point()
			.ac((int) (getWidth() * 0.32680410043353f), (int) (getHeight() * 0.72816552667451f))
			.ac((int) (getWidth() * 0.32680410043353f), (int) (getHeight() * 0.78582234465318f))
			, new Point()
			.ac((int) (getWidth() * 0.32680410043353f), (int) (getHeight() * 0.72816552667451f))
			.ac((int) (getWidth() * 0.32680410043353f), (int) (getHeight() * 0.78582234465318f))
		));

	pathAnimator.addAction(new Action(Action.CUBIC_TO, new Point()
			.ac((int) (getWidth() * 0.32680410043353f), (int) (getHeight() * 0.72816552667451f))
			.ac((int) (getWidth() * 0.32680410043353f), (int) (getHeight() * 0.78582234465318f))
			, new Point()
			.ac((int) (getWidth() * 0.48246785846954f), (int) (getHeight() * 0.64400145673543f))
			.ac((int) (getWidth() * 0.32680410043353f), (int) (getHeight() * 0.48833769869942f))
			, new Point()
			.ac((int) (getWidth() * 0.48246785846954f), (int) (getHeight() * 0.64400145673543f))
			.ac((int) (getWidth() * 0.32680410043353f), (int) (getHeight() * 0.48833769869942f))
		));

	pathAnimator.addAction(new Action(Action.CUBIC_TO, new Point()
			.ac((int) (getWidth() * 0.48246785846954f), (int) (getHeight() * 0.64400145673543f))
			.ac((int) (getWidth() * 0.32680410043353f), (int) (getHeight() * 0.48833769869942f))
			, new Point()
			.ac((int) (getWidth() * 0.6621093856806f), (int) (getHeight() * 0.54599451087533f))
			.ac((int) (getWidth() * 0.611328125f), (int) (getHeight() * 0.48833769869942f))
			, new Point()
			.ac((int) (getWidth() * 0.6621093856806f), (int) (getHeight() * 0.54599451087533f))
			.ac((int) (getWidth() * 0.611328125f), (int) (getHeight() * 0.48833769869942f))
		));

	pathAnimator.addAction(new Action(Action.CUBIC_TO, new Point()
			.ac((int) (getWidth() * 0.6621093856806f), (int) (getHeight() * 0.54599451087533f))
			.ac((int) (getWidth() * 0.611328125f), (int) (getHeight() * 0.48833769869942f))
			, new Point()
			.ac((int) (getWidth() * 0.6621093856806f), (int) (getHeight() * 0.54599451087533f))
			.ac((int) (getWidth() * 0.611328125f), (int) (getHeight() * 0.78582234465318f))
			, new Point()
			.ac((int) (getWidth() * 0.6621093856806f), (int) (getHeight() * 0.54599451087533f))
			.ac((int) (getWidth() * 0.611328125f), (int) (getHeight() * 0.78582234465318f))
		));

	pathAnimator.addAction(new Action(Action.CUBIC_TO, new Point()
			.ac((int) (getWidth() * 0.6621093856806f), (int) (getHeight() * 0.54599451087533f))
			.ac((int) (getWidth() * 0.611328125f), (int) (getHeight() * 0.78582234465318f))
			, new Point()
			.ac((int) (getWidth() * 0.7682329405939f), (int) (getHeight() * 0.48833769869942f))
			.ac((int) (getWidth() * 0.7174516799133f), (int) (getHeight() * 0.78582234465318f))
			, new Point()
			.ac((int) (getWidth() * 0.7682329405939f), (int) (getHeight() * 0.48833769869942f))
			.ac((int) (getWidth() * 0.7174516799133f), (int) (getHeight() * 0.78582234465318f))
		));

	pathAnimator.addAction(new Action(Action.CUBIC_TO, new Point()
			.ac((int) (getWidth() * 0.7682329405939f), (int) (getHeight() * 0.48833769869942f))
			.ac((int) (getWidth() * 0.7174516799133f), (int) (getHeight() * 0.78582234465318f))
			, new Point()
			.ac((int) (getWidth() * 0.7682329405939f), (int) (getHeight() * 0.48833769869942f))
			.ac((int) (getWidth() * 0.7174516799133f), (int) (getHeight() * 0.19085305274566f))
			, new Point()
			.ac((int) (getWidth() * 0.7682329405939f), (int) (getHeight() * 0.48833769869942f))
			.ac((int) (getWidth() * 0.7174516799133f), (int) (getHeight() * 0.19085305274566f))
		));

	pathAnimator.addAction(new Action(Action.CUBIC_TO, new Point()
			.ac((int) (getWidth() * 0.7682329405939f), (int) (getHeight() * 0.48833769869942f))
			.ac((int) (getWidth() * 0.7174516799133f), (int) (getHeight() * 0.19085305274566f))
			, new Point()
			.ac((int) (getWidth() * 0.6621093856806f), (int) (getHeight() * 0.43068088652351f))
			.ac((int) (getWidth() * 0.611328125f), (int) (getHeight() * 0.19085305274566f))
			, new Point()
			.ac((int) (getWidth() * 0.6621093856806f), (int) (getHeight() * 0.43068088652351f))
			.ac((int) (getWidth() * 0.611328125f), (int) (getHeight() * 0.19085305274566f))
		));

	pathAnimator.addAction(new Action(Action.CUBIC_TO, new Point()
			.ac((int) (getWidth() * 0.6621093856806f), (int) (getHeight() * 0.43068088652351f))
			.ac((int) (getWidth() * 0.611328125f), (int) (getHeight() * 0.19085305274566f))
			, new Point()
			.ac((int) (getWidth() * 0.6621093856806f), (int) (getHeight() * 0.43068088652351f))
			.ac((int) (getWidth() * 0.611328125f), (int) (getHeight() * 0.48833769869942f))
			, new Point()
			.ac((int) (getWidth() * 0.6621093856806f), (int) (getHeight() * 0.43068088652351f))
			.ac((int) (getWidth() * 0.611328125f), (int) (getHeight() * 0.48833769869942f))
		));

	pathAnimator.addAction(new Action(Action.CUBIC_TO, new Point()
			.ac((int) (getWidth() * 0.6621093856806f), (int) (getHeight() * 0.43068088652351f))
			.ac((int) (getWidth() * 0.611328125f), (int) (getHeight() * 0.48833769869942f))
			, new Point()
			.ac((int) (getWidth() * 0.4822798976165f), (int) (getHeight() * 0.33286190151645f))
			.ac((int) (getWidth() * 0.32680410043353f), (int) (getHeight() * 0.48833769869942f))
			, new Point()
			.ac((int) (getWidth() * 0.4822798976165f), (int) (getHeight() * 0.33286190151645f))
			.ac((int) (getWidth() * 0.32680410043353f), (int) (getHeight() * 0.48833769869942f))
		));
	pathAnimator.addAction(new Action(Action.CUBIC_TO, new Point()
			.ac((int) (getWidth() * 0.4822798976165f), (int) (getHeight() * 0.33286190151645f))
			.ac((int) (getWidth() * 0.32680410043353f), (int) (getHeight() * 0.48833769869942f))
			, new Point()
			.ac((int) (getWidth() * 0.32680410043353f), (int) (getHeight() * 0.24850987072433f))
			.ac((int) (getWidth() * 0.32680410043353f), (int) (getHeight() * 0.19085305274566f))
			, new Point()
			.ac((int) (getWidth() * 0.32680410043353f), (int) (getHeight() * 0.24850987072433f))
			.ac((int) (getWidth() * 0.32680410043353f), (int) (getHeight() * 0.19085305274566f))
		));
        pathAnimator.init();
        pathAnimators.add(pathAnimator);
        paths.add(path);
			paint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.rgb(0, 0, 0));
			paint.setStrokeWidth(dpToPx(4));
			paint.setStrokeCap(Paint.Cap.ROUND);
			paint.setStrokeJoin(Paint.Join.ROUND);
			paints2.add(null);
			paints.add(paint);

 
        } 
        for(int i = paths.size()-1; i >= 0; i--){
            canvas.drawPath(paths.get(i), paints.get(i));

        }
    } 

    public void animatePath(boolean backward, long duration){
        animatePath(backward, duration, null);
    }

	public void animatePath(boolean backward, long duration, ValueAnimator.AnimatorUpdateListener l){
		if((backward && listBackward.isEmpty()) || (!backward && listForward.isEmpty()) ){
            for(int i = 0; i < pathAnimators.size(); i++){
				ValueAnimator v = backward ? pathAnimators.get(i).getBackwardAnimator():pathAnimators.get(i).getForwardAnimator();
				if(i == 0)
					v.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
						@Override
						public void onAnimationUpdate(ValueAnimator valueAnimator) {
							invalidate();
						}
					});
				if(l != null)
					v.addUpdateListener(l);
				v.setDuration(duration);
				v.setInterpolator(new BounceInterpolator());
//        v.setStartDelay();
                if(backward)
                    listBackward.add(v);
                else
                    listForward.add(v);
			}
		}
        for(int i = 0; i < listForward.size(); i++){
            if(backward)
                listBackward.get(i).start();
            else
                listForward.get(i).start();
        }
	}

    public int dpToPx(int dps){
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dps * scale + 0.5f);
    }
} 
