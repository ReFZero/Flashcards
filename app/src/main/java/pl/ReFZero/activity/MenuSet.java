package pl.ReFZero.activity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String first = parts[0];
        String second = parts[1];
        List<String> listFileNames = new ArrayList<>();

        if (Arrays.stream(parts).anyMatch(s -> s.contains("english"))) {
            listFileNames = createListContainsFileNames(listFileNames, "english");
        } else if (Arrays.stream(parts).anyMatch(s -> s.contains("german"))) {
            listFileNames = createListContainsFileNames(listFileNames, "german");
        } else if (Arrays.stream(parts).anyMatch(s -> s.contains("spanish"))) {
            listFileNames = createListContainsFileNames(listFileNames, "spanish");
        } else if (Arrays.stream(parts).anyMatch(s -> s.contains("norwegian"))) {
            listFileNames = createListContainsFileNames(listFileNames, "norwegian");
        }


        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // Przykładowa liczba przycisków
        int numberOfButtons = listFileNames.size();

        // Ustawiamy ilość kolumn w siatce
        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        // Tworzenie i dodawanie przycisków do listy
        List<String> buttonList = new ArrayList<>();
        for (int i = 0; i < numberOfButtons; i++) {
            buttonList.add(extractRange(listFileNames.get(i)));
        }

        // Tworzenie adaptera i ustawianie na RecyclerView
        ButtonAdapter buttonAdapter = new ButtonAdapter(buttonList);
        recyclerView.setAdapter(buttonAdapter);
    }

    private List<String> createListContainsFileNames(List<String> listFileNames, String language) {
        try {
            listFileNames = Arrays.asList(getAssets().list(language + "/"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return listFileNames;
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

        private List<String> buttonList;

        ButtonAdapter(List<String> buttonList) {
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

        // Klasa ViewHolder dla przycisków
        class ButtonViewHolder extends RecyclerView.ViewHolder {
            Button button;

            ButtonViewHolder(Button button) {
                super(button);
                this.button = button;
                this.button.setOnClickListener(view -> {
                    Toast.makeText(MenuSet.this, "Kliknięto " + button.getText(), Toast.LENGTH_SHORT).show();
                });
            }
        }
    }
}
