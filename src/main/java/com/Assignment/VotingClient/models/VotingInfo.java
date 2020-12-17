package com.Assignment.VotingClient.models;

import lombok.*;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode
@Builder
public class VotingInfo implements Serializable {
    private String voterName;
    private String boothName;
    private String party;
}
