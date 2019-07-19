package com.subtilitas.doctalk.adapter;

import com.subtilitas.doctalk.adapter.model.Adaptation;
import com.subtilitas.doctalk.adapter.service.AdaptationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdapterController {

    private final AdaptationService adaptationService;

    @ResponseBody
    @GetMapping("/adaptation/{id}")
    public Adaptation getAdaptation(@PathVariable Long id) {
        return adaptationService.getAdaptation(id);
    }

    @ResponseBody
    @PostMapping("/adaptation")
    public Adaptation startNewAdaptation(@RequestBody String text) {
        return adaptationService.startAdaptation(text);
    }



}
