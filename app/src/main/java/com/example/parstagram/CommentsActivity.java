package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    private final String TAG = "CommentsActivity";
    private CommentsAdapter adapter;
    private List<Comment> allComments;
    private RecyclerView rvComments;
    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        rvComments = findViewById(R.id.rvComments);

        allComments = new ArrayList<>();
        adapter = new CommentsAdapter(this, allComments);

        rvComments.setAdapter(adapter);
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        queryPosts();
    }

    private void queryPosts() {
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        //query.include(Post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder("createdAt");

        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> comments, ParseException e) {
                if (e != null)
                {
                    Log.e(TAG, "Issue with getting posts", e);
                }
                else
                {
                    allComments.addAll(comments);
                    adapter.notifyDataSetChanged(); // modify to make more accurate
                }
            }
        });
    }
}