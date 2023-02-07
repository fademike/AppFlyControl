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

    public float x,y, x1,y1;

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
        x1 = event.getX();
        y1  = event.getY();

        if (x1>(x_Width-sqrSize))x1 = x_Width-sqrSize;
        else if (x1<sqrSize)x1 = sqrSize;
        if (y1>(y_Height-sqrSize))y1 = y_Height-sqrSize;
        else if (y1<sqrSize)y1 = sqrSize;

        x=x1;
        y=y1;
        //if (y1 <= 50) y =  0 + 50;
        //else if (y1 >= (y_Height-50)) y =  y_Height-50;
        //else y=y1;

//			  PointF curr = new PointF(event.getX(), event.getY());
//			  Log.i(TAG, "Received event at x=" + curr.x +", y=" + curr.y + ":");
//			  txtArduino.setText("Received event at x=" + curr.x +", y=" + curr.y + ":");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //Log.i(TAG, " ACTION_DOWN");
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.i(TAG, " ACTION_MOVE");
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //Log.i(TAG, " ACTION_UP");
                init=true;
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                //Log.i(TAG, " ACTION_CANCEL");
                break;
        }
        return true;
    }




    public void draw (Canvas canvas) {
        super.draw(canvas);
        //Log.i(TAG, " draw!!!!");

        //canvas.drawColor(Color.CYAN);
        canvas.drawColor(Color.WHITE);

        Paint xferPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        xferPaint.setColor(Color.CYAN);
        canvas.drawRoundRect(new RectF(0,0,x_Width,y_Height), 50.0f, 50.0f, xferPaint);


        Paint paint = new Paint();

        paint.setColor(Color.rgb(127, 0, 0));
        canvas.drawRect(0, n_y - 5, x_Width, n_y + 5, paint);

        canvas.drawRect(n_x - 5, 0, n_x + 5, y_Height, paint);
//			  canvas.drawLine(0, y_Height * 2/3, x_Width, y_Height * 2/3, paint);


        if (init) {
            if (y_ret) y = y_Height * 1 / 2;
            if (x_ret) x = x_Width * 1 / 2;
//            paint.setColor(Color.BLACK);
//            canvas.drawCircle(x_Width/2, y_Height * 1/2, sqrSize, paint);
//            paint.setColor(Color.GREEN);
//            canvas.drawCircle(x_Width/2, y_Height * 1/2, sqrSize-5, paint);
            init = false;
        }
        //else{
        paint.setColor(Color.BLACK);
        canvas.drawCircle(x, y, sqrSize, paint);
        paint.setColor(Color.GREEN);
        canvas.drawCircle(x, y, sqrSize - 5, paint);
        //}

    }


    public float xPosition ()
    {
        float rezult;
        rezult = ((x_Width * 1/2)-x)*100/(x_Width-(2*sqrSize));//
        //rezult =  (x-50)*100/(x_Width-(2*sqrSize));//((x_Width * 1/2)-x);

        //Log.d(TAG, "xPosition = " + x);
//        if (rezult > 0) rezult = (rezult * 100/((x_Width * 2/3)-sqrSize));
//        else if (rezult < 0) rezult = (rezult * 100/((x_Width * 1/3)-sqrSize));
        return rezult;
    }

    public float yPosition ()
    {
        float rezult;
        //rezult = ((y_Height * 1/2)-y)*100/(y_Height-(2*sqrSize));
        //rezult = (y-50)*100/(y_Height-(2*sqrSize));//((y_Height * 1/2)-y);
        if (n_y == y_Height) rezult = ((n_y)-y)*50/(y_Height-(2*sqrSize));
        else                 rezult = ((y_Height * 1/2)-y)*100/(y_Height-(2*sqrSize));

        //Log.d(TAG, "yPosition = " + y);

//        if (rezult > 0) rezult = (rezult * 100/((y_Height * 2/3)-sqrSize));
//        else if (rezult < 0) rezult = (rezult * 100/((y_Height * 1/3)-sqrSize));
        return rezult;
    }
}


