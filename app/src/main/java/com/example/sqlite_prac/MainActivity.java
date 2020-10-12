package com.example.sqlite_prac;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText et_name;
    EditText et_age;
    Switch sw_isactive;
    Button btn_viewall;
    Button btn_add;
    ListView lv_customerList;

    ArrayAdapter customerArrayAdapter;
    DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);
        sw_isactive = findViewById(R.id.sw_isactive);
        btn_viewall = findViewById(R.id.btn_viewall);
        btn_add = findViewById(R.id.btn_add);
        lv_customerList = findViewById(R.id.lv);

        showCustomerOnListview(databaseHelper);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomerModel customerModel;
                try {
                    customerModel = new CustomerModel(1, et_name.getText().toString(),
                            Integer.parseInt(et_age.getText().toString()), true );
                    Toast.makeText(MainActivity.this, "added !", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    customerModel = new CustomerModel(1, "error", 0, false);
                    Toast.makeText(MainActivity.this, "error occured", Toast.LENGTH_SHORT).show();
                }

                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);

                boolean state = databaseHelper.addOne(customerModel);
                Toast.makeText(MainActivity.this, "state: "+ state, Toast.LENGTH_SHORT).show();

                showCustomerOnListview(databaseHelper);
            }
        });

        btn_viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCustomerOnListview(databaseHelper);


//                Toast.makeText(MainActivity.this, everyone.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        lv_customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomerModel clickedCustomer = (CustomerModel) parent.getItemAtPosition(position);
                databaseHelper.deleteOne(clickedCustomer);

                showCustomerOnListview(databaseHelper);
                Toast.makeText(MainActivity.this, clickedCustomer.toString() , Toast.LENGTH_SHORT).show();


            }
        });

    }

    private void showCustomerOnListview(DatabaseHelper databaseHelper2) {
        customerArrayAdapter = new ArrayAdapter<CustomerModel>(MainActivity.this
                , android.R.layout.simple_list_item_1, databaseHelper2.getEveryone());
        lv_customerList.setAdapter(customerArrayAdapter);
    }
}