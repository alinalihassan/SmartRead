package com.teched.smartread;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.yqritc.recyclerviewflexibledivider.FlexibleDividerDecoration;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

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

    public String getName(int position) {
        return visibleCards.get(position).name;
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
    }

    @Override
    public int getItemCount() {
        return visibleCards == null ? 0 : visibleCards.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cardName;

        public ViewHolder(View itemView) {
            super(itemView);
            cardName = (TextView) itemView.findViewById(R.id.className);
        }
    }
}