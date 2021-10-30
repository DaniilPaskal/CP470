package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "ChatWindow";

    ListView chatList;
    EditText chatText;
    Button sendButton;
    ArrayList<String> chatMessages;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        chatList = findViewById(R.id.chatList);
        chatText = findViewById(R.id.chatText);
        sendButton = findViewById(R.id.sendButton);

        chatMessages = new ArrayList<String>();
        chatMessages.clear();

        final ChatAdapter messageAdapter = new ChatAdapter( this );
        chatList.setAdapter (messageAdapter);

        // Create ChatDatabaseHelper and get database
        ChatDatabaseHelper databaseHelper = new ChatDatabaseHelper(this);
        db = databaseHelper.getWritableDatabase();

        // Query existing messages
        Cursor cursor = db.rawQuery("SELECT * FROM " + ChatDatabaseHelper.TABLE_NAME, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            chatMessages.add(cursor.getString(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.KEY_MESSAGE)));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.KEY_MESSAGE)));
            Log.i(ACTIVITY_NAME, "Cursor’s  column count =" + cursor.getColumnCount());
            cursor.moveToNext();
        }

        // Print column names
        for (int i = 0; i < cursor.getCount(); i++) {
            String column = cursor.getColumnName(0);
            System.out.println(column);
        }

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentMessage = chatText.getText().toString().trim();
                chatMessages.add(currentMessage);
                messageAdapter.notifyDataSetChanged();
                chatText.setText("");

                //Add message to database
                ContentValues values = new ContentValues();
                values.put(ChatDatabaseHelper.KEY_MESSAGE, currentMessage);
                db.insert(ChatDatabaseHelper.TABLE_NAME, null, values);
            }
        });
    }
    class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return chatMessages.size();
        }

        public String getItem(int position) {
            return chatMessages.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null ;
            TextView message;

            if(position%2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
                message = result.findViewById(R.id.message_text_in);
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
                message = result.findViewById(R.id.message_text_out);
            }

            message.setText(getItem(position)); // get the string at position
            return result;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

}