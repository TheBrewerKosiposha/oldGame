package com.faa.knowyourgame_new.ui.answer_status_dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.faa.knowyourgame_new.R;
import com.faa.knowyourgame_new.ui.question_dialog.QuestionDialogFragment;

import static com.faa.knowyourgame_new.MainActivity.userDao;
import static com.faa.knowyourgame_new.ui.home.HomeFragment.AnswerTrueness;
import static com.faa.knowyourgame_new.ui.home.HomeFragment.LeagueIcon;
import static com.faa.knowyourgame_new.ui.home.HomeFragment.TextUserScore;
import static com.faa.knowyourgame_new.ui.home.HomeFragment.loadLeagueImg;
import static com.faa.knowyourgame_new.ui.question_dialog.QuestionDialogFragment.CurrentQuestion;
import static com.faa.knowyourgame_new.ui.question_dialog.QuestionDialogFragment.PointsToScore;
import static com.faa.knowyourgame_new.ui.question_dialog.QuestionDialogFragment.QuestionsCount;

public class AnswerStatusDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    private static DialogFragment NEXT_QUESTION_DIALOG = new QuestionDialogFragment();

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        View view = getLayoutInflater().inflate(R.layout.answer_status_dialog, null);
        dialog.setView(view);

        TextView answerStatus = view.findViewById(R.id.answer_status);

        Button nextQuestion = view.findViewById(R.id.btn_next_question);
        Button lastQuestion = view.findViewById(R.id.btn_last_question);

        Dialog answerStatusDialog = dialog.create();

        if(AnswerTrueness == 0){
            answerStatus.setText("Correct!\n You earned " + PointsToScore + " points");
        }
        if(AnswerTrueness == 1){
            answerStatus.setText("Wrong!\n You lost " + PointsToScore + " points");
        }

        nextQuestion.setOnClickListener(v -> {
            NEXT_QUESTION_DIALOG.show(this.getParentFragmentManager(), "NEXT_QUESTION_DIALOG");
            answerStatusDialog.dismiss();
        });

        lastQuestion.setOnClickListener(v -> {
            answerStatusDialog.dismiss();
        });

        if(CurrentQuestion == QuestionsCount){
            answerStatus.setText("Well done!\n You answered(or not) on all questions!");
            nextQuestion.setVisibility(View.INVISIBLE);
            lastQuestion.setVisibility(View.VISIBLE);

            CurrentQuestion = 0;
        }

        TextUserScore.setText("Your current score - " + userDao.getUserScore());
        loadLeagueImg(LeagueIcon);
        return  answerStatusDialog;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) { }
}
