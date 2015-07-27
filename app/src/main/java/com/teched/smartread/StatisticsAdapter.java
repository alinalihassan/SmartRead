package com.teched.smartread;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arasthel.asyncjob.AsyncJob;
import com.github.lzyzsd.circleprogress.ArcProgress;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ViewHolder> {

    private List<Users> cards;
    public List<Users> visibleCards;
    private int rowLayout;
    private Context mContext;

    public StatisticsAdapter(List<Users> cards, int rowLayout, Context context) {
        this.cards = cards;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }

    public void flushFilter(){
        visibleCards = cards;
        Collections.sort(visibleCards, new Comparator<Users>() {
            @Override
            public int compare(Users o1, Users o2) {
                return o2.answerRate - o1.answerRate;
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final Users card = visibleCards.get(i);
        viewHolder.cardName.setText(card.name);
        try {
            ContextWrapper cw = new ContextWrapper(mContext);
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File file = new File(directory, card.id + ".jpg");
            if (file.exists()) {
                viewHolder.cardImage.setImageBitmap(Utils.getRoundedCornerBitmap(BitmapFactory.decodeStream(new FileInputStream(file))));
            } else {
                AsyncJob.doInBackground(new AsyncJob.OnBackgroundJob() {
                    @Override
                    public void doOnBackground() {
                        Bitmap profile = null;
                        try {
                            URL profileURL = new URL(card.profileUrl);
                            profile = BitmapFactory.decodeStream(profileURL.openConnection().getInputStream());
                            MainActivity.saveToInternalStorage(mContext, profile, card.id);
                        } catch (Exception e) {e.printStackTrace(); }
                        final Bitmap finalProfile = profile;
                        AsyncJob.doOnMainThread(new AsyncJob.OnMainThreadJob() {
                            @Override
                            public void doInUIThread() {
                                if(finalProfile!=null)
                                    viewHolder.cardImage.setImageBitmap(Utils.getRoundedCornerBitmap(finalProfile));
                            }
                        });
                    }
                });
            }
            viewHolder.classProgress.setProgress(card.answerRate);
            viewHolder.cardStatus.setText(card.finished?"Status: Finished":(card.maxPage.equals("0")?"Status: Not Started":"Status: Page " + card.maxPage));
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public int getItemCount() {
        return visibleCards == null ? 0 : visibleCards.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cardName;
        public TextView cardStatus;
        public ImageView cardImage;
        public ArcProgress classProgress;

        public ViewHolder(View itemView) {
            super(itemView);
            cardName = (TextView) itemView.findViewById(R.id.className);
            cardStatus = (TextView) itemView.findViewById(R.id.classStatus);
            cardImage = (ImageView) itemView.findViewById(R.id.card_image);
            classProgress = (ArcProgress) itemView.findViewById(R.id.classProgress);
        }
    }
}