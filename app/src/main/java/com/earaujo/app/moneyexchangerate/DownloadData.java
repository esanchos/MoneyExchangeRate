package com.earaujo.app.moneyexchangerate;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Eduardo on 04/07/2016.
 */
public class DownloadData {

    private static final String URL_COUNTRIES = "http://apilayer.net/api/list?access_key=1e98533581cd6827c561b1d6d7883b93&prettyprint=1";
    private static final String URL_CURRENCIES = "http://apilayer.net/api/live?access_key=1e98533581cd6827c561b1d6d7883b93&format=1";

    private static long timeStamp=0;

    private static final String initialCoutries = "\n" +
            "{\"success\":true,\"terms\":\"https:\\/\\/currencylayer.com\\/terms\",\"privacy\":\"https:\\/\\/currencylayer.com\\/privacy\",\"currencies\":{\"AED\":\"United Arab Emirates Dirham\",\"AFN\":\"Afghan Afghani\",\"ALL\":\"Albanian Lek\",\"AMD\":\"Armenian Dram\",\"ANG\":\"Netherlands Antillean Guilder\",\"AOA\":\"Angolan Kwanza\",\"ARS\":\"Argentine Peso\",\"AUD\":\"Australian Dollar\",\"AWG\":\"Aruban Florin\",\"AZN\":\"Azerbaijani Manat\",\"BAM\":\"Bosnia-Herzegovina Convertible Mark\",\"BBD\":\"Barbadian Dollar\",\"BDT\":\"Bangladeshi Taka\",\"BGN\":\"Bulgarian Lev\",\"BHD\":\"Bahraini Dinar\",\"BIF\":\"Burundian Franc\",\"BMD\":\"Bermudan Dollar\",\"BND\":\"Brunei Dollar\",\"BOB\":\"Bolivian Boliviano\",\"BRL\":\"Brazilian Real\",\"BSD\":\"Bahamian Dollar\",\"BTC\":\"Bitcoin\",\"BTN\":\"Bhutanese Ngultrum\",\"BWP\":\"Botswanan Pula\",\"BYR\":\"Belarusian Ruble\",\"BZD\":\"Belize Dollar\",\"CAD\":\"Canadian Dollar\",\"CDF\":\"Congolese Franc\",\"CHF\":\"Swiss Franc\",\"CLF\":\"Chilean Unit of Account (UF)\",\"CLP\":\"Chilean Peso\",\"CNY\":\"Chinese Yuan\",\"COP\":\"Colombian Peso\",\"CRC\":\"Costa Rican Col\\u00f3n\",\"CUC\":\"Cuban Convertible Peso\",\"CUP\":\"Cuban Peso\",\"CVE\":\"Cape Verdean Escudo\",\"CZK\":\"Czech Republic Koruna\",\"DJF\":\"Djiboutian Franc\",\"DKK\":\"Danish Krone\",\"DOP\":\"Dominican Peso\",\"DZD\":\"Algerian Dinar\",\"EEK\":\"Estonian Kroon\",\"EGP\":\"Egyptian Pound\",\"ERN\":\"Eritrean Nakfa\",\"ETB\":\"Ethiopian Birr\",\"EUR\":\"Euro\",\"FJD\":\"Fijian Dollar\",\"FKP\":\"Falkland Islands Pound\",\"GBP\":\"British Pound Sterling\",\"GEL\":\"Georgian Lari\",\"GGP\":\"Guernsey Pound\",\"GHS\":\"Ghanaian Cedi\",\"GIP\":\"Gibraltar Pound\",\"GMD\":\"Gambian Dalasi\",\"GNF\":\"Guinean Franc\",\"GTQ\":\"Guatemalan Quetzal\",\"GYD\":\"Guyanaese Dollar\",\"HKD\":\"Hong Kong Dollar\",\"HNL\":\"Honduran Lempira\",\"HRK\":\"Croatian Kuna\",\"HTG\":\"Haitian Gourde\",\"HUF\":\"Hungarian Forint\",\"IDR\":\"Indonesian Rupiah\",\"ILS\":\"Israeli New Sheqel\",\"IMP\":\"Manx pound\",\"INR\":\"Indian Rupee\",\"IQD\":\"Iraqi Dinar\",\"IRR\":\"Iranian Rial\",\"ISK\":\"Icelandic Kr\\u00f3na\",\"JEP\":\"Jersey Pound\",\"JMD\":\"Jamaican Dollar\",\"JOD\":\"Jordanian Dinar\",\"JPY\":\"Japanese Yen\",\"KES\":\"Kenyan Shilling\",\"KGS\":\"Kyrgystani Som\",\"KHR\":\"Cambodian Riel\",\"KMF\":\"Comorian Franc\",\"KPW\":\"North Korean Won\",\"KRW\":\"South Korean Won\",\"KWD\":\"Kuwaiti Dinar\",\"KYD\":\"Cayman Islands Dollar\",\"KZT\":\"Kazakhstani Tenge\",\"LAK\":\"Laotian Kip\",\"LBP\":\"Lebanese Pound\",\"LKR\":\"Sri Lankan Rupee\",\"LRD\":\"Liberian Dollar\",\"LSL\":\"Lesotho Loti\",\"LTL\":\"Lithuanian Litas\",\"LVL\":\"Latvian Lats\",\"LYD\":\"Libyan Dinar\",\"MAD\":\"Moroccan Dirham\",\"MDL\":\"Moldovan Leu\",\"MGA\":\"Malagasy Ariary\",\"MKD\":\"Macedonian Denar\",\"MMK\":\"Myanma Kyat\",\"MNT\":\"Mongolian Tugrik\",\"MOP\":\"Macanese Pataca\",\"MRO\":\"Mauritanian Ouguiya\",\"MUR\":\"Mauritian Rupee\",\"MVR\":\"Maldivian Rufiyaa\",\"MWK\":\"Malawian Kwacha\",\"MXN\":\"Mexican Peso\",\"MYR\":\"Malaysian Ringgit\",\"MZN\":\"Mozambican Metical\",\"NAD\":\"Namibian Dollar\",\"NGN\":\"Nigerian Naira\",\"NIO\":\"Nicaraguan C\\u00f3rdoba\",\"NOK\":\"Norwegian Krone\",\"NPR\":\"Nepalese Rupee\",\"NZD\":\"New Zealand Dollar\",\"OMR\":\"Omani Rial\",\"PAB\":\"Panamanian Balboa\",\"PEN\":\"Peruvian Nuevo Sol\",\"PGK\":\"Papua New Guinean Kina\",\"PHP\":\"Philippine Peso\",\"PKR\":\"Pakistani Rupee\",\"PLN\":\"Polish Zloty\",\"PYG\":\"Paraguayan Guarani\",\"QAR\":\"Qatari Rial\",\"RON\":\"Romanian Leu\",\"RSD\":\"Serbian Dinar\",\"RUB\":\"Russian Ruble\",\"RWF\":\"Rwandan Franc\",\"SAR\":\"Saudi Riyal\",\"SBD\":\"Solomon Islands Dollar\",\"SCR\":\"Seychellois Rupee\",\"SDG\":\"Sudanese Pound\",\"SEK\":\"Swedish Krona\",\"SGD\":\"Singapore Dollar\",\"SHP\":\"Saint Helena Pound\",\"SLL\":\"Sierra Leonean Leone\",\"SOS\":\"Somali Shilling\",\"SRD\":\"Surinamese Dollar\",\"STD\":\"S\\u00e3o Tom\\u00e9 and Pr\\u00edncipe Dobra\",\"SVC\":\"Salvadoran Col\\u00f3n\",\"SYP\":\"Syrian Pound\",\"SZL\":\"Swazi Lilangeni\",\"THB\":\"Thai Baht\",\"TJS\":\"Tajikistani Somoni\",\"TMT\":\"Turkmenistani Manat\",\"TND\":\"Tunisian Dinar\",\"TOP\":\"Tongan Pa\\u02bbanga\",\"TRY\":\"Turkish Lira\",\"TTD\":\"Trinidad and Tobago Dollar\",\"TWD\":\"New Taiwan Dollar\",\"TZS\":\"Tanzanian Shilling\",\"UAH\":\"Ukrainian Hryvnia\",\"UGX\":\"Ugandan Shilling\",\"USD\":\"United States Dollar\",\"UYU\":\"Uruguayan Peso\",\"UZS\":\"Uzbekistan Som\",\"VEF\":\"Venezuelan Bol\\u00edvar Fuerte\",\"VND\":\"Vietnamese Dong\",\"VUV\":\"Vanuatu Vatu\",\"WST\":\"Samoan Tala\",\"XAF\":\"CFA Franc BEAC\",\"XAG\":\"Silver (troy ounce)\",\"XAU\":\"Gold (troy ounce)\",\"XCD\":\"East Caribbean Dollar\",\"XDR\":\"Special Drawing Rights\",\"XOF\":\"CFA Franc BCEAO\",\"XPF\":\"CFP Franc\",\"YER\":\"Yemeni Rial\",\"ZAR\":\"South African Rand\",\"ZMK\":\"Zambian Kwacha (pre-2013)\",\"ZMW\":\"Zambian Kwacha\",\"ZWL\":\"Zimbabwean Dollar\"}}";

