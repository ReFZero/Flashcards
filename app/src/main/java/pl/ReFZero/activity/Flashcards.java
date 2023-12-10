package pl.ReFZero.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.ReFZero.R;
import pl.ReFZero.model.EnglishWord;
import pl.ReFZero.model.GermanWord;
import pl.ReFZero.model.NorwegianWord;
import pl.ReFZero.model.SpanishWord;

@SuppressLint("MissingInflatedId")
public class Flashcards extends AppCompatActivity {

    private List<?> wordList = new ArrayList<>(50);
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards);

        Button bNext = findViewById(R.id.b_next);
        Button bReverse = findViewById(R.id.b_reverse);

        TextView message = findViewById(R.id.word_content);
        TextView wordNumber = findViewById(R.id.word_number);

        Bundle extras = getIntent().getExtras();
        String firstLanguage = extras.getString("firstLanguage");
        String secondLanguage = extras.getString("secondLanguage");
        String numberFileName = extras.getString("fileNameToUse");

        String providedLanguage = provideLanguage(firstLanguage, secondLanguage);

        if (numberFileName != null) {
            switch (providedLanguage) {
                case "english": {
                    createListWords(EnglishWord[].class,numberFileName,providedLanguage);
                    break;
                }
                case "german": {
                    createListWords(GermanWord[].class,numberFileName,providedLanguage);
                    break;
                }
                case "spanish": {
                    createListWords(SpanishWord[].class,numberFileName,providedLanguage);
                    break;
                }
                case "norwegian": {
                    createListWords(NorwegianWord[].class,numberFileName,providedLanguage);
                    break;
                }
                default:
                    // Throw Exception
                    break;
            }
        }

        Log.i("word 0", (wordList.get(49).toString()));
    }

    private <T> void createListWords(Class<T[]> languageClass, String numberFileName, String providedLanguage) {

        String fullFileName = createFullFileName(providedLanguage, numberFileName);
        if (languageClass == null) {
            Log.e("App", "Error: languageClass array is null or empty!");
            return;
        }

        try {
            InputStream inputStream = getAssets().open(fullFileName);
            wordList = Arrays.asList(mapper.readValue(inputStream, languageClass));
            inputStream.close();
        } catch (IOException ioException) {
            Log.e("App", "Error: File not found! Check 'extras' file name.");
        }
    }

    private String createFullFileName(String fromLanguage, String numberFileName) {
        return fromLanguage + "/" + fromLanguage + "_" + numberFileName + ".json";
    }

    private String provideLanguage(String first, String second) {
        if (first.equals("english") || second.equals("english")) {
            return "english";
        } else if (first.equals("german") || second.equals("german")) {
            return "german";
        } else if (first.equals("spanish") || second.equals("spanish")) {
            return "spanish";
        } else if (first.equals("norwegian") || second.equals("norwegian")) {
            return "norwegian";
        }
        return "Error";
    }
}
