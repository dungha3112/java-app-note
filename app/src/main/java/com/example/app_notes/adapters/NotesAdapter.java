package com.example.app_notes.adapters;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_notes.R;
import com.example.app_notes.entities.Note;
import com.example.app_notes.listeners.NotesListeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private List<Note> notes;
    private NotesListeners notesListeners ;
    private Timer timer;
    private List<Note> notesSource;

    public NotesAdapter(List<Note> notes, NotesListeners notesListeners) {
        this.notes = notes;
        this.notesListeners = notesListeners;
        notesSource = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_note, parent, false)
        );
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder,  int position) {
        holder.setNote(notes.get(position));
        holder.layoutNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notesListeners.onNoteClicked(notes.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static  class  NoteViewHolder extends RecyclerView.ViewHolder{
        TextView txtTile , txtSubtitle , txtDateTime;
        LinearLayout layoutNote;
        ImageView imageNote;

        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTile = itemView.findViewById(R.id.txtTitle);
            txtSubtitle = itemView.findViewById(R.id.txtSubTitle);
            txtDateTime = itemView.findViewById(R.id.txtDateTime);
            layoutNote  = itemView.findViewById(R.id.layoutNote);
            imageNote = itemView.findViewById(R.id.imageNote);
        }
        void  setNote(Note note){
            txtTile.setText(note.getTitle());
            txtSubtitle.setText(note.getSubtitle());
            txtDateTime.setText(note.getDataTime());
            GradientDrawable gradientDrawable= (GradientDrawable) layoutNote.getBackground();
            if (note.getColor()!= null){
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
            }else {
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }
            if (note.getImagePath() !=null){
                imageNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
                imageNote.setVisibility(View.VISIBLE);
            }else {
                imageNote.setVisibility(View.GONE);
            }
        }
    }
    public void searchNote(final  String searchKeyWord){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (searchKeyWord.trim().isEmpty()){
                    notes = notesSource;
                }else {
                    ArrayList<Note> temp= new ArrayList<>();
                    for (Note note: notesSource){
                        if (note.getTitle().toLowerCase().contains(searchKeyWord.toLowerCase())
                            ||note.getSubtitle().toLowerCase().contains(searchKeyWord.toLowerCase())
                            ||note.getNoteText().toLowerCase().contains(searchKeyWord.toLowerCase()) ){
                            temp.add(note);
                        }
                    }
                    notes = temp;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        },500);
    }
    public void cancelTimer(){
        if (timer !=null){
            timer.cancel();
        }
    }
}
