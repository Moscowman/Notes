package ru.varasoft.notes.data;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;

public class NoteDataMapping {
    public static class Fields{
        public final static String TITLE = "title";
        public final static String TEXT = "text";
        public final static String CREATOR = "creator";
        public final static String CREATION_DATE_TIME = "creation_date_time";
        public final static String MODIFICATION_DATE_TIME = "modification_date_time";
    }

    public static Note toNote(String id, Map<String, Object> doc) {
        Timestamp creationDateTimeStamp = (Timestamp)doc.get(Fields.CREATION_DATE_TIME);
        Timestamp modificationDateTimeStamp = (Timestamp)doc.get(Fields.MODIFICATION_DATE_TIME);
        Note answer = new Note((String) doc.get(Fields.TITLE),
                (String) doc.get(Fields.TEXT),
                (String) doc.get(Fields.CREATOR),
                creationDateTimeStamp.toDate(),
                modificationDateTimeStamp.toDate());
        answer.setId(id);
        return answer;
    }

    public static Map<String, Object> toDocument(Note note){
        Map<String, Object> answer = new HashMap<>();
        answer.put(Fields.TITLE, note.getTitle());
        answer.put(Fields.TEXT, note.getText());
        answer.put(Fields.CREATOR, note.getCreator());
        answer.put(Fields.CREATION_DATE_TIME, note.getCreationDateTime());
        answer.put(Fields.MODIFICATION_DATE_TIME, note.getModificationDateTime());
        return answer;
    }
}
