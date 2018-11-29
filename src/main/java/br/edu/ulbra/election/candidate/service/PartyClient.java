package br.edu.ulbra.election.candidate.service;

import br.edu.ulbra.election.candidate.output.v1.PartyOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value="party-service", url="http://localhost:8083")
public interface PartyClient {
    @GetMapping("/v1/party/{partyId}")
    PartyOutput getById(@PathVariable(name = "partyId") Long
                                partyId);
}
