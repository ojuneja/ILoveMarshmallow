package com.zappos.ojasjuneja.ilovemarshmallow.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.zappos.ojasjuneja.ilovemarshmallow.R;
import com.zappos.ojasjuneja.ilovemarshmallow.async.MyAsyncTaskDownloadDetails;
import com.zappos.ojasjuneja.ilovemarshmallow.utils.LRUCacheClass;
import com.zappos.ojasjuneja.ilovemarshmallow.utils.UtilityFunctions;
import com.zappos.ojasjuneja.ilovemarshmallow.variables.Tag;

/**
 * Created by Ojas Juneja on 9/14/2015.
 * Class created to show product information page and the necessary details
 * class contains floating action button to share URL
 */
public class ProductInformationPageActivity extends AppCompatActivity {

    private ProductInformationPageAdaptor productInformationPageAdaptor;
    private String PIP_URL;
    private  String price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getIntent().getExtras();
        setContentView(R.layout.activity_product_information_page);
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        price = (String)args.get(Tag.PRICE);
        if(toolbar!=null) {
            setSupportActionBar(toolbar);
            setTitle((String)args.get(Tag.TITLE));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        PIP_URL = (String)args.get(Tag.PIP_URL);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view_pip);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        productInformationPageAdaptor = ProductInformationPageAdaptor.SingletonInstance();
        productInformationPageAdaptor.updateCacheAndInitializeArray(LRUCacheClass.getCache());
        recyclerView.setAdapter(productInformationPageAdaptor);
        UtilityFunctions.onProgressBarShow(this);
        MyAsyncTaskDownloadDetails myAsyncTaskDownloadDetails =  new MyAsyncTaskDownloadDetails();
        myAsyncTaskDownloadDetails.execute(PIP_URL, Tag.PIP,price);
        //floating action button to share the URL
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Tag.FOUND + PIP_URL);
                startActivity(Intent.createChooser(sharingIntent, ""));
            }
        });

    }



    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        return true;
    }
    }
