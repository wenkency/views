package cn.carhouse.viewsample;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.carhouse.views.XEditLayout;

public class EditActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        XEditLayout editLayout=findViewById(R.id.xet);
        editLayout.setOnRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditActivity.this, "one", Toast.LENGTH_SHORT).show();
            }
        });
        editLayout.setOnRightTwoClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditActivity.this, "two", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
