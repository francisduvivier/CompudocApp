package duviwin.compudocapp.OpdrachtDetails;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import duviwin.compudocapp.R;
import duviwin.compudocapp.html_info.OpdrListHtmlInfo.Nms;


public class ShowDetailsActivity extends ActionBarActivity {
    Opdracht opdracht=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);
        Intent intent=getIntent();
        opdracht=(Opdracht) intent.getSerializableExtra("opdracht");
        if(opdracht==null){
            //In this case this is a call from an URI
            String url=intent.getData().toString();
            opdracht=Opdracht.getDummy("test");
            opdracht.isDummy=false;
            opdracht.shrtInfo[Nms.opdrachtNr.i]=url.replaceAll(".*opdrachtnr=([\\d]*).*","$1");

        }

        class GetExtraInfoTask extends AsyncTask<Object, Void, Opdracht> {
            @Override
            protected Opdracht doInBackground(Object... params) {
                opdracht.getExtraInfo();
                return opdracht;
            }
            @Override
            protected void onPostExecute(Opdracht result) {
                ShowDetailsActivity.this.update(result);
            }
        }
        GetExtraInfoTask task=new GetExtraInfoTask();
        task.execute();
    }

    public void update(Opdracht opdr){
        int i=0;
        for (String prop : opdr.allProperties) {
            TextView tv=((TextView) findViewById(Opdracht.propertyIds[i++]));
            tv.setPadding(0,5,0,5);
            tv.setText(prop);
            ((ViewGroup.MarginLayoutParams) tv.getLayoutParams()).setMargins(0,5,0,5);
            tv.setBackgroundColor(Color.parseColor(opdr.uitlegClr));

        }
        findViewById(opdr.getPropertyId("gepost")).setBackgroundColor(Color.parseColor(opdr.numberClr));
        findViewById(opdr.getPropertyId("omschrijving")).setBackgroundColor(Color.parseColor(opdr.uitlegClr));
        ((TextView) findViewById(R.id.det_bod_result)).setText(opdr.bodResult);



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void bieden(View view) {
        EditText minBodView = (EditText) findViewById(R.id.min_bod);
        EditText maxBodView = (EditText) findViewById(R.id.max_bod);
        int minBod=Integer.parseInt(minBodView.getText().toString());
        int maxBod=Integer.parseInt(maxBodView.getText().toString());

        opdracht.bied(minBod,maxBod, this);
    }
    public void openMaps(View view){
        String straat=opdracht.getProperty("straat").replaceAll(".*Adres: ","");
        Uri geoLocation=Uri.parse("http://maps.google.com/maps?daddr="+straat+", "+opdracht.getProperty("stad"));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
