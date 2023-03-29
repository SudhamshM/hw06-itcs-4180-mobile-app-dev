package edu.uncc.hw06;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import edu.uncc.hw06.databinding.FragmentCreateForumBinding;

public class CreateForumFragment extends Fragment
{
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String TAG = "hw06";
    public CreateForumFragment()
    {
        // Required empty public constructor
    }

    FragmentCreateForumBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentCreateForumBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Create Forum");

        binding.buttonCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mListener.cancelCreateForum();
            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String title = binding.editTextForumTitle.getText().toString();
                String desc = binding.editTextForumDesc.getText().toString();
                if (title.isEmpty())
                {
                    Toast.makeText(getActivity(), "Title cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (desc.isEmpty())
                {
                    Toast.makeText(getActivity(), "Description cannot be empty", Toast.LENGTH_SHORT).show();
                } else
                {
                    DocumentReference docRef = db.collection("forums").document();
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("title", title);
                    data.put("content", desc);
                    data.put("author", mAuth.getCurrentUser().getDisplayName());
                    data.put("ownerID", mAuth.getCurrentUser().getUid());
                    data.put("likeCount", "0");
                    data.put("commentCount", "0");
                    data.put("timestamp", FieldValue.serverTimestamp());
                    data.put("docID", docRef.getId());
                    docRef.set(data).addOnSuccessListener(new OnSuccessListener<Void>()
                    {
                        @Override
                        public void onSuccess(Void unused)
                        {
                            Log.d(TAG, "onSuccess: added forum");
                            mListener.doneCreateForum();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener()
                            {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    Log.d(TAG, "onFailure: " + e.getMessage());
                                }
                            });
                }
            }
        });

    }

    CreateForumListener mListener;

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        mListener = (CreateForumListener) context;
    }

    interface CreateForumListener
    {
        void cancelCreateForum();

        void doneCreateForum();
    }

}