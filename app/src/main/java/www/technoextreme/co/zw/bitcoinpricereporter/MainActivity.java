package www.technoextreme.co.zw.bitcoinpricereporter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/BTC";

    TextView mPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = findViewById(R.id.priceLabel);
        Spinner spinner = findViewById(R.id.currency_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Bitcoin", "" + adapterView.getItemAtPosition(i));
                Log.d("Bitcoin", "Position is: " + i);

                String finalUrl = BASE_URL + adapterView.getItemAtPosition(i);
                Log.d("Bitcoin", "Final url is: " + finalUrl);
                letsDoSomeNetworking(finalUrl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("Bitcoin", "Nothing selected");
            }
        });

    }

    private void letsDoSomeNetworking(String url) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                // this is called when response HTTP ststus is "200 OK"
                Log.d("Bitcoin", "JSON: " + response.toString());

                try {
                    String price = response.getString("last");
                    mPriceTextView.setText(price);
                } catch(JSONException e){
                    e.printStackTrace();
                    Log.e("ERROR", e.toString());
                }
            }



            public void onFailure(int statusCode, Header[] headers, JSONObject response){
                // this is called when response HTTP status is "4XX"
                Log.d("Bitcoin", "Request fail! Status code: " + statusCode);
                Log.d("Bitcoin", "Fail response: " + response);
                Toast.makeText(MainActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
