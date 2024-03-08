package main.java.bytemusketeers.heslingtonhustle.Sprites;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class TileMap {

    private TiledMap tiledMap;
    private float scale;

    public TileMap() {

    }

    public float getScale (){ return this.scale; }
    public MapProperties getProperties() {
        return tiledMap.getProperties();
    }
    public OrthogonalTiledMapRenderer setupMap() {
        tiledMap = new TmxMapLoader().load("Maps/test-map.tmx");
        this.scale = 0.05f;
        return new OrthogonalTiledMapRenderer(tiledMap, this.scale);
    }
}
