package com.faa.knowyourgame_new.retrofit.utils;

import android.util.Log;

import com.faa.knowyourgame_new.dao.AnswerDao;
import com.faa.knowyourgame_new.dao.DifficultyDao;
import com.faa.knowyourgame_new.dao.LeagueDao;
import com.faa.knowyourgame_new.dao.QuestionDao;
import com.faa.knowyourgame_new.dao.ThemeDao;
import com.faa.knowyourgame_new.dto.AnswersDto;
import com.faa.knowyourgame_new.dto.DbDto;
import com.faa.knowyourgame_new.dto.DifficultyDto;
import com.faa.knowyourgame_new.dto.LeagueDto;
import com.faa.knowyourgame_new.dto.LogsDto;
import com.faa.knowyourgame_new.dto.QuestionDto;
import com.faa.knowyourgame_new.dto.RatingDto;
import com.faa.knowyourgame_new.dto.ThemeDto;
import com.faa.knowyourgame_new.dto.UserDto;
import com.faa.knowyourgame_new.entity.Answer;
import com.faa.knowyourgame_new.entity.Difficulty;
import com.faa.knowyourgame_new.entity.League;
import com.faa.knowyourgame_new.entity.Logs;
import com.faa.knowyourgame_new.entity.Question;
import com.faa.knowyourgame_new.entity.Theme;

import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.faa.knowyourgame_new.MainActivity.IMAGE_PATH;
import static com.faa.knowyourgame_new.MainActivity.questionDao;
import static com.faa.knowyourgame_new.retrofit.RetrofitClient.myService;

public class DbUtils {

    private static final String TAG = "DbUtils";

    public interface DbCallBack<T>{
        void dbData(DbDto getDataResponse);
    }

    public interface RatingCallback<T>{
        void rating(RatingDto getRatingResponse);
    }

    public static void sendLog(Logs log) {
        myService.sendLogs(
                log.getLogin(),
                log.getAnswerStatus(),
                log.getPoints(),
                log.getDateTime()
        ).enqueue(new Callback<LogsDto>() {
            @Override
            public void onResponse(
                    @NotNull Call<LogsDto> call,
                    @NotNull Response<LogsDto> response) {

            }

            @Override
            public void onFailure(
                    @NotNull Call<LogsDto> call,
                    @NotNull Throwable t) {
                Log.e(TAG, "Error loading from API (LOGS)");
            }
        });
    }

    public static void getRating(RatingCallback ratingCallback){
       myService.getRating().enqueue(new Callback<RatingDto>() {
           @Override
           public void onResponse(
                   @NotNull Call<RatingDto> call,
                   @NotNull Response<RatingDto> response) {
               ratingCallback.rating(response.body());
           }

           @Override
           public void onFailure(
                   @NotNull Call<RatingDto> call,
                   @NotNull Throwable t) {
               Log.e(TAG, "Error loading from API (Rating)");
           }
       });
    }

    public static void getData(DbCallBack dbCallBack){
        myService.getData().enqueue(new Callback<DbDto>() {
            @Override
            public void onResponse(
                    @NotNull Call<DbDto> call,
                    @NotNull Response<DbDto> response) {
                dbCallBack.dbData(response.body());
            }

            @Override
            public void onFailure(
                    @NotNull Call<DbDto> call,
                    @NotNull Throwable t) {
                Log.e(TAG, "Error loading from API");
            }
        });
    }

    public static void firstLaunch(
            DbDto dbDto,
            ThemeDao themeDao,
            DifficultyDao difficultyDao,
            LeagueDao leagueDao,
            QuestionDao questionDao,
            AnswerDao answerDao) {

        Log.d(TAG + ":FIRST_LAUNCH", dbDto.toString());

        List<Theme> serverThemes = new ArrayList<>();
        List<Difficulty> serverDifficulties = new ArrayList<>();
        List<League> serverLeagues = new ArrayList<>();
        List<Question> serverQuestions = new ArrayList<>();
        List<Answer> serverAnswers = new ArrayList<>();

        mapDto(dbDto,
                serverThemes,
                serverDifficulties,
                serverLeagues,
                serverQuestions,
                serverAnswers);

        themeDao.insertMany(serverThemes);
        difficultyDao.insertMany(serverDifficulties);
        leagueDao.insertMany(serverLeagues);
        questionDao.insertMany(serverQuestions);
        answerDao.insertMany(serverAnswers);

        for(League _serverLeague : serverLeagues) {
            new DownloadImageTask(
                    _serverLeague.getImage(),
                    IMAGE_PATH).execute(ApiUtils.BASE_SERVER_LEAGUE_IMAGE_DIR + _serverLeague.getImage());
        }

        for(Question _serverQuestion : serverQuestions) {
            new DownloadImageTask(
                    _serverQuestion.getImage(),
                    IMAGE_PATH).execute(ApiUtils.BASE_SERVER_QUESTION_IMAGE_DIR + _serverQuestion.getImage());
        }
    }

