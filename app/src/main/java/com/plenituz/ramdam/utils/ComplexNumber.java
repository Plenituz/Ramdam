package com.plenituz.ramdam.utils;

/**
 * Created by Plenituz on 30/01/2016 for Ramdam.
 * Qui l'eut cru, les complexes servent aussi dans la vraie vie
 */
public class ComplexNumber{

    public static final boolean NUMERICAL = true;
    public static final boolean GEOMETRICAL = false;


    private double realPart;
    private double imaginaryPart;
    private double modulus;
    /**
     * in radians
     */
    private double argument;

    /**
     * initialisation as a+ib or a*exp(ib)
     * @param a real part(if NUMERICAL) or modulus(if GEOMETRICAL)
     * @param b imaginary part(if NUMERICAL) or argument(if(GEOMETRICAL)
     * @param type NUMERICAL or GEOMETRICAL
     */
    public ComplexNumber(double a, double b, boolean type){
        if(type == NUMERICAL){
            realPart = a;
            imaginaryPart = b;
            fillComponents(GEOMETRICAL);
        }else if(type == GEOMETRICAL){
            this.modulus = a;
            this.argument = b;
            fillComponents(NUMERICAL);
        }

    }

    private void fillComponents(boolean what) {
        if (what == NUMERICAL){
            argument %= Math.PI*2;
            realPart = modulus/Math.sqrt(1 + Math.pow(Math.tan(argument), 2));
            imaginaryPart = (modulus * Math.tan(argument))/Math.sqrt(1 + Math.pow(Math.tan(argument), 2));
            if(argument > Math.PI/2 && argument <= Math.PI + (Math.PI/2)){
                realPart *= -1;
                imaginaryPart*= -1;
            }
        }else if(what == GEOMETRICAL){
            modulus = Math.sqrt(Math.pow(realPart, 2) + Math.pow(imaginaryPart, 2));
            argument = Math.atan2(imaginaryPart, realPart);
            if(argument < 0){
                argument += Math.PI*2;
            }
            if(argument > Math.PI*2){
                argument -= Math.PI*2;
            }
        }

    }

    public double getRealPart() {
        return realPart;
    }

    public double getImaginaryPart() {
        return imaginaryPart;
    }

    public double getModulus() {
        return modulus;
    }

    /**
     *
     * @return argument in radians
     */
    public double getArgument() {
        return argument;
    }


    /**
     *
     * @param anchor point to rotate arround
     * @param pointToRotate point which is getting rotated arround anchor
     * @param angle in radian
     * @return the new coordonates of pointToRotate
     */
    public static android.graphics.Point rotate(android.graphics.Point anchor, android.graphics.Point pointToRotate, double angle){
        ComplexNumber vector = new ComplexNumber(pointToRotate.x - anchor.x, pointToRotate.y - anchor.y, ComplexNumber.NUMERICAL);
        ComplexNumber convers = new ComplexNumber(vector.getModulus(), vector.getArgument() + angle, ComplexNumber.GEOMETRICAL);
        return new android.graphics.Point((int) (convers.getRealPart() + anchor.x), (int) (convers.getImaginaryPart() + anchor.y));
    }

    /**
     * same as get point on line at distance but better
     * @param origin
     * @param direction
     * @param dist
     * @return
     */
    public static android.graphics.Point/*double[]*/ getPointOnVector(android.graphics.Point origin, android.graphics.Point direction, double dist){
        ComplexNumber vector = new ComplexNumber(direction.x, direction.y, ComplexNumber.NUMERICAL);
        ComplexNumber convers = new ComplexNumber(dist, vector.getArgument(), ComplexNumber.GEOMETRICAL);
        return new android.graphics.Point((int) convers.getRealPart() + origin.x, (int) convers.getImaginaryPart() + origin.y);
        //return new double[]{convers.getRealPart() + origin.x, convers.getImaginaryPart() + origin.y};
    }
}
