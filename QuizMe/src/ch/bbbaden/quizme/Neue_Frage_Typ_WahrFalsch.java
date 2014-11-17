package ch.bbbaden.quizme;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Neue_Frage_Typ_WahrFalsch extends ActionBarActivity {
	private String frage;
	private SQLiteDatabase sqLiteDatabase;
	private DBZugriff dbZugriff;
	private int themenID;
	private String typ;
	private Intent starter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_neue_frage_typ_wahrfalsch);
		setDatabase();
		getAllExtras();
		setIntent();
	}
	
	private void setIntent(){
		starter = new Intent("ch.bbbaden.quizme.STARTER");
		starter.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		starter.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
	}

	private void setDatabase() {
		dbZugriff = new DBZugriff(this);
		sqLiteDatabase = dbZugriff.getWritableDatabase();
	}
	
	private void getAllExtras() {
		Intent vorher = getIntent();
		Bundle index = vorher.getExtras();
		themenID = index.getInt("themenID");
		typ = index.getString("typ");
	}

	public void validateEintrag(View view) {
		EditText frageView = (EditText) findViewById(R.id.editText1);
		frage = frageView.getText().toString();

		if (frage.isEmpty()) {
			Toast.makeText(this, "Geben Sie bitte eine Frage ein.",
					Toast.LENGTH_LONG).show();
		} else {
			executeEintrag();
		}
	}

	private void executeEintrag() {
		int typID = dbZugriff.getTypID(sqLiteDatabase, typ);
		long fragenID = dbZugriff.setFrage(sqLiteDatabase, typID, themenID, frage, 0);
		
		RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroup1);
		int rbID = rg.getCheckedRadioButtonId();
		RadioButton rb = (RadioButton)findViewById(rbID);
		
		String antwort = rb.getText().toString();
		dbZugriff.setAntwort(sqLiteDatabase, fragenID, antwort, 1);
		
		if(antwort.equals("Wahr")){
			antwort = "Falsch";
		}
		else{
			antwort = "Wahr";
		}
		dbZugriff.setAntwort(sqLiteDatabase, fragenID, antwort, 0);
		Toast.makeText(this, "Die Frage wurde gespeichert.", Toast.LENGTH_LONG).show();
		onBackPressed();
	}
	
	@Override
	public void onBackPressed(){
		startActivity(starter);
		finish();
	}

}
