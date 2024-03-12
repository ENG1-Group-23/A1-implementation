package main.java.bytemusketeers.heslingtonhustle.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.ArrayList;
import java.util.List;

abstract public class Menu extends Game {

    protected final float BUTTON_WIDTH = 150.0f;
    protected final float BUTTON_HEIGHT = 60.0f;
    protected List<TextButton> buttons = new ArrayList<TextButton>();
    protected List<TextArea> textAreas = new ArrayList<>();
}
