package com.example.androidassignments;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
    Boolean tabletCheck;
    Cursor cursor;
    Bundle fragmentBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        chatList = findViewById(R.id.chatList);
        chatText = findViewById(R.id.chatText);
        sendButton = findViewById(R.id.sendButton);

        chatMessages = new ArrayList<String>();
        chatMessages.clear();

        if (findViewById(R.id.chatFrame) != null) {
            tabletCheck = true;
        } else {
            tabletCheck = false;
        }

        final ChatAdapter messageAdapter = new ChatAdapter( this );
        chatList.setAdapter (messageAdapter);

        // Create ChatDatabaseHelper and get database
        ChatDatabaseHelper databaseHelper = new ChatDatabaseHelper(this);
        db = databaseHelper.getWritableDatabase();

        // Query existing messages
        cursor = db.rawQuery("SELECT * FROM " + ChatDatabaseHelper.TABLE_NAME, null);
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

        chatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentBundle.putLong("id", messageAdapter.getItemId(cursor.getPosition()));
                fragmentBundle.putString("message", messageAdapter.getItem(cursor.getPosition()));

                if (tabletCheck == true) {
                    MessageDetails.MessageFragment fragment = new MessageDetails.MessageFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    fragment.setArguments(fragmentBundle);
                    ft.addToBackStack(null);
                    ft.add(R.id.fragmentFrame, fragment);
                    ft.commit();

                } else {
                    Intent intent = new Intent(ChatWindow.this, MessageDetails.class);
                    intent.putExtras(fragmentBundle);
                    startActivityForResult(intent, 10);
                }
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

        public long getItemId(int position) {
            cursor.moveToPosition(position);
            return Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.KEY_ID)));
        }
    }

    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == fragmentBundle.getInt("id", 0)) {
            db.rawQuery("DELETE FROM " + ChatDatabaseHelper.TABLE_NAME + " WHERE " + ChatDatabaseHelper.KEY_ID + " = " + resultCode, null);
        }
    }


}