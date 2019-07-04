package prateek.artmetio.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Collections;
import java.util.List;

import prateek.artmetio.R;
import prateek.artmetio.fragment.RecyclerItemClickListener;
import prateek.artmetio.models.NavDrawerItem;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    private List<NavDrawerItem> data = Collections.emptyList();
    private Context context;
    private int[] disableItems= new int[]{1,2,3,4,6,8}; //For disable data for staff items
    private int userType=0;
    private RecyclerItemClickListener listener;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data, RecyclerItemClickListener listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.nav_drawer_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        NavDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());

/*
        if (position%2==0) {

            holder.title.setBackgroundColor(ContextCompat.getColor(context,R.color.view_color));

        } else{
            holder.title.setBackgroundColor(ContextCompat.getColor(context,R.color.black));
        }
*/

        holder.title.setCompoundDrawablesWithIntrinsicBounds(data.get(position).getIcon(), 0, 0, 0);

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { listener.onRecyclerItemClickListener(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }



    public void onRefreshData(int userType){
        this.userType=userType;
        notifyDataSetChanged();
    }
}
