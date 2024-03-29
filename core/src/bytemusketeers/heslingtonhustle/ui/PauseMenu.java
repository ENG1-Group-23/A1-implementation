package bytemusketeers.heslingtonhustle.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * The {@link PauseMenu} provides a pleasant {@link Overlay} to display upon a pause being requested, or a game
 * {@link com.badlogic.gdx.Screen} losing focus.
 *
 * @author ENG1 Team 23 (Cohort 3)
 */
class PauseMenu extends Overlay {
    /**
     * Creates a new {@link PauseMenu} relating to the given {@link SpriteBatch}
     *
     * @param batch The {@link SpriteBatch} to which the {@link PauseMenu} should be connected
     */
    PauseMenu(SpriteBatch batch) {
        super(batch);

        final Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        final Table table = new Table();

        table.setFillParent(true);
        table.add(new Label("GAME PAUSED", labelStyle)).padBottom(GENERAL_FORM_PADDING);
        table.row();
        table.add(new Label("Press Escape to Resume Gameplay", labelStyle));
        table.row();
        table.add(new Label("Press Q to Quit", labelStyle));

        super.addActor(table);
    }
}