    private static final String initialRates = "{\n" +
            "  \"success\":true,\n" +
            "  \"terms\":\"https:\\/\\/currencylayer.com\\/terms\",\n" +
            "  \"privacy\":\"https:\\/\\/currencylayer.com\\/privacy\",\n" +
            "  \"timestamp\":1467635828,\n" +
            "  \"source\":\"USD\",\n" +
            "  \"quotes\":{\n" +
            "    \"USDAED\":3.673095,\n" +
            "    \"USDAFN\":68.64987,\n" +
            "    \"USDALL\":123.220209,\n" +
            "    \"USDAMD\":477.159907,\n" +
            "    \"USDANG\":1.774992,\n" +
            "    \"USDAOA\":165.732213,\n" +
            "    \"USDARS\":15.060241,\n" +
            "    \"USDAUD\":1.32785,\n" +
            "    \"USDAWG\":1.7929,\n" +
            "    \"USDAZN\":1.542499,\n" +
            "    \"USDBAM\":1.755172,\n" +
            "    \"USDBBD\":2.00445,\n" +
            "    \"USDBDT\":78.312025,\n" +
            "    \"USDBGN\":1.75535,\n" +
            "    \"USDBHD\":0.377301,\n" +
            "    \"USDBIF\":1658.95,\n" +
            "    \"USDBMD\":0.9997,\n" +
            "    \"USDBND\":1.34585,\n" +
            "    \"USDBOB\":6.882312,\n" +
            "    \"USDBRL\":3.22275,\n" +
            "    \"USDBSD\":0.9998,\n" +
            "    \"USDBTC\":0.001492,\n" +
            "    \"USDBTN\":67.269607,\n" +
            "    \"USDBWP\":10.801999,\n" +
            "    \"USDBYR\":2,\n" +
            "    \"USDBZD\":2.00945,\n" +
            "    \"USDCAD\":1.285926,\n" +
            "    \"USDCDF\":955.575304,\n" +
            "    \"USDCHF\":0.97281,\n" +
            "    \"USDCLF\":0.025103,\n" +
            "    \"USDCLP\":666.666667,\n" +
            "    \"USDCNY\":6.664445,\n" +
            "    \"USDCOP\":3333.333333,\n" +
            "    \"USDCRC\":546.615021,\n" +
            "    \"USDCUC\":1,\n" +
            "    \"USDCUP\":0.999504,\n" +
            "    \"USDCVE\":99.115002,\n" +
            "    \"USDCZK\":24.32945,\n" +
            "    \"USDDJF\":177.719953,\n" +
            "    \"USDDKK\":6.677796,\n" +
            "    \"USDDOP\":45.928991,\n" +
            "    \"USDDZD\":110.143981,\n" +
            "    \"USDEEK\":14.048008,\n" +
            "    \"USDEGP\":8.888889,\n" +
            "    \"USDERN\":10.469997,\n" +
            "    \"USDETB\":21.904039,\n" +
            "    \"USDEUR\":0.89765,\n" +
            "    \"USDFJD\":2.061502,\n" +
            "    \"USDFKP\":0.75324,\n" +
            "    \"USDGBP\":0.75325,\n" +
            "    \"USDGEL\":2.329566,\n" +
            "    \"USDGGP\":0.75324,\n" +
            "    \"USDGHS\":3.94905,\n" +
            "    \"USDGIP\":0.75324,\n" +
            "    \"USDGMD\":42.850181,\n" +
            "    \"USDGNF\":8982.901299,\n" +
            "    \"USDGTQ\":7.636351,\n" +
            "    \"USDGYD\":207.189475,\n" +
            "    \"USDHKD\":7.757952,\n" +
            "    \"USDHNL\":22.745049,\n" +
            "    \"USDHRK\":6.7437,\n" +
            "    \"USDHTG\":63.234501,\n" +
            "    \"USDHUF\":284.505012,\n" +
            "    \"USDIDR\":13072.5,\n" +
            "    \"USDILS\":3.856537,\n" +
            "    \"USDIMP\":0.75324,\n" +
            "    \"USDINR\":67.114094,\n" +
            "    \"USDIQD\":1167.85,\n" +
            "    \"USDIRR\":12292.508457,\n" +
            "    \"USDISK\":122.640106,\n" +
            "    \"USDJEP\":0.75324,\n" +
            "    \"USDJMD\":126.569748,\n" +
            "    \"USDJOD\":0.708994,\n" +
            "    \"USDJPY\":103.092784,\n" +
            "    \"USDKES\":101.144499,\n" +
            "    \"USDKGS\":67.401278,\n" +
            "    \"USDKHR\":4081.504216,\n" +
            "    \"USDKMF\":442.349668,\n" +
            "    \"USDKPW\":129.591379,\n" +
            "    \"USDKRW\":1111.111111,\n" +
            "    \"USDKWD\":0.3016,\n" +
            "    \"USDKYD\":0.832743,\n" +
            "    \"USDKZT\":336.740373,\n" +
            "    \"USDLAK\":8088.089682,\n" +
            "    \"USDLBP\":1507.55004,\n" +
            "    \"USDLKR\":146.315017,\n" +
            "    \"USDLRD\":89.999723,\n" +
            "    \"USDLSL\":14.505496,\n" +
            "    \"USDLTL\":2.933498,\n" +
            "    \"USDLVL\":0.627402,\n" +
            "    \"USDLYD\":1.382398,\n" +
            "    \"USDMAD\":9.786199,\n" +
            "    \"USDMDL\":19.830499,\n" +
            "    \"USDMGA\":3229.092727,\n" +
            "    \"USDMKD\":55.270216,\n" +
            "    \"USDMMK\":1176.249918,\n" +
            "    \"USDMNT\":1986.70406,\n" +
            "    \"USDMOP\":7.98885,\n" +
            "    \"USDMRO\":356.550203,\n" +
            "    \"USDMUR\":35.299008,\n" +
            "    \"USDMVR\":15.270173,\n" +
            "    \"USDMWK\":713.124999,\n" +
            "    \"USDMXN\":18.348624,\n" +
            "    \"USDMYR\":3.9858,\n" +
            "    \"USDMZN\":63.589848,\n" +
            "    \"USDNAD\":14.505502,\n" +
            "    \"USDNGN\":282.685001,\n" +
            "    \"USDNIO\":28.449502,\n" +
            "    \"USDNOK\":8.3022,\n" +
            "    \"USDNPR\":107.615002,\n" +
            "    \"USDNZD\":1.38695,\n" +
            "    \"USDOMR\":0.38505,\n" +
            "    \"USDPAB\":0.99975,\n" +
            "    \"USDPEN\":3.283533,\n" +
            "    \"USDPGK\":3.16615,\n" +
            "    \"USDPHP\":46.842035,\n" +
            "    \"USDPKR\":104.725032,\n" +
            "    \"USDPLN\":3.979308,\n" +
            "    \"USDPYG\":5000.000122,\n" +
            "    \"USDQAR\":3.640994,\n" +
            "    \"USDRON\":4.051795,\n" +
            "    \"USDRSD\":110.659993,\n" +
            "    \"USDRUB\":63.749711,\n" +
            "    \"USDRWF\":746.29,\n" +
            "    \"USDSAR\":3.750938,\n" +
            "    \"USDSBD\":7.901701,\n" +
            "    \"USDSCR\":12.950502,\n" +
            "    \"USDSDG\":6.078549,\n" +
            "    \"USDSEK\":8.42815,\n" +
            "    \"USDSGD\":1.345261,\n" +
            "    \"USDSHP\":0.75324,\n" +
            "    \"USDSLL\":5516.804567,\n" +
            "    \"USDSOS\":586.999795,\n" +
            "    \"USDSRD\":7.09495,\n" +
            "    \"USDSTD\":22020,\n" +
            "    \"USDSVC\":8.74835,\n" +
            "    \"USDSYP\":62.563503,\n" +
            "    \"USDSZL\":14.528982,\n" +
            "    \"USDTHB\":35.056497,\n" +
            "    \"USDTJS\":7.85815,\n" +
            "    \"USDTMT\":3.5,\n" +
            "    \"USDTND\":2.189765,\n" +
            "    \"USDTOP\":2.257602,\n" +
            "    \"USDTRY\":2.898551,\n" +
            "    \"USDTTD\":6.66845,\n" +
            "    \"USDTWD\":32.206119,\n" +
            "    \"USDTZS\":2189.399605,\n" +
            "    \"USDUAH\":24.814501,\n" +
            "    \"USDUGX\":3396.049808,\n" +
            "    \"USDUSD\":1,\n" +
            "    \"USDUYU\":30.58104,\n" +
            "    \"USDUZS\":2934.34978,\n" +
            "    \"USDVEF\":9.99001,\n" +
            "    \"USDVND\":22301.5,\n" +
            "    \"USDVUV\":115.000173,\n" +
            "    \"USDWST\":2.533202,\n" +
            "    \"USDXAF\":589.603286,\n" +
            "    \"USDXAG\":0.048864,\n" +
            "    \"USDXAU\":0.00074,\n" +
            "    \"USDXCD\":2.70255,\n" +
            "    \"USDXDR\":0.71655,\n" +
            "    \"USDXOF\":589.598357,\n" +
            "    \"USDXPF\":107.196935,\n" +
            "    \"USDYER\":250.125028,\n" +
            "    \"USDZAR\":14.492754,\n" +
            "    \"USDZMK\":5156.097801,\n" +
            "    \"USDZMW\":9.509989,\n" +
            "    \"USDZWL\":322.387247\n" +
            "  }\n" +
            "}";

