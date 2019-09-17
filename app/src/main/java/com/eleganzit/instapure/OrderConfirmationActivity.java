package com.eleganzit.instapure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class OrderConfirmationActivity extends AppCompatActivity {

    ImageView notification,feedback;
    TextView txt_month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);


        ImageView back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Calendar cal= Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(cal.getTime());

        txt_month=findViewById(R.id.txt_month);
        notification=findViewById(R.id.notification);
        feedback=findViewById(R.id.feedback);

        txt_month.setText(getResources().getString(R.string.qr_scanned_successfully_for_the_month)+" "+month_name+".");

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderConfirmationActivity.this, NotificationActivity.class).putExtra("from","orderc"));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OrderConfirmationActivity.this, FeedbackActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

            }
        });

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(OrderConfirmationActivity.this, HomeActivity.class)
                .putExtra("from","order_confirm")
        );
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();

    }
}
