package codepath.com.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import codepath.com.nytimessearch.Article;
import codepath.com.nytimessearch.ArticleArrayAdapter;
import codepath.com.nytimessearch.EndlessScrollListener;
import codepath.com.nytimessearch.FragmentFilter;
import codepath.com.nytimessearch.R;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity implements FragmentFilter.FilterDialogListener {

    EditText etQuery;
    GridView gvResults;
    Button btnSearch;
    public String orderBy;
    public String query;
    public String newsDeskItems;
    public RequestParams params;
    public String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";

    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupViews();

    }

    public void setupViews(){
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter); // set adapter to gridview

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                GetArticles();
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });


        // add click listener for grid click
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // create an intent to display the article
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                // get the article to display
                Article article = articles.get(position);
                // pass in that article into intent
                intent.putExtra("article", article);
                // launch the activity.
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onArticleSearch(View view) {

        this.query = etQuery.getText().toString();
        this.params = new RequestParams();
        params.put("api-key", "f3401d347c764c40a5145e572a2b4600");
        params.put("page", 0);
        params.put("q", query);
        GetArticles();
    }


    public void onArticleFilter(View view){
        FragmentManager fm = getSupportFragmentManager();
        FragmentFilter filterFragment = FragmentFilter.newInstance("Filter Search");
        filterFragment.show(fm, "fragment_filter");
        //   Toast.makeText(getApplicationContext(), "HI THERE IM A FAB", Toast.LENGTH_LONG).show();
    }

    public void onFinishFilterDialog(String orderBy, String newsDeskItems){
        this.orderBy = orderBy;
        this.newsDeskItems = newsDeskItems;
        query = etQuery.getText().toString();

        this.params = new RequestParams();
        params.put("api-key", "f3401d347c764c40a5145e572a2b4600");
        params.put("news_desk", newsDeskItems);
        params.put("sort", orderBy);
        params.put("page", 0);
        params.put("q", query);

        this.params = params;

        GetArticles();


    }

    // 3. This method is invoked in the activity when the listener is triggered
    // Access the data result passed to the activity here
    @Override
    public void onFinishEditDialog(String inputText) {
        Toast.makeText(this, "Hi, " + inputText, Toast.LENGTH_SHORT).show();
    }

    public void GetArticles(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(this.url, this.params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) { // NY Times api sends back JSONObject, others use JSONArray
                JSONArray articleJsonResults = null;
                try{
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(articleJsonResults)); // modify the adapter instead of view so that data changes immediately
                    adapter.notifyDataSetChanged();
                    Log.d("DEBUG", articles.toString());
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }


}
