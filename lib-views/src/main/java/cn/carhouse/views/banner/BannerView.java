package cn.carhouse.views.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    private RelativeLayout mBannerBottom;
    private TextView mBannerDesc;
    private LinearLayout mBannerPoints, mBannerPoints2;
    private TextView mTvSize, mTvSizeCount;
    private BannerPagerAdapter<T> mAdapter;

    private int mPointSelectedColor = Color.parseColor("#ffffffff");// 点的选中颜色
    private int mPointUnSelectedColor = Color.parseColor("#77ffffff");// 点的未选颜色
    private int mBottomColor = Color.parseColor("#00ffffff");// 底部背景角色
    private float mRadius;// 点的圆角
    private int mPosition = -1;
    private int mPointWidth, mPointHeight;// 点的宽高
    private int mPointDistance;// 点的间距
    private int mPointGravity = 1;// 左：-1  中：0   右：1

    private int mPointStyle = STYLE_POINT;// 0 圆  1 是点 默认的
    private static final int STYLE_CIRCLE = 0;//  圆
    private static final int STYLE_POINT = 1;//  1 是点 默认的

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
        // 点的间距
        // mPointDistance = (int) array.getDimension(R.styleable.BannerView_point_distance, dip2px(5));
        mPointDistance = dip2px(3);
        // 点的圆角
        mRadius = array.getDimension(R.styleable.BannerView_point_radius, dip2px(3));
        // 点的颜色
        mPointSelectedColor = array.getColor(R.styleable.BannerView_point_color_selected, mPointSelectedColor);
        mPointUnSelectedColor = array.getColor(R.styleable.BannerView_point_color_unselected, mPointUnSelectedColor);
        // 底部背景角色
        mBottomColor = array.getColor(R.styleable.BannerView_banner_bottom_bg, mBottomColor);
        // 点的位置
        mPointGravity = array.getInt(R.styleable.BannerView_point_gravity, mPointGravity);
        mPointStyle = array.getInt(R.styleable.BannerView_point_style, mPointStyle);


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
        mBannerDesc = findViewById(R.id.m_banner_desc);
        mBannerPoints = findViewById(R.id.m_banner_points);
        mBannerPoints2 = findViewById(R.id.m_banner_points2);

        mTvSize = findViewById(R.id.id_tv_size);
        mTvSizeCount = findViewById(R.id.id_tv_size2);
        // 点的位置
        mBannerPoints.setGravity(getPointGravity());
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
    }

    public void setAdapter(BannerPagerAdapter<T> adapter) {
        setAdapter(adapter, true);
    }

    /**
     * 设置适配器
     *
     * @param adapter
     */
    public void setAdapter(BannerPagerAdapter<T> adapter, boolean isLoop) {
        mBannerPoints.removeAllViews();

        int size = adapter.getDataSize();
        // 当数据只有一条的时候点不出来
        if (size <= 1) {
            mBannerPoints.setVisibility(INVISIBLE);
            // 不轮播
            setLoop(false);
        } else {

            if (isShowPoint) {
                mBannerPoints.setVisibility(VISIBLE);
            } else {
                mBannerPoints.setVisibility(INVISIBLE);
            }
            setLoop(isLoop);
        }

        mAdapter = adapter;
        mBannerPager.setAdapter(adapter);

        // 初始化点
        if (mPointStyle == STYLE_POINT) {
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
        } else if (mPointStyle == STYLE_CIRCLE) {
            mBannerPoints.setVisibility(GONE);
            mBannerPoints2.setVisibility(VISIBLE);

        }
        pageSelected(0);
        mBannerPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // 改变选中的状态
                pageSelected(position % mAdapter.getDataSize());
            }
        });
        // Banner的描述
        String desc = mAdapter.getBannerDesc(0);
        if (desc != null) {
            mBannerDesc.setText(desc);
        }
        // 设置图片的宽高比
        if (mWidthRadio != 0 && mHeightRadio != 0) {
            int measuredWidth = getScreenWidth();
            int height = (int) (measuredWidth * mHeightRadio / mWidthRadio + 0.5f);
            getLayoutParams().height = height;
        }
        if (isLoop) {
            // 向前面移动100
            mBannerPager.setCurrentItem(mAdapter.getDataSize() * 100, false);
        }
        // 开启轮播
        startRoll();
    }

    public void startRoll() {
        if (mBannerPager != null) {
            mBannerPager.startRoll();
        }

    }

    public void stopRoll() {
        if (mBannerPager != null)
            mBannerPager.stopRoll();
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
                if (mPointStyle == STYLE_POINT) {
                    // 选中的变色
                    BannerPointView currPoint = (BannerPointView) mBannerPoints.getChildAt(position);
                    currPoint.setColorAndRadius(mPointSelectedColor, mRadius);
                    // 上一个点也变色
                    BannerPointView prePoint = (BannerPointView) mBannerPoints.getChildAt(mPosition);
                    if (prePoint != null) {
                        prePoint.setColorAndRadius(mPointUnSelectedColor, mRadius);
                    }
                    mPosition = position;
                    // Banner的描述
                    String desc = mAdapter.getBannerDesc(mPosition);
                    if (desc != null) {
                        mBannerDesc.setText(desc);
                    }
                } else if (mPointStyle == STYLE_CIRCLE) {
                    mPosition = position;
                    mTvSize.setText(mPosition + 1 + "/");
                    int size = mAdapter.getDataSize();
                    mTvSizeCount.setText(size + "");
                }
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
