package br.edu.ulbra.election.candidate.service;

import br.edu.ulbra.election.candidate.exception.GenericOutputException;
import br.edu.ulbra.election.candidate.input.v1.CandidateInput;
import br.edu.ulbra.election.candidate.model.Candidate;
import br.edu.ulbra.election.candidate.output.v1.ElectionOutput;
import br.edu.ulbra.election.candidate.output.v1.GenericOutput;
import br.edu.ulbra.election.candidate.output.v1.CandidateOutput;
import br.edu.ulbra.election.candidate.output.v1.PartyOutput;
import br.edu.ulbra.election.candidate.repository.CandidateRepository;
import javassist.NotFoundException;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final ModelMapper modelMapper;
    private final PartyClientService partyClientService;
    private final ElectionClientService electionClientService;

    private static final String MESSAGE_INVALID_ID = "Invalid id";
    private static final String MESSAGE_CANDIDATE_NOT_FOUND = "Candidate not found";

    @Autowired
    public CandidateService(CandidateRepository candidateRepository, ModelMapper modelMapper,
                            PartyClientService partyClientService, ElectionClientService electionClientService){
        this.candidateRepository = candidateRepository;
        this.modelMapper = modelMapper;
        this.partyClientService = partyClientService;
        this.electionClientService = electionClientService;
    }

    public List<CandidateOutput> getAll(){
        Type candidateOutputListType = new TypeToken<List<CandidateOutput>>(){}.getType();
        return modelMapper.map(candidateRepository.findAll(), candidateOutputListType);
    }

    public CandidateOutput create(CandidateInput candidateInput) {
        validateInput(candidateInput, false);
        Candidate candidate = modelMapper.map(candidateInput, Candidate.class);
        candidate = candidateRepository.save(candidate);
        CandidateOutput candidateOutput = modelMapper.map(candidate, CandidateOutput.class);

        candidateOutput.setElectionOutput(electionClientService.getById(candidateInput.getElectionId()));
        candidateOutput.setPartyOutput(partyClientService.getById(candidateInput.getPartyId()));
        return candidateOutput;
    }

    public CandidateOutput getById(Long candidateId){
        if (candidateId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        if (candidate == null){
            throw new GenericOutputException(MESSAGE_CANDIDATE_NOT_FOUND);
        }

        return modelMapper.map(candidate, CandidateOutput.class);
    }

    public CandidateOutput update(Long candidateId, CandidateInput candidateInput) {
        if (candidateId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }
        validateInput(candidateInput, true);

        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        if (candidate == null){
            throw new GenericOutputException(MESSAGE_CANDIDATE_NOT_FOUND);
        }
        candidate.setPartyId(candidateInput.getPartyId());
        candidate.setElectionId(candidateInput.getElectionId());
        candidate.setName(candidateInput.getName());
        candidate = candidateRepository.save(candidate);
        return modelMapper.map(candidate, CandidateOutput.class);
    }

    public GenericOutput delete(Long candidateId) {
        if (candidateId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);
        if (candidate == null){
            throw new GenericOutputException(MESSAGE_CANDIDATE_NOT_FOUND);
        }

        candidateRepository.delete(candidate);

        return new GenericOutput("Candidate deleted");
    }

    private void validateInput(CandidateInput candidateInput, boolean isUpdate){
        try {
            PartyOutput party =  partyClientService.getById(candidateInput.getPartyId());
            String candidateNumberToString = candidateInput.getNumberElection().toString();
            String partyNumberToString = party.getNumber().toString();
            if(partyNumberToString.charAt(0) != candidateNumberToString.charAt(0)){
                throw new GenericOutputException("INVALID ELECTION NUMBER");
            }
            if(partyNumberToString.charAt(1) != candidateNumberToString.charAt(1)){
                throw new GenericOutputException("INVALID ELECTION NUMBER");
            }
        } catch(Exception ex) {
            throw new GenericOutputException("INVALID PARTY ID");
        }
        try {
            ElectionOutput election =  electionClientService.getById(candidateInput.getElectionId());
        } catch(Exception ex) {
            throw new GenericOutputException("INVALID ELECTION ID");
        }

        if (StringUtils.isBlank(candidateInput.getName())){
            throw new GenericOutputException("Invalid name");
        }

        if (candidateInput.getName().length() < 5) {
            throw new GenericOutputException("Invalid name");
        }

        if (candidateInput.getName().indexOf(" ") == -1) {
            throw new GenericOutputException("Invalid name");
        }

    }

}
