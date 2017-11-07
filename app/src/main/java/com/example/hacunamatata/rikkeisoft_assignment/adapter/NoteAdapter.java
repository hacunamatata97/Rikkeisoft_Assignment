package com.example.hacunamatata.rikkeisoft_assignment.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.hacunamatata.rikkeisoft_assignment.R;
import com.example.hacunamatata.rikkeisoft_assignment.controller.Controller;
import com.example.hacunamatata.rikkeisoft_assignment.model.Note;
import com.example.hacunamatata.rikkeisoft_assignment.realm.RealmController;
import com.squareup.picasso.Picasso;
import io.realm.Realm;

public class NoteAdapter extends RealmRecyclerViewAdapter<Note> {

    final Context context;
    private Realm realm;
    private LayoutInflater layoutInflater;

    public NoteAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notes, parent, false);
        return new CardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        realm = RealmController.getInstance().getRealm();
        // Get note
        final Note note = getItem(position);

        // Convert to CardView
        final CardViewHolder cardViewHolder = (CardViewHolder) holder;

        // Set components
        cardViewHolder.tvTitle.setText(note.getTitle());

        if (note.getAlarm() != null) {
            cardViewHolder.tvAlarm.setText(note.getAlarm());
        }

        cardViewHolder.tvContent.setText(note.getContent());

        if (note.getImageUrl() != null && !note.getImageUrl().trim().equals("")) {
            Picasso.with(context)
                    .load(note.getImageUrl().replace("https", "http"))
                    .noPlaceholder()
                    .centerCrop()
                    .resize(100, 100)
                    .into(cardViewHolder.ivPicture);
        }

        // Remove item
        Controller.removeItem(context, this, cardViewHolder, realm, position);

        Controller.editItem(context, this, cardViewHolder, note, realm, position);
    }

    @Override
    public int getItemCount() {
        return getRealmAdapter() != null ? getRealmAdapter().getCount() : 0;
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;
        ImageView ivPicture;
        TextView tvTitle;
        TextView tvContent;
        TextView tvAlarm;

        CardViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_notes);
            ivPicture = itemView.findViewById(R.id.iv_picture);
            tvTitle = itemView.findViewById(R.id.tv_item_title);
            tvContent = itemView.findViewById(R.id.tv_item_content);
            tvAlarm = itemView.findViewById(R.id.tv_item_alarm);
        }
    }
}
