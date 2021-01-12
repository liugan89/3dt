package com.haphest.a3dtracking.navigation.widget;

import android.graphics.Path;
import android.graphics.Region;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ViewPath {

    public List<XY> xys = new ArrayList<>();

    public void realLineTo(int x, int y) {
        xys.add(new XY(x, y));
    }

    public void startAt(int x, int y) {
        xys.add(new XY(x, y, 0));
    }

    public Path createPath(BigDecimal ratio, float padding) {
        Path p = new Path();
        if (ratio != null) {
            for (XY xy : xys) {
                BigDecimal x = ratio.multiply(new BigDecimal(xy.x));
                BigDecimal y = ratio.multiply(new BigDecimal(xy.y));
                if (xy.type == 0) {
                    p.moveTo(x.floatValue() + padding, y.floatValue() + padding);
                } else if (xy.type == 1) {
                    p.lineTo(x.floatValue() + padding, y.floatValue() + padding);
                }
            }
        }
        return p;
    }

    public Path createRealPath() {
        Path p = new Path();
        for (XY xy : xys) {
            if (xy.type == 0) {
                p.moveTo(xy.x, xy.y);
            } else if (xy.type == 1) {
                p.lineTo(xy.x, xy.y);
            }
        }
        return p;
    }

    public Region createRealRegion(Region region) {
        Path p = createRealPath();
        boolean b = region.setPath(p, region);
        return region;
    }

    class XY {
        float x, y;
        int type = 1;//0:startAt   1:lineTo

        public XY(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public XY(float x, float y, int type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }
    }
}
