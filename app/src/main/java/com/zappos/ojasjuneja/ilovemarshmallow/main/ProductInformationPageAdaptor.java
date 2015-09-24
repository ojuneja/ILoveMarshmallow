package com.zappos.ojasjuneja.ilovemarshmallow.main;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zappos.ojasjuneja.ilovemarshmallow.R;
import com.zappos.ojasjuneja.ilovemarshmallow.variables.Tag;
import com.zappos.ojasjuneja.ilovemarshmallow.async.MyAsyncTaskDownloadImage;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ojas Juneja on 9/14/2015.
 * Class servers as adaptor class for ProductInformationPageActivity
 * Downloads details and images using Async class present in utility package
 * shows the details on the screen
 */
public class ProductInformationPageAdaptor extends RecyclerView.Adapter<ProductInformationPageAdaptor.ViewHolder> {

    private LruCache lruCache;
    private Bitmap bitmap;
    private String price;
    private ArrayList<HashMap<String,String>> arrayListProductDescription;


    public ProductInformationPageAdaptor(LruCache lruCache)
    {
      this.lruCache = lruCache;
        arrayListProductDescription = new ArrayList<>();
    }

    //updates data in PIP Adaptor
    public void updateData(ArrayList<HashMap<String,String>> arrayList,String price)
    {
        this.arrayListProductDescription = arrayList;
        this.price = price;
    }

    @Override
    public ProductInformationPageAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_product_information_page_row,parent,false));
    }

    @Override
    public void onBindViewHolder(ProductInformationPageAdaptor.ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return 1;
    }
    //View Holder to hold the view first time it is created
    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView,imageViewSquare;
        TextView textViewTitle,textViewGender,textViewPrice;
        HtmlTextView textViewDescription;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.image_product);
            textViewTitle = (TextView)itemView.findViewById(R.id.text_title);
            textViewDescription = (HtmlTextView)itemView.findViewById(R.id.text_description);
            textViewGender = (TextView)itemView.findViewById(R.id.text_gender);
            textViewPrice = (TextView)itemView.findViewById(R.id.text_price);
            imageViewSquare = (ImageView)itemView.findViewById(R.id.image_product_square);
        }
    //binds the data for the first time and also after updating
        void bindData(int position)
        {
            if(arrayListProductDescription.size() > 0)
            {
                String imageURL = arrayListProductDescription.get(position).get(Tag.IMAGE_PIP);
                textViewTitle.setText(arrayListProductDescription.get(position).get(Tag.PRODUCT_FULL_TITLE));
                textViewDescription.setHtmlFromString(arrayListProductDescription.get(position).get(Tag.PIP_DESCRIPTION),new HtmlTextView.RemoteImageGetter());
                textViewGender.setText(Tag.TEXT_GENDER + arrayListProductDescription.get(position).get(Tag.GENDER));
                textViewPrice.setText(price);
                textViewDescription.setEnabled(false);
                if(!imageURL.equals("")) {
                    bitmap = (Bitmap) lruCache.get(imageURL);
                    if (bitmap == null) {
                        MyAsyncTaskDownloadImage myAsyncTaskDownloadImage = new MyAsyncTaskDownloadImage(imageView, lruCache);
                        myAsyncTaskDownloadImage.execute(new String[]{imageURL, Tag.PIP});
                    } else {
                        imageView.setImageBitmap(bitmap);
                        imageViewSquare.setImageBitmap(bitmap);
                    }
                }
                else {
                    imageView.setImageResource(R.drawable.default_image);
                    imageViewSquare.setImageResource(R.drawable.default_image);
                }
            }
        }
    }
}