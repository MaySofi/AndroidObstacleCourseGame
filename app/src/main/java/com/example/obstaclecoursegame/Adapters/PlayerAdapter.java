package com.example.obstaclecoursegame.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.obstaclecoursegame.Interfaces.PlayerCallback;
import com.example.obstaclecoursegame.Models.Player;
import com.example.obstaclecoursegame.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Locale;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {
    private final ArrayList<Player> players;
    private PlayerCallback playerCallback;

    public PlayerAdapter(ArrayList<Player> players) {
        this.players = players;
    }

    public void setPlayerCallback(PlayerCallback playerCallback) {
        this.playerCallback = playerCallback;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_item, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player player = getItem(position);
        holder.player_LBL_name.setText(player.getName());
        holder.player_LBL_score.setText(String.format(Locale.US,"%d", player.getScore()));
    }

    @Override
    public int getItemCount() {
        return this.players == null ? 0 : this.players.size();
    }

    private Player getItem(int position) {
        return this.players.get(position);
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder {
        private final MaterialTextView player_LBL_name;
        private final MaterialTextView player_LBL_score;
        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            this.player_LBL_name = itemView.findViewById(R.id.player_LBL_name);
            this.player_LBL_score = itemView.findViewById(R.id.player_LBL_score);

            itemView.setOnClickListener(view -> {
                if(playerCallback != null)
                    playerCallback.itemClicked(getItem(getAdapterPosition()), getAdapterPosition());
            });
        }
    }
}
