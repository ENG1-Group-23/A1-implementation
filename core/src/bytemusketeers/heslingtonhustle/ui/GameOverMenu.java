package bytemusketeers.heslingtonhustle.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * The {@link GameOverMenu} should display once the final day has been reached
 *
 * @author ENG1 Team 23 (Cohort 3)
 */
class GameOverMenu extends Overlay {
    /**
     * Creates a new {@link GameOverMenu} relating to the given {@link SpriteBatch}
     *
     * @param batch The {@link SpriteBatch} to which the {@link GameOverMenu} should be connected
     */
    GameOverMenu(SpriteBatch batch) {
        super(batch);

        final Label.LabelStyle labelStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        final Table table = new Table();

        table.setFillParent(true);
        table.add(new Label("GAME OVER", labelStyle)).padBottom(GENERAL_FORM_PADDING);
        table.row();
        table.add(new Label("Press Q to Quit", labelStyle));

        super.addActor(table);
    }
}
