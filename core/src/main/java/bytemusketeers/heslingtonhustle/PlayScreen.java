package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link PlayScreen} class represents a screen which is shown after the game starts, implementing {@link Screen}
 * interface. It manages the various game cameras, the {@link Character}, and the {@link Area} vector which the player
 * can explore. All assets for the game are preloaded when {@link PlayScreen} is constructed,
 */
class PlayScreen implements Screen {
    private final OrthographicCamera gameCam;
    private final SpriteBatch batch;
    private final Viewport gamePort;
    private final Character character;
    private final List<Area> areas = new ArrayList<>();
    private Area activeArea; // TODO: should be non-final

    /**
     * Initialise some sample areas into the {@link PlayScreen}
     */
    private void initialiseAreas() {
        Area area;

        /* Test Map */
        area = new Area("Maps/test-map.tmx");
        area.addInteractable(new Interactable(
            new Vector2(
                MathUtils.random(0, Gdx.graphics.getWidth() / HeslingtonHustle.PPM),
                MathUtils.random(0, Gdx.graphics.getHeight() / HeslingtonHustle.PPM)),
            new Texture("libgdx.png"),
            area,0.5f, 0.5f, () -> System.out.println("Interacted with the logo!")));
        areas.add(area);

        /* Piazza Map */
        area = new Area("Maps/piazza-map.tmx");
        areas.add(area);
    }

    /**
     * Handles user system events, such as key-presses
     */
    private void handleInput(){
        if(Gdx.input.isKeyPressed(Input.Keys.W))
            character.moveUp();

        if(Gdx.input.isKeyPressed(Input.Keys.S))
            character.moveDown();

        if(Gdx.input.isKeyPressed(Input.Keys.A))
            character.moveLeft();

        if(Gdx.input.isKeyPressed(Input.Keys.D))
            character.moveRight();

        if(Gdx.input.isKeyJustPressed(Input.Keys.E))
            // activeArea.triggerInteractables(character.getPosition());
            switchArea(1);
    }

    /**
     * Handles the game logic and updates the state of the game world
     */
    public void update() {
        // Configure the collision-detection parameters in the game world
        activeArea.step();

        // Handle movement and update the character velocities and position accordingly
        handleInput();
        character.move();

        // Update the game camera position, such that the character is followed and centralised, unless close to a
        // viewport boundary
        Vector2 characterPosition = character.getPosition();

        if (character.isOutOfHorizontalBound(activeArea))
            gameCam.position.x = characterPosition.x;

        if (character.isOutOfVerticalBound(activeArea))
            gameCam.position.y = characterPosition.y;

        activeArea.updateView(gameCam);
        gameCam.update();
    }

    /**
     * Handles the {@link PlayScreen} being resized
     *
     * @param width The new width, in pixels
     * @param height The new height, in pixels
     */
    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        System.out.println("PlayScreen resized");
    }

    /**
     * Handles the {@link PlayScreen} becoming the active {@link Screen}; currently a placeholder
     */
    @Override
    public void show() {
        System.out.println("PlayScreen shown");
    }

    /**
     * Dual of the {@link #show()}, this handles the {@link PlayScreen} being hidden
     */
    @Override
    public void hide() {
        System.out.println("PlayScreen hidden");
    }

    /**
     * Handles the {@link PlayScreen}, and hence general gameplay execution, being paused; currently a placeholder
     */
    @Override
    public void pause() {
        System.out.println("PlayScreen paused");
    }

    /**
     * Dual of the {@link #pause()}, this handles the {@link PlayScreen}, and hence general gameplay execution, being
     * resumed; currently a placeholder
     */
    @Override
    public void resume() {
        System.out.println("PlayScreen resumed");
    }

    /**
     * Switch to the {@link Area} identified by the given index
     *
     * @param areaIdx The index of the new {@link Area}
     */
    private void switchArea(int areaIdx) {
        activeArea = areas.get(areaIdx);
        character.switchCharacterContext(areaIdx);
    }

    /**
     * Releases all resources used by the {@link PlayScreen}
     */
    @Override
    public void dispose() {
        for (Area area : areas)
            area.dispose();

        character.dispose();
        System.out.println("Disposing...");
    }

    /**
     * Handles the graphical rendering obligations of the {@link Screen}. In particular, this involves rendering all
     * visible objects including the {@link Area}---and hence all {@link Interactable} elements on the
     * {@link com.badlogic.gdx.maps.tiled.TiledMap}---, and the {@link Character}.
     *
     * @param delta The time in seconds since the last render; not currently used
     */
    @Override
    public void render(float delta) {
        update();

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(gameCam.combined);
        batch.begin();

        activeArea.render(batch);
        character.render(batch);

        batch.end();
    }

    /**
     * Instantiates a new {@link PlayScreen} to be used for the playing area of the game. This constructor loads all
     * assets for all stages of the playing area, to be manipulated and rendered when required.
     */
    public PlayScreen(SpriteBatch batch) {
        this.batch = batch;
        gameCam = new OrthographicCamera();
        gamePort = new StretchViewport(Gdx.graphics.getWidth() / HeslingtonHustle.PPM,
            Gdx.graphics.getHeight() / HeslingtonHustle.PPM, gameCam);

        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        initialiseAreas();
        activeArea = areas.get(0);

        character = new Character(areas, new Vector2((float) Gdx.graphics.getWidth() / HeslingtonHustle.PPM / 2,
            (float) Gdx.graphics.getHeight() / HeslingtonHustle.PPM / 2), 0);
    }
}
