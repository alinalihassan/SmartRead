package com.teched.smartread;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {

    private List<Class> cards;
    public List<Class> visibleCards;
    private int rowLayout;
    private Context mContext;

    public ClassAdapter(List<Class> cards, int rowLayout, Context context) {
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
        final Class card = visibleCards.get(i);
        viewHolder.cardName.setText(card.name);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(viewHolder.cardName.getText().toString().length()>0?String.valueOf(viewHolder.cardName.getText().toString().charAt(0)):"A", calculateColor(viewHolder.cardName.getText().toString()));
        viewHolder.cardImage.setImageDrawable(drawable);
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
    public int calculateColorBase(String name) {
        String opacity = "#ff";
        String hexColor = String.format(
                opacity + "%06X", (0xeeeeee & name.hashCode()));

        return Color.parseColor(hexColor);
    }

    public int calculateColor(String name) {

        ShapeDrawable drawable = new ShapeDrawable(new RectShape());

        drawable.getPaint().setColor(calculateColorBase(name));
        drawable.setIntrinsicHeight(2);
        drawable.setIntrinsicWidth(2);

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        Palette palette = Palette.generate(bitmap);

        return palette.getVibrantColor(0xff00bcd4);
    }
}