    public static void checkForUpdates(
            DbDto dbDto,
            ThemeDao themeDao,
            DifficultyDao difficultyDao,
            LeagueDao leagueDao,
            QuestionDao questionDao,
            AnswerDao answerDao) {

        Log.d(TAG + ":CHECK_FOR_UPDATES", dbDto.toString());

        List<Theme> serverThemes = new ArrayList<>();
        List<Difficulty> serverDifficulties = new ArrayList<>();
        List<League> serverLeagues = new ArrayList<>();
        List<Question> serverQuestions = new ArrayList<>();
        List<Answer> serverAnswers = new ArrayList<>();

        mapDto(dbDto,
                serverThemes,
                serverDifficulties,
                serverLeagues,
                serverQuestions,
                serverAnswers);

        List<Theme> dbThemes = themeDao.getAll();
        List<Difficulty> dbDifficulties = difficultyDao.getAll();
        List<League> dbLeagues = leagueDao.getAll();
        List<Question> dbQuestions = questionDao.getAll();
        List<Answer> dbAnswers = answerDao.getAll();

        insertMissingInDbAndUpdateExist(
                themeDao,
                difficultyDao,
                leagueDao,
                questionDao,
                answerDao,

                serverThemes,
                serverDifficulties,
                serverLeagues,
                serverQuestions,
                serverAnswers,

                dbThemes,
                dbDifficulties,
                dbLeagues,
                dbQuestions,
                dbAnswers);

        deleteData(
                themeDao,
                difficultyDao,
                leagueDao,
                questionDao,
                answerDao,

                serverThemes,
                serverDifficulties,
                serverLeagues,
                serverQuestions,
                serverAnswers,

                dbThemes,
                dbDifficulties,
                dbLeagues,
                dbQuestions,
                dbAnswers);
    }

    private static void mapDto(
            DbDto dbDto,
            List<Theme> serverThemes,
            List<Difficulty> serverDifficulties,
            List<League> serverLeagues,
            List<Question> serverQuestions,
            List<Answer> serverAnswers) {

        ModelMapper modelMapper = new ModelMapper();

        Theme serverTheme;
        Difficulty serverDifficulty;
        League serverLeague;
        Question serverQuestion;
        Answer serverAnswer;

        for(ThemeDto elem : dbDto.getThemes()){
            serverTheme = modelMapper.map(elem, Theme.class);
            serverThemes.add(serverTheme);
        }

        for(DifficultyDto elem: dbDto.getDifficulties()){
            serverDifficulty = modelMapper.map(elem, Difficulty.class);
            serverDifficulties.add(serverDifficulty);
        }

        for(LeagueDto elem: dbDto.getLeagues()){
            serverLeague = modelMapper.map(elem, League.class);
            serverLeagues.add(serverLeague);
        }

        for(QuestionDto elem: dbDto.getQuestions()){
            serverQuestion = modelMapper.map(elem, Question.class);
            serverQuestions.add(serverQuestion);
        }

        for(AnswersDto elem: dbDto.getAnswers()){
            serverAnswer = modelMapper.map(elem, Answer.class);
            serverAnswers.add(serverAnswer);
        }
    }

