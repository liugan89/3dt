package com.haphest.a3dtracking.navigation.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.haphest.a3dtracking.R;
import com.haphest.a3dtracking.utils.ToastUtil;

import java.math.BigDecimal;

import androidx.annotation.RequiresApi;

public class NavigationView extends ZoomView {
    Area warehouse;//默认仓库
    Paint locationPaint;
    Paint locationAreaPaint;
    Paint shapeResourcePaint;

    Paint namePaint;

    int strokeWidth = 1;
    private BigDecimal ratio;

    float x1, x2, y1, y2;
    float padding;

    private long first = 0;


    public NavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.parseColor("#FFFFFF"));
    }

    public Area getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Area warehouse) {
        this.warehouse = warehouse;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        padding = getPaddingTop();//只能设置paddingTop其他padding都无效

        BigDecimal viewWidth = new BigDecimal(getMeasuredWidth() - padding * 2);
        BigDecimal viewHeight = new BigDecimal(getMeasuredHeight() - padding * 2);
        BigDecimal warehouseh = new BigDecimal(warehouse.h);
        BigDecimal warehousew = new BigDecimal(warehouse.w);

        if (viewHeight.divide(viewWidth, 10, BigDecimal.ROUND_HALF_UP).compareTo(warehouseh.divide(warehousew, 10, BigDecimal.ROUND_HALF_UP)) <= 0) {
            ratio = viewHeight.divide(warehouseh, 10, BigDecimal.ROUND_HALF_UP);
        } else {
            ratio = viewWidth.divide(warehousew, 10, BigDecimal.ROUND_HALF_UP);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        locationPaint = new Paint();
        locationPaint.setColor(getResources().getColor(R.color.navigation_yellow));
        locationPaint.setStyle(Paint.Style.STROKE);
        locationPaint.setStrokeWidth(strokeWidth);

        locationAreaPaint = new Paint();
        locationAreaPaint.setColor(getResources().getColor(R.color.navigation_yellow));
        locationAreaPaint.setStyle(Paint.Style.STROKE);
        locationAreaPaint.setStrokeWidth(strokeWidth);

        shapeResourcePaint = new Paint();

        namePaint = new Paint();
        namePaint.setColor(getResources().getColor(R.color.white));
        namePaint.setTextSize(10);


        long first = System.currentTimeMillis();
        drawArea(warehouse, canvas);
        Log.e("draw", System.currentTimeMillis() - first + "");
    }

    private void drawArea(Area area, Canvas canvas) {

        if (area.shapeResource != -1) {
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
            BitmapDrawable d = (BitmapDrawable) getResources().getDrawable(area.shapeResource);
            float l = new BigDecimal(area.x).multiply(ratio).floatValue() + padding;
            float t = new BigDecimal(area.y).multiply(ratio).floatValue() + padding;
            float r = new BigDecimal(area.x + area.w).multiply(ratio).floatValue() + padding;
            float b = new BigDecimal(area.y + area.h).multiply(ratio).floatValue() + padding;
            canvas.drawBitmap(d.getBitmap(), null, new RectF(l, t, r, b), shapeResourcePaint);
        }

        if (area.viewPath != null) {
            canvas.drawPath(area.viewPath.createPath(ratio, padding), locationAreaPaint);
        }

        if (area.isShowName) {
            if (area.name != null) {
                Rect rect = new Rect();
                namePaint.getTextBounds(area.name, 0, area.name.length(), rect);
                int x = new BigDecimal(area.x + area.w / 2).multiply(ratio).intValue() - rect.width() / 2;
                int y = new BigDecimal(area.y + area.h / 2).multiply(ratio).intValue() + rect.height() / 2;
                canvas.drawText(area.name, x + padding, y + padding, namePaint);
            }
        }

        if (area.areas != null) {
            for (Area a : area.areas) {
                drawArea(a, canvas);
            }
        }

        if (area.isLocator && area.locations != null) {
            for (Location m : area.locations) {
                drawLocation(m, canvas);
            }
        }
    }

    private void drawLocation(Location location, Canvas canvas) {
        int x = new BigDecimal(location.x + location.w / 2).multiply(ratio).intValue();
        int y = new BigDecimal(location.y + location.h / 2).multiply(ratio).intValue();
        int r = new BigDecimal(location.w / 2).multiply(ratio).intValue() - strokeWidth / 2;
        canvas.drawCircle(x + padding, y + padding, r, locationPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                long now = System.currentTimeMillis();
                if (now - first < 200) {
                    translationX = 0;
                    translationY = 0;
                    scale = 1;
                    rotation = 0;
                    setTranslationX(translationX);
                    setTranslationY(translationY);
                    setScaleX(scale);
                    setScaleY(scale);
                    setRotation(rotation);
                    return true;
                } else {
                    first = now;

                    x2 = event.getX();
                    y2 = event.getY();
                    if (x2 - x1 < 5 && y2 - y1 < 5) {
                        int rawx = new BigDecimal(x1 - padding).divide(ratio, 10, BigDecimal.ROUND_HALF_UP).intValue();
                        int rawy = new BigDecimal(y1 - padding).divide(ratio, 10, BigDecimal.ROUND_HALF_UP).intValue();
                        //根据x,y得到货位
//                        Location l = getLocationFromXY(warehouse, rawx, rawy);
//                        if (l != null) {
//                            Toast.makeText(getContext(), l.x + "/" + l.y, Toast.LENGTH_SHORT).show();
//                        }
                        ToastUtil.showToast(getContext(),rawx + "/" + rawy);
                    }
                    return true;
                }
        }
        return super.onTouchEvent(event);
    }

    private Location getLocationFromXY(Area area, int rawx, int rawy) {
        Location location = null;
        if (area.realRegion.contains(rawx, rawy)) {
            if (area.areas != null) {
                for (Area a : area.areas) {
                    Location ll = getLocationFromXY(a, rawx, rawy);
                    if (ll != null) {
                        location = ll;
                    }
                }
            } else if (area.locations != null) {
                for (Location l : area.locations) {
                    if (l.containXY(rawx, rawy)) {
                        return l;
                    }
                }
            }

        }
        return location;
    }

    public interface Click {

    }
}
