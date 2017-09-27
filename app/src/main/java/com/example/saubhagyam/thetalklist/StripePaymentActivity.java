package com.example.saubhagyam.thetalklist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.saubhagyam.thetalklist.StripePaymentGatewayIntegrationFolder.ProgressDialogFragment;
import com.example.saubhagyam.thetalklist.StripePaymentGatewayIntegrationFolder.RetrofitFactory;
import com.example.saubhagyam.thetalklist.StripePaymentGatewayIntegrationFolder.StripeService;
import com.parse.FunctionCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Source;
import com.stripe.android.model.SourceCardData;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class StripePaymentActivity extends AppCompatActivity {
    // client's test key
//    public static final String  PUBLISHABLE_KEY = "pk_test_lY8qjvjuPEEyX8mX1ovIEsN7";



//        public static final String  PUBLISHABLE_KEY = "pk_test_m2095bSj8vVA0n55nBjcBRDH";
    public static final String  PUBLISHABLE_KEY = "pk_live_cZQHiFEshZyEHQrIzyqc2rA9";
    public static final String APPLICATION_ID = "RKNck9SdN6sqcznBvy5lqnN2ln1FrrSabNcq8YEK";
    public static final String CLIENT_KEY = "zWtkaYFS0Ia91jKkgmIHJql30cARcrDmKUGAXLTY";
    public static final String BACK4PAPP_API = "https://parseapi.back4app.com/";
    private CardInputWidget mCardInputWidget;
    private ProgressDialogFragment mProgressDialogFragment;
    private long mPrice;
    private Stripe mStripe;
    private CompositeSubscription mCompositeSubscription;
    Card card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_payment);

        mPrice=100;

        progress=new ProgressDialog(this);



        mCardInputWidget= (CardInputWidget) findViewById(R.id.card_input_widget);

        mCompositeSubscription = new CompositeSubscription();
       /* Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(APPLICATION_ID)
                .clientKey(CLIENT_KEY)
                .server(BACK4PAPP_API).build());
        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);*/

        final FragmentManager fragmentManager=getSupportFragmentManager();

        mStripe = new Stripe(this,PUBLISHABLE_KEY);


        mProgressDialogFragment =
                ProgressDialogFragment.newInstance(R.string.completing_purchase);

        Button purchase = (Button) findViewById(R.id.purchase);


        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                   /* Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                            .applicationId(APPLICATION_ID)
                            .clientKey(CLIENT_KEY)
                            .server(BACK4PAPP_API).build());
                    Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);*/
                    card = mCardInputWidget.getCard();
                    if (card == null) {
                        displayError("Card Input Error");
                        return;
                    } else buy();
                }catch (IllegalStateException e){
                    Toast.makeText(StripePaymentActivity.this, "Reopen application", Toast.LENGTH_SHORT).show();
                  /*  Intent i=new Intent(getApplicationContext(),StripePaymentActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);*/
                    /*Parse.
                    Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
                    card = mCardInputWidget.getCard();
                    if (card == null) {
                        displayError("Card Input Error");
                        return;
                    } else buy();*/
                }




              /*  mStripe.createToken(card, PUBLISHABLE_KEY, new TokenCallback() {
                    public void onSuccess(Token token) {
                        // TODO: Send Token information to your backend to initiate a charge
                        Toast.makeText(getApplicationContext(), "Token created: " + token.getId(), Toast.LENGTH_LONG).show();
                       *//* tok = token;
                        new StripeCharge(token.getId()).execute();*//*
                       buy();
                    }

                    public void onError(Exception error) {
                        Log.d("Stripe", error.getLocalizedMessage());
                    }
                });

                Toast.makeText(StripePaymentActivity.this, "before buy method call", Toast.LENGTH_SHORT).show();
                final SourceParams cardParams = SourceParams.createCardParams(card);
                Observable<Source> cardSourceObservable =
                        Observable.fromCallable(new Callable<Source>() {
                            @Override
                            public Source call() throws Exception {
                                return mStripe.createSourceSynchronous(
                                        cardParams,
                                        PUBLISHABLE_KEY);
                            }
                        });

                mCompositeSubscription.add(cardSourceObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(
                                new Action0() {
                                    @Override
                                    public void call() {
                                        mProgressDialogFragment.show(fragmentManager, "progress");
                                    }
                                })
                        .subscribe(
                                new Action1<Source>() {
                                    @Override
                                    public void call(Source source) {
                                        proceedWithPurchaseIf3DSCheckIsNotNecessary(source);
                                    }
                                },
                                new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        if (mProgressDialogFragment != null) {
                                            mProgressDialogFragment.dismiss();
                                        }
                                        displayError(throwable.getLocalizedMessage());
                                    }
                                }));*/



