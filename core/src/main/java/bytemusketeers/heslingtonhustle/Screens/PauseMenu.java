package main.java.bytemusketeers.heslingtonhustle.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import main.java.bytemusketeers.heslingtonhustle.HeslingtonHustle;

import java.util.List;

public class PauseMenu extends Menu {

    Stage stage;
    Skin skin;
    TextureAtlas textureAtlas;
    Boolean isPressed = false;
    Boolean isPaused;
    PlayScreen playscreen;


    public PauseMenu(PlayScreen playscreen) {
        this.playscreen = playscreen;
        this.create();
    }

    @Override
    public void create() {
        this.stage = new Stage();
        stage.clear();
        Gdx.input.setInputProcessor(stage);
        this.skin = new Skin();
        this.textureAtlas = new TextureAtlas("buttons.pack");
        skin.addRegions(textureAtlas);
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = skin.getDrawable("button1");
        style.down = skin.getDrawable("button1");
        style.font = new BitmapFont(Gdx.files.internal("exo-medium.fnt"),false);

        TextButton resumeButton = new TextButton("Continue", style);
        TextButton exitButton = new TextButton("Quit", style);
        super.buttons.add(exitButton);
        super.buttons.add(resumeButton);
        //TODO modify/rerender button when window is rescaled
        resumeButton.setPosition(HeslingtonHustle.W_WIDTH / 2 - ((BUTTON_WIDTH + 70.0f) / 2), HeslingtonHustle.W_HEIGHT / 2);
        exitButton.setPosition(HeslingtonHustle.W_WIDTH / 2 - (BUTTON_WIDTH / 2), 100);
        resumeButton.setHeight(BUTTON_HEIGHT);
        resumeButton.setWidth(BUTTON_WIDTH + 70.0f); //needs to be wider to fit the text
        exitButton.setHeight(BUTTON_HEIGHT);
        exitButton.setWidth(BUTTON_WIDTH);

        exitButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.exit(1); //Quits the game (kills the process)
                return true;
            }
        });
        resumeButton.addListener(new InputListener() {
           //this is the even of when the button is pressed
           //it removes the current stage (the Pause Menu) from all the stages that need to be rendered
           //this currently means that there are no stages in the stages variable which is the default state
           public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
               playscreen.pauseMenu();
               return true;
           }
        });

        for(TextButton button : this.buttons) {
            stage.addActor(button);
        }
    }

    @Override
    public void render() {
        super.render();
        stage.draw();
    }

    @Override
    public void dispose() {
        this.skin.dispose();
        this.textureAtlas.dispose();
        this.stage.dispose();
    }
}
