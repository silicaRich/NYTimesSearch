package codepath.com.nytimessearch;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by floko_000 on 7/30/2016.
 */
public class ArticleArrayAdapter extends ArrayAdapter<Article> {
    public ArticleArrayAdapter(Context context, List<Article> articles){
        super(context, android.R.layout.simple_list_item_1);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get data item for current position
        Article article = this.getItem(position);
        // check to see if existing view is being used or recycled
        // see http://guides.codepath.com/android/Endless-Scrolling-with-AdapterViews-and-RecyclerView
        // not using a recucled view? then inflate layout
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);

        }
        // find image view
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);
        // clear out recycled image from convertView from last time
        imageView.setImageResource(0);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);

        tvTitle.setText(article.getHeadline());

        // populate thumbnail image using Picasso :)
        // see http://guides.codepath.com/android/Displaying-Images-with-the-Picasso-Library
        // remote download the image in the background

        String thumbnail = article.getThumbnail();
        if(!TextUtils.isEmpty(thumbnail)){
            Picasso.with(getContext()).load(thumbnail).into(imageView);
        }

        return convertView; // always return view :)

    }
}
