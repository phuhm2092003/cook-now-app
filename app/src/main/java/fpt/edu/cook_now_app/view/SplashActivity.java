package fpt.edu.cook_now_app.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import fpt.edu.cook_now_app.MainActivity;
import fpt.edu.cook_now_app.R;

public class SplashActivity extends AppCompatActivity {
    public static final int TIME_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() ->
            launchMainActivity()
        , TIME_DELAY);
    }

    private void launchMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }
}