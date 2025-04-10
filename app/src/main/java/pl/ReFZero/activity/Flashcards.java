/*
Flashcards class.
By receiving information from the previous activity, it creates a list of flashcards
and starts with the appropriate first language. Allows to change and rotate flashcards.
*/
package pl.ReFZero.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import pl.ReFZero.R;
import pl.ReFZero.exceptions.LanguageNotSupportedException;
import pl.ReFZero.model.DataCollector;
import pl.ReFZero.model.english.EnglishFlashcard;
import pl.ReFZero.model.german.GermanFlashcard;
import pl.ReFZero.model.LanguageProvider;
import pl.ReFZero.model.norwegian.NorwegianFlashcard;
import pl.ReFZero.model.spanish.SpanishFlashcard;
import pl.ReFZero.utility.Validator;

@SuppressLint("MissingInflatedId")
public class Flashcards extends AppCompatActivity {

    private LanguageProvider languageProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards);

        Button b_next = findViewById(R.id.b_next);
        Button b_reverse = findViewById(R.id.b_reverse);

        TextView word_content = findViewById(R.id.word_content);
        TextView word_number = findViewById(R.id.word_number);

        DataCollector dataCollector = getIntent().getParcelableExtra("dataCollector");
        dataCollector.updateData();
        if (Validator.validateFullFilePath(dataCollector.getFullFilePath())) {
            switch (dataCollector.getLanguageType()) {
                case ENGLISH:
                    languageProvider = new EnglishFlashcard(getAssets(), dataCollector.getFullFilePath());
                    break;
                case GERMAN:
                    languageProvider = new GermanFlashcard(getAssets(), dataCollector.getFullFilePath());
                    break;
                case SPANISH:
                    languageProvider = new SpanishFlashcard(getAssets(), dataCollector.getFullFilePath());
                    break;
                case NORWEGIAN:
                    languageProvider = new NorwegianFlashcard(getAssets(), dataCollector.getFullFilePath());
                    break;
                default:
                    throw new LanguageNotSupportedException("Provided language is not supported! Check dataCollector object");
            }

            b_next.setOnClickListener(v -> languageProvider.createFlashcard(dataCollector.getFirstLanguage(), word_number, word_content));
            b_reverse.setOnClickListener(v -> languageProvider.reverseFlashcard(word_content));
            b_next.performClick();
        }
    }
}
