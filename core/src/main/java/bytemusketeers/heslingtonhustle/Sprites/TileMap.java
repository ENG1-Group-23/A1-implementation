package main.java.bytemusketeers.heslingtonhustle.Sprites;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class TileMap {
    private TiledMap tiledMap;
    private float scale;

    public float getScale () {
        return this.scale;
    }

    public MapProperties getProperties() {
        return tiledMap.getProperties();
    }

    public TiledMap getTiledMap() {
        return this.tiledMap;
    }

    public OrthogonalTiledMapRenderer setupMap(String map) {
        tiledMap = new TmxMapLoader().load(map);
        this.scale = 0.04f;
        return new OrthogonalTiledMapRenderer(tiledMap, this.scale);
    }

    public MapObjects getObjectLayers(String layerName) {
        MapLayer mapLayer = tiledMap.getLayers().get(layerName);
        if (mapLayer != null) {
            return mapLayer.getObjects();
        }
        return null;
    }



}
