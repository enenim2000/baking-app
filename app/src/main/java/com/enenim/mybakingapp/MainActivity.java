package com.enenim.mybakingapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.enenim.mybakingapp.config.Constants;
import com.enenim.mybakingapp.model.Recipe;
import com.enenim.mybakingapp.rest.ApiClient;
import com.enenim.mybakingapp.rest.ApiInterface;
import com.enenim.mybakingapp.rest.InternetUtil;
import com.enenim.mybakingapp.util.CommonUtil;
import com.enenim.mybakingapp.util.RetrofitUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements Constants, RetrofitUtil.RequestCallBack {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    private List<Recipe> recipes;
    private DataAdapter dataAdapter;
    private ApiInterface apiService;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @BindView(R.id.recycler_view_recipe)
    RecyclerView recyclerView;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    @BindView(R.id.retrofit_error_text_view)
    TextView retrofitErrorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Timber.d("Activity Created");

        boolean connected = InternetUtil.isNetworkConnected(this);

        if(!connected){
            InternetUtil.showDialog(this, android.R.drawable.ic_dialog_alert, R.string.no_network_message)
                    .setPositiveButton(R.string.action_settings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);

                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivity(intent);
                            }else{
                                InternetUtil.showDialog(MainActivity.this, android.R.drawable.ic_dialog_alert, R.string.no_setting_app)
                                        .show();
                            }
                        }
                    })
                    .show();
            return;
        }

        context = getApplicationContext();
        dataAdapter = new DataAdapter();
        dataAdapter.setContext(context);
        dataAdapter.setRecipes(null);

        if (savedInstanceState != null) {
            recipes = savedInstanceState.getParcelableArrayList(KEY_RECIPE_LIST);
            initViews(recipes);
        } else {
            mLoadingIndicator.setVisibility(View.VISIBLE);

            Timber.i("About to make rest call to api service");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_RECIPE_LIST, (ArrayList<? extends Parcelable>) recipes);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        setLayoutManager();
    }

    @Override
    public void onResume(){
        super.onResume();
        //TODO
    }

    public void setLayoutManager(){
        if(CommonUtil.isXLargeScreen(this)){
            layoutManager = new GridLayoutManager(context, 3);
        }else if(this.getResources().getBoolean(R.bool.is_landscape)){
            layoutManager = new GridLayoutManager(context, 2);
        }else {
            layoutManager = new GridLayoutManager(context, 1);
        }
    }

    private void initViews(final List<Recipe> recipes){
        this.recipes = recipes;
        recyclerView.setHasFixedSize(true);
        setLayoutManager();
        recyclerView.setLayoutManager(layoutManager);
        dataAdapter.setRecipes(recipes);
        recyclerView.setAdapter(dataAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Recipe recipe = recipes.get(position);

                Intent intent = new Intent(MainActivity.this, RecipeStepDescriptionHostActivity.class);
                Bundle bundle = new Bundle();

                bundle.putParcelable(KEY_RECIPE, recipe);
                intent.putExtras(bundle);

                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));

    }

    @Override
    public void onDone(ArrayList<Recipe> _recipes) {
        initViews(_recipes);

        Timber.i("Number of recipes received: " + recipes.size());
    }

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public SimpleIdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onStart() {
        super.onStart();

        //getIdlingResource();

        apiService = ApiClient.getClient().create(ApiInterface.class);

        RetrofitUtil retrofitUtil = new RetrofitUtil();
        retrofitUtil.setApiService(apiService);
        retrofitUtil.setmLoadingIndicator(mLoadingIndicator);
        retrofitUtil.setRecyclerView(recyclerView);
        retrofitUtil.setMessageTextView(retrofitErrorTextView);

        retrofitUtil.processRequest(this, getIdlingResource());
    }

}