package com.jotangi.NumberHealthy.ui.home.bar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.jotangi.NumberHealthy.api.watch.SleepDataBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * 上間隔 2
 * 三角形置頂, 高6, 可設定位置
 * 垂直間隔 2
 * 色條高 20
 * 垂直間隔 2
 * 文字高 16
 * 下間隔 2
 * 色條平均分配View的寬度
 */
public class SleepHorizontalColorBarView extends View {

    private int[] barColorSleep = {
            Color.rgb(106, 80, 181),
            Color.rgb(131, 89, 252),
            Color.rgb(216, 208, 242)
    };

    private int[] barColor = barColorSleep;

    private String[] barLabel = new String[2];
    private int[] barwidth = null;
    private int[] offx = null;
    private int[] bartype = null;

    private int labelColor = Color.BLACK;

    private int vw;
    private int vh;
    private int barHeight;
    private float labelSize;
    private int offsetY;

    private int totalSeconds = 0;
    private List<SleepDataBean> datalist = new ArrayList<>();

    private Paint.FontMetrics fontMetrics;
    private Paint barPaint;
    private Paint textPaint;

    public SleepHorizontalColorBarView(Context context) {
        super(context);
        init();
    }

    public SleepHorizontalColorBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SleepHorizontalColorBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SleepHorizontalColorBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        barPaint = new Paint();
        barPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setTextSize(labelSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        vw = w;
        vh = h;
        offsetY = (h * 2 / 50);

        barHeight = h * 20 / 50;
        labelSize = h * 16 / 50f;
        textPaint.setTextSize(labelSize);
        fontMetrics = textPaint.getFontMetrics();

        calculateBarWidth();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (vw == 0 || vh == 0) return;
        if (barColor.length == 0) return;
        if (barwidth == null) return;

        // draw color bar
        int y = offsetY;
        int x = 0;
        int y2 = y + barHeight;


        barPaint.setColor(barColor[2]);
        canvas.drawRect(x, y, vw, y2, barPaint);

        for (int i = barwidth.length - 1; i >= 0; i--) {
            int x2 = x + barwidth[i];
            barPaint.setColor(barColor[bartype[i]]);
            canvas.drawRect(x, y, x2, y2, barPaint);
            x = x + offx[i];
        }

        textPaint.setColor(labelColor);
        y = y2 + offsetY - Math.round(fontMetrics.ascent);
        x = 0;

        labelSize = vh * 12 / 50f;
        textPaint.setTextSize(labelSize);
        canvas.drawText(barLabel[0], x, y, textPaint);
        canvas.drawText(barLabel[1], vw - Math.round(textPaint.measureText(barLabel[1])), y, textPaint);

    }

    public void setDatalist(List<SleepDataBean> list, String starttime, String stoptime) {

        fixType(list);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm", Locale.getDefault());
        try {
            Date start = sdf.parse(starttime);
            Date stop = sdf.parse(stoptime);
            assert start != null;
            barLabel[0] = sdf2.format(start);
            assert stop != null;
            barLabel[1] = sdf2.format(stop);
            totalSeconds = (int) ((stop.getTime() - start.getTime()) / 1000);

            datalist.clear();
            barwidth = null;
            bartype = null;

            if (list == null) return;
            if (list.size() == 0) return;

            barwidth = new int[list.size()];
            bartype = new int[list.size()];
            offx = new int[list.size()];
            datalist.addAll(list);

            calculateBarWidth();

            invalidate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void fixType(List<SleepDataBean> list) {

        for (int i = 0; i < list.size(); i++) {

            if ( "0xF2".equals(list.get(i).getSleepType()) ) {
                list.get(i).setSleepType("242");
            }

            if ( "0xF1".equals(list.get(i).getSleepType()) ) {
                list.get(i).setSleepType("241");
            }
        }
    }

    private void calculateBarWidth() {
        // todo: 資料不全時的處理
        if (barwidth == null) return;

        for (int i = 0; i < datalist.size(); i++) {
            int n = getItemSleepSeconds(datalist.get(i));
            barwidth[i] = n;

            int t = getItemType(datalist.get(i));
            bartype[i] = t;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        for (int i = datalist.size() - 1; i > 0; i--) {
            String t1s = datalist.get(i).getSleepStartTime();
            String t2s = datalist.get(i - 1).getSleepStartTime();
            try {
                Date d1 = sdf.parse(t1s);
                Date d2 = sdf.parse(t2s);
                offx[i] = (int) ((d2.getTime() - d1.getTime()) / 1000);
            } catch (Exception ex) {
                ex.printStackTrace();
                offx[i] = 0;
            }
        }
        try {
            offx[0] = Integer.parseInt(datalist.get(0).getSleepLen());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        for (int i = 0; i < barwidth.length; i++) {
            barwidth[i] = barwidth[i] * vw / totalSeconds;
            offx[i] = offx[i] * vw / totalSeconds;
        }
    }

    private int getItemType(SleepDataBean item) {
        if (item != null) {
            try {
                if ("241".compareToIgnoreCase(Objects.requireNonNull(item.getSleepType())) == 0)
                    return 0;
                else if ("242".compareToIgnoreCase(item.getSleepType()) == 0) return 1;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return 2;
    }

    private int getItemSleepSeconds(SleepDataBean item) {
        if (item != null) {
            try {
                return Integer.parseInt(Objects.requireNonNull(item.getSleepLen()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return 0;
    }

}