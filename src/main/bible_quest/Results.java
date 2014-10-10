package main.bible_quest;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

public class Results extends TabActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.results_page);

			Intent intent = getIntent();
			Bundle bundle = intent.getExtras();
			Integer questionsPut = bundle.getInt("questions_put");
			questionsPut--;

			TabHost tabHost = getTabHost();
			tabHost.clearAllTabs();
			TabHost.TabSpec spec;
			Intent intentTab = null;

			intentTab = new Intent().setClass(this, ResultRender.class);
			bundle.putInt("game_type", 10);
			if (questionsPut != 10)
				bundle.putInt("just_show_results", 1);
			intentTab.putExtras(bundle);
			spec = tabHost.newTabSpec("10").setIndicator("10").setContent(
					intentTab);
			tabHost.addTab(spec);
			bundle.putInt("just_show_results", 0);
			
			intentTab = new Intent().setClass(this, ResultRender.class);
			bundle.putInt("game_type", 15);
			if (questionsPut != 15)
				bundle.putInt("just_show_results", 1);
			intentTab.putExtras(bundle);
			spec = tabHost.newTabSpec("15").setIndicator("15").setContent(
					intentTab);
			tabHost.addTab(spec);
			bundle.putInt("just_show_results", 0);
			
			intentTab = new Intent().setClass(this, ResultRender.class);
			bundle.putInt("game_type", 30);
			if (questionsPut != 30)
				bundle.putInt("just_show_results", 1);
			intentTab.putExtras(bundle);
			spec = tabHost.newTabSpec("30").setIndicator("30").setContent(
					intentTab);
			tabHost.addTab(spec);
			bundle.putInt("just_show_results", 0);
			
			intentTab = new Intent().setClass(this, ResultRender.class);
			bundle.putInt("game_type", 45);
			if (questionsPut != 45)
				bundle.putInt("just_show_results", 1);

			intentTab.putExtras(bundle);
			spec = tabHost.newTabSpec("45").setIndicator("45").setContent(
					intentTab);
			tabHost.addTab(spec);
			bundle.putInt("just_show_results", 0);
			
			Integer pos = 0;
			switch (questionsPut) {
				case 10:
					pos = 0;
					break;
				case 15:
					pos = 1;
					break;
				case 30:
					pos = 2;
					break;
				case 45:
					pos = 3;
					break;					
			}
			tabHost.setCurrentTab(pos);
		} catch (Exception e) {
			Log.println(1, "error", e.getMessage());
		}
	}
	
	@Override
	public void onBackPressed() {
		return;
	}
}