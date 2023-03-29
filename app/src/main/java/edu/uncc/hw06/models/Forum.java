package edu.uncc.hw06.models;

import com.google.firebase.Timestamp;

public class Forum
{
    String title, author, content, likeCount, commentCount, ownerID;
    Timestamp timestamp;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getLikeCount()
    {
        return likeCount;
    }

    public void setLikeCount(String likeCount)
    {
        this.likeCount = likeCount;
    }

    public String getCommentCount()
    {
        return commentCount;
    }

    public void setCommentCount(String commentCount)
    {
        this.commentCount = commentCount;
    }

    public String getOwnerID()
    {
        return ownerID;
    }

    public void setOwnerID(String ownerID)
    {
        this.ownerID = ownerID;
    }

    public Forum()
    {
    }

    public Timestamp getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp)
    {
        this.timestamp = timestamp;
    }
}
