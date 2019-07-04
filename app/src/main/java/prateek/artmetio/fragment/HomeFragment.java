package prateek.artmetio.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import prateek.artmetio.utils.CommonUtils;
import prateek.artmetio.utils.Constants;
import prateek.artmetio.R;
import prateek.artmetio.adapter.HomeAdapter;
import prateek.artmetio.models.Upload;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private View view;

    //recyclerview object
    private RecyclerView recyclerView;

    //adapter object
    private HomeAdapter adapter;

    //database reference
    private DatabaseReference mDatabase;

    //progress dialog
    private ProgressDialog progressDialog;

    //list to hold all the uploaded images
    private List<Upload> uploads;
    private Context context;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getViewId(view);
        return view;
    }

    private void getViewId(View view) {

        if(CommonUtils.isOnline(context))
        {
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            adapter= new HomeAdapter(context,uploads);

            progressDialog = new ProgressDialog(context);

            uploads = new ArrayList<>();

            //displaying progress dialog while fetching images
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

            //adding an event listener to fetch values
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    //dismissing the progress dialog
                    progressDialog.dismiss();

                    //iterating through all the values in database
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Upload upload = postSnapshot.getValue(Upload.class);
                        uploads.add(upload);
                    }
                    //creating adapter
                    adapter = new HomeAdapter(context, uploads);

                    //adding adapter to recyclerview
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    progressDialog.dismiss();
                }
            });

        }
        else
        {
            CommonUtils.showToast(context,context.getString(R.string.please_check));
        }
    }

}
