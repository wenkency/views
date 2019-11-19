package cn.carhouse.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 右边A-Z 个字母的View
 */

public class LetterView extends View {

    private int normalBgColor = Color.parseColor("#0d000000");  // 正常的背景色
    private int pressBgColor = Color.parseColor("#1a000000");  // 按下的背景色
    private int pressTextColor = Color.parseColor("#dd2828");// 按下的字母颜色
    private int normalTextColor = Color.parseColor("#333333");// 正常的字母颜色
    public int TEXT_SIZE = 12;// 默认字体的大小
    private List<Character> words;// # A-Z的集合
    // 第个条目的宽高
    private int itemWidth;
    private int itemHeight;
    // 第个字母的宽高
    private int wordWidth;
    private int wordHeight;

    private Paint mPaint;
    private int currentIndex = -1;// 当前的移动位置
    private OnWordChangeListener listener;


    public LetterView(Context context) {
        this(context, null);
    }

    public LetterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    private void initData() {
        // 初始化集合的数据
        words = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            char word = (char) ('A' + i);
            words.add(word);
        }
        words.add('#');
        // 初始化画笔
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(dip2px(TEXT_SIZE));
        mPaint.setTextAlign(Paint.Align.CENTER);
        // 计算每个字母的宽高
        Rect bounds = new Rect();
        mPaint.getTextBounds("A", 0, 1, bounds);
        wordWidth = bounds.width();
        wordHeight = bounds.height();
    }

    /**
     * dip----to---px
     *
     * @return
     */
    public int dip2px(int dip) {
        // 缩放比例(密度)
        float density = getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 计算每个Item的宽高
        itemWidth = getMeasuredWidth();
        itemHeight = (getMeasuredHeight() - getPaddingTop() - getPaddingBottom()) / words.size();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < words.size(); i++) {
            // 计算出绘制的X Y坐标
            int wordX = (itemWidth) / 2;
            int wordY = (itemHeight + wordHeight) / 2 + i * itemHeight + getPaddingTop();
            if (i == currentIndex) {
                // 按中的字体颜色
                mPaint.setColor(pressTextColor);
                canvas.drawText(words.get(i) + "", wordX, wordY, mPaint);
            } else {
                mPaint.setColor(normalTextColor);
                canvas.drawText(words.get(i) + "", wordX, wordY, mPaint);
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 按下的背景色
                break;
            case MotionEvent.ACTION_MOVE:
                // 计算出当前移动的位置
                int index = (int) ((event.getY() - getPaddingTop()) / itemHeight);
                if (index != currentIndex) {
                    currentIndex = index;
                    invalidate();
                    if (currentIndex >= 0 && listener != null && currentIndex < words.size()) {
                        listener.onWordChanged(words.get(currentIndex) + "");
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 抬起的背景色
                currentIndex = -1;
                if (listener != null) {
                    listener.onChangeUp();
                }
                invalidate();
                break;
        }
        return true;
    }

    /**
     * 移动的位置发生改变
     */
    public interface OnWordChangeListener {
        void onWordChanged(String word);

        void onChangeUp();
    }

    public void setOnWordChangeListener(OnWordChangeListener listener) {
        this.listener = listener;
    }
}
