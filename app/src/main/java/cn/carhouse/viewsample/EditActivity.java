package cn.carhouse.viewsample;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.carhouse.views.XEditLayout;

public class EditActivity extends AppCompatActivity {
    boolean isVisible = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        final XEditLayout editLayout = findViewById(R.id.xet);
        editLayout.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editLayout.setOnRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isVisible = !isVisible;
                editLayout.changePassInputType(isVisible);
            }
        });
        editLayout.setOnRightTwoClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditActivity.this, "two", Toast.LENGTH_SHORT).show();
            }
        });
        editLayout.setOnRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditActivity.this, "one", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
