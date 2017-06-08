package com.example.yls.wuziqi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yls on 2017/6/8.
 */

public class WuZiQiPanel extends View{

    private int mPanelWidth;
    private float mLinearHeight;
    private static final int MAX_LINE=10;
    public static final int MAX_PIECES_NUMBER=MAX_LINE*MAX_LINE;
    private Paint mPaint=new Paint();
    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;
    private static final float RATIO_PIECE=3*1.0f/4;
    private boolean mIsWhite=true;
    private List<Point> mWhiteArray=new ArrayList<>();
    private List<Point> mBlackArray=new ArrayList<>();
    private boolean mIsGameOver;
    private int mResult;
    public static final int DRAW=0;
    public static final int WHITE_WON=1;
    public static final int BLACK_WON=2;
    private ResultListener mListener;

    public void setListener(ResultListener listener) {
        mListener=listener;
    }

    public WuZiQiPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint.setColor(0xff000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mWhitePiece= BitmapFactory.decodeResource(getResources(),R.drawable.stone_w2);
        mBlackPiece= BitmapFactory.decodeResource(getResources(),R.drawable.stone_b1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);

        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int length=Math.min(widthSize,heightSize);
        if(widthMode==MeasureSpec.UNSPECIFIED) {
            length=heightSize;
        } else if(heightMode==MeasureSpec.UNSPECIFIED) {
            length=widthSize;
        }
        setMeasuredDimension(length,length);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w,h,oldw,oldh);
        mPanelWidth=w;
        mLinearHeight=mPanelWidth*1.0f/MAX_LINE;
        int pieceWidth=(int)(mLinearHeight*RATIO_PIECE);
        mWhitePiece=Bitmap.createScaledBitmap(mWhitePiece,pieceWidth,pieceWidth,false);
        mBlackPiece=Bitmap.createScaledBitmap(mBlackPiece,pieceWidth,pieceWidth,false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawPieces(canvas);
        checkGameOver();
    }

    private void checkGameOver() {
        boolean isWhiteWon = WuZiQiUtils.checkFiveInLine(mWhiteArray);
        boolean isBlackWon = WuZiQiUtils.checkFiveInLine(mBlackArray);
        if(isWhiteWon||isBlackWon) {
            mIsGameOver=true;
            mResult=isWhiteWon?WHITE_WON:BLACK_WON;
            mListener.showResult(mResult);
            return;
        }

        boolean isFull=WuZiQiUtils.checkIsFull(mWhiteArray.size()+mBlackArray.size());
        if(isFull) {
            mResult=DRAW;
            mListener.showResult(mResult);
        }
    }

    private void drawPieces(Canvas canvas) {
        for(Point whitePoint:mWhiteArray) {
            canvas.drawBitmap(mWhitePiece,(whitePoint.x+(1-RATIO_PIECE)/2)*mLinearHeight,(whitePoint.y+(1-RATIO_PIECE)/2)*mLinearHeight,null);
        }
        for(Point blackPoint:mBlackArray) {
            canvas.drawBitmap(mBlackPiece,(blackPoint.x+(1-RATIO_PIECE)/2)*mLinearHeight,(blackPoint.y+(1-RATIO_PIECE)/2)*mLinearHeight,null);
        }
    }

    private void drawBoard(Canvas canvas) {
        for (int i=0;i<MAX_LINE;i++) {
            int startX=(int)mLinearHeight/2;
            int endX=(int)(mPanelWidth-mLinearHeight/2);
            int y=(int)((0.5+i)*mLinearHeight);
            canvas.drawLine(startX,y,endX,y,mPaint);
            canvas.drawLine(y,startX,y,endX,mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mIsGameOver) return false;
        int action=event.getAction();
        if(action==MotionEvent.ACTION_UP) {
            int x=(int)event.getX();
            int y=(int)event.getY();
            Point point=getValidPoint(x,y);
            if(mWhiteArray.contains(point)||mBlackArray.contains(point)) {
                return false;
            }

            if(mIsWhite) {
                mWhiteArray.add(point);
            } else {
                mBlackArray.add(point);
            }
            invalidate();
            mIsWhite=!mIsWhite;
            return true;
        }
        return true;
    }

    private Point getValidPoint(int x, int y) {
        return new Point((int)(x/mLinearHeight),(int)(y/mLinearHeight));
    }

    private static final String INSTANCE="instance";
    private static final String INSTANCE_GAME_OVER="instance_game_over";
    private static final String INSTANCE_WHITE_ARRAY="instance_white_array";
    private static final String INSTANCE_BLACK_ARRAY="instance_black_array";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle=new Bundle();
        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER,mIsGameOver);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY,(ArrayList)mWhiteArray);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY,(ArrayList)mBlackArray);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if(state instanceof Bundle) {
            Bundle bundle=(Bundle)state;
            mIsGameOver=bundle.getBoolean(INSTANCE_GAME_OVER);
            mWhiteArray=bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBlackArray=bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    protected void restart() {
        mWhiteArray.clear();
        mBlackArray.clear();
        mIsGameOver=false;
        invalidate();
    }
}