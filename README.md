# views
开发常用自定义View的集合。

### 引入

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}


implementation 'com.github.wenkency:views:1.0.0'

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
```

