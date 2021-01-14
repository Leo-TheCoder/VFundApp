package com.example.android.vfund.controller.CreateEventController;

import android.app.DatePickerDialog;
import android.os.Bundle;

import com.example.android.vfund.R;
import com.example.android.vfund.controller.QueryUtils;
import com.example.android.vfund.model.FundraisingEvent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.AsyncTaskLoader;

import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateEventActivity extends AppCompatActivity {

    private FundraisingEvent mEvent;
    private static final String EVENT_CREATE_REQUEST_URL = "http://10.0.2.2:8080/api/events/createevent";

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
                String deadline = eventDate;
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                     deadline = FundraisingEvent.inputFormat.format(simpleDateFormat.parse(eventDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

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

                if(eventName.length() < 1 || eventStory.length() < 1 || eventMoney.length() < 1 ||
                        eventCategory.length() < 1 || eventDate.length() < 1 || name.length() < 1 ||
                        cardNumber.length() < 1 || address.length() < 1 || phone.length() < 1 || bank.length() < 1) {
                    Toast.makeText(getBaseContext(), "All infomation must be filled!", Toast.LENGTH_SHORT).show();
                }
                else {
                    mEvent = new FundraisingEvent(-1,eventName,eventStory, deadline,
                            true, Float.parseFloat(eventMoney), 0 );


                }

                finish();
            }
        });
    }

    public void makeHttpRequestCreate() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                StringBuilder requestUrl = new StringBuilder(EVENT_CREATE_REQUEST_URL);
                URL url = QueryUtils.createUrl("");
            }
        });
    }
}