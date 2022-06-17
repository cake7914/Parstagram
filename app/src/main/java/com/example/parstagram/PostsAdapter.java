package com.example.parstagram;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        try {
            holder.bind(post);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvUsername2 = itemView.findViewById(R.id.tvUsername2);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnComment = itemView.findViewById(R.id.btnComment);
            tvCreationTime = itemView.findViewById(R.id.tvCreationTime);
            tvCommentCount = itemView.findViewById(R.id.tvCommentCount);
        }

        public void bind(Post post) throws JSONException {
            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            tvUsername2.setText(post.getUser().getUsername());
            tvCreationTime.setText(Post.getRelativeTimeAgo(post.getCreatedAt().toString()));
            if(post.getUsersLiked() != null) {
                tvLikeCount.setText((post.getUsersLiked()).length() + " Likes");
            }
            tvCommentCount.setText("View all "+ post.getCommentCount() + " comments");
            ParseFile image = post.getImage();
            if(image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            } else {
                ivImage.setImageBitmap(null);
            }

            ParseFile profilePic = post.getUser().getParseFile("profilePhoto");
            if(profilePic != null && ivProfilePic != null)
            {
                Glide.with(context).load(profilePic.getUrl()).circleCrop().into(ivProfilePic);
            }
            else if(ivProfilePic != null)
            {
                ivProfilePic.setImageBitmap(null);
            }

            int pos = checkUserLikedPost(ParseUser.getCurrentUser().getObjectId(), post.getUsersLiked());
            if(pos == -1) //user has not liked post
            {
                btnLike.setColorFilter(Color.BLACK);
            } else {
                btnLike.setColorFilter(Color.RED);
            }

            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int pos = checkUserLikedPost(ParseUser.getCurrentUser().getObjectId(), post.getUsersLiked());
                        if(pos != -1) //if user has already liked the post, unlike the post
                        {
                            post.unlikePost(pos);
                            btnLike.setColorFilter(Color.BLACK);
                        } else { //if user has not liked the post, then like the post
                            post.likePost(ParseUser.getCurrentUser());
                            btnLike.setColorFilter(Color.RED);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    tvLikeCount.setText((post.getUsersLiked()).length() + " Likes");
                    post.saveInBackground();
                }
            });


            btnComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, CommentsActivity.class);
                    i.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                    context.startActivity(i);
                }
            });


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, PostDetailsActivity.class);
                    i.putExtra(Post.class.getSimpleName(), Parcels.wrap(post));
                    context.startActivity(i);
                }
            });
        }
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

