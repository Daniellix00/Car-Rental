package com.crud.rental.service;

import com.crud.rental.domain.Option;
import com.crud.rental.exception.OptionNotFoundException;
import com.crud.rental.repository.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OptionService {
private final OptionRepository optionRepository;
public void addOption(final Option option){  optionRepository.save(option);}
public List<Option> getAllOptions(){return optionRepository.findAll();}
    public Option getOptionById(final Long optionId) throws OptionNotFoundException{
    return optionRepository.findById(optionId).orElseThrow(OptionNotFoundException::new);
}
public void deleteOption(final Long optionId) throws OptionNotFoundException{
    optionRepository.delete(optionRepository.findById(optionId).orElseThrow(OptionNotFoundException::new));
}

}
