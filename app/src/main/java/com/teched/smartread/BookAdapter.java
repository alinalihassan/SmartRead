package com.teched.smartread;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
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
        setFilter("");
    }
    public void setFilter(String queryText) {
        visibleCards = new ArrayList<>();
        for (Book item: cards) {
            if (queryText.equals("") || item.name.toLowerCase().contains(queryText.toLowerCase()) || item.author.toLowerCase().contains(queryText.toLowerCase()))
                visibleCards.add(item);
        }
        notifyDataSetChanged();
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
        viewHolder.cardPrice.setText(MainActivity.bp.getPurchaseListingDetails(card.id).priceText);
    }

    @Override
    public int getItemCount() {
        return visibleCards == null ? 0 : visibleCards.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cardName;
        public TextView cardAuthor;
        public TextView cardPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            cardName = (TextView) itemView.findViewById(R.id.bookName);
            cardAuthor = (TextView) itemView.findViewById(R.id.bookAuthor);
            cardPrice = (TextView) itemView.findViewById(R.id.bookPrice);
        }
    }
}