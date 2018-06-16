package com.crazy_putting.game.Physics;

import com.badlogic.gdx.math.Vector3;
import com.crazy_putting.game.Components.Colliders.*;
import com.crazy_putting.game.GameLogic.CourseManager;
import com.crazy_putting.game.GameLogic.GraphicsManager;
import com.crazy_putting.game.GameObjects.GameObject;
import com.crazy_putting.game.GameObjects.PhysicsGameObject;
import com.crazy_putting.game.Others.Velocity;
import org.apache.velocity.runtime.parser.node.MathUtils;

import java.util.ArrayList;

public abstract class Physics {

    protected final float g = 9.806f;
    protected float EPSILON = 1;
    protected static float mu;

    protected final float RESTITUTION = 0.95f;

    protected ArrayList<PhysicsGameObject> movingObjects = new ArrayList<PhysicsGameObject>();


    protected State state = new State();

    protected Sphere sphere;
    protected  AABB box;
    protected CollisionDetector detector = new CollisionDetector();

    protected Contact cont;



    public static Physics physics = new RK4();

    /*
    Updating physics
     */

    public abstract void update(double dt);

    public abstract void updateObject(PhysicsGameObject obj, double dt);

    /*
    other
     */

    public void addMovableObject(PhysicsGameObject obj) {
        movingObjects.add(obj);
    }

    public static void updateCoefficients() {
        mu = CourseManager.getActiveCourse().getFriction();
    }


    private float equation2Points(float dx, float dy, float xValue, float previousX, float previousY) {
        return (dy/dx) * (xValue -  previousX) + previousY;
    }


    /*
    Collision
     */

    void dealCollision(PhysicsGameObject obj){
        obj.setPosition(CourseManager.getStartPosition());


        obj.fix(true);

        obj.setVelocity(0.00001f,0.000001f);

        //Gdx.app.log("Message","Ball collided");
    }

    public boolean collided(PhysicsGameObject obj ){
        state.update(obj);

        float xCur = state.getX();
        float yCur = state.getY();


        float xPrev = obj.getPreviousPosition().x;
        float yPrev = obj.getPreviousPosition().y;


        if(xCur > GraphicsManager.WORLD_WIDTH / 2 || xCur < GraphicsManager.WORLD_WIDTH / 2 * (-1) ||
                yCur > GraphicsManager.WORLD_HEIGHT / 2 || yCur < GraphicsManager.WORLD_HEIGHT / 2 * (-1) ){

            return true;
        }

        float dx = xCur-xPrev;
        float dy = yCur-yPrev;

        for (int i = 1; i < 4; i++){
            float height = CourseManager.calculateHeight(xPrev + dx / i, equation2Points(dx, dy, xPrev + dx / i, xPrev, yPrev));
            if (height < 0){
                return true;
            }
        }
        return false;
    }


        /*
    Acceleration a = F/m = G + H
     */

    public boolean isGoingToStop(PhysicsGameObject obj) {
        state.update(obj);

        Vector3 gravity = gravityForce(state);
        double grav = Math.sqrt(Math.pow(gravity.x,2)+ Math.pow(gravity.y,2));

        Vector3 friction = frictionForce(state);
        double fric = Math.sqrt(Math.pow(friction.x,2)+ Math.pow(friction.y,2));
        return obj.isSlow()&&fric>grav;
    }

    public Vector3 acceleration(State s){
        return new Vector3(frictionForce(s).x + gravityForce(s).x,frictionForce(s).y + gravityForce(s).y,0);
    }

    /*
    Calculate H
     */

    public Vector3 frictionForce(State s){
        float numeratorX = (-mu * g * s.getVx());
        float numeratorY = (-mu * g * s.getVy());

        float lengthOfVelocityVector = (float) (Math.pow(s.getVx(), 2) + Math.pow(s.getVy(), 2));
        float denominator = (float) Math.sqrt(lengthOfVelocityVector);

        return new Vector3(numeratorX/denominator,numeratorY/denominator,0);
    }

    /*
    Calculate G
     */

    public Vector3 gravityForce(State s){
        Vector3 partials = partialDerivatives(s);
        float gx = -partials.x * g ;
        float gy = -partials.y * g ;

        partials.x = gx;
        partials.y = gy;

        return partials;

    }

    /*
    Partial Derivatives
     */

