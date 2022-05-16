package com.example.app_notes.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.app_notes.entities.Note;

import java.util.List;

@Dao
public interface NotesDao  {
    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<Note> getAllNotes();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void  insertNote(Note note);
    @Delete
    void delete(Note note);

}
