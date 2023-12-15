package pl.ReFZero.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import pl.ReFZero.R;
import pl.ReFZero.decorator.MarginItemDecoration;

public class MenuSet extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_set);


        Bundle extras = getIntent().getExtras();
        String languageType = extras.getString("languageType");
        String[] parts = languageType.split("_");

        // Przypisz części do zmiennych
        String firstLanguage = parts[0];
        String secondLanguage = parts[1];
        List<String> listFileNames = new ArrayList<>();


        if (Arrays.stream(parts).anyMatch(s -> s.contains("english"))) {
            listFileNames = createListContainsFileNames("english");
        } else if (Arrays.stream(parts).anyMatch(s -> s.contains("german"))) {
            listFileNames = createListContainsFileNames("german");
        } else if (Arrays.stream(parts).anyMatch(s -> s.contains("spanish"))) {
            listFileNames = createListContainsFileNames("spanish");
        } else if (Arrays.stream(parts).anyMatch(s -> s.contains("norwegian"))) {
            listFileNames = createListContainsFileNames("norwegian");
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // Ustawiamy ilość kolumn w siatce
        int NUMBER_OF_COLUMNS = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, NUMBER_OF_COLUMNS));

        List<String> buttonLabels = listFileNames.stream()
                .map(MenuSet::extractRange)
                .collect(Collectors.toList());
        // Tworzenie adaptera i ustawianie na RecyclerView
        ButtonAdapter buttonAdapter = new ButtonAdapter(
                firstLanguage,
                secondLanguage,
                buttonLabels
        );


        MarginItemDecoration itemDecoration = new MarginItemDecoration(16);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(buttonAdapter);
    }

    private List<String> createListContainsFileNames(String language) {
        List<String> fileNames;
        String delimiter = "/";
        try {
            fileNames = Arrays.asList(getAssets().list(language + delimiter));
        } catch (IOException e) {
            throw new RuntimeException(e); // Dodac obsluge wyjatku
        }
        return fileNames;
    }

    private static String extractRange(String input) {

        String regex = ".*_(\\d+)_(\\d+)\\.json";

        // Kompilacja wyrażenia regularnego
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        // Sprawdzenie dopasowań
        if (matcher.matches()) {
            // Pobranie dopasowanych grup
            String start = removeLeadingZeros(Objects.requireNonNull(matcher.group(1)));
            String end = removeLeadingZeros(Objects.requireNonNull(matcher.group(2)));

            // Utworzenie ciągu wynikowego "start-end"
            return start + "-" + end;
        } else {
            // Jeżeli brak dopasowania, zwróć pusty ciąg lub inny komunikat błędu
            return "Error: Failed to match.";
        }
    }

    private static String removeLeadingZeros(String input) {
        // Usunięcie zer wiodących
        return input.replaceFirst("^0+", "");
    }


    // Adapter dla RecyclerView
    private class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.ButtonViewHolder> {
        private final String firstLanguage;
        private final String secondLanguage;
        private final List<String> buttonList;
        //Set button font
        private final int BUTTON_FONT = R.font.font_button_menu_set;
        private final float BUTTON_FONT_SIZE = 17;

        public ButtonAdapter(String firstLanguage, String secondLanguage, List<String> buttonList) {
            this.firstLanguage = firstLanguage;
            this.secondLanguage = secondLanguage;
            this.buttonList = buttonList;
        }

        @Override
        public ButtonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Button button = new Button(parent.getContext());
            button.setBackgroundResource(R.drawable.custom_button_menu_set);
            button.setTextColor(Color.parseColor("#FFFFFF"));
            button.setTypeface(ResourcesCompat.getFont(getApplicationContext(), BUTTON_FONT));

            button.setTextSize(BUTTON_FONT_SIZE);
            return new ButtonViewHolder(button);
        }

        @Override
        public void onBindViewHolder(ButtonViewHolder holder, int position) {
            holder.button.setText(buttonList.get(position));
        }

        @Override
        public int getItemCount() {
            return buttonList.size();
        }

        public String getFirstLanguage() {
            return firstLanguage;
        }

        public String getSecondLanguage() {
            return secondLanguage;
        }



        // Klasa ViewHolder dla przycisków
        class ButtonViewHolder extends RecyclerView.ViewHolder {

            Button button;

            @SuppressLint("ResourceType")
            ButtonViewHolder(Button button) {
                super(button);
                this.button = button;
                this.button.setOnClickListener(view -> {
                    Intent intent = new Intent(getApplicationContext(), Flashcards.class);

                    addInfoFileNameToUse(intent, button);
                    addInfoAboutLanguages(intent,
                            ButtonAdapter.this.getFirstLanguage(),
                            ButtonAdapter.this.getSecondLanguage());
                    startActivity(intent);
                });
            }

            @SuppressLint("DefaultLocale")
            private String buttonTextToFileNameConverter(Button b) {
                String text = b.getText().toString();

                // Podziel tekst na zakres
                String[] range = text.split("-");
                int start = Integer.parseInt(range[0]);
                int end = Integer.parseInt(range[1]);

                // Sformatuj liczby do oczekiwanej postaci
                String formattedStart = String.format("%04d", start);
                String formattedEnd = String.format("%04d", end);

                // Utwórz ostateczny łańcuch znaków
                return formattedStart + "_" + formattedEnd;
            }

            private void addInfoFileNameToUse(Intent intent, Button b) {
                String fileName = buttonTextToFileNameConverter(b);
                intent.putExtra("fileNameToUse", fileName);
            }

            private void addInfoAboutLanguages(Intent intent, String firstLanguage, String secondLanguage) {
                intent.putExtra("firstLanguage", firstLanguage);
                intent.putExtra("secondLanguage", secondLanguage);
            }
        }
    }
}
