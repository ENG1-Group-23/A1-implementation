package bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.EnumMap;
import java.util.Map;

/**
 * The {@link PlayScreen} class represents a screen which is shown after the game starts, implementing {@link Screen}
 * interface. It manages the various game cameras, the {@link Character}, and the {@link Area} vector which the player
 * can explore. All assets for the game are preloaded when {@link PlayScreen} is constructed,
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
    private final Viewport gamePort;

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
     * @see HeadsUpDisplay#updateMetricElement(MetricManager.Metric, String)
     */
    private final MetricManager metricManager = new MetricManager(new Runnable() {
        /**
         * Update the HUD with the last-updated metric from {@link MetricManager}
         */
        @Override
        public void run() {
            MetricManager.Metric updated = metricManager.getLastChangedMetric();
            hud.updateMetricElement(updated, metricManager.getMetricValue(updated).toString());
        }
    });

    /**
     * Indicates the game-state; currently a binary selector of 'paused' or 'not paused'; intended to inform the
     * input-handler and update cycles
     *
     * @see #handleInput()
     * @see #update()
     */
    private boolean paused = false;

    /**
     * Initialise some sample areas into the {@link PlayScreen}
     */
    private void initialiseAreas() {
        AreaFactory factory = new AreaFactory(metricManager);

        areas.put(Area.AreaName.TestMap, factory.createTestMap());
        areas.put(Area.AreaName.PiazzaBuilding, factory.createPiazzaMap());
        areas.put(Area.AreaName.CompSciBuilding, factory.createCSMap());
        areas.put(Area.AreaName.BedroomBuilding, factory.createBedroomMap());
    }

    /**
     * Handles user system events, such as key-presses
     */
    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q))
            Gdx.app.exit();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            paused = !paused;

        if (paused)
            return;

        if(Gdx.input.isKeyPressed(Input.Keys.W))
            character.moveUp();

        if(Gdx.input.isKeyPressed(Input.Keys.S))
            character.moveDown();

        if(Gdx.input.isKeyPressed(Input.Keys.A))
            character.moveLeft();

        if(Gdx.input.isKeyPressed(Input.Keys.D))
            character.moveRight();

        if(Gdx.input.isKeyJustPressed(Input.Keys.E))
            if(!activeArea.triggerInteractables(character.getPosition()))
                // TODO: If no interactions were triggered, switch to Piazza. Just a temporary test.
                switchArea(Area.AreaName.PiazzaBuilding);

        if(Gdx.input.isKeyJustPressed(Input.Keys.M))
            switchArea(Area.AreaName.CompSciBuilding);

        if(Gdx.input.isKeyJustPressed(Input.Keys.N))
            switchArea(Area.AreaName.BedroomBuilding);

        if(Gdx.input.isKeyJustPressed(Input.Keys.B))
            switchArea(Area.AreaName.TestMap);
    }

    /**
     * Handles the game logic and updates the state of the game world
     */
    public void update() {
        // Configure the collision-detection parameters in the game world
        activeArea.step();

        // Handle movement and update the character velocities and position accordingly
        character.move();

        // Update the game camera position, such that the character is followed and centralised, unless close to a
        // viewport boundary
        Vector2 characterPosition = character.getPosition();

        if (character.isWithinHorizontalBound(activeArea))
            gameCam.position.x = characterPosition.x;

        if (character.isWithinVerticalBound(activeArea))
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
        paused = true;
    }

    /**
     * Dual of the {@link #pause()}, this handles the {@link PlayScreen}, and hence general gameplay execution, being
     * resumed; currently a placeholder
     */
    @Override
    public void resume() {
        System.out.println("PlayScreen resumed");
        paused = false;
    }

    /**
     * Switch to the {@link Area} identified by the given index
     *
     * @param areaName The {@link Area.AreaName} of the new {@link Area}
     * @see Area
     */
    private void switchArea(@SuppressWarnings("SameParameterValue") Area.AreaName areaName) {
        // Switch the active area render target and inform the character of its body context change
        activeArea = areas.get(areaName);
        character.switchCharacterContext(areaName);
        character.setPosition(activeArea.getInitialCharacterPosition());

        // Update the game camera, bounding as usual
        Vector3 newCameraPosition = new Vector3(activeArea.getInitialCharacterPosition(), 0);

        if (!character.isWithinHorizontalBound(activeArea))
            newCameraPosition.x = HeslingtonHustle.WIDTH_METRES_BOUND;

        if (!character.isWithinVerticalBound(activeArea))
            newCameraPosition.y = HeslingtonHustle.HEIGHT_METRES_BOUND;

        gameCam.position.set(newCameraPosition);
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
     * @param delta The time in seconds since the last render; not currently used
     * @see Area
     * @see Character
     * @see HeadsUpDisplay
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
     */
    public PlayScreen(SpriteBatch batch) {
        this.batch = batch;
        gameCam = new OrthographicCamera();
        gamePort = new StretchViewport(Gdx.graphics.getWidth() / HeslingtonHustle.PPM,
            Gdx.graphics.getHeight() / HeslingtonHustle.PPM, gameCam);
        hud = new HeadsUpDisplay(batch);
        pauseMenu = new PauseMenu(batch);

        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        initialiseAreas();
        activeArea = areas.get(Area.AreaName.TestMap);
        character = new Character(areas, Area.AreaName.TestMap);
    }
}
