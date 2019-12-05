package com.woosik.androidpratice;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.woosik.androidpratice.fingerprint.FingerprintImpl;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private Logger logger = Logger.getLogger(MainActivity.class.getName());

    private TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                       .setAction("Action", null).show();

            }
        });

        Button btn1 = (Button) findViewById(R.id.button_gen_key);
        btn1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeNewKey();
            }
        });

        Button btn2 = (Button) findViewById(R.id.button_req_auth);
        btn2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                fingerprintAuthetication();
            }
        });

        txtView = (TextView) findViewById(R.id.textViewInfo);
        txtView.setText(FingerprintImpl.getInstance().getKeyStoreState());

    }

    private void makeNewKey() {
        logger.info("makeNewKey");
        FingerprintImpl.getInstance().generateKey(this);
    }

    private void fingerprintAuthetication() {
        logger.info("fingerprintAuthetication");
        FingerprintImpl.getInstance().authenticateFingerprint(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
