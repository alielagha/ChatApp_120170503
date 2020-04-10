package com.example.chatapp_120170503.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp_120170503.R;
import com.example.chatapp_120170503.adapters.MessagesAdapter;
import com.example.chatapp_120170503.helpers.PaginationScrollListener;
import com.example.chatapp_120170503.models.Message;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ImageButton sendBtn;
    private EditText editText;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference parentReference;
    private DatabaseReference chatsReference;
    private RecyclerView messagesRecyclerView;
    private MessagesAdapter messagesAdapter;
    private List<Message> allMessages = new ArrayList<>();
    private List<Message> someMessages = new ArrayList<>();
    private String senderId = "Hk557rxXihj57Xioj";
    private String senderName = "Ali Alagha";
    private String receiverId;
    private String receiverName;
    int startIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if (getIntent() != null) {
            Intent intent = getIntent();
            receiverId = intent.getStringExtra("receiverId");
        }

        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
        usersRecyclerViewSetup();

        sendBtn = findViewById(R.id.imageButton);
        editText = findViewById(R.id.editText);

        firebaseDatabase = FirebaseDatabase.getInstance();
        parentReference = firebaseDatabase.getReference();
        chatsReference = parentReference.child("chats");

        readAllMessages(senderId, receiverId);

        listenToNewReceivedMessage();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                Message message = new Message(text, senderId, receiverId, senderName, receiverName);
                sendMessage(message);

            }
        });

    }

    private void usersRecyclerViewSetup() {

        messagesAdapter = new MessagesAdapter(getApplicationContext(), someMessages);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        messagesRecyclerView.setLayoutManager(linearLayoutManager);
        messagesRecyclerView.setAdapter(messagesAdapter);

        messagesRecyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {

                if ((startIndex + 10) < allMessages.size()) {
                    loadOnlyTenMessages(startIndex, startIndex + 10);
                }

            }

            @Override
            public int getTotalPageCount() {
                return allMessages.size();
            }

            @Override
            public boolean isLastPage() {
                return false;
            }

            @Override
            public boolean isLoading() {
                return false;
            }
        });

    }

    public void readAllMessages(final String senderId, final String receiverId) {

        chatsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Message message = snapshot.getValue(Message.class);

                    if (message.getSenderId().equals(senderId) &&
                            message.getReceiverId().equals(receiverId)
                            || message.getSenderId().equals(receiverId) &&
                            message.getReceiverId().equals(senderId))

                        allMessages.add(message);

                }

                if ((startIndex + 10) < allMessages.size()) {
                    loadOnlyTenMessages(0,  10);
                }

                messagesAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void loadOnlyTenMessages(int startIndex, int toIndex) {

        for (int i = startIndex; i < toIndex; i++) {

            someMessages.add(allMessages.get(i));

            messagesAdapter.notifyDataSetChanged();

        }

        this.startIndex = this.startIndex + 10;

    }

    private void listenToNewReceivedMessage() {

        Query lastQuery = chatsReference.orderByKey().limitToLast(1);

        lastQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    someMessages.add(message);
                }

                messagesAdapter.notifyItemInserted(someMessages.size() - 1);

                messagesRecyclerView.scrollToPosition(someMessages.size() - 1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public boolean sendMessage(Message message) {
        boolean status = false;

        Task task = chatsReference.push().setValue(message);
        if (task.isSuccessful()) {
            status = true;
        }

        return status;
    }

}
