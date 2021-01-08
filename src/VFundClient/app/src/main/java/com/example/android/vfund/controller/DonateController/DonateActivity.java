package com.example.android.vfund.controller.DonateController;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.android.vfund.R;
import com.example.android.vfund.model.FundraisingEvent;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DonateActivity extends AppCompatActivity implements View.OnClickListener {

    private FundraisingEvent mEvent;
    private Button btnDonate;
    private TextInputLayout txtMoney;
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
            float moneyDonate = Float.parseFloat(txtMoney.getEditText().getText().toString());
            mEvent.getDonate(moneyDonate);
            Intent intent = getIntent();
            Bundle bundle = new Bundle();
            bundle.putParcelable("event", mEvent);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);

            Toast.makeText(this, "Donate Successfully", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}