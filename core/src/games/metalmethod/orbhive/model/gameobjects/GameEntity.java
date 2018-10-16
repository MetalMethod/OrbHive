package games.metalmethod.orbhive.model.gameobjects;

import games.metalmethod.orbhive.model.Constants;
import games.metalmethod.orbhive.view.interfaces.BaseRectangle;
import games.metalmethod.orbhive.view.interfaces.Vector;

/**
 * Abstract class for all game entities that can be controlled.
 * made to abstract Player, Enemy
 */
public abstract class GameEntity {

    private Vector position;
    private Vector velocity;
    private Vector acceleration;

    /**
     * positive change in rotation is a clockwise rotation and that a negative change in rotation is a counterclockwise rotation.
     */
    private float rotation;

    private int width;
    private int height;


    /**
     * Object for collision detection
     */
    private BaseRectangle boundingRectangle;

    private EntityState currentState;
    private int lifes;

    /**
     * Constructor
     * @param x
     * @param y
     */
    public GameEntity(float x, float y) {
        this.width = 40;
        this.height = 25;

        this.position = new Vector(x, y);
        this.velocity = new Vector(0, 0);
        this.acceleration = new Vector(Constants.wind, Constants.gravity);

        boundingRectangle = new BaseRectangle();

        this.currentState = EntityState.FULL;
        this.lifes = Constants.initialLives;
    }

    /**
     * Update , called every frame
     * @param delta
     */
    public void update(float delta) {
        velocity.add(acceleration.cpy().scl(delta));
        position.add(velocity.cpy().scl(delta));

        boundingRectangle.set(getPosition().x, getPosition().y + 7, width, height);

    }



    public Vector getPosition() {
        return position;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public Vector getAcceleration() {
        return acceleration;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public void setAcceleration(Vector acceleration) {
        this.acceleration = acceleration;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public BaseRectangle getBoundingRectangle() {
        return boundingRectangle;
    }

    abstract void takeHit(int hitAmount);

    abstract EntityState getState();

}