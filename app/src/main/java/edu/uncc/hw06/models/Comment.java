package edu.uncc.hw06.models;

import com.google.firebase.Timestamp;

public class Comment
{
    String author, content, ownerID, postID, docID;
    Timestamp timestamp;

    public Comment()
    {
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

    public Timestamp getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getCommentTime()
    {
        if (timestamp != null)
        {
            return timestamp.toDate().toLocaleString();
        }
        return "";
    }

    public String getOwnerID()
    {
        return ownerID;
    }

    public void setOwnerID(String ownerID)
    {
        this.ownerID = ownerID;
    }

    public String getPostID()
    {
        return postID;
    }

    public void setPostID(String postID)
    {
        this.postID = postID;
    }

    public String getDocID()
    {
        return docID;
    }

    public void setDocID(String docID)
    {
        this.docID = docID;
    }
}
