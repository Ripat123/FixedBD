package com.sbitbd.fixedbd.ImageSlider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sbitbd.fixedbd.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class sliderAdapter extends SliderViewAdapter<sliderAdapter.sliderViewAdap>{
    private Context context;
    private List<sliderModel> sliderModels = new ArrayList<>();
//    DoConfig config = new DoConfig();

    public sliderAdapter(Context context, List<sliderModel> sliderModels) {
        this.context = context;
        this.sliderModels = sliderModels;
    }

    @Override
    public sliderViewAdap onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.sliderimage,null);
        return new sliderViewAdap(inflate);
    }

    @Override
    public void onBindViewHolder(sliderViewAdap viewHolder, int position) {
        try {
            sliderModel sliderItem = sliderModels.get(position);
//            Glide.with(viewHolder.itemView)
//                    .load(sliderItem.getImageurl())
//                    .fitCenter()
//                    .into(viewHolder.imageView);
            Picasso.get().load(sliderItem.getImageurl())
                    .fit().centerInside()
                    .placeholder(R.drawable.water)
                    .error(R.drawable.water)
                    .into(viewHolder.imageView);
        }catch (Exception e){
        }
    }

    @Override
    public int getCount() {
        return sliderModels.size();
    }
    public void ClearSlider(){
        sliderModels.clear();
        notifyDataSetChanged();
    }

    class sliderViewAdap extends SliderViewAdapter.ViewHolder{
        final View itemView;
        ImageView imageView;
        public sliderViewAdap(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_for_slider_id);
            this.itemView = itemView;
        }
    }
}
