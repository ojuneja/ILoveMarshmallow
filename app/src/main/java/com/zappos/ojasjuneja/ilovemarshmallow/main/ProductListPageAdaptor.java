package com.zappos.ojasjuneja.ilovemarshmallow.main;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.zappos.ojasjuneja.ilovemarshmallow.R;
import com.zappos.ojasjuneja.ilovemarshmallow.variables.Tag;
import com.zappos.ojasjuneja.ilovemarshmallow.async.MyAsyncTaskDownloadImage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ojas Juneja on 9/13/2015.
 */
public class ProductListPageAdaptor extends RecyclerView.Adapter<ProductListPageAdaptor.ViewHolder> {

    private Bitmap bitmap;
    private LruCache<String,Bitmap> lruCache;
    ArrayList<HashMap<String,String>> arrayListPLPDetails;
    private OnListItemSelected onListItemSelected;

    //interface that wraps onClick method for better coding standards
    public interface OnListItemSelected
    {
        void onListItemSelected(String asinID,String title,String price);
    }

    //method that provides a hook so that multiple PLP page can use same adaptor
    public void onListItemSelectedListener(OnListItemSelected onListItemSelected)
    {
        this.onListItemSelected = onListItemSelected;
    }

    ProductListPageAdaptor(LruCache lruCache)
    {
        arrayListPLPDetails = new ArrayList<>();
        this.lruCache = lruCache;
    }

    //updates data
    void updateData(ArrayList<HashMap<String,String>> arrayListPLPDetails)
    {
        this.arrayListPLPDetails = arrayListPLPDetails;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_main_row,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return arrayListPLPDetails.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewProduct,imageViewError;
        TextView textViewTitle,textViewDescription,textViewPrice;
        RatingBar ratingBar;

        public ViewHolder(final View itemView) {
            super(itemView);
            textViewTitle = (TextView)itemView.findViewById(R.id.text_title);
            textViewDescription = (TextView)itemView.findViewById(R.id.text_description);
            textViewPrice = (TextView)itemView.findViewById(R.id.text_price);
            ratingBar = (RatingBar)itemView.findViewById(R.id.rating_bar);
            imageViewProduct = (ImageView)itemView.findViewById(R.id.image_product);
            imageViewError = (ImageView)itemView.findViewById(R.id.image_product);
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if(onListItemSelected!=null)
                    {
                        onListItemSelected.onListItemSelected(arrayListPLPDetails.get(getPosition()).get(Tag.ASIN),
                                textViewTitle.getText().toString(),textViewPrice.getText().toString());
                    }
                }
            });
        }

        //binds the data for the first time and also after updating
        void bindData(int position)
        {
            if(arrayListPLPDetails.size() > 0)
            {
                imageViewError.setImageBitmap(null);
                String imageURL = arrayListPLPDetails.get(position).get(Tag.IMAGE);
                String rating = arrayListPLPDetails.get(position).get(Tag.RATING);
                String textDescription = arrayListPLPDetails.get(position).get(Tag.PRODUCT_FULL_TITLE);
                if(textDescription.length() > 20)
                {
                    textDescription = textDescription.substring(0,20);
                    textDescription = textDescription + "...";
                }
                textViewTitle.setText(arrayListPLPDetails.get(position).get(Tag.TITLE));
                textViewDescription.setText(textDescription);
                textViewPrice.setText(arrayListPLPDetails.get(position).get(Tag.PRICE));

                Float floatRating  = Float.parseFloat(rating);
                if(floatRating > 0.0)
                {
                    ratingBar.setRating(floatRating);
                }
                //download image if it is not present in LRU Cache
                bitmap = lruCache.get(imageURL);
                if(bitmap == null)
                {
                    MyAsyncTaskDownloadImage myAsyncTaskDownloadImage = new MyAsyncTaskDownloadImage(imageViewProduct,lruCache,null);
                    myAsyncTaskDownloadImage.execute(new String[]{imageURL,Tag.PLP});
                }
                else
                {
                    imageViewProduct.setImageBitmap(bitmap);
                }
            }
        }
    }
}
