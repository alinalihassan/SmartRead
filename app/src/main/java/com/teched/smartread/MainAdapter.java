package com.teched.smartread;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONObject;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private String Type = "Library";
    private SharedPreferences prefs;
    private List<Card> cards;
    public List<Card> visibleCards;
    private int rowLayout;
    private Context mContext;
    private String mPath;

    public MainAdapter(List<Card> cards, int rowLayout, Context context, String Path) {
        this.cards = cards;
        this.rowLayout = rowLayout;
        this.mContext = context;
        this.mPath = Path;
        this.prefs = mContext.getSharedPreferences("com.teched.smartread", Context.MODE_PRIVATE);
        flushFilter();
    }

    public void setType(String type) {
        this.Type = type;
    }

    public boolean hasType(Card item){
        switch (Type) {
            case "Library":
                return true;
            case "Favorites":
                try {
                    JSONObject mainObject = new JSONObject(MainActivity.readFromFile(mPath + "/" + item.name + ".json"));
                    if (mainObject.getBoolean("Favorite"))
                        return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            case "Started":
                JSONObject mainObject;
                try {
                    mainObject = new JSONObject(MainActivity.readFromFile(mPath + "/" + item.name + ".json"));
                    return !mainObject.getBoolean("Finished") && mainObject.getInt("LastPage") != 0;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
        }
        return false;
    }

    public void flushFilter(){
        visibleCards = cards;
        setFilter("");
    }

    public void setFilter(String queryText) {
        visibleCards = new ArrayList<Card>();
        for (Card item: cards) {
            if ((queryText.equals("") || item.name.toLowerCase().contains(queryText.toLowerCase()) || item.author.toLowerCase().contains(queryText.toLowerCase())) && hasType(item))
                visibleCards.add(item);
        }
        Collections.sort(visibleCards, new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                if (prefs.getInt("pref_sort", 1) == 1)
                    try {
                        JSONObject mainObject = new JSONObject(MainActivity.readFromFile(mPath + "/" + o1.name + ".json"));
                        JSONObject mainObject2 = new JSONObject(MainActivity.readFromFile(mPath + "/" + o2.name + ".json"));
                        return mainObject.getString("Title").compareTo(mainObject2.getString("Title"));
                    } catch (Exception ignored) {}
                else if(prefs.getInt("pref_sort", 1) == 2) {
                    try {
                        JSONObject mainObject = new JSONObject(MainActivity.readFromFile(mPath + "/" + o1.name + ".json"));
                        JSONObject mainObject2 = new JSONObject(MainActivity.readFromFile(mPath + "/" + o2.name + ".json"));
                        return mainObject.getString("Author").compareTo(mainObject2.getString("Author"));
                    } catch (Exception ignored) {}
                }
                return 0;
            }
        });
        notifyDataSetChanged();
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
        final Card card = visibleCards.get(i);
        try {
            JSONObject mainObject = new JSONObject(MainActivity.readFromFile(mPath + "/" + card.name + ".json"));
            viewHolder.cardAuthor.setText(mainObject.getString("Author"));
            viewHolder.cardName.setText(mainObject.getString("Title"));
            card.author = mainObject.getString("Author");
            viewHolder.cardHeart.setAlpha(mainObject.getBoolean("Favorite") ? 1.0f : 0.0f);
        } catch(Exception ignore) {}
    }

    @Override
    public int getItemCount() {
        return visibleCards == null ? 0 : visibleCards.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cardName;
        public TextView cardAuthor;
        public ImageView cardHeart;

        public ViewHolder(View itemView) {
            super(itemView);
            cardName = (TextView) itemView.findViewById(R.id.cardName);
            cardAuthor = (TextView) itemView.findViewById(R.id.cardAuthor);
            cardHeart = (ImageView) itemView.findViewById(R.id.heart);
        }
    }
}