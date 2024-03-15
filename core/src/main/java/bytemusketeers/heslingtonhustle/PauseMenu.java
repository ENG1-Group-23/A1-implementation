package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * The {@link PauseMenu} provides a pleasant {@link Overlay} to display upon a pause being requested, or the
 * {@link PlayScreen} losing focus
 */
class PauseMenu extends Overlay {
    /**
     * Creates a new {@link PauseMenu} relating to the given {@link SpriteBatch}
     *
     * @param batch The {@link SpriteBatch} to which the {@link PauseMenu} should be connected
     */
    public PauseMenu(SpriteBatch batch, PlayScreen playScreen) {
        super(batch);

        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin();
        TextureAtlas textureAtlas = new TextureAtlas("buttons.pack");

        skin.addRegions(textureAtlas);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = skin.getDrawable("button1");
        buttonStyle.down = skin.getDrawable("button1");
        buttonStyle.font = new BitmapFont();

        final Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        final Table table = new Table();

        final TextButton toTitleButton = new TextButton("Main menu", buttonStyle);

        toTitleButton.addListener(new InputListener() {

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                playScreen.toggleMainMenu();
                return true;
            }

        });

        table.setFillParent(true);
        table.add(new Label("GAME PAUSED", labelStyle)).padBottom(GENERAL_PADDING);
        table.row();
        table.add(new Label("Press Escape to Resume Gameplay", labelStyle));
        table.row();
        table.add(new Label("Press Q to Quit", labelStyle));
        table.row();
        table.add(toTitleButton).width(200).height(70).bottom();

        super.addActor(table);
    }
}
