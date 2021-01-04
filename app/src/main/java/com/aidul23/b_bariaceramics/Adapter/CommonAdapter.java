package com.aidul23.b_bariaceramics.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aidul23.b_bariaceramics.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.ViewHolder> {

    public static final int SUB_CAT = 1;
    public static final int PRODUCT = 2;
    private final List<String> mList;
    private final String mSender;
    private final String categoryTitle;

    private final Context mContext;

    private final static String[] colors = {"#E74C3C", "#2980B9", "#16A085", "#34495E"};

    private OnItemClickListener mListener;

    public CommonAdapter(List<String> list, String sender, Context mContext, String categoryTitle) {
        mList = list;
        mSender = sender;
        this.mContext = mContext;
        this.categoryTitle = categoryTitle;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public CommonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
                return new CommonAdapter.ViewHolder(view, mListener);
            case 2:
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
                return new CommonAdapter.ViewHolder(view1, mListener);
            default:
                View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
                return new CommonAdapter.ViewHolder(view2, mListener);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mSender.equals("subCat")) {

            switch (categoryTitle) {
                case "Wash Basin":
                    holder.itemThumb.setImageResource(R.drawable.basin_1hdpi);
                    break;
                case "High Commode":
                    holder.itemThumb.setImageResource(R.drawable.commade_2hdpi);
                    break;
                case "Low Commode":
                    holder.itemThumb.setImageResource(R.drawable.commade_2hdpi);
                    break;
                case "Metal":
                    holder.itemThumb.setImageResource(R.drawable.tap_3hdpi);
                    break;
            }

            holder.titleTextView.setText(mList.get(position));
        } else if (mSender.equals("product")) {
            Picasso.get().load(mList.get(position)).into(holder.imageView);
        }
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imageView;
        TextView titleTextView;
        ImageView itemThumb;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewId);
            titleTextView = itemView.findViewById(R.id.itemName);
            itemThumb = itemView.findViewById(R.id.itemImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null) {
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mSender.equals("subCat")) {
            return SUB_CAT;
        } else if (mSender.equals("product")) {
            return PRODUCT;
        }
        return -1;
    }
}
