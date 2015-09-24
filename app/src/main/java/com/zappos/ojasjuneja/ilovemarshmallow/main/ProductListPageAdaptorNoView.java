package com.zappos.ojasjuneja.ilovemarshmallow.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zappos.ojasjuneja.ilovemarshmallow.R;
import com.zappos.ojasjuneja.ilovemarshmallow.variables.Tag;

/**
 * Created by Ojas Juneja on 9/17/2015.
 * Class used when there are no results
 * Also shows the homepage before any search is made
 */
public class ProductListPageAdaptorNoView extends RecyclerView.Adapter<ProductListPageAdaptorNoView.ViewHolder> {

    private boolean errorFlag = false;

    //updates flag - true errorFlag stands for no result is returned
    public void updateData(boolean errorFlag)
    {
        this.errorFlag = errorFlag;
    }

    @Override
    public ProductListPageAdaptorNoView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_main_no_result,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(ProductListPageAdaptorNoView.ViewHolder holder, int position) {
        holder.bindData();
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.image_error);
            textView = (TextView)itemView.findViewById(R.id.text_error);
        }
        //binds the data for the first time and also after updating
        public void bindData()
        {
            if(errorFlag)
            {
                imageView.setImageResource(R.drawable.error);
                textView.setText(Tag.NOT_FOUND);
            }
            else
            {
                imageView.setImageResource(R.drawable.zappos_logo);
                textView.setText("");
            }
        }
    }
}
