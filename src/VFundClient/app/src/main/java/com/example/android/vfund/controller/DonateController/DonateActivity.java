package com.example.android.vfund.controller.DonateController;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.android.vfund.R;
import com.example.android.vfund.controller.QueryUtils;
import com.example.android.vfund.model.FundraisingEvent;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DonateActivity extends AppCompatActivity implements View.OnClickListener {

    private FundraisingEvent mEvent;
    private Button btnDonate;
    private TextInputLayout txtMoney;
    private AutoCompleteTextView txtPaymentMethod;
    private TextInputLayout txtCardNumber;

    private static final String DONATE_REQUEST = "http://10.0.2.2:8080/api/events/patchevent/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        Intent callingIntent = getIntent();
        Bundle eventBundle = callingIntent.getExtras();
        mEvent = eventBundle.getParcelable("event");

        btnDonate = (Button)findViewById(R.id.btnDonate_Donate);
        btnDonate.setOnClickListener(this);
        txtMoney = (TextInputLayout)findViewById(R.id.txtMoneyField_Donate);
        txtPaymentMethod = (AutoCompleteTextView)findViewById(R.id.autoComplete) ;
        txtCardNumber = (TextInputLayout)findViewById(R.id.textField);

        ArrayList<String> items = new ArrayList<String> (Arrays.asList("Thẻ ngân hàng", "Visa/Mastercard", "Momo", "ZaloPay"));
        ArrayAdapter adapter = new ArrayAdapter(getBaseContext(), R.layout.list_item, items);

        AutoCompleteTextView autoCompleteTxt = findViewById(R.id.autoComplete);
        autoCompleteTxt.setAdapter(adapter);

        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FrameLayout selectedLayout;
                ArrayList<FrameLayout> layouts = new ArrayList<FrameLayout>();
                layouts.add((FrameLayout)findViewById(R.id.layout_creditcard));
                layouts.add((FrameLayout)findViewById(R.id.layout_creditcard));
                layouts.add((FrameLayout)findViewById(R.id.layout_momo));
                layouts.add((FrameLayout)findViewById(R.id.layout_zalopay));
                for (FrameLayout layout : layouts)
                    layout.setVisibility(View.GONE);
                selectedLayout = layouts.get(position);
                selectedLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnDonate.getId()) {
            String moneyAmountStr = txtMoney.getEditText().getText().toString();
            if(moneyAmountStr.length() < 1) {
                Toast.makeText(getApplicationContext(),"Số tiền quyên góp không hợp lệ", Toast.LENGTH_SHORT).show();
            }
            else if(txtCardNumber.getEditText().getText().toString().length() < 1){
                Toast.makeText(getApplicationContext(),"Phương thức quyên góp không hợp lệ", Toast.LENGTH_SHORT).show();
            }
            else {
                float moneyDonate = Float.parseFloat(txtMoney.getEditText().getText().toString());
                if(moneyDonate <= 0) {
                    Toast.makeText(getApplicationContext(), "Số tiền quyên góp không hợp lệ", Toast.LENGTH_SHORT).show();
                }
                else if(txtPaymentMethod.getText().toString().length() < 1){
                    Toast.makeText(getApplicationContext(), "Cần chọn phương thức chuyển tiền ", Toast.LENGTH_SHORT).show();
                }
                else {

                    mEvent.getDonate(moneyDonate);
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("event", mEvent);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);

                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    Handler donateHandler = new Handler(Looper.getMainLooper());
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            boolean isRequest = false;
                            StringBuilder request = new StringBuilder();
                            request.append(DONATE_REQUEST).append("E").append(mEvent.get_eventID());
                            URL requestUrl = QueryUtils.createUrl(request.toString());
                            try {
                                isRequest = QueryUtils.makeHttpRequestUpdateEvent(requestUrl, (int)mEvent.get_currentGain());
                            } catch (IOException e) {
                                Log.e("LOZ", "LOZ");
                                e.printStackTrace();
                            }
                            if(isRequest) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Donate Successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                            else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Donate Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            }



        }

    }
}