package com.faa.knowyourgame_new.ui.question_dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.faa.knowyourgame_new.R;
import com.faa.knowyourgame_new.entity.Answer;
import com.faa.knowyourgame_new.entity.Logs;
import com.faa.knowyourgame_new.entity.Question;
import com.faa.knowyourgame_new.entity.User;
import com.faa.knowyourgame_new.ui.answer_status_dialog.AnswerStatusDialogFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.faa.knowyourgame_new.MainActivity.IMAGE_PATH;
import static com.faa.knowyourgame_new.MainActivity.answerDao;
import static com.faa.knowyourgame_new.MainActivity.difficultyDao;
import static com.faa.knowyourgame_new.MainActivity.logsDao;
import static com.faa.knowyourgame_new.MainActivity.themeDao;
import static com.faa.knowyourgame_new.MainActivity.userDao;
import static com.faa.knowyourgame_new.ui.home.HomeFragment.AnswerTrueness;
import static com.faa.knowyourgame_new.ui.home.HomeFragment.ChosenQuestions;

public class QuestionDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    public static int CurrentQuestion = 0;
    public static int PointsToScore = 0;
    public static int QuestionsCount = 0;

    private DialogFragment answerStatusDialogFragment = new AnswerStatusDialogFragment();

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        View view = getLayoutInflater().inflate(R.layout.question_dialog, null);
        dialog.setView(view);

        TextView themeText = view.findViewById(R.id.theme_text);
        TextView chosenAnswer = view.findViewById(R.id.chosen_answer);
        TextView timerCountdown = view.findViewById(R.id.countdown);
        TextView questionText = view.findViewById(R.id.question_text);

        ImageView questionImage = view.findViewById(R.id.question_image);

        Button answer_var_one = view.findViewById(R.id.btn_answer_var_one);
        Button answer_var_two = view.findViewById(R.id.btn_answer_var_two);
        Button answer_var_three = view.findViewById(R.id.btn_answer_var_three);
        Button answer_on_question = view.findViewById(R.id.btn_answer);

        Dialog questionDialog = dialog.create();

        List<Question> QUESTIONS = ChosenQuestions;
        List<Answer> ANSWERS;

        themeText.setText("Current theme - " + themeDao.getNameById(QUESTIONS.get(CurrentQuestion).getTheme_id()));

        ANSWERS = answerDao.getAnswersForQuestion(QUESTIONS.get(CurrentQuestion).get_id());

        questionImage.setImageDrawable(Drawable.createFromPath(IMAGE_PATH + "/" + QUESTIONS.get(CurrentQuestion).getImage()));
        questionText.setText(QUESTIONS.get(CurrentQuestion).getText());

        QuestionsCount = QUESTIONS.size();

        answer_var_one.setText(ANSWERS.get(0).getText());
        answer_var_two.setText(ANSWERS.get(1).getText());
        answer_var_three.setText(ANSWERS.get(2).getText());

        CountDownTimer answerTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerCountdown.setText("Time left: " + millisUntilFinished / 1000);
            }
            public void onFinish() {
                timerCountdown.setText("Times up!");
                userAnswerStatus(QUESTIONS, AnswerTrueness);
                questionDialog.dismiss();
            }
        }.start();


        answer_var_one.setOnClickListener(v -> {
            AnswerTrueness = ANSWERS.get(0).getTrueness();
            chosenAnswer.setText("Chosen 1 variant");
        });

        answer_var_two.setOnClickListener(v -> {
            AnswerTrueness = ANSWERS.get(1).getTrueness();
            chosenAnswer.setText("Chosen 2 variant");
        });

        answer_var_three.setOnClickListener(v -> {
            AnswerTrueness = ANSWERS.get(2).getTrueness();
            chosenAnswer.setText("Chosen 3 variant");
        });

        answer_on_question.setOnClickListener(v -> {
            answerTimer.cancel();

            userAnswerStatus(QUESTIONS, AnswerTrueness);
            questionDialog.dismiss();

            answerStatusDialogFragment.show(this.getParentFragmentManager(), "ANSWER_STATUS_DIALOG");
        });

        return questionDialog;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) { }

    private static void userAnswerStatus(List<Question> questions, int _answerStatus) {

        User currentUser = userDao.getCurrentUser();
        double multiplier = difficultyDao.getMultiplierById(questions.get(CurrentQuestion).getDifficulty_id());
        int questionCost = questions.get(CurrentQuestion).getCost();

        int pointsToScore = (int) (questionCost * multiplier);
        PointsToScore = pointsToScore;

        if(currentUser.getScore() < 0){
            currentUser.setScore(0);
            userDao.update(currentUser);
        }
        if(_answerStatus == 1){
            currentUser.setScore(currentUser.getScore() - pointsToScore);
            userDao.update(currentUser);
        }
        if(_answerStatus == 0){
            currentUser.setScore(currentUser.getScore() + pointsToScore);
            userDao.update(currentUser);
        }

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);

        Logs userLog = new Logs();
        userLog.setAnswerStatus(AnswerTrueness);
        userLog.setLogin(currentUser.getLogin());
        userLog.setPoints(pointsToScore);
        userLog.setDateTime(dateText);

        logsDao.insert(userLog);

        CurrentQuestion++;
    }
}
