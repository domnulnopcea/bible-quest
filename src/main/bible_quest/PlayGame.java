package main.bible_quest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class PlayGame extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setId(R.string.linear_layout_id);

		linearLayout.setOrientation(LinearLayout.VERTICAL);

		RelativeLayout relativeLayout = new RelativeLayout(this);
		relativeLayout.setBackgroundResource(R.drawable.background);
		relativeLayout.addView(linearLayout);

		generateInterface(relativeLayout, getIntent());
	}

	@Override
	public void onBackPressed() {
		return;
	}

	// private methods
	private void showResults(Intent intent) {
		intent.setClass(this, Results.class);
		startActivity(intent);
	}

	int goodAnswer = -1;
	int[] questionsPutSoFar = new int[45];
	String[] questionsPlayerAnswers = new String[45];
	String[] questionsGoodAnswers = new String[45];
	private String[] raspunsuri = new String[3];

	private void generateInterface(final RelativeLayout relativeLayout,
			final Intent intent) {

		try {
			Bundle bundle = intent.getExtras();
			Integer questionsPut = bundle.getInt("questions_put");
			Integer questionsNumber = bundle.getInt("questions");
			String language = bundle.getString("language");

			LinearLayout linearLayout = (LinearLayout) relativeLayout
					.findViewById(R.string.linear_layout_id);
			TableLayout tableLayout = (TableLayout) relativeLayout
					.findViewById(R.string.table_layout_id);
			if (tableLayout != null)
				relativeLayout.removeView(tableLayout);

			linearLayout.removeAllViews();

			// read questions from DB
			Context context = getApplicationContext();
			Database db = new Database(context);
			SQLiteDatabase read_db = db.getReadableDatabase();
			String[] columnsQuestions = { "id", "content", "a", "b", "c",
					"answer" };
			String where = "";
			if (questionsPut != 1) {
				where = "id NOT IN (";
				for (int i = 0; i < questionsPut - 1; i++)
					where += questionsPutSoFar[i] + ", ";
				where += questionsPutSoFar[questionsPut - 1];
				where += ")";
				where += " AND language = '" + language + "'";
			} else
				where = "language = '" + language + "'";

			Cursor cursorQuestions = read_db.query("question",
					columnsQuestions, where, null, null, null, "RANDOM()", "1");

			// show the question and the answer options
			final RadioGroup radioGroupQuestions = new RadioGroup(context);

			if (cursorQuestions.getCount() > 0) {
				while (cursorQuestions.moveToNext()) {
					String question_content = cursorQuestions.getString(1);
					TextView text_view = new TextView(this);
					text_view.setTextColor(this.getResources().getColor(R.color.white));
					text_view.setTextSize(20);
					text_view.setTypeface(Typeface.DEFAULT_BOLD);
					text_view.setText(questionsPut + ". " + question_content);
					linearLayout.addView(text_view);
					int k = 0;
					goodAnswer = cursorQuestions.getInt(5);

					questionsGoodAnswers[questionsPut - 1] = cursorQuestions
							.getString(goodAnswer + 1);
					questionsPutSoFar[questionsPut - 1] = cursorQuestions
							.getInt(0);

					while (k < 3) {
						final RadioButton rb = new RadioButton(context);
						rb.setId(k + 1);
						rb.setClickable(true);
						rb.setText(cursorQuestions.getString(k + 2));
						radioGroupQuestions.addView(rb);
						raspunsuri[k] = cursorQuestions.getString(k + 2);
						k++;
					}
				}
			}

			// close db connection
			cursorQuestions.close();
			db.close();

			// temp
			/*
			 * String[] test = { "id", "content", "a", "b", "c", "answer" };
			 * String whereTest = "language = '" + language + "'"; Cursor
			 * cursorQuestionsTest = read_db .query("question", test, whereTest,
			 * null, null, null, "id", "1000"); while
			 * (cursorQuestionsTest.moveToNext()) { Log.i("questions_shown",
			 * cursorQuestionsTest.getString(0)); } cursorQuestionsTest.close();
			 */
			// temp

			//add buttons
			linearLayout.addView(radioGroupQuestions);

			final Button buttonNext = new Button(context);
			final Button buttonReview = new Button(context);

			String translation;
			final Utils utils = Utils.getInstance(getApplicationContext());

			translation = utils.getTranslationForLanguageKey("review_game");
			buttonReview.setText(translation);
			buttonReview.setEnabled(false);
			if (questionsPut == questionsNumber) {
				translation = utils
						.getTranslationForLanguageKey("show_results");
				buttonNext.setText(translation);
			} else {
				translation = utils
						.getTranslationForLanguageKey("next_question");
				buttonNext.setText(translation);
			}

			buttonNext.setEnabled(false);
			buttonNext.setId(R.string.next_button_id);
			final boolean gata = (questionsPut == questionsNumber);

			radioGroupQuestions
					.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup group,
								int checkedId) {

							buttonNext.setEnabled(true);
							if (gata) {
								int count = group.getChildCount();
								for (int k = 0; k < count; k++)
									group.getChildAt(k).setEnabled(false);

								buttonReview.setEnabled(true);
							}

						};
					});

			LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

			CharSequence textButtonNext = buttonNext.getText();
			tableLayout = new TableLayout(context);
			tableLayout.setId(R.string.table_layout_id);

			TableRow tableRow = new TableRow(context);
			tableRow.addView(buttonNext);

			if (textButtonNext.toString().equals(
					utils.getTranslationForLanguageKey("show_results")))
				tableRow.addView(buttonReview);

			tableLayout.addView(tableRow);
			relativeLayout.addView(tableLayout, layoutParams);

			this.setContentView(relativeLayout);

			buttonNext.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					int selectedAnswer = radioGroupQuestions
							.getCheckedRadioButtonId();
					final Intent new_intent = new Intent(PlayGame.this,
							PlayGame.class);
					Bundle bundle = intent.getExtras();
					Integer questions_put = bundle.getInt("questions_put");

					questionsPlayerAnswers[questions_put - 1] = raspunsuri[selectedAnswer - 1];
					questions_put++;
					bundle.putInt("questions_put", questions_put);
					if (goodAnswer == selectedAnswer) {
						int pointsSoFar = bundle.getInt("points");
						pointsSoFar += 1;
						bundle.putInt("points", pointsSoFar);
					}
					new_intent.putExtras(bundle);
					CharSequence text_button_next = buttonNext.getText();
					Utils utils = Utils.getInstance(getApplicationContext());
					if (text_button_next.toString().equals(
							utils.getTranslationForLanguageKey("show_results")))
						showResults(new_intent);
					else
						generateInterface(relativeLayout, new_intent);
				}
			});

			buttonReview.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent goReview = new Intent(PlayGame.this,
							ReviewGame.class);

					Bundle bundle = intent.getExtras();
					Integer questions_put = bundle.getInt("questions_put");
					int selected_answer = radioGroupQuestions
							.getCheckedRadioButtonId();
					questionsPlayerAnswers[questions_put - 1] = raspunsuri[selected_answer - 1];

					bundle = new Bundle();

					bundle.putIntArray("questions_put_so_far",
							questionsPutSoFar);
					bundle.putStringArray("questions_player_answers",
							questionsPlayerAnswers);
					bundle.putStringArray("questions_good_answers",
							questionsGoodAnswers);
					bundle.putString("language", Utils.language);
					goReview.putExtras(bundle);

					startActivity(goReview);
				}
			});

		} catch (Exception e) {
			Log.println(1, "error", e.getMessage());
		}
	}

}