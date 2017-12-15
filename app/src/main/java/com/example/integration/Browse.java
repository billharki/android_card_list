package com.example.integration;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Browse extends AppCompatActivity {

    static final int LIST_VIEW_MODE = 0;
    static final int GRID_VIEW_MODE = 1;
    private ViewStub listStub;
    private ViewStub gridStub;
    private ListView listView;
    private GridView gridView;
    private CardView cardView;
    private ListViewAdapter listViewAdapter;
    private GridViewAdapter gridViewAdapter;
    private CardViewAdapter cardViewAdapter;
    private RecyclerView recyclerCardView;
    private TextView mTextMessage;
    private List<Product> productList = new ArrayList<>();
    AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getApplicationContext(), productList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
        }
    };
    private RecyclerView recyclerView;
    private ProductAdapter pAdapter;
    private Button switchViewBtn;
    private int currentViewMode = 1;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_browse:
                    mTextMessage.setText(R.string.text_browse);
                    return true;
                case R.id.navigation_alerts:
                    mTextMessage.setText(R.string.text_alerts);
                    return true;
                case R.id.navigation_settings:
                    mTextMessage.setText(R.string.text_settings);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        switchViewBtn = (Button) findViewById(R.id.switchBtn);


        //for scrolling one item per scroll
        SnapHelper snapHelper = new PagerSnapHelper();

        pAdapter = new ProductAdapter(productList);
        RecyclerView.LayoutManager pLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(pLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pAdapter);
        recyclerView.addItemDecoration(new LinePagerIndicatorDecoration());

        listStub = (ViewStub) findViewById(R.id.listStub);
        gridStub = (ViewStub) findViewById(R.id.gridStub);

        listStub.inflate();
        gridStub.inflate();

        cardViewAdapter = new CardViewAdapter(this, productList);
        recyclerCardView = (RecyclerView) findViewById(R.id.card_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerCardView.setLayoutManager(mLayoutManager);
        recyclerCardView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerCardView.setItemAnimator(new DefaultItemAnimator());
        recyclerCardView.setAdapter(cardViewAdapter);

        listView = (ListView) findViewById(R.id.listView);
//        gridView = (GridView) findViewById(R.id.gridView);
        cardView = (CardView) findViewById(R.id.card_view);

        prepareProductData();

        SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
        currentViewMode = sharedPreferences.getInt("currentViewMode", GRID_VIEW_MODE);

        listView.setOnItemClickListener(onItemClick);
//        gridView.setOnItemClickListener(onItemClick);

        switchView();

//        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        switchViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("browse", "btn clicked");
                switchView();
            }
        });

    }

    private void prepareProductData() {
        productList.add(new Product(R.drawable.dog1, "Title 1", "Description 1"));
        productList.add(new Product(R.drawable.dog1, "Title 2", "Description 2"));
        productList.add(new Product(R.drawable.dog1, "Title 3", "Description 3"));
        productList.add(new Product(R.drawable.dog1, "Title 4", "Description 4"));
        productList.add(new Product(R.drawable.dog1, "Title 5", "Description 5"));
    }

    private void switchView() {
        Log.d("browse", "switchView with " + currentViewMode);
        if (LIST_VIEW_MODE == currentViewMode) {
            currentViewMode = GRID_VIEW_MODE;
            Drawable listIcon = getApplicationContext().getResources().getDrawable(R.drawable.list_view);
            listIcon.setBounds(0, 0, 60, 60);
            switchViewBtn.setCompoundDrawables(listIcon, null, null, null);
            listStub.setVisibility(View.GONE);
            gridStub.setVisibility(View.VISIBLE);
        } else {
            currentViewMode = LIST_VIEW_MODE;
            Drawable gridIcon = getApplicationContext().getResources().getDrawable(R.drawable.tile_view);
            gridIcon.setBounds(0, 0, 60, 60);
            switchViewBtn.setCompoundDrawables(gridIcon, null, null, null);
            listStub.setVisibility(View.VISIBLE);
            gridStub.setVisibility(View.GONE);

        }

        setAdapters();
    }

    private void setAdapters() {
        if (LIST_VIEW_MODE == currentViewMode) {
            listViewAdapter = new ListViewAdapter(this, R.layout.list_item, productList);
            listView.setAdapter(listViewAdapter);
        }
        if (GRID_VIEW_MODE == currentViewMode) {
            cardViewAdapter = new CardViewAdapter(this, productList);
            recyclerCardView.setAdapter(cardViewAdapter);
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}
