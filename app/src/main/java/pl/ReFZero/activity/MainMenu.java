package pl.ReFZero.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import pl.ReFZero.R;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Button bStart = findViewById(R.id.b_start);
        Button bAbout = findViewById(R.id.b_about);
        Button bExit = findViewById(R.id.b_exit);

        bStart.setOnClickListener(this);
        bAbout.setOnClickListener(this);
        bExit.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_start:
                navigateToActivity(MenuLanguages.class);
                break;
            case R.id.b_about:
                navigateToActivity(About.class);
                break;
            case R.id.b_exit:
                finish();
                System.exit(0);
                break;
        }
    }

    public void navigateToActivity(Class<?> activity){
        startActivity(new Intent(this, activity));
    }
}