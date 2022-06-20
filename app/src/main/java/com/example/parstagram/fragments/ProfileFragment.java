package com.example.parstagram.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.parstagram.Post;
import com.example.parstagram.R;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";
    private ProfileAdapter adapter;
    private List<Post> allPosts;
    RecyclerView rvPosts;
    private SwipeRefreshLayout swipeContainer;
    private ImageView ivProfilePicture;
    private TextView tvUsername;
    ParseUser user;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeContainer = view.findViewById(R.id.swipeContainer2);
        swipeContainer.setOnRefreshListener(() -> {
            // Your code to refresh the list here.
            // Make sure you call swipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
            adapter.clear();
            queryPosts();
            swipeContainer.setRefreshing(false);
        });

        Bundle bundle = getArguments();
        if(bundle != null)
        {
            Post post = bundle.getParcelable(Post.class.getSimpleName());
            user = post.getUser();
        }
        else
        {
            user = ParseUser.getCurrentUser();
        }

        ivProfilePicture = view.findViewById(R.id.ivProfileAvatar);
        ParseFile image = user.getParseFile("profilePhoto");
        if(image != null) {
            Glide.with(ProfileFragment.this).load(image.getUrl()).circleCrop().into(ivProfilePicture);
        } else {
            ivProfilePicture.setImageBitmap(null);
        }
        tvUsername = view.findViewById(R.id.tvUsername3);
        tvUsername.setText(user.getUsername());

        rvPosts = view.findViewById(R.id.rvPosts2);

        allPosts = new ArrayList<>();
        adapter = new ProfileAdapter(getContext(), allPosts);

        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new GridLayoutManager(getContext(), 3));

        queryPosts();
    }

    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, user);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);

        query.findInBackground((posts, e) -> {
            if (e != null)
            {
                Log.e(TAG, "Issue with getting posts", e);
            }
            else
            {
                for (Post post : posts)
                {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername() + ", created at: " + post.getCreatedAt());
                }
                adapter.addAll(posts);
            }
        });
    }
}