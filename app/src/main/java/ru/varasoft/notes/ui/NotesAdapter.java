package ru.varasoft.notes.ui;


import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;

import ru.varasoft.notes.data.Note;
import ru.varasoft.notes.data.NotesSource;
import ru.varasoft.notes.R;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private NotesSource dataSource;
    private OnItemClickListener itemClickListener;
    private final Fragment fragment;

    private int menuPosition;

    public int getMenuPosition() {
        return menuPosition;
    }

    public NotesAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setDataSource(NotesSource dataSource){
        this.dataSource = dataSource;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder viewHolder, int i) {
        viewHolder.setData(dataSource.getNoteData(i));
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public void SetOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView note;
        private TextView creationDateTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            note = itemView.findViewById(R.id.item_note);
            creationDateTime = itemView.findViewById(R.id.item_creation_date_time);

            registerContextMenu(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    menuPosition = getLayoutPosition();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        itemView.showContextMenu(10, 10);
                    } else {
                        itemView.showContextMenu();
                    }
                    return true;
                }
            });

        }

        private void registerContextMenu(@NonNull View itemView) {
            if (fragment != null){
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        menuPosition = getLayoutPosition();
                        return false;
                    }
                });

                fragment.registerForContextMenu(itemView);
            }
        }

        public void setData(Note noteData) {
            title.setText(noteData.getTitle());
            note.setText(noteData.getText());

            SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy hh:mm");

            String strDt = simpleDate.format(noteData.getCreationDateTime());
            creationDateTime.setText(String.format("Создано: %s", strDt));
        }
    }
}