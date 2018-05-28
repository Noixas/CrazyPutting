package com.crazy_putting.game.GameLogic.Splines;

import com.badlogic.gdx.math.Vector2;

public class SplineInfo {
    private double[][] coeff;
    private Vector2 _pos;
    private Vector2 _dimensions;
    private float  _scale;

    public SplineInfo(Vector2 posStart, Vector2 pDimensions, float pScale) {
    _pos = posStart;
    _scale = pScale;
    _dimensions = pDimensions;
    }
    public void setCoeff(double[][] pCoeff){
        coeff = pCoeff;
    }
    public double[][] getCoeff()
    {
      //  printArray(coeff);
        return coeff;
    }
    public Vector2 normPos(Vector2 pos){
        System.out.println(pos+"Actual pos");
        float scaleX = 1.0f/(_dimensions.x *1.0f);
        float scaleY = 1.0f/(_dimensions.y*1.0f);
        Vector2 cache = new Vector2(_pos);
        System.out.println(cache +"Block Pos");
        System.out.println(scaleX + " ScaleY "+scaleY);
        Vector2 nPos = new Vector2(pos);
       nPos.x= pos.x  -cache.x;
        nPos.y = pos.y-cache.y;
        nPos.x *=scaleX;
        nPos.y *=scaleY;
        System.out.println(nPos);
        return nPos;
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
