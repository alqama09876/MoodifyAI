package com.developer.moodifyai.tips;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.developer.moodifyai.R;
import com.developer.moodifyai.adapter.CategoryAdapter;
import com.developer.moodifyai.model.Tip;

public class TipsCategoryScreen extends AppCompatActivity {

    private GridView gridView;
    private TextView titleText;
    ImageView btn_back;

    private String[][] categoryDescriptions = {
            {"Replace self-critical or overly negative thoughts with constructive and positive alternatives.",
                    "Deep breathing calms your mind by reducing stress hormones.",
                    "Music can have a soothing effect on your mind and body.",
                    "Regular breaks improve focus and reduce burnout.",
                    "Writing down tasks helps organize your day and manage stress.",
                    "Physical activity releases endorphins that reduce stress.",
                    "Meditation helps you focus on the present and calm your thoughts.",
                    "Too much caffeine can increase anxiety and restlessness.",
                    "Sharing your feelings with someone supportive helps lighten your emotional load.",
                    "Focusing on what you’re thankful for improves mental well-being."},

            {"Positive statements help boost confidence and energy.",
                    "Nature helps refresh your mind and reduce stress.",
                    "Laughter releases endorphins, instantly lifting your mood.",
                    "Doing what you love brings joy and relaxation.",
                    "Nutritious foods can positively affect your mood and energy.",
                    "Giving a genuine compliment spreads positivity to others and yourself.",
                    "Dancing releases stress and boosts energy.",
                    "Essential oils like lavender or citrus can lift your mood.",
                    "Smiling, even when forced, can trick your brain into feeling happier.",
                    "Acknowledging small achievements keeps you motivated."},

            {"Identifying what causes anxiety helps you manage it better.",
                    "Progressive muscle relaxation can reduce tension.",
                    "Too much screen time can overstimulate your mind.",
                    "Writing your thoughts helps you process them.",
                    "Grounding helps bring you back to the present.",
                    "Focus on what you can control, not hypothetical scenarios.",
                    "Meditation apps can guide you in reducing anxiety.",
                    "Dehydration can worsen anxiety symptoms.",
                    "Setting boundaries prevents overwhelm.",
                    "A therapist can provide tools to manage ongoing anxiety."},

            {"Expanding your skill set keeps your mind sharp, boosts confidence, and opens new opportunities.",
                    "Working toward small, achievable goals gives a sense of accomplishment and keeps you motivated.",
                    "Taking time to think about your actions and progress helps you identify strengths and areas for improvement.",
                    "Understanding and managing your emotions can improve your relationships and decision-making.",
                    "Growth happens when you try things that feel uncomfortable at first but help you gain new experiences.",
                    "Staying committed to habits and routines builds resilience and leads to long-term growth.",
                    "Constructive feedback helps you learn from others and improve faster.",
                    "Believing you can develop your abilities through effort encourages continuous improvement.",
                    "Reading broadens your knowledge, inspires creativity, and provides different perspectives.",
                    "Setting boundaries helps you focus on what matters most and prevents burnout."}
    };

    private String[][] categoryExamples = {
            {"Instead of “I’m failing,” think, “I’m learning and improving every day.",
                    "Inhale for 4 seconds, hold for 7 seconds, and exhale for 8 seconds.",
                    "Play soft piano or nature sounds while working or relaxing.",
                    "Take a 5-minute break every hour to stretch or grab a glass of water.",
                    "List your top 3 priorities for the day and check them off as you complete them.",
                    "Go for a 20-minute walk in the morning to feel refreshed.",
                    "Spend 10 minutes sitting quietly, focusing on your breath.",
                    "Swap your afternoon coffee with herbal tea.",
                    "Call a friend and tell them about your day.",
                    "Write down three things you’re grateful for each night before sleeping."},

            new String[]{"Start your day with “I am capable and strong.",
                    "Take a walk in the park or sit by a lake.",
                    "Watch a comedy show or funny YouTube videos.",
                    "Spend time painting, playing an instrument, or reading.",
                    "Snack on dark chocolate, nuts, or fresh fruits.",
                    "Tell a colleague, “You did a great job on this project!",
                    "Play an upbeat song and move to the rhythm for 5 minutes.",
                    "Use a diffuser with lavender oil in your room.",
                    "Look at yourself in the mirror and smile for a few seconds.",
                    "Treat yourself to your favorite snack after completing a task."},

            {"Keep a journal to track situations that make you anxious.",
                    "Tense and release each muscle group, starting from your toes.",
                    "Set a 1-hour limit on social media each day.",
                    "Before sleeping, jot down what’s worrying you and possible solutions.",
                    "Name 5 things you see, 4 you can touch, 3 you hear, 2 you smell, and 1 you taste.",
                    "If you’re worried about a meeting, prepare for it rather than imagining negative outcomes.",
                    "Use an app like MoodifyAI for a 5-minute relaxation session.",
                    "Keep a water bottle with you and sip throughout the day.",
                    "Politely decline an extra task if your schedule is already full.",
                    "Schedule an appointment with a counselor for personalized guidance."},

            {"Take a cooking class to learn how to prepare different cuisines or learn a new programming language if you’re in tech.",
                    "Challenge yourself to read one book every month or to run a 5K by a specific date.",
                    "Write in a journal about what went well and what didn’t at the end of each week.",
                    "Practice pausing before reacting when you feel angry or frustrated.",
                    "Speak at a public event or join a group where you don’t know anyone.",
                    "Stick to a morning routine of exercising and meditating, even on weekends.",
                    "Ask your colleagues or friends for feedback on a project or personal behavior.",
                    "Instead of saying, \"I’m not good at math,\" say, \"I’m learning to get better at math.\"",
                    "Read self-help books, autobiographies, or fiction to gain new ideas.",
                    "Politely decline extra work if it interferes with your personal priorities."}
    };

