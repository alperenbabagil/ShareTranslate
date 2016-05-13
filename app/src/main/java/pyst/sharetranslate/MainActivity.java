package pyst.sharetranslate;

import android.app.Activity;
import android.content.*;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.havafyhyky.gmjxaksqjr205421.AdConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.havafyhyky.gmjxaksqjr205421.AdListener;

import java.util.*;


public class MainActivity extends Activity {

    final WindowManager.LayoutParams paramsview = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT);


    Context c=this;
    String url="";
    JSONArray contacts = null;
    ArrayList<HashMap<String, String>> contactList;
    ListView lv;
    WindowManager windowManager;
    View ll;
    LinearLayout view;
    Display display;
    AutoCompleteTextView ac;
    String soldil="en";
    String sagdil="fr";
    String ceviritext="";
    GetContacts g;
    boolean taskflag = true;
    com.google.android.gms.ads.AdView gadview;
    String AD_UNIT_ID1 = "ca-app-pub-8664437664796355/1777585425";
    ImageView ikon;

    boolean ilk=false;



   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( event.getKeyCode()==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN) toastyap("vi");
        return super.onKeyDown(keyCode, event);
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        g=null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras2=null;

        g = new GetContacts();

        final SharedPreferences mSharedPrefs = getSharedPreferences("xmlFile",
                MODE_PRIVATE);
        final SharedPreferences.Editor mPrefsEditor = mSharedPrefs.edit();

        Locale enl= new Locale("en");
        String def=enl.getDisplayLanguage()+" - "+enl.getISO3Language();


        soldil=mSharedPrefs.getString("soldil",def);
        sagdil=mSharedPrefs.getString("sagdil", Locale.getDefault().getDisplayLanguage()+" - "+Locale.getDefault().getISO3Language());

        ArrayList <String> al=new ArrayList<>();

        if (mSharedPrefs.getInt("ilk",-1)==-1){
            String[] dil={"afr","ara","aze","bel","ben","bod","bos","bul","hrv","ces","dan","deu","ell","eng","est","eus","fas","fin","fra",
                    "hin","hun","hye","ind","gle","ita","jap","kat","kaz","kir","kor","lat","lao","lit","may","mkd","mon","ndl","nep","nor"
                    ,"pol","por","ron","rus","ser","slk","slv","som","spa","sqi","swe","tha","tur","uig","ukr","urd","vie","zho","zul"};

            String[] languages = Locale.getISOLanguages();



            int counter=0;
            //Map<String, Locale> localeMap = new HashMap<String, Locale>(languages.length);
            for (String language : languages) {


                Locale locale = new Locale(language);
                String s=locale.getISO3Language();
                for (int i=0;i<dil.length;i++){
                    if (s.equals(dil[i])){
                        counter++;
                        mPrefsEditor.putString(counter+"",locale.getDisplayLanguage()+" - "+s);
                        al.add(locale.getDisplayLanguage()+" - "+s);
                        mPrefsEditor.commit();
                    }
                }
            }

            toastyap(counter+" conter");


            mPrefsEditor.putInt("ilk",1);
            mPrefsEditor.commit();
        }
        else{
            for (int i=1;i<55;i++){
                al.add(mSharedPrefs.getString(i+"",""));
            }
        }



        contactList=new ArrayList<>();
        view=new LinearLayout(c){
            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {
                if ( event.getKeyCode()==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
                    toastyap("goodbye ♥");
                    finish();
                }
                return super.dispatchKeyEvent(event);

            }
        };

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();/// ekran boyutlarını alma

        DisplayMetrics  metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);


        //orientation = getResources().getConfiguration().orientation;





        paramsview.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        paramsview.x=0;
        paramsview.y=0;
        //paramsview.height= (int) Math.ceil(400 * metrics.density);
        paramsview.width=display.getWidth();




        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        ll=inflater.inflate(R.layout.goster, view, false);

        //AutoCompleteTextView ac=(AutoCompleteTextView) ll.findViewById(R.id.ac);
        LinearLayout rl=(LinearLayout)ll.findViewById(R.id.linlay);
        LinearLayout admob=(LinearLayout)ll.findViewById(R.id.rek);
        LinearLayout airpsuh=(LinearLayout)ll.findViewById(R.id.airpush);
        LinearLayout diller=(LinearLayout)ll.findViewById(R.id.dillerll);
        final ImageView im=(ImageView) ll.findViewById(R.id.kapa);
        lv=(ListView) ll.findViewById(R.id.listView);
        final EditText ed=(EditText) ll.findViewById(R.id.editText);
        ikon=(ImageView) ll.findViewById(R.id.ikon);


        ed.getLayoutParams().height=(int) Math.ceil(50 * metrics.density);
        admob.getLayoutParams().height=(int) Math.ceil(50 * metrics.density);
        airpsuh.getLayoutParams().height=(int) Math.ceil(60 * metrics.density);
        im.getLayoutParams().height=(int) Math.ceil(45 * metrics.density);
        ikon.getLayoutParams().height=(int) Math.ceil(45 * metrics.density);
        diller.getLayoutParams().height=(int) Math.ceil(50 * metrics.density);
        lv.getLayoutParams().height=(int) Math.ceil(200 * metrics.density);
        rl.getLayoutParams().height=(int) Math.ceil(400 * metrics.density);

        //ikon.setAnimation(AnimationUtils.loadAnimation(c, R.anim.rotate_around_center_point));










        //toastyap(localeMap.get("eng").getLanguage());





        Spinner solspinner = (Spinner) ll.findViewById(R.id.solspinner);
        Spinner sagspinner = (Spinner) ll.findViewById(R.id.sagspinner);

        spin_adapter sadapter=new spin_adapter(c,al);
        solspinner.setAdapter(sadapter);
        sagspinner.setAdapter(sadapter);
        //toastyap("soldil"+soldil);
        solspinner.setSelection(sadapter.getPosition(soldil));
        sagspinner.setSelection(sadapter.getPosition(sagdil));

        ed.clearFocus();
        im.requestFocus();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // createpage(hisclasslist.get(position).url,true,true);
                toastyap("The translated text copied to clipboard");


                android.content.ClipboardManager clipboardMgr = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied text", contactList.get(position).get("translation"));
                clipboardMgr.setPrimaryClip(clip);
            }


        });

        //toastyap("---"+Locale.getDefault().getDisplayLanguage());

        //sadapter.setDropDownViewResource(R.layout.spinner_row);

        /*ArrayAdapter<String> adapter = new ArrayAdapter<>
                (c, android.R.layout.simple_spinner_item,dizi);*/

       // adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        //spinner.setAdapter(adapter);
       // spinner.setSelection(adapter.getPosition("kelebek"));

       /* view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                toastyap("viv");
            }
        });

        im.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                toastyap("im");
            }
        });

        rl.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                toastyap("rl");
            }
        });

        diller.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                toastyap("diller");
            }
        });

        lv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                toastyap("lv");
            }
        });*/

        solspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // TODO Auto-generated method stub
                String selectedItem = parent.getItemAtPosition(pos).toString();


                soldil = selectedItem;
                mPrefsEditor.putString("soldil", selectedItem);
                mPrefsEditor.commit();
                if (ilk && !ceviritext.isEmpty()) cevir();
                ilk=true;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        sagspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // TODO Auto-generated method stub
                String selectedItem = parent.getItemAtPosition(pos).toString();
                /*Toast.makeText(MainActivity.this,
                        selectedItem + " is selected", Toast.LENGTH_SHORT)
                        .show();*/
                sagdil=selectedItem;
                mPrefsEditor.putString("sagdil", selectedItem);
                mPrefsEditor.commit();
                if (ilk && !ceviritext.isEmpty()) cevir();
                ilk=true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        solspinner.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(ed.getWindowToken(), 0);
                return false;
            }
        }) ;

        sagspinner.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(ed.getWindowToken(), 0);
                return false;
            }
        }) ;
