
package com.faa.knowyourgame_new;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.faa.knowyourgame_new.dao.AnswerDao;
import com.faa.knowyourgame_new.dao.DifficultyDao;
import com.faa.knowyourgame_new.dao.LeagueDao;
import com.faa.knowyourgame_new.dao.LogsDao;
import com.faa.knowyourgame_new.dao.QuestionDao;
import com.faa.knowyourgame_new.dao.ThemeDao;
import com.faa.knowyourgame_new.dao.UserDao;
import com.faa.knowyourgame_new.db.AppDatabase;
import com.faa.knowyourgame_new.dto.DbDto;
import com.faa.knowyourgame_new.entity.Logs;
import com.faa.knowyourgame_new.retrofit.utils.AuthUtils;
import com.faa.knowyourgame_new.retrofit.utils.DbUtils;
import com.faa.knowyourgame_new.ui.login_dialog.LoginDialogFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import java.io.File;

public class MainActivity extends AppCompatActivity {


    private DialogFragment loginDialogFragment = new LoginDialogFragment();
    private static final String TAG = "MainActivity";
    private static final String PREFS_FILE = "Config";

    public static AppDatabase db;

    public static ThemeDao themeDao;
    public static DifficultyDao difficultyDao;
    public static QuestionDao questionDao;
    public static AnswerDao answerDao;
    public static LeagueDao leagueDao;
    public static UserDao userDao;
    public static LogsDao logsDao;

    public static DbDto dbDto;
    public static SharedPreferences configuration;
    public static SharedPreferences.Editor configEditor;

    public static File IMAGE_PATH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IMAGE_PATH = this.getFilesDir();

        configuration = getSharedPreferences(PREFS_FILE, MODE_PRIVATE);
        configEditor = configuration.edit();

        int firstLaunch = configuration.getInt("FIRST_LAUNCH", 0);

        // Initializing database
        db =  Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "main_db")
                .allowMainThreadQueries()
                .build();
        initDao(db);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications).build();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().
                findFragmentById(R.id.nav_host_fragment);

        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        NavigationUI.setupWithNavController(navView, navController);

        Log.d(TAG, IMAGE_PATH.getPath());

        loginDialogFragment.show(getSupportFragmentManager(), "LOGIN_DIALOG");

        if(hasConnection(getApplicationContext())) {

            dbDto = new DbDto();
            DbUtils.getData((getDataResponse -> {

                dbDto.setThemes(getDataResponse.getThemes());
                dbDto.setDifficulties(getDataResponse.getDifficulties());
                dbDto.setQuestions(getDataResponse.getQuestions());
                dbDto.setAnswers(getDataResponse.getAnswers());
                dbDto.setLeagues(getDataResponse.getLeagues());

                if(configuration.getInt("FIRST_LAUNCH", 0) == 0) {
                    DbUtils.firstLaunch(
                            dbDto,
                            themeDao,
                            difficultyDao,
                            leagueDao,
                            questionDao,
                            answerDao);
                }
                else { DbUtils.checkForUpdates(
                        dbDto,
                        themeDao,
                        difficultyDao,
                        leagueDao,
                        questionDao,
                        answerDao);
                }
            }));

            firstLaunch += 1;

            configEditor.remove("FIRST_LAUNCH");
            configEditor.apply();

            configEditor.putInt("FIRST_LAUNCH", firstLaunch);
            configEditor.apply();
        }
    }


    @Override
    public void onBackPressed() {
        AuthUtils.logoutUser((logoutResponse ->
                Log.d(TAG, logoutResponse)));

        if(hasConnection(getApplicationContext())) {
            AuthUtils.updateUserData(userDao);
        }

        finishAffinity();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "ON DESTROY");

        if(hasConnection(getApplicationContext())) {
            AuthUtils.updateUserData(userDao);

            for(Logs elem: logsDao.getLogs())
            DbUtils.sendLog(elem);
        }

        db.close();
        AuthUtils.logoutUser((logoutResponse ->
                Log.d(TAG, logoutResponse)));

    }

    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void initDao(AppDatabase _db){
        themeDao = _db.themeDao();
        difficultyDao = _db.difficultyDao();
        questionDao = _db.questionDao();
        answerDao = _db.answerDao();
        leagueDao = _db.leagueDao();
        userDao = _db.userDao();
        logsDao = _db.logsDao();
    }
}