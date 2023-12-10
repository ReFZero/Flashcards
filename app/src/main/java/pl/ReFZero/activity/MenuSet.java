package pl.ReFZero.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import pl.ReFZero.R;

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
        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        List<String> buttonLabels = listFileNames.stream()
                .map(MenuSet::extractRange)
                .collect(Collectors.toList());
        // Tworzenie adaptera i ustawianie na RecyclerView
        ButtonAdapter buttonAdapter = new ButtonAdapter(
                firstLanguage,
                secondLanguage,
                buttonLabels
        );
        recyclerView.setAdapter(buttonAdapter);
    }

    private List<String> createListContainsFileNames(String language) {
        List<String> fileNames;
        try {
            fileNames = Arrays.asList(getAssets().list(language + "/"));
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
            String start = removeLeadingZeros(matcher.group(1));
            String end = removeLeadingZeros(matcher.group(2));

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
        private String firstLanguage;
        private String secondLanguage;
        private final List<String> buttonList;

        public ButtonAdapter(String firstLanguage, String secondLanguage, List<String> buttonList) {
            this.firstLanguage = firstLanguage;
            this.secondLanguage = secondLanguage;
            this.buttonList = buttonList;
        }

        @Override
        public ButtonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Button button = new Button(parent.getContext());
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

            ButtonViewHolder(Button button) {
                super(button);
                this.button = button;
                this.button.setOnClickListener(view -> {
//                    Toast.makeText(MenuSet.this, "Kliknięto " + button.getText(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Flashcards.class);
                    addInfoFileNameToUse(intent, button);
                    addInfoAboutLanguages(intent,
                            ButtonAdapter.this.getFirstLanguage(),
                            ButtonAdapter.this.getSecondLanguage());
                    startActivity(intent);
                });
            }
        }
    }

    private void addInfoFileNameToUse(Intent intent, Button b) {
        String fileName = buttonTextToFileNameConverter(b);
        intent.putExtra("fileNameToUse", fileName);
    }

    private void addInfoAboutLanguages(Intent intent, String firstLanguage, String secondLanguage) {
        intent.putExtra("firstLanguage", firstLanguage);
        intent.putExtra("secondLanguage", secondLanguage);
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


}
