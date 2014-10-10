package main.bible_quest;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddPlayer extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_player);

		setScreenLanguage();

		Button addPlayerButton = (Button) findViewById(R.id.add_player_button);
		addPlayerButton.setOnClickListener(addPlayerListener);
	}

	// listeners
	View.OnClickListener addPlayerListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			EditText nameEditText = (EditText) findViewById(R.id.new_player_text);

			if (nameEditText.getText().length() == 0) {
				final String translationString = Utils.getInstance(getApplicationContext()).getTranslationForLanguageKey("warning_empty_player");
				Toast.makeText(AddPlayer.this, translationString,
						Toast.LENGTH_SHORT).show();
			} else {
				Database db = new Database(getApplicationContext());

				// save the results in the database
				SQLiteDatabase write_db = db.getWritableDatabase();
				String query = "INSERT INTO player(name, date) VALUES(?, ?)";
				String[] parms = { nameEditText.getText().toString(),
						new Date().toString() };
				write_db.execSQL(query, parms);

				Intent go_back = new Intent(AddPlayer.this, InputName.class);
				startActivity(go_back);
			}
		}
	};

	// private methods
	private void setScreenLanguage() {
		String translation;

		Utils utils = Utils.getInstance(getApplicationContext());

		translation = utils.getTranslationForLanguageKey("add_player_button");
		Button button = (Button) findViewById(R.id.add_player_button);
		button.setText(translation);

		translation = utils.getTranslationForLanguageKey("add_new_player");
		TextView tv = (TextView) findViewById(R.id.add_new_player);
		tv.setText(translation);
	}
}
