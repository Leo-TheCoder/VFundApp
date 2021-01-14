package com.example.android.vfund.controller.CreateEventController;

import android.app.DatePickerDialog;
import android.os.Bundle;

import com.example.android.vfund.R;
import com.example.android.vfund.model.FundraisingEvent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

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

        final LinearLayout firstPage = findViewById(R.id.firstPage);
        final LinearLayout secondPage = findViewById(R.id.secondPage);
        final LinearLayout lastPage = findViewById(R.id.lastPage);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstPage.getVisibility() == View.VISIBLE)
                    finish();
                else if (secondPage.getVisibility() == View.VISIBLE)
                {
                    firstPage.setVisibility(View.VISIBLE);
                    secondPage.setVisibility(View.INVISIBLE);
                }
                else if (lastPage.getVisibility() == View.VISIBLE)
                {
                    secondPage.setVisibility(View.VISIBLE);
                    lastPage.setVisibility(View.INVISIBLE);
                }
            }
        });

        TextInputEditText eText;
        eText=(TextInputEditText) firstPage.findViewById(R.id.txtDate);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(CreateEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                TextInputEditText eText;
                                cldr.set(year, monthOfYear, dayOfMonth);
                                String pattern= "dd-MM-yyyy";
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                                String date = simpleDateFormat.format(cldr.getTime());
                                eText=(TextInputEditText) firstPage.findViewById(R.id.txtDate);
                                eText.setText(date);
                                //eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

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
                firstPage.setVisibility(View.INVISIBLE);
                secondPage.setVisibility(View.VISIBLE);
            }
        });

        btnNext = secondPage.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondPage.setVisibility(View.INVISIBLE);
                lastPage.setVisibility(View.VISIBLE);
            }
        });

        btnNext = lastPage.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* to-do: complete activity and send event to database */
                String eventName, eventStory, eventMoney, eventDate, eventCategory;
                TextInputEditText contents;

                //first page contents
                contents = firstPage.findViewById(R.id.txtEventName);
                eventName = contents.getText().toString();

                contents = firstPage.findViewById(R.id.txtEventStory);
                eventStory = contents.getText().toString();

                contents = firstPage.findViewById(R.id.txtEventMoneyAmount);
                eventMoney = contents.getText().toString();

                contents = firstPage.findViewById(R.id.txtDate);
                eventDate = contents.getText().toString();

                contents = firstPage.findViewById(R.id.autoComplete);
                eventCategory = contents.getText().toString();

                String name, cardNumber, address, phone, bank;
                //second page contents
                contents = secondPage.findViewById(R.id.txtName);
                name = contents.getText().toString();

                contents = secondPage.findViewById(R.id.txtCardNumber);
                cardNumber = contents.getText().toString();

                contents = secondPage.findViewById(R.id.txtAddress);
                address = contents.getText().toString();

                contents = secondPage.findViewById(R.id.txtPhone);
                phone = contents.getText().toString();

                contents = secondPage.findViewById(R.id.autoComplete);
                bank = contents.getText().toString();

                finish();
            }
        });


    }
}