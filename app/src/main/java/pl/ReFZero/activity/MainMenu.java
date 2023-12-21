/*
About App:
The application allows to learn words from various languages.
Converts paper flashcards to electronic versions.
Uses JSON files as a vocabulary database.
Java version used - 11

Main menu class. is the entry point to the application.
Application flow:
MainMenu -> MenuLanguages -> MenuSet -> Flashcards
In:
 MenuLanguages - allows you to select the language
 MenuSet - allows you to select a collection of flashcards
 Flashcards - allows you to learn words from a selected language and set
 ReFZero 2023
*/

package pl.ReFZero.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import pl.ReFZero.R;
import pl.ReFZero.exceptions.ButtonNotSupportedException;

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
            default: throw new ButtonNotSupportedException("Button not supported. Check id button.");
        }
    }

    public void navigateToActivity(Class<?> activity) {
        startActivity(new Intent(this, activity));
    }
}