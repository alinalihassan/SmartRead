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

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private List<Users> cards;
    public List<Users> visibleCards;
    private int rowLayout;
    private Context mContext;

    public UsersAdapter(List<Users> cards, int rowLayout, Context context) {
        this.cards = cards;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }

    public void flushFilter(){
        visibleCards = cards;
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
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public int getItemCount() {
        return visibleCards == null ? 0 : visibleCards.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cardName;
        public ImageView cardImage;

        public ViewHolder(View itemView) {
            super(itemView);
            cardName = (TextView) itemView.findViewById(R.id.className);
            cardImage = (ImageView) itemView.findViewById(R.id.card_image);
        }
    }
}