package prateek.artmetio.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import prateek.artmetio.previewinsta.ImagePreviewer;
import prateek.artmetio.R;
import prateek.artmetio.activity.ItemDetailsActivity;
import prateek.artmetio.models.Upload;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private Context context;
    private List<Upload> uploads;
    public Intent intent;

    public HomeAdapter(Context context, List<Upload> uploads) {
        this.uploads = uploads;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

            final Upload upload = uploads.get(position);

            holder.textViewName.setText(upload.getName());

            Picasso.with(context).load(upload.getUrl()).fit().centerCrop()
                    .placeholder(R.mipmap.pic)
                    .error(R.mipmap.pic)
                    .into(holder.imageView);
            //Glide.with(context).load(upload.getUrl()).into(holder.imageView);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent=new Intent(context, ItemDetailsActivity.class);
                    intent.putExtra("uploads", (Serializable) uploads);
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            });
            holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new ImagePreviewer().show(v.getContext(), (ImageView) holder.imageView);
                    return false;
                }
            });

    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewName;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}

