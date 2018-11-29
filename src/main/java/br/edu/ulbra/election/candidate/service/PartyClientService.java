package br.edu.ulbra.election.candidate.service;

import br.edu.ulbra.election.candidate.output.v1.PartyOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartyClientService {
    private final PartyClient partyClient;
    @Autowired
    public PartyClientService(PartyClient partyClient){
        this.partyClient = partyClient;
    }
    public PartyOutput getById(Long id){
        return this.partyClient.getById(id);
    }
}
