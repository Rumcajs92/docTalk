package com.subtilitas.doctalk.wikiextractor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.Tuple2;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
public class WikiExtractor {

    private final String hostWiki;
    private final ObjectMapper objectMapper;

    public List<String> extract(List<Tuple2<String, Integer>> wikiPagesSections) {
        List<HttpRequest> requests = buildRequests(wikiPagesSections);
        //TODO important call in async and return in the same order
        return requests.stream()
                .peek(log::debug)
                .map(this::sendRequest)
                .map(response-> readResponse(objectMapper, response))
                .map(this::getTextToParse)
                .map(Jsoup::parse)
                .map(Document::wholeText)
                .collect(Collectors.toList());
    }

    private String getTextToParse(JsonNode jsonNode) {
        return jsonNode
                .get("parse")
                    .get("text")
                        .get("*")
                .asText();
    }

    @SneakyThrows
    private JsonNode readResponse(ObjectMapper objectMapper, HttpResponse<String> response)  {
        return objectMapper.readTree(response.body());
    }

    @SneakyThrows
    private HttpResponse<String> sendRequest(HttpRequest httpRequest) {
        return HttpClient.newBuilder()
                .build()
                .send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    private List<HttpRequest> buildRequests(List<Tuple2<String, Integer>> wikiPages) {
        final URIBuilder uriBuilder = getUriBuilderTemplate();
        final HttpRequest.Builder requestBuilder =  HttpRequest.newBuilder().GET();
        return wikiPages.stream()
                .map(createURI(uriBuilder))
                .map(getRequest(requestBuilder))
                .collect(Collectors.toList());
    }

    private Function<URI, HttpRequest> getRequest(HttpRequest.Builder requestBuilder) {
        return wikiUri -> requestBuilder.uri(wikiUri)
                .build();
    }

    private Function<Tuple2<String, Integer>, URI> createURI(URIBuilder uriBuilder) {
        return wikiPageSection -> {
            try {
                return uriBuilder
                        .setParameter("page", wikiPageSection._1())
                        .setParameter("section", wikiPageSection._2().toString())
                        .build();
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(String.format("The page: '%s' is not a single wiki page", wikiPageSection));
            }
        };
    }

    private URIBuilder getUriBuilderTemplate() {
        return new URIBuilder()
                .setScheme("https")
                .setPath("w/api.php")
                .setHost(hostWiki)
                .addParameter("action","parse")
                .addParameter("format","json")
                .addParameter("prop","text")
                .addParameter("noimages","true")
                .addParameter("disabletoc","true")
                .addParameter("disableeditsection","true");
    }
}
