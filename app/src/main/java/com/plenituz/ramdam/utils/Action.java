package com.plenituz.ramdam.utils;

/**
 * Created by Plenituz on 15/05/2015.
 */
public class Action {
    public static final int LINE_TO = 1;
    public static final int MOVE_TO = 2;
    public static final int CUBIC_TO = 3;
    public static final int SET_LAST_POINT = 4;
    public static final int CLOSE = 5;
    public static final int QUAD_TO = 6;

    Point[] points;
    int action;

    public Action(int action, Point... points){
        this.action = action;
        this.points = points;
    }
}
