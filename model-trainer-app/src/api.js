const host = "http://localhost:8080";

const api = {
  speechModels: function() {
    return host + "/speech-models";
  },
  speechModel: function(id) {
    return host + "/speech-model/" + id;
  },
  adaptationStart(modelId) {
    return host + "/speech-models/" + modelId + "/adaptations";
  },
  adaptation(id) {
    return host + "/adaptations/" + id;
  },
  storeTranscriptionRecording(adaptationId, transcriptionId) {
    return (
      host +
      "/adaptations/" +
      adaptationId +
      "/transcriptions/" +
      transcriptionId
    );
  },
  getFile(path) {
    return host + path;
  }
};

export default api;
