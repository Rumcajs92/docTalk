const host = "http://localhost:8080";

const api = {
  speechModels: function() {
    return host + "/speech-models";
  },
  speechModel: function(id) {
    return host + "/speech-model/" + id;
  }
};

export default api;
