package pl.ReFZero.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import pl.ReFZero.R;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

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

    public void navigateToActivity(Class<?> activity) {
        startActivity(new Intent(this, activity));
    }
}