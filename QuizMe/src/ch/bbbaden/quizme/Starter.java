package ch.bbbaden.quizme;


import java.util.ArrayList;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;



public class Starter extends ActionBarActivity{
	private DBZugriff dbZugriff;
	private SQLiteDatabase sqLiteDatabase;
	private TableLayout table;
	private Intent frage;
	private Intent neues_thema;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);
        
        table = (TableLayout) findViewById(R.id.table);
        
        setIntent();
    	setDatabase();
        viewThemen();
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    }
    
    private void setIntent(){
        frage = new Intent("ch.bbbaden.quizme.FRAGEN_EINES_THEMAS");
        frage.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        frage.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        
        neues_thema = new Intent("ch.bbbaden.quizme.NEUESTHEMA");
    	neues_thema.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    	neues_thema.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    }
    

    
    public void openNeuesThema(View view){
    	startActivity(neues_thema);
    }
    
    private void setDatabase(){
		dbZugriff = new DBZugriff(this);
		sqLiteDatabase = dbZugriff.getWritableDatabase();
    }
    
    private void viewThemen(){
    	ArrayList<String> Themen = dbZugriff.getAllThemen(sqLiteDatabase);
    	ArrayList<Integer> ThemenID = dbZugriff.getAllThemenID(sqLiteDatabase);
    	
    	
    	for(int i = 0; i < Themen.size(); i++){
    		final String dasThema = Themen.get(i);
    		final int dieID = ThemenID.get(i);
    		
    		Button Thema = new Button(this);
    		Thema.setText(dasThema);
    		Thema.setTextColor(Color.parseColor("#FFFFFF"));
    		Thema.setId(dieID);
    		Drawable d  = getResources().getDrawable(R.drawable.folder);
    		Thema.setBackgroundDrawable(d);
			TableRow tr = new TableRow(this);
			tr.addView(Thema);
			
    		Thema.setOnClickListener(new OnClickListener(){    			
				@Override
				public void onClick(View v) {
					frage.putExtra("themenName", dasThema);
					frage.putExtra("themenID", dieID);
					startActivity(frage);
				}});
    		
    		table.addView(tr);
    	}
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.neuesthema:
	    	startActivity(neues_thema);
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.starter, menu);
		return true;
	}

	@Override
	public void onPause(){
		super.onPause();
	}
}
