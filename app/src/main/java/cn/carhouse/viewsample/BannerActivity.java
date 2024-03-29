package cn.carhouse.viewsample;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import cn.carhouse.adapter.XQuickViewHolder;
import cn.carhouse.views.banner.BannerPagerAdapter;
import cn.carhouse.views.banner.BannerView;
import cn.carhouse.views.transformer.ScaleInTransformer;

public class BannerActivity extends AppCompatActivity {
    // 轮播图片
    private BannerView mBannerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        mBannerView = findViewById(R.id.banner_view);
        List<String> images = new ArrayList<>();
        images.add("https://desk-fd.zol-img.com.cn/t_s960x600c5/g5/M00/02/05/ChMkJ1bKyaOIB1YfAAusnvE99Z8AALIQQPgER4AC6y2052.jpg");
        images.add("https://desk-fd.zol-img.com.cn/t_s960x600c5/g5/M00/02/05/ChMkJlbKyaOILdoIAAh4myV9U2gAALIQQPXmGsACHiz186.jpg");
        images.add("https://desk-fd.zol-img.com.cn/t_s960x600c5/g5/M00/02/05/ChMkJ1bKyaOIB1YfAAusnvE99Z8AALIQQPgER4AC6y2052.jpg");
        images.add("https://desk-fd.zol-img.com.cn/t_s960x600c5/g5/M00/02/05/ChMkJlbKyZmIH6mxAAid0G3SjEoAALIQAAZxrIACJ3o977.jpg");
        BannerPagerAdapter<String> adapter = new BannerPagerAdapter<String>(images,R.layout.item_banner) {
            @Override
            protected void convert(XQuickViewHolder holder, String imageUrl, int position) {
                //View view = holder.getView(R.id.iv_icon);
                holder.displayRadiusImage(R.id.iv_icon, imageUrl,20);
            }
        };
        mBannerView.setPageTransformer(true,new ScaleInTransformer(0.9f));
        mBannerView.setViewPagerMargin(15);
        mBannerView.setPageMargin(8);
        mBannerView.setAdapter(adapter);
    }
}
