package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        chatList = findViewById(R.id.chatList);
        chatText = findViewById(R.id.chatText);
        sendButton = findViewById(R.id.sendButton);
        chatMessages = new ArrayList<String>();

        ChatAdapter messageAdapter = new ChatAdapter( this );
        chatList.setAdapter (messageAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatMessages.add(chatText.getText().toString());
                messageAdapter.notifyDataSetChanged();
                chatText.setText("");
            }
        });
    }
    private class ChatAdapter extends ArrayAdapter<String> {
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

            if(position%2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }

            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(getItem(position)); // get the string at position
            return result;
        }
    }

}