package edu.uncc.hw06;

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;

import edu.uncc.hw06.databinding.CommentRowItemBinding;
import edu.uncc.hw06.databinding.FragmentForumBinding;
import edu.uncc.hw06.models.Comment;
import edu.uncc.hw06.models.Forum;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ForumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForumFragment extends Fragment
{


    private ArrayList<Comment> mComments = new ArrayList<>();
    private static final String ARG_PARAM1 = "forum1";
    public static final String TAG = "hw06";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private Forum mForum;
    private ForumCommentAdapter adapter;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ForumFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param forum Parameter 1.
     * @return A new instance of fragment ForumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ForumFragment newInstance(Forum forum)
    {
        ForumFragment fragment = new ForumFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, forum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mForum = (Forum) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    FragmentForumBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentForumBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Forum");

        binding.textViewForumTitle.setText(mForum.getTitle());
        binding.textViewForumText.setText(mForum.getContent());
        binding.textViewForumCreatedBy.setText(mForum.getAuthor());
        adapter = new ForumCommentAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(adapter);

        binding.buttonSubmitComment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String content = binding.editTextComment.getText().toString();
                if (content.isEmpty())
                {
                    Toast.makeText(getActivity(), "Please enter a comment.", Toast.LENGTH_SHORT).show();
                    return;
                }

                DocumentReference docRef = db.collection("comments").document(mForum.getDocID());
                HashMap<String, Object> data = new HashMap<>();
                data.put("content", content);
                data.put("postID", mForum.getDocID());
                data.put("docID", docRef.getId());
                data.put("ownerID", mAuth.getCurrentUser().getUid());
                data.put("timestamp", FieldValue.serverTimestamp());

                docRef.set(data).addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void unused)
                    {
                        Log.d(TAG, "onSuccess: add comment");
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Log.d(TAG, "onFailure: add comment failed:" + e.getMessage());
                    }
                });

            }
        });

        db.collection("comments").addSnapshotListener(new EventListener<QuerySnapshot>()
        {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error)
            {
                mComments.clear();
                if (error != null)
                {
                    Log.d(TAG, "onEvent: error: " + error.getMessage());
                    return;
                }

                for (DocumentSnapshot doc: value)
                {
                    Comment comment = doc.toObject(Comment.class);
                    mComments.add(comment);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    public class ForumCommentAdapter extends RecyclerView.Adapter<ForumCommentAdapter.ForumCommentViewHolder>
    {
        @NonNull
        @Override
        public ForumCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            CommentRowItemBinding commentRowItemBinding = CommentRowItemBinding.inflate(getLayoutInflater(), parent, false);
            return new ForumCommentViewHolder(commentRowItemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull ForumCommentViewHolder holder, int position)
        {
            Comment comment = mComments.get(position);
            holder.setupUI(comment);
        }

        @Override
        public int getItemCount()
        {
            return mComments.size();
        }

        public class ForumCommentViewHolder extends RecyclerView.ViewHolder
        {
            CommentRowItemBinding binding;
            Comment mComment;

            public ForumCommentViewHolder(@NonNull CommentRowItemBinding binding)
            {
                super(binding.getRoot());
                this.binding = binding;
            }

            public void setupUI(Comment comment)
            {
                this.mComment = comment;
                binding.textViewCommentText.setText(comment.getContent());
                binding.textViewCommentCreatedAt.setText(comment.getCommentTime());
                binding.textViewCommentCreatedBy.setText(comment.getAuthor());


            }
        }
    }
}