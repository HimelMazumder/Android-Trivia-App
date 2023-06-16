package com.loghima.triviaapp01.data;

import com.loghima.triviaapp01.model.Question;

import java.util.ArrayList;

public interface QuestionBankAsyncResponse {
    void processFinished(ArrayList<Question> questionBank);
}
