package test.jaiboondemand.DonateMain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import test.jaiboondemand.R;

public class CateGoryUser extends AppCompatActivity {
    private Spinner spinner;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("เลือกประเภทลูกค้า");
        setContentView(R.layout.activity_cate_gory);
        addListenerOnButton();
    }
    public void addListenerOnButton(){
        spinner = (Spinner) findViewById(R.id.spinner_type);
        ArrayAdapter mAdapter = new ArrayAdapter<String>(CateGoryUser.this,android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.Type_Customer));
        spinner.setAdapter(mAdapter);
        button = (Button) findViewById(R.id.btn_next);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = spinner.getSelectedItem().toString();
                if(text.equals("General Customer")){
                    Intent intent = new Intent(CateGoryUser.this,ProfileCustomer.class);
                    startActivity(intent);
                }
                else if(text.equals("Temple")){
                    Intent intent = new Intent(CateGoryUser.this,ProfileTemple.class);
                    startActivity(intent);
                }
                else if(text.equals("Foundation")){
                    Intent intent = new Intent(CateGoryUser.this,ProfileFoundation.class);
                    startActivity(intent);
                }

            }
        });
    }
}
