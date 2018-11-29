package br.edu.ulbra.election.candidate.service;

import br.edu.ulbra.election.candidate.output.v1.ElectionOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value="election-service", url="http://localhost:8085")
public interface ElectionClient {
    @GetMapping("/v1/election/{electionId}")
    ElectionOutput getById(@PathVariable(name = "electionId") Long
                                electionId);
}
