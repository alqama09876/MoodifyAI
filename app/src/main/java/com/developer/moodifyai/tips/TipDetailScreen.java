package com.developer.moodifyai.tips;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.developer.moodifyai.R;

public class TipDetailScreen extends AppCompatActivity {
    ImageView btn_back, tipImage;
    TextView tipTitle, tipDescription, tipExample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tip_detail_screen);

        btn_back = findViewById(R.id.btn_back);
        tipImage = findViewById(R.id.tip_image);
        tipTitle = findViewById(R.id.tip_title);
        tipDescription = findViewById(R.id.tip_description);
        tipExample = findViewById(R.id.tip_example);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(TipDetailScreen.this, TipsCategoryScreen.class));
                finish();
            }
        });

        int imageResId = getIntent().getIntExtra("TIP_IMAGE", R.drawable.slap);
        String title = getIntent().getStringExtra("TIP_TITLE");
        String description = getIntent().getStringExtra("TIP_DESCRIPTION");
        String example = getIntent().getStringExtra("TIP_EXAMPLE");

        tipImage.setImageResource(imageResId);
        tipTitle.setText(title);
        tipDescription.setText(description);
        tipExample.setText(example);
    }
}