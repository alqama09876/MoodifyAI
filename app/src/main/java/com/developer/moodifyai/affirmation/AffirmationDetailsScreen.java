package com.developer.moodifyai.affirmation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.developer.moodifyai.R;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class AffirmationDetailsScreen extends AppCompatActivity {
    ImageView userImage, btn_back, share_icon;
    TextView affirmationTitle, affirmationText;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_affirmation_details_screen);

        userImage = findViewById(R.id.user_image);
        affirmationTitle = findViewById(R.id.affirmation_title);
        affirmationText = findViewById(R.id.affirmation_text);
        btn_back = findViewById(R.id.btn_back);
        progressBar = findViewById(R.id.progressBar);
        share_icon = findViewById(R.id.share_icon);

        progressBar.setVisibility(View.VISIBLE);

        String title = getIntent().getStringExtra("AFFIRMATION_TITLE");
        String text = getIntent().getStringExtra("AFFIRMATION_TEXT");
        String imageUrl = getIntent().getStringExtra("AFFIRMATION_IMAGE");

        affirmationTitle.setText(title);
        affirmationText.setText(text);

        Glide.with(this)
                .load(imageUrl)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(15, 2)))
                .into(userImage);

        progressBar.setVisibility(View.GONE);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        share_icon.setOnClickListener(v -> {
            shareAffirmation(text);
        });
    }

    private void shareAffirmation(String affirmationText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, affirmationText);

        Intent chooser = Intent.createChooser(shareIntent, "Share via");
        startActivity(chooser);
    }
}