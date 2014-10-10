package main.bible_quest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class HomeScreen extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.home_screen);
		try {
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				public void run() {
					startActivity(new Intent(HomeScreen.this, InputName.class));
					overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
				}
			}, 2000);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}