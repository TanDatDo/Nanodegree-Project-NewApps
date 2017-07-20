package com.dan.newapps;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Dat T Do on 7/19/2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    /**
     * Constructs a new {@link NewsAdapter}.
     *
     * @param context of the app
     * @param listOfNews  is the list of news, which is the data source of the adapter
     */
    public NewsAdapter(Context context, ArrayList<News> listOfNews) {
        super(context, 0, listOfNews);
    }

    /**
     * Returns a list item view that displays information about the news at the given position
     * in the list of news
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
            viewHolder = new ViewHolder();
            //Find the ImageView view by ID
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
            //Find the titleTextView by ID
            viewHolder.titleTextView = (TextView) convertView.findViewById(R.id.title_text_view);
            //Find the sectionTextView by ID
            viewHolder.sectionTextView = (TextView) convertView.findViewById(R.id.section_text_view);
            //find the dateTextView by ID
            viewHolder.dateTextView=(TextView) convertView.findViewById(R.id.date_text_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Find the news at the given position in the list of books
        final News currentNews = getItem(position);

        //Display the image of the current news in that ImageView
        String imageURl = currentNews.getImageUrl();
        if (imageURl.isEmpty()) {
            viewHolder.imageView.setImageResource(R.drawable.newspaper);
        } else {
            // set image to image view from image url
            new DownloadImageAsyncTask(viewHolder.imageView)
                    .execute(imageURl);
        }

        //Display the title of the current news in the title textview
        viewHolder.titleTextView.setText(currentNews.getTitle());

        //Display the section name of the current news in the section textview
        viewHolder.sectionTextView.setText("Section: "+currentNews.getSection());

        String publishTime= currentNews.getPublishTime();
        //modify the string to get the desire format of date and time
        String [] separate=publishTime.split("T");
        String date=separate[0];
        String time=separate[1].replace("Z","");
        //Display the date of the current new in the date textview
        viewHolder.dateTextView.setText("Publish on " +date+" at "+time);

        //navigate the the website when one single item in the listview is clicked
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the url of the website from current news object
                String Url= currentNews.getWebUrl();
                //sent intent to the website
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(Url));
                getContext().startActivity(intent);
            }
        });

        // Return the list item view that is now showing the appropriate data
        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView sectionTextView;
        TextView dateTextView;
    }

    //using Async Task to dowdload image from image url
    private class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        ImageView bmImage;

        public DownloadImageAsyncTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];

            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            bmImage.setVisibility(View.VISIBLE);
        }
    }
}
