package com.crazy_putting.game.GameLogic.Splines;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class BiCubicSpline {
    private double[][] A = {   {1,     0,     0 ,    0,     0,     0,     0,     0,     0,     0,     0,     0,     0 ,    0,     0,     0},
                                {0,     0,     0,     0,     1,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0},
                                {-3,     3,     0,     0,    -2,    -1,     0,     0,     0,     0,     0,    0,     0,     0,     0,     0},
                                {2,    -2 ,    0   ,  0,     1 ,    1,     0  ,   0,     0   ,  0,     0   ,  0,     0  ,   0,     0 ,    0},
                                {0,     0 ,    0   ,  0 ,    0 ,    0,     0  ,   0,     1   ,  0,     0   ,  0 ,    0  ,   0,     0 ,    0},
                                {0,     0 ,    0   ,  0 ,    0 ,    0,     0  ,   0,     0   ,  0,     0   ,  0 ,    1  ,   0,     0 ,    0},
                                {0 ,    0 ,    0   ,  0 ,    0 ,    0,     0  ,   0,    -3   ,  3 ,    0   ,  0 ,   -2  ,  -1,     0 ,    0},
                                {0,     0 ,    0  ,   0 ,    0 ,    0,     0  ,   0,     2   , -2 ,    0   ,  0 ,    1  ,   1,     0 ,    0},
                                {-3,     0 ,    3 ,    0 ,    0 ,    0,     0 ,    0,   -2  ,   0 ,   -1  ,   0 ,    0 ,    0,     0,     0},
                                {0 ,    0  ,   0  ,   0 ,   -3 ,    0,     3  ,   0 ,    0  ,   0  ,   0   ,  0 ,   -2  ,   0 ,   -1 ,    0},
                                {9 ,   -9  ,  -9  ,   9 ,    6,     3,    -6  ,  -3 ,    6  ,  -6  ,   3   , -3 ,    4  ,   2 ,    2 ,    1},
                                {-6 ,    6  ,   6 ,   -6 ,   -3 ,   -3,     3  ,   3,   -4 ,    4 ,   -2  ,   2 ,   -2 ,   -2,    -1,    -1},
                                {2 ,    0 ,   -2  ,   0 ,    0 ,    0,     0  ,   0 ,    1  ,   0  ,   1   ,  0  ,   0  ,   0,     0 ,    0},
                                {0 ,    0 ,    0  ,   0 ,    2,     0,    -2 ,    0 ,    0 ,    0  ,   0  ,   0  ,   1 ,    0,     1 ,    0},
                                {-6 ,    6 ,    6,    -6 ,   -4,    -2,     4,     2,   -3,     3 ,   -3 ,    3 ,   -2,    -1,    -2,    -1},
                                {4  ,  -4 ,   -4,     4 ,    2,     2,    -2,    -2 ,    2,    -2  ,   2 ,   -2  ,   1 ,    1,     1,     1}};
    private List<SplineInfo> _splineList = new ArrayList<SplineInfo>();
    public BiCubicSpline(){

    }
    public SplineInfo createSplineBlock(double[][] points, Vector2 posStart, Vector2 pDimensions, float pScale){
        SplineInfo spline =new SplineInfo(posStart,pDimensions,pScale);
        _splineList.add(spline);
        updateSplineCoeff(spline,points);
    return spline;
    }
    private double[][] mulMat(double[][] pPoints, double[][] pSOE)
    {
        int aRows = pPoints.length;
        int aColumns = pPoints[0].length;
        int bRows = pSOE.length;
        int bColumns = pSOE[0].length;

        if (aColumns != bRows) {
            throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
        }

        double[][] out = new double[aRows][bColumns];
//        for (int i = 0; i < aRows; i++) {
//            for (int j = 0; j < bColumns; j++) {
//                out[i][j] = 0.00000;
//            }
//        }
        for (int i = 0; i < aRows; i++) { // aRow
            for (int j = 0; j < bColumns; j++) { // bColumn
                for (int k = 0; k < aColumns; k++) { // aColumn
                    out[i][j] += pPoints[i][k] * pSOE[k][j];
                }
            }
        }
        return out;
    }

    public SplineInfo updateSplineCoeff(SplineInfo info, double[][] pPoints)
    {
        double[][] cache = mulMat(A,pPoints);
        double[][] newCoeff = {{cache[0][0], cache[4][0], cache[8][0], cache[12][0]},
                {  cache[1][0], cache[5][0], cache[9][0], cache[13][0]},
                {cache[2][0], cache[6][0], cache[10][0], cache[14][0]},
                {cache[3][0], cache[7][0], cache[11][0], cache[15][0]}};
        info.setCoeff(newCoeff);

        //normalize spline lenght
        //f(x,y) = [1,x,x2,x3] * coeff * [1;y;y2;y3]
        return info;
    }
    public float getHeightAt(Vector2 pPos, SplineInfo spline){
        Vector2 posLocal = spline.normPos(pPos);
        System.out.println("INFO PSO LOCAL");
        System.out.println(posLocal);
        double[][] x = {{1,posLocal.x,Math.pow(posLocal.x,2),Math.pow(posLocal.x,3)}};
        double[][] y = {{1},{posLocal.y},{Math.pow(posLocal.y,2)},{Math.pow(posLocal.y,3)}};
        double[][] out = mulMat(x, mulMat(spline.getCoeff(),y));
       printArray(out);
        return (float)out[0][0];
    }
    private void printArray(double[][] array)
    {
        String out = "\n";
        for(int i = 0; i<array.length; i++)
        {for(int j = 0; j<array.length; j++)
        {
            out += array[i][j]+"  ";
        }
            out+="\n";
        }
        System.out.println(out);
    }
}
