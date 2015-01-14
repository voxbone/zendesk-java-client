package org.zendesk.client.v2.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

/**
 * @author stephenc
 * @since 04/04/2013 14:25
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImportTicket extends Ticket {

    private Date solvedAt;
    protected List<Comment> comments;

    public ImportTicket() {
    }

    public ImportTicket(Requester requester, String subject, List<Comment> comments) {
        this.subject = subject;
        this.requester = requester;
        this.comments = comments;
    }

    public ImportTicket(long requesterId, String subject, List<Comment> comments) {
        this.subject = subject;
        this.requesterId = requesterId;
        this.comments = comments;
    }

    @JsonProperty("solved_at")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'", timezone="UTC")
    public Date getSolvedAt() {
        return solvedAt;
    }

    public void setSolvedAt(Date solvedAt) {
        this.solvedAt = solvedAt;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

}
