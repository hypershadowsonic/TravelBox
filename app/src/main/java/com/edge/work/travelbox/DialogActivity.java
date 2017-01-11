package com.edge.work.travelbox;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class DialogActivity extends Activity {

    ImageView title, btn_fb, btn_close;
    TextView shopname, address, description, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_placeinfo);

        shopname = (TextView) findViewById(R.id.place_name);
        address = (TextView) findViewById(R.id.place_address);
        description = (TextView) findViewById(R.id.place_description);
        time = (TextView) findViewById(R.id.place_time);
        title = (ImageView) findViewById(R.id.place_title);
        btn_fb = (ImageView) findViewById(R.id.place_facebook);
        btn_close = (ImageView) findViewById(R.id.place_btn_close);

        shopname.setText(getIntent().getExtras().getString("name"));
        address.setText(getIntent().getExtras().getString("address"));
        description.setText(getIntent().getExtras().getString("description").replace("\\n", "\n"));
        time.setText(getIntent().getExtras().getString("businesshour").replace("\\n", "\n"));
        ImageLoader.getInstance().displayImage(getIntent().getExtras().getString("titleimg"),title);
        if(getIntent().getExtras().getString("facebookurl")!=null){
            btn_fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getIntent().getExtras().getString("facebookurl")));
                    startActivity(browserIntent);
                }
            });
        } else {
            btn_fb.setVisibility(View.GONE);
        }

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
