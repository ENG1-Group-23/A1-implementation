package bytemusketeers.heslingtonhustle;

import bytemusketeers.heslingtonhustle.metrics.MetricController;
import bytemusketeers.heslingtonhustle.metrics.MetricListener;
import bytemusketeers.heslingtonhustle.metrics.MetricUpdater;
import bytemusketeers.heslingtonhustle.scene.Area;
import bytemusketeers.heslingtonhustle.scene.AreaFactory;
import bytemusketeers.heslingtonhustle.scene.Character;
import bytemusketeers.heslingtonhustle.scene.InvalidAreaException;
import bytemusketeers.heslingtonhustle.ui.Overlay;
import bytemusketeers.heslingtonhustle.ui.OverlayFactory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.EnumMap;
import java.util.Map;

/**
 * The {@link PlayScreen} class represents a screen which is shown after the game starts, implementing {@link Screen}
 * interface. It manages the various game cameras, the {@link Character}, and the {@link Area} vector which the player
 * can explore. All assets for the game are preloaded when {@link PlayScreen} is constructed.
 *
 * @author ENG1 Team 23 (Cohort 3)
 */
public class PlayScreen implements Screen {
    /**
     * Distinguish between internal game states; used to inform, among others, the {@link #render(float)}} and
     * {@link #handleInput()} cycles
     */
    private enum GameState {
        GAME_PLAYING, GAME_PAUSED, GAME_OVER
    }

    /**
     * Stores the transient {@link GameState} being experienced by the player in the {@link PlayScreen}
     */
    private GameState state = GameState.GAME_PLAYING;

    /**
     * The {@link OrthographicCamera} associated with the parental {@link com.badlogic.gdx.Game} instance
     */
    private final OrthographicCamera gameCam;

    /**
     * The {@link SpriteBatch} associated with the parental {@link com.badlogic.gdx.Game} instance
     */
    private final SpriteBatch batch;

    /**
     * The render {@link Viewport} associated with the parental {@link com.badlogic.gdx.Game}
     */
    private final Viewport viewport;

    /**
     * The player-controlled {@link com.badlogic.gdx.graphics.g2d.Sprite} that is tracked by the game camera
     *
     * @see Character
     * @see OrthographicCamera
     */
    private final Character character;

    /**
     * The persistent heads-up display {@link Overlay} presenting real-time metric information to the player
     *
     * @see MetricController
     * @see MetricController.Metric
     */
    private final Overlay hud;

    /**
     * The transient-rendered pause menu {@link Overlay}, intended to be displayed to the user upon request,
     * or a system event such as loss of {@link PlayScreen} focus
     *
     * @see GameState#GAME_PAUSED
     */
    private final Overlay pauseMenu;

    /**
     * The {@link Overlay} rendered upon game completion
     *
     * @see GameState#GAME_OVER
     */
    private final Overlay gameOverMenu;

    /**
     * The relationship between {@link Area} and the {@link Area.Name}
     *
     * @see Area
     * @see Area.Name
     */
    private final Map<Area.Name, Area> areas = new EnumMap<>(Area.Name.class);

    /**
     * The {@link Area} subject to world collision, interaction, and rendering
     *
     * @see Area#step()
     * @see #update()
     * @see #render(float)
     */
    private Area activeArea;

    /**
     * The {@link MetricController} stores and provides means of manipulating and updating
     * {@link MetricController.Metric} values
     *
     * @see MetricListener
     * @see MetricUpdater
     */
    private final MetricController metricController;

    /**
     * Initialise the play {@link Area}s
     *
     * @see Area
     * @see AreaFactory
     */
    private void initialiseAreas() throws InvalidAreaException {
        AreaFactory factory = new AreaFactory(metricController, this);

        areas.put(Area.Name.TestMap, factory.createTestMap());
        areas.put(Area.Name.PiazzaBuilding, factory.createPiazzaMap());
        areas.put(Area.Name.CompSciBuilding, factory.createCSMap());
        areas.put(Area.Name.BedroomBuilding, factory.createBedroomMap());
    }

