package main.bible_quest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ReviewGame extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.review_game);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		int[] questionsPutSoFar = bundle
				.getIntArray("questions_put_so_far");
		String[] questionsPlayerAnswers = bundle
				.getStringArray("questions_player_answers");
		String[] questionsGoodAnswers = bundle
				.getStringArray("questions_good_answers");
		String language = bundle.getString("language");

		Database db = new Database(getApplicationContext());
		SQLiteDatabase readDb = db.getReadableDatabase();

		String[] columns = { "content" };
		int i = 0;
		String where = "";

		String translation;
		final Utils utils = Utils.getInstance(getApplicationContext());

		translation = utils.getTranslationForLanguageKey("review_game");
		TextView headReviewGame = (TextView) findViewById(R.id.head_review_game);
		headReviewGame.setText(translation);

		TableLayout tableLayout = (TableLayout) findViewById(R.id.layout_review_table);
		tableLayout.setStretchAllColumns(true);
		tableLayout.removeAllViews();
		Context context = getApplicationContext();
		int k = 0;

		TableRow.LayoutParams trParams = new TableRow.LayoutParams();
		trParams.span = 2;
		TableRow tr = new TableRow(context);
		TextView question_title = new TextView(context);
		translation = utils.getTranslationForLanguageKey("question");
		question_title.setText(translation);
		question_title.setLayoutParams(trParams);
		tr.addView(question_title);

		TextView good_title = new TextView(context);
		translation = utils.getTranslationForLanguageKey("good_answer");
		good_title.setGravity(Gravity.CENTER);
		good_title.setText(translation);
		good_title.setLayoutParams(trParams);
		tr.addView(good_title);

		TextView answerTitle = new TextView(context);
		translation = utils.getTranslationForLanguageKey("your_answer");
		answerTitle.setText(translation);
		answerTitle.setLayoutParams(trParams);
		answerTitle.setGravity(Gravity.CENTER);
		tr.addView(answerTitle);
		tableLayout.addView(tr);
		TextView tv;

		tv = new TextView(context);
		tv.setBackgroundResource(R.color.grey);
		tv.setHeight(2);
		tableLayout.addView(tv);

		for (i = 0; i < questionsPutSoFar.length - 1; i++) {
			if (questionsPutSoFar[i] != 0) {
				where = "id IN (" + questionsPutSoFar[i]
						+ ") AND language = '" + language + "'";
				try {
					Cursor cursor = readDb.query("question", columns, where,
							null, null, null, null);

					while (cursor.moveToNext()) {
						tr = new TableRow(context);

						trParams = new TableRow.LayoutParams();
						trParams.span = 2;

						TextView question = new TextView(context);
						question.setText((i + 1) + ". " + cursor.getString(0));
						question.setLayoutParams(trParams);
						question.setPadding(0, 2, 2, 2);
						tr.addView(question);

						TextView goodAnswer = new TextView(context);
						goodAnswer.setText(questionsGoodAnswers[k]);
						trParams.span = 2;
						goodAnswer.setLayoutParams(trParams);
						goodAnswer.setPadding(0, 2, 2, 2);
						goodAnswer.setGravity(Gravity.CENTER
								| Gravity.CENTER_VERTICAL);
						tr.addView(goodAnswer);

						TextView playerAnswer = new TextView(context);
						playerAnswer.setText(questionsPlayerAnswers[k]);
						playerAnswer.setPadding(0, 2, 2, 2);
						trParams.span = 2;

						playerAnswer.setLayoutParams(trParams);
						playerAnswer.setGravity(Gravity.CENTER
								| Gravity.CENTER_VERTICAL);
						tr.addView(playerAnswer);

						tableLayout.addView(tr);

						if (questionsPutSoFar[i + 1] != 0) {
							tv = new TextView(context);
							tv.setBackgroundResource(R.color.grey);
							tv.setHeight(2);
							tableLayout.addView(tv);
						}
						k++;
					}
					cursor.close();
				} catch (Exception e) {
					Log.println(1, "error", e.getMessage());
				}
			}
		}
	}
}
