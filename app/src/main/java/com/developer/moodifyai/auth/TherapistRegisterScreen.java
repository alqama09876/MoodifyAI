package com.developer.moodifyai.auth;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.developer.moodifyai.R;
import com.developer.moodifyai.TherapistDashboardScreen;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TherapistRegisterScreen extends AppCompatActivity {

    private EditText edtName, edtEmail, edtPassword, edtPhone, edtExperience, edtAvailability,
            edtBio, edtClinicName, edtAddress, edtSessionFee, edt_licence_no;
    private Spinner spinnerQualification, spinnerFieldOfStudy, spinnerSpecialization;
    private ImageView img_id_front, img_id_back, img_licence_certificate;
    private RadioGroup radio_group_session_duration;
    private MultiAutoCompleteTextView multi_language;
    private TextView btn_signin;
    private AppCompatButton btn_signup;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private boolean isPasswordVisible = false;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmapFront, bitmapBack, bitmapLicense;
    private boolean isFrontImageSelected = false;
    private boolean isLicenseImageSelected = false;
    ProgressBar progressBar;
    private String startTime = "";
    private String endTime = "";
    private List<String> selectedDays = new ArrayList<>();
    private static final String[] DAYS_OF_WEEK = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_therapist_register);

        // Initialize views
        edtName = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edtPhone = findViewById(R.id.edt_phone);
        edtExperience = findViewById(R.id.edt_experience);
        radio_group_session_duration = findViewById(R.id.radio_group_session_duration);
        edtAvailability = findViewById(R.id.edt_availability);
        edtBio = findViewById(R.id.edt_bio);
        edtClinicName = findViewById(R.id.edt_clinicName);
        edtSessionFee = findViewById(R.id.edt_sessionFee);
        edtAddress = findViewById(R.id.edt_address);
        edt_licence_no = findViewById(R.id.edt_licence_no);
        img_id_front = findViewById(R.id.img_id_front);
        img_id_back = findViewById(R.id.img_id_back);
        img_licence_certificate = findViewById(R.id.img_licence_certificate);
        spinnerQualification = findViewById(R.id.spn_qualification);
        spinnerFieldOfStudy = findViewById(R.id.spn_field);
        spinnerSpecialization = findViewById(R.id.spn_specialization);
        multi_language = findViewById(R.id.multi_language);
        btn_signup = findViewById(R.id.btn_signup);
        btn_signin = findViewById(R.id.btn_signin);
        progressBar = findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        setupSpinners();

        // Setup session fee formatting
        setupSessionFeeFormatting();

        // Setup availability calendar
        setupAvailabilityCalendar();

        edtPassword.setOnTouchListener(this::handlePasswordToggle);

        img_id_front.setOnClickListener(view -> {
            isFrontImageSelected = true;
            isLicenseImageSelected = false;
            pickImage();
        });

        img_id_back.setOnClickListener(view -> {
            isFrontImageSelected = false;
            isLicenseImageSelected = false;
            pickImage();
        });

        img_licence_certificate.setOnClickListener(view -> {
            isLicenseImageSelected = true;
            pickImage();
        });

        btn_signup.setOnClickListener(v -> validateAndSubmit());

        btn_signin.setOnClickListener(v ->
                startActivity(new Intent(TherapistRegisterScreen.this, TherapistLoginScreen.class))
        );
    }

    private void setupSessionFeeFormatting() {
        edtSessionFee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                edtSessionFee.removeTextChangedListener(this);

                String original = s.toString().replaceAll("[^\\d]", "");
                if (!TextUtils.isEmpty(original)) {
                    String formatted = "Rs. " + original;
                    edtSessionFee.setText(formatted);
                    edtSessionFee.setSelection(formatted.length());
                }

                edtSessionFee.addTextChangedListener(this);
            }
        });
    }

    private void setupAvailabilityCalendar() {
        edtAvailability.setFocusable(false);
        edtAvailability.setClickable(true);
        edtAvailability.setOnClickListener(v -> showAvailabilityDialog());
    }

    private void showAvailabilityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Availability");

        // Inflate custom layout
        View dialogView = getLayoutInflater().inflate(R.layout.availability_dialog, null);
        builder.setView(dialogView);

        // Get references to dialog components
        HorizontalScrollView scrollView = dialogView.findViewById(R.id.scroll_view);
        LinearLayout daysContainer = dialogView.findViewById(R.id.days_container);
        TextView txtStartTime = dialogView.findViewById(R.id.txt_start_time);
        TextView txtEndTime = dialogView.findViewById(R.id.txt_end_time);
        Button btnSetTimeRange = dialogView.findViewById(R.id.btn_set_time_range);

        // Clear previous selections
        selectedDays.clear();
        startTime = "";
        endTime = "";
        txtStartTime.setText("Start: Not set");
        txtEndTime.setText("End: Not set");

        // Setup day checkboxes
        for (String day : DAYS_OF_WEEK) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(day);
            checkBox.setTextColor(ContextCompat.getColor(this, R.color.black));
            checkBox.setPadding(16, 8, 16, 8); // Add padding for better touch area
            checkBox.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedDays.add(day);
                } else {
                    selectedDays.remove(day);
                }
            });
            daysContainer.addView(checkBox);
        }

        // Time range button
        btnSetTimeRange.setOnClickListener(v -> showTimeRangePicker(txtStartTime, txtEndTime));

        // Set dialog buttons
        builder.setPositiveButton("Set", (dialog, which) -> {
            if (selectedDays.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
                Toast.makeText(this, "Please select days and time range", Toast.LENGTH_SHORT).show();
                return;
            }

            // Format days (e.g., "Mon-Fri" or "Mon, Wed, Fri")
            String daysFormatted = formatDays(selectedDays);
            edtAvailability.setText(daysFormatted + ", " + startTime + " - " + endTime);
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String formatDays(List<String> days) {
        if (days.contains("Mon") && days.contains("Tue") && days.contains("Wed") &&
                days.contains("Thu") && days.contains("Fri") && !days.contains("Sat") && !days.contains("Sun")) {
            return "Mon-Fri";
        } else if (days.contains("Mon") && days.contains("Tue") && days.contains("Wed") &&
                days.contains("Thu") && days.contains("Fri") && days.contains("Sat") && !days.contains("Sun")) {
            return "Mon-Sat";
        } else if (days.contains("Mon") && days.contains("Tue") && days.contains("Wed") &&
                days.contains("Thu") && days.contains("Fri") && days.contains("Sat") && days.contains("Sun")) {
            return "Everyday";
        } else {
            // Format as comma-separated list
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < days.size(); i++) {
                sb.append(days.get(i));
                if (i < days.size() - 1) {
                    sb.append(", ");
                }
            }
            return sb.toString();
        }
    }

    private void showTimeRangePicker(TextView txtStart, TextView txtEnd) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Start time picker
        TimePickerDialog startTimePicker = new TimePickerDialog(
                this,
                (view, hourOfDay, minute1) -> {
                    startTime = formatTime(hourOfDay, minute1);
                    txtStart.setText("Start: " + startTime);

                    // End time picker
                    TimePickerDialog endTimePicker = new TimePickerDialog(
                            TherapistRegisterScreen.this,
                            (view1, hourOfDay1, minute11) -> {
                                endTime = formatTime(hourOfDay1, minute11);
                                txtEnd.setText("End: " + endTime);
                            },
                            hourOfDay, // Default to start time hour
                            minute1,   // Default to start time minute
                            false
                    );
                    endTimePicker.setTitle("Select End Time");
                    endTimePicker.show();
                },
                hour,
                minute,
                false
        );
        startTimePicker.setTitle("Select Start Time");
        startTimePicker.show();
    }

    private String formatTime(int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(cal.getTime());
    }

    private boolean handlePasswordToggle(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int drawableRight = 2;
            if (event.getRawX() >= (edtPassword.getRight() - edtPassword.getCompoundDrawables()[drawableRight].getBounds().width())) {
                togglePasswordVisibility();
                return true;
            }
        }
        return false;
    }

    private void togglePasswordVisibility() {
        int drawableStart = R.drawable.password_icon;
        edtPassword.setTransformationMethod(isPasswordVisible ? new PasswordTransformationMethod() : null);
        edtPassword.setCompoundDrawablesWithIntrinsicBounds(drawableStart, 0,
                isPasswordVisible ? R.drawable.eye_close_icon : R.drawable.eye_open_icon, 0);
        isPasswordVisible = !isPasswordVisible;
        edtPassword.setSelection(edtPassword.getText().length());
    }

    private void validateAndSubmit() {
        if (!validateName() || !validateEmail() || !validatePassword() || !validatePhone() ||
                !validateQualification() || !validateField() || !validateSpecialization() ||
                !validateExperience() || !validateSessionDuration() || !validateAvailability() ||
                !validateBio() || !validateClinicName() || !validateAddress() || !validateSessionFee() ||
                !validateLanguage() || !validateLicenseNo() || !validateLicenseImage() || !validateImages()) {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btn_signup.setEnabled(false);

        // Save images to local storage and get their paths
        String frontImagePath = saveImageToLocalStorage(bitmapFront, "front_image");
        String backImagePath = saveImageToLocalStorage(bitmapBack, "back_image");
        String licenseImagePath = saveImageToLocalStorage(bitmapLicense, "licence_certificate");

        if (frontImagePath == null || backImagePath == null || licenseImagePath == null) {
            Toast.makeText(this, "Failed to save images", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            btn_signup.setEnabled(true);
            return;
        }

        // Get selected session duration from radio group
        int selectedDurationId = radio_group_session_duration.getCheckedRadioButtonId();
        RadioButton selectedButton = findViewById(selectedDurationId);
        int sessionDurationValue = Integer.parseInt(
                selectedButton.getText().toString().replace(" mins", "")
        );

        // Get selected languages
        String languages = multi_language.getText().toString().trim();

        // Clean session fee value (remove "Rs. ")
        String cleanSessionFee = edtSessionFee.getText().toString().replace("Rs. ", "").trim();

        // Proceed with registration
        registerTherapist(
                edtName.getText().toString().trim(),
                edtEmail.getText().toString().trim(),
                edtPassword.getText().toString(),
                edtPhone.getText().toString().trim(),
                spinnerQualification.getSelectedItem().toString(),
                spinnerFieldOfStudy.getSelectedItem().toString(),
                spinnerSpecialization.getSelectedItem().toString(),
                sessionDurationValue,  // Now an int
                edtExperience.getText().toString().trim(),
                edtAvailability.getText().toString().trim(),
                edtBio.getText().toString().trim(),
                edtClinicName.getText().toString().trim(),
                edtAddress.getText().toString().trim(),
                cleanSessionFee,
                languages,  // Comma-separated string
                edt_licence_no.getText().toString().trim(),
                licenseImagePath,  // Licence certificate image path
                frontImagePath,
                backImagePath
        );
    }

    private boolean validateName() {
        String name = edtName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            edtName.setError("Enter your full name");
            return false;
        }
        return true;
    }

    private boolean validateEmail() {
        String email = edtEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Enter a valid email");
            return false;
        }
        return true;
    }

    private boolean validatePassword() {
        String password = edtPassword.getText().toString();
        if (TextUtils.isEmpty(password) || password.length() < 6 || !password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{6,}$")) {
            edtPassword.setError("Password must be at least 6 characters and contain at least one uppercase letter, one lowercase letter, one digit, and one special character");
            return false;
        }
        return true;
    }

    private boolean validatePhone() {
        String phone = edtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone) || !Patterns.PHONE.matcher(phone).matches()) {
            edtPhone.setError("Enter a valid phone number");
            return false;
        }
        return true;
    }

    private boolean validateQualification() {
        if (spinnerQualification.getSelectedItem().toString().equals("Select Qualification")) {
            Toast.makeText(this, "Please select a qualification", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateField() {
        if (spinnerFieldOfStudy.getSelectedItem().toString().equals("Select Field of Study")) {
            Toast.makeText(this, "Please select a field", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateSpecialization() {
        if (spinnerSpecialization.getSelectedItem().toString().equals("Select Specialization")) {
            Toast.makeText(this, "Please select a specialization", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateExperience() {
        String experience = edtExperience.getText().toString().trim();
        if (TextUtils.isEmpty(experience)) {
            edtExperience.setError("Enter your experience");
            return false;
        }
        return true;
    }

    private boolean validateSessionDuration() {
        int selectedId = radio_group_session_duration.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Please select session duration", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateAvailability() {
        String availability = edtAvailability.getText().toString().trim();
        if (TextUtils.isEmpty(availability)) {
            Toast.makeText(this, "Please set your availability", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateBio() {
        String bio = edtBio.getText().toString().trim();
        if (TextUtils.isEmpty(bio)) {
            edtBio.setError("Enter your bio");
            return false;
        }
        return true;
    }

    private boolean validateClinicName() {
        String clinicName = edtClinicName.getText().toString().trim();
        if (TextUtils.isEmpty(clinicName)) {
            edtClinicName.setError("Enter your clinic name");
            return false;
        }
        return true;
    }

    private boolean validateAddress() {
        String address = edtAddress.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            edtAddress.setError("Enter your address");
            return false;
        }
        return true;
    }

    private boolean validateSessionFee() {
        String sessionFee = edtSessionFee.getText().toString().trim();
        if (TextUtils.isEmpty(sessionFee) || sessionFee.equals("Rs.") || !sessionFee.replace("Rs. ", "").matches("\\d+(\\.\\d{1,2})?")) {
            edtSessionFee.setError("Enter a valid session fee");
            return false;
        }
        return true;
    }

    private boolean validateLanguage() {
        String languages = multi_language.getText().toString().trim();
        if (TextUtils.isEmpty(languages)) {
            Toast.makeText(this, "Please select at least one language", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateLicenseNo() {
        String licenseNo = edt_licence_no.getText().toString().trim();
        if (TextUtils.isEmpty(licenseNo)) {
            edt_licence_no.setError("Enter your licence number");
            return false;
        }
        return true;
    }

    private boolean validateLicenseImage() {
        if (bitmapLicense == null) {
            Toast.makeText(this, "Please upload your licence certificate", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateImages() {
        if (bitmapFront == null) {
            Toast.makeText(this, "Please select a front ID image", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (bitmapBack == null) {
            Toast.makeText(this, "Please select a back ID image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setupSpinners() {
        // Qualification Spinner
        String[] qualificationOptions = {"Select Qualification", "BA", "BSc", "Ms", "MSc", "MPhil", "PhD"};
        setupSpinner(spinnerQualification, qualificationOptions);

        // Field of Study Spinner
        String[] fieldOptions = {"Select Field of Study", "Psychology", "Counseling", "Social Work", "Psychiatry"};
        setupSpinner(spinnerFieldOfStudy, fieldOptions);

        // Specialization Spinner
        String[] specializationOptions = {"Select Specialization", "Clinical Psychology", "Counseling Psychology",
                "Marriage and Family Therapy", "Career Counseling", "Addiction Counseling",
                "Clinical Social Work"};
        setupSpinner(spinnerSpecialization, specializationOptions);

        // Setup language multi-select
        setupLanguageInput();
    }

    private void setupLanguageInput() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                getResources().getStringArray(R.array.languages)
        );
        multi_language.setAdapter(adapter);
        multi_language.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    private void setupSpinner(Spinner spinner, String[] options) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void registerTherapist(String name, String email, String password, String phone, String qualification, String field,
                                   String specialization, int sessionDuration, String experience, String availability,
                                   String bio, String clinicName, String address, String sessionFee, String languages,
                                   String licenseNo, String licenseImage, String frontImagePath, String backImagePath) {
        if (!isNetworkAvailable()) {
            showSnackbar(TherapistRegisterScreen.this, getRootView(this), "No internet connection", true);
            progressBar.setVisibility(View.GONE);
            btn_signup.setEnabled(true);
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();

                            double sessionFeeValue = Double.parseDouble(sessionFee);
                            int experienceValue = Integer.parseInt(experience);

                            // Get selected session duration text with units
                            int selectedDurationId = radio_group_session_duration.getCheckedRadioButtonId();
                            RadioButton selectedButton = findViewById(selectedDurationId);
                            String sessionDurationWithUnit = selectedButton.getText().toString();

                            // Format session fee with currency
                            String sessionFeeWithCurrency = "Rs " + sessionFee;

                            Map<String, Object> therapistData = new HashMap<>();
                            therapistData.put("uid", uid);
                            therapistData.put("name", name);
                            therapistData.put("email", email);
                            therapistData.put("phone", phone);
                            therapistData.put("qualification", qualification);
                            therapistData.put("field", field);
                            therapistData.put("specialization", specialization);
                            therapistData.put("sessionDuration", sessionDurationWithUnit);
                            therapistData.put("sessionDurationValue", sessionDuration);
                            therapistData.put("experience", experienceValue);
                            therapistData.put("availability", availability);
                            therapistData.put("bio", bio);
                            therapistData.put("clinicName", clinicName);
                            therapistData.put("address", address);
                            therapistData.put("sessionFee", sessionFeeWithCurrency);
                            therapistData.put("sessionFeeValue", sessionFeeValue);
                            therapistData.put("language", languages);
                            therapistData.put("role", "therapist");
                            therapistData.put("licenseNo", licenseNo);
                            therapistData.put("licenseImage", licenseImage);
                            therapistData.put("frontImagePath", frontImagePath);
                            therapistData.put("backImagePath", backImagePath);
                            therapistData.put("verified", false);
                            therapistData.put("rejected", false);

                            // Save to pending_therapists collection
                            db.collection("pending_therapists").document(uid)
                                    .set(therapistData)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("TherapistRegister", "Data saved to pending_therapists");
                                        progressBar.setVisibility(View.GONE);
                                        btn_signup.setEnabled(true);

                                        // Show confirmation dialog
                                        new AlertDialog.Builder(com.developer.moodifyai.auth.TherapistRegisterScreen.this)
                                                .setTitle("Request Submitted")
                                                .setMessage("Your request has been submitted. Please wait for admin approval.")
                                                .setPositiveButton("OK", (dialog, which) -> {
                                                    // Close the app
                                                    finishAffinity();
                                                })
                                                .setCancelable(false)
                                                .show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("TherapistRegister", "Failed to save data: " + e.getMessage());
                                        progressBar.setVisibility(View.GONE);
                                        btn_signup.setEnabled(true);
                                        showSnackbar(TherapistRegisterScreen.this, getRootView(this), "Registration failed: " + e.getMessage(), true);
                                    });
                        } else {
                            progressBar.setVisibility(View.GONE);
                            btn_signup.setEnabled(true);
                            showSnackbar(TherapistRegisterScreen.this, getRootView(this), "User registration failed: User is null", true);
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        btn_signup.setEnabled(true);
                        showSnackbar(TherapistRegisterScreen.this, getRootView(this), "Registration failed: " + task.getException().getMessage(), true);
                    }
                });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private static View getRootView(Context context) {
        try {
            return ((android.app.Activity) context).findViewById(R.id.ll_main);
        } catch (Exception e) {
            Toast.makeText(context, "Unable to find root view", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public static void showSnackbar(Context context, View view, String message, boolean isError) {
        if (view == null) {
            view = getRootView(context);
        }

        if (view != null) {
            Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);

            if (isError) {
                snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.red));
                snackbar.setTextColor(ContextCompat.getColor(context, R.color.white));
            } else {
                snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.olive_green));
                snackbar.setTextColor(ContextCompat.getColor(context, R.color.white));
            }

            snackbar.setAction("DISMISS", v -> snackbar.dismiss());
            snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.white));

            snackbar.show();
        } else {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                Bitmap compressedBitmap = compressImage(bitmap); // Compress the image dynamically

                if (isLicenseImageSelected) {
                    bitmapLicense = compressedBitmap;
                    img_licence_certificate.setImageBitmap(bitmapLicense);
                } else if (isFrontImageSelected) {
                    bitmapFront = compressedBitmap;
                    img_id_front.setImageBitmap(bitmapFront);
                } else {
                    bitmapBack = compressedBitmap;
                    img_id_back.setImageBitmap(bitmapBack);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap compressImage(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int quality = 100; // Start with 100% quality
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);

        // Loop until image size is less than 1MB
        while (outputStream.toByteArray().length > 1024 * 1024 && quality > 0) {
            outputStream.reset(); // Clear the output stream
            quality -= 10; // Reduce quality by 10%
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        }
        return BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.toByteArray().length);
    }

    private String saveImageToLocalStorage(Bitmap bitmap, String imageName) {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, imageName + ".jpg");

        try (FileOutputStream out = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}


//package com.developer.moodifyai.auth;
//
//import android.app.AlertDialog;
//import android.app.TimePickerDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.text.method.PasswordTransformationMethod;
//import android.util.Log;
//import android.util.Patterns;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.EditText;
//import android.widget.HorizontalScrollView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.MultiAutoCompleteTextView;
//import android.widget.ProgressBar;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.TimePicker;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.AppCompatButton;
//import androidx.core.content.ContextCompat;
//
//import com.developer.moodifyai.R;
//import com.developer.moodifyai.TherapistDashboardScreen;
//import com.google.android.material.snackbar.Snackbar;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//
//public class TherapistRegisterScreen extends AppCompatActivity {
//
//    private EditText edtName, edtEmail, edtPassword, edtPhone, edtExperience, edtAvailability,
//            edtBio, edtClinicName, edtAddress, edtSessionFee, edt_licence_no;
//    private Spinner spinnerQualification, spinnerFieldOfStudy, spinnerSpecialization;
//    private ImageView img_id_front, img_id_back, img_licence_certificate;
//    private RadioGroup radio_group_session_duration;
//    private MultiAutoCompleteTextView multi_language;
//    private TextView btn_signin;
//    private AppCompatButton btn_signup;
//    private FirebaseAuth auth;
//    private FirebaseFirestore db;
//    private boolean isPasswordVisible = false;
//    private static final int PICK_IMAGE_REQUEST = 1;
//    private Bitmap bitmapFront, bitmapBack, bitmapLicense;
//    private boolean isFrontImageSelected = false;
//    private boolean isLicenseImageSelected = false;
//    ProgressBar progressBar;
//    private String startTime = "";
//    private String endTime = "";
//    private List<String> selectedDays = new ArrayList<>();
//    private static final String[] DAYS_OF_WEEK = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_therapist_register);
//
//        // Initialize views
//        edtName = findViewById(R.id.edt_name);
//        edtEmail = findViewById(R.id.edt_email);
//        edtPassword = findViewById(R.id.edt_password);
//        edtPhone = findViewById(R.id.edt_phone);
//        edtExperience = findViewById(R.id.edt_experience);
//        radio_group_session_duration = findViewById(R.id.radio_group_session_duration);
//        edtAvailability = findViewById(R.id.edt_availability);
//        edtBio = findViewById(R.id.edt_bio);
//        edtClinicName = findViewById(R.id.edt_clinicName);
//        edtSessionFee = findViewById(R.id.edt_sessionFee);
//        edtAddress = findViewById(R.id.edt_address);
//        edt_licence_no = findViewById(R.id.edt_licence_no);
//        img_id_front = findViewById(R.id.img_id_front);
//        img_id_back = findViewById(R.id.img_id_back);
//        img_licence_certificate = findViewById(R.id.img_licence_certificate);
//        spinnerQualification = findViewById(R.id.spn_qualification);
//        spinnerFieldOfStudy = findViewById(R.id.spn_field);
//        spinnerSpecialization = findViewById(R.id.spn_specialization);
//        multi_language = findViewById(R.id.multi_language);
//        btn_signup = findViewById(R.id.btn_signup);
//        btn_signin = findViewById(R.id.btn_signin);
//        progressBar = findViewById(R.id.progressBar);
//
//        auth = FirebaseAuth.getInstance();
//        db = FirebaseFirestore.getInstance();
//
//        setupSpinners();
//
//        // Setup session fee formatting
//        setupSessionFeeFormatting();
//
//        // Setup availability calendar
//        setupAvailabilityCalendar();
//
//        edtPassword.setOnTouchListener(this::handlePasswordToggle);
//
//        img_id_front.setOnClickListener(view -> {
//            isFrontImageSelected = true;
//            isLicenseImageSelected = false;
//            pickImage();
//        });
//
//        img_id_back.setOnClickListener(view -> {
//            isFrontImageSelected = false;
//            isLicenseImageSelected = false;
//            pickImage();
//        });
//
//        img_licence_certificate.setOnClickListener(view -> {
//            isLicenseImageSelected = true;
//            pickImage();
//        });
//
//        btn_signup.setOnClickListener(v -> validateAndSubmit());
//
//        btn_signin.setOnClickListener(v ->
//                startActivity(new Intent(TherapistRegisterScreen.this, TherapistLoginScreen.class))
//        );
//    }
//
//    private void setupSessionFeeFormatting() {
//        edtSessionFee.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                edtSessionFee.removeTextChangedListener(this);
//
//                String original = s.toString().replaceAll("[^\\d]", "");
//                if (!TextUtils.isEmpty(original)) {
//                    String formatted = "Rs. " + original;
//                    edtSessionFee.setText(formatted);
//                    edtSessionFee.setSelection(formatted.length());
//                }
//
//                edtSessionFee.addTextChangedListener(this);
//            }
//        });
//    }
//
//    private void setupAvailabilityCalendar() {
//        edtAvailability.setFocusable(false);
//        edtAvailability.setClickable(true);
//        edtAvailability.setOnClickListener(v -> showAvailabilityDialog());
//    }
//
//    private void showAvailabilityDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Set Availability");
//
//        // Inflate custom layout
//        View dialogView = getLayoutInflater().inflate(R.layout.availability_dialog, null);
//        builder.setView(dialogView);
//
//        // Get references to dialog components
//        HorizontalScrollView scrollView = dialogView.findViewById(R.id.scroll_view);
//        LinearLayout daysContainer = dialogView.findViewById(R.id.days_container);
//        TextView txtStartTime = dialogView.findViewById(R.id.txt_start_time);
//        TextView txtEndTime = dialogView.findViewById(R.id.txt_end_time);
//        Button btnSetTimeRange = dialogView.findViewById(R.id.btn_set_time_range);
//
//        // Clear previous selections
//        selectedDays.clear();
//        startTime = "";
//        endTime = "";
//        txtStartTime.setText("Start: Not set");
//        txtEndTime.setText("End: Not set");
//
//        // Setup day checkboxes
//        for (String day : DAYS_OF_WEEK) {
//            CheckBox checkBox = new CheckBox(this);
//            checkBox.setText(day);
//            checkBox.setTextColor(ContextCompat.getColor(this, R.color.black));
//            checkBox.setPadding(16, 8, 16, 8); // Add padding for better touch area
//            checkBox.setLayoutParams(new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//            ));
//            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
//                if (isChecked) {
//                    selectedDays.add(day);
//                } else {
//                    selectedDays.remove(day);
//                }
//            });
//            daysContainer.addView(checkBox);
//        }
//
//        // Time range button
//        btnSetTimeRange.setOnClickListener(v -> showTimeRangePicker(txtStartTime, txtEndTime));
//
//        // Set dialog buttons
//        builder.setPositiveButton("Set", (dialog, which) -> {
//            if (selectedDays.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
//                Toast.makeText(this, "Please select days and time range", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // Format days (e.g., "Mon-Fri" or "Mon, Wed, Fri")
//            String daysFormatted = formatDays(selectedDays);
//            edtAvailability.setText(daysFormatted + ", " + startTime + " - " + endTime);
//        });
//
//        builder.setNegativeButton("Cancel", null);
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//
//    private String formatDays(List<String> days) {
//        if (days.contains("Mon") && days.contains("Tue") && days.contains("Wed") &&
//                days.contains("Thu") && days.contains("Fri") && !days.contains("Sat") && !days.contains("Sun")) {
//            return "Mon-Fri";
//        } else if (days.contains("Mon") && days.contains("Tue") && days.contains("Wed") &&
//                days.contains("Thu") && days.contains("Fri") && days.contains("Sat") && !days.contains("Sun")) {
//            return "Mon-Sat";
//        } else if (days.contains("Mon") && days.contains("Tue") && days.contains("Wed") &&
//                days.contains("Thu") && days.contains("Fri") && days.contains("Sat") && days.contains("Sun")) {
//            return "Everyday";
//        } else {
//            // Format as comma-separated list
//            StringBuilder sb = new StringBuilder();
//            for (int i = 0; i < days.size(); i++) {
//                sb.append(days.get(i));
//                if (i < days.size() - 1) {
//                    sb.append(", ");
//                }
//            }
//            return sb.toString();
//        }
//    }
//
//    private void showTimeRangePicker(TextView txtStart, TextView txtEnd) {
//        Calendar calendar = Calendar.getInstance();
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);
//
//        // Start time picker
//        TimePickerDialog startTimePicker = new TimePickerDialog(
//                this,
//                (view, hourOfDay, minute1) -> {
//                    startTime = formatTime(hourOfDay, minute1);
//                    txtStart.setText("Start: " + startTime);
//
//                    // End time picker
//                    TimePickerDialog endTimePicker = new TimePickerDialog(
//                            TherapistRegisterScreen.this,
//                            (view1, hourOfDay1, minute11) -> {
//                                endTime = formatTime(hourOfDay1, minute11);
//                                txtEnd.setText("End: " + endTime);
//                            },
//                            hourOfDay, // Default to start time hour
//                            minute1,   // Default to start time minute
//                            false
//                    );
//                    endTimePicker.setTitle("Select End Time");
//                    endTimePicker.show();
//                },
//                hour,
//                minute,
//                false
//        );
//        startTimePicker.setTitle("Select Start Time");
//        startTimePicker.show();
//    }
//
//    private String formatTime(int hour, int minute) {
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.HOUR_OF_DAY, hour);
//        cal.set(Calendar.MINUTE, minute);
//
//        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
//        return sdf.format(cal.getTime());
//    }
//
//    private boolean handlePasswordToggle(View v, MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_UP) {
//            int drawableRight = 2;
//            if (event.getRawX() >= (edtPassword.getRight() - edtPassword.getCompoundDrawables()[drawableRight].getBounds().width())) {
//                togglePasswordVisibility();
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private void togglePasswordVisibility() {
//        int drawableStart = R.drawable.password_icon;
//        edtPassword.setTransformationMethod(isPasswordVisible ? new PasswordTransformationMethod() : null);
//        edtPassword.setCompoundDrawablesWithIntrinsicBounds(drawableStart, 0,
//                isPasswordVisible ? R.drawable.eye_close_icon : R.drawable.eye_open_icon, 0);
//        isPasswordVisible = !isPasswordVisible;
//        edtPassword.setSelection(edtPassword.getText().length());
//    }
//
//    private void validateAndSubmit() {
//        if (!validateName() || !validateEmail() || !validatePassword() || !validatePhone() ||
//                !validateQualification() || !validateField() || !validateSpecialization() ||
//                !validateExperience() || !validateSessionDuration() || !validateAvailability() ||
//                !validateBio() || !validateClinicName() || !validateAddress() || !validateSessionFee() ||
//                !validateLanguage() || !validateLicenseNo() || !validateLicenseImage() || !validateImages()) {
//            return;
//        }
//
//        progressBar.setVisibility(View.VISIBLE);
//        btn_signup.setEnabled(false);
//
//        // Save images to local storage and get their paths
//        String frontImagePath = saveImageToLocalStorage(bitmapFront, "front_image");
//        String backImagePath = saveImageToLocalStorage(bitmapBack, "back_image");
//        String licenseImagePath = saveImageToLocalStorage(bitmapLicense, "licence_certificate");
//
//        if (frontImagePath == null || backImagePath == null || licenseImagePath == null) {
//            Toast.makeText(this, "Failed to save images", Toast.LENGTH_SHORT).show();
//            progressBar.setVisibility(View.GONE);
//            btn_signup.setEnabled(true);
//            return;
//        }
//
//        // Get selected session duration from radio group
//        int selectedDurationId = radio_group_session_duration.getCheckedRadioButtonId();
//        RadioButton selectedButton = findViewById(selectedDurationId);
//        int sessionDurationValue = Integer.parseInt(
//                selectedButton.getText().toString().replace(" mins", "")
//        );
//
//        // Get selected languages
//        String languages = multi_language.getText().toString().trim();
//
//        // Clean session fee value (remove "Rs. ")
//        String cleanSessionFee = edtSessionFee.getText().toString().replace("Rs. ", "").trim();
//
//        // Proceed with registration
//        registerTherapist(
//                edtName.getText().toString().trim(),
//                edtEmail.getText().toString().trim(),
//                edtPassword.getText().toString(),
//                edtPhone.getText().toString().trim(),
//                spinnerQualification.getSelectedItem().toString(),
//                spinnerFieldOfStudy.getSelectedItem().toString(),
//                spinnerSpecialization.getSelectedItem().toString(),
//                sessionDurationValue,  // Now an int
//                edtExperience.getText().toString().trim(),
//                edtAvailability.getText().toString().trim(),
//                edtBio.getText().toString().trim(),
//                edtClinicName.getText().toString().trim(),
//                edtAddress.getText().toString().trim(),
//                cleanSessionFee,
//                languages,  // Comma-separated string
//                edt_licence_no.getText().toString().trim(),
//                licenseImagePath,  // Licence certificate image path
//                frontImagePath,
//                backImagePath
//        );
//    }
//
//    private boolean validateName() {
//        String name = edtName.getText().toString().trim();
//        if (TextUtils.isEmpty(name)) {
//            edtName.setError("Enter your full name");
//            return false;
//        }
//        return true;
//    }
//
//    private boolean validateEmail() {
//        String email = edtEmail.getText().toString().trim();
//        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            edtEmail.setError("Enter a valid email");
//            return false;
//        }
//        return true;
//    }
//
//    private boolean validatePassword() {
//        String password = edtPassword.getText().toString();
//        if (TextUtils.isEmpty(password) || password.length() < 6 || !password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{6,}$")) {
//            edtPassword.setError("Password must be at least 6 characters and contain at least one uppercase letter, one lowercase letter, one digit, and one special character");
//            return false;
//        }
//        return true;
//    }
//
//    private boolean validatePhone() {
//        String phone = edtPhone.getText().toString().trim();
//        if (TextUtils.isEmpty(phone) || !Patterns.PHONE.matcher(phone).matches()) {
//            edtPhone.setError("Enter a valid phone number");
//            return false;
//        }
//        return true;
//    }
//
//    private boolean validateQualification() {
//        if (spinnerQualification.getSelectedItem().toString().equals("Select Qualification")) {
//            Toast.makeText(this, "Please select a qualification", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }
//
//    private boolean validateField() {
//        if (spinnerFieldOfStudy.getSelectedItem().toString().equals("Select Field of Study")) {
//            Toast.makeText(this, "Please select a field", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }
//
//    private boolean validateSpecialization() {
//        if (spinnerSpecialization.getSelectedItem().toString().equals("Select Specialization")) {
//            Toast.makeText(this, "Please select a specialization", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }
//
//    private boolean validateExperience() {
//        String experience = edtExperience.getText().toString().trim();
//        if (TextUtils.isEmpty(experience)) {
//            edtExperience.setError("Enter your experience");
//            return false;
//        }
//        return true;
//    }
//
//    private boolean validateSessionDuration() {
//        int selectedId = radio_group_session_duration.getCheckedRadioButtonId();
//        if (selectedId == -1) {
//            Toast.makeText(this, "Please select session duration", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }
//
//    private boolean validateAvailability() {
//        String availability = edtAvailability.getText().toString().trim();
//        if (TextUtils.isEmpty(availability)) {
//            Toast.makeText(this, "Please set your availability", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }
//
//    private boolean validateBio() {
//        String bio = edtBio.getText().toString().trim();
//        if (TextUtils.isEmpty(bio)) {
//            edtBio.setError("Enter your bio");
//            return false;
//        }
//        return true;
//    }
//
//    private boolean validateClinicName() {
//        String clinicName = edtClinicName.getText().toString().trim();
//        if (TextUtils.isEmpty(clinicName)) {
//            edtClinicName.setError("Enter your clinic name");
//            return false;
//        }
//        return true;
//    }
//
//    private boolean validateAddress() {
//        String address = edtAddress.getText().toString().trim();
//        if (TextUtils.isEmpty(address)) {
//            edtAddress.setError("Enter your address");
//            return false;
//        }
//        return true;
//    }
//
//    private boolean validateSessionFee() {
//        String sessionFee = edtSessionFee.getText().toString().trim();
//        if (TextUtils.isEmpty(sessionFee) || sessionFee.equals("Rs.") || !sessionFee.replace("Rs. ", "").matches("\\d+(\\.\\d{1,2})?")) {
//            edtSessionFee.setError("Enter a valid session fee");
//            return false;
//        }
//        return true;
//    }
//
//    private boolean validateLanguage() {
//        String languages = multi_language.getText().toString().trim();
//        if (TextUtils.isEmpty(languages)) {
//            Toast.makeText(this, "Please select at least one language", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }
//
//    private boolean validateLicenseNo() {
//        String licenseNo = edt_licence_no.getText().toString().trim();
//        if (TextUtils.isEmpty(licenseNo)) {
//            edt_licence_no.setError("Enter your licence number");
//            return false;
//        }
//        return true;
//    }
//
//    private boolean validateLicenseImage() {
//        if (bitmapLicense == null) {
//            Toast.makeText(this, "Please upload your licence certificate", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }
//
//    private boolean validateImages() {
//        if (bitmapFront == null) {
//            Toast.makeText(this, "Please select a front ID image", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (bitmapBack == null) {
//            Toast.makeText(this, "Please select a back ID image", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }
//
//    private void setupSpinners() {
//        // Qualification Spinner
//        String[] qualificationOptions = {"Select Qualification", "BA", "BSc", "Ms", "MSc", "MPhil", "PhD"};
//        setupSpinner(spinnerQualification, qualificationOptions);
//
//        // Field of Study Spinner
//        String[] fieldOptions = {"Select Field of Study", "Psychology", "Counseling", "Social Work", "Psychiatry"};
//        setupSpinner(spinnerFieldOfStudy, fieldOptions);
//
//        // Specialization Spinner
//        String[] specializationOptions = {"Select Specialization", "Clinical Psychology", "Counseling Psychology",
//                "Marriage and Family Therapy", "Career Counseling", "Addiction Counseling",
//                "Clinical Social Work"};
//        setupSpinner(spinnerSpecialization, specializationOptions);
//
//        // Setup language multi-select
//        setupLanguageInput();
//    }
//
//    private void setupLanguageInput() {
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                this,
//                android.R.layout.simple_dropdown_item_1line,
//                getResources().getStringArray(R.array.languages)
//        );
//        multi_language.setAdapter(adapter);
//        multi_language.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
//    }
//
//    private void setupSpinner(Spinner spinner, String[] options) {
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//    }
//
//    private void registerTherapist(String name, String email, String password, String phone, String qualification, String field,
//                                   String specialization, int sessionDuration, String experience, String availability,
//                                   String bio, String clinicName, String address, String sessionFee, String languages,
//                                   String licenseNo, String licenseImage, String frontImagePath, String backImagePath) {
//        if (!isNetworkAvailable()) {
//            showSnackbar(TherapistRegisterScreen.this, getRootView(this), "No internet connection", true);
//            progressBar.setVisibility(View.GONE);
//            btn_signup.setEnabled(true);
//            return;
//        }
//
//        auth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        FirebaseUser user = auth.getCurrentUser();
//                        if (user != null) {
//                            String uid = user.getUid();
//
//                            double sessionFeeValue = Double.parseDouble(sessionFee);
//                            int experienceValue = Integer.parseInt(experience);
//
//                            // Get selected session duration text with units
//                            int selectedDurationId = radio_group_session_duration.getCheckedRadioButtonId();
//                            RadioButton selectedButton = findViewById(selectedDurationId);
//                            String sessionDurationWithUnit = selectedButton.getText().toString();
//
//                            // Format session fee with currency
//                            String sessionFeeWithCurrency = "Rs " + sessionFee;
//
//                            Map<String, Object> therapistData = new HashMap<>();
//                            therapistData.put("uid", uid);
//                            therapistData.put("name", name);
//                            therapistData.put("email", email);
//                            therapistData.put("phone", phone);
//                            therapistData.put("qualification", qualification);
//                            therapistData.put("field", field);
//                            therapistData.put("specialization", specialization);
//                            therapistData.put("sessionDuration", sessionDurationWithUnit);
//                            therapistData.put("sessionDurationValue", sessionDuration);
//                            therapistData.put("experience", experienceValue);
//                            therapistData.put("availability", availability);
//                            therapistData.put("bio", bio);
//                            therapistData.put("clinicName", clinicName);
//                            therapistData.put("address", address);
//                            therapistData.put("sessionFee", sessionFeeWithCurrency);
//                            therapistData.put("sessionFeeValue", sessionFeeValue);
//                            therapistData.put("language", languages);
//                            therapistData.put("role", "therapist");
//                            therapistData.put("licenseNo", licenseNo);
//                            therapistData.put("licenseImage", licenseImage);
//                            therapistData.put("frontImagePath", frontImagePath);
//                            therapistData.put("backImagePath", backImagePath);
//
//                            db.collection("therapists").document(uid)
//                                    .set(therapistData)
//                                    .addOnSuccessListener(aVoid -> {
//                                        Log.d("TherapistRegisterScreen", "User registered successfully: " + uid);
//                                        progressBar.setVisibility(View.GONE);
//                                        btn_signup.setEnabled(true);
//                                        showSnackbar(TherapistRegisterScreen.this, getRootView(this), "Registration Successful!", false);
//                                        startActivity(new Intent(TherapistRegisterScreen.this, TherapistDashboardScreen.class));
//                                        finish();
//                                    })
//                                    .addOnFailureListener(e -> {
//                                        Log.e("TherapistRegisterScreen", "Failed to store data: " + e.getMessage());
//                                        progressBar.setVisibility(View.GONE);
//                                        btn_signup.setEnabled(true);
//                                        showSnackbar(TherapistRegisterScreen.this, getRootView(this), "Failed to store data: " + e.getMessage(), true);
//                                    });
//                        } else {
//                            progressBar.setVisibility(View.GONE);
//                            btn_signup.setEnabled(true);
//                            showSnackbar(TherapistRegisterScreen.this, getRootView(this), "User registration failed: User is null", true);
//                        }
//                    } else {
//                        progressBar.setVisibility(View.GONE);
//                        btn_signup.setEnabled(true);
//                        showSnackbar(TherapistRegisterScreen.this, getRootView(this), "Registration failed: " + task.getException().getMessage(), true);
//                    }
//                });
//    }
//
//    private boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }
//
//    private static View getRootView(Context context) {
//        try {
//            return ((android.app.Activity) context).findViewById(R.id.ll_main);
//        } catch (Exception e) {
//            Toast.makeText(context, "Unable to find root view", Toast.LENGTH_SHORT).show();
//            return null;
//        }
//    }
//
//    public static void showSnackbar(Context context, View view, String message, boolean isError) {
//        if (view == null) {
//            view = getRootView(context);
//        }
//
//        if (view != null) {
//            Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
//
//            if (isError) {
//                snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.red));
//                snackbar.setTextColor(ContextCompat.getColor(context, R.color.white));
//            } else {
//                snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.olive_green));
//                snackbar.setTextColor(ContextCompat.getColor(context, R.color.white));
//            }
//
//            snackbar.setAction("DISMISS", v -> snackbar.dismiss());
//            snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.white));
//
//            snackbar.show();
//        } else {
//            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
//        }
//    }
//
//    private void pickImage() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
//                Bitmap compressedBitmap = compressImage(bitmap); // Compress the image dynamically
//
//                if (isLicenseImageSelected) {
//                    bitmapLicense = compressedBitmap;
//                    img_licence_certificate.setImageBitmap(bitmapLicense);
//                } else if (isFrontImageSelected) {
//                    bitmapFront = compressedBitmap;
//                    img_id_front.setImageBitmap(bitmapFront);
//                } else {
//                    bitmapBack = compressedBitmap;
//                    img_id_back.setImageBitmap(bitmapBack);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private Bitmap compressImage(Bitmap bitmap) {
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        int quality = 100; // Start with 100% quality
//        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
//
//        // Loop until image size is less than 1MB
//        while (outputStream.toByteArray().length > 1024 * 1024 && quality > 0) {
//            outputStream.reset(); // Clear the output stream
//            quality -= 10; // Reduce quality by 10%
//            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
//        }
//        return BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.toByteArray().length);
//    }
//
//    private String saveImageToLocalStorage(Bitmap bitmap, String imageName) {
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File imageFile = new File(storageDir, imageName + ".jpg");
//
//        try (FileOutputStream out = new FileOutputStream(imageFile)) {
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//            return imageFile.getAbsolutePath();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//}