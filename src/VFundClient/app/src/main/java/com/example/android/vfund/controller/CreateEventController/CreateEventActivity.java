package com.example.android.vfund.controller.CreateEventController;

import android.os.Bundle;

import com.example.android.vfund.R;
import com.example.android.vfund.model.FundraisingEvent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateEventActivity extends AppCompatActivity {

    LinearLayout firstPage, secondPage, lastPage;
    TextInputLayout txtEventName, txtEventStory, txtEventMoneyAmount, txtUserName, txtCardNumber;
    TextInputLayout txtAddress, txtPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        FundraisingEvent event = new FundraisingEvent();
        txtEventName = findViewById(R.id.txtEventName);
        txtEventStory = findViewById(R.id.txtEventStory);
        txtEventMoneyAmount = findViewById(R.id.txtEventMoneyAmount);
        txtUserName = findViewById(R.id.txtUserName);
        txtCardNumber = findViewById(R.id.txtCardNumber);
        txtAddress = findViewById(R.id.txtAddress);
        txtPhone = findViewById(R.id.txtPhone);
        firstPage = findViewById(R.id.firstPage);
        secondPage = findViewById(R.id.secondPage);
        lastPage = findViewById(R.id.lastPage);

        ArrayList<String> categories = new ArrayList<String> (Arrays.asList("Sức khỏe", "Âm nhạc", "Giáo dục", "Thể thao"));
        ArrayAdapter categoryAdapter = new ArrayAdapter(getBaseContext(), R.layout.list_item, categories);

        ArrayList<String> banks = new ArrayList<String> (Arrays.asList("ACB", "Agribank", "ANZ", "BIDV", "Eximbank", "HSBC", "VietCapital Bank", "Sacombank", "Techcombank", "Vietcombank"));
        ArrayAdapter bankAdapter = new ArrayAdapter(getBaseContext(), R.layout.list_item, banks);

        AutoCompleteTextView autoCompleteTxt = firstPage.findViewById(R.id.autoComplete);
        autoCompleteTxt.setAdapter(categoryAdapter);

        autoCompleteTxt = secondPage.findViewById(R.id.autoComplete);
        autoCompleteTxt.setAdapter(bankAdapter);

        ImageButton btnNext = firstPage.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstPage.setVisibility(View.GONE);
                secondPage.setVisibility(View.VISIBLE);
            }
        });

        btnNext = secondPage.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondPage.setVisibility(View.GONE);
                lastPage.setVisibility(View.VISIBLE);
            }
        });

        btnNext = lastPage.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* to-do: complete activity and send event to database */
                finish();
            }
        });
    }
}