    public Vector3 partialDerivatives(State s){
        float x1 =  s.getX() + EPSILON;
        float x2 =  x1 - 2 * EPSILON;
        float yCur = s.getY();

        float partialX = 2 * ((CourseManager.calculateHeight(x1, yCur) - CourseManager.calculateHeight(x2, yCur)) / 2 * EPSILON);

        x1-=EPSILON;
        yCur+=EPSILON;
        float y2 = yCur - 2 * EPSILON;

        float partialY = 2* ((CourseManager.calculateHeight(x1, yCur) - CourseManager.calculateHeight(x1, y2)) / 2 * EPSILON);

        return new Vector3(partialX,partialY,0);

    }
    public void addSphere(Sphere sphere){
        this.sphere = sphere;

    }
    public void addBox(AABB box){
        this.box = box;
    }


    public boolean testCollision() {
        if (this.box != null && this.sphere != null) {
            float result = detector.SphereWithAABB(sphere,box);
            if(result ==1){
                Contact contact = detector.getContact();
                dealCollision(contact);
                return true;
            }
        }
        return false;

    }

    public void updateSphere(GameObject obj, State state){
        this.sphere.setPosition(obj.getPosition());
        this.sphere.getVelocity().Vx = state.getVx();
        this.sphere.getVelocity().Vy = state.getVy();
    }

    public void dealCollision(Contact contact){

        resolveVelocity(contact);
        resolvePenetration(contact);

        this.cont = contact;
    }

    public Contact getCont() {
        return cont;
    }

    private void resolveVelocity(Contact contact){
        System.out.println("Sphere old Vx: " + contact.object1.getVelocity().Vx);
        System.out.println("Sphere old Vy: " + contact.object1.getVelocity().Vy);
        float separatingVelocity = calculateSeparatingVelocity(contact);

        if(separatingVelocity > 0){
            return;
        }

        float realSeparatingVelocity = -RESTITUTION * separatingVelocity;
        System.out.println("realSeparatingVelocity" + realSeparatingVelocity);

        float deltavelocity = realSeparatingVelocity - separatingVelocity;
        System.out.println("deltaVelocity: " + deltavelocity);

        float totalInverseMass = contact.object1.getInversemass() +contact.object2.getInversemass();


        float impulse = deltavelocity / totalInverseMass;
        System.out.println("impulse " + impulse);

        Vector3 impulsePerMass = contact.contactNormal.cpy().scl(impulse);
        System.out.println("Impulse vector: " + impulsePerMass);

        contact.object1.getVelocity().Vx +=impulsePerMass.x * contact.object1.getInversemass();
        contact.object1.getVelocity().Vy +=impulsePerMass.y * contact.object1.getInversemass();
        System.out.println("Sphere new Vx: " + contact.object1.getVelocity().Vx);
        System.out.println("Sphere new Vy: " + contact.object1.getVelocity().Vy);

        contact.object2.getVelocity().Vx +=impulsePerMass.x * contact.object2.getMass();
        contact.object2.getVelocity().Vy +=impulsePerMass.y * contact.object2.getMass();




    }

    private float calculateSeparatingVelocity(Contact contact) {
        Velocity relativeVelocity;
        //if (!contact.object1.isStatic()) {
            relativeVelocity = contact.object1.getVelocity();
        //}
        //if(!contact.object2.isStatic()){
            relativeVelocity.sub(contact.object2.getVelocity());
        //}

        float result = relativeVelocity.multiply(contact.contactNormal);

        return result;
    }

    private void resolvePenetration(Contact contact){
        System.out.println("old position" + contact.object1.getPosition() );
        float totalMass = contact.object1.getMass() + contact.object2.getMass();

        Vector3 changeInPosition1 = contact.contactNormal.scl((contact.object1.getMass()/totalMass)*contact.penetration);
        System.out.println("Change in position1: " + changeInPosition1);
        Vector3 changeInPosition2 = contact.contactNormal.scl((-contact.object2.getMass()/totalMass)*contact.penetration);
        System.out.println("Change in position2: " + changeInPosition2);

        contact.object1.setPosition(contact.object1.getPosition().cpy().add(changeInPosition1));
        System.out.println("New position: " + contact.object1.getPosition());
        contact.object2.setPosition(contact.object2.getPosition().cpy().add(changeInPosition2));
    }

}
