package com.faa.knowyourgame_new.ui.home;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.faa.knowyourgame_new.R;
import com.faa.knowyourgame_new.entity.Difficulty;
import com.faa.knowyourgame_new.entity.Question;
import com.faa.knowyourgame_new.ui.question_dialog.QuestionDialogFragment;
import com.faa.knowyourgame_new.ui.rating_dialog.RatingDialogFragment;

import java.util.ArrayList;
import java.util.List;

import static com.faa.knowyourgame_new.MainActivity.IMAGE_PATH;
import static com.faa.knowyourgame_new.MainActivity.difficultyDao;
import static com.faa.knowyourgame_new.MainActivity.leagueDao;
import static com.faa.knowyourgame_new.MainActivity.questionDao;
import static com.faa.knowyourgame_new.MainActivity.userDao;
import static com.faa.knowyourgame_new.ui.login_dialog.LoginDialogFragment.LoginUserName;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private DialogFragment MainQuestionDialogFragment = new QuestionDialogFragment();
    private DialogFragment RatingDialogFragment = new RatingDialogFragment();
    private HomeViewModel homeViewModel;

    private static int ChosenDif;
    public static List<Question> ChosenQuestions;
    public static TextView TextUserScore;
    public static ImageView LeagueIcon;
    public static int AnswerTrueness = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        TextUserScore = root.findViewById(R.id.text_home);
        Spinner spinner = root.findViewById(R.id.difficulty_spinner);
        Button playButton = root.findViewById(R.id.btn_play);

        ImageView ratingIcon = root.findViewById(R.id.rating_icon);
        LeagueIcon = root.findViewById(R.id.league_icon);

        loadLeagueImg(LeagueIcon);

        ratingIcon.setOnClickListener(listener ->
                RatingDialogFragment.show(this.getParentFragmentManager(), "RATING_DIALOG"));

        playButton.setOnClickListener(listener ->
                MainQuestionDialogFragment.show(this.getParentFragmentManager(), "QUESTION_DIALOG")
        );

        List<Difficulty> difficultyList = difficultyDao.getAll();
        List<String> difficultyNames = new ArrayList<>();

        for(int i = 0; i < difficultyList.size(); i++){
            difficultyNames.add(difficultyList.get(i).getName()) ;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(),
                android.R.layout.simple_spinner_item, difficultyNames);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                ChosenDif = difficultyDao.getIdByName(item);
                ChosenQuestions = questionDao.getQuestionsByDiffId(ChosenDif);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        };
        spinner.setOnItemSelectedListener(itemSelectedListener);

        /*homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                TextUserScore.setText(s);
            }
        });*/

        TextUserScore.setText("Your current score - " + userDao.getUserScore());
        return root;
    }


    public static void loadLeagueImg(ImageView _leagueIcon){

        if(userDao.getUserScore() >= 0 && userDao.getUserScore() <= 100) {
            String leagueImgName = leagueDao.getLeagueImg("Bronze");
            _leagueIcon.setImageDrawable(Drawable.createFromPath(IMAGE_PATH + "/" + leagueImgName));
        }
        if(userDao.getUserScore() >= 101 && userDao.getUserScore() <= 150){
            String leagueImgName = leagueDao.getLeagueImg("Silver");
            _leagueIcon.setImageDrawable(Drawable.createFromPath(IMAGE_PATH + "/" + leagueImgName));
        }
        if(userDao.getUserScore() >= 151){
            String leagueImgName = leagueDao.getLeagueImg("Gold");
            _leagueIcon.setImageDrawable(Drawable.createFromPath(IMAGE_PATH + "/" + leagueImgName));
        }
    }
}