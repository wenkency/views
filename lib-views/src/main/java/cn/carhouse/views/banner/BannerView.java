package cn.carhouse.views.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.ViewPager;

import cn.carhouse.views.R;


/**
 * 轮播控件
 */

/**
 * <cn.carhouse.yctone.view.banner.BannerView
 * android:id="@+id/banner_view"
 * android:layout_width="match_parent"
 * android:layout_height="0dp"
 * app:height_radio="10"
 * app:point_distance="5dp"
 * app:point_gravity="right"
 * app:point_height="3dp"
 * app:point_radius="1dp"
 * app:point_width="12dp"
 * app:width_radio="17.1" />
 */
public class BannerView<T> extends RelativeLayout {
    private BannerViewPager mBannerPager;
    private FrameLayout mBannerBottom;
    private LinearLayout mBannerPoints;
    private BannerPagerAdapter<T> mAdapter;

    private int mPointSelectedColor = Color.parseColor("#ffffffff");// 点的选中颜色
    private int mPointUnSelectedColor = Color.parseColor("#77ffffff");// 点的未选颜色
    private int mBottomColor = Color.parseColor("#00ffffff");// 底部背景角色
    private float mRadius;// 点的圆角
    private int mPosition = -1;
    private int mPointWidth, mPointHeight;// 点的宽高
    private int mPointSelectedWidth, mPointSelectedHeight;// 选中点的宽高
    private int mPointDistance;// 点的间距
    private int mPointBottomDistance;// 点底部间距
    private int mPointGravity = 0;// 左：-1  中：0   右：1
    private float mWidthRadio, mHeightRadio;// 图片的宽高比
    private OnPageSelectedListener mOnPageSelectedListener;
    /**
     * 是否显示点
     */
    private boolean isShowPoint = true;


    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 加载布局
        inflate(context, R.layout.layout_view_banner, this);
        // 加载自定义的属性
        obtainStyledAttributes(context, attrs);
        // 初始化View
        initViews();

    }

    private void obtainStyledAttributes(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BannerView);
        // 点的宽高
        mPointWidth = (int) array.getDimension(R.styleable.BannerView_point_width, dip2px(5));
        mPointHeight = (int) array.getDimension(R.styleable.BannerView_point_height, dip2px(5));
        // 选中点的宽高
        mPointSelectedWidth = (int) array.getDimension(R.styleable.BannerView_point_select_width, mPointWidth);
        mPointSelectedHeight = (int) array.getDimension(R.styleable.BannerView_point_select_height, mPointHeight);
        // 点的间距
        mPointDistance = (int) array.getDimension(R.styleable.BannerView_point_distance, dip2px(5));
        mPointBottomDistance = (int) array.getDimension(R.styleable.BannerView_point_bottom_distance, dip2px(6));
        // 点的圆角
        mRadius = array.getDimension(R.styleable.BannerView_point_radius, dip2px(5));
        // 点的颜色
        mPointSelectedColor = array.getColor(R.styleable.BannerView_point_color_selected, mPointSelectedColor);
        mPointUnSelectedColor = array.getColor(R.styleable.BannerView_point_color_unselected, mPointUnSelectedColor);
        // 底部背景角色
        mBottomColor = array.getColor(R.styleable.BannerView_banner_bottom_bg, mBottomColor);
        // 点的位置
        mPointGravity = array.getInt(R.styleable.BannerView_point_gravity, mPointGravity);
        mWidthRadio = array.getFloat(R.styleable.BannerView_radio_width, 0);
        mHeightRadio = array.getFloat(R.styleable.BannerView_radio_height, 0);
        array.recycle();
    }

    /**
     * 初始化View
     */
    private void initViews() {
        mBannerPager = findViewById(R.id.m_banner_pager);
        mBannerBottom = findViewById(R.id.m_banner_bottom);
        mBannerBottom.setBackgroundColor(mBottomColor);
        mBannerPoints = findViewById(R.id.m_banner_points);
        // 点的位置
        mBannerPoints.setGravity(getPointGravity());
        // 点距离底部距离
        setBottomDistance(mPointBottomDistance);
    }

    /**
     * 点距离底部距离
     */
    public void setBottomDistance(int distance) {
        // 点
        mBannerBottom.setPadding(mBannerBottom.getPaddingLeft(),
                mBannerBottom.getPaddingTop(),
                mBannerBottom.getPaddingRight(), distance);
    }

    /**
     * 获取点的位置
     */
    private int getPointGravity() {
        switch (mPointGravity) {
            case -1:
                return Gravity.LEFT;
            case 0:
                return Gravity.CENTER;
            case 1:
                return Gravity.RIGHT;
        }
        return Gravity.CENTER;
    }

    public void setPointGravity(int gravity) {
        mPointGravity = gravity;
        // 点的位置
        mBannerPoints.setGravity(getPointGravity());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpec = widthMeasureSpec;
        int heightSpec = heightMeasureSpec;
        // 设置图片的宽高比-- mWidthRadio = 9  mHeightRadio = 5   9/5
        if (mWidthRadio != 0 && mHeightRadio != 0) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width * mHeightRadio / mWidthRadio + 0.5f);
            widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
            heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        } else if (mWidthRadio != 0) {
            // mWidthRadio = 1.8 = 9/5
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width / mWidthRadio + 0.5f);
            widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
            heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        } else if (mHeightRadio != 0) {
            // mHeightRadio = 1.8 = 9/5
            int height = MeasureSpec.getSize(heightMeasureSpec);
            int width = (int) (height * mHeightRadio + 0.5f);
            widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
            heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthSpec, heightSpec);

    }

    public void setAdapter(BannerPagerAdapter<T> adapter) {
        setAdapter(adapter, true);
    }

    private DataSetObserver dataSetObserver;
    private ViewPager.SimpleOnPageChangeListener changeListener;
    private boolean isLoop;

    /**
     * 设置适配器
     *
     * @param adapter
     */
    public void setAdapter(BannerPagerAdapter<T> adapter, boolean isLoop) {
        mAdapter = adapter;
        this.isLoop = isLoop;
        mBannerPager.setAdapter(adapter);
        // 设置数据发生改变监听
        if (dataSetObserver != null) {
            mAdapter.registerDataSetObserver(dataSetObserver);
        }
        if (dataSetObserver == null) {
            dataSetObserver = new DataSetObserver() {
                @Override
                public void onChanged() {
                    setPoints();
                }
            };
        }
        mAdapter.registerDataSetObserver(dataSetObserver);

        setPoints();

        pageSelected(0);

        if (changeListener != null) {
            mBannerPager.removeOnPageChangeListener(changeListener);
        }
        if (changeListener == null) {
            changeListener = new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    // 改变选中的状态
                    pageSelected(position % mAdapter.getDataSize());
                }
            };
        }
        mBannerPager.addOnPageChangeListener(changeListener);

        // 开启轮播
        startRoll();
    }

    // 设置点
    private void setPoints() {
        setLoop(isLoop);
        // 点的控制
        if (isShowPoint) {
            mBannerPoints.setVisibility(VISIBLE);
        } else {
            mBannerPoints.setVisibility(INVISIBLE);
        }
        mBannerPoints.removeAllViews();
        int size = mAdapter.getDataSize();
        // 初始化点
        for (int i = 0; i < size; i++) {
            BannerPointView pointView = new BannerPointView(getContext());
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(mPointWidth, mPointHeight);
            ll.leftMargin = mPointDistance;
            mBannerPoints.addView(pointView, ll);
            if (i == 0) {
                pointView.setColorAndRadius(mPointSelectedColor, mRadius);
            } else {
                pointView.setColorAndRadius(mPointUnSelectedColor, mRadius);
            }
        }
        if (size > 1 && mAdapter.isLooper()) {
            // 向前面移动100
            mBannerPager.setCurrentItem(mAdapter.getDataSize() * 100, false);
        }
        // 如果只有一个点就不轮播了
        if (size == 1) {
            mBannerPoints.setVisibility(INVISIBLE);
            setLoop(false);
        }
    }


    public void startRoll() {
        if (mBannerPager != null) {
            mBannerPager.startRoll();
        }
    }

    public void stopRoll() {
        if (mBannerPager != null) {
            mBannerPager.stopRoll();
        }
    }

    /**
     * 获取屏幕宽度(px)
     */
    public int getScreenWidth() {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int width = (int) Math.floor(dm.widthPixels);
        return width;
    }

    private void pageSelected(int position) {
        try {
            if (mPosition != position) {
                if (mOnPageSelectedListener != null) {
                    mOnPageSelectedListener.onPageSelected(position);
                }
                // 选中的变色
                BannerPointView currPoint = (BannerPointView) mBannerPoints.getChildAt(position);
                LinearLayout.LayoutParams ll = (LinearLayout.LayoutParams) currPoint.getLayoutParams();
                ll.width = mPointSelectedWidth;
                ll.height = mPointSelectedHeight;
                ll.leftMargin = mPointDistance;
                currPoint.setLayoutParams(ll);
                currPoint.setColorAndRadius(mPointSelectedColor, mRadius);
                // 上一个点也变色
                BannerPointView prePoint = (BannerPointView) mBannerPoints.getChildAt(mPosition);
                if (prePoint != null) {
                    ll = (LinearLayout.LayoutParams) prePoint.getLayoutParams();
                    ll.width = mPointWidth;
                    ll.height = mPointHeight;
                    ll.leftMargin = mPointDistance;
                    prePoint.setLayoutParams(ll);
                    prePoint.setColorAndRadius(mPointUnSelectedColor, mRadius);
                }
                mPosition = position;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int dip2px(int dip) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics()) + 0.5);
    }

    /**
     * 设置滚动的时间
     */
    public void setRollTime(long rollTime) {
        mBannerPager.setRollTime(rollTime);
    }

    /**
     * 设置是否轮播
     *
     * @param isLoop
     */
    public void setLoop(boolean isLoop) {
        if (mBannerPager != null) {
            mBannerPager.setLoop(isLoop);
        }
    }

    /**
     * ViewPager划动速度时间
     */
    public void setDuration(int duration) {
        mBannerPager.setDuration(duration);
    }

    /**
     * ViewPager滚动监听
     */
    public void addOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        if (mBannerPager != null) {
            mBannerPager.addOnPageChangeListener(listener);
        }
    }

    public void setCurrentItem(int position) {
        if (mBannerPager != null) {
            mBannerPager.setCurrentItem(position);
        }
    }


    public interface OnPageSelectedListener {
        void onPageSelected(int position);
    }

    /**
     * 获取ViewPager
     */
    public ViewPager getViewPager() {
        return mBannerPager;
    }

    public void setOnPageSelectedListener(OnPageSelectedListener onPageSelectedListener) {
        mOnPageSelectedListener = onPageSelectedListener;
    }


    public void setShowPoint(boolean showPoint) {
        isShowPoint = showPoint;
    }

}
