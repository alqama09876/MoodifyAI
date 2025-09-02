package com.developer.moodifyai.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import com.developer.moodifyai.model.ChatRequest;
import com.developer.moodifyai.model.ChatResponse;

public interface ChatbotApiService {
    @POST("chat") // API endpoint
    Call<ChatResponse> getBotResponse(@Body ChatRequest request);
}