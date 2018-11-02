package games.metalmethod.orbhive.controller;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.math.Intersector;
import games.metalmethod.orbhive.model.gameobjects.entities.BasicEnemy;
import games.metalmethod.orbhive.model.gameobjects.EnemyFactory;
import games.metalmethod.orbhive.model.gameobjects.entities.EntityState;
import games.metalmethod.orbhive.model.gameobjects.entities.Player;
import games.metalmethod.orbhive.view.assets.AssetLoader;
import games.metalmethod.orbhive.view.interfaces.Vector;
import games.metalmethod.orbhive.view.screens.GameScreen;
import games.metalmethod.orbhive.model.gameworld.GameWorld;
import games.metalmethod.orbhive.model.Constants;

public class Controller extends Game {

    private GameWorld gameWorld;
    private GameScreen gameScreen;

    private Player player;
    private boolean isPlayerMoving = false;

    private EnemyFactory enemyFactory;



    private float runTime;

    private float playerHitTime;

    @Override
    public void create() {
        Gdx.app.log("Controller", "created");

        AssetLoader.load();
        enemyFactory = new EnemyFactory();

        player = new Player(Constants.playerWidth, Constants.playerHeight, new Vector(50, 110));
        playerHitTime = 500;

        gameScreen = new GameScreen(this);
        setScreen(gameScreen);

        int midPointY = gameScreen.getMidPointY();
        gameWorld = new GameWorld(midPointY);
    }

    public void update(float delta) {
        runTime = gameScreen.getRunTime();
        player.update(delta);
        detectWalls();

        gameScreen.update(delta);

        // create enemy after 3 secs
        //singleBasicEnemy.update(delta);
        //waspBasicEnemy.update(delta);

        enemyFactory.update(delta);

    }

    @Override
    public void dispose() {
        super.dispose();

        AssetLoader.dispose();
    }

    public GameWorld getGameWorld() {
        return gameWorld;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public Player getPlayer() {
        return player;
    }

    public void movePlayerUp() {

        player.setVelocity((Vector) player.getVelocity().add(0, -Constants.movementVelocity));
        isPlayerMoving = true;
    }

    public void movePlayerForward() {
        player.setVelocity((Vector) player.getVelocity().add(Constants.movementVelocity, 0));
        isPlayerMoving = true;
    }

    public void movePlayerDown() {
        player.setVelocity((Vector) player.getVelocity().add(0, Constants.movementVelocity));
        isPlayerMoving = true;
    }

    public void movePlayerBack() {
        player.setVelocity((Vector) player.getVelocity().add(-Constants.movementVelocity, 0));
        isPlayerMoving = true;
    }

    public void stopPlayer() {
        stopMovePlayerY();
        stopMovePlayerX();
    }

    public void stopMovePlayerY() {
        player.setVelocity((Vector) player.getVelocity().set(player.getVelocity().x, 0));
        isPlayerMoving = false;
    }

    public void stopMovePlayerX() {
        player.setVelocity((Vector) player.getVelocity().set(0, player.getVelocity().y));
        isPlayerMoving = false;
    }

    public void detectWalls() {
        //left
        if (player.getPosition().x < 5) {
            player.setPosition((Vector) player.getPosition().set(5, player.getPosition().y));
        }
        //right
        if (player.getPosition().x > 410) {
            player.setPosition((Vector) player.getPosition().set(410, player.getPosition().y));
        }
        //top
        if (player.getPosition().y < 0) {
            player.setPosition((Vector) player.getPosition().set(player.getPosition().x, 0));
        }
        //down
        if (player.getPosition().y > 220) {
            player.setPosition((Vector) player.getPosition().set(player.getPosition().x, 220));
        }
    }

    public boolean isPlayerMoving() {
        return isPlayerMoving;
    }


    public BasicEnemy createSingleEnemy() {
        return enemyFactory.obtainBasicEnemy();
    }

    public BasicEnemy createWaspEnemy() {
        return enemyFactory.obtainBasicEnemy();
    }

    public boolean detectPlayerCollisionEnemy(Player player, BasicEnemy basicEnemy){

        boolean result = Intersector.overlaps(player.getBoundingRectangle(), basicEnemy.getBoundingRectangle());

        if(result){
            playerHitTime = 0;
            player.takeHit(Constants.playerHitAcceleration);
            basicEnemy.takeHit(Constants.enemyHitAcceleration);
            enemyFactory.disposeEnemy(basicEnemy);
        }else {
            playerHitTime++;
        }

        return result;
    }

    public boolean isPlayerHit() {

        if(playerHitTime < 50){
                return true;
            }else {
                return false;
            }
        }

    public EntityState playerState(Player player) {

        if(player.getState() == EntityState.FULL){
                return EntityState.FULL;
        }

        if(player.getState() == EntityState.MID){
            return EntityState.MID;
        }

        if(player.getState() == EntityState.LAST){
            return EntityState.LAST;
        }

//        if(player.getState() == EntityState.DEAD){
//            return EntityState.DEAD;
//        }

        return EntityState.DEAD;

    }


    public void playerShoot() {
        player.shoot();

    }

    public boolean isPlayerShooting() {
        return false;
    }
}
