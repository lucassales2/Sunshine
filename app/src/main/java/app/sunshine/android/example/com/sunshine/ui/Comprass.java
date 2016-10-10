package app.sunshine.android.example.com.sunshine.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

import app.sunshine.android.example.com.sunshine.R;

/**
 * Created by lucas on 06/10/16.
 */

public class Comprass extends View {
    public static final int DESIRED_WIDTH = 300;
    public static final int DESIRED_HEIGHT = 300;
    private Paint paintCircle;
    private float position = 0;

    public Comprass(Context context) {
        super(context);
        init();
    }

    public Comprass(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Comprass(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paintCircle = new Paint();
        paintCircle.setAntiAlias(true);
        paintCircle.setStrokeWidth(5);
        paintCircle.setTextSize(30);
        paintCircle.setStyle(Paint.Style.STROKE);
        paintCircle.setColor(ContextCompat.getColor(getContext(), R.color.sunshine_blue));
        AccessibilityManager accessibilityManager = (AccessibilityManager) getContext().getSystemService(
                Context.ACCESSIBILITY_SERVICE);
        if (accessibilityManager.isEnabled()) {
            sendAccessibilityEvent(
                    AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(DESIRED_WIDTH, widthSize);
        } else {
            width = DESIRED_WIDTH;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(DESIRED_HEIGHT, heightSize);
        } else {
            height = DESIRED_HEIGHT;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int xPoint = getMeasuredWidth() / 2;
        int yPoint = getMeasuredHeight() / 2;
        int marginNorthOrEast = Math.max(getMeasuredWidth() / 8, getMeasuredHeight() / 8);
        int marginSouthOrWest = Math.max(getMeasuredWidth() / 5, getMeasuredHeight() / 5);
        float radius = (float) (Math.max(xPoint, yPoint) * 0.6);
        canvas.drawCircle(xPoint, yPoint, radius, paintCircle);
        canvas.drawLine(xPoint,
                yPoint,
                (float) (xPoint + radius
                        * Math.sin((double) (position) / 180 * Math.PI)),
                (float) (yPoint - radius
                        * Math.cos((double) (position) / 180 * Math.PI)), paintCircle);

        canvas.drawText(getContext().getString(R.string.south), xPoint, getMeasuredWidth() - radius + marginSouthOrWest, paintCircle);
        canvas.drawText(getContext().getString(R.string.north), xPoint, radius - marginNorthOrEast, paintCircle);

        canvas.drawText(getContext().getString(R.string.east), getMeasuredHeight() - radius + marginNorthOrEast, yPoint, paintCircle);
        canvas.drawText(getContext().getString(R.string.west), radius - marginSouthOrWest, yPoint, paintCircle);

    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        event.getText().add("Wind diraction " + position);
        return true;
    }

    public void updateData(float position) {
        this.position = position;
        invalidate();
    }

}
