package com.haphest.a3dtracking.navigation.widget;

import android.graphics.Region;

import java.util.List;

/**
 * 货位
 */
public class Location {
    public int x = -1;//货位左上X
    public int y = -1;//货位左上Y
    public int w;//货位宽
    public int h;//货位高
    public int floorCount = 6;//总层高
    public List<Floor> floors;//货位有货的层数
    public Region region;

    public static Location copyLocationWithoutXY(Location location) {
        return new Location(location.w, location.h);
    }

    /**
     * 创建一个有位置的货位
     *
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public Location(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        setRegion(x, y, w, h);
    }

    public void setRegion(int x, int y, int w, int h) {
        region = new Region();
        region.set(x, y, x + w, y + h);
    }

    public boolean containXY(int rawx, int rawy) {
        return region.contains(rawx, rawy);
    }

    /**
     * 创建一个无位置的货位，用于循环自动生成货位
     *
     * @param w
     * @param h
     */
    public Location(int w, int h) {
        this.w = w;
        this.h = h;
    }
}
