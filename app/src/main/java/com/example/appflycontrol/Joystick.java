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
    boolean init = true;
    private static final String TAG = "test";//"bluetooth2";

    public float n_x, n_y ;
    public boolean x_ret, y_ret ;

    public int sqrSize = 50;

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
        xferPaint.setColor(Color.CYAN);
        canvas.drawRoundRect(new RectF(0,0,x_Width,y_Height), 50.0f, 50.0f, xferPaint);

        Paint paint = new Paint();
        paint.setColor(Color.rgb(127, 0, 0));
        canvas.drawRect(0, n_y - 5, x_Width, n_y + 5, paint);
        canvas.drawRect(n_x - 5, 0, n_x + 5, y_Height, paint);

        if (init) {
            if (y_ret) y = y_Height * 1 / 2;
            if (x_ret) x = x_Width * 1 / 2;
            init = false;
        }
        paint.setColor(Color.BLACK);
        canvas.drawCircle(x, y, sqrSize, paint);
        paint.setColor(Color.GREEN);
        canvas.drawCircle(x, y, sqrSize - 5, paint);
    }


    public float xPosition ()
    {
        float rezult;
        rezult = ((x_Width * 1/2)-x)*100/(x_Width-(2*sqrSize));//
        return rezult;
    }

    public float yPosition ()
    {
        float rezult;
        if (n_y == y_Height) rezult = ((n_y)-y)*50/(y_Height-(2*sqrSize));
        else                 rezult = ((y_Height * 1/2)-y)*100/(y_Height-(2*sqrSize));

        return rezult;
    }
}


