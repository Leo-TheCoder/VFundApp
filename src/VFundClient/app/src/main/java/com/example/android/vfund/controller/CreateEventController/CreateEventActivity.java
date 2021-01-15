package com.example.android.vfund.controller.CreateEventController;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.android.vfund.R;
import com.example.android.vfund.controller.QueryUtils;
import com.example.android.vfund.model.FundraisingEvent;
import com.example.android.vfund.model.User;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<FundraisingEvent> {

    private FundraisingEvent mEvent;
    private User mUser;
    private static final int ID_FETCH_CREATE_EVENT = 3;
    private static final String EVENT_CREATE_REQUEST_URL = "http://10.0.2.2:8080/api/events/createevent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        mUser = bundle.getParcelable("user");

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

                TextInputEditText contents;
                contents = secondPage.findViewById(R.id.txtName);
                contents.setText(mUser.get_name());

                contents = secondPage.findViewById(R.id.txtPhone);
                contents.setText(mUser.get_phone());
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
                AutoCompleteTextView autoCompleteTextView;

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

                autoCompleteTextView = firstPage.findViewById(R.id.autoComplete);
                eventCategory = autoCompleteTextView.getText().toString();

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

                autoCompleteTextView = secondPage.findViewById(R.id.autoComplete);
                bank = autoCompleteTextView.getText().toString();

                CheckBox checkBox = lastPage.findViewById(R.id.checkBoxTerms);
                boolean agreedTerm = checkBox.isChecked();

                if(eventName.length() < 1 || eventStory.length() < 1 || eventMoney.length() < 1 ||
                        eventDate.length() < 1 || name.length() < 1 || eventCategory.length() < 1 ||
                        cardNumber.length() < 1 || address.length() < 1 || phone.length() < 1 || bank.length() < 1) {
                    Toast.makeText(getBaseContext(), "Cần phải điền mọi thông tin!", Toast.LENGTH_SHORT).show();
                    Log.e("TEST", "GET HERE!x1");
                }
                else if(Float.parseFloat(eventMoney) < 100000) {
                    Toast.makeText(getBaseContext(), "Tiền cần quyên góp ít nhất 100,000đ",Toast.LENGTH_SHORT).show();
                }
                else if(!agreedTerm) {
                    Toast.makeText(getBaseContext(), "Cần phải đồng ý với điều khoản!", Toast.LENGTH_SHORT).show();
                }
                else {
                    mEvent = new FundraisingEvent(-1,eventName,eventStory, deadline,
                            true, Float.parseFloat(eventMoney), 0 );
                    mEvent.set_owner(mUser);
                    createEventToServer();
                }
            }
        });
    }

    private void createEventToServer() {
        Log.e("TEST", "CLICKED");
        LoaderManager.getInstance(this).initLoader(ID_FETCH_CREATE_EVENT,null,this);
    }

    @NonNull
    @Override
    public Loader<FundraisingEvent> onCreateLoader(int id, @Nullable Bundle args) {
        return new CreateEventAsyncTaskLoader(this, mEvent);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<FundraisingEvent> loader, FundraisingEvent data) {
        Intent intent = getIntent();
        Bundle newEventBundle = new Bundle();
        mEvent = data;
        newEventBundle.putParcelable("event", mEvent);
        intent.putExtras(newEventBundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<FundraisingEvent> loader) {
        finish();
    }

    private static class CreateEventAsyncTaskLoader extends AsyncTaskLoader<FundraisingEvent> {
        private FundraisingEvent mNewEvent = null;
        public CreateEventAsyncTaskLoader(@NonNull Context context, FundraisingEvent event) {
            super(context);
            mNewEvent = event;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }

        @Nullable
        @Override
        public FundraisingEvent loadInBackground() {
            Log.e("TEST", "GET HERE!");
            FundraisingEvent event;
            event = QueryUtils.createEventToServer(EVENT_CREATE_REQUEST_URL, mNewEvent);
            return event;
        }
    }
}