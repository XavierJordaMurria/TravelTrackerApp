package cat.jorda.traveltrack.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xj1 on 14/09/2017.
 */

public class CurrencyExchangeRate
{
    private String base;
    private String date;
    private Map<String, Integer> rates = new HashMap<>();

    public String getBase()
    {
        return base;
    }
    public void setBase(String base)
    {
        this.base = base;
    }

    public String getDate()
    {
        return date;
    }
    public void setDate(String date)
    {
        this.date = date;
    }

    public Map<String, Integer> getRates() {
        return rates;
    }

    public void setRates(Map<String, Integer> rates) {
        this.rates = rates;
    }
}
