//package com.crazy_putting.game.Physics;
//
//import com.crazy_putting.game.GameObjects.GameObject;
//
//public class PhysicsMidPoint {
//
//        private static final double g = 9.81;
//
//        //just create a friction coefficient here for now
//        private static final float mu = (float) 0.4;
//
//
//        /** Calculates the partial derivative of x.
//         * @param obj
//         * @return double
//         */
//        private static double partialDerivativeX(GameObject obj) {
//            return 0.1 + 0.06 * obj.getPosition().x;
//        }
//
//        /** Calculates the partial derivative of y.
//         * @param obj
//         * @return double
//         */
//
//        private static double partialDerivativeY(GameObject obj) {
//            return 0.2;
//        }
//
//
//        /** applying the force from user
//         * @param someObj, angle, speed
//         */
//        public static void appliedForce(GameObject someObj, float angle, float speed){
//            someObj.getVelocity().setAngle(angle);
//            someObj.getVelocity().setSpeed(speed);
//
//        }
//
//        /** Updates the coordinates (position) and velocity of the object.
//         * @param obj, dt
//         */
//
//        public static void update(GameObject obj, double dt){
//
//            float x = obj.getPosition().x;
//            float y = obj.getPosition().y;
//
//
//            //calculation of a new X position
//            // x(t + h) = x(t) +hVx(t);
//            float newX = upDatex(obj, dt);
//
//            //calculation of a new Y position
//            // y(t+h) = y(t) + hVy(t);
//            float newY = (float) (y + (dt * obj.getVelocity().Vy));
//
//            //calculation of a new total velocity of the ball
//            // v(t+h) = v(t) + h*F(x,y,vx,vy)/m
//            float newSpeedX = (float) (obj.getVelocity().Vx + dt * totalForceX(obj) / obj.getMass());
//            float newSpeedY = (float) (obj.getVelocity().Vy + dt * totalForceY(obj) / obj.getMass());
//
//            obj.getVelocity().Vx = upDateVx(obj, dt );
//            obj.getVelocity().Vy = newSpeedY;
//            // System.out.println("X: "+newSpeedX);
//            obj.setPositionX(newX);
//            obj.setPositionY(newY);
//
//        }
//        public static float upDatex(GameObject obj, double dt){
//            float parametricX= (float)(obj.getPosition().x + dt*obj.getVelocity().Vx);
//            float oldVel=obj.getVelocity().Vx;
//            float Vel= (float)(oldVel+dt*totalForceX(obj) / obj.getMass());
//            float newT= (float) (dt+(Vel-oldVel)*obj.getMass()/totalForceX(obj));
//            float newVx= parametricX/newT;
//            float xNew= (float)(obj.getPosition().x + dt*newVx);
//            return xNew;
//        }
//        public static float upDateVx(GameObject obj, double dt){
//            float parametricVx= (float)(obj.getVelocity().Vx + dt*totalForceX(obj) / obj.getMass());
//            float oldVel=obj.getVelocity().Vx;
//            float Vel= (float)(oldVel+dt*totalForceX(obj) / obj.getMass());
//            float newT= (float) (dt+(Vel-oldVel)*obj.getMass()/totalForceX(obj));
//            float newAx= parametricVx/newT;
//            float VxNew=(float)(obj.getVelocity().Vx + dt*newAx);
//            return VxNew;
//        }
//
//        /**Calculation of the Gravitational Force
//         *G = -mgh(,x)
//         * @return float
//         * @param obj
//         */
//        private static float gravityForceX(GameObject obj) {
//            float result = (float) (- obj.getMass() * g * partialDerivativeX(obj));
//
//            return result;
//        }
//        /**Calculation of the Gravitational Force
//         *G = - mgh(,y)
//         * @return float
//         * @param obj
//         */
//
//        private static float gravityForceY(GameObject obj) {
//            float result = (float) (- obj.getMass() * g * partialDerivativeY(obj));
//            return result;
//        }
//
//        /**
//         *Calculation of the Force of friction
//         *H = -(mu)* m* g* v / ||V||
//         *V = vx/cos(x)
//         * @param obj
//         * @return float
//         */
//        private static float frictionForceX(GameObject obj) {
//
//            float numerator = (float) (-mu * obj.getMass() * g * obj.getVelocity().Vx);
//            float lengthOfVelocityVector = (float) (Math.pow(obj.getVelocity().Vx, 2) + Math.pow(obj.getVelocity().Vy, 2));
//            float denominator = (float) Math.sqrt(lengthOfVelocityVector);
//
//            return numerator / denominator;
//        }
//        /**
//         *Calculation of the Force of friction
//         *H = -(mu)* m* g* v / ||V||
//         *V = vy/sin(x)
//         * @param obj
//         * @return float
//         */
//
//        private static float frictionForceY(GameObject obj){
//            float numerator = (float) (-mu * obj.getMass() * g * obj.getVelocity().Vy);
//            float lengthOfVelocityVector = (float) (Math.pow(obj.getVelocity().Vx, 2) + Math.pow(obj.getVelocity().Vy, 2));
//            float denominator = (float) Math.sqrt(lengthOfVelocityVector);
//
//            return numerator / denominator;
//        }
//        /**
//         * Calculates total force acting on object in x direction.
//         * @param obj
//         * @return float
//         */
//
//
//        public static float totalForceX(GameObject obj){
//            return gravityForceX(obj) + frictionForceX(obj);
//        }
//        /**
//         * Calculates total force acting on object in y direction.
//         * @param obj
//         * @return float
//         */
//
//        public static float totalForceY(GameObject obj){
//            return gravityForceY(obj) + frictionForceY(obj);
//        }
//
//
//    }
