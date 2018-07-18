package com.example.mariakovaleva.mariasnewsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News>{

    private View listItemView;

    private static final String DATE_SEPARATOR = "T";
    private static final String TIME_SEPARATOR = "Z";

    public NewsAdapter(@NonNull Activity context, ArrayList<News> newsArrayList) {
        super(context, 0, newsArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list, parent, false);
        }

        final News currentNews = getItem(position);

        TextView nameTextView = (TextView) listItemView.findViewById(R.id.author_name_text_view);
        nameTextView.setText(currentNews.getAuthorName());

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_text_view);
        dateTextView.setText(getDate(currentNews.getPublicationDateAndTime()));

        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time_text_view);
        timeTextView.setText(getTime(currentNews.getPublicationDateAndTime()));

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title_text_view);
        titleTextView.setText(currentNews.getWebTitle());

        TextView textTextView = (TextView) listItemView.findViewById(R.id.text_text_view);
        textTextView.setText(currentNews.getTrailText());

        TextView categoryTextView = (TextView) listItemView.findViewById(R.id.category_text_view);
        categoryTextView.setText("#" + currentNews.getSectionId());

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(currentNews.getUrl()));
                getContext().startActivity(intent);
            }
        });
            return listItemView;
            }

        private String getDate(String dateAndTime) {
        return dateAndTime.substring(0, dateAndTime.indexOf(DATE_SEPARATOR));
        }

        private String getTime(String dateAndTime) {
        return dateAndTime.substring(dateAndTime.indexOf(DATE_SEPARATOR),
                dateAndTime.indexOf(TIME_SEPARATOR) - 4);
        }

}
