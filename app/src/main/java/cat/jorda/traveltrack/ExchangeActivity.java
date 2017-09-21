package cat.jorda.traveltrack;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by xj1 on 15/09/2017.
 */

public class ExchangeActivity extends AppCompatActivity  implements  PopupMenu.OnMenuItemClickListener
{
    private static String TAG = ExchangeActivity.class.getSimpleName();
    private HttpURLConnection urlConnection;
    private TextView fromCurrencyTV_;
    private TextView toCurrencyTV_;
    private EditText fromCurrencyValueET_;
    private TextView toCurrencyValueTV_;
    private Button convertBtn_;

    private String inputCurrency_;
    private String outputCurrency_;

    private double exchangeRate;

//    private String[] currencies = {"EUR","AUD","BGN","BRL","CAD","CHF","CNY","CZK","DKK","GBP","HKD","HRK","HUF","IDR","ILS","INR","JPY","KRW","MXN","MYR","NOK","NZD","PHP","PLN","RON","RUB","SEK","SGD","THB","TRY","USD","ZAR",};

    private @CurrencyMenuType.ICurrencyMenuType int currencyMenuType_;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.exchange_rate);
        fromCurrencyTV_ = (TextView) findViewById(R.id.exchange_from_value);
        fromCurrencyTV_.setOnClickListener(v -> showPopup(v, CurrencyMenuType.FROM_CURRENCY));
        inputCurrency_ = fromCurrencyTV_.getText().toString();

        toCurrencyTV_ = (TextView) findViewById(R.id.exchange_to_value);
        toCurrencyTV_.setOnClickListener(v -> showPopup(v, CurrencyMenuType.TO_CURRENCY));
        outputCurrency_ = toCurrencyTV_.getText().toString();

        fromCurrencyValueET_ = (EditText) findViewById(R.id.exchange_amount);
        toCurrencyValueTV_ = (TextView)findViewById(R.id.converted_amount);

        convertBtn_ =   (Button) findViewById(R.id.convert_btn);
        convertBtn_.setOnClickListener(v -> rateConversion(exchangeRate));

        new GetExchagneRates(fromCurrencyTV_.getText().toString()).execute("");
    }


    @Override
    public boolean onMenuItemClick(MenuItem item)
    {
        if (currencyMenuType_ == CurrencyMenuType.FROM_CURRENCY)
        {
            inputCurrency_ = item.getTitle().toString();
            fromCurrencyTV_.setText(inputCurrency_);
        }
        else
        {
            outputCurrency_ = item.getTitle().toString();
            toCurrencyTV_.setText(outputCurrency_);
        }

        new GetExchagneRates(fromCurrencyTV_.getText().toString()).execute("");

        return true;
    }

    public void showPopup(View v, @CurrencyMenuType.ICurrencyMenuType int currencyMenuType)
    {
        currencyMenuType_ = currencyMenuType;

        PopupMenu popup = new PopupMenu(this, v);

        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.currency_type, popup.getMenu());
        popup.show();
    }

    private class GetExchagneRates extends AsyncTask<String, Void, String>
    {
        private final String baseCurrency_;
        GetExchagneRates(String baseCurrency)
        {
            baseCurrency_   =   baseCurrency;
        }

        @Override
        protected String doInBackground(String... params)
        {
            StringBuilder result = new StringBuilder();

            try
            {
                URL url = new URL("http://api.fixer.io/latest?base=" + baseCurrency_);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;

                while ((line = reader.readLine()) != null)
                    result.append(line);

            }
            catch( Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                urlConnection.disconnect();
            }


            return result.toString();
        }

        @Override
        protected void onPostExecute(String result)
        {
            Log.d(TAG,"Got something");
            JSONObject jObject;
            try
            {
                jObject = new JSONObject(result);
                JSONObject ratesJson = jObject.getJSONObject("rates");

                String outPutCurrencyValue = ratesJson.getString(outputCurrency_);

                exchangeRate = Double.parseDouble(outPutCurrencyValue);

                rateConversion(exchangeRate);
                Log.d(TAG,"Got something");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }


    private void rateConversion(double exchangeRate)
    {
        double inputValue = Double.parseDouble(fromCurrencyValueET_.getText().toString());
        toCurrencyValueTV_.setText(String.valueOf(inputValue*exchangeRate));
    }
}
