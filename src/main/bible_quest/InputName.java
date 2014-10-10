package main.bible_quest;

import android.app.Activity;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class InputName extends Activity {

	Spinner spinnerQuestions = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.input_name);

		final Spinner spinnerPlayers = (Spinner) findViewById(R.id.player_spinner);
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.questions_array,
				android.R.layout.simple_spinner_item);

		ArrayAdapter<CharSequence> playerAdapter;

		playerAdapter = new ArrayAdapter<CharSequence>(this,
				android.R.layout.simple_spinner_item);
		playerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerPlayers.setAdapter(playerAdapter);

		setScreenLanguage();

		Context context = getApplicationContext();
		Database db = new Database(context);
		final SQLiteDatabase read_db = db.getReadableDatabase();
		String[] columns = { "id", "name" };
		Cursor cursor = read_db.query("player", columns, null, null, null,
				null, "id DESC");

		while (cursor.moveToNext()) {
			String playerName = cursor.getString(1);
			playerAdapter.add(playerName);
		}
		
		//close db
		cursor.close();
		db.close();
		
		spinnerQuestions = (Spinner) findViewById(R.id.spinner);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerQuestions.setAdapter(adapter);
		
		Utils utils = Utils.getInstance(getApplicationContext());
		String prompt = utils.getTranslationForLanguageKey("question_prompt");
		spinnerQuestions.setPrompt(prompt);

		prompt = utils.getTranslationForLanguageKey("player_prompt");
		spinnerPlayers.setPrompt(prompt);
		
		Button button_start_game = (Button) findViewById(R.id.start_game);
		Button button_add_player = (Button) findViewById(R.id.add_player);
		Button button_show_results = (Button) findViewById(R.id.show_results);
		Button button_settings = (Button) findViewById(R.id.settings);
		
		button_add_player.setOnClickListener(add_player);
		button_start_game.setOnClickListener(start_game);		
		button_show_results.setOnClickListener(show_results);
		button_settings.setOnClickListener(show_settings);
	}
	
	//listeners
	private OnClickListener show_settings = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent settings = new Intent(InputName.this, MySettings.class);
			Bundle bundle = new Bundle();
			bundle.putString("language", Utils.language);
			settings.putExtras(bundle);
			startActivity(settings);			
		}
	};
	
	private OnClickListener show_results = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(InputName.this, Results.class);
			Bundle bundle = new Bundle();
			bundle.putInt("just_show_results", 1);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	};
    private OnClickListener add_player = new OnClickListener() {
        public void onClick(View v) {
			Intent add_player = new Intent(InputName.this, AddPlayer.class);
			startActivity(add_player);
        }
    };
    
    private OnClickListener start_game = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Spinner inputName = (Spinner) findViewById(R.id.player_spinner);
			if (inputName.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
	
				AlertDialog.Builder alertbox = new AlertDialog.Builder(InputName.this);
				Utils utils = Utils.getInstance(getApplicationContext());
				String translation = utils.getTranslationForLanguageKey("message_add_player");
				
				alertbox.setMessage(translation);
				alertbox.setNeutralButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0,
									int arg1) {
								arg0.cancel();
							}
						});
				alertbox.show();
			} else {
				String player_name = inputName.getSelectedItem()
						.toString();
				// set the data sent to the next activity
				Bundle bundle = new Bundle();
				spinnerQuestions = (Spinner) findViewById(R.id.spinner);
				bundle.putInt("questions", Integer.valueOf(spinnerQuestions
						.getSelectedItem().toString()));
				bundle.putInt("questions_put", 1);
				bundle.putInt("points", 0);
				bundle.putString("player_name", player_name);
				bundle.putString("language", Utils.language);

				Intent intent = new Intent(InputName.this, PlayGame.class);
				intent.putExtras(bundle);

				startActivity(intent);
			}			
		}
	};
    
	//private methods
	private void setScreenLanguage() {

		String translation_string;
		TextView textView;
		Utils utils = Utils.getInstance(getApplicationContext());

		translation_string = utils.getTranslationForLanguageKey("select_or_add_player");
		textView = (TextView) findViewById(R.id.select_or_add_player);
		textView.setText(translation_string);

		translation_string = utils.getTranslationForLanguageKey("add_player");
		textView = (TextView) findViewById(R.id.add_player);
		textView.setText(translation_string);

		translation_string = utils.getTranslationForLanguageKey("number_of_questions");
		textView = (TextView) findViewById(R.id.number_of_questions);
		textView.setText(translation_string);

		translation_string = utils.getTranslationForLanguageKey("start_new_game");
		textView = (TextView) findViewById(R.id.start_game);
		textView.setText(translation_string);

		translation_string = utils.getTranslationForLanguageKey("show_results");
		textView = (TextView) findViewById(R.id.show_results);
		textView.setText(translation_string);

		translation_string = utils.getTranslationForLanguageKey("settings");
		textView = (TextView) findViewById(R.id.settings);
		textView.setText(translation_string);
	}

}