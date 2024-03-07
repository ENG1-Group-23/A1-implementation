package main.java.bytemusketeers.heslingtonhustle.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import main.java.bytemusketeers.heslingtonhustle.Drawable;
import main.java.bytemusketeers.heslingtonhustle.HeslingtonHustle;
import main.java.bytemusketeers.heslingtonhustle.Interactable;
import main.java.bytemusketeers.heslingtonhustle.Item;
import main.java.bytemusketeers.heslingtonhustle.Sprites.Character;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * The {@code PlayScreen} class represents a screen which is shown after the game starts, implementing {@link Screen} interface
 * It initialises the world map and its contents, configure the world size and game camera
 */
public class PlayScreen implements Screen {
    private final static int INTERACTION_DISTANCE = 50000;
    private OrthographicCamera gameCam;
    private HeslingtonHustle game;
    private Viewport gamePort;
    private World world;
    private Box2DDebugRenderer b2dr;
    private Character character;
    private HashMap<Integer, Drawable> drawables = new HashMap<>();

    public PlayScreen(HeslingtonHustle game){
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new StretchViewport(HeslingtonHustle.W_WIDTH / HeslingtonHustle.PPM, HeslingtonHustle.W_HEIGHT / HeslingtonHustle.PPM, gameCam);

        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getScreenHeight() / 2, 0);
        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();

        character = new Character(world);
        Item test;
        try {
            URL resource = getClass().getClassLoader().getResource("missing.png");
            URI resourceURI = resource.toURI();
            Path imgPath = Paths.get(resourceURI);
            test = new Item(new Vector2(100, 100), new Texture(imgPath.toString()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        drawables.put(0, test);
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

        // interaction
        if(Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            //if there is an interactable nearby the player then interact with it
            for(Drawable drawable : this.drawables.values()) {
                float distance = this.character.b2body.getPosition().scl(100).dst2(drawable.position);
                if((drawable instanceof Interactable) && (distance <= INTERACTION_DISTANCE)) {
                        ((Interactable) drawable).interact();
                }
            }
        }


            //call the interact function of the interactable
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
//        gameCam.position.x = character.b2body.getPosition().x;
//        gameCam.position.y = character.b2body.getPosition().y;
        // update the gamecam with correct coordinates after changes
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
        for (Drawable drawable : drawables.values()) {
            drawable.draw();
        }
        game.batch.begin();
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
        for (Drawable drawable : drawables.values()) {
            drawable.spriteBatch.dispose();
        }
        world.dispose();
    }
}
