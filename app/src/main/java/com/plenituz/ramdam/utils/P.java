package com.plenituz.ramdam.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Plenituz's utility class
 */
public abstract class P {
	/**
	 * 
	 */
	static DisplayMetrics displayMetrics;
	private static int maxWidth;
	private static int maxHeight;
	private static Context context;
	
	static public void init(Context contextt){
		context = contextt;
		displayMetrics = context.getResources().getDisplayMetrics();
		
		Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		maxWidth = size.x;
		maxHeight = size.y;
	}

    /**
     * not really effective way to measure things, use poc
     * @param dp
     * @return
     */
	static public int dp(int dp) {
	    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));   
	    return px;
	}

    static public int getDistance(int x1, int y1, int x2, int y2){
        return (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
    static public double getDistance(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

	/**
	 * Give the lenght in pixel in relation to the screen  (percent of screen)
	 * 
	 * poc()[0] is relative to the width of the screen
	 * poc()[1] is relative to the height of the screen
	 * /!\ 1.0f is not counting the on screen buttons, 1.1f does
	 */
	static public int[] poc(float percent){
		int[] p = new int[2];
        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            p[0] = (int) (maxWidth*percent);
            p[1] = (int) (maxHeight*percent);
        }else{
            p[1] = (int) (maxWidth*percent);
            p[0] = (int) (maxHeight*percent);
        }
		return p;
	}

    /**
     *
     * @param xa anchor
     * @param ya anchor
     * @param angle
     * @param xPoint point to check
     * @param yPoint point ot check
     * @return
     */
    public static float getDistanceFromLine(int xa, int ya, float angle, int xPoint, int yPoint){
        double tan = Math.tan(Math.toRadians(angle));
        float r = (float) (Math.abs((-(tan)*xPoint)+yPoint+(tan*xa)-ya) / Math.sqrt((-tan)*(-tan) + 1));
        return r;
    }

    // SI Y AUGMENTE  DE h ALORS X AUGMENTE DE h/A (A = SLOPE)
    public static double getSlope(int xa, int ya, int xb, int yb){
        return (double) (yb - ya)/(xb - xa);
    }

    public static void copyToClipBoard(String str){
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(str);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Message",str);
            clipboard.setPrimaryClip(clip);
        }
    }
    public static String pasteFromClipBoard(){
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            return (String) clipboard.getText();
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            try{
                return clipboard.getPrimaryClip().getItemAt(0).getText().toString();
            }catch (NullPointerException e){
                return "";
            }
        }
    }

    /**
     *
     * @param x1 point 1
     * @param y1 point 1
     * @param x2 point 2
     * @param y2 point 2
     * @param r distance
     * @return les deux points sur la droite a cette distance
     */
    public static int[] getPointOnLineAtDistance(int x1, int y1, int x2, int y2, int r){
        double a = P.getSlope(x1, y1, x2, y2);
        double[] inters = getIntersectBetweenLineAndCircle(x1, y1, a, (int) (y1 - (a*x1)), r);

        return new int[]{(int) inters[0], (int) inters[1], (int) inters[2], (int) inters[3]};
    }

    public static int[] getPointOnLineAtDistance(int x, int y, float a, float r){
        double[] inters = getIntersectBetweenLineAndCircle(x, y, a, (int) (y - (a * x)), r);
        return new int[]{(int) inters[0], (int) inters[1], (int) inters[2], (int) inters[3]};
    }

    /**
     *
     * @param x1 centre du cercle
     * @param y1 centre du cercle
     * @param a pente de la droite
     * @param b
     * @param r rayon du cercle
     * @return les points d'intersections [0,1] = x1, y1 [2, 3] = x2, y2
     */
    public static double[] getIntersectBetweenLineAndCircle(int x1, int y1, double a, int b, float r){
        double X1 = (-((2*a*b) - (2*x1) - (2*a*y1)) + Math.sqrt(Math.pow(((2 * a * b) - (2 * x1) - (2 * a * y1)), 2) - (4 * ((a * a) + 1) * ((b * b) + (x1 * x1) + (y1 * y1) - (2 * b * y1) - (r * r)))))/(2*((a*a) + 1));
        double X2 = (-((2*a*b) - (2*x1) - (2*a*y1)) - Math.sqrt(Math.pow(((2 * a * b) - (2 * x1) - (2 * a * y1)), 2) - (4 * ((a * a) + 1) * ((b * b) + (x1 * x1) + (y1 * y1) - (2 * b * y1) - (r * r)))))/(2*((a*a) + 1));

        double Y1 = a*X1 + b;
        double Y2 = a*X2 + b;
        return new double[]{ X1,  Y1,  X2,  Y2};
    }

    public static int[] getClosestPoint(int[] pointsToDifferentiate, int[] anchor){
        return P.getDistance(pointsToDifferentiate[0], pointsToDifferentiate[1], anchor[0], anchor[1]) >  P.getDistance(pointsToDifferentiate[2], pointsToDifferentiate[3], anchor[0], anchor[1]) ?
                new int[]{pointsToDifferentiate[2], pointsToDifferentiate[3]}: new int[]{pointsToDifferentiate[0], pointsToDifferentiate[1]};
    }

    /**
     *
     * @param anchor
     * @param pointToRotate
     * @param angle in radian
     * @return
     */
    public static Point rotate(Point anchor, Point pointToRotate, double angle){
        ComplexNumber vector = new ComplexNumber(pointToRotate.x - anchor.x, pointToRotate.y - anchor.y, ComplexNumber.NUMERICAL);
        ComplexNumber convers = new ComplexNumber(vector.getModulus(), vector.getArgument() + angle, ComplexNumber.GEOMETRICAL);
        return new Point((int) (convers.getRealPart() + anchor.x), (int) (convers.getImaginaryPart() + anchor.y));
    }

    /**
     *
     * @param p1
     * @param p2
     * @param p3
     * @return angle formed by the 3 points, in radians
     */
    public static double getAngle(Point p1, Point p2, Point p3){
        double a = getDistance((double) p1.x, (double) p1.y, (double) p2.x, (double) p2.y);
        double b = getDistance((double) p1.x, (double) p1.y, (double) p3.x, (double) p3.y);
        double c = getDistance((double) p2.x, (double) p2.y, p3.x, (double) p3.y);
        double phi = Math.acos((Math.pow(a, 2) + Math.pow(c, 2) - Math.pow(b, 2)) / (2*a*c));
        return phi;
    }

    public static Point/*double[]*/ getPointOnVector(Point origin, Point direction, double dist){
        ComplexNumber vector = new ComplexNumber(direction.x, direction.y, ComplexNumber.NUMERICAL);
        ComplexNumber convers = new ComplexNumber(dist, vector.getArgument(), ComplexNumber.GEOMETRICAL);
        return new Point((int) convers.getRealPart() + origin.x, (int) convers.getImaginaryPart() + origin.y);
        //return new double[]{convers.getRealPart() + origin.x, convers.getImaginaryPart() + origin.y};
    }

    public static float[] getCoorOnPathAtPercent(float percent, Path path){
        PathMeasure pm = new PathMeasure(path, false);
        //coordinates will be here
        float aCoordinates[] = {0f, 0f};

        //get coordinates of the point
        pm.getPosTan(pm.getLength() * percent, aCoordinates, null);
        return aCoordinates;
    }

    public static Point getClosestPointOnPath(Point anchor, Path path, float step){
        float[] tmp = getCoorOnPathAtPercent(0, path);
        Point r = new Point((int) tmp[0], (int) tmp[1]);
        double minDist = getDistance(anchor.x, anchor.y, tmp[0], tmp[1]);
        for(float percent = 0.0f; percent < 1.0f; percent += step){
            tmp = getCoorOnPathAtPercent(percent, path);
            if(getDistance(anchor.x, anchor.y, tmp[0], tmp[1]) < minDist){
                minDist = getDistance(anchor.x, anchor.y, tmp[0], tmp[1]);
                r = new Point((int) tmp[0], (int) tmp[1]);
            }
        }
        return r;
    }
}
