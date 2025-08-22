package com.kd.aiservices.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kd.aiservices.model.Activity;
import com.kd.aiservices.model.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAi {

    private final GeminiService geminiService;

    private Recommendation processAiResponse(Activity activity,String aiResponse){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(aiResponse);
            JsonNode textNode = rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text");


            String jsonContent = textNode.asText()
                    .replaceAll("```json\\n", "")
                    .replaceAll("\\n```", "")
                    .trim();

            log.info("PARSED RESOPONSE FROM AI : {}",jsonContent);

            JsonNode analysisJson = objectMapper.readTree(jsonContent);
            JsonNode analysisNode = analysisJson.path("analysis");

            StringBuilder fullAnalysis = new StringBuilder();
            addAnalysisSection(fullAnalysis, analysisNode, "overall", "Overall: "  );
            addAnalysisSection(fullAnalysis, analysisNode, "pace", "Pace: "  );
            addAnalysisSection(fullAnalysis, analysisNode, "heartRate", "HeartRate: "  );
            addAnalysisSection(fullAnalysis, analysisNode, "CaloriesBurned", "CaloriesBurned: "  );

            List<String> improvements = extractImprovements(analysisJson.path("improvements"));

            List<String> suggestions = extractSuggestions(analysisJson.path("suggestions"));

            List<String> safety = extractSafety(analysisJson.path("safety"));

            return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(activity.getType())
                    .recommendation(fullAnalysis.toString().trim())
                    .improvements(improvements)
                    .suggestions(suggestions)
                    .safety(safety)
                    .createAt(LocalDateTime.now())
                    .build();

        }catch(Exception e){
            e.printStackTrace();
            return createDefaultRecommendation(activity);
        }

    }

    private Recommendation createDefaultRecommendation(Activity activity) {
            return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(activity.getType())
                    .recommendation("Unable to Generate")
                    .improvements(Collections.singletonList("Unable to Generate"))
                    .suggestions(Collections.singletonList("Unable to Generate"))
                    .safety(Arrays.asList("Unable to Generate"))
                    .createAt(LocalDateTime.now())
                    .build();
    }

    private List<String> extractSuggestions(JsonNode suggestionsNode) {
        List<String> suggestions = new ArrayList<>();
        if(suggestionsNode.isArray()){
            suggestionsNode.forEach(suggestion->{
                String workout = suggestion.path("workout").asText();
                String description = suggestion.path("description").asText();
                suggestions.add(String.format("%s\n%s", workout, description));
            });
        }
        return suggestions.isEmpty()?
                Collections.singletonList("No specific suggestions provided"):
                suggestions;
    }

    private List<String> extractImprovements(JsonNode improvementsNode){
        List<String> improvements = new ArrayList<>();
        if(improvementsNode.isArray()){
            improvementsNode.forEach(improvement -> {
                String area = improvement.path("area").asText();
                String detail = improvement.path("recommendation").asText();
                improvements.add(String.format("%s (%s)", area, detail));
            });
        }
        return improvements.isEmpty()?
                Collections.singletonList("No Specific improvements provided "):
                improvements;
    }

    private List<String> extractSafety(JsonNode safetyNode) {
        List<String> safety = new ArrayList<>();
        if(safetyNode.isArray()){
            safetyNode.forEach(item->safety.add(item.asText()));
        }
        return safety.isEmpty()?
                Collections.singletonList("No Specific safety guidelines provided "):
                safety;
    }

    private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix) {
        if(!analysisNode.path(key).isMissingNode()){
            fullAnalysis.append(prefix)
                    .append(analysisNode.path(key).asText())
                    .append("\n\n");
        }
    }

    public Recommendation generateRecommendation(Activity activity) {
        String prompt = createPromptForActivity(activity);
        String aiResponse = geminiService.getAnswer(prompt);
        log.info("AI Response: {}", aiResponse);
        return processAiResponse(activity,aiResponse);
    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
            Analyze this fitness activity and provide detailed recommendations in the following format
            {
                "analysis" : {
                    "overall": "Overall analysis here",
                    "pace": "Pace analysis here",
                    "heartRate": "Heart rate analysis here",
                    "CaloriesBurned": "Calories Burned here"
                },
                "improvements": [
                    {
                        "area": "Area name",
                        "recommendation": "Detailed Recommendation"
                    }
                ],
                "suggestions" : [
                    {
                        "workout": "Workout name",
                        "description": "Detailed workout description"
                    }
                ],
                "safety": [
                    "Safety point 1",
                    "Safety point 2"
                ]
            }

            Analyze this activity:
            - User ID: %s
            - Activity Type: %s
            - Duration: %d minutes
            - Calories Burned: %d
            - Start Time: %s
            - Additional Metrics: %s

            Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
            Ensure the response follows the EXACT JSON format shown above.
            """,
                activity.getUserId(),
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getStartTime(),
                activity.getAdditionalMetrics() == null ? "{}" : activity.getAdditionalMetrics().toString()
        );
    }

}
