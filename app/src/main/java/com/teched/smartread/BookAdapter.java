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

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private List<Book> cards;
    public List<Book> visibleCards;
    private int rowLayout;
    private Context mContext;

    public BookAdapter(List<Book> cards, int rowLayout, Context context) {
        this.cards = cards;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }

    public void flushFilter(){
        visibleCards = cards;
    }

    public String getID(int position) {
        return visibleCards.get(position).id;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final Book card = visibleCards.get(i);
        viewHolder.cardName.setText(card.name);
        viewHolder.cardAuthor.setText(card.author);
    }

    @Override
    public int getItemCount() {
        return visibleCards == null ? 0 : visibleCards.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cardName;
        public TextView cardAuthor;

        public ViewHolder(View itemView) {
            super(itemView);
            cardName = (TextView) itemView.findViewById(R.id.bookName);
            cardAuthor = (TextView) itemView.findViewById(R.id.bookAuthor);
        }
    }
}