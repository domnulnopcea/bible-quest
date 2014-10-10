package main.bible_quest;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.DisplayMetrics;

public final class Utils {

	public static String language = null;
	public static DisplayMetrics metrics;
	public static Resources resources;
	private static Utils instance = null;

	public static Utils getInstance(Context context) {
		if (instance == null) {
			synchronized (Utils.class) {
				instance = new Utils(context);
			}
		}

		return instance;
	}

	public String getTranslationForLanguageKey(String key) {

		int id = resources.getIdentifier(language + "_" + key, "string",
				"main.bible_quest");
		return resources.getString(id);
	}
	
	//private methods
	private Utils(Context context) {
		Database db = new Database(context);
		SQLiteDatabase readDb = db.getReadableDatabase();
		String[] columns = { "language" };
		Cursor cursorSettings = readDb.query("settings", columns, null, null,
				null, null, null);

		while (cursorSettings.moveToNext())
			Utils.language = cursorSettings.getString(0);
		cursorSettings.close();
		db.close();
		Utils.metrics = new DisplayMetrics();
		Utils.resources = new Resources(context.getAssets(), metrics, null);
	}
}