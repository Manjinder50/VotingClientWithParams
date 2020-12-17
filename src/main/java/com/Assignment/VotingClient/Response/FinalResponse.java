package com.Assignment.VotingClient.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
public class FinalResponse implements Serializable {

    List<VoteCountResponse> finalResponse;
}
