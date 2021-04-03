package  com.CodingErgo.sticker.NewStickerManager.testView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.Point;
import android.graphics.RectF;
import android.text.BoringLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;

import com.CodingErgo.sticker.R;

import java.util.ArrayList;
import java.util.List;

public class testView extends View implements View.OnTouchListener {
    Bitmap mainBitmap;
    Point onStart;
    Point onClose;
    boolean isStepOne , isStepTwo;
    List<Point>points;
    Paint mPaint;
    Path mPath;
    public testView(Context context) {
        super(context);
        OnCreate(null);
    }

    public testView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        OnCreate(attrs);
        this.setOnTouchListener(this);
    }

    public testView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        OnCreate(attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
    }


    public void OnCreate(@Nullable AttributeSet set){
        mainBitmap = BitmapFactory.decodeResource(getResources() , R.drawable.ppp);
        setFocusable(true);
        setFocusableInTouchMode(true);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mainBitmap = getResizedMap(mainBitmap , getWidth() -50, getHeight()-50);
            }
        });
        points = new ArrayList<>();
        invalidate();
    }

    private Bitmap getResizedMap(Bitmap mainBitmap, int width, int height) {
        Matrix m = new Matrix();
        RectF src = new RectF(0 ,0 , mainBitmap.getHeight(), mainBitmap.getWidth());
        RectF dest = new RectF(0,0,width , height);
        m.setRectToRect(src ,dest , Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(mainBitmap , 0,0 ,mainBitmap.getWidth() , mainBitmap.getHeight(), m , true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = (getWidth() - mainBitmap.getWidth()) / 2;
        int y = (getHeight() - mainBitmap.getHeight()) / 2;
        canvas.drawBitmap(mainBitmap ,x ,y , null);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setPathEffect(new DashPathEffect(new float[]{10,20,30} ,20));
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.RED);
        mPath = new Path();

        boolean First = true;
        for (int i = 0 ; i<  points.size() ; i+=2){
            Point Initial= points.get(i);
            if (First) {
                First = false;
                mPath.moveTo(Initial.x ,Initial.y);
            }else if (i < points.size() -1){
                Point next = points.get(i + 1);
                mPath.quadTo(Initial.x , Initial.y , next.x , next.y);
            }else {
                onClose = points.get(i);
                mPath.lineTo(Initial.x , Initial.y);
            }
        }
        canvas.drawPath(mPath ,mPaint);

    }
    public void setImageBitmap(Bitmap bm){
        mainBitmap = bm;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Point point = new Point();
        point.x = (int) event.getX();
        point.y = (int) event.getY();
        if (isStepOne) {
            if (isStepTwo){
                if (ComparePoint(onStart , point)){
                    points.add(onStart);
                    isStepOne = false;
                    // Todo: intent
                }else {
                    points.add(point);
                }
            }else {
                points.add(point);
            }
            if (!isStepTwo){
                isStepTwo= true;
                onStart = point;
            }
        }
        invalidate();
        if (event.getAction() == MotionEvent.ACTION_UP){
            onClose = point ;
            if (isStepOne){
                if (ComparePoint(onStart , onClose)){
                    if (points.size() > 12){
                        points.add(onStart);
                        //Todo: Intent
                    }
                }
            }

        }

        return true;
    }
    public  boolean ComparePoint(Point onStart , Point Current){
        int leftX = (int) Current.x - 3;
        int leftY = (int) Current.y -3;
        int rightX = (int) Current.x +3 ;
        int rightY = (int) Current.y +3;
        if (leftX < onStart.x && onStart.x < rightX && leftY < onStart.y && onStart.y < rightY ) {
            if (points.size() <10){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
}
