package pl.ReFZero.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import pl.ReFZero.R;
import pl.ReFZero.model.EnglishWord;
import pl.ReFZero.model.GermanWord;
import pl.ReFZero.model.NorwegianWord;
import pl.ReFZero.model.SpanishWord;

@SuppressLint("MissingInflatedId")
public class Flashcards extends AppCompatActivity {

    private Boolean isPolish;
    private ObjectMapper mapper = new ObjectMapper();

    private Random rand = new Random();
    private Integer random = rand.nextInt(50);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

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
            isPolish = firstLanguage.equals("polish");
            switch (providedLanguage) {
                case "english": {
                    ArrayList<EnglishWord> englishWords = createListWords(EnglishWord[].class, numberFileName, providedLanguage);
                    bNext.setOnClickListener(v -> {
                        Integer currentRandom = random;
                        while (currentRandom.equals(random)) {
                            random = rand.nextInt(50);
                        }
                        if (isPolish) {
                            message.setText(englishWords.get(random).getPolish());
                            wordNumber.setText(String.valueOf(englishWords.get(random).getId()));
                        } else {
                            message.setText(englishWords.get(random).getEnglish());
                            wordNumber.setText(String.valueOf(englishWords.get(random).getId()));
                        }
                    });
                    bReverse.setOnClickListener(v -> {
                        if (!isPolish) {
                            message.setText(englishWords.get(random).getPolish());
                            isPolish = true;
                        } else {
                            message.setText(englishWords.get(random).getEnglish());
                            isPolish = false;
                        }
                    });
                    break;
                }
                case "german": {
                    ArrayList<GermanWord> germanWords = createListWords(GermanWord[].class, numberFileName, providedLanguage);
                    bNext.setOnClickListener(v -> {
                        Integer currentRandom = random;
                        while (currentRandom.equals(random)) {
                            random = rand.nextInt(50);
                        }
                        if (isPolish) {
                            message.setText(germanWords.get(random).getPolish());
                            wordNumber.setText(String.valueOf(germanWords.get(random).getId()));
                        } else {
                            message.setText(germanWords.get(random).getGerman());
                            wordNumber.setText(String.valueOf(germanWords.get(random).getId()));
                        }
                    });
                    bReverse.setOnClickListener(v -> {
                        if (!isPolish) {
                            message.setText(germanWords.get(random).getPolish());
                            isPolish = true;
                        } else {
                            message.setText(germanWords.get(random).getGerman());
                            isPolish = false;
                        }
                    });
                    break;
                }
                case "spanish": {
                    ArrayList<SpanishWord> spanishWords = createListWords(SpanishWord[].class, numberFileName, providedLanguage);
                    bNext.setOnClickListener(v -> {
                        Integer currentRandom = random;
                        while (currentRandom.equals(random)) {
                            random = rand.nextInt(50);
                        }
                        if (isPolish) {
                            message.setText(spanishWords.get(random).getPolish());
                            wordNumber.setText(String.valueOf(spanishWords.get(random).getId()));
                        } else {
                            message.setText(spanishWords.get(random).getSpanish());
                            wordNumber.setText(String.valueOf(spanishWords.get(random).getId()));
                        }
                    });
                    bReverse.setOnClickListener(v -> {
                        if (!isPolish) {
                            message.setText(spanishWords.get(random).getPolish());
                            isPolish = true;
                        } else {
                            message.setText(spanishWords.get(random).getSpanish());
                            isPolish = false;
                        }
                    });
                    break;
                }
                case "norwegian": {
                    ArrayList<NorwegianWord> norwegianWords = createListWords(NorwegianWord[].class, numberFileName, providedLanguage);
                    bNext.setOnClickListener(v -> {
                        Integer currentRandom = random;
                        while (currentRandom.equals(random)) {
                            random = rand.nextInt(50);
                        }
                        if (isPolish) {
                            message.setText(norwegianWords.get(random).getPolish());
                            wordNumber.setText(String.valueOf(norwegianWords.get(random).getId()));
                        } else {
                            message.setText(norwegianWords.get(random).getNorwegian());
                            wordNumber.setText(String.valueOf(norwegianWords.get(random).getId()));
                        }
                    });
                    bReverse.setOnClickListener(v -> {
                        if (!isPolish) {
                            message.setText(norwegianWords.get(random).getPolish());
                            isPolish = true;
                        } else {
                            message.setText(norwegianWords.get(random).getNorwegian());
                            isPolish = false;
                        }
                    });
                    break;
                }
                default:
                    throw new RuntimeException("Error: Provided language is not supported!");
            }
            bNext.performClick();
        }

    }

    private <T> ArrayList<T> createListWords(Class<T[]> languageClass, String numberFileName, String providedLanguage) {
        List<T> wordList = new ArrayList<>(50);
        String fullFileName = createFullFileName(providedLanguage, numberFileName);
        if (languageClass == null) {
            Log.e("App", "Error: languageClass array is null or empty!");
        }

        try {
            InputStream inputStream = getAssets().open(fullFileName);
            wordList = Arrays.asList(mapper.readValue(inputStream, languageClass));
            inputStream.close();
        } catch (IOException ioException) {
            Log.e("App", "Error: File not found! Check 'extras' file name.");
        }
        return new ArrayList<>(wordList);
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
