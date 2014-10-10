package main.bible_quest;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MySettings extends Activity {

	private void setScreenLanguage() {
		TextView tv;
		String translation;
		
		Utils utils = Utils.getInstance(getApplicationContext());
		
		translation = utils.getTranslationForLanguageKey("select_game_language");
		tv = (TextView) findViewById(R.id.select_game_language);
		tv.setText(translation);

		translation = utils.getTranslationForLanguageKey("save_settings");
		tv = (TextView) findViewById(R.id.save_settings);
		tv.setText(translation);		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		setScreenLanguage();
		
		final String[] languages = { "Romanian", "English" };
		final String[] languages_code = { "ro", "en" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, languages);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		final Spinner spinnerLanguage = (Spinner) findViewById(R.id.language_spinner);
		spinnerLanguage.setAdapter(adapter);

		Utils utils = Utils.getInstance(getApplicationContext());
		String prompt = utils.getTranslationForLanguageKey("language_prompt");
		spinnerLanguage.setPrompt(prompt);
		
		Bundle bundle = getIntent().getExtras();
		String game_language = bundle.getString("language");
		int pos;
		if (game_language.equals("ro"))
			pos = 0;
		else
			pos = 1;

		spinnerLanguage.setSelection(pos);
		Button save_settings = (Button) findViewById(R.id.save_settings);
		save_settings.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// save the language in the settings table
				try {
					Database db = new Database(getApplicationContext());
					int pos = spinnerLanguage.getSelectedItemPosition();

					String selected_language = languages_code[pos];
					Utils.language = selected_language;
					SQLiteDatabase write_db = db.getWritableDatabase();
					String query = "UPDATE settings SET language = ?";
					String[] parms = { selected_language };
					write_db.execSQL(query, parms);

					//close db
					write_db.close();
					db.close();
					
					Intent goBack = new Intent(MySettings.this, InputName.class);
					
					startActivity(goBack);
				} catch (Exception e) {
					Log.println(1, "error", e.getMessage());
				}
			}
		});
	}
}