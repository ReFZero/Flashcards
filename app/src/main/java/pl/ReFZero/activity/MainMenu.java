package pl.ReFZero.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import pl.ReFZero.R;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button bStart = findViewById(R.id.bStart);
        Button bAbout = findViewById(R.id.bAbout);
        Button bExit = findViewById(R.id.bExit);

        bStart.setOnClickListener(this);
        bAbout.setOnClickListener(this);
        bExit.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bStart:
                navigateToActivity(MenuLanguages.class);
                break;
            case R.id.bAbout:
                navigateToActivity(About.class);
                break;
            case R.id.bExit:
                finish();
                System.exit(0);
                break;
        }
    }

    public void navigateToActivity(Class<?> activity){
        startActivity(new Intent(this, activity));
    }
}