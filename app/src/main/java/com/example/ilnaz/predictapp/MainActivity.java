package com.example.ilnaz.predictapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText query;
    private TextView textView;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        query = (EditText) findViewById(R.id.edit_query);
        textView = (TextView) findViewById(R.id.predict);
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://predictor.yandex.net/")
                .build();
        query.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString();
                str = str.replace(' ', '+');
                final String finalStr = str;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Call<PredictResp> call = retrofit
                                .create(PredictService.class)
                                .complete(getString(R.string.API_KEY), "tt", finalStr);
                        Callback<PredictResp> callback = new Callback<PredictResp>() {
                            @Override
                            public void onResponse(Call<PredictResp> call, Response<PredictResp> response) {
                                textView.setText(response.body().getText().toString());
                            }

                            @Override
                            public void onFailure(Call<PredictResp> call, Throwable t) {
                                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        };
                        call.enqueue(callback);
                    }
                }, 100);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}