    public static void getCountries(final Listener l, Context c) {
        JsonObjectRequest requisicao =
                new JsonObjectRequest(
                        Request.Method.GET,
                        URL_COUNTRIES,
                        (String)null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                if(response.has("currencies")) {
                                    try {
                                        JSONObject currencies = (JSONObject) response.get("currencies");
                                        //saveCuntries(l, currencies);
                                        //l.onGetCountriesNames(saveCuntries(currencies));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else {
                                    //tvCotacao.setText("Não foi possível obter a cotação!");
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //tvCotacao.setText("Não foi possível obter a cotação!");
                            }
                        }

                );

        RequestQueue fila = Volley.newRequestQueue(c);
        fila.add(requisicao);

        try {
            JSONObject jo = new JSONObject(initialCoutries);
            if(jo.has("currencies")) {
                try {
                    JSONObject currencies = (JSONObject) jo.get("currencies");
                    l.onGetCountriesNames(saveCuntries(c,currencies));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                //tvCotacao.setText("Não foi possível obter a cotação!");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static List<CountryItem> saveCuntries(Context c, JSONObject response) throws JSONException {

        List<CountryItem> countryDataList = new ArrayList<CountryItem>();

        Iterator<?> iterator = response.keys();
        while (iterator.hasNext()) {
            String obj = iterator.next().toString();
            String url="";

            //North America
            if (obj.equals("USD"))  //USA
                url = "US";
            else if (obj.equals("MXN"))  //Mexico
                url = "MX";
            else if (obj.equals("CAD"))  //Canada
                url = "CA";
                //CENTRAL AMERICA
            else if (obj.equals("BZD"))  //Belize
                url = "BZ";
            else if (obj.equals("SVC"))  //El Salvador
                url = "SV";
            else if (obj.equals("GTQ"))  //Guatemala
                url = "GT";
            else if (obj.equals("HNL"))  //Honduras
                url = "HN";
            else if (obj.equals("NIO"))  //Nicaragua
                url = "NI";
            else if (obj.equals("PAB"))  //Panama
                url = "PA";
                //SOUTH AMERICA
            else if (obj.equals("ARS"))  //Argentina
                url = "AR";
            else if (obj.equals("BOB"))  //Bolivia
                url = "BO";
            else if (obj.equals("BRL"))  //Brasil
                url = "BR";
            else if (obj.equals("CLP"))  //Chile
                url = "CL";
            else if (obj.equals("COP"))  //Colombia
                url = "CO";
                //else if (obj.equals("USD"))  //Eccuador
                //    url = "EC";
            else if (obj.equals("FKP"))  //Ilhas Malvinas
                url = "FK";
                //else if (obj.equals("EUR"))  //Guiana Francesa
                //    url = "FR";
            else if (obj.equals("GYD"))  //Guiné
                url = "GY";
            else if (obj.equals("PYG"))  //Paraguai
                url = "PY";
            else if (obj.equals("PEN"))  //Peru
                url = "PE";
            else if (obj.equals("SRD"))  //Surinami
                url = "SR";
            else if (obj.equals("UYU"))  //Uruguai
                url = "UY";
            else if (obj.equals("VEF"))  //Venezuela
                url = "VE";
            //BITCOIN
            else if (obj.equals("BTC"))  //Venezuela
                url = "";
            else
                url = obj.substring(0,2);

            //if (obj.equals("BRL") || obj.equals("USD"))
                countryDataList.add(new CountryItem(c, response.getString(obj),obj,url));
        }
        return countryDataList;
    }

    public static List<CountryItem> getDefaultCountries(Context c) {

        try {
            JSONObject jo = new JSONObject(initialCoutries);
            if(jo.has("currencies")) {
                try {
                    JSONObject currencies = (JSONObject) jo.get("currencies");
                    return saveCuntries(c, currencies);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void getCurrencies(final Listener l, final Context c) {
        JsonObjectRequest requisicao =
                new JsonObjectRequest(
                        Request.Method.GET,
                        URL_CURRENCIES,
                        (String)null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                if(response.has("timestamp")) {
                                    FileOperations.writeCurrencyJson(c,response.toString());
                                    try {
                                        timeStamp = response.getLong("timestamp");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if(response.has("quotes")) {
                                    try {
                                        JSONObject quotes = (JSONObject) response.get("quotes");

                                        l.onGetCurrencyCompleted(saveRates(quotes));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else {
                                    //tvCotacao.setText("Não foi possível obter a cotação!");
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //tvCotacao.setText("Não foi possível obter a cotação!");
                            }
                        }

                );

        RequestQueue fila = Volley.newRequestQueue(c);
        fila.add(requisicao);
    }

    private static List<RateData> saveRates(JSONObject response) throws JSONException {

        List<RateData> rateDataList = new ArrayList<RateData>();

        Iterator<?> iterator = response.keys();
        while (iterator.hasNext()) {
            String obj = iterator.next().toString();
            rateDataList.add(new RateData(obj,response.getDouble(obj)));
        }
        return rateDataList;
    }

    public static List<RateData> getDefaultRates(Context c) {

        String currencyJSON = FileOperations.readCurrencyJson(c);

        if (currencyJSON==null)
            currencyJSON = initialRates;

        try {
            JSONObject jo = new JSONObject(currencyJSON);
            if(jo.has("timestamp")) {
                timeStamp = jo.getLong("timestamp");
            }
            if(jo.has("quotes")) {
                try {
                    JSONObject currencies = (JSONObject) jo.get("quotes");
                    return saveRates(currencies);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                //tvCotacao.setText("Não foi possível obter a cotação!");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static long getTimeStamp() {
        return timeStamp*1000L;
    }

    public interface Listener {
        void onGetCurrencyCompleted(List<RateData> rd);
        void onGetCountriesNames(List<CountryItem> ci);
    }
}