    private String[][] categoryTitles = {
            {"Challenge your negative thoughts", "Practice deep breathing", "Listen to calming music",
                    "Take short breaks", "Make a to-do list", "Exercise daily", "Meditate Regularly",
                    "Limit caffeine", "Talk to a friend", "Write a gratitude journal"},
            {"Say positive affirmation", "Spend time in nature", "Watch something funny",
                    "Enjoy your hobbies", "Eat healthy snacks", "Compliment Someone",
                    "Dance to your favorite songs", "Try aromatherapy", "Smile more often", "Celebrate small wins"},
            {"Spot your triggers", "Relax your muscles", "Reduce screen time",
                    "Write down worries", "Use grounding techniques", "Avoid overthinking",
                    "Try guided meditation", "Stay hydrated", "Learn to say no", "Seek professional help"},
            {"Learn New Skills", "Set Personal Challenges", "Reflect Regularly",
                    "Develop Emotional Intelligence", "Expand Your Comfort Zone", "Practice Self-Discipline",
                    "Seek Feedback", "Cultivate a Growth Mindset", "Build a Reading Habit", "Learn to Say No"}
    };

    private int[][] categoryImages = {
            {R.drawable.challenge_icon, R.drawable.park_icon, R.drawable.music_icon, R.drawable.barriers_icon, R.drawable.todo_icon,
                    R.drawable.walk_icon, R.drawable.meditate_icon, R.drawable.tea_icon, R.drawable.call_icon, R.drawable.gratitude_icon},
            {R.drawable.attitude_icon, R.drawable.nature_icon, R.drawable.movie_icon, R.drawable.moment_icon, R.drawable.snack_icon,
                    R.drawable.thank_icon, R.drawable.dance_icon, R.drawable.aromatherapy_icon, R.drawable.smile_icon, R.drawable.celebrate_icon},
            {R.drawable.triggers_icon, R.drawable.relax_icon, R.drawable.screen_icon, R.drawable.thought_icon,
                    R.drawable.augmented_icon, R.drawable.thinking_icon, R.drawable.meditation_icon, R.drawable.hydrated_icon,
                    R.drawable.say_no_icon, R.drawable.doctors_icon},
            {R.drawable.skills_icon, R.drawable.challenges_icon, R.drawable.reflect_icon, R.drawable.emotion_icon,
                    R.drawable.comfort_icon, R.drawable.discipline_icon, R.drawable.feedback_icon, R.drawable.growth_icon,
                    R.drawable.habit_icon, R.drawable.learn_say_no_icon}
    };

    private String[] categoryNames = {"Stress Management", "Mood Boost", "Anxiety Reduction", "Personal Growth"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tips_category_screen);

        gridView = findViewById(R.id.gridView);
        titleText = findViewById(R.id.titleText);
        btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        int categoryId = getIntent().getIntExtra("CATEGORY_ID", 0);

        titleText.setText(categoryNames[categoryId]);

        CategoryAdapter adapter = new CategoryAdapter(this, categoryTitles[categoryId], categoryImages[categoryId], categoryDescriptions[categoryId], categoryExamples[categoryId]);
        gridView.setAdapter(adapter);
    }
}