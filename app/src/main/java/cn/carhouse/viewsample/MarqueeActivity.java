package cn.carhouse.viewsample;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.carhouse.views.marquee.LooperLayoutManager;
import cn.carhouse.views.marquee.MarqueeRecyclerView;

public class MarqueeActivity extends AppCompatActivity {
    private MarqueeRecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marquee);
        recyclerView = findViewById(R.id.marquee);
        recyclerView.setAutoRun(true);
        recyclerView.setAdapter(new SimpleAdapter());
        LooperLayoutManager layoutManager = new LooperLayoutManager();
        layoutManager.setScrollVertical(false);
        layoutManager.setLooperEnable(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.start();
    }
}
