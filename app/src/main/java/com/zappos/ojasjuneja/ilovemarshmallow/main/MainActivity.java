package com.zappos.ojasjuneja.ilovemarshmallow.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zappos.ojasjuneja.ilovemarshmallow.R;
import com.zappos.ojasjuneja.ilovemarshmallow.async.MyAsyncTaskDownloadDetails;
import com.zappos.ojasjuneja.ilovemarshmallow.utils.LRUCacheClass;
import com.zappos.ojasjuneja.ilovemarshmallow.utils.NetworkUtility;
import com.zappos.ojasjuneja.ilovemarshmallow.variables.Tag;

import java.util.ArrayList;
import java.util.HashMap;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;

/**
 * Created by Ojas Juneja on 9/13/2015.
 * This is the Homepage of our application
 * Starts a search from PLP page and display results if results are there
 * uses recycler view to display results
 * handle configuration changes
 */

public class MainActivity extends AppCompatActivity {
    Toolbar toolbarSearch;
    private boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent intent = getIntent();
        final Uri myURI = intent.getData();
        LRUCacheClass lruCacheClass = new LRUCacheClass();
        lruCacheClass.setLRUCache();
        if(!isNetworkAvailable())
        {
            setContentView(R.layout.activity_error_main);
        }
        else {
            if(myURI == null) {
                setContentView(R.layout.activity_main);
                toolbarSearch = (Toolbar) findViewById(R.id.tool_bar);
                setSupportActionBar(toolbarSearch);
                toolbarSearch.setTitleTextColor(Color.WHITE);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, PlaceHolderFragment.newInstance()).commit();
            }
            else
            {
                Intent intentPIP = new Intent(this,ProductInformationPageActivity.class);
                intentPIP.putExtra(Tag.PIP_URL,myURI.toString());
                startActivity(intentPIP);
            }
        }
    }

    //check if network available before starting the application
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    //quits on pressing back button twice
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            PlaceHolderFragment.clearVariables();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, Tag.BACK, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 3000);
    }

    public static class PlaceHolderFragment extends Fragment {

        private static ProductListPageAdaptor productListPageAdaptor;
        private static ProductListPageAdaptorNoView productListPageAdaptorNoView;
        private static RecyclerView recyclerView,recyclerViewNoResult;
        private static ArrayList<HashMap<String,String>> arrayListPLPDetails;
        private static String query;


        public static PlaceHolderFragment newInstance()
        {
            return new PlaceHolderFragment();
        }

        //updates recycler view when data is changed after a new search is done
        public static void  updateAndRefreshAdaptor( ArrayList<HashMap<String,String>> arrayListPLPDetailsUpdated,boolean errorFlag)
        {

            arrayListPLPDetails = arrayListPLPDetailsUpdated;
            //if no data is returned
            if(errorFlag)
            {
                setPLPRecyclerNoResultView();
                productListPageAdaptorNoView.updateData(true);
                productListPageAdaptorNoView.notifyDataSetChanged();
            }
            //if data is returned
            else {
                setPLPRecyclerView();
                productListPageAdaptor.updateData(arrayListPLPDetails);
                productListPageAdaptor.notifyDataSetChanged();
                AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(productListPageAdaptor);
                alphaAdapter.setDuration(500);
                alphaAdapter.notifyDataSetChanged();
            }
        }

        public static void clearVariables()
        {
            // must clear static variables on exit from application
            if(arrayListPLPDetails!=null)
            arrayListPLPDetails.clear();
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            setHasOptionsMenu(true);
            //initialize list that holds results of search
            if(arrayListPLPDetails == null)
            {
                arrayListPLPDetails = new ArrayList<>();
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanse) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            //recycler view in case if no data is returned
            recyclerViewNoResult = (RecyclerView)rootView.findViewById(R.id.recycler_view_home_noresult);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerViewNoResult.setLayoutManager(linearLayoutManager);
            productListPageAdaptorNoView =  new ProductListPageAdaptorNoView();
            recyclerViewNoResult.setAdapter(productListPageAdaptorNoView);
            //recycler view if data is returned and handling configuration changes too
            recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view_home);
            GridLayoutManager gridLayoutManager;
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                gridLayoutManager = new GridLayoutManager(getActivity(),3);
            }
            else
               gridLayoutManager = new GridLayoutManager(getActivity(),2);
            recyclerView.setLayoutManager(gridLayoutManager);
           /* recyclerView.setOnScrollListener(new EndlessRecyclerView(gridLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    PlaceHolderFragment.query = PlaceHolderFragment.query + Tag.Page + current_page;
                    NetworkUtility.onProgressBarShow(getActivity());
                    MyAsyncTaskDownloadDetails myAsyncTaskDownloadDetails = new MyAsyncTaskDownloadDetails();
                    myAsyncTaskDownloadDetails.execute(new String[]{Tag.PLP_URL + PlaceHolderFragment.query, Tag.PLP});
                }
            });*/
            productListPageAdaptor = new ProductListPageAdaptor(LRUCacheClass.getCache());
            recyclerView.setAdapter(productListPageAdaptor);
            productListPageAdaptor.updateData(arrayListPLPDetails);
            //uses method that is declared in adaptor
            productListPageAdaptor.onListItemSelectedListener(new ProductListPageAdaptor.OnListItemSelected() {
                @Override
                public void onListItemSelected(String asinID,String title,String price) {
                    //starting PIP activity with necessary data
                    Intent i = new Intent(getActivity(), ProductInformationPageActivity.class);
                    i.putExtra(Tag.PIP_URL, Tag.PIP_URL + asinID);
                    i.putExtra(Tag.TITLE, title);
                    i.putExtra(Tag.PRICE, price);
                    startActivity(i);
                }
            });
            // if no data is returned then hide PLPRecyclerView otherwise hide PLPRecyclerNoResultView
            if(arrayListPLPDetails.size() == 0)
            {
                setPLPRecyclerNoResultView();
            }
            else
            {
                setPLPRecyclerView();
            }

           // recyclerView.setAdapter(new AlphaInAnimationAdapter(productListPageAdaptor));
            return rootView;
        }

        //function to hide ResultView and display NoResultView
        public static void setPLPRecyclerNoResultView()
        {
            recyclerView.setVisibility(View.GONE);
            recyclerViewNoResult.setVisibility(View.VISIBLE);
        }

        //function to hide NoResultView and display ResultView
        public static void setPLPRecyclerView()
        {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerViewNoResult.setVisibility(View.GONE);
        }

            @Override
        public void onCreateOptionsMenu(Menu menu,MenuInflater menuInflater) {
            // Inflate the menu; this adds items to the action bar if it is present.
            menuInflater.inflate(R.menu.menu_search, menu);
            MenuItem menuItem = menu.findItem(R.id.action_search);
            SearchView searchView = null;
            if (menuItem != null)
                searchView = (SearchView) menuItem.getActionView();

            if (searchView != null) {

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        //searching is done in async task
                        PlaceHolderFragment.query = query;
                        NetworkUtility.onProgressBarShow(getActivity());
                        MyAsyncTaskDownloadDetails myAsyncTaskDownloadDetails = new MyAsyncTaskDownloadDetails();
                        myAsyncTaskDownloadDetails.execute(new String[]{Tag.PLP_URL + PlaceHolderFragment.query, Tag.PLP});
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return true;
                    }
                });
            }
            super.onCreateOptionsMenu(menu, menuInflater);
        }
    }



}