    /**
     * Handles user system events, such as key-presses.
     *
     * @implNote In the future, this implementation should be replaced by an implementation of
     *           {@link com.badlogic.gdx.InputProcessor} or an extension of {@link com.badlogic.gdx.InputAdapter}. In
     *           that case, {@link Input.Keys} could be switched over by overrides of
     *           {@link com.badlogic.gdx.InputProcessor#keyDown(int)},
     *           {@link com.badlogic.gdx.InputProcessor#keyUp(int)},
     *           {@link com.badlogic.gdx.InputProcessor#keyTyped(char)}, etc. This may be chained with a
     *           {@link com.badlogic.gdx.InputMultiplexer} to account for differing, per-{@link Screen} input schemas.
     *
     * @implNote In such an implementation as above, the key-bindings could be separated out into a distinct enum or
     *           class, in which eventuality, the constructor would match a {@link Input.Keys} with corresponding
     *           {@link Runnable} from the context of the {@link PlayScreen}. This would avoid the "magic keys" as seen
     *           here, and also allow for runtime re-bindings of key combinations with their respective {@link Runnable}
     *           action.
     */
    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q))
            Gdx.app.exit();

        if (state != GameState.GAME_OVER) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
                togglePaused();

            if (Gdx.input.isKeyPressed(Input.Keys.W)) character.moveUp();
            if (Gdx.input.isKeyPressed(Input.Keys.S)) character.moveDown();
            if (Gdx.input.isKeyPressed(Input.Keys.A)) character.moveLeft();
            if (Gdx.input.isKeyPressed(Input.Keys.D)) character.moveRight();

            if (Gdx.input.isKeyJustPressed(Input.Keys.E))
                activeArea.triggerInteractables(character.getPosition());

            // TODO: just a few temporary tests for switching scene
            if (Gdx.input.isKeyJustPressed(Input.Keys.P)) switchArea(Area.Name.PiazzaBuilding);
            if (Gdx.input.isKeyJustPressed(Input.Keys.C)) switchArea(Area.Name.CompSciBuilding);
            if (Gdx.input.isKeyJustPressed(Input.Keys.B)) switchArea(Area.Name.BedroomBuilding);
            if (Gdx.input.isKeyJustPressed(Input.Keys.T)) switchArea(Area.Name.TestMap);
        }
    }

    /**
     * Handles the game logic and updates the state of the game world
     */
    private void update() {
        final float horizontalGutter = viewport.getWorldWidth() / 2;
        final float verticalGutter = viewport.getWorldHeight() / 2;

        // Configure the collision-detection parameters in the game world
        activeArea.step();

        // Handle movement and update the character velocities and position accordingly
        character.move();

        // Update the game camera position, such that the character is followed, unless close to a map boundary
        gameCam.position.set(activeArea.bound(character.getPosition(), horizontalGutter, verticalGutter), 0);
        gameCam.update();

        // Update the viewport boundaries with the game camera information
        activeArea.updateView(gameCam);
    }

    /**
     * Handles the {@link PlayScreen} being resized
     *
     * @param width The new width, in pixels
     * @param height The new height, in pixels
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    /**
     * Handles the {@link PlayScreen} becoming the active {@link Screen}; currently a placeholder
     *
     * @see #hide()
     */
    @Override
    public void show() {
        System.out.println("PlayScreen shown");
    }

    /**
     * Handles the {@link PlayScreen} being hidden
     *
     * @see #show()
     */
    @Override
    public void hide() {
        System.out.println("PlayScreen hidden");
    }

    /**
     * Handles the {@link PlayScreen}, and hence general gameplay execution, being paused
     *
     * @see #resume()
     * @see #pauseMenu
     */
    @Override
    public void pause() {
        togglePaused();
    }

    /**
     * Handles the {@link PlayScreen}, and hence general gameplay execution, being resumed
     *
     * @see #pause()
     */
    @Override
    public void resume() {
        togglePaused();
    }

    /**
     * Toggle the {@link #state} such that the game is paused or unpaused, unless the game is over
     *
     * @see GameState
     */
    private void togglePaused() {
        if (state == GameState.GAME_PLAYING)
            state = GameState.GAME_PAUSED;
        else if (state == GameState.GAME_PAUSED)
            state = GameState.GAME_PLAYING;
    }

    /**
     * Switch to the {@link Area} identified by the given {@link Area.Name} key
     *
     * @param areaName The {@link Area.Name} of the new {@link Area}
     * @see Area
     */
    private void switchArea(Area.Name areaName) {
        // Switch the active area render target and inform the character of its body context change
        activeArea = areas.get(areaName);
        metricController.changeAreaMetric(areaName);
        character.switchCharacterContext(areaName);
        character.setPosition(activeArea.getInitialCharacterPosition());

        // Update the game camera; bounding on map edges will be performed during the render cycle
        gameCam.position.set(activeArea.getInitialCharacterPosition(), 0);
        gameCam.update();
    }

    /**
     * Advances the day, or finishes the game as appropriate
     *
     * @see MetricController#advanceDOWMetric()
     * @see GameState
     */
    public void advanceDay() {
        if (metricController.isFinalDay())
            state = GameState.GAME_OVER;
        else
            metricController.advanceDOWMetric();
    }

    /**
     * Releases all resources used by the {@link PlayScreen}
     */
    @Override
    public void dispose() {
        for (Area area : areas.values())
            area.dispose();

        character.dispose();
        hud.dispose();
    }

    /**
     * Handles the graphical rendering obligations of the {@link Screen}. In particular, this involves rendering all
     * visible objects including the {@link Area} and all {@link bytemusketeers.heslingtonhustle.scene.Drawable}
     * elements therein {@link com.badlogic.gdx.maps.tiled.TiledMap}---, the {@link Character}, and various
     * {@link Overlay} users.
     *
     * @param delta The time in seconds since the last render
     * @see Area
     * @see Character
     * @implNote In the future, the time-delta parameter should be used for consistent cross-platform rendering and
     *           input-handling; see {@link #handleInput()}.
     */
    @Override
    public void render(float delta) {
        handleInput();
        update();

        batch.setProjectionMatrix(gameCam.combined);
        batch.begin();
        activeArea.render(batch);
        character.render(batch);
        batch.end();
        hud.render(batch);

        if (state == GameState.GAME_PAUSED)
            pauseMenu.render(batch);
        else if (state == GameState.GAME_OVER)
            gameOverMenu.render(batch);
    }

    /**
     * Instantiates a new {@link PlayScreen} to be used for the playing area of the game. This constructor loads all
     * assets for all stages of the playing area, to be manipulated and rendered when required.
     *
     * @param batch The LibGDX {@link SpriteBatch} to use for batch object rendering
     * @throws InvalidAreaException At least one {@link Area} required by the {@link PlayScreen} could not be properly
     *                              instantiated by the {@link AreaFactory}
     */
    PlayScreen(SpriteBatch batch) throws InvalidAreaException {
        // LibGDX core components
        this.batch = batch;
        gameCam = new OrthographicCamera();
        viewport = new StretchViewport(HeslingtonHustle.scaleToMetres(Gdx.graphics.getWidth()),
            HeslingtonHustle.scaleToMetres(Gdx.graphics.getHeight()), gameCam);
        gameCam.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        // UI elements
        OverlayFactory overlayFactory = new OverlayFactory(batch);
        pauseMenu = overlayFactory.createPauseMenu();
        gameOverMenu = overlayFactory.createGameOverMenu();

        // Create the HUD and prepare it to receive metrics of the given keys
        hud = overlayFactory.createHUD(
            new MetricController.Metric[]{
                MetricController.Metric.Area,
                MetricController.Metric.Day
            },
            new MetricController.Metric[]{
                MetricController.Metric.Sleep,
                MetricController.Metric.Study,
                MetricController.Metric.Eat,
                MetricController.Metric.Play
            }
        );

        // Create the controller assigned with the standard updater, thus linking the controller and the HUD
        final MetricUpdater metricUpdater = new MetricUpdater((MetricListener) hud);
        metricController = new MetricController(metricUpdater);

        // Initialise final-stage gameplay elements
        initialiseAreas();
        character = new Character(areas);
        switchArea(Area.Name.TestMap);

        // Send an initial pulse of each established metric to the updater
        for (MetricController.Metric metric : MetricController.Metric.values())
            metricUpdater.sendUpdate(metric, metricController.getMetricStringValue(metric));
    }
}
