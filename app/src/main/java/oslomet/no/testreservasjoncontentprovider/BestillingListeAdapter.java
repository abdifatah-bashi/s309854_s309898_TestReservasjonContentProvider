package oslomet.no.testreservasjoncontentprovider;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import oslomet.no.testreservasjoncontentprovider.modeller.Bestilling;
import oslomet.no.testreservasjoncontentprovider.modeller.Bestilling_Venner;
import oslomet.no.testreservasjoncontentprovider.modeller.Restaurant;
import oslomet.no.testreservasjoncontentprovider.modeller.Venn;


public class BestillingListeAdapter extends ArrayAdapter<Bestilling> {

    private Context context;
    private int resource;
    private ArrayList<Bestilling> bestillingArrayList;

    public final static String PROVIDER="oslomet.no.s309898_s309854";
    public static final Uri RESTAURANT_URI= Uri.parse("content://"+ PROVIDER + "/restaurant");
    public static final Uri VENN_URI= Uri.parse("content://"+ PROVIDER + "/venn");
    public static final Uri BESTILLING_VENNER_URI= Uri.parse("content://"+ PROVIDER + "/bestillingvenner");



    /**
     * Holder variabler i View-et
     */
    private static class ViewHolder {
        TextView restaurantNavn;
        TextView tid;
        TextView venner;

    }

    // Konstruktør
    public BestillingListeAdapter(Context context, int resource, ArrayList<Bestilling> bestillingArrayList) {

        super(context, resource, bestillingArrayList);
        this.bestillingArrayList = bestillingArrayList;
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int id_best = getItem(position).getId();
        Bestilling best =getItem(position);
        String tid = best.getDato() + " " + best.getKlokkeslett();
        Restaurant restaurant = hentRestaurant(String.valueOf(best.getId()));
        String restaurantNavn = restaurant.getNavn();

        List<Venn> venner = hentBestilling(best.getId());
        Log.i("TestAntallVenner: ", venner.size() + "");
        List<String> vennNavn = new ArrayList<>();
        for (int i = 0; i < venner.size(); i++) {
            vennNavn.add(venner.get(i).getFornavn() + " " + venner.get(i).getEtternavn());
        }
        //ViewHolder object
        ViewHolder holder;


        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();

            holder.restaurantNavn = convertView.findViewById(R.id.r_navn);
            holder.tid = convertView.findViewById(R.id.bestilling_dato);
            holder.venner = convertView.findViewById(R.id.b_venner);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.restaurantNavn.setText("Restaurant: " + restaurantNavn);
        holder.tid.setText("Tid: " + tid);
        holder.venner.setText("Venner: \n" + toString(vennNavn));
        return convertView;
    }

    // Hjelp metoder
    public String toString(List<String> venner) {
        StringBuilder builder = new StringBuilder();
        for (String v : venner) {
            builder.append(v).append("\n");
        }
        return builder.toString();
    }

    public Restaurant hentRestaurant(String bestillingId){
        Restaurant restaurant = new Restaurant();
        Cursor cursor = context.getContentResolver().query(RESTAURANT_URI, null, bestillingId,
                null, null);

        if(cursor != null){
            if(cursor.moveToFirst()) {
                restaurant = new Restaurant(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3),cursor.getString(4));

                cursor.close();
                return restaurant;
            }
        }

        return restaurant;
    }

    public ArrayList<Venn> hentVenner_Bestilling(String bestillingId){
        ArrayList<Venn> venner = new ArrayList<>();

        Cursor cursor =context.getContentResolver().query(BESTILLING_VENNER_URI, null, null,
                null, null);

        if(cursor != null){
            if (cursor.moveToFirst()) {
                do {



                    int venn_id = cursor.getInt(1);
                    Log.i("venn_idnaa: ", venn_id + "" );
                    Venn venn = hentVenn(venn_id);
                    venner.add(venn);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }  else {
            Log.i("cursorNull:", "Cursor is null");
        }

        for(Venn v : venner) Log.i("list_venner: " , v.getFornavn() + " , ");

        return venner;


    }

    public Restaurant hentRestaurant(int id){
        Restaurant restaurant = null;
        ArrayList<Restaurant> alleRestauranter = new ArrayList<>();
        Cursor cur =context.getContentResolver().query(RESTAURANT_URI, null, null, null, null);
        if (cur.moveToFirst()) {
            do {
                restaurant = new Restaurant(cur.getInt(0), cur.getString(1),
                        cur.getString(2), cur.getString(3), cur.getString(4));
                alleRestauranter.add(restaurant);
            }
            while (cur.moveToNext());
            cur.close();

            // Hent restaurant
            for(Restaurant r: alleRestauranter){
                if(r.getID() == id) restaurant = r;
            }

        }
        return restaurant;


    }



    public Venn hentVenn(int id ){
        Venn utVenn = null;
        ArrayList<Venn> alleVenner = new ArrayList<>();
        Cursor cursor =context.getContentResolver().query(VENN_URI, null, null, null, null);
        if(cursor != null && cursor.getCount() > 0){

        }
        while (cursor.moveToNext()) {

            Venn   venn = new Venn(cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3));

            alleVenner.add(venn);
        }
        cursor.close();

        for(Venn venn : alleVenner){
            if(venn.getId() == id) utVenn = venn;
        }


        return utVenn;
    }

    public List<Venn> hentBestilling(int bestillingId){
        // hent innholdet av tabellen bestilling_venn
        ArrayList<Bestilling_Venner> data = new ArrayList<>();
        ArrayList<Venn> venner = new ArrayList<>();
        Cursor cursor =context.getContentResolver().query(BESTILLING_VENNER_URI, null, null,
                null, null);

        if(cursor != null){
            if (cursor.moveToFirst()) {
                do {

                    Bestilling_Venner bestilling_venner = new Bestilling_Venner(cursor.getInt(0), cursor.getInt(1));
                    data.add(bestilling_venner);

                } while (cursor.moveToNext());
            }
            cursor.close();

            // Finn venner
            for(Bestilling_Venner bv : data) {
                if(bv.getB_Id() == bestillingId) {
                    Venn venn = hentVenn(bv.getV_Id());
                    venner.add(venn);
                }
            }
        }

        return venner;

    }


}

