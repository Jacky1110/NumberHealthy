package com.jotangi.NumberHealthy.ui.home.bar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class HorizontalColorBar extends View {

    public enum Type {
        NORMAL, SLEEP, DBP, SBP, OXYGEN, ECG
    }

    Type type = Type.NORMAL;

    private int[] barlenNormal = {1, 1, 1, 1};
    private int[] barlenSleep = {1, 1, 1, 1};
    private int[] barlenDBP = {101, 119, 32, 84};
    private int[] barlenSBP = {82, 105, 65, 84};
    private int[] barlenOxygen = {73, 86, 143};
    private int[] barlenECG = {1, 1, 1};
    private int[] barlen = new int[4];

    private int[] barColor1 = {
            Color.rgb(126, 169, 230),
            Color.rgb(138, 230, 184),
            Color.rgb(252, 226, 121),
            Color.rgb(250, 148, 158),
    };
    private int[] barColor2 = {
            Color.rgb(250, 148, 158),
            Color.rgb(252, 226, 121),
            Color.rgb(138, 230, 184),
            Color.rgb(126, 169, 230),
    };

    private int[] barColor3 = {
            Color.rgb(250, 148, 158),
            Color.rgb(252, 226, 121),
            Color.rgb(138, 230, 184),
    };

    private int[] barColor4 = {
            Color.rgb(126, 169, 230),
            Color.rgb(138, 230, 184),
            Color.rgb(250, 148, 158),
    };
    private int[] barColor = new int[4];

    private String[] label1 = {"差", "中等", "良好", "優良"};
    private String[] label2 = {"60", "75", "90"};
    private String[] label3 = {"低", "正常", "高"};
    private String[] label4 = {"0", "15", "60", "100"};

    // 設定相關數值時, levelValues必須比barLabel, barColor多一個, 畫圖時邏輯才不會出錯
    private int[] levelDBP = {0, 90, 140, 160, 250};
    private int[] levelSBP = {0, 60, 90, 110, 250};
    private int[] levelSleep = {0, 60, 75, 90, 100};
    private int[] levelOxygen = {0, 50, 80, 100};
    private int[] levelECG = {0, 15, 60, 100};
    private int[] levelNormal = {0, 25, 50, 75, 100};
    private int[] levelValues = {0, 117, 137, 156, 176, 250};

    private int triangleColor = Color.rgb(254, 104, 103);

    private int vw;
    private int vh;
    private int triangleHeight;
    private int triangleWidth;
    private int triangleLeft;
    private int barHeight;
    private int[] barWidth = new int[4];
    private int offsetY;

    private float labelSize;
    private int labelColor = Color.BLACK;

    private int dataValue;

    private Path trianglePath;
    private Paint barPaint;
    private Paint textPaint;
    private Paint.FontMetrics fontMetrics;

    public HorizontalColorBar(Context context) {
        super(context);
        init();
    }

    public HorizontalColorBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalColorBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public HorizontalColorBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        dataValue = levelValues[1];
        barPaint = new Paint();
        barPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setTextSize(labelSize);

        triangleLeft = calculateTriangleLeft();
        trianglePath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        vw = w;
        vh = h;
        offsetY = (h * 2 / 60);
        triangleHeight = h * 12 / 60;
        triangleWidth = h * 24 / 60;
        barHeight = h * 24 / 60;
        labelSize = h * 16 / 60f;
        textPaint.setTextSize(labelSize);
        fontMetrics = textPaint.getFontMetrics();
        calculateBarWidth(vw);
        updateTrianglePosition();
    }

    private void setTypeValues(int[] len, int[] color, int[] level) {
        barlen = len;
        barColor = color;
        levelValues = level;
    }

    private void updateTypeValues() {
        switch (type) {
            case SLEEP:
                setTypeValues(barlenSleep, barColor2, levelSleep);
                break;
            case DBP:
                setTypeValues(barlenDBP, barColor1, levelDBP);
                break;
            case SBP:
                setTypeValues(barlenSBP, barColor1, levelSBP);
                break;
            case OXYGEN:
                setTypeValues(barlenOxygen, barColor3, levelOxygen);
                break;
            case ECG:
                setTypeValues(barlenECG, barColor4, levelECG);
                break;
            default:
                setTypeValues(barlenNormal, barColor1, levelNormal);
        }
    }

    private void calculateBarWidth(int vw) {
        updateTypeValues();

        int sum = 0;
        for (int i = 0; i < barlen.length; i++) {
            sum += barlen[i];
        }

        for (int i = 0; i < barlen.length; i++) {
            barWidth[i] = vw * barlen[i] / sum;
        }
    }

    private int calculateTriangleLeft() {

        if (barColor.length < 1) return 0;

        int index = 0;
        int startx = 0;
        while (index < barColor.length) {
            if (dataValue >= levelValues[index] && dataValue < levelValues[index + 1]) {
                return startx + (dataValue - levelValues[index]) * barWidth[index] / (levelValues[index + 1] - levelValues[index]);
            }
            startx += barWidth[index];
            index++;
        }
        return 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (vw == 0 || vh == 0) return;
        if (barColor.length == 0) return;

        if (type != Type.SLEEP) {
            // draw triangle
            barPaint.setColor(triangleColor);
            canvas.drawPath(trianglePath, barPaint);
        }

        // draw color bar
        int y = offsetY + triangleHeight + offsetY;
        int x = 0;
        int y2 = y + barHeight;
        for (int i = 0; i < barColor.length; i++) {
            int x2 = x + barWidth[i];
            barPaint.setColor(barColor[i]);
            canvas.drawRect(x, y, x2, y2, barPaint);
            x = x2;
        }

        textPaint.setColor(labelColor);
        y = y2 + offsetY - Math.round(fontMetrics.ascent);
        x = 0;
        if (type == Type.SLEEP) {
            // draw bottom label
            for (int i = 0; i < label1.length; i++) {
                int tw = Math.round(textPaint.measureText(label1[i]));
                int x1 = x;
                int x2 = x + barWidth[i];
                if (tw < barWidth[i]) {
                    x1 += (barWidth[i] - tw) / 2;
                }
                canvas.drawText(label1[i], x1, y, textPaint);
                x = x2;
            }

            x = 0;
            for (int i = 0; i < label2.length; i++) {
                int tw = Math.round(textPaint.measureText(label2[i]));
                int x1 = x + barWidth[i];
                int x2 = x1;
                x1 -= tw / 2;
                canvas.drawText(label2[i], x1, y, textPaint);
                x = x2;
            }
        } else if (type == Type.ECG) {
            for (int i = 0; i < label3.length; i++) {
                int tw = Math.round(textPaint.measureText(label3[i]));
                int x1 = x;
                int x2 = x + barWidth[i];
                if (tw < barWidth[i]) {
                    x1 += (barWidth[i] - tw) / 2;
                }
                canvas.drawText(label3[i], x1, y, textPaint);
                x = x2;
            }

            x = 0;
            for (int i = 0; i < label4.length; i++) {
                int tw = Math.round(textPaint.measureText(label4[i]));
                canvas.drawText(label4[i], x, y, textPaint);
                x += (barWidth[i] - tw / 2);
            }
        }
    }

    public void setType(Type type) {
        this.type = type;
        calculateBarWidth(vw);
        updateTrianglePosition();
    }

    public void setDataValue(int v) {
        dataValue = v;
        updateTrianglePosition();
        invalidate();
    }

    private void updateTrianglePosition() {
        triangleLeft = calculateTriangleLeft();
        trianglePath.reset();
        trianglePath.moveTo(triangleLeft, offsetY);
        trianglePath.lineTo(triangleLeft + triangleWidth, offsetY);
        trianglePath.lineTo(triangleLeft + triangleWidth / 2, offsetY + triangleHeight);
        trianglePath.lineTo(triangleLeft, offsetY);
        trianglePath.close();
    }
}

