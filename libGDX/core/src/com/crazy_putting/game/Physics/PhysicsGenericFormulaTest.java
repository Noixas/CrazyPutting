package com.crazy_putting.game.Physics;

import com.crazy_putting.game.FormulaParser.*;
import com.crazy_putting.game.FormulaParser.FormulaParser;
import com.crazy_putting.game.GameObjects.GameObject;


public class PhysicsGenericFormulaTest {

        private static final double g = 9.81;
        //just create a friction coefficient here for now
        private static final float mu = (float) 0.4;

        private static final String formula = "0.1 * x + 0.03*x^2 + 0.2*y";

        private static FormulaParser parser = new FormulaParser();
        private static ExpressionNode expr = null;

        private static final double EPSILON = 1;



        private static double partialDerivativeX(GameObject obj) {
            float x1 = (float) (obj.getPosition().x + EPSILON);
            float x2 = (float) (x1 - 2*EPSILON);
            float y = obj.getPosition().y;
            float result = (float) ((calcFunction(x1,y) - calcFunction(x2,y))/2*EPSILON);
            //float difference = (float) (result - (0.01 + obj.getPosition().x * 0.06));
            //System.out.println("approximation error: " + difference);
            return result;
        }

        private static double partialDerivativeY(GameObject obj) {
            float x = (float) (obj.getPosition().x + EPSILON);
            float y1 = obj.getPosition().y;
            float y2 = (float) (y1 - 2*EPSILON);
            float result = (float) ((calcFunction(x,y1) - calcFunction(x,y2))/2*EPSILON);
            //System.out.println(result);
            return result;
        }

        private static float calcFunction(float x, float y){

            try {
                if (expr == null) {
                    expr = parser.parse(formula);
                }
                    expr.accept(new SetVariable("x", x));
                    expr.accept(new SetVariable("y", y));

                    float result = (float) expr.getValue();
                    return result;

            }
            catch (ParserException e)
            {
                System.out.println(e.getMessage());
            }
            catch (EvaluationException e)
            {
                System.out.println(e.getMessage());
            }
            return 0;
        }



    /*
    applying the force from user
    */

        public static void appliedForce(GameObject someObj, float angle, float speed){
            someObj.getVelocity().setAngle(angle);
            someObj.getVelocity().setSpeed(speed);

        }



        public static void update(GameObject obj, double dt){

            float x = obj.getPosition().x;
            float y = obj.getPosition().y;


            //calculation of a new X position
            // x(t + h) = x(t) +hVx(t);
            float newX = (float) (x + (dt * obj.getVelocity().Vx));

            //calculation of a new Y position
            // y(t+h) = y(t) + hVy(t);
            float newY = (float) (y + (dt * obj.getVelocity().Vy));

            //calculation of a new total velocity of the ball
            // v(t+h) = v(t) + h*F(x,y,vx,vy)/m
            float newSpeedX = (float) (obj.getVelocity().Vx + dt * totalForceX(obj) / obj.getMass());
            float newSpeedY = (float) (obj.getVelocity().Vy + dt * totalForceY(obj) / obj.getMass());

            obj.getVelocity().Vx = newSpeedX;
            obj.getVelocity().Vy = newSpeedY;
            obj.setPositionX(newX);
            obj.setPositionY(newY);

        }

    /*
    total power that affects the ball is
    F = G + H;
    */

        //Calculation of the Gravitational Force
        //G = -mgh(,x) - mgh(,y)
        private static float gravityForceX(GameObject obj) {
            float result = (float) (- obj.getMass() * g * partialDerivativeX(obj));

            return result;
        }

        private static float gravityForceY(GameObject obj) {
            float result = (float) (- obj.getMass() * g * partialDerivativeY(obj));
            return result;
        }

        /*
        Calculation of the Force of friction
        H = -(mu)* m* v / ||V||
        V = vx/cos(x)
      */
        private static float frictionForceX(GameObject obj) {

            float numerator = (float) (-mu * obj.getMass() * g * obj.getVelocity().Vx);
            float lengthOfVelocityVector = (float) (Math.pow(obj.getVelocity().Vx, 2) + Math.pow(obj.getVelocity().Vy, 2));
            float denominator = (float) Math.sqrt(lengthOfVelocityVector);

            return numerator / denominator;
        }

        private static float frictionForceY(GameObject obj){
            float numerator = (float) (-mu * obj.getMass() * g * obj.getVelocity().Vy);
            float lengthOfVelocityVector = (float) (Math.pow(obj.getVelocity().Vx, 2) + Math.pow(obj.getVelocity().Vy, 2));
            float denominator = (float) Math.sqrt(lengthOfVelocityVector);

            return numerator / denominator;
        }


        public static float totalForceX(GameObject obj){
            return gravityForceX(obj) + frictionForceX(obj);
        }

        public static float totalForceY(GameObject obj){
            return gravityForceY(obj) + frictionForceY(obj);
        }


    }


