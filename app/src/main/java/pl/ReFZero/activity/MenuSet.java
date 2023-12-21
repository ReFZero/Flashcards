/*
The class allows you to select a set of flashcards in the given languages.
Buttons are created dynamically depending on how many files are in a specific folder assigned to a specific language.
The class extracts information from the file name and converts it to button labels, e.g.
(file) "english_0001_0050" -> (button label) "1-50"
*/
package pl.ReFZero.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import pl.ReFZero.R;
import pl.ReFZero.decorator.MarginItemDecoration;
import pl.ReFZero.exceptions.CreateListContainsFileNamesException;
import pl.ReFZero.exceptions.FailedToMatchException;
import pl.ReFZero.exceptions.LanguageNotSupportedException;

public class MenuSet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_set);

        // Divide the received "languageType" into appropriate parts.
        // Used to determine what languages will be used and which will be the base language.
        Bundle extras = getIntent().getExtras();
        String languageType = extras.getString("languageType");
        String[] parts = languageType.split("_");
        String firstLanguage = parts[0];
        String secondLanguage = parts[1];

        // Depending on the language received, an appropriate list is created.
        List<String> buttonLabels = createButtonLabel(parts);

        // RecycleView - creating and setting appropriate parameters.
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // Set the number of columns in the grid
        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        // Create adapter and set to RecyclerView.
        // Additionally, it adds information about the languages that will be used to the buttons.
        ButtonAdapter buttonAdapter = new ButtonAdapter(
                firstLanguage,
                secondLanguage,
                buttonLabels
        );

        // Using a decorator. creates margins between buttons.
        MarginItemDecoration itemDecoration = new MarginItemDecoration(16);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(buttonAdapter);
    }

    // Creates labels for buttons, e.g. 1-50, 51-100 ...
    private List<String> createButtonLabel(String[] parts) {
        List<String> listFileNames;
        if (Arrays.stream(parts).anyMatch(s -> s.contains("english"))) {
            listFileNames = createListContainsFileNames("english");
        } else if (Arrays.stream(parts).anyMatch(s -> s.contains("german"))) {
            listFileNames = createListContainsFileNames("german");
        } else if (Arrays.stream(parts).anyMatch(s -> s.contains("spanish"))) {
            listFileNames = createListContainsFileNames("spanish");
        } else if (Arrays.stream(parts).anyMatch(s -> s.contains("norwegian"))) {
            listFileNames = createListContainsFileNames("norwegian");
        } else throw new LanguageNotSupportedException("Language not supported.");
        return listFileNames.stream()
                .map(MenuSet::extractRange)
                .collect(Collectors.toList());
    }

    // Convert filename to button label
    private static String extractRange(String input) {

        String regex = ".*_(\\d+)_(\\d+)\\.json";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        // Checking matches
        if (matcher.matches()) {
            String start = removeLeadingZeros(Objects.requireNonNull(matcher.group(1)));
            String end = removeLeadingZeros(Objects.requireNonNull(matcher.group(2)));
            // Creating the "start-end" result string
            return start + "-" + end;
        } else {
            throw new FailedToMatchException("Failed to match.");
        }
    }

    private List<String> createListContainsFileNames(String language) {
        List<String> fileNames;
        String delimiter = "/";
        try {
            fileNames = Arrays.asList(getAssets().list(language + delimiter));
        } catch (IOException e) {
            throw new CreateListContainsFileNamesException("Failed to create filename list");
        }
        return fileNames;
    }


    // Removing leading zeros, e.g. from 0001_0050 to 1_50
    private static String removeLeadingZeros(String input) {
        return input.replaceFirst("^0+", "");
    }


    // Adapter for RecyclerView
    private class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.ButtonViewHolder> {
        private final String firstLanguage;
        private final String secondLanguage;
        private final List<String> buttonList;
        //Set button font
        private static final int BUTTON_FONT = R.font.font_button_menu_set;
        private static final float BUTTON_FONT_SIZE = 17;

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


        // ViewHolder class for buttons
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

            // Depending on which button was pressed, a different range of flashcards is assigned to it.
            @SuppressLint("DefaultLocale")
            private String buttonTextToFileNameConverter(Button b) {
                String text = b.getText().toString();

                // Split text into a range
                String[] range = text.split("-");
                int start = Integer.parseInt(range[0]);
                int end = Integer.parseInt(range[1]);

                // Format the numbers into the expected form
                String formattedStart = String.format("%04d", start);
                String formattedEnd = String.format("%04d", end);

                // Create the final string
                return formattedStart + "_" + formattedEnd;
            }

            // Adds information about which file to use in the next activity
            private void addInfoFileNameToUse(Intent intent, Button b) {
                String fileName = buttonTextToFileNameConverter(b);
                intent.putExtra("fileNameToUse", fileName);
            }

            // Adds information about the languages to be used in the next activity
            private void addInfoAboutLanguages(Intent intent, String firstLanguage, String secondLanguage) {
                intent.putExtra("firstLanguage", firstLanguage);
                intent.putExtra("secondLanguage", secondLanguage);
            }
        }
    }
}
