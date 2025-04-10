/*
The class allows you to choose learning languages.
The base language is the one on the left and every flashcard will start with this language.
 */
package pl.ReFZero.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import pl.ReFZero.R;
import pl.ReFZero.exceptions.ButtonNotSupportedException;
import pl.ReFZero.model.DataCollector;

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

    // Assigns appropriate behavior to buttons
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
        DataCollector dataCollector = new DataCollector();
        getLanguageTypeFromButtonId(buttonId, dataCollector);
        intent.putExtra("dataCollector", dataCollector);
    }

    @SuppressLint("NonConstantResourceId")
    private void getLanguageTypeFromButtonId(int buttonId, DataCollector collector) {
        switch (buttonId) {
            case R.id.ib_polish_english:
                collector.setLanguages( "polish", "english");
                break;
            case R.id.ib_english_polish:
                collector.setLanguages( "english", "polish");
                break;
            case R.id.ib_polish_german:
                collector.setLanguages( "polish", "german");
                break;
            case R.id.ib_german_polish:
                collector.setLanguages( "german", "polish");
                break;
            case R.id.ib_polish_spanish:
                collector.setLanguages( "polish", "spanish");
                break;
            case R.id.ib_spanish_polish:
                collector.setLanguages( "spanish", "polish");
                break;
            case R.id.ib_polish_norwegian:
                collector.setLanguages( "polish", "norwegian");
                break;
            case R.id.ib_norwegian_polish:
                collector.setLanguages( "norwegian", "polish");
                break;
            default:
                throw new ButtonNotSupportedException("Button not supported. Check id button.");
        }
    }
}
