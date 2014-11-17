package ch.bbbaden.quizme;

import java.util.ArrayList;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Neues_Thema extends ActionBarActivity {
	private String neuesThemaString;
	private DBZugriff dbZugriff;
	private SQLiteDatabase sqLiteDatabase;
	private Intent starter;
	private ArrayList<String> themenNamen; 
	private boolean vorhanden;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_neues_thema);

		dbZugriff = new DBZugriff(this);
		sqLiteDatabase = dbZugriff.getWritableDatabase();

		starter = new Intent("ch.bbbaden.quizme.STARTER");
		starter.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		starter.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
	}

	public void validateThemenName(View view) {
		EditText neuesThema = (EditText) findViewById(R.id.neuesThema);
		neuesThemaString = neuesThema.getText().toString();

		if (neuesThemaString.length() > 15) {
			Toast.makeText(this, "Maximal sind 15 Buchstaben erlaubt.",
					Toast.LENGTH_LONG).show();
		}
		else if(neuesThemaString.isEmpty()){
			Toast.makeText(this, "Geben Sie einen Namen ein.", Toast.LENGTH_LONG).show();
		}

		themenNamen = dbZugriff.getAllThemen(sqLiteDatabase);
		abgleichThemenName();
		
		if (vorhanden == true) {
			Toast.makeText(this, "Dieser Themen-Name ist bereits vorhanden.", Toast.LENGTH_LONG).show();
		} else {
			executeThemenName();
		}
	}

	private void executeThemenName() {
		dbZugriff.insertNewThema(sqLiteDatabase, neuesThemaString);
		startActivity(starter);
	}
	
	private void abgleichThemenName(){
		for(int i = 0; i < themenNamen.size(); i++){
			if(themenNamen.get(i).equals(neuesThemaString)){
				vorhanden = true;
			}
			else{
				vorhanden = false;
			}
		}
	}
	
	@Override
	public void onBackPressed(){
		startActivity(starter);
		finish();
	}
}
