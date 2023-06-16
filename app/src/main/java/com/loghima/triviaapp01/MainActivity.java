package com.loghima.triviaapp01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.snackbar.Snackbar;
import com.loghima.triviaapp01.data.Repository;
import com.loghima.triviaapp01.databinding.ActivityMainBinding;
import com.loghima.triviaapp01.model.Question;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private int currentQuestionIndex;
    private int prevQIndex;

    private int score;
    private static final String SCORE_FILE = "Score File";
    private List<Question> questionBank;
    private boolean isAnswered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        questionBank = new Repository().getQuestionBank(questionBank1 -> {

            SharedPreferences sharedPreferencesData = getSharedPreferences(SCORE_FILE, MODE_PRIVATE);

            currentQuestionIndex = sharedPreferencesData.getInt("current_question_index", 0);
            score = sharedPreferencesData.getInt("score", 0);
            isAnswered = sharedPreferencesData.getBoolean("is_answered", false);
            prevQIndex = sharedPreferencesData.getInt("prev_q_index", 0);

            binding.textViewScore.setText(String.format(getString(R.string.score_text),
                    score, (questionBank.size() * 2)));



            updateStuffs();
        });

        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!questionBank.isEmpty()) {
                    if (isAnswered) {
                        updateStuffs();
                        isAnswered = false;
                        binding.textViewAnswer.setVisibility(View.INVISIBLE);
                    } else {
                        Snackbar.make(binding.cardView, R.string.no_answered_prompt, Snackbar.LENGTH_SHORT)
                                .show();
                    }
                }

            }
        });

        binding.trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!questionBank.isEmpty()) {
                    checkAnswer(true);
                    isAnswered = true;
                }
            }
        });

        binding.falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!questionBank.isEmpty()) {
                    checkAnswer(false);
                    isAnswered = true;
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getSharedPreferences(SCORE_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        currentQuestionIndex = prevQIndex;

        editor.putInt("current_question_index", prevQIndex);
        editor.putInt("score", score);
        editor.putBoolean("is_answered", isAnswered);
        editor.putInt("prev_q_index", prevQIndex);
        editor.apply();
    }

    public void checkAnswer(boolean userChoseTrue) {
        if (questionBank.get(currentQuestionIndex).isTrue() == userChoseTrue) {
            showAnswer(true);
            fadeAnimation();
            if (!isAnswered) {
                score += 2;
                binding.textViewScore.setText(String.format(getString(R.string.score_text),
                        score, (questionBank.size() * 2)));
            }
        } else {
            showAnswer(false);
            shakeAnimation();
        }
    }

    public void updateStuffs() {
        binding.questionTextView.setText(questionBank.get(currentQuestionIndex).getQuestion());
        binding.textViewQuestionOutOf.setText(String.format(getString(R.string.out_of_text),
                (currentQuestionIndex + 1), questionBank.size()));

        if (currentQuestionIndex == 0) {
            if (currentQuestionIndex == prevQIndex) {
                if (!isAnswered) {
                    score = 0;
                    binding.textViewScore.setText(String.format(getString(R.string.score_text),
                            score, (questionBank.size() * 2)));
                }
            } else {
                score = 0;
                binding.textViewScore.setText(String.format(getString(R.string.score_text),
                        score, (questionBank.size() * 2)));
            }

        }

        prevQIndex = currentQuestionIndex;
        currentQuestionIndex = (currentQuestionIndex + 1) % questionBank.size();
    }

    public void showAnswer(boolean userIsRight) {
        if (userIsRight) {
            binding.textViewAnswer.setText(R.string.right_answer);
            binding.textViewAnswer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.right_answer_color));
        } else {
            binding.textViewAnswer.setText(R.string.wrong_answer);
            binding.textViewAnswer.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.wrong_answer_color));
        }
        binding.textViewAnswer.setVisibility(View.VISIBLE);
    }

    public void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);
        // binding.textViewAnswer.setAnimation(shake);
        binding.cardView.setAnimation(shake);

        // after attaching the animation to a view,
        // the view needs to be refreshed,
        // in order for the animation to be in effect
        // binding.questionTextView.setAnimation(shake);
        // binding.questionTextView.setText(questionBank.get(currentQuestionIndex - 1).getQuestion());

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextView.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextView.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void fadeAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(250);
        alphaAnimation.setRepeatCount(2);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        binding.cardView.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextView.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextView.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void bounceAnimation() {
        Animation blink = AnimationUtils.loadAnimation(MainActivity.this, R.anim.blink_animation);
        binding.cardView.setAnimation(blink);

        // setting animation listener
        blink.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextView.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextView.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}