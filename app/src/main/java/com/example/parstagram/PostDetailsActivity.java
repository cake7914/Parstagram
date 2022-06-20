package com.example.parstagram;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

public class PostDetailsActivity extends AppCompatActivity {
    private TextView tvUsername;
    private TextView tvUsername2;
    private ImageView ivImage;
    private TextView tvDescription;
    private ImageView ivProfilePic;
    private TextView tvLikeCount;
    private ImageButton btnLike;
    private ImageButton btnComment;
    private TextView tvCreationTime;
    private TextView tvCommentCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_post);

        tvUsername = findViewById(R.id.tvUsername);
        tvUsername2 = findViewById(R.id.tvUsername2);
        ivImage = findViewById(R.id.ivImage);
        tvDescription = findViewById(R.id.tvDescription);
        ivProfilePic = findViewById(R.id.ivProfilePic);
        tvLikeCount = findViewById(R.id.tvLikeCount);
        btnLike = findViewById(R.id.btnLike);
        btnComment = findViewById(R.id.btnComment);
        tvCreationTime = findViewById(R.id.tvCreationTime);
        tvCommentCount = findViewById(R.id.tvCommentCount);

        Post post = Parcels.unwrap(getIntent().getParcelableExtra(Post.class.getSimpleName()));

        tvDescription.setText(post.getDescription());
        tvUsername.setText(post.getUser().getUsername());
        tvUsername2.setText(post.getUser().getUsername());
        tvCreationTime.setText(Post.getRelativeTimeAgo(post.getCreatedAt().toString()));

        if(post.getUsersLiked() != null) {
            tvLikeCount.setText(String.format(PostDetailsActivity.this.getString(R.string.likes), (post.getUsersLiked()).length()));
        }
        tvCommentCount.setText(String.format(PostDetailsActivity.this.getString(R.string.view_all), post.getCommentCount()));
        ParseFile image = post.getImage();
        if(image != null) {
            Glide.with(PostDetailsActivity.this).load(image.getUrl()).into(ivImage);
        } else {
            ivImage.setImageBitmap(null);
        }

        ParseFile profilePic = post.getUser().getParseFile("profilePhoto");
        if(profilePic != null && ivProfilePic != null)
        {
            Glide.with(PostDetailsActivity.this).load(profilePic.getUrl()).circleCrop().into(ivProfilePic);
        }
        else if(ivProfilePic != null)
        {
            ivProfilePic.setImageBitmap(null);
        }

        int pos = 0;
        try {
            pos = checkUserLikedPost(ParseUser.getCurrentUser().getObjectId(), post.getUsersLiked());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(pos == -1) //user has not liked post
        {
            btnLike.setColorFilter(Color.BLACK);
        } else {
            btnLike.setColorFilter(Color.RED);
        }

        btnLike.setOnClickListener(v -> {
            try {
                int pos1 = checkUserLikedPost(ParseUser.getCurrentUser().getObjectId(), post.getUsersLiked());
                if(pos1 != -1) //if user has already liked the post, unlike the post
                {
                    post.unlikePost(pos1);
                    btnLike.setColorFilter(Color.BLACK);
                } else { //if user has not liked the post, then like the post
                    post.likePost(ParseUser.getCurrentUser());
                    btnLike.setColorFilter(Color.RED);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            post.saveInBackground();
        });

        btnComment.setOnClickListener(v -> {
            Intent i = new Intent(PostDetailsActivity.this, CommentsActivity.class);
            i.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
            startActivity(i);
        });
    }

    public int checkUserLikedPost(String userID, JSONArray usersLiked) throws JSONException {
        if(usersLiked != null) {
            for (int i = 0; i < usersLiked.length(); i++) {
                if (usersLiked.getString(i).equals(userID)) {
                    return i;
                }
            }
        }
        return -1;
    }


}
