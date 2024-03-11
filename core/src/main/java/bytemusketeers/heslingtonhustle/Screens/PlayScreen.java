package main.java.bytemusketeers.heslingtonhustle.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import main.java.bytemusketeers.heslingtonhustle.HeslingtonHustle;
import main.java.bytemusketeers.heslingtonhustle.Interactable;
import main.java.bytemusketeers.heslingtonhustle.Item;
import main.java.bytemusketeers.heslingtonhustle.Player.Metrics;
import main.java.bytemusketeers.heslingtonhustle.Sprites.Character;
import main.java.bytemusketeers.heslingtonhustle.Sprites.TileMap;

import java.util.HashMap;
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
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private TileMap tileMap;
    private String[] studyItems = {"missing.png", "libgdx.png"};
    private Metrics metrics;


    public PlayScreen(HeslingtonHustle game){
        this.game = game;
        this.tileMap = new TileMap();
        this.orthogonalTiledMapRenderer = tileMap.setupMap();
        gameCam = new OrthographicCamera();
        gamePort = new StretchViewport(HeslingtonHustle.W_WIDTH / HeslingtonHustle.PPM, HeslingtonHustle.W_HEIGHT / HeslingtonHustle.PPM, gameCam);

        gameCam.position.set(gamePort.getWorldWidth() / 2, (float) gamePort.getScreenHeight() / 2, 0);
        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();

        character = new Character(world);

        metrics = new Metrics();
        //Need to add different stages so that we can change to different spawnable items
        for (int i = 0; i < studyItems.length; i++) {
            float randomX = MathUtils.random(0, HeslingtonHustle.W_WIDTH / HeslingtonHustle.PPM);
            float randomY = MathUtils.random(0, HeslingtonHustle.W_HEIGHT / HeslingtonHustle.PPM);
            Interactable interactable = new Interactable(new Vector2(randomX, randomY), new Texture(studyItems[i]), world, 0.5f, 0.5f);
            interactables.put(i, interactable);
        }
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
        final float velocity = 4.0f;
        float velX = 0, velY = 0;
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            velY += velocity;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            velX += velocity;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            velY += -velocity;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            velX += -velocity;
        }

        // Checks vel values
        // This is so that the player doesn't move faster when going diagonal
        if (velY != 0 && velX != 0) {
            velX /= 1.5;
            velY /= 1.5;
        }
        character.b2body.setLinearVelocity(velX, velY);

        // interaction
        if(Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            //if there is an interactable nearby the player then interact with it
            for(Map.Entry<Integer, Interactable> entry : this.interactables.entrySet()) {
                Interactable interactable = entry.getValue();
                float distance = this.character.b2body.getPosition().dst2(interactable.getPosition());
                if(distance <= INTERACTION_DISTANCE) {
                        interactable.interact();
                        metrics.itemPickedUp(entry.getKey());
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
        MapProperties props = this.tileMap.getProperties();
        int mapWidthInTiles = props.get("width", Integer.class);
        int mapHeightInTiles = props.get("height", Integer.class);

        int tileWidth = props.get("tilewidth", Integer.class);
        int tileHeight = props.get("tileheight", Integer.class);

        int mapWidthInPixels = mapWidthInTiles * tileWidth;
        int mapHeightInPixels = mapHeightInTiles * tileHeight;
        float mapWidthInMeters = mapWidthInPixels * tileMap.getScale();
        float mapHeightInMeters = mapHeightInPixels * tileMap.getScale();
        if (character.b2body.getPosition().x >= HeslingtonHustle.WIDTH_METRES_BOUND && character.b2body.getPosition().x <= mapWidthInMeters - HeslingtonHustle.WIDTH_METRES_BOUND) {
            gameCam.position.x = character.b2body.getPosition().x;
        }
        if (character.b2body.getPosition().y >= HeslingtonHustle.HEIGHT_METRES_BOUND && character.b2body.getPosition().y <= mapHeightInMeters - HeslingtonHustle.HEIGHT_METRES_BOUND) {
            gameCam.position.y= character.b2body.getPosition().y;
        }
        // tracking character's moves with the cam

        orthogonalTiledMapRenderer.setView(gameCam);
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


        orthogonalTiledMapRenderer.render();
        // render the Box2DDebugLines
        b2dr.render(world, gameCam.combined);

        // recognises where the camera is in the game world and render only what the camera can see
        game.batch.setProjectionMatrix(gameCam.combined);
        // prepares the batch for drawing textures
        game.batch.begin();
        game.batch.draw(character.playerTexture, character.b2body.getPosition().x - Character.WIDTH / 2, character.b2body.getPosition().y - Character.HEIGHT / 2, Character.WIDTH, Character.HEIGHT);
        for(Interactable interactable : interactables.values()) {
            if(!interactable.isHidden()) {
                //the position being set to x - width / 2, y - height / 2 makes it so the center of the item is spawned on the position
                game.batch.draw(interactable.getTexture(),
                            interactable.getX() - (interactable.getWidth()/2),
                            interactable.getY() - (interactable.getHeight() /2),
                                interactable.getWidth(),
                                interactable.getHeight()
                );
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
        character.playerTexture.dispose();
        for (Interactable interactable : interactables.values()) {
            interactable.getTexture().dispose();
        }
        world.dispose();
    }


}
