package com.funon.com.funon.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.funon.com.funon.R;
import com.funon.com.funon.SessionManager;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class vAdapter extends RecyclerView.Adapter<vAdapter.RecyclerViewHolder> {


        private List<VenueAdapter> venueModels;
    private List<VenueAdapter> backup=new ArrayList<>();
    VenueAdapter generatemodel=new VenueAdapter();
        Context context;
        SessionManager manager;
        public vAdapter(Context context, List<VenueAdapter> venueModels)
        {
            this.context=context;
            this.venueModels = venueModels;
            manager=new SessionManager();
        }



        @NonNull
        @Override
        public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.du_venue, parent, false);
            // SearchChemistAdapter.ViewHolder viewHolder = new SearchChemistAdapter.ViewHolder(v);
            return new RecyclerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
            generatemodel= venueModels.get(position);
            holder.name.setText(generatemodel.getVenuename());
            holder.loc.setText(generatemodel.getVenuelocation());
            if(position==1)
            {
                holder.rating.setText(3.2+"");
                holder.venuepre.setBackground(context.getResources().getDrawable(R.mipmap.ic_fore_barwala));
            }
         /*   else if(position==2)
            {
                holder.rating.setText(3.5+"");
                holder.venuepre.setBackground(context.getResources().getDrawable(R.mipmap.ic_bar3));
            }
            else if(position==3)
            {
                holder.rating.setText(3.9+"");
                holder.venuepre.setBackground(context.getResources().getDrawable(R.mipmap.ic_bar4));
            }*/

        }

        @Override
        public int getItemCount() {
            return venueModels.size();
        }
        @Override
        public int getItemViewType(int Position) {
            return Position;
        }

        public class RecyclerViewHolder extends RecyclerView.ViewHolder{

            public TextView name,loc,rating;
            ImageView venuepre;
            public RecyclerViewHolder(View itemView) {
                super(itemView);
                name=(TextView)itemView.findViewById(R.id.name);
                loc=(TextView)itemView.findViewById(R.id.loc);
                rating=(TextView)itemView.findViewById(R.id.rating);
                venuepre=(ImageView)itemView.findViewById(R.id.icon);
            }
        }
        public void setBackup(){
            backup.clear();
            backup.addAll(venueModels);
        }
      /*  public class FetchOTP extends AsyncTask<Void, Void, String> {
            String itemcode,stf;
            SessionManager manager;
            Context context;
            TextView tvotp,otp;
            Button get;
            int posi;
            EditText qty;


            public FetchOTP(String itemcode, Context context,TextView tvotp,TextView otp, Button get,int posi){
                this.itemcode=itemcode;
                this.context=context;
                this.otp=otp;
                this.tvotp=tvotp;
                this.get=get;
                this.posi=posi;
                manager=new SessionManager();

            }
            @Override
            public void onPreExecute() {
                super.onPreExecute();
            }


            public void onPostExecute(String param) {
                try {
                    if(!param.isEmpty()) {

                        generatemodel.setOtp(param);
                    *//*medicineModels.remove(posi);
                    medicineModels.add(posi,itemModel);*//*
                        notifyDataSetChanged();
                        otp.setVisibility(View.VISIBLE);
                        otp.setText(param);
                        tvotp.setVisibility(View.VISIBLE);
                        get.setVisibility(View.GONE);
                    }

                }
                catch (Exception e){

                }
            }

            public String doInBackground(Void... param) {

                stf=fetchstock(itemcode);
                return stf;
            }


            private String fetchstock(String itemcode) {
                String s="";
                try {

                    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new GetInterceptor(context)).build();
                    Request request = new Request.Builder()
                            .url(context.getString(R.string.getdelivery_otp)

                                    +"invoice_code="+itemcode
                                    +"&did="+manager.getPrefs(context,"aid")
                            )
                            .build();
                    Log.e("OTP", "get: " + request.url() + request.headers());
                    try {
                        Response response = client.newCall(request).execute();
                        final String myResponse = response.body().string();
                        JSONObject result = new JSONObject(myResponse);
                        if(result.getString("status").contains("true")) {
                            Log.i("Response", "run: " + result.getString("status"));
                            s =result.getString("otp");
                            Log.i("STOCK",s);
                            return s;
                        }
                    }catch (JSONException e){

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return s;
            }

        }
*/
    public void filterList(){
        venueModels.clear();
        venueModels.addAll(backup);

        Boolean parking,pool,cocktail,birthday,marriage,theamed,chair,table,organizerliable;
        birthday=manager.getPrefs(context,"vbirthday",true);
        theamed= manager.getPrefs(context,"vtheamed",true);
        pool= manager.getPrefs(context,"vpool",true);
        cocktail=manager.getPrefs(context,"vcocktail",true);
        marriage=manager.getPrefs(context,"vmarriage",true);
        chair=manager.getPrefs(context,"vchair",true);
        table=manager.getPrefs(context,"vtable",true);
        parking=manager.getPrefs(context,"vparking",true);

        organizerliable= manager.getPrefs(context,"vorgini",true);

        for(int g=0;g<venueModels.size();g++){
            if(birthday!=venueModels.get(g).getBirthday()&&
                    theamed!=venueModels.get(g).getTheamed()&&
                    cocktail!=venueModels.get(g).getCocktail()&&
                    pool!=venueModels.get(g).getPool()&&
                    marriage!=venueModels.get(g).getMarriage()){

                venueModels.remove(g);
                g--;
                break;
            }

            if(parking!=venueModels.get(g).getParking())
            {

                venueModels.remove(g);
                g--;
                break;
            }

            if(chair!=venueModels.get(g).getChair()&&table!=venueModels.get(g).getTable())
            {

                venueModels.remove(g);
                g--;
                break;
            }


        }

        notifyDataSetChanged();
    }

    public void clearFilter(){
        venueModels.clear();
        venueModels.addAll(backup);
        notifyDataSetChanged();
    }


    }





