package com.apress.mediaplayer;

import android.content.Context;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

/**
 * Created by Paul on 9/17/15.
 */
public class CardPresenter extends Presenter {

    static class ViewHolder extends Presenter.ViewHolder {
        private ImageCardView mCardView;

        public ViewHolder(View view) {
            super(view);
            mCardView = (ImageCardView) view;
        }

        public ImageCardView getCardView() {
            return mCardView;
        }

        public void updateCardViewImage( Context context, String link ) {
            Picasso.with(context).load(link).resize(210, 210).centerCrop().into(mCardView.getMainImageView());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        ImageCardView cardView = new ImageCardView( parent.getContext() );
        cardView.setFocusable( true );
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        Video video = (Video) item;

        if ( !TextUtils.isEmpty(video.getPoster()) ) {
            ((ViewHolder) viewHolder).mCardView.setTitleText(video.getTitle());
            ((ViewHolder) viewHolder).mCardView.setMainImageDimensions(
                    210,
                    210 );
            ( (ViewHolder) viewHolder ).updateCardViewImage( ( (ViewHolder) viewHolder ).getCardView().getContext(), video.getPoster() );
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
    }

    @Override
    public void onViewAttachedToWindow(Presenter.ViewHolder viewHolder) {
    }
}
