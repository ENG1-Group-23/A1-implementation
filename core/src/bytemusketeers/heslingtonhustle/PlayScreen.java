package bytemusketeers.heslingtonhustle;

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
class PlayScreen implements Screen {
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
     * The persistent {@link HeadsUpDisplay} {@link Overlay} presenting real-time metric information to the player
     *
     * @see MetricManager
     * @see MetricManager.Metric
     */
    private final HeadsUpDisplay hud;

    /**
     * The transient-rendered {@link PauseMenu} {@link Overlay}, intended to be displayed to the user upon request,
     * or a system event such as loss of {@link PlayScreen} focus
     */
    private final PauseMenu pauseMenu;

    /**
     * The relationship between {@link Area} and the {@link Area.AreaName}
     *
     * @see Area
     * @see Area.AreaName
     */
    private final Map<Area.AreaName, Area> areas = new EnumMap<>(Area.AreaName.class);

    /**
     * The {@link Area} subject to world collision, interaction, and rendering
     *
     * @see Area#step()
     * @see #update()
     * @see #render(float)
     */
    private Area activeArea;

    /**
     * The controller for collating, managing, and reporting {@link MetricManager.Metric} information, principally
     * passing real-time information to {@link Overlay} components.
     *
     * @implNote To make the {@link MetricManager} useful in an event-driven context, see
     *           {@link MetricManager#assignUpdater(Runnable)}, and in particular {@link MetricUpdater}.
     */
    private final MetricManager metricManager = new MetricManager();

    /**
     * Indicates the game-state; currently a binary selector of 'paused' or 'not paused'; intended to inform the
     * input-handler and update cycles
     *
     * @see #handleInput()
     * @see #update()
     */
    private boolean paused = false;

    /**
     * Initialise the play {@link Area}s
     *
     * @see Area
     * @see AreaFactory
     */
    private void initialiseAreas() throws InvalidAreaException {
        AreaFactory factory = new AreaFactory(metricManager);

        areas.put(Area.AreaName.TestMap, factory.createTestMap());
        areas.put(Area.AreaName.PiazzaBuilding, factory.createPiazzaMap());
        areas.put(Area.AreaName.CompSciBuilding, factory.createCSMap());
        areas.put(Area.AreaName.BedroomBuilding, factory.createBedroomMap());
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q))      Gdx.app.exit();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) paused = !paused;

        if (!paused) {
            if (Gdx.input.isKeyPressed(Input.Keys.W)) character.moveUp();
            if (Gdx.input.isKeyPressed(Input.Keys.S)) character.moveDown();
            if (Gdx.input.isKeyPressed(Input.Keys.A)) character.moveLeft();
            if (Gdx.input.isKeyPressed(Input.Keys.D)) character.moveRight();

            if (Gdx.input.isKeyJustPressed(Input.Keys.E))
                activeArea.triggerInteractables(character.getPosition());

            // TODO: just a few temporary tests for switching areas
            if (Gdx.input.isKeyJustPressed(Input.Keys.P)) switchArea(Area.AreaName.PiazzaBuilding);
            if (Gdx.input.isKeyJustPressed(Input.Keys.C)) switchArea(Area.AreaName.CompSciBuilding);
            if (Gdx.input.isKeyJustPressed(Input.Keys.B)) switchArea(Area.AreaName.BedroomBuilding);
            if (Gdx.input.isKeyJustPressed(Input.Keys.T)) switchArea(Area.AreaName.TestMap);
        }
    }

    /**
     * Handles the game logic and updates the state of the game world
     */
    public void update() {
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
        paused = true;
    }

    /**
     * Handles the {@link PlayScreen}, and hence general gameplay execution, being resumed
     *
     * @see #pause()
     */
    @Override
    public void resume() {
        paused = false;
    }

    /**
     * Switch to the {@link Area} identified by the given {@link Area.AreaName} key
     *
     * @param areaName The {@link Area.AreaName} of the new {@link Area}
     * @see Area
     */
    private void switchArea(Area.AreaName areaName) {
        // Switch the active area render target and inform the character of its body context change
        activeArea = areas.get(areaName);
        metricManager.setMetric(MetricManager.Metric.Area, areaName.ordinal());
        character.switchCharacterContext(areaName);
        character.setPosition(activeArea.getInitialCharacterPosition());

        // Update the game camera; bounding on map edges will be performed during the render cycle
        gameCam.position.set(activeArea.getInitialCharacterPosition(), 0);
        gameCam.update();
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
     * visible objects including the {@link Area}---and hence all {@link Interactable} elements on the
     * {@link com.badlogic.gdx.maps.tiled.TiledMap}---, the {@link Character}, and the {@link HeadsUpDisplay}.
     *
     * @param delta The time in seconds since the last render
     * @see Area
     * @see Character
     * @see HeadsUpDisplay
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

        if (paused)
            pauseMenu.render(batch);
    }

    /**
     * Instantiates a new {@link PlayScreen} to be used for the playing area of the game. This constructor loads all
     * assets for all stages of the playing area, to be manipulated and rendered when required.
     *
     * @param batch The LibGDX {@link SpriteBatch} to use for batch object rendering
     * @throws InvalidAreaException At least one {@link Area} required by the {@link PlayScreen} could not be properly
     *                              instantiated by the {@link AreaFactory}
     */
    public PlayScreen(SpriteBatch batch) throws InvalidAreaException {
        // LibGDX core components setup
        this.batch = batch;
        gameCam = new OrthographicCamera();
        viewport = new StretchViewport(HeslingtonHustle.scaleToMetres(Gdx.graphics.getWidth()),
            HeslingtonHustle.scaleToMetres(Gdx.graphics.getHeight()), gameCam);
        gameCam.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        // UI elements
        hud = new HeadsUpDisplay(batch,
            new MetricManager.Metric[] {
                MetricManager.Metric.Area,
                MetricManager.Metric.Day
            },
            new MetricManager.Metric[] {
                MetricManager.Metric.Sleep,
                MetricManager.Metric.Study,
                MetricManager.Metric.Eat,
                MetricManager.Metric.Play
            }
        );

        metricManager.assignUpdater(new MetricUpdater(metricManager, hud));
        pauseMenu = new PauseMenu(batch);

        // Gameplay elements
        initialiseAreas();
        activeArea = areas.get(Area.AreaName.TestMap);
        character = new Character(areas, Area.AreaName.TestMap);
    }
}
