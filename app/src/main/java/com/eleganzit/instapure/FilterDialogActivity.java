package com.eleganzit.instapure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.appyvet.materialrangebar.RangeBar;

import java.util.ArrayList;

public class FilterDialogActivity extends AppCompatActivity {


    private ImageView close;
    TextView apply,clear;

    ScrollView scroll_v;
    RangeBar rangeBar1;
    float left_pin_value=1;
    float right_pin_value=10;
    Spinner spinner1,spinner2;
    CheckBox jan,feb,mar,apr,may,jun,jul,aug,sep,oct,nov,dec;
    ArrayList<String> arrayList=new ArrayList<>();

    String select_months="",from_limit="1",to_limit="8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_dialog);

        close=findViewById(R.id.close);
        clear=findViewById(R.id.clear);
        apply=findViewById(R.id.apply);

        scroll_v=findViewById(R.id.scroll_v);
        rangeBar1=findViewById(R.id.rangebar1);
        spinner1=findViewById(R.id.spinner1);
        spinner2=findViewById(R.id.spinner2);
        jan=findViewById(R.id.jan);
        feb=findViewById(R.id.feb);
        mar=findViewById(R.id.mar);
        apr=findViewById(R.id.apr);
        may=findViewById(R.id.may);
        jun=findViewById(R.id.jun);
        jul=findViewById(R.id.jul);
        aug=findViewById(R.id.aug);
        sep=findViewById(R.id.sep);
        oct=findViewById(R.id.oct);
        nov=findViewById(R.id.nov);
        dec=findViewById(R.id.dec);

        rangeBar1.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                Log.d("ranghdfdf",leftPinIndex+"  "+leftPinValue+" <-> "+rightPinIndex+"  "+rightPinValue);
                spinner1.setSelection(leftPinIndex);
                spinner2.setSelection(rightPinIndex);
                left_pin_value=Float.valueOf(leftPinValue);
                right_pin_value=Float.valueOf(rightPinValue);
            }

            @Override
            public void onTouchStarted(RangeBar rangeBar) {

            }

            @Override
            public void onTouchEnded(RangeBar rangeBar) {

            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("ranghdfdf",i+"  <--"+l);

                rangeBar1.setRangePinsByValue(i+1,right_pin_value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("ranghdfdf",i+"  <--"+l);
                rangeBar1.setRangePinsByValue(left_pin_value,i+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                scrollToView(scroll_v,may);
                return false;
            }
        });

        spinner2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                scrollToView(scroll_v,may);
                return false;
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FilterDialogActivity.this,HomeActivity.class)
                        .putExtra("from","filter")
                        .putExtra("from_limit",from_limit+"")
                        .putExtra("to_limit",""+to_limit)
                        .putExtra("select_months",select_months+"")
                );
                overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
                HomeActivity.homeActivity.finish();
                finish();

            }
        });

        jan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    arrayList.add("1");
                }
                else
                {
                    arrayList.remove("1");
                }
            }
        });

        feb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    arrayList.add("2");
                }
                else
                {
                    arrayList.remove("2");
                }
            }
        });

        mar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    arrayList.add("3");
                }
                else
                {
                    arrayList.remove("3");
                }
            }
        });

        apr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    arrayList.add("4");
                }
                else
                {
                    arrayList.remove("4");
                }
            }
        });

        may.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    arrayList.add("5");
                }
                else
                {
                    arrayList.remove("5");
                }
            }
        });

        jun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    arrayList.add("6");
                }
                else
                {
                    arrayList.remove("6");
                }
            }
        });

        jul.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    arrayList.add("7");
                }
                else
                {
                    arrayList.remove("7");
                }
            }
        });

        aug.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    arrayList.add("8");
                }
                else
                {
                    arrayList.remove("8");
                }
            }
        });

        sep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    arrayList.add("9");
                }
                else
                {
                    arrayList.remove("9");
                }
            }
        });

        oct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    arrayList.add("10");
                }
                else
                {
                    arrayList.remove("10");
                }
            }
        });

        nov.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    arrayList.add("11");
                }
                else
                {
                    arrayList.remove("11");
                }
            }
        });

        dec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    arrayList.add("12");
                }
                else
                {
                    arrayList.remove("12");
                }
            }
        });

        spinner2.setSelection(9);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rangeBar1.setRangePinsByValue(1,10);
                spinner1.setSelection(0);
                spinner2.setSelection(9);
                jan.setChecked(false);
                feb.setChecked(false);
                mar.setChecked(false);
                apr.setChecked(false);
                may.setChecked(false);
                jun.setChecked(false);
                jul.setChecked(false);
                aug.setChecked(false);
                sep.setChecked(false);
                oct.setChecked(false);
                nov.setChecked(false);
                dec.setChecked(false);
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringBuilder sb = new StringBuilder();
                for(int i=0;i<arrayList.size();i++)
                {
                    Log.d("productsssssssss",arrayList.get(i)+"");

                    if (i==arrayList.size()-1)
                    {
                        sb.append(arrayList.get(i)).append("");
                    }
                    else {
                        sb.append(arrayList.get(i)).append(",");

                    }
                }

                from_limit=rangeBar1.getLeftPinValue();
                to_limit=rangeBar1.getRightPinValue();

                Log.d("uuuuuuuuuuu",sb.toString()+"");
                select_months=sb.toString();

                startActivity(new Intent(FilterDialogActivity.this,HomeActivity.class)
                    .putExtra("from","filter")
                        .putExtra("from_limit",from_limit+"")
                        .putExtra("to_limit",""+to_limit)
                        .putExtra("select_months",select_months+"")
                );
                overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
                HomeActivity.homeActivity.finish();
                finish();

            }
        });

    }

    private void scrollToView(final ScrollView scrollViewParent, final View view) {
        // Get deepChild Offset
        Point childOffset = new Point();
        getDeepChildOffset(scrollViewParent, view.getParent(), view, childOffset);
        // Scroll to child.
        scrollViewParent.smoothScrollTo(0, 280);

        Log.d("ghdfssdfsd",childOffset.y+"");
    }

    private void getDeepChildOffset(final ViewGroup mainParent, final ViewParent parent, final View child, final Point accumulatedOffset) {
        ViewGroup parentGroup = (ViewGroup) parent;
        accumulatedOffset.x += child.getLeft();
        accumulatedOffset.y += child.getTop();
        if (parentGroup.equals(mainParent)) {
            return;
        }
        getDeepChildOffset(mainParent, parentGroup.getParent(), parentGroup, accumulatedOffset);
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(FilterDialogActivity.this,HomeActivity.class)
                .putExtra("from","filter")
                .putExtra("from_limit",from_limit+"")
                .putExtra("to_limit",""+to_limit)
                .putExtra("select_months",select_months+"")
        );
        overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
        HomeActivity.homeActivity.finish();
        finish();

    }
}
