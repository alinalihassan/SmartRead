package com.teched.smartread;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.ViewHolder> {

    private List<TeacherBook> cards;
    public List<TeacherBook> visibleCards;
    private int rowLayout;
    private Context mContext;
    private String mPath;

    public TeacherAdapter(List<TeacherBook> cards, int rowLayout, Context context, String Path) {
        this.cards = cards;
        this.rowLayout = rowLayout;
        this.mContext = context;
        this.mPath = Path;
    }

    public String getID(int position) {
        return visibleCards.get(position).id;
    }
    public void flushFilter(){
        visibleCards = cards;
        setFilter("");
    }
    public void setFilter(String queryText) {
        JSONObject mainObject;
        visibleCards = new ArrayList<>();
        for (TeacherBook item: cards) {
            try {
                mainObject = new JSONObject(MainActivity.readFromFile(mPath + "/" + item.id + ".json"));
                if ((queryText.equals("") || mainObject.getString("Title").toLowerCase().contains(queryText.toLowerCase())))
                    visibleCards.add(item);
            } catch (Exception e) {e.printStackTrace();}
        }
        Collections.sort(visibleCards, new Comparator<TeacherBook>() {
            @Override
            public int compare(TeacherBook o1, TeacherBook o2) {
                try {
                    JSONObject mainObject = new JSONObject(MainActivity.readFromFile(mPath + "/" + o1.name + ".json"));
                    JSONObject mainObject2 = new JSONObject(MainActivity.readFromFile(mPath + "/" + o2.name + ".json"));
                    return mainObject.getString("Title").compareTo(mainObject2.getString("Title"));
                } catch (Exception e) {e.printStackTrace();}
                return 0;
            }
        });
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final TeacherBook card = visibleCards.get(i);
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
            cardName = (TextView) itemView.findViewById(R.id.bookName);
        }
    }
}