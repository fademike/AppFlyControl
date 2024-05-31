package com.example.appflycontrol;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class Joystick extends View {

    public float x,y;
    public float x_Width, y_Height;
    boolean init = true;            // for return to default state
    private static final String TAG = "test";

    public float n_x, n_y ;         // default state
    public boolean x_ret, y_ret ;   // return axes
    public int zero_line_thickness = 5;

    public int zeropoint_thickness_color = Color.BLACK;//Color.rgb(127, 0, 0);
    public int background_color = Color.GRAY;//.BLUE;//Color.CYAN;

    public int sqrSize = 50;
    public int sqrt_thickness = 5;
    public int sqrt_color = Color.BLUE;//.RED;
    public int sqrt_th_color = Color.BLACK;

    public Joystick(Context context, AttributeSet attrs) {
        super(context, attrs);
        x_Width = 300;
        y_Height = 300;
        n_x = x_Width/2;
        n_y = y_Height/2;
        x_ret = true;
        y_ret = true;
        setMinimumWidth((int)x_Width);
        setMinimumHeight((int)y_Height);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                getSuggestedMinimumWidth(),
                getSuggestedMinimumHeight());
    }
    @Override
    protected void onDraw(Canvas canvas) {
        //Log.i(TAG, " onDraw!!!!");
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x_tmp = event.getX();
        float y_tmp  = event.getY();

        if (x_tmp>(x_Width-sqrSize)) x_tmp = x_Width-sqrSize;
        else if (x_tmp<sqrSize) x_tmp = sqrSize;
        if (y_tmp>(y_Height-sqrSize)) y_tmp = y_Height-sqrSize;
        else if (y_tmp<sqrSize) y_tmp = sqrSize;

        x=x_tmp;
        y=y_tmp;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                init=true;
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    public void draw (Canvas canvas) {
        super.draw(canvas);
        //Log.i(TAG, " draw!!!!");

        canvas.drawColor(Color.WHITE);

        Paint xferPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        xferPaint.setColor(background_color);
        canvas.drawRoundRect(new RectF(0,0,x_Width,y_Height), sqrSize, sqrSize, xferPaint); // background

        Paint paint = new Paint();

        //zero line
        int th = zero_line_thickness;
        paint.setColor(zeropoint_thickness_color);
        canvas.drawRect(0, n_y - th, x_Width, n_y + th, paint);     // horizont line
        canvas.drawRect(n_x - th, 0, n_x + th, y_Height, paint);      // vertical line

        if (init) {
            if (y_ret) y = y_Height * 1 / 2;
            if (x_ret) x = x_Width * 1 / 2;
            init = false;
        }
        paint.setColor(sqrt_th_color);
        canvas.drawCircle(x, y, sqrSize, paint);
        paint.setColor(sqrt_color);
        canvas.drawCircle(x, y, sqrSize - sqrt_thickness, paint);
    }

    public float xPosition ()
    {
        float rezult = ((x_Width * 1/2)-x)*100/(x_Width-(2*sqrSize));//
        return rezult;
    }

    public float yPosition ()
    {
        float rezult = ((y_Height * 1/2)-y)*100/(y_Height-(2*sqrSize));
        //rezult = (((n_y)-y)*100/(y_Height-(2*sqrSize)))*(1+(((y_Height/2)-n_y)/((y_Height+2*sqrSize)/2)) );

        return rezult;
    }
}


