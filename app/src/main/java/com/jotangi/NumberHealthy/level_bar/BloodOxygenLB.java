package com.jotangi.NumberHealthy.level_bar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.jotangi.NumberHealthy.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BloodOxygenLB extends View {

    private String TAG = getClass().getSimpleName() + "(TAG)";

    /**
     * 設計要點
     * 1. 數值必需照順序左低右高
     * 2. 至少要兩個值
     * 3. levelValues.length - 1 = barLabel.length, barColor.length, barWidth.length
     */
    private final int[] levelValues = {0, 90, 95, 100};
    private final String[] barColor =
            getContext().getResources().getStringArray(R.array.ryg_color);
    private final String[] barLabel = {"危險", "注意", "正常"};
    private int[] barWidth = {4, 1, 1};


    private Paint barPaint, textPaint;
    private Path trianglePath;
    private Paint.FontMetrics fontMetrics;


    private int dataValue;
    private int vw, vh;
    private int offsetH;
    private int triangleWidth, triangleHeight, triangleOffsetW;
    private int barHeight;


    public BloodOxygenLB(Context context) {
        super(context);
        init();
    }


    public BloodOxygenLB(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BloodOxygenLB(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BloodOxygenLB(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

        dataValue = levelValues[0];

        barPaint = new Paint();
        barPaint.setStyle(Paint.Style.FILL);
        textPaint = new Paint();
        trianglePath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        vw = w;
        vh = h;

        int base = 52;
        offsetH = (h * 2 / base);

        triangleWidth = h * 14 / base;
        triangleHeight = h * 10 / base;
        triangleOffsetW = triangleWidth / 2;

        int scale = 0;
        for (int num : barWidth) {
            scale += num;
        }
        int baseNum = (w - triangleOffsetW * 2) / scale;
        List<Integer> list = new ArrayList<>();
        for (int num : barWidth) {
            list.add(num * baseNum);
        }
        barWidth = list.stream().mapToInt(i -> i).toArray();
        barHeight = h * 20 / base;

        upPosition();

        textPaint.setTextSize(h * 16F / base);
        fontMetrics = textPaint.getFontMetrics();
    }

    private void upPosition() {

        int positionX = calculatePosition();

        trianglePath.reset();
        trianglePath.moveTo(positionX, offsetH);
        trianglePath.lineTo(positionX + triangleWidth, offsetH);
        trianglePath.lineTo(positionX + triangleOffsetW, offsetH + triangleHeight);
        trianglePath.lineTo(positionX, offsetH);
        trianglePath.close();
    }

    private int calculatePosition() {

        if (levelValues.length == 0 || dataValue < levelValues[0]) return 0;

        int size = levelValues.length;
        for (int i = 1; i < size; i++) {

            if (dataValue < levelValues[i]) {

                return baseValue(i - 1) +
                        (dataValue - levelValues[i - 1]) * barWidth[i - 1] / (levelValues[i] - levelValues[i - 1]);
            }
        }

        return baseValue(barWidth.length);
    }

    private int baseValue(int size) {

        int value = 0;

        for (int i = 0; i < size; i++) {
            value += barWidth[i];
        }

        return value;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (vw == 0 || vh == 0 || barColor.length == 0) return;

        // 畫三角形
        barPaint.setColor(Color.RED);
        canvas.drawPath(trianglePath, barPaint);

        // 畫長條圖
        int barY1 = offsetH + triangleHeight + offsetH;
        int barY2 = barY1 + barHeight;
        int barX1 = triangleOffsetW;
        int barX2;
        for (int i = 0; i < barColor.length; i++) {
            barX2 = barX1 + barWidth[i];
            barPaint.setColor(Color.parseColor(barColor[i]));
            canvas.drawRect(barX1, barY1, barX2, barY2, barPaint);
            barX1 = barX2;
        }

        // 畫數值
        int numY = barY1 + offsetH - Math.round(fontMetrics.ascent);
        barX1 = barWidth[0] + triangleOffsetW;
        textPaint.setColor(Color.WHITE);
        for (int i = 1; i < barLabel.length; i++) {
            String value = String.format(Locale.getDefault(), "%d", levelValues[i]);
            int tw = Math.round(textPaint.measureText(value));
            int numX = barX1 - tw / 2;
            canvas.drawText(value, numX, numY, textPaint);
            barX1 = barX1 + barWidth[i];
        }

        // 畫說明欄
        textPaint.setColor(Color.WHITE);
        barY1 = barY2 + offsetH - Math.round(fontMetrics.ascent);
        barX1 = triangleOffsetW;
        for (int i = 0; i < barLabel.length; i++) {
            int tw = Math.round(textPaint.measureText(barLabel[i]));
            int legendX = barX1;
            int x3 = barX1 + barWidth[i];
            if (tw < barWidth[i]) {
                legendX += (barWidth[i] - tw) / 2;
            }
            canvas.drawText(barLabel[i], legendX, barY1, textPaint);
            barX1 = x3;
        }
    }

    public void setDataValue(int v) {

        dataValue = v;
        upPosition();
        invalidate();
    }
}
