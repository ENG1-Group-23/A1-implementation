package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Item extends Interactable {

    private boolean isHidden = false;

    public Item(Vector2 position, Texture texture) {
        super.position = position;
        super.texture = texture;
        super.spriteBatch = new SpriteBatch();
    }

    @Override
    public void draw() {
        this.spriteBatch.begin();
        if(!this.isHidden) {
            this.spriteBatch.draw(this.texture, this.position.x, this.position.y);
        }
        this.spriteBatch.end();
    }

    @Override
    public void interact() {
        toggleHide();
    }

    public void toggleHide() {
        this.isHidden = !this.isHidden;
    }
}
