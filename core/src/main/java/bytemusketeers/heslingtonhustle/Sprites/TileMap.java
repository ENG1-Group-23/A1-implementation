package main.java.bytemusketeers.heslingtonhustle.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class TileMap {

    private TiledMap tiledMap;

    public TileMap() {

    }

    public OrthogonalTiledMapRenderer setupMap() {
        tiledMap = new TmxMapLoader().load("Maps/test-map.tmx");
        return new OrthogonalTiledMapRenderer(tiledMap);
    }
}
