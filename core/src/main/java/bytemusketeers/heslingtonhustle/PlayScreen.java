package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import main.java.bytemusketeers.heslingtonhustle.Sprites.Character;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link PlayScreen} class represents a screen which is shown after the game starts, implementing {@link Screen}
 * interface. It manages the various game cameras, the {@link Character}, and the {@link Area} vector which the player
 * can explore.
 */
public class PlayScreen implements Screen {
    private final OrthographicCamera gameCam;
    private final HeslingtonHustle gameReference;
    private final Viewport gamePort;
    private final World world;
    private Character character;
    private final List<Area> areas = new ArrayList<>();
    private Area activeArea;

    public PlayScreen(HeslingtonHustle gameReference){
        this.gameReference = gameReference;

        areas.add(new Area("Maps/test-map.tmx"));
        activeArea = areas.get(0);

        gameCam = new OrthographicCamera();
        gamePort = new StretchViewport(HeslingtonHustle.W_WIDTH / HeslingtonHustle.PPM,
            HeslingtonHustle.W_HEIGHT / HeslingtonHustle.PPM, gameCam);

        world = new World(new Vector2(0, 0), true);
    }

    @Override
    public void show() {
        gameCam.position.set(gamePort.getWorldWidth() / 2, (float) gamePort.getScreenHeight() / 2, 0);
        character = new Character(world, new Vector2(HeslingtonHustle.W_WIDTH / 2 / HeslingtonHustle.PPM,
            HeslingtonHustle.W_HEIGHT / 2 / HeslingtonHustle.PPM));
    }

    /**
     * Handles inputs
     */
    public void handleInput(){
        if(Gdx.input.isKeyPressed(Input.Keys.W))
            character.moveUp();

        if(Gdx.input.isKeyPressed(Input.Keys.S))
            character.moveDown();

        if(Gdx.input.isKeyPressed(Input.Keys.A))
            character.moveLeft();

        if(Gdx.input.isKeyPressed(Input.Keys.D))
            character.moveRight();
    }

    /**
     * Handle the game logic and updates the state of the game world
     */
    public void update(){
        handleInput();
        character.move();

        // calculates physics interactions, such as object movement, collisions, and forces, for a specific time interval
        world.step(1/60f, 6, 2);
        MapProperties props = activeArea.getAreaMapProperties();
        int mapWidthInTiles = props.get("width", Integer.class);
        int mapHeightInTiles = props.get("height", Integer.class);

        int tileWidth = props.get("tilewidth", Integer.class);
        int tileHeight = props.get("tileheight", Integer.class);

        int mapWidthInPixels = mapWidthInTiles * tileWidth;
        int mapHeightInPixels = mapHeightInTiles * tileHeight;
        float mapWidthInMeters = mapWidthInPixels * Area.MAP_SCALE;
        float mapHeightInMeters = mapHeightInPixels * Area.MAP_SCALE;

        if (character.b2body.getPosition().x >= HeslingtonHustle.WIDTH_METRES_BOUND &&
                character.b2body.getPosition().x <= mapWidthInMeters - HeslingtonHustle.WIDTH_METRES_BOUND)
            gameCam.position.x = character.b2body.getPosition().x;

        if (character.b2body.getPosition().y >= HeslingtonHustle.HEIGHT_METRES_BOUND &&
                character.b2body.getPosition().y <= mapHeightInMeters - HeslingtonHustle.HEIGHT_METRES_BOUND)
            gameCam.position.y= character.b2body.getPosition().y;

        // tracking character's moves with the cam
        activeArea.updateView(gameCam);
        gameCam.update();
    }

    @Override
    public void render(float delta) {
        update();
        // sets the color specified when the color buffer is cleared
        Gdx.gl.glClearColor(1, 0, 0, 1);
        // GL_COLOR_BUFFER_BIT specifies that the color buffer is to be cleared
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        activeArea.render();

        gameReference.batch.setProjectionMatrix(gameCam.combined);
        gameReference.batch.begin();
        gameReference.batch.draw(character.playerTexture, character.b2body.getPosition().x - Character.WIDTH / 2,
            character.b2body.getPosition().y - Character.HEIGHT / 2, Character.WIDTH, Character.HEIGHT);
        gameReference.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {
        System.out.println("PlayScreen paused");
    }

    @Override
    public void resume() {
        System.out.println("PlayScreen resumed");
    }

    @Override
    public void hide() {
        System.out.println("PlayScreen hidden");
    }

    @Override
    public void dispose() {
        character.playerTexture.dispose();
        world.dispose();

        // TODO: do we need to dispose the area (and hence all interactables) here?
    }
}
