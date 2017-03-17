package com.plenituz.ramdam.utils;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Path;

import java.util.ArrayList;

/**
 * Created by Plenituz on 15/05/2015.
 */
public class PathAnimator3 {
    private Path mPath;
    private ArrayList<Action> actions = new ArrayList<Action>();

    public PathAnimator3(Path path){
        mPath = path;
    }

    public void addAction(Action action){
        actions.add(action);
    }

    public void init() {
        mPath.reset();
        for(int i = 0; i < actions.size(); i++){
            Action a = actions.get(i);
            switch (a.action){
                case Action.SET_LAST_POINT:
                    mPath.setLastPoint(a.points[0].getCoords(0)[0], a.points[0].getCoords(0)[1]);
                    break;
                case Action.CLOSE:
                    mPath.close();
                    break;
                case Action.LINE_TO:
                    mPath.lineTo(a.points[0].getCoords(0)[0], a.points[0].getCoords(0)[1]);
                    break;
                case Action.MOVE_TO:
                    mPath.moveTo(a.points[0].getCoords(0)[0], a.points[0].getCoords(0)[1]);
                    break;
                case Action.CUBIC_TO:
                    mPath.cubicTo(a.points[0].getCoords(0)[0], a.points[0].getCoords(0)[1], a.points[1].getCoords(0)[0], a.points[1].getCoords(0)[1], a.points[2].getCoords(0)[0], a.points[2].getCoords(0)[1]);
                    break;
                case Action.QUAD_TO:
                    mPath.quadTo(a.points[0].getCoords(0)[0], a.points[0].getCoords(0)[1], a.points[1].getCoords(0)[0], a.points[1].getCoords(0)[1]);
                    break;
            }
        }
    }


    public ValueAnimator getForwardAnimator(){
        ArrayList<PropertyValuesHolder> list = new ArrayList<PropertyValuesHolder>();
        for(int i = 0; i < actions.size(); i++){
            Action a = actions.get(i);
            for(int k = 0; k < a.points.length; k++){
                Point p = a.points[k];
                int[] coordsX = new int[p.coords.size()];
                int[] coordsY = new int[p.coords.size()];
                for(int j = 0; j < p.coords.size(); j++){
                    coordsX[j] = p.coords.get(j)[0];
                    coordsY[j] = p.coords.get(j)[1];
                }
                PropertyValuesHolder propertyValuesHolderX = PropertyValuesHolder.ofInt(a.action + ":a" + i + "p" + k + "x", coordsX);
                PropertyValuesHolder propertyValuesHolderY = PropertyValuesHolder.ofInt(a.action + ":a" + i + "p" + k + "y", coordsY);
                list.add(propertyValuesHolderX);
                list.add(propertyValuesHolderY);
            }
        }
        PropertyValuesHolder[] all = new PropertyValuesHolder[list.size()];
        for(int i = 0; i < all.length; i++){
            all[i] = list.get(i);
        }

        ValueAnimator valueAnimator = ValueAnimator.ofPropertyValuesHolder(all);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mPath.rewind();
                for (int i = 0; i < actions.size(); i++) {
                    Action a = actions.get(i);
                    switch (a.action) {
                        case Action.SET_LAST_POINT:
                            mPath.setLastPoint((Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p0x"), (Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p0y"));
                            break;
                        case Action.CLOSE:
                            mPath.close();
                            break;
                        case Action.LINE_TO:
                            mPath.lineTo((Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p0x"), (Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p0y"));
                            break;
                        case Action.MOVE_TO:
                            mPath.moveTo((Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p0x"), (Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p0y"));
                            break;
                        case Action.CUBIC_TO:
                            mPath.cubicTo(
                                    (Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p0x"),
                                    (Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p0y"),
                                    (Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p1x"),
                                    (Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p1y"),
                                    (Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p2x"),
                                    (Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p2y")
                            );
                            break;
                        case Action.QUAD_TO:
                            mPath.quadTo(
                                    (Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p0x"),
                                    (Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p0y"),
                                    (Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p1x"),
                                    (Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p1y")
                            );
                            break;
                    }
                }
            }
        });
        return valueAnimator;
    }

    public ValueAnimator getBackwardAnimator(){
        ArrayList<PropertyValuesHolder> list = new ArrayList<PropertyValuesHolder>();
        for(int i = 0; i < actions.size(); i++){
            Action a = actions.get(i);
            for(int k = 0; k < a.points.length; k++){
                Point p = a.points[k];
                ArrayList<int[]> tmp = new ArrayList<int[]>();
                ArrayList<int[]> backup = p.coords;
                for(int[] t:p.coords){
                    tmp.add(0, t);
                }
                p.coords = tmp;

                int[] coordsX = new int[p.coords.size()];
                int[] coordsY = new int[p.coords.size()];
                for(int j = 0; j < p.coords.size(); j++){
                    coordsX[j] = p.coords.get(j)[0];
                    coordsY[j] = p.coords.get(j)[1];
                }
                PropertyValuesHolder propertyValuesHolderX = PropertyValuesHolder.ofInt(a.action + ":a" + i + "p" + k + "x", coordsX);
                PropertyValuesHolder propertyValuesHolderY = PropertyValuesHolder.ofInt(a.action + ":a" + i + "p" + k + "y", coordsY);
                list.add(propertyValuesHolderX);
                list.add(propertyValuesHolderY);
                p.coords = backup;
            }
        }
        PropertyValuesHolder[] all = new PropertyValuesHolder[list.size()];
        for(int i = 0; i < all.length; i++){
            all[i] = list.get(i);
        }

        ValueAnimator valueAnimator = ValueAnimator.ofPropertyValuesHolder(all);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mPath.rewind();
                for (int i = 0; i < actions.size(); i++) {
                    Action a = actions.get(i);
                    switch (a.action) {
                        case Action.SET_LAST_POINT:
                            mPath.setLastPoint((Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p0x"), (Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p0y"));
                            break;
                        case Action.CLOSE:
                            mPath.close();
                            break;
                        case Action.LINE_TO:
                            mPath.lineTo((Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p0x"), (Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p0y"));
                            break;
                        case Action.MOVE_TO:
                            mPath.moveTo((Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p0x"), (Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p0y"));
                            break;
                        case Action.CUBIC_TO:
                            mPath.cubicTo(
                                    (Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p0x"),
                                    (Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p0y"),
                                    (Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p1x"),
                                    (Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p1y"),
                                    (Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p2x"),
                                    (Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p2y")
                            );
                            break;
                        case Action.QUAD_TO:
                            mPath.quadTo(
                                    (Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p0x"),
                                    (Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p0y"),
                                    (Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p1x"),
                                    (Integer) valueAnimator.getAnimatedValue(a.action + ":a" + i + "p1y")
                            );
                            break;
                    }
                }
            }
        });
        return valueAnimator;
    }
}
