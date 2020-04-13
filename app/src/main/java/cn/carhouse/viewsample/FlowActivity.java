package cn.carhouse.viewsample;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import cn.carhouse.views.XTagLayout;
import cn.carhouse.views.adapter.XCommAdapter;
import cn.carhouse.views.adapter.XViewHolder;

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
