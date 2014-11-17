package ch.bbbaden.quizme;

import java.util.ArrayList;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Fragen_eines_Themas extends ActionBarActivity {
	private String themenName;
	private int themenID;
	private SQLiteDatabase sqLiteDatabase;
	private DBZugriff dbZugriff;
	private Intent starter, neue_frage_typen_auswahl, spielen_aus_thema;
	private LinearLayout lin;
	private ArrayList<String> fragen;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLayout();
		setIntent();
		getAllExtras();
		setDatabase();
		setFragenView();
	}

	private void getAllExtras() {
		Intent vorher = getIntent();
		Bundle index = vorher.getExtras();
		themenName = index.getString("themenName");
		themenID = index.getInt("themenID");
	}

	private void setDatabase() {
		dbZugriff = new DBZugriff(this);
		sqLiteDatabase = dbZugriff.getWritableDatabase();
	}
	
	private void setIntent(){
		starter  = new Intent("ch.bbbaden.quizme.STARTER");
		starter.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		starter.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		
		neue_frage_typen_auswahl = new Intent("ch.bbbaden.quizme.NEUE_FRAGE_TYPEN_AUSWAHL");
		neue_frage_typen_auswahl.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		neue_frage_typen_auswahl.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		
		spielen_aus_thema = new Intent("ch.bbbaden.quizme.SPIELEN_AUS_THEMA");
		spielen_aus_thema.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		spielen_aus_thema.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	}
	
	private void setLayout(){
		setContentView(R.layout.activity_fragen_eines_themas);
		lin = (LinearLayout) findViewById(R.id.lin);
		TextView themenNameView = new TextView(this);
		themenNameView.setText(themenName);
		lin.addView(themenNameView);
	}
	
	@Override
	public void onBackPressed(){
		startActivity(starter);
		finish();
	}
	
	private void setFragenView(){
		fragen =dbZugriff.getAllFragenInTABLE_FRAGEbyThema(sqLiteDatabase, themenID);
		ArrayList<Integer> FID_Typen = dbZugriff.getAllFID_TypInFRAGEN_TABLE(sqLiteDatabase, themenID);
		ArrayList<Integer> fragenID = dbZugriff.getAllFragenIDIntTABLE_FRAGEbyThema(sqLiteDatabase, themenID);
		ArrayList<String> Typ = new ArrayList<String>();
		
		for(int x = 0; x < FID_Typen.size(); x++){
			int typID = FID_Typen.get(x);
			String TypName = dbZugriff.getTypNamebyID(sqLiteDatabase, typID);
			Typ.add(TypName);
		}
		
		for(int i = 0; i < fragen.size(); i++){
		Button button = new Button(this);
		button.setText(Typ.get(i) + ": " +fragen.get(i));
		button.setId(fragenID.get(i));
		lin.addView(button);
	}}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.neuefrage:
			neue_frage_typen_auswahl.putExtra("themenID", themenID);
			neue_frage_typen_auswahl.putExtra("themenName", themenName);
			startActivity(neue_frage_typen_auswahl);
			break;
			
		case R.id.spielen:
			if(!fragen.isEmpty()){
				spielen_aus_thema.putExtra("themenID", themenID);
				startActivity(spielen_aus_thema);
			}
				else{
					Toast.makeText(this, "Sie brauchen mindestens eine Frage.", Toast.LENGTH_LONG).show();
				}
			break;
			
		case R.id.themaloeschen:
			dbZugriff.loescheThema(sqLiteDatabase, themenID);
			startActivity(starter);
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater blowUp = getMenuInflater();
		blowUp.inflate(R.menu.fragen_eines_themas, menu);
		return true;
	}
}
