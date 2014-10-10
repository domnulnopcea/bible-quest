package main.bible_quest;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ResultRender extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.result_render);

			Intent intent = getIntent();
			Bundle bundle = intent.getExtras();
			int gameType = bundle.getInt("game_type");
			Context context = getApplicationContext();
			Database db = new Database(context);
			LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layout_results);

			if (bundle.getInt("just_show_results") == 1) {
				linearLayout = renderResults(linearLayout, db, gameType);
			} else {
				// save the results in the database
				String strPlayerName = bundle.getString("player_name");

				SQLiteDatabase readDb = db.getReadableDatabase();
				String[] columns = { "id", "name" };
				String[] parms = { strPlayerName };
				Cursor cursor = readDb.query("player", columns, "name=?",
						parms, null, null, "id DESC");
				String player_id = null;
				while (cursor.moveToNext())
					player_id = cursor.getString(0);
				cursor.close();
				SQLiteDatabase write_db = db.getWritableDatabase();
				Date now = new Date();

				Integer month = now.getMonth() + 1;
				String month_string = String.valueOf(month);
				if (month < 10)
					month_string = "0" + month;

				Integer day = now.getDate();
				String day_string = String.valueOf(day);
				if (day < 10)
					day_string = "0" + day;

				Integer hours = now.getHours();
				String hours_string = String.valueOf(hours);
				if (hours < 10)
					hours_string = "0" + hours;

				Integer minutes = now.getMinutes();
				String minutes_string = String.valueOf(minutes);
				if (minutes < 10)
					minutes_string = "0" + minutes;

				Integer seconds = now.getSeconds();
				String seconds_string = String.valueOf(seconds);
				if (seconds < 10)
					seconds_string = "0" + seconds;

				String now_string = (now.getYear() + 1900) + "-" + month_string
						+ "-" + day_string + " " + hours_string + ":"
						+ minutes_string + ":" + seconds_string;

				String query = "INSERT INTO result(player_id, game_type, game_language, points, date) VALUES(?, ?, ?, ?, ?)";
				String[] parms2 = { player_id,
						String.valueOf(bundle.getInt("questions")),
						Utils.language,
						String.valueOf(bundle.getInt("points")), now_string };
				write_db.execSQL(query, parms2);

				// show the results
				renderResults(linearLayout, db, gameType);
			}

			Utils utils = Utils.getInstance(getApplicationContext());
			String translationString;

			Button buttonGoBack = (Button) findViewById(R.id.go_back);
			translationString = utils.getTranslationForLanguageKey("go_back");
			buttonGoBack.setText(translationString);

			buttonGoBack.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent go_back = new Intent(ResultRender.this,
							InputName.class);
					startActivity(go_back);
				}
			});
		} catch (Exception e) {
			Log.println(1, "error", e.getMessage());
		}
	}

	@Override
	public void onBackPressed() {

		return;
	}

	public LinearLayout renderResults(LinearLayout linearLayout, Database db,
			int gameType) {

		Utils utils = Utils.getInstance(getApplicationContext());
		String translation;

		SQLiteDatabase readDb = db.getReadableDatabase();

		String[] columns = { "player_id", "points", "date", "id" };
		String where = "game_type = " + String.valueOf(gameType);
		where += " AND game_language = '" + Utils.language + "'";

		Cursor cursor = readDb.query("result", columns, where, null, null,
				null, "date DESC");
		TableLayout tableLayout = (TableLayout) findViewById(R.id.layout_results_table);
		tableLayout.setGravity(Gravity.TOP);
		tableLayout.setStretchAllColumns(true);

		TableRow tableRow = new TableRow(this);

		TextView tvColumnPlayerName = new TextView(this);
		translation = utils.getTranslationForLanguageKey("player");
		tvColumnPlayerName.setTextColor(R.color.black);
		tvColumnPlayerName.setTypeface(Typeface.DEFAULT_BOLD);
		tvColumnPlayerName.setText(translation);
		tableRow.addView(tvColumnPlayerName);

		TextView tvColumnPlayerPoints = new TextView(this);
		translation = utils.getTranslationForLanguageKey("points");
		tvColumnPlayerPoints.setTypeface(Typeface.DEFAULT_BOLD);
		tvColumnPlayerPoints.setTextColor(R.color.black);
		tvColumnPlayerPoints.setText(translation);
		tableRow.addView(tvColumnPlayerPoints);

		TextView tvColumnPlayerDate = new TextView(this);
		translation = utils.getTranslationForLanguageKey("date");
		tvColumnPlayerDate.setTypeface(Typeface.DEFAULT_BOLD);
		tvColumnPlayerDate.setTextColor(R.color.black);
		tvColumnPlayerDate.setText(translation);
		tableRow.addView(tvColumnPlayerDate);
		tableLayout.addView(tableRow);
		int color = 0;
		while (cursor.moveToNext()) {
			tableRow = new TableRow(this);
			color = (color == R.color.grey) ? R.color.red : R.color.grey;
			tableRow.setBackgroundResource(color);

			String[] columnsPlayer = { "id", "name" };
			String[] playerParms = { cursor.getString(0) };
			Cursor cursor_player = readDb.query("player", columnsPlayer,
					"id=?", playerParms, null, null, null);
			cursor_player.moveToNext();

			TextView tvPlayerName = new TextView(this);
			tvPlayerName.setTextColor(R.color.black);
			tvPlayerName.setText(cursor_player.getString(1));
			tableRow.addView(tvPlayerName);

			TextView tvPointsRes = new TextView(this);
			tvPointsRes.setText(String.valueOf(cursor.getInt(1)));
			tvPointsRes.setTextColor(R.color.black);
			tableRow.addView(tvPointsRes);

			TextView tvDate = new TextView(this);
			tvDate.setTextColor(R.color.black);
			tvDate.setText(cursor.getString(2));
			tableRow.addView(tvDate);
			tableLayout.addView(tableRow);
		}

		// close db
		cursor.close();
		readDb.close();

		return linearLayout;
	}

}