package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RadioGroup gender;
        final RadioButton[] checked = new RadioButton[1];
        final EditText ediText = findViewById(R.id.editText);
        final EditText newage = findViewById(R.id.editText3);
        gender=findViewById(R.id.radioGroup);
        Button saveCompo = findViewById(R.id.button);
        saveCompo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = ediText.getText().toString();
                String age = newage.getText().toString();
                int selected = gender.getCheckedRadioButtonId();
                checked[0] =(RadioButton)findViewById(selected);
                String gender_selected = checked[0].getText().toString();
                JSONObject obj = new JSONObject();

                try {
                    obj.put("name", name);
                    obj.put("age", age);
                    obj.put("gender", gender_selected);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray formDetails = null;
                try {
                    formDetails = new JSONArray(readFromFile(getApplicationContext()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                formDetails.put(obj);
                writeToFile(formDetails.toString(),getApplicationContext());

                Log.i("MainActivvity", readFromFile(MainActivity.this));

                }
            });

        }
    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("details.json", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("details.json");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) { 
            ret="[]";
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}

