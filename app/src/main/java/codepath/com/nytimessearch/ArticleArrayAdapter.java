package codepath.com.nytimessearch;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by floko_000 on 7/30/2016.
 */
public class ArticleArrayAdapter extends ArrayAdapter<Article> {
    public ArticleArrayAdapter(Context context, List<Article> articles){
        super(context, android.R.layout.simple_list_item_1);

    }
}
