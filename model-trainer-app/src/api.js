const host = "http://localhost:8080";

const api = {
  speechModels: function() {
    return host + "/speech-models";
  },
  speechModel: function(id) {
    return host + "/speech-models/" + id;
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
  getResultFromAdaptedModel(adaptationId) {
    return host + "/adaptations/" + adaptationId + "/result";
  },
  getFile(path) {
    return host + path;
  },
  processAdaptation(id) {
    return host + "/adaptations/" + id + "/processed-model";
  }
};

export default api;