/*
        ed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastyap("editakbayram");
                paramsview.softInputMode= WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
                imm.showSoftInput(ed,InputMethodManager.SHOW_IMPLICIT);
            }
        });*/



        ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    ceviritext=ed.getText().toString();
                    ed.clearFocus();
                    im.requestFocus();
                    cevir();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(ed.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

       /* ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {


            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    imm.hideSoftInputFromWindow(ed.getWindowToken(), 0);
                    toastyap("klac");
                    ceviritext=ed.getText().toString();
                    ed.clearFocus();
                    im.requestFocus();
                    cevir();
                    return true;
                }


                return false;
            }
        });*/

        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ceviritext=s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /*im.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ( keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_UP){
                    toastyap("bek");

                }
                //Toast.makeText(c,Integer.toString(new Random().nextInt(10000)),Toast.LENGTH_SHORT).show();


                if ( keyCode==KeyEvent.KEYCODE_MENU && event.getAction()==KeyEvent.ACTION_UP){
                    toastyap("set");
                }

                return false;
            }
        });*/




        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastyap("goodbye ♥");
                finish();
            }
        });

//        paramsview.softInputMode=32;

        view.addView(ll);

        windowManager.addView(view,paramsview);



        /*Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {



               // im.requestFocus();
               // Log.i("zaaaaa",getCurrentFocus().toString());
            }
        },500,500);*/

        //String[] keys = this.getResources().getStringArray(R.);

        AdConfig.setAppId(272440);  //setting appid.
        AdConfig.setApiKey("1412001861205421340"); //setting apikey

        com.havafyhyky.gmjxaksqjr205421.AdView adView=(com.havafyhyky.gmjxaksqjr205421.AdView) ll.findViewById(R.id.myAdView3);

        adView.setBannerType(com.havafyhyky.gmjxaksqjr205421.AdView.BANNER_TYPE_RICH_MEDIA);

        adView.setBannerAnimation(com.havafyhyky.gmjxaksqjr205421.AdView.ANIMATION_TYPE_FADE);

        adView.showMRinInApp(false);

        adView.setNewAdListener(new AdListener() {
            @Override
            public void onAdCached(AdConfig.AdType adType) {

            }

            @Override
            public void onIntegrationError(String s) {

            }

            @Override
            public void onAdError(String s) {

            }

            @Override
            public void noAdListener() {

            }

            @Override
            public void onAdShowing() {

            }

            @Override
            public void onAdClosed() {

            }

            @Override
            public void onAdLoadingListener() {

            }

            @Override
            public void onAdLoadedListener() {

            }

            @Override
            public void onCloseListener() {

            }

            @Override
            public void onAdExpandedListner() {

            }

            @Override
            public void onAdClickedListener() {

            }
        });
        adView.loadAd();

        LinearLayout reklamll=(LinearLayout) ll.findViewById(R.id.rek);

        gadview = new com.google.android.gms.ads.AdView(this);
        gadview.setAdSize(AdSize.SMART_BANNER);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("F89D4206B80FD589EA4B463644220E75").addTestDevice("E649B9B2AC045C9CDFC17D35A53EDE71").addTestDevice("20BD990D28E3CEDB1B3F89DB9769CAFE").addTestDevice("E76E4FA653B2C5C9C12BCB76E43C6095")
                .addTestDevice("F717FE4194A33293A15902E239575098").addTestDevice("7E7CD540FC648B1B5D603840E58973A2")
                .addTestDevice("E2B2CBFC95FAC8EE137BAAE45FC69A3B")

                .addTestDevice("FD90FC1DA24EDA037772B7BF933C1842")
                .addTestDevice("FD53CF0EB2DD06B6C8B4FAF5E4F26577")
                .addTestDevice("2E2CBF71CB6DBEF7335E97454E601F2D")
                .addTestDevice("74C504A179DAA3A53229D9F491FCBCA5")
                .build();
        gadview.setAdUnitId(AD_UNIT_ID1);
        gadview.loadAd(adRequest);

        reklamll.addView(gadview);


        Log.i("Bilgi", "qapaq2");
        try{
            extras2=getIntent().getExtras();
        }
        catch(NullPointerException e){
            // e.addSuppressed(e);
            e.printStackTrace();
        }

        if (extras2==null) {
            /*Toast.makeText(c, "nul", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(c,Settings.class);
            startActivity(i);*/
        }

        try {
            //Toast.makeText(c,getIntent().getAction(),Toast.LENGTH_SHORT).show();
            if (!extras2.getString(Intent.EXTRA_TEXT).equals("")) {
                 ceviritext=extras2.getString(Intent.EXTRA_TEXT);

                  Toast.makeText(c, ceviritext, Toast.LENGTH_SHORT).show();
                  ed.setText(ceviritext);

                  cevir();




            }
            //Toast.makeText(c,extras2.getString(Intent.EXTRA_TEXT)+55,Toast.LENGTH_SHORT).show();
            else Toast.makeText(c, "bos", Toast.LENGTH_SHORT).show();
            // if (extras2.getString("url").equals("salihGamyoncu3131")) Toast.makeText(c,"bohcıhtı",Toast.LENGTH_SHORT).show();

            //url=paylasimlagelenurl;



        }catch (NullPointerException e){
            e.printStackTrace();
           // finish();
        }



    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void toastyap(String s){
        Toast.makeText(c,s,Toast.LENGTH_SHORT).show();
    }

    public void cevir(){

        if (isOnline()){
            String soltemp=soldil.substring(soldil.length()-3,soldil.length());
            String sagtemp=sagdil.substring(sagdil.length()-3,sagdil.length());
            String sol=soltemp;
            String sag=sagtemp;

            switch (sagtemp){
                case "sqi":
                    sag="alb";
                    break;
                case "hye":
                    sag="arm";
                    break;
                case "eus":
                    sag="baq";
                    break;
                case "zho":
                    sag="chi";
                    break;
                case "ces":
                    sag="cze";
                    break;
                case "ndl":
                    sag="dut";
                    break;
                case "fra":
                    sag="fre";
                    break;
                case "kat":
                    sag="geo";
                    break;
                case "deu":
                    sag="ger";
                    break;
                case "ell":
                    sag="gre";
                    break;
                case "mkd":
                    sag="mac";
                    break;
                case "fas":
                    sag="per";
                    break;
                case "ron":
                    sag="rum";
                    break;
                case "slk":
                    sag="slo";
                    break;
                case "bod":
                    sag="tib";
                    break;
            }

            switch (soltemp){
                case "sqi":
                    sol="alb";
                    break;
                case "hye":
                    sol="arm";
                    break;
                case "eus":
                    sol="baq";
                    break;
                case "zho":
                    sol="chi";
                    break;
                case "ces":
                    sol="cze";
                    break;
                case "ndl":
                    sol="dut";
                    break;
                case "fra":
                    sol="fre";
                    break;
                case "kat":
                    sol="geo";
                    break;
                case "deu":
                    sol="ger";
                    break;
                case "ell":
                    sol="gre";
                    break;
                case "mkd":
                    sol="mac";
                    break;
                case "fas":
                    sol="per";
                    break;
                case "ron":
                    sol="rum";
                    break;
                case "slk":
                    sol="slo";
                    break;
                case "bod":
                    sol="tib";
                    break;
            }



            url="http://api.mymemory.translated.net/get?q="+Uri.encode(ceviritext)+"&langpair="+sol+Uri.encode("|")+sag;
            // toastyap("ceviriliyor");

            if (taskflag) {
                g.execute();
                taskflag = false;
            }
            if (g == null) {
                g = new GetContacts();
                g.execute();
            }
        }

        else toastyap("Please check your internet connection");




    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            ikon.startAnimation(AnimationUtils.loadAnimation(c, R.anim.rotate_around_center_point));

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            Service sh = new Service();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, Service.GET);

            Log.d("Response: ", "> " + jsonStr);

            contactList.clear();

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    contacts = jsonObj.getJSONArray("matches");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = null;
                        try {
                            c = contacts.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String sagment = c.getString("segment");
                        String translation = c.getString("translation");


                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        contact.put("sagment", sagment);
                        contact.put("translation", translation);


                        // adding contact to contact list
                        contactList.add(contact);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            /*if (pDialog.isShowing())
                pDialog.dismiss();
            *//**
             * Updating parsed JSON data into ListView
             * *//*
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, contactList,
                    R.layout.list_item, new String[] { TAG_NAME, TAG_EMAIL,
                    TAG_PHONE_MOBILE }, new int[] { R.id.name,
                    R.id.email, R.id.mobile });

            setListAdapter(adapter);*/



            /*for (int i=0;i<contactList.size();i++){
                Toast.makeText(c,contactList.get(i).get("translation"),Toast.LENGTH_LONG).show();
            }
            i
*/
            ikon.clearAnimation();
            tadapter a=new tadapter(contactList,c,display);
            a.notifyDataSetChanged();
            lv.setAdapter(a);
            g=null;
        }

    }



}
