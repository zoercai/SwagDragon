package com.example.zoe.swagdragon;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class TreeInfoActivity extends AppCompatActivity {
    public static final String INFOEXTRA = "TREE_INFO_EXTRA_INFO";
    public static final String INFOID = "TREEID";

    private String id;
    private EditText descField;
    static public HashMap<String, String> hm = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_info);

        Intent i = getIntent();
        TextView t = (TextView) findViewById(R.id.description);
        String desc = i.getStringExtra(INFOEXTRA);
        final Button b = (Button) findViewById(R.id.btn_save);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("ON CLICK HAS CALLED", "ED");
                if (descField.getText().toString().length() == 0){
                    Toast.makeText(TreeInfoActivity.this, "Please enter a message first", Toast.LENGTH_SHORT).show();
                } else {
                    hm.put(id, descField.getText().toString());
                    descField.setHint(descField.getText().toString());
                    descField.setText("");
                    if (v != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    Toast.makeText(TreeInfoActivity.this, "Message Saved", Toast.LENGTH_SHORT).show();
                }
            }
        });

        descField = (EditText) findViewById(R.id.treeMessage);
        /*
        see if youve already been to tree
         */
        id = i.getStringExtra(INFOID);

        if(hm.containsKey(id)){
                descField.setHint(hm.get(id));
        }

        if (desc != null) {
            t.setText(desc);
        } else {
            t.setText("Not much is known about this elusive tree.");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tree_info, menu);
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
