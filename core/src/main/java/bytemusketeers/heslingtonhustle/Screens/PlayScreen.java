package main.java.bytemusketeers.heslingtonhustle.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import main.java.bytemusketeers.heslingtonhustle.HeslingtonHustle;
import main.java.bytemusketeers.heslingtonhustle.Interactable;
import main.java.bytemusketeers.heslingtonhustle.Item;
import main.java.bytemusketeers.heslingtonhustle.Player.Metrics;
import main.java.bytemusketeers.heslingtonhustle.Sprites.*;
import main.java.bytemusketeers.heslingtonhustle.Sprites.Character;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * The {@link PlayScreen} class represents a screen which is shown after the game starts, implementing {@link Screen} interface
 * It initialises the world map and its contents, configure the world size and game camera
 */
public class PlayScreen implements Screen {
    protected final static int INTERACTION_DISTANCE = 3;
    protected OrthographicCamera gameCam;
    protected HeslingtonHustle game;
    protected Viewport gamePort;
    protected World world;
    protected Box2DDebugRenderer b2dr;
    protected Character character;
    protected Map<Integer, Interactable> interactables = new HashMap<>();
    protected OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    protected TileMap tileMap;
    protected String[] studyItems = {"missing.png", "libgdx.png"};
    protected Metrics metrics;
    private Vector2 initialCharacterPos;
    private Vector2 initialCameraPos;
    private Boolean isPaused = false;
    private PauseMenu pauseMenu;


    public PlayScreen(HeslingtonHustle game, String map, Vector2 initialCharacterPos, Vector2 initialCameraPos){
        this.pauseMenu = new PauseMenu(this);
        this.game = game;
        this.tileMap = new TileMap();
        this.orthogonalTiledMapRenderer = tileMap.setupMap(map);
        this.initialCharacterPos = initialCharacterPos;
        this.initialCameraPos = initialCameraPos;
        gameCam = new OrthographicCamera();
        gamePort = new StretchViewport(HeslingtonHustle.W_WIDTH / HeslingtonHustle.PPM, HeslingtonHustle.W_HEIGHT / HeslingtonHustle.PPM, gameCam);

        world = new World(new Vector2(0, 0), true);
        b2dr = new Box2DDebugRenderer();
        metrics = new Metrics();
    }

    @Override
    public void show() {
        // Set gameCam position
        gameCam.position.set(initialCameraPos.x, initialCameraPos.y, 0);

        // Creating the character
        Vector2 characterPos = new Vector2(initialCharacterPos.x, initialCharacterPos.y);
        character = new Character(world, characterPos);

        MapLayers layers = tileMap.getTiledMap().getLayers();
        for (MapLayer layer : layers) {
            if (layer.getName().equals("borders")) {
                generateBorders(layers);
            }
            if (layer.getName().equals("eating-place")) {
                generateEatingPlaces(layers);
            }
            if (layer.getName().equals("recreation-place")) {
                generateRecreationalPlaces(layers);
            }
        }
    }

    /**
     * Generates spawnable objects
     */
    public void generateSpawnable () {
        //Need to add different stages so that we can change to different spawnable items
        for (int i = 0; i < studyItems.length; i++) {
            float randomX = MathUtils.random(0, HeslingtonHustle.W_WIDTH / HeslingtonHustle.PPM);
            float randomY = MathUtils.random(0, HeslingtonHustle.W_HEIGHT / HeslingtonHustle.PPM);
            Interactable interactable = new Interactable(new Vector2(randomX, randomY), new Texture(studyItems[i]), world, 0.5f, 0.5f);
            interactables.put(i, interactable);
        }
    }

    /**
     * Creating borders by iterating through all the objects which exist at the walls layer on the tiled map
     */
    public void generateBorders (MapLayers layers) {
        int index = layers.getIndex("borders");
        for (RectangleMapObject object : layers.get(index).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();

            new Border(world, tileMap, rect);
        }
    }

    /**
     * Creating eating places by iterating through all the objects which exist at the eating-place layer on the tiled map
     */
    public void generateEatingPlaces (MapLayers layers) {
        int index = layers.getIndex("eating-place");
        for (RectangleMapObject object : layers.get(index).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();

            new EatingPlace(world, tileMap, rect);
        }
    }

    /**
     * Creating recreation places by iterating through all the objects which exist at the recreation-place layer on the tiled map
     */
    public void generateRecreationalPlaces (MapLayers layers) {
        int index = layers.getIndex("recreation-place");
        for (RectangleMapObject object : layers.get(index).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = object.getRectangle();

            new RecreationalPlace(world, tileMap, rect);
        }
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
            velX /= 1.5f;
            velY /= 1.5f;
        }
        if(!isPaused) character.b2body.setLinearVelocity(velX, velY);
        else character.b2body.setLinearVelocity(0, 0);

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isPaused = !isPaused;
        }

        // interaction
        if(Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            // uncomment if you want to check the screen changing functionality
//            game.setPiazzaScreen();
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
        float mapWidthInMeters = mapWidthInPixels * TileMap.SCALE;
        float mapHeightInMeters = mapHeightInPixels * TileMap.SCALE;
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

        // render the Box2DDebugLines, uncomment it if you want to see green collision lines
//        b2dr.render(world, gameCam.combined);

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

        if(isPaused) {
            this.pauseMenu = new PauseMenu(this);
            this.pauseMenu.stage.draw();
        }
        // ends the drawing session
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) { gamePort.update(width,height); }

    @Override
    public void pause() {

    }

    public void pauseGame() {
        this.isPaused = !this.isPaused;
    }

    public void changeScene() {
        if (game.getScreen() == game.screens.get(0)) { game.setCurrentScreen(1); }
        else { game.setCurrentScreen(0); }
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
