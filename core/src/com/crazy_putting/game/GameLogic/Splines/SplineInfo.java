package com.crazy_putting.game.GameLogic.Splines;

import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class SplineInfo {
    private double[][] coeff;
    private Vector2 _pos;
    private Vector2 _dimensions;
    private float  _scale;
    private int[][] _pointsInGrid;//indexes to the points in bicubicSplines
    private Node _node;
    private Rectangle _rect;
    public SplineInfo(Vector2 posStart, Vector2 pDimensions, float pScale, Node pNode) {
    _pos = posStart;
    _scale = pScale;
    _dimensions = pDimensions;
    _node = pNode;
        createRectangle(this);
    }
    public Rectangle getRec(){
        return _rect;
    }
    private void createRectangle(SplineInfo spline){//TODO> clean, erase parameter since now the method is in the itself and not in bicubic we dont need to retreive data
        Vector2 start = spline.getStartPos();
        Vector2 dimensions = spline.getDimensions();
        _rect = new Rectangle(start.x,start.y,dimensions.x,dimensions.y);

    }
    public void setCoeff(double[][] pCoeff,int[][] pPoints){
        coeff = pCoeff;
        _pointsInGrid = pPoints;
    }

    public Node getNode() {
        return _node;
    }

    public double[][] getCoeff()
    {
      //  printArray(coeff);
        return coeff;
    }
    public Vector2 normPos(Vector2 pos){

        float scaleX = 1.0f/(_dimensions.x *1.0f);
        float scaleY = 1.0f/(_dimensions.y*1.0f);
        Vector2 cache = new Vector2(_pos);
        Vector2 nPos = new Vector2(pos);
        nPos.x = pos.x - cache.x;
        nPos.y = pos.y - cache.y;
        nPos.x *= scaleX;
        nPos.y *= scaleY;
        return nPos;
    }
    public int[][] getPoints(){
        return _pointsInGrid;
    }

    public Vector2 getDimensions() {
        return _dimensions;
    }

    public Vector2 getStartPos() {
        return _pos;
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
    private String printArray(int[][] array)
    {
        String out = "\n";
        for(int i = 0; i<array.length; i++)
        {for(int j = 0; j<array[0].length; j++)
        {
            out += array[i][j]+"  ";
        }
            out+="\n";
        }
       return out;
    }


    @Override
    public String toString() {
        String out = "SplineInfo\n"+ printArray(_pointsInGrid) +"\n Pos "+_pos;
        return out;
    }
}
