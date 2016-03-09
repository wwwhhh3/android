package com.amb.wave_textview;

import android.os.Bundle;
import android.app.Activity;

public class MainActivity extends Activity {

	private TitanicTextView tv;
	Titanic titanic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv = (TitanicTextView) findViewById(R.id.titanicTextView1);
		tv.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));
		titanic = new Titanic();
		titanic.start(tv);
	}
}
