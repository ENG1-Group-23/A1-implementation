package main.java.bytemusketeers.heslingtonhustle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Interactable extends Item {

    private boolean isHidden = false;

    public Interactable(Vector2 position, Texture texture) {
        this.setPosition(position.x, position.y); //for sprite rendering
        super.texture = texture;
    }

    @Override
    public void interact() {
        toggleHide();
    }

    public void toggleHide() {
        this.isHidden = !this.isHidden;
    }

    public boolean isHidden() {
        return this.isHidden;
    }

    public void dispose() {
        this.getTexture().dispose();
    }
}
