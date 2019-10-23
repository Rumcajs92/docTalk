package com.subtilitas.doctalk.adapter.service;

import com.subtilitas.doctalk.adapter.model.dto.AdaptationDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Objects;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AdaptationModelPathMatcher extends TypeSafeMatcher<AdaptationDTO> {

    private final String path;

    public static AdaptationModelPathMatcher hasPath(String path) {
        return new AdaptationModelPathMatcher(path);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(String.format("Asserting if AdaptationDTO has '%s' as path", path));
    }

    @Override
    protected void describeMismatchSafely(AdaptationDTO item, Description mismatchDescription) {
        mismatchDescription.appendText(String.format("AdaptationDTO has '%s' as path", item.getPath()));
    }

    @Override
    protected boolean matchesSafely(AdaptationDTO item) {
        return Objects.nonNull(item) && Objects.equals(item.getPath(), path);
    }
}
