package com.teched.smartread;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private List<Question> cards;
    public List<Question> visibleCards;
    private int rowLayout;
    private Context mContext;

    public QuestionAdapter(List<Question> cards, int rowLayout, Context context) {
        this.cards = cards;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }

    public void flushFilter(){
        visibleCards = cards;
        Collections.sort(visibleCards, new Comparator<Question>() {
            @Override
            public int compare(Question o1, Question o2) {
                return (int)(o1.sum/o1.entries) - (int)(o2.sum/o2.entries);
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
        final Question card = visibleCards.get(i);
        viewHolder.cardName.setText(card.QuestionString);
        viewHolder.cardProgress.setProgress((int) (card.sum / card.entries));
    }

    @Override
    public int getItemCount() {
        return visibleCards == null ? 0 : visibleCards.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cardName;
        public ArcProgress cardProgress;

        public ViewHolder(View itemView) {
            super(itemView);
            cardName = (TextView) itemView.findViewById(R.id.bookName);
            cardProgress = (ArcProgress) itemView.findViewById(R.id.classProgress);
        }
    }
}