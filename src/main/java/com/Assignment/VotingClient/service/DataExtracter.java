package com.Assignment.VotingClient.service;

import com.Assignment.VotingClient.Response.FinalResponse;
import com.Assignment.VotingClient.Response.VoteCountResponse;
import com.Assignment.VotingClient.config.MessagingConfig;
import com.Assignment.VotingClient.models.VotingInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataExtracter {

    final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void extractData(String path){

        //Read the input file
        Resource resource = resourceLoader.getResource(path);

        try(InputStream inputStream = resource.getInputStream()) {
            byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
            String data = new String(bdata, StandardCharsets.UTF_8);
            LOGGER.info(data);
            //Map the input data to VotingInfo class
            Set<VotingInfo> votingInfoSet = mapToVotingInfo(data);
            //publish data to queue
            publishToQueue(votingInfoSet);
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        }
    }

    public Set<VotingInfo> mapToVotingInfo(String data) {
        Set<VotingInfo> votingInfoSet = new HashSet<>();
        String lines[] = data.split("\\r?\\n");
        Arrays.stream(lines).spliterator().forEachRemaining(line -> {
            String[] a = line.split(";");
            VotingInfo votingInfo = VotingInfo.builder()
                    .voterName(a[0])
                    .boothName(a[1])
                    .party(a[2])
                    .build();

            votingInfoSet.add(votingInfo);

        });
        return votingInfoSet;
    }

    public void publishToQueue(Set<VotingInfo> votingInfoSet) {

        Map<String, Integer> countMap = votingInfoSet.stream()
                .collect(Collectors.groupingBy(VotingInfo::getParty, Collectors.summingInt(e -> 1)));

        //Create multiple objects of VoteCountResponse for each entry of the countMap and then add those objects
        //to the list
        List<VoteCountResponse> finalList = countMap.entrySet()
                .stream()
                .map(entry -> new VoteCountResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        FinalResponse finalResponse = FinalResponse.builder()
                .finalResponse(finalList)
                .build();

        rabbitTemplate.convertAndSend(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY, finalResponse);
    }
}
