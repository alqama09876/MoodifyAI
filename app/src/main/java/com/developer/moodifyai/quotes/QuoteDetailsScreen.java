package com.developer.moodifyai.quotes;

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

public class QuoteDetailsScreen extends AppCompatActivity {

    ImageView userImage, btn_back, share_icon;
    TextView quoteTitle, quoteText, quoteAuthor;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quote_details_screen);

        userImage = findViewById(R.id.user_image);
        quoteTitle = findViewById(R.id.quote_title);
        quoteText = findViewById(R.id.quote_text);
        quoteAuthor = findViewById(R.id.quote_author);
        btn_back = findViewById(R.id.btn_back);
        progressBar = findViewById(R.id.progressBar);
        share_icon = findViewById(R.id.share_icon);

        progressBar.setVisibility(View.VISIBLE);

        String title = getIntent().getStringExtra("QUOTE_TITLE");
        String text = getIntent().getStringExtra("QUOTE_TEXT");
        String author = getIntent().getStringExtra("QUOTE_AUTHOR");
        String imageUrl = getIntent().getStringExtra("QUOTE_IMAGE");

        quoteTitle.setText(title);
        quoteText.setText(text);
        quoteAuthor.setText(author);

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
            shareQuote(text);
        });
    }

    private void shareQuote(String quoteText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, quoteText);

        Intent chooser = Intent.createChooser(shareIntent, "Share via");
        startActivity(chooser);
    }
}