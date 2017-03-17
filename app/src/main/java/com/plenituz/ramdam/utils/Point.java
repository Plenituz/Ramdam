package com.plenituz.ramdam.utils;

import java.util.ArrayList;

/**
 * Created by Plenituz on 14/05/2015.
 */
public class Point{
    ArrayList<int[]> coords = new ArrayList<int[]>();

    public Point addCoord(int x, int y){
        coords.add(new int[]{x, y});
        return this;
    }

    public Point ac(int x, int y){
        return addCoord(x, y);
    }

    public int[] getCoords(int i){
        return coords.get(i);
    }


}
