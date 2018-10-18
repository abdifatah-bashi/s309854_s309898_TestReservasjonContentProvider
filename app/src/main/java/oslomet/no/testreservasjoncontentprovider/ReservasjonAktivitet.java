package oslomet.no.testreservasjoncontentprovider;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import oslomet.no.testreservasjoncontentprovider.modeller.Bestilling;

public class ReservasjonAktivitet extends AppCompatActivity {

    public final static String PROVIDER="oslomet.no.s309898_s309854";
    public static final Uri CONTENT_URI= Uri.parse("content://"+ PROVIDER + "/bestilling");
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aktivitet_reservasjon);

        ArrayList<Bestilling> bestillinger = hentAlleReservasjoner();
        listView = findViewById(R.id.list_view);
        BestillingListeAdapter adapter = new BestillingListeAdapter(this, R.layout.bestilling_liste_view, bestillinger);
        listView.setAdapter(adapter);
    }

    public ArrayList<Bestilling> hentAlleReservasjoner( ){
        ArrayList<Bestilling> allebestillinger = new ArrayList<>();
        Cursor cursor =this.getContentResolver().query(CONTENT_URI, null, null, null, null);


        if(cursor != null && cursor.getCount() > 0){

        }
        while (cursor.moveToNext()) {

            Bestilling bestilling = new Bestilling();
            bestilling.setId(cursor.getInt(0));
            bestilling.setDato(cursor.getString(1));
            bestilling.setKlokkeslett(cursor.getString(2));
            bestilling.setRestaurant_id(cursor.getInt(3));

            allebestillinger.add(bestilling);
        }
        cursor.close();


        return allebestillinger;
    }
}
