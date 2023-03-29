package edu.uncc.hw06.models;

import com.google.firebase.Timestamp;

import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;
import java.util.ArrayList;

public class Forum implements Serializable
{
    String title, author, content, likeCount, commentCount, ownerID, docID;
    Timestamp timestamp;

    ArrayList<String> likedByUsersList = new ArrayList<>();

    public ArrayList<String> getLikedByUsersList()
    {
        return likedByUsersList;
    }

    public void setLikedByUsersList(ArrayList<String> likedByUsersList)
    {
        this.likedByUsersList = likedByUsersList;
    }

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

    public String getGoodTime()
    {
        return this.timestamp.toDate().toLocaleString();
    }

    public void setTimestamp(Timestamp timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getDocID()
    {
        return docID;
    }

    public void setDocID(String docID)
    {
        this.docID = docID;
    }



    @Override
    public String toString()
    {
        return "Forum{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", likeCount='" + likeCount + '\'' +
                ", commentCount='" + commentCount + '\'' +
                ", ownerID='" + ownerID + '\'' +
                ", docID='" + docID + '\'' +
                ", timestamp=" + timestamp +
                ", likedByUsersList=" + likedByUsersList +
                '}';
    }
}
