package cn.carhouse.viewsample;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import cn.carhouse.views.XFlowLayout;
import cn.carhouse.views.adapter.XCommAdapter;
import cn.carhouse.views.adapter.XViewHolder;

public class FlowActivity extends AppCompatActivity {

    private XCommAdapter<String> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);
        final XFlowLayout tagLayout = findViewById(R.id.tag_layout);
        final List<String> data = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            data.add("itemitem" + i);
        }
        adapter = new XCommAdapter<String>(this, R.layout.item_flow_layout) {
            @Override
            public void convert(XViewHolder holder, final String item, final int position) {
                holder.setText(R.id.tv, item);
            }
        };
        tagLayout.setAdapter(adapter);

        adapter.replaceAll(data);

    }

    public void add(View view) {
        adapter.add("item" + adapter.getCount());
    }
}
