# views
开发常用自定义View的集合，
常用流式布局：XFlowLayout(多一个显示最大行数功能)、XTagLayout（margin padding可以设置）
九宫格：XGridLayout
侧划删除：SwipeMenuLayout
物流进度：StepProgressLayout
字母：LetterView
指示器：XTabLayout
轮播图：BannerView
悬浮至顶：StickFrameLayout

### 引入

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}


implementation 'com.github.wenkency:views:2.2.0'
implementation 'com.github.wenkency:quickadapter:1.7.0'
// 基于Glide图片加载封装
implementation 'com.github.wenkency:imageloader:2.7.0'
// Glide图片加载库
implementation "com.github.bumptech.glide:glide:4.11.0"
annotationProcessor "com.github.bumptech.glide:compiler:4.11.0"

```

### 使用方式
```
public class BannerActivity extends AppCompatActivity {
    // 轮播图片
    private BannerView mBannerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        mBannerView=findViewById(R.id.banner_view);
        List<String> images=new ArrayList<>();
        images.add("https://img.car-house.cn/Upload/activity/20200219/EEAmKbASEh3T3ECXFwT8D2e8m56BGhBe.jpg");
        images.add("https://img.car-house.cn/Upload/activity/20200226/TZ5izRtnAzWGHSYKWeBBx6yhPFR62aX2.png");
        images.add("https://img.car-house.cn/Upload/activity/20200304/sKFHyzZwTPBMzbjGyFTzMKsyYQEAZF6h.jpg");
        mBannerView.setAdapter(new BannerPagerAdapter<String>(images,R.layout.item_banner) {
            @Override
            protected void convert(XQuickViewHolder holder, String imageUrl, int position) {
                holder.displayImage(R.id.iv_icon,imageUrl);
            }
        });
    }
}
// ==============================================================================
// 底部的Tab + ViewPager
public class MainActivity extends AppCompatActivity {

    private XCommAdapter<String> tabCommAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 底部的Tab
        XTabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);
        final List<String> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            data.add("");
        }
        viewPager.setAdapter(new XQuickPagerAdapter<String>(data, R.layout.item_view_pager, false) {
            @Override
            protected void convert(XQuickViewHolder holder, String data, int position) {
                holder.setText(R.id.tv, "ViewPager" + position);
            }
        });

        tabCommAdapter = new XCommAdapter<String>(this, data, R.layout.item_tab_layout) {
            @Override
            public void convert(XViewHolder holder, String item, int position) {
                holder.setText(R.id.tv, "Tab" + position);
                String url = "https://img.car-house.cn/Upload/activity/20200226/TZ5izRtnAzWGHSYKWeBBx6yhPFR62aX2.png";
                if (position % 3 == 1) {
                    url = "https://img.car-house.cn/Upload/activity/20200219/EEAmKbASEh3T3ECXFwT8D2e8m56BGhBe.jpg";
                }
                if (position % 3 == 2) {
                    url = "https://img.car-house.cn/Upload/activity/20200304/sKFHyzZwTPBMzbjGyFTzMKsyYQEAZF6h.jpg";
                }
                holder.displayImage(R.id.iv_icon, url);
            }

            @Override
            public void convertTabReset(XViewHolder holder, String item, int position) {
                holder.setBold(R.id.tv, false);
            }

            @Override
            public void convertTabSelected(XViewHolder holder, String item, int position) {
                holder.setBold(R.id.tv, true);
            }

            @Override
            public View getTabBottomLineView(ViewGroup parent) {
                View view = new View(parent.getContext());
                view.setBackgroundColor(Color.RED);
                return view;
            }
        };
        // 设置底部线宽高和距离底部高度
        tabLayout.setLineWidth(100);
        tabLayout.setLineHeight(20);
        tabLayout.setTabLineBottomMargin(10);
        tabLayout.setTabEqual(true);

        tabLayout.setAdapter(tabCommAdapter, viewPager);

    }
}
// ==============================================================================
// 流式布局
public class FlowActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);
        final XTagLayout tagLayout = findViewById(R.id.tag_layout);
        final List<String> data = new ArrayList<>();
        for (int i = 0; i < 35; i++) {
            data.add("");
        }
        final XCommAdapter<String> adapter = new XCommAdapter<String>(this, data, R.layout.item_flow_layout) {
            @Override
            public void convert(XViewHolder holder, String item, final int position) {
                holder.setText(R.id.tv, "TagTag" + position);
                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(FlowActivity.this, "Tag" + position, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        tagLayout.setAdapter(adapter);
    }
}

```

