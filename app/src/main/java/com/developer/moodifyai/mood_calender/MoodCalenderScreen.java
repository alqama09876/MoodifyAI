package com.developer.moodifyai.mood_calender;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.developer.moodifyai.DashboardScreen;
import com.developer.moodifyai.R;
import com.developer.moodifyai.adapter.MonthlyCalenderAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoodCalenderScreen extends AppCompatActivity {
    LinearLayout ll_weekly, ll_monthly;
    ImageView btn_back, btn_previous, btn_next;
    TextView tv_monthYear, tv_weekly, tv_monthly;
    RecyclerView rv_monthly_calendar;
    ViewPager2 vp_weekly_calendar;
    FirebaseAuth auth;
    FirebaseFirestore db;
    int currentMonth;
    int currentYear;
    MonthlyCalenderAdapter monthlyCalendarAdapter;
    Map<String, Integer> moodMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mood_calender_screen);

        btn_back = findViewById(R.id.btn_back);
        btn_previous = findViewById(R.id.btn_previous);
        btn_next = findViewById(R.id.btn_next);
        tv_monthYear = findViewById(R.id.tv_monthYear);
        tv_weekly = findViewById(R.id.tv_weekly);
        tv_monthly = findViewById(R.id.tv_monthly);
        ll_weekly = findViewById(R.id.ll_weekly);
        ll_monthly = findViewById(R.id.ll_monthly);
        rv_monthly_calendar = findViewById(R.id.rv_monthly_calendar);
        vp_weekly_calendar = findViewById(R.id.vp_weekly_calendar);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Calendar calendar = Calendar.getInstance();
        currentMonth = calendar.get(Calendar.MONTH) + 1;
        currentYear = calendar.get(Calendar.YEAR);

        updateSelection(ll_monthly, tv_monthly, true);
        updateSelection(ll_weekly, tv_weekly, false);

        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(MoodCalenderScreen.this, DashboardScreen.class);
            boolean cameFromHome = getIntent().getBooleanExtra("cameFromHome", false);
            if (cameFromHome) {
                intent.putExtra("navigate_to", "HomeFragment");
            } else {
                intent.putExtra("navigate_to", "ExploreFragment");
            }
            startActivity(intent);
            finish();
        });


//        btn_back.setOnClickListener(v -> {
//            Intent intent = new Intent(MoodCalenderScreen.this, DashboardScreen.class);
//            intent.putExtra("navigate_to", "ExploreFragment");
//            startActivity(intent);
//            finish();
//        });

        ll_weekly.setOnClickListener(v -> {
            updateSelection(ll_weekly, tv_weekly, true);
            updateSelection(ll_monthly, tv_monthly, false);
        });

        ll_monthly.setOnClickListener(v -> {
            updateSelection(ll_weekly, tv_weekly, false);
            updateSelection(ll_monthly, tv_monthly, true);
        });

        rv_monthly_calendar.setLayoutManager(new GridLayoutManager(this, 7));
        monthlyCalendarAdapter = new MonthlyCalenderAdapter(generateMonthlyCalendar(), moodMap);
        rv_monthly_calendar.setAdapter(monthlyCalendarAdapter);

        fetchUserMoodData();
        showMonthlyCalendar();

        btn_previous.setOnClickListener(v -> navigateMonth(false));
        btn_next.setOnClickListener(v -> navigateMonth(true));
    }

    private void updateSelection(LinearLayout layout, TextView textView, boolean isSelected) {
        if (isSelected) {
            layout.setBackground(ContextCompat.getDrawable(this, R.drawable.quote_item_bg));
            layout.setBackgroundColor(ContextCompat.getColor(this, R.color.olive_green));
            textView.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else {
            layout.setBackground(ContextCompat.getDrawable(this, R.drawable.quote_item_bg));
            textView.setTextColor(ContextCompat.getColor(this, R.color.black));
        }
    }

    private void showMonthlyCalendar() {
        rv_monthly_calendar.setVisibility(View.VISIBLE);
        vp_weekly_calendar.setVisibility(View.GONE);
        updateMonthYearDisplay();
    }

    private void navigateMonth(boolean isNext) {
        if (isNext) {
            currentMonth++;
            if (currentMonth > 12) {
                currentMonth = 1;
                currentYear++;
            }
        } else {
            currentMonth--;
            if (currentMonth < 1) {
                currentMonth = 12;
                currentYear--;
            }
        }
        monthlyCalendarAdapter.updateCalendar(generateMonthlyCalendar(), moodMap);
        updateMonthYearDisplay();
    }

    private void updateMonthYearDisplay() {
        String[] months = {"January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December"};
        tv_monthYear.setText(String.format("%s %d", months[currentMonth - 1], currentYear));
    }

    private List<String> generateMonthlyCalendar() {
        List<String> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, currentYear);
        calendar.set(Calendar.MONTH, currentMonth - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        for (int i = 0; i < firstDayOfWeek; i++) {
            dates.add("");
        }

        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= daysInMonth; i++) {
            String formattedDate = (i < 10) ? "0" + i : String.valueOf(i);
            dates.add(formattedDate);
        }

        int totalDays = dates.size();
        while (totalDays % 7 != 0) {
            dates.add("");
            totalDays++;
        }

        return dates;
    }

    private void fetchUserMoodData() {
        String userId = auth.getCurrentUser().getUid();
        String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        String currentMonth = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
        String currentWeek = "Week_" + getCurrentWeekOfMonth();

        db.collection("mood_tracker")
                .document(userId)
                .collection(currentYear)
                .document(currentMonth)
                .collection(currentWeek)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        moodMap.clear();
                        for (DocumentSnapshot document : querySnapshot) {
                            String currentDate = document.getId();
                            Log.d("Fetched Date", "Date: " + currentDate);
                            String mood = document.getString("mood");
                            int moodEmoji = getEmojiDrawable(mood);
                            moodMap.put(currentDate, moodEmoji);
                        }
                        monthlyCalendarAdapter.updateCalendar(generateMonthlyCalendar(), moodMap);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FetchUserMoodData", "Error fetching mood data: " + e.getMessage());
                });
    }

    private int getCurrentWeekOfMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    private int getEmojiDrawable(String mood) {
        switch (mood) {
            case "Happy":
                return R.drawable.happy;
            case "Sad":
                return R.drawable.unhappy;
            case "Very Happy":
                return R.drawable.very_happy;
            case "Angry":
                return R.drawable.very_unhappy;
            case "Neutral":
                return R.drawable.neutral;
            default:
                return R.drawable.default_mood;
        }
    }
}