package com.haphest.a3dtracking.navigation;

import android.graphics.Region;

import com.haphest.a3dtracking.R;
import com.haphest.a3dtracking.navigation.widget.Area;
import com.haphest.a3dtracking.navigation.widget.Location;
import com.haphest.a3dtracking.navigation.widget.ViewPath;

public class NavigationMap {
    private static Location locationSample = new Location(300, 300);//用于测试的标准货位样式

    public static Area createWarehouse() {
        Area m1 = createRectPathArea(430, 380, 1555, 1960);
        Area m2 = createRectPathArea(330, 2430, 1555, 3500);
        ViewPath path = new ViewPath();
        path.startAt(430, 3940);
        path.realLineTo(430, 5860);
        path.realLineTo(650, 5860);
        path.realLineTo(650, 6100);
        path.realLineTo(1555, 6100);
        path.realLineTo(1555, 3940);
        path.realLineTo(430, 3940);
        Area m3 = createPathArea(430, 3940, 1555, 6100, path);
        Area m4 = createRectPathArea(2000, 380, 5950, 1720);
        Area m5 = createRectPathArea(2000, 2380, 5950, 4150);
        Area m6 = createRectPathArea(2000, 4780, 5950, 6100);

        ViewPath path1 = new ViewPath();
        path1.startAt(6400, 3220);
        path1.realLineTo(6400, 5910);
        path1.realLineTo(6830, 5910);
        path1.realLineTo(6830, 4750);
        path1.realLineTo(7620, 4750);
        path1.realLineTo(7620, 3220);
        path1.realLineTo(6400, 3220);
        Area m7 = createPathArea(6400, 3220, 7620, 5910, path1);

        Region warehouseRegion = new Region(0, 0, 7940, 6810);
        ViewPath warehousePath = new ViewPath();
        warehousePath.startAt(0, 0);
        warehousePath.realLineTo(0, 6810);
        warehousePath.realLineTo(7940, 6810);
        warehousePath.realLineTo(7940, 0);
        warehousePath.realLineTo(0, 0);
        Area warehouse = new Area.Builder(warehouseRegion, warehousePath)
                .setShapeResource(R.mipmap.mapsmall)
                .addArea(m1)
                .addArea(m2)
                .addArea(m3)
                .addArea(m4)
                .addArea(m5)
                .addArea(m6)
                .addArea(m7)
                .build();
        return warehouse;
    }

    public static Area createPathArea(int l, int t, int r, int b, ViewPath path) {
        Region region = new Region(l, t, r, b);
        Area area = new Area.Builder(region, path)
                .build();
        return area;
    }

    public static Area createRectPathArea(int l, int t, int r, int b) {
        Region region = new Region(l, t, r, b);
        ViewPath path = new ViewPath();
        path.startAt(l, t);
        path.realLineTo(l, b);
        path.realLineTo(r, b);
        path.realLineTo(r, t);
        path.realLineTo(l, t);
        Area area = new Area.Builder(region, path)
                .build();
        return area;
    }
}