    private static void insertMissingInDbAndUpdateExist(
            ThemeDao themeDao,
            DifficultyDao difficultyDao,
            LeagueDao leagueDao,
            QuestionDao questionDao,
            AnswerDao answerDao,

            List<Theme> serverThemes,
            List<Difficulty> serverDifficulties,
            List<League> serverLeagues,
            List<Question> serverQuestions,
            List<Answer> serverAnswers,

            List<Theme> dbThemes,
            List<Difficulty> dbDifficulties,
            List<League> dbLeagues,
            List<Question> dbQuestions,
            List<Answer> dbAnswers){

        for(Question _serverQuestion : serverQuestions) {
            new DownloadImageTask(
                    _serverQuestion.getImage(),
                    IMAGE_PATH).execute(ApiUtils.BASE_SERVER_QUESTION_IMAGE_DIR + _serverQuestion.getImage());
        }

        for(League _serverLeague : serverLeagues) {
            new DownloadImageTask(
                    _serverLeague.getImage(),
                    IMAGE_PATH).execute(ApiUtils.BASE_SERVER_LEAGUE_IMAGE_DIR + _serverLeague.getImage());
        }

        for(Theme _serverTheme : serverThemes) {
            if(dbThemes.contains(_serverTheme)) {
                themeDao.update(_serverTheme);
            }
            else{
                dbThemes.add(_serverTheme);
                themeDao.insert(_serverTheme);
            }
        }

        for(Difficulty _serverDifficulty : serverDifficulties) {
            if(dbDifficulties.contains(_serverDifficulty)) {
                difficultyDao.update(_serverDifficulty);
            }
            else{
                dbDifficulties.add(_serverDifficulty);
                difficultyDao.insert(_serverDifficulty);
            }
        }

        for(League _serverLeague : serverLeagues) {
            if(dbLeagues.contains(_serverLeague)) {
                leagueDao.update(_serverLeague);
            }
            else{
                dbLeagues.add(_serverLeague);
                leagueDao.insert(_serverLeague);
            }
        }

        for(Question _serverQuestion : serverQuestions) {
            if(dbQuestions.contains(_serverQuestion)) {
                questionDao.update(_serverQuestion);
            }
            else{
                dbQuestions.add(_serverQuestion);
                questionDao.insert(_serverQuestion);
            }
        }

        for(Answer _serverAnswer : serverAnswers) {
            if(dbAnswers.contains(_serverAnswer)) {
                answerDao.update(_serverAnswer);
            }
            else{
                dbAnswers.add(_serverAnswer);
                answerDao.insert(_serverAnswer);
            }
        }
    }

    private static void deleteData(
            ThemeDao themeDao,
            DifficultyDao difficultyDao,
            LeagueDao leagueDao,
            QuestionDao questionDao,
            AnswerDao answerDao,

            List<Theme> serverThemes,
            List<Difficulty> serverDifficulties,
            List<League> serverLeagues,
            List<Question> serverQuestions,
            List<Answer> serverAnswers,

            List<Theme> dbThemes,
            List<Difficulty> dbDifficulties,
            List<League> dbLeagues,
            List<Question> dbQuestions,
            List<Answer> dbAnswers){

        // Removing same themes from temp collection
        if(serverThemes.size() < dbThemes.size()) {
            for(int i = 0; i < serverThemes.size(); i++) {
                dbThemes.remove(serverThemes.get(i));
            }
            // Deleting themes from DB
            themeDao.deleteMany(dbThemes);
        }

        if(serverDifficulties.size() < dbDifficulties.size()) {
            for(int i = 0; i < serverDifficulties.size(); i++) {
                dbDifficulties.remove(serverDifficulties.get(i));
            }
            difficultyDao.deleteMany(dbDifficulties);
        }

        if(serverLeagues.size() < dbLeagues.size()) {
            for(int i = 0; i < serverLeagues.size(); i++) {
                dbLeagues.remove(serverLeagues.get(i));
            }
            leagueDao.deleteMany(dbLeagues);
        }

        if(serverQuestions.size() < dbQuestions.size()) {
            for(int i = 0; i < serverQuestions.size(); i++) {
                dbQuestions.remove(serverQuestions.get(i));
            }
            questionDao.deleteMany(dbQuestions);
        }

        if(serverAnswers.size() < dbAnswers.size()) {
            for(int i = 0; i < serverAnswers.size(); i++) {
                dbAnswers.remove(serverAnswers.get(i));
            }
            answerDao.deleteMany(dbAnswers);
        }
    }
}
