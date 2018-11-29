package br.edu.ulbra.election.candidate.service;

import br.edu.ulbra.election.candidate.output.v1.ElectionOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElectionClientService {
    private final ElectionClient electionClient;
    @Autowired
    public ElectionClientService(ElectionClient electionClient){
        this.electionClient = electionClient;
    }
    public ElectionOutput getById(Long id){
        return this.electionClient.getById(id);
    }
}
