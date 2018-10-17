package net.chrysaetos.myreports;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

public class mobileverification extends AppCompatActivity {
    EditText etPhoneno;
    CountryCodePicker countryCodePicker;
    Button pbContinue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobileverification);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        etPhoneno = (EditText) findViewById(R.id.eTphnNo);
        pbContinue = (Button) findViewById(R.id.pbcontinue) ;

        countryCodePicker = (CountryCodePicker) findViewById(R.id.ccp);

        etPhoneno.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
//                if(s.length() != 0)
//                    field2.setText("");
                if(s.length() >= 10)
                    pbContinue.setEnabled(true);
                else
                    pbContinue.setEnabled(false);
            }
        });


        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
//                Toast.makeText( WelcomeActivity.this, "This is from OnCountryChangeListener. \n Country updated to " + countryCodePicker.getSelectedCountryName() + "(" + countryCodePicker.getSelectedCountryCodeWithPlus() + ")", Toast.LENGTH_SHORT).show();
                etPhoneno.setText(countryCodePicker.getSelectedCountryCodeWithPlus());
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public void otpreceive(View v)
    {
        Intent i = new Intent(this, receiveotp.class);
        i.putExtra("phoneNo",countryCodePicker.getSelectedCountryCodeWithPlus()+etPhoneno.getText());
        startActivity(i);
        finish();
    }


}
