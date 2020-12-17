package com.Assignment.VotingClient.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class VoteCountResponse implements Serializable {

    @JsonProperty("party_name")
    private String partyName;

    @JsonProperty("no_votes")
    private int noOfVotes;
}
