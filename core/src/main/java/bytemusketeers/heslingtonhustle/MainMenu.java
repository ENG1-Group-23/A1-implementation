package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class MainMenu extends Overlay {

    private TextButton startButton;
    private InputListener il;

    public MainMenu(SpriteBatch batch, PlayScreen playScreen) {
        super(batch);

        Gdx.input.setInputProcessor(super.stage);
        final Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        final Table table = new Table();

        Skin skin = new Skin();
        TextureAtlas textureAtlas = new TextureAtlas("buttons.pack");

        skin.addRegions(textureAtlas);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = skin.getDrawable("button1");
        buttonStyle.down = skin.getDrawable("button1");
        buttonStyle.font = new BitmapFont();

        startButton = new TextButton("Start", buttonStyle);

        InputListener il = new InputListener() {

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                playScreen.toggleMainMenu();
                return true;
            }

        };

        startButton.addListener(il);


        table.setFillParent(true);
        table.row();
        table.add(startButton).width(200).height(70);


        super.addActor(table);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(stage.getCamera().combined);
        batch.begin();
        //TODO: logo.png is NOT FINAL background image for main menu screen
        batch.draw(new Texture("logo.png"), HeslingtonHustle.WIDTH_METRES_BOUND * 100 / 2, HeslingtonHustle.HEIGHT_METRES_BOUND * 100 / 2, HeslingtonHustle.WIDTH_METRES_BOUND * 100, HeslingtonHustle.HEIGHT_METRES_BOUND * 100);
        batch.end();
        super.stage.act();
        super.stage.draw();
    }

    @Override
    public void dispose() {
        if(startButton.getListeners().contains(il, true)) startButton.removeListener(il);
        stage.dispose();
    }
}
