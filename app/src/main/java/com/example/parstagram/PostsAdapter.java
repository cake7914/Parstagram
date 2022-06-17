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

import org.parceler.Parcels;

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
        holder.bind(post);
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
        private TextView tvCreationTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvUsername2 = itemView.findViewById(R.id.tvUsername2);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
            btnLike = itemView.findViewById(R.id.btnLike);
            tvCreationTime = itemView.findViewById(R.id.tvCreationTime);
        }

        public void bind(Post post) {
            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            tvUsername2.setText(post.getUser().getUsername());
            tvCreationTime.setText(Post.getRelativeTimeAgo(post.getCreatedAt().toString()));
            tvLikeCount.setText(post.getLikeCount() + " Likes");
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

            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(post.getLiked()) //unlike the post, decrease likes count
                    {
                        post.setLiked(false);
                        post.updateLikeCount(-1);
                        btnLike.setColorFilter(Color.BLACK);
                    } else { //like the post, increase the likes count
                        post.setLiked(true);
                        post.updateLikeCount(1);
                        btnLike.setColorFilter(Color.RED);
                    }
                    tvLikeCount.setText(post.getLikeCount() + " Likes");
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
}

