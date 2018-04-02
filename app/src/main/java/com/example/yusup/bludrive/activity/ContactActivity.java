package com.example.yusup.bludrive.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import com.example.yusup.bludrive.R;


public class ContactActivity extends AppCompatActivity {

    TextView contact_person, contact_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        contact_person = (TextView)findViewById(R.id.contact_person);
        contact_email = (TextView)findViewById(R.id.contact_email);

        String udata= "+62 896 3341 0789";
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);//where first 0 shows the starting and udata.length() shows the ending span.if you want to span only part of it than you can change these values like 5,8 then it will underline part of it.
        contact_person.setText(content);


        contact_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+contact_person.getText().toString().trim()));
                startActivity(intent);
            }
        });


        contact_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "yusup.bludrive@gmail.com"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "FeedBack");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Masukan pesan anda...");
                //emailIntent.putExtra(Intent.EXTRA_HTML_TEXT, body); //If you are using HTML in your body text

                startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
            }
        });
        getSupportActionBar().setTitle("BLuDrive");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
