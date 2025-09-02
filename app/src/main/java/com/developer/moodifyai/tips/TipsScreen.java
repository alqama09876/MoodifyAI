package com.developer.moodifyai.tips;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.developer.moodifyai.DashboardScreen;
import com.developer.moodifyai.R;
import com.developer.moodifyai.model.Tip;
import com.developer.moodifyai.mood_calender.MoodCalenderScreen;
import com.developer.moodifyai.notepad.NotepadScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TipsScreen extends AppCompatActivity {
    LinearLayout sectionsContainer;
    ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tips_screen);

        btn_back = findViewById(R.id.btn_back);
        sectionsContainer = findViewById(R.id.sections_container);

        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(TipsScreen.this, DashboardScreen.class);
            boolean cameFromHome = getIntent().getBooleanExtra("cameFromHome", false);
            if (cameFromHome) {
                intent.putExtra("navigate_to", "HomeFragment");
            } else {
                intent.putExtra("navigate_to", "ExploreFragment");
            }
            startActivity(intent);
            finish();
        });

//        btn_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(TipsScreen.this, DashboardScreen.class);
//                intent.putExtra("navigate_to", "ExploreFragment");
//                startActivity(intent);
//                finish();
//            }
//        });

        Map<String, List<Tip>> sectionsData = new HashMap<>();
        sectionsData.put("Stress Management", getStressManagementTips());
        sectionsData.put("Mood Boost", getMoodBoostTips());
        sectionsData.put("Anxiety Reduction", getAnxietyReductionTips());
        sectionsData.put("Personal Growth", getPersonalGrowthTips());

        for (Map.Entry<String, List<Tip>> entry : sectionsData.entrySet()) {
            addSection(entry.getKey(), entry.getValue());
        }
    }

    private void addSection(String sectionTitle, List<Tip> tipsList) {
        View sectionView = LayoutInflater.from(this).inflate(R.layout.item_section_layout, sectionsContainer, false);

        TextView title = sectionView.findViewById(R.id.section_title);
        LinearLayout itemsContainer = sectionView.findViewById(R.id.section_items_container);
        LinearLayout view_all = sectionView.findViewById(R.id.section_view_all);

        title.setText(sectionTitle);
        view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TipsScreen.this, TipsCategoryScreen.class);
                switch (sectionTitle) {
                    case "Stress Management":
                        intent.putExtra("CATEGORY_ID", 0);
                        break;
                    case "Mood Boost":
                        intent.putExtra("CATEGORY_ID", 1);
                        break;
                    case "Anxiety Reduction":
                        intent.putExtra("CATEGORY_ID", 2);
                        break;
                    case "Personal Growth":
                        intent.putExtra("CATEGORY_ID", 3);
                        break;
                    default:
                        intent.putExtra("CATEGORY_ID", -1);
                }
                startActivity(intent);
            }
        });

        for (Tip tip : tipsList) {
            View itemView = LayoutInflater.from(this).inflate(R.layout.custom_tips_layout, itemsContainer, false);

            ImageView itemImage = itemView.findViewById(R.id.item_image);
            TextView itemText = itemView.findViewById(R.id.item_text);

            itemImage.setImageResource(tip.getImageResId());
            itemText.setText(tip.getText());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TipsScreen.this, TipDetailScreen.class);
                    intent.putExtra("TIP_IMAGE", tip.getImageResId());
                    intent.putExtra("TIP_TITLE", tip.getText());
                    intent.putExtra("TIP_DESCRIPTION", tip.getDescription() != null ? tip.getDescription() : "No description available");
                    intent.putExtra("TIP_EXAMPLE", tip.getExample() != null ? tip.getExample() : "No example available");
                    startActivity(intent);
                }
            });

            itemsContainer.addView(itemView);
        }

        sectionsContainer.addView(sectionView);
    }

    private List<Tip> getStressManagementTips() {
        List<Tip> tips = new ArrayList<>();
        tips.add(new Tip("Challenge your negative thoughts", R.drawable.challenge_icon, "Replace self-critical or overly negative thoughts with constructive and positive alternatives.", "Instead of “I’m failing,” think, “I’m learning and improving every day."));
        tips.add(new Tip("Practice deep breathing", R.drawable.park_icon, "Deep breathing calms your mind by reducing stress hormones.", "Inhale for 4 seconds, hold for 7 seconds, and exhale for 8 seconds."));
        tips.add(new Tip("Listen to calming music", R.drawable.music_icon, "Music can have a soothing effect on your mind and body.", "Play soft piano or nature sounds while working or relaxing."));
        tips.add(new Tip("Take short breaks", R.drawable.barriers_icon, "Regular breaks improve focus and reduce burnout.", "Take a 5-minute break every hour to stretch or grab a glass of water."));
        tips.add(new Tip("Make a to-do list", R.drawable.todo_icon, "Writing down tasks helps organize your day and manage stress.", "List your top 3 priorities for the day and check them off as you complete them."));
        tips.add(new Tip("Exercise daily", R.drawable.walk_icon, "Physical activity releases endorphins that reduce stress.", "Go for a 20-minute walk in the morning to feel refreshed."));
        tips.add(new Tip("Meditate Regularly", R.drawable.meditate_icon, "Meditation helps you focus on the present and calm your thoughts.", "Spend 10 minutes sitting quietly, focusing on your breath."));
        tips.add(new Tip("Limit caffeine", R.drawable.tea_icon, "Too much caffeine can increase anxiety and restlessness.", "Swap your afternoon coffee with herbal tea."));
        tips.add(new Tip("Talk to a friend", R.drawable.call_icon, "Sharing your feelings with someone supportive helps lighten your emotional load.", "Call a friend and tell them about your day."));
        tips.add(new Tip("Write a gratitude journal", R.drawable.gratitude_icon, "Focusing on what you’re thankful for improves mental well-being.", "Write down three things you’re grateful for each night before sleeping."));
        return tips;
    }

    private List<Tip> getMoodBoostTips() {
        List<Tip> tips = new ArrayList<>();
        tips.add(new Tip("Say positive affirmation", R.drawable.attitude_icon, "Positive statements help boost confidence and energy.", "Start your day with “I am capable and strong."));
        tips.add(new Tip("Spend Time in nature", R.drawable.nature_icon, "Nature helps refresh your mind and reduce stress.", "Take a walk in the park or sit by a lake."));
        tips.add(new Tip("Watch something funny", R.drawable.movie_icon, "Laughter releases endorphins, instantly lifting your mood.", "Watch a comedy show or funny YouTube videos."));
        tips.add(new Tip("Enjoy your hobbies", R.drawable.moment_icon, "Doing what you love brings joy and relaxation.", "Spend time painting, playing an instrument, or reading."));
        tips.add(new Tip("Eat healthy snacks", R.drawable.snack_icon, "Nutritious foods can positively affect your mood and energy.", "Snack on dark chocolate, nuts, or fresh fruits."));
        tips.add(new Tip("Compliment Someone", R.drawable.thank_icon, "Giving a genuine compliment spreads positivity to others and yourself.", "Tell a colleague, “You did a great job on this project!"));
        tips.add(new Tip("Dance to your favorite songs", R.drawable.dance_icon, "Dancing releases stress and boosts energy.", "Play an upbeat song and move to the rhythm for 5 minutes."));
        tips.add(new Tip("Try aromatherapy", R.drawable.aromatherapy_icon, "Essential oils like lavender or citrus can lift your mood.", "Use a diffuser with lavender oil in your room."));
        tips.add(new Tip("Smile more often", R.drawable.smile_icon, "Smiling, even when forced, can trick your brain into feeling happier.", "Look at yourself in the mirror and smile for a few seconds."));
        tips.add(new Tip("Celebrate small wins", R.drawable.celebrate_icon, "Acknowledging small achievements keeps you motivated.", "Treat yourself to your favorite snack after completing a task."));
        return tips;
    }

    private List<Tip> getAnxietyReductionTips() {
        List<Tip> tips = new ArrayList<>();
        tips.add(new Tip("Spot your triggers", R.drawable.triggers_icon, "Identifying what causes anxiety helps you manage it better.", "Keep a journal to track situations that make you anxious."));
        tips.add(new Tip("Relax your muscles", R.drawable.relax_icon, "Progressive muscle relaxation can reduce tension.", "Tense and release each muscle group, starting from your toes."));
        tips.add(new Tip("Reduce screen time", R.drawable.screen_icon, "Too much screen time can overstimulate your mind.", "Set a 1-hour limit on social media each day."));
        tips.add(new Tip("Write down worries", R.drawable.thought_icon, "Writing your thoughts helps you process them.", "Before sleeping, jot down what’s worrying you and possible solutions."));
        tips.add(new Tip("Use grounding techniques", R.drawable.augmented_icon, "Grounding helps bring you back to the present.", "Name 5 things you see, 4 you can touch, 3 you hear, 2 you smell, and 1 you taste."));
        tips.add(new Tip("Avoid overthinking", R.drawable.thinking_icon, "Focus on what you can control, not hypothetical scenarios.", "If you’re worried about a meeting, prepare for it rather than imagining negative outcomes."));
        tips.add(new Tip("Try guided meditation", R.drawable.meditation_icon, "Meditation apps can guide you in reducing anxiety.", "Use an app like MoodifyAI for a 5-minute relaxation session."));
        tips.add(new Tip("Stay hydrated", R.drawable.hydrated_icon, "Dehydration can worsen anxiety symptoms.", "Keep a water bottle with you and sip throughout the day."));
        tips.add(new Tip("Learn to say \"no\"", R.drawable.say_no_icon, "Setting boundaries prevents overwhelm.", "Politely decline an extra task if your schedule is already full."));
        tips.add(new Tip("Seek professional help if needed", R.drawable.doctors_icon, "A therapist can provide tools to manage ongoing anxiety.", "Schedule an appointment with a counselor for personalized guidance."));
        return tips;
    }

    private List<Tip> getPersonalGrowthTips() {
        List<Tip> tips = new ArrayList<>();
        tips.add(new Tip("Learn New Skills", R.drawable.skills_icon, "Expanding your skill set keeps your mind sharp, boosts confidence, and opens new opportunities.", "Take a cooking class to learn how to prepare different cuisines or learn a new programming language if you’re in tech."));
        tips.add(new Tip("Set Personal Challenges", R.drawable.challenges_icon, "Working toward small, achievable goals gives a sense of accomplishment and keeps you motivated.", "Challenge yourself to read one book every month or to run a 5K by a specific date."));
        tips.add(new Tip("Reflect Regularly", R.drawable.reflect_icon, "Taking time to think about your actions and progress helps you identify strengths and areas for improvement.", "Write in a journal about what went well and what didn’t at the end of each week."));
        tips.add(new Tip("Develop Emotional Intelligence", R.drawable.emotion_icon, "Understanding and managing your emotions can improve your relationships and decision-making.", "Practice pausing before reacting when you feel angry or frustrated."));
        tips.add(new Tip("Expand Your Comfort Zone", R.drawable.comfort_icon, "Growth happens when you try things that feel uncomfortable at first but help you gain new experiences.", "Speak at a public event or join a group where you don’t know anyone."));
        tips.add(new Tip("Practice Self-Discipline", R.drawable.discipline_icon, "Staying committed to habits and routines builds resilience and leads to long-term growth.", "Stick to a morning routine of exercising and meditating, even on weekends."));
        tips.add(new Tip("Seek Feedback", R.drawable.feedback_icon, "Constructive feedback helps you learn from others and improve faster.", "Ask your colleagues or friends for feedback on a project or personal behavior."));
        tips.add(new Tip("Cultivate a Growth Mindset", R.drawable.growth_icon, "Believing you can develop your abilities through effort encourages continuous improvement.", "Instead of saying, \"I’m not good at math,\" say, \"I’m learning to get better at math.\""));
        tips.add(new Tip("Build a Reading Habit", R.drawable.habit_icon, "Reading broadens your knowledge, inspires creativity, and provides different perspectives.", "Read self-help books, autobiographies, or fiction to gain new ideas."));
        tips.add(new Tip("Learn to Say No", R.drawable.learn_say_no_icon, "Setting boundaries helps you focus on what matters most and prevents burnout.", "Politely decline extra work if it interferes with your personal priorities."));
        return tips;
    }
}