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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.ReFZero.R;
import pl.ReFZero.decorator.MarginItemDecoration;
import pl.ReFZero.model.DataCollector;
import pl.ReFZero.utility.ListCreator;

public class MenuSet extends AppCompatActivity {

    private DataCollector dataCollector = new DataCollector();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_set);
        dataCollector = getIntent().getParcelableExtra("dataCollector");
        // Depending on the language received, an appropriate list is created.
        List<String> buttonLabels = ListCreator.createButtonLabel(getAssets(), dataCollector);

        // RecycleView - creating and setting appropriate parameters.
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // Set the number of columns in the grid
        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        // Create adapter and set to RecyclerView.
        // Additionally, it adds information about the languages that will be used to the buttons.
        ButtonAdapter buttonAdapter = new ButtonAdapter(buttonLabels);

        // Using a decorator. creates margins between buttons.
        MarginItemDecoration itemDecoration = new MarginItemDecoration(16);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(buttonAdapter);
    }

    // Adapter for RecyclerView
    private class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.ButtonViewHolder> {

        private final List<String> buttonList;
        //Set button font
        private static final int BUTTON_FONT = R.font.font_button_menu_set;
        private static final float BUTTON_FONT_SIZE = 17;

        public ButtonAdapter(List<String> buttonList) {

            this.buttonList = buttonList;
        }

        @NonNull
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
                dataCollector.setVocabularyRange(formattedStart + "_" + formattedEnd);
                // Create the final string
                return formattedStart + "_" + formattedEnd;
            }

            // Adds information about which file to use in the next activity
            private void addInfoFileNameToUse(Intent intent, Button b) {
                dataCollector.setVocabularyRange(buttonTextToFileNameConverter(b));
                intent.putExtra("dataCollector", dataCollector);
            }
        }
    }
}
