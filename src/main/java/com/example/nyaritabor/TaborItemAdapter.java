package com.example.nyaritabor;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;

public class TaborItemAdapter extends RecyclerView.Adapter<TaborItemAdapter.ViewHolder> implements Filterable {
    private ArrayList<TaborItem> mTaboritemsData;
    private ArrayList<TaborItem> mTaboritemsDataAll;
    private Context mContext;
    private int lastposition = -1;

    EditText kereses;



    TaborItemAdapter(Context context, ArrayList<TaborItem> itemsdata){
        this.mTaboritemsData = itemsdata;
        this.mTaboritemsDataAll = itemsdata;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent,false));
    }

    @Override
    public void onBindViewHolder( TaborItemAdapter.ViewHolder holder, int position) {
        TaborItem currentItem = mTaboritemsData.get(position);

        holder.bindTo(currentItem);

        if(holder.getAdapterPosition() > lastposition){
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.animacio2);
            holder.itemView.startAnimation(animation);
            lastposition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mTaboritemsData.size();
    }

    @Override
    public Filter getFilter() {

        return TaborFilter;
    }



    private Filter TaborFilter = new Filter() {


        @Override
        protected FilterResults performFiltering(CharSequence kereses) {
            ArrayList<TaborItem> filteredList =new ArrayList<>();
            FilterResults results = new FilterResults();

            if(kereses == null || kereses.length() ==0){
                results.count = mTaboritemsDataAll.size();
                results.values = mTaboritemsDataAll;
            }else{
                String filterPattern = kereses.toString().toLowerCase().trim();

                for(TaborItem item : mTaboritemsDataAll){
                    if(item.getNev().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            mTaboritemsData = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };


    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTitleText;
        private TextView mInfo;
        private TextView mar;
        private ImageView mItemImage;
        private RatingBar mRatingBar;
        private TextView mFerohely;

        public ViewHolder(View itemView) {
            super(itemView);

            mTitleText = itemView.findViewById(R.id.taborneve);
            mInfo= itemView.findViewById(R.id.subTitle);
            mar= itemView.findViewById(R.id.ara);
            mItemImage= itemView.findViewById(R.id.kep);
            mRatingBar= itemView.findViewById(R.id.csillag);
            mFerohely = itemView.findViewById(R.id.ferohely);



        }

        public void bindTo(TaborItem currentItem) {
            mTitleText.setText(currentItem.getNev());
            mInfo.setText(currentItem.getInfo());
            mar.setText(currentItem.getAr());
            mRatingBar.setRating(currentItem.getCsillag());
            //Glide.with(mContext).load(currentItem.getKepforrasa()).into(mItemImage);
            mItemImage.setImageResource(currentItem.getKepforrasa());
            mFerohely.setText("Létszám: " +currentItem.getJelentkezett_db()+"/"+currentItem.getFerohely());

            itemView.findViewById(R.id.jelentkezes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Activity","jelentkezes");
                    ((TaboroklistajaActivity)mContext).frissitesjelentkezessel(currentItem);

                }
            });

            itemView.findViewById(R.id.jelentkezesvisszavon).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Activity","jelentkezesmegse");
                    ((TaboroklistajaActivity)mContext).jelentkezesVissazvonasa(currentItem);

                }
            });
        }
    }

}

