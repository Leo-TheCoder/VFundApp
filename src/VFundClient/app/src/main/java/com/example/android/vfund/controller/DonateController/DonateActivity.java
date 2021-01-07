package com.example.android.vfund.controller.DonateController;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;

import com.example.android.vfund.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DonateActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);



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

    }
}