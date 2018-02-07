package test.jaiboondemand.Factor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import test.jaiboondemand.R;

public class FacCategory extends AppCompatActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main_fac_cate);
        setSupportActionBar(toolbar);
        setTitle("Category");

    }
    public void Education_donate(View view){
        Intent educattion = new Intent(FacCategory.this,FactorMain.class);
        educattion.putExtra("Type","Education");
        startActivity(educattion);
        finish();
    }
    public void Poor_donate(View view){
        Intent poor = new Intent(FacCategory.this,FactorMain.class);
        poor.putExtra("Type","Poor");
        startActivity(poor);
        finish();
    }
    public void maintain_donate(View view){
        Intent maintain = new Intent(FacCategory.this,FactorMain.class);
        maintain.putExtra("Type","maintain");
        startActivity(maintain);
        finish();
    }
    public void Victim_donate(View view){
        Intent victim = new Intent(FacCategory.this,FactorMain.class);
        victim.putExtra("Type","victim");
        startActivity(victim);
        finish();
    }
    public void helpchild_donate(View view){
        Intent helpchild = new Intent(FacCategory.this,FactorMain.class);
        helpchild.putExtra("Type","child");
        startActivity(helpchild);
        finish();
    }
    public void helpanimal_donate(View view){
        Intent helpanimal = new Intent(FacCategory.this,FactorMain.class);
        helpanimal.putExtra("Type","animal");
        startActivity(helpanimal);
        finish();
    }
    public void Disadvantaged_donate(View view){
        Intent disadbantaged = new Intent(FacCategory.this,FactorMain.class);
        disadbantaged.putExtra("Type","disadvantaged");
        startActivity(disadbantaged);
        finish();
    }
    public void helpsocial_donate(View view){
        Intent helpsocial = new Intent(FacCategory.this,FactorMain.class);
        helpsocial.putExtra("Type","social");
        startActivity(helpsocial);
        finish();
    }
    public void older_donate(View view){
        Intent older = new Intent(FacCategory.this,FactorMain.class);
        older.putExtra("Type","older");
        startActivity(older);
        finish();
    }
    public void disabled_donate(View view){
        Intent disabled= new Intent(FacCategory.this,FactorMain.class);
        disabled.putExtra("Type","disabled");
        startActivity(disabled);
        finish();
    }
}
