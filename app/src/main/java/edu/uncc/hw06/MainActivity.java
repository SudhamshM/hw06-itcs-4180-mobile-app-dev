package edu.uncc.hw06;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import edu.uncc.hw06.models.Forum;

public class MainActivity extends AppCompatActivity
        implements LoginFragment.LoginListener,
        SignUpFragment.SignUpListener,
        ForumsFragment.ForumsListener,
        CreateForumFragment.CreateForumListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // check if user is not logged in, then show login page
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new LoginFragment())
                    .commit();
        }
        // if user is logged in, then show forums page
        else
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new ForumsFragment())
                    .commit();
        }
    }

    @Override
    public void createNewAccount()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new SignUpFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void login()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void authSuccessful()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new ForumsFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void createNewForum()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new CreateForumFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void logout()
    {
        FirebaseAuth.getInstance().signOut();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToForum(Forum forum)
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, ForumFragment.newInstance(forum))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void cancelCreateForum()
    {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void doneCreateForum()
    {
        getSupportFragmentManager().popBackStack();
    }
}