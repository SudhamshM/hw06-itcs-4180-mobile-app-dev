package edu.uncc.hw06;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import edu.uncc.hw06.databinding.ForumRowItemBinding;
import edu.uncc.hw06.databinding.FragmentForumsBinding;
import edu.uncc.hw06.models.Forum;

public class ForumsFragment extends Fragment
{
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String TAG = "hw06";
    private ArrayList<Forum> mForums = new ArrayList<>();
    private ForumsAdapter adapter;

    public ForumsFragment()
    {
        // Required empty public constructor
    }

    FragmentForumsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentForumsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Forums");
        binding.textViewWelcome.setText("Welcome, " + mAuth.getCurrentUser().getDisplayName());
        binding.buttonCreateForum.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mListener.createNewForum();
            }
        });

        binding.buttonLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mListener.logout();
            }
        });
        adapter = new ForumsAdapter();
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // get data from db with snapshot
        db.collection("forums").addSnapshotListener(new EventListener<QuerySnapshot>()
        {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error)
            {
                if (error != null)
                {
                    Log.d(TAG, "onEvent: " + error.getMessage());
                    return;
                }
                mForums.clear();
                for (DocumentSnapshot doc: value)
                {
                    Forum forum = doc.toObject(Forum.class);
                    mForums.add(forum);
                }
                adapter.notifyDataSetChanged();
            }
        });


    }

    public class ForumsAdapter extends RecyclerView.Adapter<ForumsAdapter.ForumsViewHolder>
    {

        @NonNull
        @Override
        public ForumsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            ForumRowItemBinding forumRowItemBinding =ForumRowItemBinding.inflate(getLayoutInflater(), parent, false);
            return new ForumsViewHolder(forumRowItemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull ForumsViewHolder holder, int position)
        {
            Forum forum = mForums.get(position);
            holder.setupUI(forum);
        }

        @Override
        public int getItemCount()
        {
            return mForums.size();
        }

        public class ForumsViewHolder extends RecyclerView.ViewHolder
        {
            ForumRowItemBinding mBinding;
            Forum mForum;

            public ForumsViewHolder(@NonNull ForumRowItemBinding rowBinding)
            {
                super(rowBinding.getRoot());
                this.mBinding = rowBinding;
            }

            public void setupUI(Forum forum)
            {
                this.mForum = forum;
                if (mForum.getOwnerID().equals(mAuth.getCurrentUser().getUid()))
                {
                    mBinding.imageViewDelete.setVisibility(View.VISIBLE);
                }
                else
                {
                    mBinding.imageViewDelete.setVisibility(View.INVISIBLE);
                }
                mBinding.textViewForumCreatedBy.setText(mForum.getAuthor());
                mBinding.textViewForumTitle.setText(mForum.getTitle());
                mBinding.textViewForumText.setText(mForum.getContent());
            }

            private void deleteForum()
            {
                db.collection("forums").document(mForum.getDocID()).delete().addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void unused)
                    {
                        Log.d(TAG, "onSuccess: deleted forum");
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Log.d(TAG, "onFailure: fail delete forum " + e.getMessage());
                    }
                });
            }
        }
    }



    ForumsListener mListener;

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        mListener = (ForumsListener) context;
    }

    interface ForumsListener
    {
        void createNewForum();

        void logout();
    }
}