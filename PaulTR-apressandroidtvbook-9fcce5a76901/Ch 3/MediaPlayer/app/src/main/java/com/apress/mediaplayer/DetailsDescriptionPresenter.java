package com.apress.mediaplayer;

import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;

/**
 * Created by Paul on 9/27/15.
 */
public class DetailsDescriptionPresenter extends AbstractDetailsDescriptionPresenter {
    @Override
    protected void onBindDescription(AbstractDetailsDescriptionPresenter.ViewHolder viewHolder, Object item) {
        Video video = (Video) item;

        if (video != null) {
            viewHolder.getTitle().setText(video.getTitle());
            viewHolder.getSubtitle().setText(video.getCategory());
            viewHolder.getBody().setText(video.getDescription());
        }
    }
}
