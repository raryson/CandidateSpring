package br.edu.ulbra.election.candidate.api.v1;

import br.edu.ulbra.election.candidate.input.v1.CandidateInput;
import br.edu.ulbra.election.candidate.model.Candidate;
import br.edu.ulbra.election.candidate.output.v1.CandidateOutput;
import br.edu.ulbra.election.candidate.output.v1.GenericOutput;
import br.edu.ulbra.election.candidate.service.CandidateService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/candidate")
public class CandidateApi {

    private final CandidateService _candidateService;

    @Autowired
    public CandidateApi(CandidateService candidateService){
        _candidateService = candidateService;
    }

    @GetMapping("/")
    @ApiOperation(value = "Get candidates List")
    public List<CandidateOutput> getAll(){
        return _candidateService.getAll() ;
    }

    @GetMapping("/{candidateId}")
    @ApiOperation(value = "Get candidate by Id")
    public CandidateOutput getById(@PathVariable Long candidateId){
        return _candidateService.getById(candidateId);
    }

    @PostMapping("/")
    @ApiOperation(value = "Create new candidate")
    public CandidateOutput create(@RequestBody CandidateInput candidateInput){
        return _candidateService.create(candidateInput);
    }

    @PutMapping("/{candidateId}")
    @ApiOperation(value = "Update candidate")
    public CandidateOutput update(@PathVariable Long candidateId, @RequestBody CandidateInput candidateInput){
        return _candidateService.update(candidateId, candidateInput);
    }

    @DeleteMapping("/{candidateId}")
    @ApiOperation(value = "Delete candidate")
    public GenericOutput delete(@PathVariable Long candidateId){
        return _candidateService.delete(candidateId);
    }
}
