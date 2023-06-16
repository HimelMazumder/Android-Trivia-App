package com.loghima.triviaapp01.model;

import androidx.annotation.NonNull;

public class Question {
    private String question;
    private boolean isTrue;

    public Question() {
    }

    public Question(String question, boolean isTrue) {
        this.question = question;
        this.isTrue = isTrue;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isTrue() {
        return isTrue;
    }

    public void setTrue(boolean aTrue) {
        isTrue = aTrue;
    }



    @NonNull
    @Override
    public String toString() {
        return "Question { question: " + question +
                " isTrue: " + isTrue + "}";
    }
}
