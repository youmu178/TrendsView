package com.youmu.trendsview.weight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.youmu.trendsview.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by youzehong on 15/11/15.
 */
public class TrendsView extends View {

    private ViewThread mThread;
    private boolean isRunning = true;

    private ArrayList<Item> mItemList = new ArrayList<>();

    public TrendsView(Context context) {
        this(context, null);
    }

    public TrendsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrendsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initData() {
        for (int i = 0; i < 100; i++) {
            Item item = new Item(getWidth(), getHeight());
            mItemList.add(item);
        }
    }

    private Bitmap mMoveBitmap;

    public class Item {
        private float mLeft;
        private float mTop;
        private float mOpt;
        private Paint mPaint;
        private int mScreenWidth;
        private int mScreenHeight;
        Random mRandom = new Random();

        public Item(int width, int height) {
            this.mScreenWidth = width;
            this.mScreenHeight = height;
            init();
            if (mMoveBitmap == null) {
                mMoveBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.leaf);
            }
        }

        private void init() {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mLeft = mRandom.nextInt(mScreenWidth);
            mTop = mRandom.nextInt(mScreenHeight);
            mOpt = mRandom.nextFloat();
//            mOpt = mRandom.nextInt(100) - 80 + 1;
        }

        private void onMove() {
            mLeft += 20 * mOpt;
            mTop += 30 * mOpt;
            if (mTop > mScreenHeight) {
                init();
            }
        }

        private void onDraw(Canvas canvas) {
            canvas.drawBitmap(mMoveBitmap, mLeft, mTop, mPaint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        postDelayed(mRunnable, 30);
        if (mThread == null) {
            mThread = new ViewThread();
            mThread.start();
        } else {
            for (Item item : mItemList) {
                item.onDraw(canvas);
            }
        }
    }

    class ViewThread extends Thread {
        @Override
        public void run() {
            initData();
            while (isRunning) {
                for (Item item : mItemList) {
                    item.onMove();
                }
                postInvalidate();
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    Runnable mRunnable = new Runnable() {
//        @Override
//        public void run() {
//            initData();
//            // 移动
//
//        };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        removeCallbacks(mRunnable);
        isRunning = false;
    }
}
