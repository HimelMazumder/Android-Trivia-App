package com.loghima.triviaapp01.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.loghima.triviaapp01.controller.AppController;
import com.loghima.triviaapp01.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    private ArrayList<Question> questionBank;
    private String quizApiUrl = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public ArrayList<Question> getQuestionBank(final QuestionBankAsyncResponse questionBankAsyncResponse) {
        questionBank = new ArrayList<>();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, quizApiUrl, null,
                response -> {
                    JSONArray jsonArray;
                    // questionBank = new ArrayList<>();

                    for (int i = 0; i < 25; i++) {
                        try {
                            jsonArray = response.getJSONArray(i);

                            // getString() returns String
                            // getBoolean() returns boolean
                            // get() returns ultimate super class Object
                            String q = jsonArray.getString(0);
                            boolean isTrue = jsonArray.getBoolean(1);

                            // Log.d("Repository", "getQuestionBank: " + jsonArray);
                            // Log.d("Repository", "getQuestions: Q: " + q);
                            // Log.d("Repository", "getQuestions: A: " + isTrue);

                            Question question = new Question(q, isTrue);
                            questionBank.add(question);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // checking if questionBank is null or not
                    // means whether data is fetched or not
                    if (questionBank != null) {
                        questionBankAsyncResponse.processFinished(questionBank);
                    }
                },
                error -> Log.d("Repository", "onErrorResponse: failed to fetch!"));
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return questionBank;
    }
}
