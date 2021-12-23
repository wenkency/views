package cn.carhouse.viewsample;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.carhouse.adapter.XQuickAdapter;
import cn.carhouse.adapter.XQuickViewHolder;
import cn.carhouse.viewsample.stick.StickActivity;

/**
 * 测试类
 */
public class TestActivity extends AppCompatActivity {

    private List<Class> data;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initData();
        initViews();
    }

    private void initData() {
        data = new ArrayList<>();
        data.add(BannerActivity.class);
        data.add(XTabActivity.class);
        data.add(FlowActivity.class);
        data.add(StickActivity.class);
        data.add(TextActivity.class);
        data.add(EditActivity.class);
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new XQuickAdapter<Class>(this, data, R.layout.item_activity_test) {
            @Override
            protected void convert(XQuickViewHolder holder, Class item, int position) {
                holder.setText(R.id.btn_text,item.getSimpleName().replace("Activity",""));
                holder.setOnClickListener(R.id.btn_text, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(item);
                    }
                });
            }
        });
    }
}
