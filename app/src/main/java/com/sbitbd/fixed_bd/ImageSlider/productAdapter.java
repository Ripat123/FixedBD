package com.sbitbd.fixed_bd.ImageSlider;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sbitbd.fixed_bd.Config.DoConfig;
import com.sbitbd.fixed_bd.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class productAdapter extends SliderViewAdapter<productAdapter.productAdap>{
    private Context context;
    private List<sliderModel> sliderModels = new ArrayList<>();
    DoConfig config = new DoConfig();

    public productAdapter(Context context, List<sliderModel> sliderModels) {
        this.context = context;
        this.sliderModels = sliderModels;
    }

    @Override
    public productAdap onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_slider,null);
        return new productAdapter.productAdap(inflate);
    }

    @Override
    public void onBindViewHolder(productAdap viewHolder, int position) {
        try {
            sliderModel sliderItem = sliderModels.get(position);
            viewHolder.bind(sliderItem);
//            Glide.with(viewHolder.itemView)
//                    .load(sliderItem.getImageurl())
//                    .fitCenter()
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
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

    class productAdap extends SliderViewAdapter.ViewHolder{
        final View itemView;
        ImageView imageView;
        final Context context1;
        public productAdap(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_for_product_id);
            this.itemView = itemView;
            context1 = itemView.getContext();

        }
        public void bind(sliderModel sliderModel){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent;
                    intent = new Intent(context1,ImageFullview.class);
                    intent.putExtra("image",sliderModel.getImageurl());
                    context1.startActivity(intent);
                }
            });
        }
    }
}

