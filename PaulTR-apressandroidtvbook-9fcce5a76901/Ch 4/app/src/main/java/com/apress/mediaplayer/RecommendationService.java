package com.apress.mediaplayer;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 11/22/15.
 */
public class RecommendationService extends IntentService {
    private int MAX_RECOMMENDATIONS = 3;
    private List<Video> mVideos;

    public RecommendationService() {
        super("RecommendationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        loadData();
        NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        int numOfRecommendations = MAX_RECOMMENDATIONS;

        if( mVideos == null ) {
            return;
        } else if( mVideos.size() < MAX_RECOMMENDATIONS ){
            numOfRecommendations = mVideos.size();
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder( getApplicationContext() )
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLocalOnly(true)
                .setOngoing(true)
                .setColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black))
                .setCategory(Notification.CATEGORY_RECOMMENDATION);

        for(int i = 0; i < numOfRecommendations; i++ ) {
            Video video = mVideos.get( i );
            Bitmap bitmap;
            try {
                bitmap = Picasso.with(this).load(video.getPoster()).resize(313, 176).get();
            } catch( IOException e ) {
                continue;
            }
            builder.setPriority(numOfRecommendations - i)
                    .setContentTitle(video.getTitle())
                    .setContentText(video.getCategory())
                    .setLargeIcon(bitmap)
                    .setContentIntent(buildPendingIntent(video, i + 1));

            notificationManager.notify( i + 1, builder.build() );
        }
    }

    private PendingIntent buildPendingIntent(Video video, long id ) {
        Intent detailsIntent = new Intent(this, VideoDetailsActivity.class);
        detailsIntent.putExtra(VideoDetailsFragment.EXTRA_VIDEO, video);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(VideoDetailsActivity.class);
        stackBuilder.addNextIntent(detailsIntent);

        detailsIntent.setAction(Long.toString(id));

        PendingIntent intent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        return intent;
    }

    private void loadData() {
        String json = Utils.loadJSONFromResource( getApplicationContext(), R.raw.videos );
        Type collection = new TypeToken<ArrayList<Video>>(){}.getType();

        Gson gson = new Gson();
        mVideos = gson.fromJson( json, collection );
    }
}
