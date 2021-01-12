package com.haphest.a3dtracking.navigation.widget;

import android.graphics.Rect;
import android.graphics.Region;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 区域
 */
public class Area {
    public String name;//区域名称
    public int shapeResource = -1;//区域图形资源
    public int shapeURL;//区域图形远程资源（待开发）
    public boolean isLocator;//是否为储货区
    public boolean isShowName = false;
    public List<Area> areas;//区域内区域
    public List<Location> locations;//区域内货位
    public Region rectRegion;//长方形区域
    public ViewPath viewPath;//真实路径
    public Region realRegion;//不规则区域


    public int x, y, w, h;

    public Area(Region region) {//长方形区域
        this.rectRegion = region;
        realRegion = region;
        Rect rect = region.getBounds();
        x = rect.left;
        y = rect.top;
        w = rect.right - rect.left;
        h = rect.bottom - rect.top;
    }

    public Area(Region rectRegion, ViewPath viewPath) {//不规则区域
        this(rectRegion);
        this.viewPath = viewPath;
        realRegion = viewPath.createRealRegion(rectRegion);
    }


    public Area(int x, int y, int w, int h) {//长方形区域
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        rectRegion = new Region(x, y, x + w, y + h);
        realRegion = rectRegion;
    }


    public static class Builder {
        private Area area;

        public Builder(Region region) {
            area = new Area(region);
        }

        public Builder(Region region, ViewPath viewPath) {
            area = new Area(region, viewPath);
        }

        public Builder(int x, int y, int w, int h) {
            area = new Area(x, y, w, h);
        }

        public Builder setViewPath(ViewPath viewPath) {
            area.viewPath = viewPath;
            area.realRegion = viewPath.createRealRegion(area.rectRegion);
            return this;
        }

        public Builder setName(String name) {
            area.name = name;
            return this;
        }

        public Builder addArea(Area r) {
            if (area.areas == null) {
                area.areas = new ArrayList<>();
            }
            area.areas.add(r);
            return this;
        }

        public Builder addLocation(Location l) {
            if (area.locations == null) {
                area.locations = new ArrayList<>();
            }
            area.isLocator = true;
            area.locations.add(l);
            return this;
        }

        public Builder fillLocations(Location location) {
            long first = System.currentTimeMillis();
            if (location.w <= 0 || location.h <= 0) {
                return this;
            }
            if (area.locations == null) {
                area.locations = new ArrayList<>();
            }
            area.isLocator = true;
            for (int x = area.x; x + location.w <= area.w; x += location.w) {
                for (int y = area.y; y + location.h <= area.h; y += location.h) {
                    //判断location的四角是否都在region内，不能使用quickContains
                    if (area.realRegion.contains(x, y) && area.realRegion.contains(x + location.w, y)
                            && area.realRegion.contains(x, y + location.h) && area.realRegion.contains(x + location.w, y + location.h)) {
                        Location l = Location.copyLocationWithoutXY(location);
                        l.x = x;
                        l.y = y;
                        l.setRegion(x, y, l.w, l.h);
                        area.locations.add(l);
                    }
                }
            }
            Log.e("build", System.currentTimeMillis() - first + "");
            return this;
        }

        public Builder setShapeResource(int shapeResource) {
            area.shapeResource = shapeResource;
            return this;
        }

        public Builder setIsShowName(boolean isShowName) {
            area.isShowName = isShowName;
            return this;
        }

        public Area build() {
            return area;
        }
    }
}
