package com.example.android.newsapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    /**
     * Constructs a new {@link NewsAdapter}.
     *
     * @param context of the app
     * @param news is the list of earthquakes, which is the data source of the adapter
     */
    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        News currentNews = getItem(position);

        // Check if the existing view is being reused, otherwise inflate the view
        // null is when a screen was first loaded (never cached)
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list, parent, false); // Inflater translates XML files to actual view objects
        }

        TextView newsTitleTextView = (TextView) listItemView.findViewById(R.id.news_title);
        newsTitleTextView.setText(currentNews.getNewsTitle());

        TextView newsCategoryTextView = (TextView) listItemView.findViewById(R.id.news_category);
        newsCategoryTextView.setText(currentNews.getNewsCategory());

        TextView newsDateTextView = (TextView) listItemView.findViewById(R.id.news_date);
        newsDateTextView.setText(currentNews.getNewsDate());

        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author);

        if (currentNews.hasAuthor()) {
            authorTextView.setText(currentNews.getmAuthor());
            authorTextView.setVisibility(View.VISIBLE);
        }
        else {
            authorTextView.setVisibility(View.GONE);
        }

        return listItemView;
    }

}