//                buy();
            }
        });


    }


    private void buy(){
        boolean validation = card.validateCard();
        if(validation){
            startProgress("Validating Credit Card");
            new Stripe(this,PUBLISHABLE_KEY).createToken(
                    card,
                    PUBLISHABLE_KEY,
                    new TokenCallback() {
                        @Override
                        public void onError(Exception error) {
                            Log.d("Stripe",error.toString());
                        }

                        @Override
                        public void onSuccess(Token token) {
                            finishProgress();
//                            charge(token);
                                    SharedPreferences paymentPref=getSharedPreferences("loginStatus",Context.MODE_PRIVATE);
                            final int money=paymentPref.getInt("ammount",0);


                           /* StripePaymentActivity stripePaymentActivity=new StripePaymentActivity();
                            startActivity(new Intent(getApplicationContext(),stripePaymentActivity.getClass()));*/

//                            Toast.makeText(StripePaymentActivity.this, "stripe token "+token.getId(), Toast.LENGTH_SHORT).show();

                            String URL="https://www.thetalklist.com/api/stripe_payment?access_token="+token.getId()+"&id="+getSharedPreferences("loginStatus",MODE_PRIVATE).getInt("id",0)+"&amount="+money;
                            Log.e("stripe  url",URL);

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {


                                    try {
                                        JSONObject resObj=new JSONObject(response);
                                        if (resObj.getInt("status")==0){
                                            Toast.makeText(StripePaymentActivity.this, "Payment Successful!", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(StripePaymentActivity.this, money+" Credits Added!", Toast.LENGTH_SHORT).show();
                                            Log.e("payment response",response);
                                            startActivity(new Intent(getApplicationContext(),SettingFlyout.class));
                                        }
                                    else {
                                            Toast.makeText(StripePaymentActivity.this, "Error in credit card entry.", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }





                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Toast.makeText(StripePaymentActivity.this, "Stripe Payment error. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 15, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                            queue.add(stringRequest);
                        }
                    });
        } else if (!card.validateNumber()) {
            Toast.makeText(this, "Card details invalid", Toast.LENGTH_SHORT).show();
            Log.d("Stripe","The card number that you entered is invalid");
        } else if (!card.validateExpiryDate()) {
            Toast.makeText(this, "Card details invalid", Toast.LENGTH_SHORT).show();
            Log.d("Stripe","The expiration date that you entered is invalid");
        } else if (!card.validateCVC()) {
            Toast.makeText(this, "Card details invalid", Toast.LENGTH_SHORT).show();
            Log.d("Stripe","The CVC code that you entered is invalid");
        } else {
            Toast.makeText(this, "Card details invalid", Toast.LENGTH_SHORT).show();
            Log.d("Stripe","The card details that you entered are invalid");
        }
    }

    private void charge(Token cardToken){
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("itemName", "test");
        params.put("cardToken", cardToken.getId());
        params.put("name","Dominic Wong");
        params.put("email","dominwong4@gmail.com");
        params.put("address","HIHI");
        params.put("zip","99999");
        params.put("city_state","CA");
        startProgress("Purchasing Item");
        ParseCloud.callFunctionInBackground("purchaseItem", params, new FunctionCallback<Object>() {
            public void done(Object response, ParseException e) {
                finishProgress();
                if (e == null) {
                    Log.d("Cloud Response", "There were no exceptions! " + response.toString());
                    Toast.makeText(getApplicationContext(),
                            "Item Purchased Successfully ",
                            Toast.LENGTH_LONG).show();
                } else {
                    Log.d("Cloud Response", "Exception: " + e);
                    Toast.makeText(getApplicationContext(),
                            e.getMessage().toString(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private ProgressDialog progress;
    private void startProgress(String title){
        progress.setTitle(title);
        progress.setMessage("Please Wait");
        progress.show();
    }
    private void finishProgress(){
        progress.dismiss();
    }



    private void proceedWithPurchaseIf3DSCheckIsNotNecessary(Source source) {
        if (source == null || !Source.CARD.equals(source.getType())) {
            displayError("Something went wrong - this should be rare");
            return;
        }

        SourceCardData cardData = (SourceCardData) source.getSourceTypeModel();
        if (SourceCardData.REQUIRED.equals(cardData.getThreeDSecureStatus())) {
            // In this case, you would need to ask the user to verify the purchase.
            // You can see an example of how to do this in the 3DS example application.
            // In stripe-android/example.
        } else {
            // If 3DS is not required, you can charge the source.
            completePurchase(source);
        }
    }


    private void completePurchase(Source source) {
        Retrofit retrofit = RetrofitFactory.getInstance();
        StripeService stripeService = retrofit.create(StripeService.class);
        Observable<Void> stripeResponse = stripeService.createQueryCharge(mPrice, source.getId());

        mCompositeSubscription.add(stripeResponse
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnUnsubscribe(
                        new Action0() {
                            @Override
                            public void call() {
                                if (mProgressDialogFragment != null) {
                                    mProgressDialogFragment.dismiss();
                                    final PopupWindow popupWindow=new PopupWindow(getApplicationContext());
                                    popupWindow.setOutsideTouchable(false);
                                    View view=getLayoutInflater().inflate(R.layout.cashout_layout,null);
                                    popupWindow.showAtLocation(view, Gravity.CENTER,0,0);

                                    Handler handler = new Handler();

                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            popupWindow.dismiss();
                                        }
                                    }, 1000);
                                }
                            }
                        })
                .subscribe(
                        new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                finishCharge();
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                displayError(throwable.getLocalizedMessage());
                            }
                        }));
    }

    private void displayError(String errorMessage) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage(errorMessage);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent I =new Intent(getApplicationContext(), StripePaymentActivity.class);
                        startActivity(I);
                    }
                });
        alertDialog.show();
    }

    private void finishCharge() {

        finish();
    }
}
