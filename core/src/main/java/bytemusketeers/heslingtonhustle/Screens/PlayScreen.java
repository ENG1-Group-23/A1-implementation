package main.java.bytemusketeers.heslingtonhustle.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import main.java.bytemusketeers.heslingtonhustle.HeslingtonHustle;
import main.java.bytemusketeers.heslingtonhustle.Interactable;
import main.java.bytemusketeers.heslingtonhustle.Item;
import main.java.bytemusketeers.heslingtonhustle.Sprites.Character;

import javax.lang.model.type.NullType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code PlayScreen} class represents a screen which is shown after the game starts, implementing {@link Screen} interface
 * It initialises the world map and its contents, configure the world size and game camera
 */
public class PlayScreen implements Screen {
    private final static int INTERACTION_DISTANCE = 3;
    private OrthographicCamera gameCam;
    private HeslingtonHustle game;
    private Viewport gamePort;
    private World world;
    private Box2DDebugRenderer b2dr;
    private Character character;
    private Map<Integer, Interactable> interactables = new HashMap<>();
    private List<Stage> stages = new ArrayList<>();
    private PauseMenu pauseMenu;

    public PlayScreen(HeslingtonHustle game){
        this.pauseMenu = new PauseMenu(stages);
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new StretchViewport(HeslingtonHustle.W_WIDTH / HeslingtonHustle.PPM, HeslingtonHustle.W_HEIGHT / HeslingtonHustle.PPM, gameCam);

        gameCam.position.set(gamePort.getWorldWidth() / 2, (float) gamePort.getScreenHeight() / 2, 0);
        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();

        character = new Character(world);

        float randomX = MathUtils.random(0, HeslingtonHustle.W_WIDTH / HeslingtonHustle.PPM);
        float randomY = MathUtils.random(0, HeslingtonHustle.W_HEIGHT / HeslingtonHustle.PPM);
        Interactable test = new Interactable(new Vector2(randomX, randomY), new Texture("missing.png"));
        interactables.put(0, test);
    }

    @Override
    public void show() throws RuntimeException {
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Handles inputs
     */
    public void handleInput(){
        // moving the character
        float velX = 0, velY = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            velY = 2.0f ;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            velX = 2.0f;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            velY = -2.0f;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            velX = -2.0f;
        }
        character.b2body.setLinearVelocity(velX, velY);

        // menu
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if(this.stages.contains(this.pauseMenu.stage)) {
                stages.remove(this.pauseMenu.stage);
            } else {
                //TODO clear the world of bodies (character still shows when PauseMenu is up)
                //the above is if we don't want the world to be visible while the menu is up
                stages.add(this.pauseMenu.stage);
            }
        }

        // interaction
        if(Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            //if there is an interactable nearby the player then interact with it
            for(Interactable interactable : this.interactables.values()) {
                float distance = this.character.b2body.getPosition().dst2(interactable.getPosition());
                if(distance <= INTERACTION_DISTANCE) {
                        interactable.interact();
                }
            }
        }
    }

    /**
     * Handle the game logic and updates the state of the game world
     *
     * @param dt Stands for "delta time," which represents the time elapsed since the last frame update.
     */
    public void update(float dt){
        // handle user input
        handleInput();
        // calculates physics interactions, such as object movement, collisions, and forces, for a specific time interval
        world.step(1/60f, 6, 2);
        // tracking character's moves with the cam
        gameCam.position.x = character.b2body.getPosition().x;
        gameCam.position.y = character.b2body.getPosition().y;
        // update the gameCam with correct coordinates after changes
        gameCam.update();
    }

    @Override
    public void render(float delta) {
        update(delta);
        // sets the color specified when the color buffer is cleared
        Gdx.gl.glClearColor(1, 0, 0, 1);
        // GL_COLOR_BUFFER_BIT specifies that the color buffer is to be cleared
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // render the Box2DDebugLines
        b2dr.render(world, gameCam.combined);
        // recognises where the camera is in the game world and render only what the camera can see
        game.batch.setProjectionMatrix(gameCam.combined);
        // prepares the batch for drawing textures
        game.batch.begin();
        for(Interactable interactable : interactables.values()) {
            if(!interactable.isHidden()) {
                game.batch.draw(interactable.getTexture(), interactable.getX(), interactable.getY(), 0.5f, 0.5f);
            }
        }
        // at the moment this draws all the stages that are in stages array
        // even though there can only be one stage in the array at a time and so the array is redundant
        // might be useful for future?
        for(Stage stage : this.stages) {
            if(!(stage instanceof NullType)) {
                stage.draw();
            }
        }
        // ends the drawing session
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) { gamePort.update(width,height); }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        b2dr.dispose();
        for (Interactable interactable : interactables.values()) {
            interactable.getTexture().dispose();
        }
        world.dispose();
    }
}
