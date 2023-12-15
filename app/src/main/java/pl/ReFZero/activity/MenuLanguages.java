package pl.ReFZero.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;
import java.util.List;

import pl.ReFZero.R;

public class MenuLanguages extends AppCompatActivity {

    @Override
    @SuppressLint("WrongViewCast")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_languages);

        List<ImageView> languageButtons = getLanguageButtons();
        assignButtonListeners(languageButtons);
    }


    private List<ImageView> getLanguageButtons() {
        List<ImageView> languageButtons = new ArrayList<>();
        languageButtons.add(findViewById(R.id.ib_polish_english));
        languageButtons.add(findViewById(R.id.ib_english_polish));
        languageButtons.add(findViewById(R.id.ib_polish_german));
        languageButtons.add(findViewById(R.id.ib_german_polish));
        languageButtons.add(findViewById(R.id.ib_polish_spanish));
        languageButtons.add(findViewById(R.id.ib_spanish_polish));
        languageButtons.add(findViewById(R.id.ib_polish_norwegian));
        languageButtons.add(findViewById(R.id.ib_norwegian_polish));
        return languageButtons;
    }

    private void assignButtonListeners(List<ImageView> languageButtons) {
        languageButtons.forEach(languageButton -> languageButton.setOnClickListener(
                view -> navigateToActivity(
                        new Intent(getApplicationContext(), MenuSet.class),
                        languageButton.getId())));
    }

    public void navigateToActivity(Intent intent, int buttonId) {
        addInformationAboutLanguageType(intent, buttonId);
        startActivity(intent);
    }

    private void addInformationAboutLanguageType(Intent intent, int buttonId) {
        String languageType = getLanguageTypeFromButtonId(buttonId);
        intent.putExtra("languageType", languageType);
    }

    @SuppressLint("NonConstantResourceId")
    private String getLanguageTypeFromButtonId(int buttonId) {
        switch (buttonId) {
            case R.id.ib_polish_english:
                return "polish_english";
            case R.id.ib_english_polish:
                return "english_polish";
            case R.id.ib_polish_german:
                return "polish_german";
            case R.id.ib_german_polish:
                return "german_polish";
            case R.id.ib_polish_spanish:
                return "polish_spanish";
            case R.id.ib_spanish_polish:
                return "spanish_polish";
            case R.id.ib_polish_norwegian:
                return "polish_norwegian";
            case R.id.ib_norwegian_polish:
                return "norwegian_polish";
            default:
                return "";
        }
    }
}
