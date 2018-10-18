package oslomet.no.testreservasjoncontentprovider;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import oslomet.no.testreservasjoncontentprovider.modeller.Bestilling;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void visReservasjoner(View view){
        Intent intent = new Intent(this, ReservasjonAktivitet.class);
        startActivity(intent);
        finish();
    }


}
