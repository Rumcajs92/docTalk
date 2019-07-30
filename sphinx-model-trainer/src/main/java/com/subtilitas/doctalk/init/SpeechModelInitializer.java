package com.subtilitas.doctalk.init;

import com.subtilitas.doctalk.adapter.model.SpeechModel;
import com.subtilitas.doctalk.adapter.repository.SpeechModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpeechModelInitializer implements ApplicationRunner {

    private final SpeechModelRepository speechModelRepository;

    @Override
    public void run(ApplicationArguments args) {
        SpeechModel enUsModel= new SpeechModel();
        enUsModel.setName("en-us speech model");
        enUsModel.setDesc("basic english model available on cmusphinx");
        enUsModel.setPath("en-us");
        speechModelRepository.save(enUsModel);
        SpeechModel deModel= new SpeechModel();
        deModel.setName("de speech model");
        deModel.setDesc("basic german monel available on cmusphinx");
        deModel.setPath("de");
        speechModelRepository.save(deModel);
    }
}
